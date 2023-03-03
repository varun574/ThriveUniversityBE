package com.example.springboot.thriveuniversitybackend.validators;

import com.example.springboot.thriveuniversitybackend.validators.annotations.FieldsValueMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldsValueMatchConstraintValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String compareField;
    private String targetField;
    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.compareField = constraintAnnotation.compareField();
        this.targetField = constraintAnnotation.targetField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object compareValue = new BeanWrapperImpl(value).getPropertyValue(compareField);
        Object targetValue = new BeanWrapperImpl(value).getPropertyValue(targetField);
        if(compareValue == null || targetValue == null)
            return false;
        return compareValue.equals(targetValue);
    }
}
