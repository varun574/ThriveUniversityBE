package com.example.springboot.thriveuniversitybackend.validators;

import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumConstraintValidator implements ConstraintValidator<EnumValue, String> {
    private List<String> acceptedValues;
    @Override
    public void initialize(EnumValue constraintAnnotation) {
        acceptedValues = Stream.of(constraintAnnotation.enumC().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null)
            return true;
        return acceptedValues.contains(value);
    }
}
