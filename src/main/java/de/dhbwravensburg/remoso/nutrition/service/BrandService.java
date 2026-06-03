package de.dhbwravensburg.remoso.nutrition.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.exception.ResourceInUseException;
import de.dhbwravensburg.remoso.nutrition.exception.ResourceNotFoundException;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.repository.BrandRepository;
import de.dhbwravensburg.remoso.nutrition.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Katrin Schaake, TIA25, Sonntag, 30.05.2026, Version: 0.3
 *
 * Umbau: ConcurrentHashMap + AtomicLong raus, Repository rein
 *
 * vorher: store.put(id, entity) - nachher: repository.save(entity)
 *
 * Seed-Dateien von hier -> in DataInitializer
 *
 * wirft ResourceNotFoundException und ResourceInUseException
 *
 */
@Service				// Dependency Injection - Spring erzeugt eine Instanz, wo gebraucht
public class BrandService {

	private final BrandRepository repository;			// statt ConcurrecntHashMap
	private final ProductRepository productRepository;	// um zu prüfen, ob Produkte existieren

	public BrandService(BrandRepository repository,  ProductRepository productRepository) {

		this.repository = repository;
		this.productRepository = productRepository;
	}

	public List<Brand> findAll() {
		return repository.findAll();
	}

	public Optional<Brand> findById(Long id) {
		return repository.findById(id);
	}

	public List<Brand> searchByName(String namePart) {
		return repository.findByNameContainingIgnoreCase(namePart);
	}

	public Brand create(Brand entity) {
		return repository.save(entity);	// JPA vergibt ID automatisch
	}

	public Brand update(Long id, Brand entity) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Brand", id);			// 404
		}
		entity.setId(id);
		return repository.save(entity);
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Brand", id);			// 404
		}
		if (!productRepository.findByBrandId(id).isEmpty()) {				// prüfen, ob auf diese Brand referenziert wird
			throw new ResourceInUseException("Brand", id, "products");	// 409
		}
		repository.deleteById(id);
	}
}
