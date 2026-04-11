package user_service.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import user_service.rest.dto.UserToInCreateDTO;
import user_service.rest.dto.UserToOutCreateDTO;
import user_service.rest.dto.UserToOutInfoDTO;
import user_service.service.contracts.UserServiceContract;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserServiceContract {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public UserToOutCreateDTO createUser(UserToInCreateDTO userToInCreateRequest) {
        UserRepresentation userToKeycloak = new UserRepresentation();
        userToKeycloak.setUsername(userToInCreateRequest.getUsername());
        userToKeycloak.setEmail(userToInCreateRequest.getEmail());
        userToKeycloak.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userToInCreateRequest.getPassword());
        credential.setTemporary(false);
        userToKeycloak.setCredentials(List.of(credential));

        String locationHeader;
        try (Response httpResponse = keycloak.realm(realm).users().create(userToKeycloak)) {
            if (httpResponse.getStatus() != HttpStatus.CREATED.value()) {
                throw new RuntimeException("FAILED TO CREATE USER: " + httpResponse.getEntity());
            }

            locationHeader = httpResponse.getHeaderString(HttpHeaders.LOCATION);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return new UserToOutCreateDTO(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));
    }

    @Override
    public UserToOutInfoDTO getUserById(String userId) {
        UserRepresentation userFromKeycloak = keycloak.realm(realm)
                .users().get(userId).toRepresentation();

        extracted(userFromKeycloak);

        return new UserToOutInfoDTO(
                userFromKeycloak.getId(), userFromKeycloak.getUsername(), userFromKeycloak.getEmail()
        );
    }


    @Override
    public UserToOutInfoDTO getUserByUsername(String username) {
        UserRepresentation userFromKeycloak = keycloak.realm(realm)
                .users().searchByUsername(username, true).get(0);

        extracted(userFromKeycloak);

        return new UserToOutInfoDTO(
                userFromKeycloak.getId(), userFromKeycloak.getUsername(), userFromKeycloak.getEmail()
        );
    }

    private void extracted(UserRepresentation userFromKeycloak) {
        log.info("FOUND USER: {} {}", userFromKeycloak.getUsername(), userFromKeycloak.getEmail());
    }
}
