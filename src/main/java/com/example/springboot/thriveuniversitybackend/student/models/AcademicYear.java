package com.example.springboot.thriveuniversitybackend.student.models;

import com.example.springboot.thriveuniversitybackend.enums.Semester;
import com.example.springboot.thriveuniversitybackend.enums.Year;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import com.example.springboot.thriveuniversitybackend.validators.annotations.NullableNonEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcademicYear {
    @EnumValue(enumC = Year.class, message = "Year must be chosen from the given accepted values")
    @NullableNonEmpty(message = "Year must not be empty")
    private String year;
    @EnumValue(enumC = Semester.class, message = "Semester must be chosen from the given accepted values")
    @NullableNonEmpty(message = "Semester must not be empty")
    private String semester;
}
