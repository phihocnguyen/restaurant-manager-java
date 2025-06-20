package com.restaurant.backend.other_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String email, String verificationCode) {
        logger.info("[EMAIL] Sending verification code to: {} | code: {}", email, verificationCode);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Your verification code is " + verificationCode);
        message.setFrom("RestaurantManager <no-reply@gmail.com>");
        try {
            this.mailSender.send(message);
            logger.info("[EMAIL] Verification code sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("[EMAIL] Failed to send verification code to: {} | error: {}", email, e.getMessage(), e);
        }
    }

    public void sendReceiptEmail(String to, String subject, String content) {
        logger.info("[EMAIL] Sending receipt email to: {} | subject: {}", to, subject);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = gửi HTML
            helper.setFrom("RestaurantManager <no-reply@gmail.com>");
            mailSender.send(message);
            logger.info("[EMAIL] Receipt email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("[EMAIL] Failed to send receipt email to: {} | error: {}", to, e.getMessage(), e);
        }
    }
}
