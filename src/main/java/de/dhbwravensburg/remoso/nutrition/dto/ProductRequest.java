package de.dhbwravensburg.remoso.nutrition.dto;

import de.dhbwravensburg.remoso.nutrition.model.Brand;

/**
 * Katrin Schaake, TIA25, Samstag, 09.05.2026, Version: 0.1
 */
public record ProductRequest(
		String barcode,
		String name,
		Long brandId,	// nur REferenz, nicht die ganze Brand
		double caloriesKcal,
		double protein,
		double carbs
) {}
