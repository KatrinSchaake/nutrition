package de.dhbwravensburg.remoso.nutrition.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * Katrin Schaake, TIA25 – Version: 0.2
 *
 * DTO für eingehende Mahlzeiten-Daten (POST/PUT).
 *
 * ohne lastModified hier, wird von JPA automatisch gesetzt
 * Client schickt nur category, name und die Items
 *
 * Beispiel-JSON:
 * {
 *	 "category": "Frühstück",
 *   "name": "Shake mit Naturjoghurt",
 *   "items": [
 *     { "productId": 1, "amountGrams": 200.0 },
 *     { "productId": 2, "amountGrams": 150.0 }
 *   ]
 * }
 */
public record MealRequest(

        @NotBlank(message = "Name for meal category is required") String category,
		@NotBlank(message = "Meal name is required") String name,

        @NotEmpty
		@Valid
		List<MealItemRequest> items  // Liste der Produkte + Mengen

) {}
