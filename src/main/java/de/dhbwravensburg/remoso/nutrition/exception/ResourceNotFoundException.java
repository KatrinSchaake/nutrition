package de.dhbwravensburg.remoso.nutrition.exception;

/**
 * Katrin Schaake, TIA25, Mittwoch, 03.06.2026, Version: 0.1
 *
 * Wird geworfen, wenn Resource per ID nicht gefunden wird
 * Bsp.: throw new ResourceNotFoundException("Brand", 42L);
 * 	-> Nachricht: "Brand w/ ID 42 not found"
 *
 * generisch: eine Exception für Brand, Product UND Meal
 */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String resourceName, Long id) {

		super(resourceName + " /w ID " + id + " not found");
	}
}
