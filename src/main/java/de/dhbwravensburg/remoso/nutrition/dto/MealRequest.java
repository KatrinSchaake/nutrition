// MealRequest.java
package de.dhbwravensburg.remoso.nutrition.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * DTO für eingehende Mahlzeiten-Daten (POST/PUT).
 *
 * Beispiel-JSON:
 * {
 *   "name": "Frühstück",
 *   "date": "2026-05-28",
 *   "items": [
 *     { "productId": 1, "amountGrams": 200.0 },
 *     { "productId": 2, "amountGrams": 150.0 }
 *   ]
 * }
 */
public record MealRequest(

        String name,

        LocalDate date,

        List<MealItemRequest> items  // Liste der Produkte + Mengen

) {}
