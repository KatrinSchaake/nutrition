package de.dhbwravensburg.remoso.nutrition.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.repository.BrandRepository;

/**
 * Katrin Schaake, TIA25, Sonntag, 30.05.2026, Version: 0.2
 *
 * Umbau: ConcurrentHashMap + AtomicLong raus, Repository rein
 *
 * vorher: store.put(id, entity)
 * nachher: repository.save(entity)
 *
 * Seed-Dateien von hier -> in DataInitializer
 */
@Service				// Dependency Injection - Spring erzeugt eine Instanz, wo gebraucht
public class BrandService {

	private final BrandRepository repository;	// statt ConcurrecntHashMap

	public BrandService(BrandRepository repository) {
		this.repository = repository;
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
		return repository.save(entity);		// JPA vergibt ID automatisch
	}

	public Optional<Brand> update(Long id, Brand entity) {
		if (!repository.existsById(id)) {
			return Optional.empty();
		}
		entity.setId(id);
		return Optional.of(repository.save(entity));
	}

	public boolean delete(Long id) {
		if (!repository.existsById(id)) {
			return false;
		}
		repository.deleteById(id);
		return true;
	}
}
