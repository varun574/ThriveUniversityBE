package com.example.springboot.thriveuniversitybackend.student.models;

import com.example.springboot.thriveuniversitybackend.enums.Department;
import com.example.springboot.thriveuniversitybackend.enums.EducationLevel;
import com.example.springboot.thriveuniversitybackend.enums.Section;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import com.example.springboot.thriveuniversitybackend.validators.annotations.NullableNonEmpty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document("personalInfo")
public class PersonalInfo {
    @NullableNonEmpty(message = "Father name must not be empty")
    private String fatherName;
    @NullableNonEmpty(message = "Mother name must not be empty")
    private String motherName;
    @Past(message = "Earlier date must be picked")
    private LocalDate dob;
    @NullableNonEmpty(message = "Mobile number must not be empty")
    @Pattern(regexp = "^(?:\\+91)?\\d{10}$", message = "Mobile must be valid phone number.")
    private String mobileNumber;
    @Enumerated(EnumType.STRING)
    @NullableNonEmpty(message = "Department must not be empty")
    @EnumValue(enumC = Department.class, message = "Department must be chosen from the given accepted values")
    private String department;
    @Enumerated(EnumType.STRING)
    @NullableNonEmpty(message = "Section must not be empty")
    @EnumValue(enumC = Section.class, message = "Section must be chosen from the given accepted values")
    private String section;
    @Enumerated(EnumType.STRING)
    @EnumValue(enumC = EducationLevel.class, message = "Education Level must be chosen from the given accepted values")
    @NullableNonEmpty(message = "Education Level must not be empty")
    private String educationLevel;
    @NullableNonEmpty(message = "Address must not be empty")
    private String address;

    public PersonalInfo(String fatherName, String motherName, LocalDate dob, String mobileNumber, String department, String section, String educationLevel, String address) {
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.dob = dob;
        this.mobileNumber = mobileNumber;
        this.department = department;
        this.section = section;
        this.educationLevel = educationLevel;
        this.address = address;
    }

    public PersonalInfo() {
    }


    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PersonalInfo{" +
                "fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", dob=" + dob +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", department='" + department + '\'' +
                ", section='" + section + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
