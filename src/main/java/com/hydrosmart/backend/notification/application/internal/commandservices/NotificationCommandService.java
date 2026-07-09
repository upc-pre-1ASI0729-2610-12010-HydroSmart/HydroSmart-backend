package com.hydrosmart.backend.notification.application.internal.commandservices;

import com.hydrosmart.backend.notification.domain.model.aggregates.NotificationRecord;
import com.hydrosmart.backend.notification.domain.services.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación del bounded context Notification.
 * Orquesta el envío de correos a través del puerto {@link EmailSender}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationCommandService {

    private final EmailSender emailSender;

    @Value("${resend.from:onboarding@resend.com}")
    private String fromAddress;

    @Value("${app.notification.default-recipient:}")
    private String defaultRecipient;

    /**
     * Envía una notificación por correo a un destinatario específico.
     */
    public NotificationRecord sendNotification(String to, String subject, String message) {
        String html = "<div style=\"font-family:Arial,sans-serif;line-height:1.6;color:#031635\">"
                + "<h2 style=\"color:#23707D\">" + escape(subject) + "</h2>"
                + "<p style=\"font-size:15px\">" + escape(message).replace("\n", "<br>") + "</p>"
                + "<hr><small style=\"color:#B7B7B7\">HydroSmart · AquaPulse — Notificación automática</small>"
                + "</div>";

        EmailSender.EmailResult result = emailSender.send(to, subject, html);
        if (result.success()) {
            log.info("[Notification] Email sent to {} (id={})", to, result.messageId());
            return NotificationRecord.sent(fromAddress, to, subject, message, result.messageId());
        }
        log.warn("[Notification] Failed to send email to {}: {}", to, result.error());
        return NotificationRecord.failed(fromAddress, to, subject, message, result.error());
    }

    /**
     * Envía una notificación al destinatario por defecto configurado
     * (útil para los disparos automáticos desde otros bounded contexts).
     */
    public NotificationRecord sendToDefaultRecipient(String subject, String message) {
        String to = (defaultRecipient == null || defaultRecipient.isBlank())
                ? fromAddress : defaultRecipient;
        return sendNotification(to, subject, message);
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
