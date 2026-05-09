package de.dhbwravensburg.remoso.nutrition.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.model.Brand;

/**
 * Katrin Schaake, TIA25, Sonntag, 10.05.2026, Version: 0.1
 */
@Service // Dependency Injection - Spring erzeugt eine Instanz, wo gebraucht
public class BrandService {

	private final ConcurrentHashMap<Long, Brand> store = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	public BrandService() {
		// seed data for development - for tests
		create(new Brand(null, "Olympus", "Griechenland"));
		create(new Brand(null, "Bauer", "Deutschland"));
		create(new Brand(null, "Weihenstephan", "Deutschland"));
	}

	public List<Brand> findAll() {
		return List.copyOf(store.values());
	}

	public Optional<Brand> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}

	public Brand create(Brand entity) {
		Long newId = idGenerator.getAndIncrement();
		entity.setId(newId);
		store.put(newId, entity);
		return entity;
	}

	public Optional<Brand> update(Long id, Brand entity) {
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
}
