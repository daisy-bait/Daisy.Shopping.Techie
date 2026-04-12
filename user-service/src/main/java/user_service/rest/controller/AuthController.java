package user_service.rest.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import user_service.rest.dto.KeycloakResponseDTO;
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginData, HttpServletResponse response) {

        KeycloakResponseDTO loginServerResponse = authService.login(loginData);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", loginServerResponse.getAccessToken())
                .httpOnly(true)
                .sameSite("Lax")
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        LoginResponseDTO loginResponse = new LoginResponseDTO(loginServerResponse.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie deleteAccessCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .sameSite("Lax")
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
