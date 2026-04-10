package user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "keycloak-auth", url = "${KEYCLOAK.SERVER_URL}")
public interface KeycloakAuthClient {

    @PostMapping(value = "/realms/${KEYCLOAK.REALM}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> login(Map<String, ?> loginData);

}
