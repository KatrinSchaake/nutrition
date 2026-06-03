package de.dhbwravensburg.remoso.nutrition.exception;

/**
 * Katrin Schaake, TIA25, Mittwoch, 03.06.2026, Version: 0.1
 *
 * Wird geworfen, wenn Resource nicht gelöscht werden kann, weil andere Ressourcen
 * sie noch referenzieren.
 *
 * HTTP 409 Conflict
 */
public class ResourceInUseException extends RuntimeException {

	public ResourceInUseException(String resourceName, Long id, String referencedBy) {

		super(resourceName + " w/ ID " + id + " cannot be deleted, still referenced by  " + referencedBy);
	}
}
