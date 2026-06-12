package de.dhbwravensburg.remoso.nutrition.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwravensburg.remoso.nutrition.dto.BrandRequest;
import de.dhbwravensburg.remoso.nutrition.dto.BrandResponse;
import de.dhbwravensburg.remoso.nutrition.mapper.BrandMapper;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.service.BrandService;
import jakarta.validation.Valid;

/**
 * Katrin Schaake, TIA25, Sonnabend, 30.05.2026, Version: 0.3
 *
 * Controller gibt keine 404 selbst zurück -> GlobalExceptionHandler, wenn Service
 * eine ResourceNotFoundException wirft
 */
@RestController								// diese Klasse ist ein REST-Controller, alle Methoden geben JSON
@RequestMapping("/api/brands")
public class BrandController {

	private final BrandService service;		// ein Feld

	//Konstruktor mit dem Service als Parameter
	public BrandController(BrandService service) {
		this.service = service;
	}

	@GetMapping
	public List<BrandResponse> getAll() {
		return service.findAll().stream()
				.map(BrandMapper::toResponse)
				.toList();
	}

	@GetMapping("/{id}")					// @PathVariable holt Wert aus URL - GET /api/brands/12
											// -> @PathVariable Long id ergibt id = 12
	public ResponseEntity<BrandResponse> getById(@PathVariable Long id) {
		return service.findById(id)
				.map(BrandMapper::toResponse)
				.map(ResponseEntity::ok)						// wenn was drin, pack es in ResponseEntity.ok(...) = 200 OK
				.orElse(ResponseEntity.notFound().build());		// wenn leer, gib 404 zurück
	}

	//GET /api/brands/search?name=bau		-> findet "Bauer"
	@GetMapping("/search")
	public List<BrandResponse> searchByName(@RequestParam String name) {
		return service.searchByName(name).stream()
				.map(BrandMapper::toResponse)
				.toList();
	}

	@PostMapping
	public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest request) {
								// JSON-Body in Java-object - Spring-Magic, sonst wirkt das nicht aus den DTOs

		Brand created = service.create(BrandMapper.toEntity(null, request));
		BrandResponse response = BrandMapper.toResponse(created);

		return ResponseEntity.created(URI.create("/api/brands/" + created.getId())).body(response);
				// setzt Statuscode 201 Created + Location-Header		->	Response-DTO als JSON in body
	}

	@PutMapping("/{id}")
	public ResponseEntity<BrandResponse> update(@PathVariable Long id,
			@Valid @RequestBody BrandRequest request) {

		Brand updated = service.update(id, BrandMapper.toEntity(id, request));
		return ResponseEntity.ok(BrandMapper.toResponse(updated));
	}

	// Service wirft 404 oder 409 - Controller macht nur Aufruf
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)			//204 wenn alles klappt
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
