package com.hydrosmart.backend.notification.infrastructure.interfaces.rest;

import com.hydrosmart.backend.notification.application.internal.commandservices.NotificationCommandService;
import com.hydrosmart.backend.notification.domain.model.aggregates.NotificationRecord;
import com.hydrosmart.backend.notification.infrastructure.interfaces.rest.resources.NotificationResource;
import com.hydrosmart.backend.notification.infrastructure.interfaces.rest.resources.SendNotificationRequestResource;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlor REST del bounded context Notification.
 * Integra el servicio externo Resend para el envío de correos.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification")
public class NotificationController {

    private final NotificationCommandService notificationCommandService;

    /**
     * Envía una notificación por correo electrónico a través del servicio externo Resend.
     */
    @PostMapping("/send")
    @Operation(summary = "Send an email notification via Resend")
    public ResponseEntity<ApiResponse<NotificationResource>> send(
            @Valid @RequestBody SendNotificationRequestResource request) {
        NotificationRecord record = notificationCommandService
                .sendNotification(request.to(), request.subject(), request.message());
        NotificationResource resource = new NotificationResource(
                record.getTo(), record.getSubject(), record.getStatus(),
                record.getMessageId(),
                record.getSentAt() != null ? record.getSentAt().toString() : null);
        return ResponseEntity.ok(ApiResponse.ok(resource));
    }
}
