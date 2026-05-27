// MealService.java
package de.dhbwravensburg.remoso.nutrition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.dto.MealItemRequest;
import de.dhbwravensburg.remoso.nutrition.dto.MealRequest;
import de.dhbwravensburg.remoso.nutrition.model.Meal;
import de.dhbwravensburg.remoso.nutrition.model.MealItem;
import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * Service für Mahlzeiten.
 *
 * Zwei ID-Generatoren:
 *   mealIdGenerator    -> IDs für Meal-Objekte
 *   itemIdGenerator    -> IDs für MealItem-Objekte
 *
 * Warum braucht der Service den ProductService?
 *   -> Beim Erstellen einer Meal müssen wir für jede productId
 *      die echte Product-Entity auflösen. Das macht nur der Service,
 *      nicht der Mapper (der kennt keine anderen Services).
 */
@Service
public class MealService {

    private final ConcurrentHashMap<Long, Meal> store = new ConcurrentHashMap<>();
    private final AtomicLong mealIdGenerator = new AtomicLong(1);
    private final AtomicLong itemIdGenerator = new AtomicLong(1);

    private final ProductService productService;    // brauchen wir zum Auflösen der productIds

    public MealService(ProductService productService) {
        this.productService = productService;

        // ---- Seed-Daten: eine Beispiel-Mahlzeit anlegen ----------------
        // Wir prüfen erst, ob die Produkte existieren (IDs 1 und 2 aus ProductService-Seed)
        productService.findById(1L).ifPresent(p1 ->
                productService.findById(2L).ifPresent(p2 -> {

                    MealRequest breakfast = new MealRequest(
                            "Frühstück",
                            java.time.LocalDate.of(2026, 5, 28),
                            List.of(
                                    new MealItemRequest(1L, 150.0),   // 150g Bauer Kirsche
                                    new MealItemRequest(2L, 30.0)     // 30g Butter
                            )
                    );
                    create(breakfast);
                })
        );
    }

    // ---- CRUD ----------------------------------------------------------

    public List<Meal> findAll() {
        return List.copyOf(store.values());
    }

    public Optional<Meal> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Erstellt eine neue Mahlzeit aus einem MealRequest.
     *
     * Ablauf:
     * 1. Neue leere Meal anlegen und ID vergeben
     * 2. Für jedes Item im Request: productId -> Product auflösen
     * 3. MealItem bauen (mit ID, Product, Menge)
     * 4. Items zur Meal hinzufügen
     * 5. Meal im Store speichern
     *
     * Wenn eine productId nicht existiert, wird das Item übersprungen
     * (Optional.ifPresent). Du könntest hier auch eine Exception werfen –
     * das ist eine Design-Entscheidung.
     */
    public Meal create(MealRequest request) {

        Long newMealId = mealIdGenerator.getAndIncrement();
        Meal meal = new Meal(newMealId, request.name(), request.date());

        List<MealItem> items = buildItems(request.items());
        meal.setItems(items);

        store.put(newMealId, meal);
        return meal;
    }

    /**
     * Ersetzt eine bestehende Mahlzeit komplett (PUT-Semantik).
     * Gibt Optional.empty() zurück, wenn die ID nicht existiert.
     */
    public Optional<Meal> update(Long id, MealRequest request) {
        if (!store.containsKey(id)) {
            return Optional.empty();
        }

        Meal meal = new Meal(id, request.name(), request.date());
        List<MealItem> items = buildItems(request.items());
        meal.setItems(items);

        store.put(id, meal);
        return Optional.of(meal);
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }

    // ---- private Hilfsmethode ------------------------------------------

    /**
     * Baut aus einer Liste von MealItemRequests eine Liste von MealItems.
     * Items mit unbekannter productId werden still übersprungen.
     */
    private List<MealItem> buildItems(List<MealItemRequest> itemRequests) {
        if (itemRequests == null) {
            return new ArrayList<>();
        }

        List<MealItem> items = new ArrayList<>();
        for (MealItemRequest itemRequest : itemRequests) {

            // productId -> echte Product-Entity auflösen
            Optional<Product> productOpt = productService.findById(itemRequest.productId());

            productOpt.ifPresent(product -> {
                Long itemId = itemIdGenerator.getAndIncrement();
                items.add(new MealItem(itemId, product, itemRequest.amountGrams()));
            });
        }
        return items;
    }
}
