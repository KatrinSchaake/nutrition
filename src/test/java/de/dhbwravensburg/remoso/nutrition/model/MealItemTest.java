package de.dhbwravensburg.remoso.nutrition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Katrin Schaake, TIA25, Mittwoch, 24.06.2026, Version: 0.1
 */
class MealItemTest {

	@Test
	void totalCalories_calcCorrectlyForGivenAmount(){
		// Arrange: Produkt mit 50kcel/100g -> davon 200g
		Product  product = new Product();
		product.setCaloriesKcal(50.0);

		MealItem mealItem = new MealItem(product, 200.0);

		// Act
		double result = mealItem.totalCalories();

		//assertEquals(erwartet, tatsächlich, delta) -> delta wegen Ungenauigkeit Fließkommazahl
		// Assert: 50 / 100 * 200 = 100
		assertEquals(100.0, result, 0.0001);
	}

	@Test
	void totalProtein_calcCorrectlyForGivenAmount(){
		Product  product = new Product();
		product.setProtein(12.45);

		MealItem mealItem = new MealItem(product, 200.0);

		double result = mealItem.totalProtein();

		assertEquals(24.9, result, 0.0001);
	}

	@Test
	void totalCarbs_calcCorrectlyForGivenAmount(){
		Product  product = new Product();
		product.setCarbs(33.3);
		MealItem mealItem = new MealItem(product, 200.0);
		double result = mealItem.totalCarbs();
		assertEquals(66.6, result, 0.0001);
	}
}
