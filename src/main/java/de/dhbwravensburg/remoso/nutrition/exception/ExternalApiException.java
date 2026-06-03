package de.dhbwravensburg.remoso.nutrition.exception;

/**
 * Katrin Schaake, TIA25, Mittwoch, 03.06.2026, Version: 0.1
 *
 * Wird geworfen,  wenn die OpenFoodFactsAPI nicht erreichbar ist oder einen Fehler zurückgibt.
 *
 * HTTP 502 Bad Gateway
 */
public class ExternalApiException extends RuntimeException {

	public ExternalApiException(String message) {
		super(message);
	}

	public ExternalApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
