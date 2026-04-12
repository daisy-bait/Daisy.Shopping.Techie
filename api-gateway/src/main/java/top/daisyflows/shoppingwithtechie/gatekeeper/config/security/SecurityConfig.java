package top.daisyflows.shoppingwithtechie.gatekeeper.config.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUrl;

    private final KeycloakAuthConverter keycloakAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disabled for STATELESS APIs
                .authorizeHttpRequests(req -> req.
                        requestMatchers(
                                "/eureka/**",
                                "/api/users/v0/users/auth/login"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/users/v0/users"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .bearerTokenResolver(cookieAccessTokenResolver())
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtToken -> {
                                    return new JwtAuthenticationToken(jwtToken, keycloakAuthConverter.convert(jwtToken));
                                })))
                .build();
    }

    private BearerTokenResolver cookieAccessTokenResolver() {
        return request -> {
            List<Cookie> cookies = Arrays.asList(request.getCookies());
            String token = cookies.stream()
                    .filter(cookie -> cookie.getName().equals("access_token"))
                    .map(Cookie::getValue)
                    .findAny().orElse(null);

            if (token == null) return null;

            try {
                JwtDecoders.fromIssuerLocation(issuerUrl).decode(token);
            } catch (Exception e) {
                return null;
            }

            return token;
        };
    }

}
