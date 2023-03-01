package com.example.springboot.thriveuniversitybackend.validators;

import com.example.springboot.thriveuniversitybackend.validators.annotations.NullableNonEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullableNonEmptyConstraintValidator implements ConstraintValidator<NullableNonEmpty, String> {
    @Override
    public void initialize(NullableNonEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null)
            return true;
        return !value.isEmpty();
    }
}
