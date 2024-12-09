package com.example.isdfarmersmarket.business.listeners;

import com.example.isdfarmersmarket.business.events.RegistrationCompleteEvent;
import com.example.isdfarmersmarket.business.services.EmailSenderService;
import com.example.isdfarmersmarket.business.services.VerificationTokenService;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.models.VerificationToken;
import com.example.isdfarmersmarket.web.commands.SendEmailCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class RegistrationListener {

    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService emailSenderService;
    private final SpringTemplateEngine templateEngine;

    @EventListener
    private void confirmRegistration(RegistrationCompleteEvent event) {
        User user = event.getUser();

        VerificationToken token = verificationTokenService.generateVerificationToken(user);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String appUrl = "http://localhost:8080";
        String confirmationUrl = appUrl + "/auth/register/confirm?token=" + token.getToken();

        Context context = new Context();
        context.setVariable("confirmationUrl", confirmationUrl);
        String htmlContent = templateEngine.process("confirm-registration", context);

        emailSenderService.sendEmail(new SendEmailCommand(
                recipientAddress,
                subject,
                htmlContent
        ));
    }
}
