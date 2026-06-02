package de.dhbwravensburg.remoso.nutrition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "Nutrition API",
				version = "0.1.0",
				description = "Nutrition tracking - REST API",
				contact = @Contact(
						name = "Katrin Schaake",
						email = "katrin.schaake-25@stud.dhbw-ravensburg.de"
				)
		)
)

@SpringBootApplication
public class NutritionApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutritionApplication.class, args);
	}



}
