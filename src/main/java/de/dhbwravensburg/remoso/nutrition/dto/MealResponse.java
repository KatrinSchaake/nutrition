package de.dhbwravensburg.remoso.nutrition.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * DTO für ausgehende Mahlzeiten-Daten (Response).
 * Enthält die berechneten Gesamtwerte direkt – der Client
 * braucht nichts mehr selbst auszurechnen.
 *
 * Beispiel-JSON (Antwort vom Server):
 * {
 *   "id": 1,
 *   "name": "Frühstück",
 *   "date": "2026-05-28",
 *   "items": [ ... ],
 *   "totalCalories": 882.3,
 *   "totalProtein": 36.5
 * }
 */
public record MealResponse(

        Long id,
		String category,
        String name,
        LocalDateTime lastModified,
        List<MealItemResponse> items,

        double totalCalories,   // Summe aller Items – fertig berechnet
        double totalProtein,     // Summe aller Items – fertig berechnet
		double totalCarbs
) {}
