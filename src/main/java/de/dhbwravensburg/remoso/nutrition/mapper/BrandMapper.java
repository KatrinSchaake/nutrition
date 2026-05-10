package de.dhbwravensburg.remoso.nutrition.mapper;

import de.dhbwravensburg.remoso.nutrition.dto.BrandRequest;
import de.dhbwravensburg.remoso.nutrition.dto.BrandResponse;
import de.dhbwravensburg.remoso.nutrition.model.Brand;

/**
 * Katrin Schaake, TIA25, Sonntag, 10.05.2026, Version: 0.1
 */
public class BrandMapper {

	private BrandMapper() {}

	public static Brand toEntity(Long id, BrandRequest request) {
		return new Brand(
				id,
				request.name(),
				request.countries()
		);
	}

	public static BrandResponse toResponse(Brand entity) {
		if (entity == null) return null;	// to avoid NullPointerException if Product w/o Brand

		return new BrandResponse(
				entity.getId(),
				entity.getName(),
				entity.getCountries()
		);
	}
}
