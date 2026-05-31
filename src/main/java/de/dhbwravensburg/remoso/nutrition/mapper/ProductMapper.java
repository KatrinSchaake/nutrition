package de.dhbwravensburg.remoso.nutrition.mapper;

import de.dhbwravensburg.remoso.nutrition.dto.BrandResponse;
import de.dhbwravensburg.remoso.nutrition.dto.ProductRequest;
import de.dhbwravensburg.remoso.nutrition.dto.ProductResponse;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25, Sonntag, 10.05.2026, Version: 0.1
 */
public final class ProductMapper {

	private ProductMapper() {}

	public static Product toEntity(Long id, ProductRequest request, Brand brand) {
		return new Product(
				id,
				request.barcode(),
				request.name(),
				brand,
				request.caloriesKcal(),
				request.protein(),
				request.carbs()
		);
	}

	public static ProductResponse toResponse(Product entity) {
		return new ProductResponse(
				entity.getId(),
				entity.getBarcode(),
				entity.getName(),
				BrandMapper.toResponse(entity.getBrand()),
				entity.getCaloriesKcal(),
				entity.getProtein(),
				entity.getCarbs()
		);
	}

}
