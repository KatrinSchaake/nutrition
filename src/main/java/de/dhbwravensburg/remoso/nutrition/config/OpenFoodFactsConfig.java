package de.dhbwravensburg.remoso.nutrition.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Katrin Schaake, TIA25, Dienstag, 02.06.2026, Version: 0.1
 *
 * Konfiguriert den RestClient für OpenFoodFacts-API, kein API-Key notwendig
 *
 * @Value liest Werte aus application.properties
 * ->	Wenn sich URL oder die Timeouts ändern, muss nur die Properties-Datei
 * 		geändert werden, nicht der Java-Code neu kompiliert werden
 *
 */
@Configuration
public class OpenFoodFactsConfig {

	@Value("${openfoodfacts.api.base-url}")
	private String baseUrl;

	@Value("${openfoodfacts.api.connect-timeout-ms}")
	private int connectTimeoutMs;

	@Value("${openfoodfacts.api.read-timeout-ms}")
	private int readTimeoutMs;

	@Bean
	public RestClient openFoodFactsRestClient() {

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(connectTimeoutMs);
		requestFactory.setReadTimeout(readTimeoutMs);

		return RestClient.builder()
				.baseUrl(baseUrl)
				.requestFactory(requestFactory)
				.defaultHeader(HttpHeaders.ACCEPT, "application/json")
				// Open Food Facts empfiehlt einen User-Agent Header
				.defaultHeader(HttpHeaders.USER_AGENT,
						"NutritionApp/0.1 (DHBW Ravensburg; Katrin Schaake)")
				.build();
	}

}
