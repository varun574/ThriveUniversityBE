package com.example.springboot.thriveuniversitybackend.enums;

import org.springframework.stereotype.Component;

public enum EducationLevel {
    B_TECH("B.Tech"),
    M_TECH("M.Tech"),
    DIPLOMA("Diploma"),
    PhD("PhD");

    String value;
    EducationLevel(String s) {
        this.value = s;
    }
};
