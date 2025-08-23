package com.notification.notificationService.function;

import com.fabrikka.common.NotificationDetailsDto;
import com.notification.notificationService.service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SendEmailFunctions {

    NotificationService notificationService;

    public SendEmailFunctions(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

/*
    This class will contain all the functions related to sending emails
 */

    /**
     * A Spring Cloud Function that consumes a NotificationDetailsDto from a message queue
     * and sends an email.
     */
    @Bean
    Consumer<NotificationDetailsDto> sendNotificationGeneric() {
        return notificationDetailsDto -> {
            try { notificationService.sendNotificationGeneric(notificationDetailsDto); }
            catch (MessagingException e) { throw new RuntimeException("Failed to send notification for user.", e); }
        };
    }

    @Bean
    Consumer<NotificationDetailsDto> sendNotification() {
        return notificationDetailsDto -> {
            try {
                notificationService.sendNotification(notificationDetailsDto);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send notification for user.", e);
            }
        };
    }
}
