package de.dhbwravensburg.remoso.nutrition.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25, Sonntag, 10.05.2026, Version: 0.1
 */
@Service
public class ProductService {

	private final ConcurrentHashMap<Long, Product> store = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	private final BrandService brandService;

	public ProductService(BrandService brandService) {
		// Constructor Injection
		this.brandService = brandService;

		// seed data for development
		Brand Bauer = brandService.findById(2L).orElseThrow();
		Brand Weihenstephan = brandService.findById(3L).orElseThrow();

		create(new Product(null, "4002334113032", "Der große Bauer - Kirsche",
				Bauer, 366, 3.2, 12.0));
		create(new Product(null, "4008452010222", "Butter",
				Weihenstephan, 747, 0.6, 1.0));
	}

	public List<Product> findAll() {
		return List.copyOf(store.values());
	}

	public Optional<Product> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}

	public Product create(Product entity) {
		Long newId = idGenerator.getAndIncrement();
		entity.setId(newId);
		store.put(newId, entity);
		return entity;
	}

	public Optional<Product> update(Long id, Product entity) {
		if (!store.containsKey(id)) {
			return Optional.empty();
		}
		entity.setId(id);
		store.put(id, entity);
		return Optional.of(entity);
	}

	public boolean delete(Long id) {
		return store.remove(id) != null;
	}

	/** Resolves a brandId to an actual Brand entity. Used by the controller befor
	 * mapping a ProductRequest to a Product
	 */
	public Optional<Brand> resolveBrand(Long brandId) {
		if (brandId == null) {
			return Optional.empty();
		}
		return brandService.findById(brandId);
	}

}
