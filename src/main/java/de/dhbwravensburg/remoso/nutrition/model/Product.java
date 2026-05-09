//Product.java
package de.dhbwravensburg.remoso.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25  -  Samstag, 02.05.2026, Version: 0.2
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	private Long id;
	private String barcode;
	private String name;
	private Brand brand;
	private double caloriesKcal;	// kcal / 100g
	private double protein;			// g / 100g
	private double carbs;			// g / 100g

}
