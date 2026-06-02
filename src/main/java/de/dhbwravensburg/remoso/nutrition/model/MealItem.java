package de.dhbwravensburg.remoso.nutrition.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;    // welches Produkt
    private double amountGrams; // wie viele Gramm davon

    // Konstruktor ohne id und meal
    // id vergibt JPA automatisch (@GeneratedValue)
    // meal wird von Meal.addItem() gesetzt (item.setMeal(this))
    public MealItem(Product product, double amountGrams) {
        this.product = product;
        this.amountGrams = amountGrams;
    }

    // ----berechnete Hilfsmethoden-----

    /**
     * Liefern die Kalorien, Eiweiß und Kohlenhydrate dieses Items.
     * product.caloriesKcal ist pro 100 g angegeben, daher / 100 * amountGrams.
     */
    public double totalCalories() {
        return product.getCaloriesKcal() / 100.0 * amountGrams;
    }

    public double totalProtein() {
        return product.getProtein() / 100.0 * amountGrams;
    }

    public double totalCarbs() {return product.getCarbs() / 100.0 * amountGrams;}
}
