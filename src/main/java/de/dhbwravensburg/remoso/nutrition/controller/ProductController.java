package de.dhbwravensburg.remoso.nutrition.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwravensburg.remoso.nutrition.dto.ProductRequest;
import de.dhbwravensburg.remoso.nutrition.dto.ProductResponse;
import de.dhbwravensburg.remoso.nutrition.mapper.ProductMapper;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;
import de.dhbwravensburg.remoso.nutrition.service.ProductService;

/**
 * Katrin Schaake, TIA25  -  Sonnabend, 30.05.2026, Version: 0.3
 */

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}			// wie bei BrandController

	// GET /api/products			-> alle Prosukte
	// GET /api/products?brandId=2	-> nur Produkte dieser Brand
	@GetMapping
	public List<ProductResponse> getAll(@RequestParam(required = false) Long brandId) {		//optionaler Filter, z.B. ?brandId=2

		List<Product> products;
		if (brandId != null) {
			products = service.findByBrandId(brandId);				// nutzt Derived Query
		} else {
			products = service.findAll();
		}
		return products.stream()
				.map(ProductMapper::toResponse)
				.toList();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
		return service.findById(id)
				.map(ProductMapper::toResponse)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// GET /api/products/search?name=joghurt						-> findet "Kirschjoghurt"
	@GetMapping("/search")
	public List<ProductResponse> searchByName(@RequestParam String name) {
		return service.searchByName(name).stream()
				.map(ProductMapper::toResponse)
				.toList();
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {

		// brandId aus Request zur echten Brand-Entity auflösen
		Brand brand = service.resolveBrand(request.brandId()).orElse(null);
		Product created = service.create(ProductMapper.toEntity(null, request, brand));
		ProductResponse response = ProductMapper.toResponse(created);

		return ResponseEntity.created(URI.create("/api/products/" + created.getId())).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable Long id,
			@RequestBody ProductRequest request) {

		Brand brand = service.resolveBrand(request.brandId()).orElse(null);

		return service.update(id, ProductMapper.toEntity(id, request, brand))
				.map(ProductMapper::toResponse)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build();	// 204
		}
		return ResponseEntity.notFound().build();		// 404
	}
}
