package dev.hero.projectmanager.validation;

import dev.hero.projectmanager.dto.request.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegisterRequest>
{
    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context)
    {
        if (request.password() == null) {
            return request.confirmPassword() == null;
        }

        return request.password().equals(request.confirmPassword());
    }
}
