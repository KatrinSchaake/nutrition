// MealMapper.java
package de.dhbwravensburg.remoso.nutrition.mapper;

import java.util.List;

import de.dhbwravensburg.remoso.nutrition.dto.MealItemResponse;
import de.dhbwravensburg.remoso.nutrition.dto.MealResponse;
import de.dhbwravensburg.remoso.nutrition.model.Meal;
import de.dhbwravensburg.remoso.nutrition.model.MealItem;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * Mapper: wandelt Meal/MealItem (Model) in Response-DTOs um.
 *
 * toEntity() fehlt hier bewusst:
 *   Das Erstellen einer Meal-Entity aus einem MealRequest
 *   braucht Zugriff auf den ProductService (um productId aufzulösen).
 *   Das ist Aufgabe des MealService, nicht des Mappers.
 *   Der Mapper kennt keine Services – er ist nur ein "Übersetzer".
 */
public final class MealMapper {

    private MealMapper() {}

    /**
     * Wandelt ein MealItem in ein MealItemResponse um.
     * Dabei werden die berechneten Werte (totalCalories, totalProtein)
     * direkt aus dem Model-Objekt geholt – MealItem kann das selbst rechnen.
     */
    public static MealItemResponse toItemResponse(MealItem item) {
        return new MealItemResponse(
                item.getId(),
                ProductMapper.toResponse(item.getProduct()),    // verschachtelt
                item.getAmountGrams(),
                item.totalCalories(),   // Methode aus dem Model
                item.totalProtein(),    // Methode aus dem Model
                item.totalCarbs()
        );
    }

    /**
     * Wandelt eine Meal-Entity in ein MealResponse um.
     * Alle Items werden einzeln gemappt, Gesamtwerte aus dem Model geholt.
     */
    public static MealResponse toResponse(Meal meal) {

        List<MealItemResponse> itemResponses = meal.getItems().stream()
                .map(MealMapper::toItemResponse)
                .toList();

        return new MealResponse(
                meal.getId(),
                meal.getName(),
                meal.getDate(),
                itemResponses,
                meal.totalCalories(),   // Summe aller Items
                meal.totalProtein(),     // Summe aller Items
                meal.totalCarbs()
        );
    }
}
