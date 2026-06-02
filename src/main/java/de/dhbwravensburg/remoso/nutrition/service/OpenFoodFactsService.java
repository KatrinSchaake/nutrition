package de.dhbwravensburg.remoso.nutrition.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.dhbwravensburg.remoso.nutrition.dto.openfoodfacts.OpenFoodFactsResponse;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Ruft die OpenFoodFacts-API auf und liefert Antwort zurück
 *
 * Gibt null zurück, wenn:
 *   - das Produkt nicht gefunden wurde (status == 0)
 *   - ein Netzwerkfehler aufgetreten ist
 *
 */
@Service
public class OpenFoodFactsService {

	private final RestClient restClient;

	public OpenFoodFactsService(RestClient restClient) {
		this.restClient = restClient;
	}

	/**
	 * Sucht ein Produkt per Barcode @ OpenFoodFacts
	 *
	 * API-Aufruf:
	 *   GET /api/v2/product/{barcode}?fields=product_name,brands,nutriments
	 *
	 * fields= begrenzt die Antwort auf die Felder, die wir brauchen.
	 * Ohne fields= kommt ein riesiges JSON mit hunderten Feldern zurück.
	 *
	 * @param barcode der EAN-Barcode (z.B. "4002334113032")
	 * @return die API-Antwort, oder null wenn nicht gefunden / Fehler
	 */
	public OpenFoodFactsResponse lookupByBarcode(String barcode) {
		try {
			OpenFoodFactsResponse response = restClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/api/v2/product/{barcode}")
							.queryParam("fields", "product_name,brands,nutriments")
							.build(barcode)
					)
					.retrieve()
					.body(OpenFoodFactsResponse.class);

			// API gibt status=0 wenn Barcode nicht existiert
			if (response == null || response.status() == 0) {
				return null;
			}
			return response;

		} catch (RestClientException e) {
			// Netzwerk-Fehler, Timeout etc.
			System.out.println("API-Fehler: " + e.getMessage());
			System.out.println("Ursache: " + e.getCause());
			// Später mit eigenem ExternalApiException ersetzen
			return null;
		}
	}
}
