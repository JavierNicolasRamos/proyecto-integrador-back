package com.proyecto.integrador.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    void sendRegisterEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String htmlContent = "<html>...</html>";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendRegisterEmail(to, subject, htmlContent);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender, times(1)).send(mimeMessageCaptor.capture());

        MimeMessage sentMimeMessage = mimeMessageCaptor.getValue();
        try {
            assertEquals(to, sentMimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());
            assertEquals(subject, sentMimeMessage.getSubject());

        } catch (MessagingException e) {
            fail("Exception should not be thrown");
        }
    }



    @Test
    void sendScheduleEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String htmlContent = "<html>...</html>";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendScheduleEmail(to, subject, htmlContent);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender, times(1)).send(mimeMessageCaptor.capture());

        MimeMessage sentMimeMessage = mimeMessageCaptor.getValue();
        try {
            assertEquals(to, sentMimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());
            assertEquals(subject, sentMimeMessage.getSubject());

        } catch (MessagingException e) {
            fail("Exception should not be thrown");
        }
    }
}
