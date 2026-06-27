package de.dhbwravensburg.remoso.nutrition.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts.OpenFoodFactsNutriments;
import de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts.OpenFoodFactsProduct;
import de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts.OpenFoodFactsResponse;
import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25, Freitag, 26.06.2026, Version: 0.1
 *
 * Testet den OpenFoodFactsMapper - reine Mapping-Logik, KEIN Netzwerk,
 * Fake-API-Antwort -> Umwandlung in ein Product prüfen
 */
class OpenFoodFactsMapperTest {

	@Test
	void toProduct_mapsAllFieldsCorrectly() {
		// Arrange: Fake-API-Antwort mit vollstaendigen Daten
		OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments(366.0, 3.2, 12.0);
		OpenFoodFactsProduct product = new OpenFoodFactsProduct("Kirschjoghurt", "Bauer", nutriments);
		OpenFoodFactsResponse response = new OpenFoodFactsResponse("4002334113032", product, 1, "product found");

		// Act
		Product result = OpenFoodFactsMapper.toProduct(response);

		// Assert
		assertEquals("4002334113032", result.getBarcode());
		assertEquals("Kirschjoghurt", result.getName());
		assertEquals(366.0, result.getCaloriesKcal(), 0.0001);
		assertEquals(3.2, result.getProtein(), 0.0001);
		assertEquals(12.0, result.getCarbs(), 0.0001);
		assertNull(result.getBrand());   // Brand wird laut Mapper bewusst NICHT gesetzt
	}

	@Test
	void toProduct_handlesNullNutrimentsAsZero() {
		// Arrange: API liefert null für alle Nährwerte
		OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments(null, null, null);
		OpenFoodFactsProduct product = new OpenFoodFactsProduct("Unbekannt", "TestBrand", nutriments);
		OpenFoodFactsResponse response = new OpenFoodFactsResponse("000", product, 1, "product found");

		// Act
		Product result = OpenFoodFactsMapper.toProduct(response);

		// Assert: null soll zu 0.0 werden, nicht crashen
		assertEquals(0.0, result.getCaloriesKcal(), 0.0001);
		assertEquals(0.0, result.getProtein(), 0.0001);
		assertEquals(0.0, result.getCarbs(), 0.0001);
	}

	@Test
	void extractBrandName_returnsFirstBrandFromCommaList() {
		// Act & Assert: "Bauer, Der große Bauer" -> "Bauer"
		String result = OpenFoodFactsMapper.extractBrandName("Bauer, Der große Bauer");
		assertEquals("Bauer", result);
	}

	@Test
	void extractBrandName_returnsNullForBlankInput() {
		// Act & Assert: leerer String -> null
		assertNull(OpenFoodFactsMapper.extractBrandName("   "));
	}
}
