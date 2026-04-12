package user_service.service.contracts;

import user_service.rest.dto.KeycloakResponseDTO;
import user_service.rest.dto.LoginRequestDTO;

public interface AuthServiceContract {

    KeycloakResponseDTO login(LoginRequestDTO loginData);

}
