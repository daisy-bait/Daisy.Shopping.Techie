package top.daisyflows.shoppingwithtechie.gatekeeper.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class KeycloakAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt token) {
        Map<String, Object> realmAccess = (Map<String, Object>) token.getClaims().get("realm_access");

        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");
        Collection<GrantedAuthority> userRoles = new ArrayList<>();
        roles.forEach(role -> {
            userRoles.add(new SimpleGrantedAuthority("ROLE_".concat(role)));
        });

        return userRoles;
    }
}
