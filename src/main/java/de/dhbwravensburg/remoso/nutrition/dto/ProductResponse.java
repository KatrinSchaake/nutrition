package de.dhbwravensburg.remoso.nutrition.dto;

/**
 * Katrin Schaake, TIA25, Samstag, 09.05.2026, Version: 0.1
 */
public record ProductResponse(
		Long id,
		String barcode,
		String name,
		BrandResponse brand, // verschachtelt - Client bekommt volle Brand

		double caloriesKcal,
		double protein,
		double carbs

) {}
