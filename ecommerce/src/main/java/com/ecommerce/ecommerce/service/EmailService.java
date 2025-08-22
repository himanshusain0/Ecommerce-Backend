package com.ecommerce.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            // HTML Email Template
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 0; margin: 0; }" +
                    ".container { max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0px 2px 8px rgba(0,0,0,0.1); }" +
                    ".header { text-align: center; background: #4CAF50; padding: 10px; color: white; border-radius: 8px 8px 0 0; }" +
                    ".otp { font-size: 28px; font-weight: bold; color: #4CAF50; margin: 20px 0; text-align: center; }" +
                    ".footer { font-size: 12px; color: #888; text-align: center; margin-top: 20px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<h2>Apna Bazar</h2>" +
                    "<p>Your Secure OTP</p>" +
                    "</div>" +
                    "<p>Hello,</p>" +
                    "<p>Your one-time password (OTP) for login/signup is:</p>" +
                    "<div class='otp'>" + otp + "</div>" +
                    "<p>This OTP will expire in <strong>5 minutes</strong>. Please do not share it with anyone.</p>" +
                    "<div class='footer'>" +
                    "<p>© 2025 Apna Bazar. All rights reserved.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(htmlContent, true); // HTML content enabled
            mimeMessageHelper.setTo(userEmail);

            javaMailSender.send(mimeMessage);

        } catch (MailException e) {
            System.out.println("error ----------->" + e);
            throw new MailSendException("failed to send email");
        }
    }
}
