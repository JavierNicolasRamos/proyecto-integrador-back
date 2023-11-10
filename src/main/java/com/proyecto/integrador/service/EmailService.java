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

    // Programar el envío de correo electrónico después de 24 horas
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // 24 horas en milisegundos
    public void sendScheduledEmail() {
        String to = "recipient@example.com";  //Pasar como parametro el email del destinatario
        String subject = "Asunto del correo"; //Pasar como parametro el asunto del email
        sendEmail(to, subject, this.createHtml());
    }

    public void sendEmail() {
        String to = "ignacio.gibbs@gmail.com"; //Pasar como parametro el email del destinatario
        String subject = "Registro usuario"; //Pasar como parametro el asunto del email
        sendEmail(to, subject, this.createHtml());
    }

    public String createHtml (){
        Context context = new Context();
        //Agregar parametros segun se lo requiera el diseño
        context.setVariable("nombre", "Ignacio"); //Pasar como parametro el nombre del destinatario
        context.setVariable("usuario", "Igibbs"); //Pasar como parametro el nombre del destinatario
        return templateEngine.process("correo.html", context);
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
            logger.error("Error al enviar el correo", e);
        }
    }
}