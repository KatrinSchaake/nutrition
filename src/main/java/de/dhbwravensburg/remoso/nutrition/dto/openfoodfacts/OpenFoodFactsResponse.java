package de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Äußere Hülle der API-Antwort von OpenFoodFacts
 *
 * Beispiel (gekürzt):
 * {
 *   "code": "4002334113032",
 *   "product": { ... },
 *   "status": 1,
 *   "status_verbose": "product found"
 * }
 *
 * @JsonIgnoreProperties(ignoreUnknown = true):
 *   Die API liefert mehr Felder als gebraucht - ohne diese Annotation würde Jackson Fehler werfen,
 *   wenn ein unbekanntes Feld im JSON steht
 *   Mit der Annotation: "Ignoriere alles was du nicht kennst."
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenFoodFactsResponse(

		String code,					// der Barcode
		OpenFoodFactsProduct product,	// die Produktdaten (verschachtelt)
		int status,          			// 1 = gefunden, 0 = nicht gefunden
		String status_verbose			// "product found" oder "product not found"
) {}
