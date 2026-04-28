package dev.hero.projectmanager.security;

import dev.hero.projectmanager.exception.AppException;
import dev.hero.projectmanager.model.User;
import dev.hero.projectmanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider
{
    private final UserRepository userRepository;

    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser()
    {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    public String getCurrentUsername()
    {
        assert SecurityContextHolder.getContext().getAuthentication() != null;
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
