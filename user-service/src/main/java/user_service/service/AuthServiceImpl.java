package user_service.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import user_service.client.KeycloakAuthClient;
import user_service.rest.dto.KeycloakResponseDTO;
import user_service.rest.dto.LoginRequestDTO;
import user_service.service.contracts.AuthServiceContract;
import user_service.service.contracts.UserServiceContract;

import java.util.Map;

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

        ResponseEntity<Map<String, Object>> loginResponse = null;
        try {
            loginResponse = keycloakAuthClient.login(authData);
        } catch (FeignException feignException) {
            throw new IllegalArgumentException("Invalid Username or Password");
        }

        return new KeycloakResponseDTO(
                userService.getUserByUsername(loginData.getUsername()).getUserId(),
                loginResponse.getBody().get("access_token").toString()
        );
    }

}
