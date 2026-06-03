package de.dhbwravensburg.remoso.nutrition.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.exception.ResourceInUseException;
import de.dhbwravensburg.remoso.nutrition.exception.ResourceNotFoundException;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;
import de.dhbwravensburg.remoso.nutrition.repository.ProductRepository;

/**
 * Katrin Schaake, TIA25, Sonnabend, 30.05.2026, Version: 0.2
 *
 * Umbau: ConcurrentHashMap zu ProductRepository
 *
 * resolveBrand() bleibt - Controller bruacht die Methode weiterhin, um eine
 * brandId aus Request in eine echte Brand-Entity aufzulösen
 */
@Service
public class ProductService {

	private final ProductRepository repository;
	private final BrandService brandService;

	public ProductService(ProductRepository repository, BrandService brandService) {
		this.repository = repository;
		this.brandService = brandService;
	}

	public List<Product> findAll() {
		return repository.findAll();
	}

	public Optional<Product> findById(Long id) {
		return repository.findById(id);
	}

	public List<Product> findByBrandId(Long brandId) {
		return repository.findByBrandId(brandId);
	}

	public List<Product> searchByName(String namePart) {
		return repository.findByNameIsContainingIgnoreCase(namePart);
	}

	public Product create(Product entity) { return repository.save(entity); }

	public Product update(Long id, Product entity) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Product", id);
		}
		entity.setId(id);
		return repository.save(entity);
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Product", id);
		}

		// Prüfen ob das Produkt in Mahlzeiten verwendet wird
		if (repository.countMealItemsByProductId(id) > 0) {
			throw new ResourceInUseException("Product", id, "meals");
		}

		repository.deleteById(id);
	}

	public Optional<Brand> resolveBrand(Long brandId) {
		if (brandId == null) {
			return Optional.empty();
		}
		return brandService.findById(brandId);
	}

}
