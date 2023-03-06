package com.example.springboot.thriveuniversitybackend.otp;

import com.example.springboot.thriveuniversitybackend.mail.MailService;
import com.example.springboot.thriveuniversitybackend.mail.EmailDetails;
import com.example.springboot.thriveuniversitybackend.mail.EmailDetails.EmailTemplate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {
    private static final int OTP_EXPIRE_MINS = 2;
    private LoadingCache<String, Integer> cache;

    @Autowired
    private MailService mailService;

    public OTPService() {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(OTP_EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {

                    @Override
                    public Integer load(String key) {
                        return -1;
                    }
                });
    }

    public int generateOTP(String key){
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        cache.put(key, otp);
        return otp;
    }

    public int getOtp(String key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException("Please try again.");
        }
    }

    public void clearOtp(String key){
        cache.invalidate(key);
    }

    public void sendOtpMail(String email, String recipient, String userName){
        String subject = "OTP for Thrive Password verification";
        String template = new String("src/main/java/com/example/springboot/thriveuniversitybackend/otp/OtpTemplate.html");
        int otp = generateOTP(email);
        HashMap<String, String> replacements = new HashMap();
        replacements.put("user", userName);
        replacements.put("otp", String.valueOf(otp));
        mailService.sendMail(new EmailDetails(new String[]{recipient}, new EmailTemplate(template, replacements), subject), null);
    }
}
