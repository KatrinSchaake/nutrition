// MealRepository.java
package de.dhbwravensburg.remoso.nutrition.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.dhbwravensburg.remoso.nutrition.model.Meal;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * Repository für Meal.
 *
 * MealItem braucht KEIN eigenes Repository:
 *   Items werden automatisch mit ihrer Meal gespeichert/gelöscht
 *   dank cascade = ALL und orphanRemoval = true (siehe Meal.java).
 *
 * Derived Queries:
 *   findByDate(...)       -> SELECT * FROM meal WHERE date = ?
 *   findByDateBetween(...)-> SELECT * FROM meal WHERE date BETWEEN ? AND ?
 *
 *   "Between" ist eines der vielen Schlüsselwörter, die Spring Data
 *   erkennt — siehe auch: Containing, IgnoreCase, GreaterThan, OrderBy...
 */
public interface MealRepository extends JpaRepository<Meal, Long> {

    // alle Mahlzeiten an einem bestimmten Tag
    List<Meal> findByDate(LocalDate date);

    // alle Mahlzeiten in einem Zeitraum (z.B. eine Woche)
    List<Meal> findByDateBetween(LocalDate start, LocalDate end);
}
