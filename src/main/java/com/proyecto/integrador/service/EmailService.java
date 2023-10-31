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


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // Programar el envío de correo electrónico después de 24 horas
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // 24 horas en milisegundos
    public void sendScheduledEmail() {
        String to = "recipient@example.com";
        String subject = "Asunto del correo";
        sendEmail(to, subject, this.createHtml());
    }

    public void sendEmail() {
        String to = "javierramosnicolas@gmail.com";
        String subject = "prueba envio";
        sendEmail(to, subject, this.createHtml());
    }

    public String createHtml (){
        Context context = new Context();
        context.setVariable("nombre", "Javier"); // Puedes establecer el valor dinámico aquí
        String htmlContent = templateEngine.process("email-template.html", context);
        return htmlContent;
    }

    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("musicrentaldh@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}