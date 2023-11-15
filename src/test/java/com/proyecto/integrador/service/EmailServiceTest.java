package com.proyecto.integrador.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendScheduledEmail() {
        MimeMessage mimeMessage = mock(MimeMessage.class);

        doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("email content");
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendScheduledEmail();

        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    void sendEmail() {
        String to = "javierramosnicolas@gmail.com";
        String subject = "prueba envio";
        String htmlContent = "<p>Hello, World!</p>";

        doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(htmlContent);

        emailService.sendEmail(to, subject, htmlContent);

        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    void createHtml() {
        String expectedHtml = "<p>Hello, World!</p>";
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(expectedHtml);

        String actualHtml = emailService.createHtml();

        assertEquals(expectedHtml, actualHtml);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String htmlContent = "<p>Hello, World!</p>";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendEmail(to, subject, htmlContent));

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}