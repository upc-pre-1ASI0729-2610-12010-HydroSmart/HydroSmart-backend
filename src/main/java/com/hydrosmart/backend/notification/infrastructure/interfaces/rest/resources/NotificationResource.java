package com.hydrosmart.backend.notification.infrastructure.interfaces.rest.resources;

public record NotificationResource(
        String to,
        String subject,
        String status,
        String messageId,
        String sentAt
) {}
