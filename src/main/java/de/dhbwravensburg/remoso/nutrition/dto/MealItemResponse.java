package de.dhbwravensburg.remoso.nutrition.dto;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * DTO für die Ausgabe eines einzelnen Items.
 * Der Client bekommt das volle Produkt (als ProductResponse),
 * die Menge, und die bereits berechneten Nährwerte für dieses Item.
 *
 * Warum berechnete Felder im Response?
 *   -> Der Client muss nicht selbst rechnen. Die App liefert fertige Werte.
 */
public record MealItemResponse(

        Long id,
        ProductResponse product,    // verschachtelt, wie BrandResponse in ProductResponse
        double amountGrams,

        double totalCalories,       // bereits berechnet: product.caloriesKcal / 100 * amountGrams
        double totalProtein,         // bereits berechnet: product.protein / 100 * amountGrams
		double totalCarbs

) {}
