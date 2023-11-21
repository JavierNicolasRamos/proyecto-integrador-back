package com.proyecto.integrador.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void sendScheduledEmail(String name, String username, String email, String subject) {
        sendScheduleEmail(email, subject, createScheduleHtml(name,  username));
    }

    public String createRegisterHtml(String name, String username){
        Context context = new Context();

        context.setVariable("nombre", name);
        context.setVariable("usuario", username);
        return templateEngine.process("registerEmail.html", context);
    }

    public void sendRegisterEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("musicrentaldh@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Error al enviar el correo", e);
        }
    }

    public String createScheduleHtml(String name, String username){
        Context context = new Context();

        context.setVariable("nombre", name);
        context.setVariable("usuario", username);
        return templateEngine.process("scheduleEmail.html", context);
    }

    public void sendScheduleEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("musicrentaldh@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Error al enviar el correo", e);
        }
    }
}