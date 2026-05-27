// MealItem.java
package de.dhbwravensburg.remoso.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * Verbindungsobjekt zwischen Meal und Product.
 * Speichert, wie viele Gramm von einem bestimmten Produkt
 * in einer bestimmten Mahlzeit stecken.
 *
 * Warum eine eigene Klasse statt nur einer Liste<Product>?
 *   -> Wir brauchen die MENGE (amountGrams), und die gehört
 *      weder zu Meal noch zu Product allein, sondern zur Verbindung.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealItem {

    private Long id;

    private Product product;    // welches Produkt

    private double amountGrams; // wie viele Gramm davon

    // ---- berechnete Hilfsmethoden --------------------------------

    /**
     * Liefert die Kalorien dieses Items.
     * product.caloriesKcal ist pro 100 g angegeben, daher / 100 * amountGrams.
     */
    public double totalCalories() {
        return product.getCaloriesKcal() / 100.0 * amountGrams;
    }

    /**
     * Liefert das Eiweiß dieses Items (ebenfalls pro 100 g im Produkt).
     */
    public double totalProtein() {
        return product.getProtein() / 100.0 * amountGrams;
    }
}
