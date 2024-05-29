package com.smatech.smatrentalpro.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Configuration
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Bean
    public SimpleMailMessage emailTemplate() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("recipient@email.com");
        message.setFrom("plannyb29@gmail.com");
        message.setSubject("SmatRentalPro Application - Account Verification");
        message.setText("FATAL - Application crash. Save your job !!");
        return message;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));

        mailSender.setUsername(fromEmail);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
