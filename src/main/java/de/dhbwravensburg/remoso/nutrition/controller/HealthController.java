// HealthController.java
package de.dhbwravensburg.remoso.nutrition.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Katrin Schaake, TIA25  -  Sonntag, 03.05.2026, Version: 0.1
 */

@RestController
public class HealthController {

	@GetMapping("/api/health")
	public Map<String, String> health() {

		return Map.of(
				"status", "UP",
				"app", "Nutrition",
				"version", "0.1.0"
		);
	}
}
