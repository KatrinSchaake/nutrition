package de.dhbwravensburg.remoso.nutrition.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Katrin Schaake, TIA25, Sonntag, 10.05.2026, Version: 0.1
 */
public record BrandRequest(

		@NotBlank(message = "Brand name is required")
		String name,
		String countries
) {}
