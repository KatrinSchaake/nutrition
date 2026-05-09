package de.dhbwravensburg.remoso.nutrition.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25  -  Sonntag, 03.05.2026, Version: 0.1
 */

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final List<Brand> mockBrands = List.of(
			new Brand(1L, "Olympus", "Griechenland"),
			new Brand(2L, "Bauer", "Deutschland"),
			new Brand(3L, "Weihenstephan", "Deutschland")
	);

	private Brand findBrand(String nameBrand) {
		return mockBrands.stream()
				.filter(brand -> brand.getName().equals(nameBrand))
				.findFirst()
				.orElseThrow();
	}

	private final List<Product> mockProducts = List.of(
			new Product("5202178009495", "E/Feta original", findBrand("Olympus"), 276, 16.5, 0.7),
			new Product("4002334113032", "Der große Bauer - Kirsche", findBrand("Bauer"), 366, 3.2, 12.0),
			new Product("4008452010222", "Butter",  findBrand( "Weihenstephan"), 747, 0.6, 1.0)
	);

	@GetMapping
	public List<Product> getProducts() {
		return mockProducts;
	}
}
