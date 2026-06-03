package de.dhbwravensburg.remoso.nutrition.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25, Mittwoch, 06.05.2026, Version: 0.1
 */

@Data
@NoArgsConstructor		// JPA braucht leeren Konstruktor
@AllArgsConstructor
@Entity					// = diese Klasse gehört zu einer Tabelle namens "brand"
public class Brand {

	@Id					// Primärschlüssel
	@GeneratedValue(strategy = GenerationType.IDENTITY)
						// hier ohne @OneToMany - schon in ProductRepository.findByBrandID()
	private Long id;	// Long instead of int - no primitive data type, null available
	private String name;
	private String countries;  //in API also countries instead of country
}
