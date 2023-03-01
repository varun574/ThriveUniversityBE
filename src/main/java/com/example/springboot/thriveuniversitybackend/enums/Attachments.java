package com.example.springboot.thriveuniversitybackend.enums;

public enum Attachments{

    PROFILE_PICTURE("profile_picture"),
    ASSIGNMENT("assignment"),
    ANONYMOUS("anonymous");
    public String value;
    Attachments(String s) {
        this.value = s;
    }
}
