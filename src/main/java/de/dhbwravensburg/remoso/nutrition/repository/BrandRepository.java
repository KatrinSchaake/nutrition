// BrandRepository.java
package de.dhbwravensburg.remoso.nutrition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.dhbwravensburg.remoso.nutrition.model.Brand;

/**
 * Katrin Schaake, TIA25 – Version: 0.1
 *
 * Repository für Brand.
 *
 * Interface, keine Klasse: Spring Data JPA generiert zur Laufzeit
 * automatisch eine Implementierung mit allen CRUD-Methoden.
 *
 * <Brand, Long>:
 *   - Brand = die Entity, mit der dieses Repository arbeitet
 *   - Long  = der Typ des Primärschlüssels (siehe @Id in Brand.java)
 *
 * Du bekommst kostenlos:
 *   findAll(), findById(id), save(entity), deleteById(id), count(), existsById(id) ...
 */
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
