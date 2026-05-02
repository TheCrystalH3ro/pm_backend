package dev.hero.projectmanager.service;

import dev.hero.projectmanager.dto.request.LoginRequest;
import dev.hero.projectmanager.dto.request.RegisterRequest;
import dev.hero.projectmanager.dto.response.AuthResult;
import dev.hero.projectmanager.exception.AppException;
import dev.hero.projectmanager.model.OrgRole;
import dev.hero.projectmanager.model.User;
import dev.hero.projectmanager.repository.UserRepository;
import dev.hero.projectmanager.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResult register(RegisterRequest request)
    {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new AppException("Username already taken", HttpStatus.CONFLICT);
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AppException("Email already registered", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setOrgRole(OrgRole.MEMBER);
        userRepository.save(user);

        String token = jwtService.generateToken(request.username());

        return new AuthResult(token, user.getUsername(), user.getOrgRole().name(), false);
    }

    public AuthResult login(LoginRequest request)
    {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AppException("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(request.username());

        return new AuthResult(token, user.getUsername(), user.getOrgRole().name(), user.getOrganization() != null);
    }
}
