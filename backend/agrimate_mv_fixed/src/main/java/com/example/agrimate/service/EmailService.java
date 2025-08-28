package com.example.agrimate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public void sendOtp(String to, String code) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Your OTP Code");
            msg.setText("Your OTP is: " + code + " (valid for 10 minutes)");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Failed to send email, logging OTP instead. email={}, otp={}", to, code);
        }
    }
}
