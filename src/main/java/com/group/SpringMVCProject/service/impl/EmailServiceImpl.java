package com.group.SpringMVCProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String token) {
        String subject = "Password Reset Request";
        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        String message = "Hi,\n\nClick the following link to reset your password:\n" + resetLink +
                "\n\nThis link will expire in 15 minutes.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
