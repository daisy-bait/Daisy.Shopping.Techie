package user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import user_service.client.KeycloakAuthClient;
import user_service.rest.dto.KeycloakResponseDTO;
import user_service.rest.dto.LoginRequestDTO;
import user_service.rest.dto.UserToOutInfoDTO;
import user_service.service.contracts.AuthServiceContract;
import user_service.service.contracts.UserServiceContract;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthServiceContract {

    private final KeycloakAuthClient keycloakAuthClient;

    private final UserServiceContract userService;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Override
    public KeycloakResponseDTO login(LoginRequestDTO loginData) {

        Map<String, String> authData = Map.of(
                "grant_type", "password",
                "client_id", clientId,
                "client_secret", clientSecret,
                "username", loginData.getUsername(),
                "password", loginData.getPassword(),
                "scope", "openid"
        );

        ResponseEntity<Map<String, Object>> loginResponse = keycloakAuthClient.login(authData);

        UserToOutInfoDTO userInfo = userService.getUserByUsername(loginData.getUsername());
        if  (Objects.isNull(userInfo)) {
            userInfo = userService.getUserByEmail(loginData.getUsername());
        }

        return new KeycloakResponseDTO(
                userInfo.getUserId(),
                loginResponse.getBody().get("access_token").toString()
        );
    }

}
