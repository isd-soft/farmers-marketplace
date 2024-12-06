package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.EmailSendingException;
import com.example.isdfarmersmarket.web.commands.SendEmailCommand;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
@AllArgsConstructor
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    public void sendEmail(SendEmailCommand sendEmailCommand) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(sendEmailCommand.getTo());
            helper.setSubject(sendEmailCommand.getSubject());
            helper.setText(sendEmailCommand.getHtmlContent(), true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send email: " + e.getMessage());
        }
    }
}
