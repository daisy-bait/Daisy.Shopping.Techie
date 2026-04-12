package top.daisyflows.shoppingwithtechie.gatekeeper.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KeycloakAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public Collection<GrantedAuthority> convert(Jwt token) {
        Map<String, Object> realmAccess = (Map<String, Object>) token.getClaims().get("realm_access");

        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");
        Collection<GrantedAuthority> userRoles = new ArrayList<>();
        roles.stream().filter(role -> !role.equals("default-roles-".concat(realm.toLowerCase())))
                .forEach(role -> {
                    userRoles.add(new SimpleGrantedAuthority("ROLE_".concat(role)));
                });

        return userRoles;
    }
}
