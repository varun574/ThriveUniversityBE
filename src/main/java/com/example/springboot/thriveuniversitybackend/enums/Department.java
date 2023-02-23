package com.example.springboot.thriveuniversitybackend.enums;

import org.springframework.stereotype.Component;


public enum Department {

    Civil_Engineering("Civil Engineering"),
    Computer_Science_Engineering("Computer Science Engineering"),
    Electronics_Communication_Engineering("Electronics and Communication Engineering"),
    Electrical_Electronics_Engineering("Electrical and Electronics Engineering"),
    Electronics_Instrumentation_Engineering("Electronics and Instrumentation Engineering"),
    Information_Technology("Information Technology"),
    Mechanical_Engineering("Mechanical Engineering");

    String value;
    Department(String s) {
        this.value=s;
    }
}
