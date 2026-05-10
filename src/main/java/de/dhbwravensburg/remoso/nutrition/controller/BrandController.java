package de.dhbwravensburg.remoso.nutrition.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwravensburg.remoso.nutrition.dto.BrandRequest;
import de.dhbwravensburg.remoso.nutrition.dto.BrandResponse;
import de.dhbwravensburg.remoso.nutrition.mapper.BrandMapper;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.service.BrandService;

/**
 * Katrin Schaake, TIA25, Sonntag, 10.05.2026, Version: 0.1
 */
@RestController		// diese Klasse ist ein REST-Controller, alle Methoden geben JSON
@RequestMapping("/api/brands")
public class BrandController {

	private final BrandService service; // ein Feld

	//Konstruktoe mit dem Service als Parameter
	public BrandController(BrandService service) {
		this.service = service;
	}

	@GetMapping
	public List<BrandResponse> getAll() {
		return service.findAll().stream()
				.map(BrandMapper::toResponse)
				.toList();
	}

	@GetMapping("/{id}")		// @PathVariable holt Wert aus URL - GET /api/brands/12
								// -> @PathVariable Long id ergibt id = 12
	public ResponseEntity<BrandResponse> getById(@PathVariable Long id) {
		return service.findById(id)
				.map(BrandMapper::toResponse)
				.map(ResponseEntity::ok)	// wenn was drin ist, pack es in ResponseEntity.ok(...) = 200 OK
				.orElse(ResponseEntity.notFound().build());		// wenn leer, gib 404 zurück
	}

	@PostMapping
	public ResponseEntity<BrandResponse> create(@RequestBody BrandRequest request) {
								// JSON-Body in Java-object - Spring-Magic

		Brand created = service.create(BrandMapper.toEntity(null, request));

		BrandResponse response = BrandMapper.toResponse(created);

		return ResponseEntity.created(URI.create("/api/brands/" + created.getId())).body(response);
				// setzt Statuscode 201 Created + Location-Header				packt Response-DTO als JSON in body
	}

	@PutMapping("/{id}")
	public ResponseEntity<BrandResponse> update(@PathVariable Long id,
			@RequestBody BrandRequest request) {

		return service.update(id, BrandMapper.toEntity(id, request))
				.map(BrandMapper::toResponse)
				.map(ResponseEntity::ok) // 200
				.orElse(ResponseEntity.notFound().build()); // 404
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build(); // 204
		}
		return ResponseEntity.notFound().build(); // 404
	}
}
