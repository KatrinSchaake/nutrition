package de.dhbwravensburg.remoso.nutrition.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25, Mittwoch, 24.06.2026, Version: 0.1
 *
 * @DataJpaTest startet NUR JPA-Schicht mit einer eigenen In-Memory-H2, jeder Test läuft in
 * einer Transaktion, die danach zurückgerollt wird -> saubere DB für jeden Test
 */
@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;

	@Test
	void findByName_findsPartialMatchIgnoringCase() {
		// Arrange: drei Produkte speichern
		repository.save(new Product(null, "111", "Kirschjoghurt", null, 80.0, 3.0, 12.0));
		repository.save(new Product(null, "222", "Naturjoghurt", null, 60.0, 4.0, 5.0));
		repository.save(new Product(null, "333", "Butter", null, 740.0, 0.6, 0.5));

		// Act: Suche nach "joghurt" (klein geschrieben)
		List<Product> result = repository.findByNameIsContainingIgnoreCase("joghurt");

		// Assert: beide Joghurt-Produkte werden gefunden, Butter nicht
		assertEquals(2, result.size());
	}

	@Test
	void findByName_returnsEmptyListWhenNoMatch() {
		repository.save(new Product(null, "444", "Ananas", null, 58.0, 3.0, 12.0));

		List<Product> result = repository.findByNameIsContainingIgnoreCase("schokolade");

		assertTrue(result.isEmpty());
	}

}
