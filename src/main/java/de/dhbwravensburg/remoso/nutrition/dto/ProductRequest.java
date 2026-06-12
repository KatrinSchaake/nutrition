package de.dhbwravensburg.remoso.nutrition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * Katrin Schaake, TIA25, Samstag, 09.05.2026, Version: 0.1
 */
public record ProductRequest(
		@NotBlank(message = "Barcode is required") String barcode,
		@NotBlank(message = "Product name is required") String name,
		Long brandId,			// nur Referenz, nicht die ganze Brand

		@PositiveOrZero(message = "Calories must be >=0") Double caloriesKcal,
		@PositiveOrZero(message = "Protein must be >=0") Double protein,
		@PositiveOrZero(message = "Carbs must be >=0") Double carbs
) {}
