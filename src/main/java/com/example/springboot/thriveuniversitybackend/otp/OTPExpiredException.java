package com.example.springboot.thriveuniversitybackend.otp;

import java.util.concurrent.ExecutionException;

public class OTPExpiredException extends RuntimeException {
    public OTPExpiredException(String s) {
        super(s);
    }
}
