package com.example.springboot.thriveuniversitybackend.mail;

import com.example.springboot.thriveuniversitybackend.attachment.FileService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private FileService fileService;

    public String sendMail(EmailDetails details, MultipartFile multipartFile)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody().generateBody(), true);
            mimeMessageHelper.setSubject(
                    details.getSubject());

        if(multipartFile!=null && !multipartFile.isEmpty()){
            File file = fileService.convertToFile(multipartFile);

            mimeMessageHelper.addAttachment(
                    file.getName(), file);
            }

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        catch (MessagingException e) {
            return "Error while sending mail!!!";
        } catch (IOException e) {
            return "Error while accessing the file!!!";
        }
    }
}
