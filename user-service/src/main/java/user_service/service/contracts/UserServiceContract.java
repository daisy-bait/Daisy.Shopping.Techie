package user_service.service.contracts;

import user_service.rest.dto.UserToInCreateDTO;
import user_service.rest.dto.UserToInUpdateDTO;
import user_service.rest.dto.UserToOutCreateDTO;
import user_service.rest.dto.UserToOutInfoDTO;

public interface UserServiceContract {

    UserToOutCreateDTO createUser(UserToInCreateDTO userToInCreateRequest);

    void updateUser(String keycloakUserId, UserToInUpdateDTO userToInUpdateRequest);

    UserToOutInfoDTO getUserById(String userId);

    UserToOutInfoDTO getUserByUsername(String username);

    void changeStatus(String keycloakUserId);

    void assignRole(String keycloakUserId, String roleName);

}
