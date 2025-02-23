package com.groupeisi.services.exception;

/**
 * Exception personnalisée pour les erreurs liées à Keycloak.
 */
public class KeycloakException extends RuntimeException {

    /**
     * Constructeur avec un message d'erreur.
     *
     * @param message Le message d'erreur.
     */
    public KeycloakException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message d'erreur et une cause.
     *
     * @param message Le message d'erreur.
     * @param cause   La cause de l'exception.
     */
    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }
}