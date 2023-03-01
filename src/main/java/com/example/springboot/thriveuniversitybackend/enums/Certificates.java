package com.example.springboot.thriveuniversitybackend.enums;

public enum Certificates {
    TENTH_CERTIFICATE("10th_certificate"),
    INTER_CERTIFICATE("12th_certificate"),
    UG_CERTIFICATE("UG_certificate"),
    APTITUDE_BASED_CERTIFICATE("aptitude_certificate"),
    ENGLISH_BASED_CERTIFICATE("english_certificate");
    public String value;
    Certificates(String s) {
        this.value = s;
    }
}
