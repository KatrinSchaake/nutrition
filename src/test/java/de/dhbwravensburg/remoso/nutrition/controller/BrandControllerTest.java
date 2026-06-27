package de.dhbwravensburg.remoso.nutrition.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import de.dhbwravensburg.remoso.nutrition.model.Brand;
import de.dhbwravensburg.remoso.nutrition.service.BrandService;

/**
 * Katrin Schaake, TIA25, Freitag, 26.06.2026, Version: 0.1
 *
 * @WebMvcTest startet NUR Web-Schicht des BrandControllers (keine DB, kein echter Service),
 * BrandService mit @MocktoBean gefaket, getestet wird, ob Validation funktioniert (Body ungültig: 400)
 */
@WebMvcTest(BrandController.class)
public class BrandControllerTest {

	@MockitoBean
	private BrandService brandService; // wird gefaket, kein echter Service nötig
	@Autowired
	private MockMvc mockMvc;

	@Test
	void create_return400_whenNameIsBlank() throws Exception {
		// Arrange: ungültiger Body - name ist leer (verstößt gegen @NotBlank)
		String invalidJson = "{ \"name\": \"\", \"countries\": \"Deutschland\" }";

		// Act & Assert: Spring soll 400 zurückgeben, bevor Service überhaupt aufgerufen wird
		mockMvc.perform(post("/api/brands")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_return400_whenNameIsMissing() throws Exception {
		// Arrange: name fehlt komplett
		String invalidJson = "{ \"countries\": \"Deutschland\" }";

		// Act & Assert
		mockMvc.perform(post("/api/brands")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
				.andExpect(status().isBadRequest());
	}
}
