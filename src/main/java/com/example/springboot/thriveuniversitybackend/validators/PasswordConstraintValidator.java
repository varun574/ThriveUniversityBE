package com.example.springboot.thriveuniversitybackend.validators;

import com.example.springboot.thriveuniversitybackend.validators.annotations.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String messageTemplate = "";
        if(value == null || value.isEmpty()){
            messageTemplate = "Password cannot be empty";
        }
        else if(value.length()<8){
            messageTemplate = "Password must contain minimum of 8 characters";
        }
        else
            return true;
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
