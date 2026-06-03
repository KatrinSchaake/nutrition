package de.dhbwravensburg.remoso.nutrition.mapper;

import de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts.OpenFoodFactsResponse;
import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Mappt OpenFoodFacts-API-Antwort auf Product-Entity
 *
 * Brand NICHT gesetzt (bleibt null)
 *   Die API liefert nur einen Brand-String, aber Brand-Entity benötigt aus DB
 *   -> Auflösen (String -> Entity) ist Aufgabe des Controllers/Service
 */
public final class OpenFoodFactsMapper {

	private OpenFoodFactsMapper() {}

	public static Product toProduct(OpenFoodFactsResponse response) {

		/**
		 * Mappt die API-Antwort auf ein Product
		 *
		 * @param response die API-Antwort (darf nicht null sein)
		 * @return ein Product ohne ID (id=null) und ohne Brand (brand=null)
		 */
		return new Product(

				null,								// id - wird von JPA vergeben
				response.code(),					// barcode
				response.product().productName(),	// Product name
				null,								// Brand - wird später gesetzt
				response.product().nutriments().energyKcal100g() == null
						? 0.0 : response.product().nutriments().energyKcal100g(),	// caloriesKcal
				response.product().nutriments().protein100g() == null
						? 0.0 : response.product().nutriments().protein100g(),		// protein
				response.product().nutriments().carbs100g() == null ? 0.0 : response.product().nutriments().carbs100g()			// carbs
		);
	}

	/**
	 * Extrahiert den 1. Brand-Namen aus dem API-String
	 *
	 * API liefert z.B. "Bauer, Der große Bauer": komma-getrennter String mit
	 * mehreren Marken-Varianten -> Nutzung des Ersten
	 *
	 * @param brands der Brand-String aus API (kann null sein)
	 * @return der erste Brand-Name, oder null
	 */
	public static String extractBrandName(String brands) {
		if (brands == null || brands.isBlank()) {
			return null;
		}
		// "Bauer, Der große Bauer" -> "Bauer"
		return brands.split(",")[0].trim();
	}
}
