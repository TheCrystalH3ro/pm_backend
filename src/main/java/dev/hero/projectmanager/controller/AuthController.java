package dev.hero.projectmanager.controller;

import dev.hero.projectmanager.dto.request.LoginRequest;
import dev.hero.projectmanager.dto.request.RegisterRequest;
import dev.hero.projectmanager.dto.response.AuthResponse;
import dev.hero.projectmanager.dto.response.AuthResult;
import dev.hero.projectmanager.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Register, login and logout")
public class AuthController
{
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response)
    {
        AuthResult result = authService.register(request);
        addTokenCookie(response, result.token());
        return ResponseEntity.ok(new AuthResponse(result.username(), result.role(), result.hasOrganization()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response)
    {
        AuthResult result = authService.login(request);
        addTokenCookie(response, result.token());
        return ResponseEntity.ok(new AuthResponse(result.username(), result.role(), result.hasOrganization()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response)
    {
        Cookie cookie = new Cookie("jwt", null);

        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    private void addTokenCookie(HttpServletResponse response, String token)
    {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }
}
