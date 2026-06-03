package de.dhbwravensburg.remoso.nutrition.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwravensburg.remoso.nutrition.dto.ProductResponse;
import de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts.OpenFoodFactsResponse;
import de.dhbwravensburg.remoso.nutrition.mapper.OpenFoodFactsMapper;
import de.dhbwravensburg.remoso.nutrition.mapper.ProductMapper;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;
import de.dhbwravensburg.remoso.nutrition.service.BrandService;
import de.dhbwravensburg.remoso.nutrition.service.OpenFoodFactsService;
import de.dhbwravensburg.remoso.nutrition.service.ProductService;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Controller für Barcode-Lookup über OpenFoodFacts-API
 *
 *   /api/barcode/{code}		-> zeigt Produktdaten von OpenFoodFacts
 *   /api/barcode/{code}/import	-> speichert Produkt in DB
 *
 * Zwei Endpunkte, weil:
 *   1. Lookup: "Zeig mir was die API über diesen Barcode weiß" (nur lesen)
 *   2. Import: "Speichere dieses Produkt in meiner Datenbank" (schreiben)
 *   -> Nutzer kann erst schauen, was kommt, vor Import
 */
@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

	private final OpenFoodFactsService openFoodFactsService;
	private final ProductService productService;
	private final BrandService brandService;

	public BarcodeController(OpenFoodFactsService openFoodFactsService,
			ProductService productService, BrandService brandService) {
		this.openFoodFactsService = openFoodFactsService;
		this.productService = productService;
		this.brandService = brandService;
	}

	/**
	 * GET /api/barcode/4002334113032
	 * Schaut bei OpenFoodFacts nach und zeigt Ergebnis an, KEINE Speicherung in DB
	 * Gibt 404 zurück, wenn Barcode bei OpenFoodFacts nicht existiert
	 */
	@GetMapping("/{code}")
	public ResponseEntity<ProductResponse> lookup(@PathVariable String code) {

		OpenFoodFactsResponse apiResponse = openFoodFactsService.lookupByBarcode(code);

		if (apiResponse == null) { return ResponseEntity.notFound().build(); }	// 404

		Product product = OpenFoodFactsMapper.toProduct(apiResponse);

		// Brand-Name extrahieren und als temporäre Brand anzeigen
		String brandName = OpenFoodFactsMapper.extractBrandName(
				apiResponse.product().brands());
		if (brandName != null) {
			product.setBrand(new Brand(null, brandName, null));
		}
		return ResponseEntity.ok(ProductMapper.toResponse(product));
	}
	/**
	 * POST /api/barcode/4002334113032/import
	 *
	 * Holt Produkt von OpenFoodFacts UND speichert in DB
	 * Ablauf:
	 * 1. API aufrufen
	 * 2. Brand-Name extrahieren
	 * 3. Prüfen ob Brand schon existiert, sonst neu anlegen
	 * 4. Product erstellen und speichern
	 *
	 * Gibt 404 zurück, wenn Barcode nicht existiert
	 * Gibt 201 (Created) zurück, wenn erfolgreich importiert
	 */
	@PostMapping("/{code}/import")
	public ResponseEntity<ProductResponse> importProduct(@PathVariable String code) {

		// 1. API aufrufen
		OpenFoodFactsResponse apiResponse = openFoodFactsService.lookupByBarcode(code);
		if (apiResponse == null) {
			return ResponseEntity.notFound().build();   // 404
		}
		// 2. Product aus API-Daten bauen
		Product product = OpenFoodFactsMapper.toProduct(apiResponse);

		// 3. Brand auflösen oder neu anlegen
		String brandName = OpenFoodFactsMapper.extractBrandName(
				apiResponse.product().brands());
		if (brandName != null) {
			// Gibt es die Brand schon? Suche per Name
			Brand brand = brandService.searchByName(brandName).stream()
					.findFirst()
					.orElseGet(() -> brandService.create(new Brand(null, brandName, null)));
			product.setBrand(brand);
		}
		// 4. Produkt speichern
		Product saved = productService.create(product);

		return ResponseEntity
				.status(201)
				.body(ProductMapper.toResponse(saved));
	}
}
