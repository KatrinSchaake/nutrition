package de.dhbwravensburg.remoso.nutrition.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25 – Version: 0.2
 *
 * Eine Mahlzeit hat:
 *      - category: z.B. Frühstück, Mittagessen, Snack
 *      - name: "Shake  mit Kokosmilch", "Butterbrezel mit Joghurt"
 *      - lastModified: wird von JPA automatisch gesetzt (manuell nicht nötig)
 *
 * @PrePersist / @PreUpdate
 * JPA-Lifecycle-Callbacks, direkt bevor JPA ein INSERT oder UPDATE in die DB schreibt, ruft es
 * diese Methode auf -> lastModified immer aktuell, ohne dass Service o. Controller sich darum kümmern müssen
 */
@Data
@NoArgsConstructor
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String name;
    private LocalDateTime lastModified;

    @OneToMany(mappedBy = "meal",   // Fremdschlpssel
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    // ArrayList, nicht List.of() – wir wollen Items hinzufügen können
    private List<MealItem> items = new ArrayList<>();

    // -----Konstruktor mit 2 Argumenten-----
    public Meal(String category, String name) { this.category = category; this.name = name; }

    // -----JPA Lifecycle: lastModified automatisch setzen-----
    @PrePersist                 // wird VOR dem ersten Speichern aufgerufen
    @PreUpdate                  // wird VOR jedem  Update aufgerufen
    protected void onSave() {
        this.lastModified = LocalDateTime.now();
    }

    // -----bidirektionale Beziehung-----
    public void addItem(MealItem item) {
        items.add(item);
        item.setMeal(this);
    }
    public void removeItem(MealItem item) {
        items.remove(item);
        item.setMeal(null);
    }

    // -----berechnete Gesamtwerte-----

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

    public double totalCarbs() {
        return items.stream()
                .mapToDouble(MealItem::totalCarbs)
                .sum();
    }
}
