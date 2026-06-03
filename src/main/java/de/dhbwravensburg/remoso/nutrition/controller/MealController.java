package de.dhbwravensburg.remoso.nutrition.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.dhbwravensburg.remoso.nutrition.dto.MealRequest;
import de.dhbwravensburg.remoso.nutrition.dto.MealResponse;
import de.dhbwravensburg.remoso.nutrition.mapper.MealMapper;
import de.dhbwravensburg.remoso.nutrition.model.Meal;
import de.dhbwravensburg.remoso.nutrition.service.MealService;

/**
 * Katrin Schaake, TIA25 – Version: 0.2
 *
 * REST-Controller für Mahlzeiten.
 *
 * Endpunkte:
 *   GET    /api/meals          -> alle Mahlzeiten
 *   GET    /api/meals/{id}     -> eine Mahlzeit mit berechneten Nährwerten
 *   GET    /api/meals/category -> alle Mahlzeiten aus der Kategorie
 *   GET    /api/meals/search   -> alle Mahlzeiten, die Suche enthalten
 *   POST   /api/meals          -> neue Mahlzeit anlegen
 *   PUT    /api/meals/{id}     -> Mahlzeit komplett ersetzen
 *   DELETE /api/meals/{id}     -> Mahlzeit löschen
 *
 * Der Controller selbst rechnet NICHTS.
 * Er ruft den Service auf, mappt das Ergebnis und gibt es zurück.
 * Berechnung passiert im Model (MealItem, Meal).
 * Mapping passiert im MealMapper.
 */
@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService service;

    // Konstruktor-Injection (wie bei BrandController und ProductController)
    public MealController(MealService service) {
        this.service = service;
    }

    // GET /api/meals
    @GetMapping
    public List<MealResponse> getAll() {
        return service.findAll().stream()
                .map(MealMapper::toResponse)
                .toList();
    }

    // GET /api/meals/1
    // -> gibt Mahlzeit zurück mit totalCalories und totalProtein bereits berechnet
    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(MealMapper::toResponse)
                .map(ResponseEntity::ok)                        // 200 OK
                .orElse(ResponseEntity.notFound().build());     // 404 Not Found
    }

    @GetMapping("/category")
    public List<MealResponse> findByCategory(@RequestParam String category) {
        return service.findByCategory(category).stream()
                .map(MealMapper::toResponse)
                .toList();
    }

    @GetMapping("/search")
    public List<MealResponse> searchByName(@RequestParam String name) {
        return service.searchByName(name).stream()
                .map(MealMapper::toResponse)
                .toList();
    }

    // POST /api/meals
    // Body-Beispiel:
    // {
    //   "category": "Mittagessen",
    //   "name": "Nudeln mit Hackfleischsoße",
    //   "items": [
    //     { "productId": 1, "amountGrams": 150.0 }
    //   ]
    // }
    @PostMapping
    public ResponseEntity<MealResponse> create(@RequestBody MealRequest request) {

        Meal created = service.create(request);
        MealResponse response = MealMapper.toResponse(created);

        return ResponseEntity
                .created(URI.create("/api/meals/" + created.getId()))
                .body(response);
    }

    // PUT /api/meals/1
    @PutMapping("/{id}")
    public ResponseEntity<MealResponse> update(
            @PathVariable Long id,
            @RequestBody MealRequest request) {

        Meal updated = service.update(id, request);
        return ResponseEntity.ok(MealMapper.toResponse(updated));
    }

    // DELETE /api/meals/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
