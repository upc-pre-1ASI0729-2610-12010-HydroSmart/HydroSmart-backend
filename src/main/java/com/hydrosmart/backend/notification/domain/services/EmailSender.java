package com.hydrosmart.backend.notification.domain.services;

/**
 * Port (interface) del servicio de envío de correos.
 * Define el contrato del servicio externo de email, de modo que el dominio
 * no dependa de un proveedor concreto (Resend). La implementación vive en
 * la capa de infraestructura.
 */
public interface EmailSender {

    EmailResult send(String to, String subject, String htmlMessage);

    /**
     * Resultado del envío de un correo.
     *
     * @param success   true si el correo fue aceptado por el proveedor.
     * @param messageId identificador retornado por el proveedor (ej. "re_xxx").
     * @param error      mensaje de error cuando success es false.
     */
    record EmailResult(boolean success, String messageId, String error) {}
}
