package de.dhbwravensburg.remoso.nutrition.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25  -  Samstag, 02.05.2026, Version: 0.2
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String barcode;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)	// Beziehung zu Brand
	@JoinColumn(name = "brand_id")		// Name der Fremdschlüssel-Spalte in DB
	private Brand brand;
	private double caloriesKcal;	// kcal / 100g
	private double protein;			// g / 100g
	private double carbs;			// g / 100g

}
