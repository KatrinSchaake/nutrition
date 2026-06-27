package de.dhbwravensburg.remoso.nutrition.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * DTO für ein einzelnes Item innerhalb eines MealRequest.
 *
 * Der Client schickt NUR die productId (Referenz) und die Menge.
 * Die echte Product-Entity wird erst im Service aufgelöst –
 * genau wie brandId in ProductRequest.
 *
 * Beispiel-JSON für ein Item:
 * {
 *   "productId": 1,
 *   "amountGrams": 200.0
 * }
 */
public record MealItemRequest(

		@NotNull Long productId,     // Referenz auf das Produkt
        @Positive Double amountGrams  // wie viele Gramm

) {}
