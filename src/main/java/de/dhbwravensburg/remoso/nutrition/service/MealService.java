package de.dhbwravensburg.remoso.nutrition.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbwravensburg.remoso.nutrition.dto.MealItemRequest;
import de.dhbwravensburg.remoso.nutrition.dto.MealRequest;
import de.dhbwravensburg.remoso.nutrition.model.Meal;
import de.dhbwravensburg.remoso.nutrition.model.MealItem;
import de.dhbwravensburg.remoso.nutrition.model.Product;
import de.dhbwravensburg.remoso.nutrition.repository.MealRepository;

/**
 * Katrin Schaake, TIA25 – Version: 0.3
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

    private final MealRepository repository;
    private final ProductService productService;    // brauchen wir zum Auflösen der productIds

    public MealService(MealRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    // -----CRUD-----

    public List<Meal> findAll() {
        return repository.findAll();
    }

    public Optional<Meal> findById(Long id) {
        return repository.findById(id);
    }

    // alle Mahlzeiten einer Kategorie (z.B. alle Frühstücke)
    public List<Meal> findByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }

    // Mahlzeiten nach Name suchen (Teilstring)
    public List<Meal> searchByName(String namePart) {
        return repository.findByNameContainingIgnoreCase(namePart);
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

        Meal meal = new Meal(request.category(), request.name());
        // lastModified automatisch von @PrePersist gesetzt

        addItemsToMeal(meal, request.items());

        return repository.save(meal);
    }

    /**
     * Ersetzt eine bestehende Mahlzeit komplett (PUT-Semantik).
     * Gibt Optional.empty() zurück, wenn die ID nicht existiert.
     */
    public Optional<Meal> update(Long id, MealRequest request) {

        return repository.findById(id).map(existing -> {

            existing.setCategory(request.category());
            existing.setName(request.name());

            existing.getItems().clear();
            addItemsToMeal(existing, request.items());

            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;

    }

    // ---- private Hilfsmethode ------------------------------------------
    /**
     * Baut aus einer Liste von MealItemRequests eine Liste von MealItems.
     * Items mit unbekannter productId werden still übersprungen.
     */
    private void addItemsToMeal(Meal meal, List<MealItemRequest> itemRequests) {
        if (itemRequests == null) {
            return;
        }
        for (MealItemRequest itemRequest : itemRequests) {

            Optional<Product> productOpt = productService.findById(itemRequest.productId());
            productOpt.ifPresent(product -> {
                MealItem item = new MealItem(product, itemRequest.amountGrams());
                meal.addItem(item);
            });
        }
    }
}
