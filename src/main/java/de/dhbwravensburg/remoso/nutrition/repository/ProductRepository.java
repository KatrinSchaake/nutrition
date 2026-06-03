package de.dhbwravensburg.remoso.nutrition.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.dhbwravensburg.remoso.nutrition.model.Product;

/**
 * Katrin Schaake, TIA25 – Version: 0.3
 *
 * Repository für Product.
 *
 * Zusätzlich zu den Standard-CRUD-Methoden + "Derived Queries": Spring liest den Methodennamen u. baut das SQL
 *
 * Beispiel findByBrandId:
 *   Spring sieht "findBy" + "Brand" + "Id" und macht daraus:
 *   SELECT * FROM product WHERE brand_id = ?
 *
 * Beispiel findByBarcode:
 *   Spring sieht "findBy" + "Barcode" und macht daraus:
 *   SELECT * FROM product WHERE barcode = ?
 *
 *   Optional<Product> als Rückgabetyp, weil ein Barcode normalerweise
 *   eindeutig ist — entweder gibt's das Produkt, oder eben nicht.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // alle Produkte einer bestimmten Brand
    List<Product> findByBrandId(Long brandId);

    // ein Produkt per Barcode finden
    Optional<Product> findByBarcode(String barcode);

    // Suche nach Name (Teilstring, case-insensitive), "joghurt" findet "Kirschjoghurt", "Joghurt Natur" etc.
    List<Product> findByNameIsContainingIgnoreCase(String namePart);

    // zahlt wie viele MealItems dieses Produkt referenzieren
    @Query("SELECT COUNT(mi) FROM MealItem mi WHERE mi.product.id = :productId")
    long countMealItemsByProductId(Long productId);
}
