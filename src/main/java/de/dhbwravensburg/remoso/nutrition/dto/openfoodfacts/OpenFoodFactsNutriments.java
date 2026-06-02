package de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Die Nährwert-Daten aus der API-Antwort.
 *
 * Problem: Die API nutzt Feld-Namen mit Bindestrichen: "energy-kcal_100g": 88
 *
 * In Java Variablenname ohne Bindestrich!
 * Lösung: @JsonProperty("energy-kcal_100g") mappt den JSON-Namen auf gültigen Java-Namen
 *
 * nur drei Werte werden gebraucht:
 *   - energy-kcal_100g		->	caloriesKcal (kcal pro 100g)
 *   - proteins_100g		->	protein (g pro 100g)
 *   - carbohydrates_100g	->	carbs (g pro 100g)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenFoodFactsNutriments(

		@JsonProperty("energy-kcal_100g")
		Double energyKcal100g,

		@JsonProperty("proteine_100g")
		Double protein100g,

		@JsonProperty("carbohydrates_100g")
		Double carbs100g
) {}
