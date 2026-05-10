//Brand.java
package de.dhbwravensburg.remoso.nutrition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Katrin Schaake, TIA25, Mittwoch, 06.05.2026, Version: 0.1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

	private Long id;	// Long instead of int - no primitive data type, null available
	private String name;
	private String countries;  //in API also contries instead of coutry

}
