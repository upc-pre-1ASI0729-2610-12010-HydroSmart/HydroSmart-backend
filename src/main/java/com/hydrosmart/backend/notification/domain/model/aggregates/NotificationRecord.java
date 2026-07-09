package com.hydrosmart.backend.notification.domain.model.aggregates;

import java.time.Instant;

/**
 * Aggregate que representa una notificación por correo enviada (o intentada)
 * dentro del bounded context Notification.
 *
 * Es inmutable: cada envío genera una nueva instancia con su resultado.
 */
public class NotificationRecord {

    private final String from;
    private final String to;
    private final String subject;
    private final String message;
    private final String status;        // "sent" | "failed"
    private final String messageId;     // id del proveedor (Resend) si aplica
    private final Instant sentAt;

    public NotificationRecord(String from, String to, String subject, String message,
                              String status, String messageId, Instant sentAt) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.messageId = messageId;
        this.sentAt = sentAt;
    }

    public static NotificationRecord sent(String from, String to, String subject,
                                          String message, String messageId) {
        return new NotificationRecord(from, to, subject, message, "sent", messageId, Instant.now());
    }

    public static NotificationRecord failed(String from, String to, String subject,
                                            String message, String error) {
        return new NotificationRecord(from, to, subject, message, "failed: " + error, null, Instant.now());
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public String getMessageId() { return messageId; }
    public Instant getSentAt() { return sentAt; }
}
