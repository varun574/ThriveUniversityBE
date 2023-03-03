package com.example.springboot.thriveuniversitybackend.otp;

public class OTPMismatchException extends RuntimeException{
    public OTPMismatchException(String message) {
        super(message);
    }
}
