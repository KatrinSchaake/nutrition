package de.dhbwravensburg.remoso.nutrition.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.dhbwravensburg.remoso.nutrition.dto.MealItemRequest;
import de.dhbwravensburg.remoso.nutrition.dto.MealRequest;
import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.model.Product;
import de.dhbwravensburg.remoso.nutrition.repository.BrandRepository;
import de.dhbwravensburg.remoso.nutrition.repository.ProductRepository;
import de.dhbwravensburg.remoso.nutrition.service.MealService;

/**
 * Katrin Schaake, TIA25, Sonntag, 31.05.2026, Version: 0.1
 *
 * Legt Seed-Daten an, wenn DB leer ist - läuft genau 1x bei App-Start
 *
 * @Configuration: sagt Spring "hier kommen Bean-Definitionen"
 * @Bean CommandLineRunner: ein Interface mit run(), das Spring nach Start automat. aufruft
 *
 */
@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner seedDatabase(BrandRepository brandRepository, ProductRepository productRepository,
			MealService mealService) {

		return args -> {
			if (brandRepository.count() > 0) {
				return;
			}

			Brand olympus = brandRepository.save(new Brand(null, "Olympus", "Deutschland"));
			Brand bauer = brandRepository.save(new Brand(null, "Bauer", "Deutschland"));
			Brand weihenstephan =  brandRepository.save(new Brand(null, "Weihenstaphan", "Deutschland"));
			Brand mayers = brandRepository.save(new Brand(null, "Mayers Bäckerei", "Deutschland"));

			Product feta = productRepository.save(new Product(null, "5202178009495", "E/Feta original",
					olympus, 276, 16.5, 0.7));
			Product joghurtKirsch = productRepository.save(new Product(null, "4002334113032", "Der große Bauer - Kirsche",
					bauer, 366, 3.2, 12.0));
			Product butter = productRepository.save(new Product(null, "4008452010222", "Butter",
					weihenstephan, 747, 0.6, 1.0));
			Product dvSemmel = productRepository.save(new Product(null, "0000000000100", "Dinkel-Vollkorn Semmel (70g)",
					mayers, 247.0, 11.7, 41.3));
			Product brezel = productRepository.save(new Product(null, "0000000003821", "Dinkelbrezel (75g)",
					mayers, 261.3, 40.7, 8.8));

			mealService.create(new MealRequest("Frühstück", "Joghurt mit Butterbrezel",
					List.of(
							new MealItemRequest(joghurtKirsch.getId(), 150.0),
							new MealItemRequest(brezel.getId(), 75.0),
							new MealItemRequest(butter.getId(), 30.0)
					)
			));
			mealService.create(new MealRequest("Snack", "Feta pur",
					List.of(
							new MealItemRequest(feta.getId(), 120.0)
					)
			));
		};
	}
}
