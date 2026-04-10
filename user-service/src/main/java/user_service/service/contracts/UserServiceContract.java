package user_service.service.contracts;

import user_service.rest.dto.UserToInCreateDTO;
import user_service.rest.dto.UserToOutCreateDTO;
import user_service.rest.dto.UserToOutInfoDTO;

public interface UserServiceContract {

    UserToOutCreateDTO createUser(UserToInCreateDTO userToInCreateRequest);

    UserToOutInfoDTO getUserById(String userId);

}
