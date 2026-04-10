package user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import user_service.client.KeycloakAuthClient;
import user_service.rest.dto.LoginRequestDTO;
import user_service.rest.dto.LoginResponseDTO;
import user_service.service.contracts.AuthServiceContract;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthServiceContract {

    private final KeycloakAuthClient keycloakAuthClient;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginData) {

        Map<String, String> authData = Map.of(
                "grant_type", "password",
                "client_id", clientId,
                "client_secret", clientSecret,
                "username", loginData.getUsername(),
                "password", loginData.getPassword(),
                "scope", "openid"
        );

        Map<String, Object> loginResponse = keycloakAuthClient.login(authData);
        log.info("login response: {}", loginResponse);
        String status = loginResponse.toString();

        return null;
    }

}
