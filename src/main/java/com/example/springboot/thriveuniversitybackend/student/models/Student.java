package com.example.springboot.thriveuniversitybackend.student.models;

import com.example.springboot.thriveuniversitybackend.validators.annotations.Password;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("students")
public class Student {
    @Id
    private String id;
    private String email;
    @Password
    private String password;
    private String name;
    private boolean isRegistered = false;
    private String rollNo;
    private PersonalInfo personalInfo;

    public Student(String email, String password, String name, boolean isRegistered, String rollNo, PersonalInfo personalInfo) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.isRegistered = isRegistered;
        this.rollNo = rollNo;
        this.personalInfo = personalInfo;
    }

    public Student(String email, String password, String name, String rollNo) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.rollNo = rollNo;
    }

    public Student(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", isRegistered=" + isRegistered +
                ", rollNo='" + rollNo + '\'' +
                ", personalInfo=" + personalInfo +
                '}';
    }
}
