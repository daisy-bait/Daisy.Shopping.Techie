package user_service.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.rest.dto.UserToInCreateDTO;
import user_service.rest.dto.UserToInUpdateDTO;
import user_service.rest.dto.UserToOutCreateDTO;
import user_service.rest.dto.UserToOutInfoDTO;
import user_service.service.contracts.UserServiceContract;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/users/v0/users")
public class UserController {

    private final UserServiceContract userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<UserToOutCreateDTO> createUser(@RequestBody UserToInCreateDTO userToInRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userToInRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<UserToOutInfoDTO> getUserById(@RequestParam String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody UserToInUpdateDTO userToInUpdateRequest) {
        userService.updateUser(userId, userToInUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/change-status/{userId}")
    public ResponseEntity<Void> changeUserStatus(@PathVariable String userId) {
        userService.changeStatus(userId);
        return ResponseEntity.noContent().build();
    }

}