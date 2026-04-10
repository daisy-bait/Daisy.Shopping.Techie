package user_service.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.rest.dto.LoginRequestDTO;
import user_service.rest.dto.LoginResponseDTO;
import user_service.service.contracts.AuthServiceContract;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/users/v0/users/auth")
public class AuthController {

    private final AuthServiceContract authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginData) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.login(loginData));
    }

}
