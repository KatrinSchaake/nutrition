package de.dhbwravensburg.remoso.nutrition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.dhbwravensburg.remoso.nutrition.model.Meal;

/**
 * Katrin Schaake, TIA25 – Version: 0.2
 *
 * Derived Queries:
 *
 *      findCategory("Frühstück")
 *      -> SELECT * FROM meal WHERE category = 'Frühstück'
 *      -> alle Frühstücks-Varianten
 *
 *      findByNameContainingIgnoreCase("shake")
 *      -> SELECT * FROM meal WHERE LOWER(name) LIKE '%shake%'
 *      -> sucht Mahlzeiten deren Name "shake" enthält, Groß-klein-Schreibung egal
 *
 *      findByCategoryIgnoreCase("frühstück")
 *      -> wie findByCategorx, aber Groß-klein-Schreibung egal
 */
public interface MealRepository extends JpaRepository<Meal, Long> {

    // alle Mahlzeiten einer Kategorie (exakte Suche)
    List<Meal> findByCategoryIgnoreCase(String category);

    // Mahlzeiten über Name suchen (Teilstring, case-insensitive)
    List<Meal> findByNameContainingIgnoreCase(String namePart);
}
