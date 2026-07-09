package com.hydrosmart.backend.notification.infrastructure.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record SendNotificationRequestResource(
        @NotBlank(message = "El destinatario (to) es obligatorio") String to,
        @NotBlank(message = "El asunto (subject) es obligatorio") String subject,
        @NotBlank(message = "El mensaje (message) es obligatorio") String message
) {}
