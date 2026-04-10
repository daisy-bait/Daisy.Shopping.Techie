package user_service.service.contracts;

import user_service.rest.dto.LoginRequestDTO;
import user_service.rest.dto.LoginResponseDTO;

public interface AuthServiceContract {

    LoginResponseDTO login(LoginRequestDTO loginData);

}
