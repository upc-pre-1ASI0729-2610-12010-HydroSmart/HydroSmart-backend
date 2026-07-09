package com.hydrosmart.backend.notification.infrastructure.external;

import com.hydrosmart.backend.notification.domain.services.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Adapter que implementa {@link EmailSender} usando el servicio externo Resend.
 *
 * Realiza una petición POST a la API de Resend autenticándose con la API key
 * leída desde la variable de entorno RESEND_API_KEY.
 *
 * Endpoint: https://api.resend.com/emails
 */
@Component
@Slf4j
public class ResendEmailSenderAdapter implements EmailSender {

    private final RestClient resendClient;
    private final String apiKey;
    private final String apiUrl;

    public ResendEmailSenderAdapter(
            @Value("${resend.api.url:https://api.resend.com/emails}") String apiUrl,
            @Value("${resend.api.key:}") String apiKey,
            RestClient.Builder restClientBuilder) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.resendClient = restClientBuilder.build();
    }

    @Override
    public EmailResult send(String to, String subject, String htmlMessage) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("[Resend] No API key configured (resend.api.key). Skipping email send.");
            return new EmailResult(false, null, "Resend API key not configured");
        }
        try {
            Map<String, Object> body = Map.of(
                    "from", "onboarding@resend.dev",
                    "to", new String[]{to},
                    "subject", subject,
                    "html", htmlMessage
            );
            ResendResponse response = resendClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(ResendResponse.class);
            String id = (response != null) ? response.id() : null;
            return new EmailResult(true, id, null);
        } catch (Exception e) {
            log.error("[Resend] Error sending email to {}: {}", to, e.getMessage());
            return new EmailResult(false, null, e.getMessage());
        }
    }

    /** Respuesta esperada de Resend: { "id": "re_..." } */
    private record ResendResponse(String id) {}
}
