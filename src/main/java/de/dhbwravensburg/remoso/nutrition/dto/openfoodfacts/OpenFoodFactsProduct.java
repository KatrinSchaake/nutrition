package de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Das "product"-Objekt innerhalb der API-Antwort.
 *
 * Beispiel (gekürzt):
 * {
 *   "product_name": "Kirsche",
 *   "brands": "Bauer, Der große Bauer",
 *   "nutriments": { ... }
 * }
 *
 * @JsonProperty("product_name"):
 *   Die API nutzt snake_case ("product_name"), Java camelCase.
 *   @JsonProperty: "Wenn du 'product_name' im JSON siehst -> pack Wert in dieses Feld"
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenFoodFactsProduct(

		@JsonProperty("product_name")
		String productName,
		String brands,                      // z.B. "Bauer, Der große Bauer"
		OpenFoodFactsNutriments nutriments
) {}
