package de.dhbwravensburg.remoso.nutrition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Katrin Schaake, TIA25, Mittwoch, 24.06.2026, Version: 0.1
 *
 * Testet Summenbildung über mehrere MealItems in einem Meal. Anders als MealItemTest (1 Item)
 * wird hier das Aufaddieren mehrerer Items + Methode addItem() getestet.
 */
class MealTest {

	@Test
	void totalCalories_sumsAllItems() {
		Product skyr = new Product();
		skyr.setCaloriesKcal(63.0);

		Product berries = new Product();
		berries.setCaloriesKcal(46.0);

		Meal meal = new Meal("Frühstück", "Skyr mit Beeren");
		meal.addItem(new MealItem(skyr, 200.0)); // 63/100 * 200 = 126.0 kcal
		meal.addItem(new MealItem(berries, 80.0)); // 46/100 * 80 = 36.8 kcal

		double result = meal.totalCalories();

		assertEquals(162.8, result, 0.0001);
	}

	@Test
	void totalCalories_isZeroForEmptyMeal() {
		Meal meal = new Meal("NachNaschi", "Leer");

		double result = meal.totalCalories();

		assertEquals(0.0, result, 0.0001);
	}

}
