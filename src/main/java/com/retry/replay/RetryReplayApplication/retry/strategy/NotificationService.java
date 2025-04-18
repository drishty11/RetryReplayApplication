package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.dto.ReplayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.password}")
    private String mailId;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReplayInitiationEmail(String correlationId, ReplayRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("drishtypalll@gmail.com");  // Recipient email address
        message.setSubject("Replay Initiated - Correlation ID: " + correlationId);
        message.setText("Replay has been initiated for the following request: \n" +
                "Transaction IDs: " + request.getTransactionIds() + "\n" +
                "Scope: " + request.getScope() + "\n" +
                "Replay Type: " + request.getReplayType() + "\n" +
                "Scheduled Time: " + request.getScheduledTime() + "\n" +
                "Correlation ID: " + correlationId);

        // Send the email
        mailSender.send(message);
        log.info("Email sent to mail id :: {} for replay initiation.", mailId);
    }
}

