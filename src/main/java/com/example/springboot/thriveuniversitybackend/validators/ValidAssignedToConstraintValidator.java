package com.example.springboot.thriveuniversitybackend.validators;

import com.example.springboot.thriveuniversitybackend.enums.AssignedToTypes;
import com.example.springboot.thriveuniversitybackend.enums.Department;
import com.example.springboot.thriveuniversitybackend.enums.Section;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import com.example.springboot.thriveuniversitybackend.validators.annotations.ValidAssignedTo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidAssignedToConstraintValidator implements ConstraintValidator<ValidAssignedTo, Assignment> {

    @Override
    public boolean isValid(Assignment assignment, ConstraintValidatorContext context) {
        List<String> assignedTo = assignment.getAssignedTo();
        String type = assignment.getAssignedToType();
        Set<String> departments = Arrays.stream(Department.class.getEnumConstants()).map(Enum::name).collect(Collectors.toUnmodifiableSet());
        Set<String> sections = Arrays.stream(Section.class.getEnumConstants()).map(Enum::name).collect(Collectors.toUnmodifiableSet());
        for (String value: assignedTo
             ) {

            if(type.equals(AssignedToTypes.STUDENT.name())){
                Pattern pattern = Pattern.compile("^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$");
                Matcher matcher = pattern.matcher(value);
                boolean matchFound = matcher.find();
                if(!matchFound) {
                    buildConstraintValidation(context, value, " is not a valid roll number in assignedTo list.");
                    return false;
                };
            } else if (type.equals(AssignedToTypes.CLASS.name())) {
                if(!value.contains("-")){
                    buildConstraintValidation(context, value, " must contain - in the class name");
                    return false;
                }
                String[] splitString= value.split("-");
                String department = splitString[0], section = splitString[1];
                if(!departments.contains(department)){
                    buildConstraintValidation(context, department, " is not a valid department in assignedTo list");
                    return false;
                }
                if(!sections.contains(section)){
                    buildConstraintValidation(context, section, " is not a valid department in assignedTo list");
                    return false;
                }
            }
            else {
                if(!departments.contains(value)) {
                    buildConstraintValidation(context, value, " is not a valid department in assignedTo list");
                    return false;
                }
            }
        }
        return true;
    }

    private static void buildConstraintValidation(ConstraintValidatorContext context, String value, String message) {
        context.buildConstraintViolationWithTemplate(value + message)
                .addPropertyNode("assignedTo")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
