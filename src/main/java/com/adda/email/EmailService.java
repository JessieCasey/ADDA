package com.adda.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * EmailService process email actions
 */

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    /**
     * Constructor for {@link EmailService}.
     *
     * @param mailSender {@link JavaMailSender}
     */
    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    /**
     * Method that sending emails to the specific address
     *
     * @param toAddress Address to send email
     * @param content   content
     * @param subject   subject in the email letter
     */
    public void sendEmail(String toAddress, String content, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, "DoubleA");
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
