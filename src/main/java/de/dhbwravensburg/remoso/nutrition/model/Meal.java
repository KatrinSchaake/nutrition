// Meal.java
package de.dhbwravensburg.remoso.nutrition.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * Eine Mahlzeit besteht aus einem Namen, einem Datum
 * und einer Liste von MealItems (= Produkt + Menge).
 *
 * Warum List statt Set?
 *   -> Man könnte dasselbe Produkt zweimal hinzufügen wollen
 *      (z.B. zwei separate Portionen), deshalb keine Duplikat-Sperre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    private Long id;

    private String name;            // z.B. "Frühstück" oder "Post-Workout"

    private LocalDate date;         // wann wurde die Mahlzeit gegessen?

    // ArrayList, nicht List.of() – wir wollen Items hinzufügen können
    private List<MealItem> items = new ArrayList<>();

    // ---- Convenience-Konstruktor ohne Items (für neue, leere Mahlzeiten) ---
    public Meal(Long id, String name, LocalDate date) {
        this.id   = id;
        this.name = name;
        this.date = date;
        // items bleibt die leere ArrayList von oben
    }

    // ---- berechnete Gesamtwerte ------------------------------------------

    /**
     * Summiert die Kalorien aller enthaltenen Items.
     * Jedes Item weiß selbst, wie es rechnet (Delegationsprinzip).
     */
    public double totalCalories() {
        return items.stream()
                .mapToDouble(MealItem::totalCalories)
                .sum();
    }

    /**
     * Summiert das Eiweiß aller enthaltenen Items.
     */
    public double totalProtein() {
        return items.stream()
                .mapToDouble(MealItem::totalProtein)
                .sum();
    }
}
