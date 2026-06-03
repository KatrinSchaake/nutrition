package de.dhbwravensburg.remoso.nutrition.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Katrin Schaake, TIA25, Mittwoch, 03.06.2026, Version: 0.1
 *
 * Globales Exception Handling für alle Controller
 *
 * @RestControllerAdvice:
 *   -> "Diese Klasse fängt Exceptions aus ALLEN Controllern ab"
 *
 * ProblemDetail:
 *   Standard-Format für Fehler-Antworten (RFC 9457)
 *   Beispiel-Antwort:
 *   {
 *     "type": "about:blank",
 *     "title": "Resource not found",
 *     "status": 404,
 *     "detail": "Brand with id 42 not found"
 *   }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 404 - Ressource nicht gefunden
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleNotFound(ResourceNotFoundException ex) {

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("Resource not found");
		return problem;
	}
	/**
	 * 409 - Konflikt beim Löschen
	 */
	@ExceptionHandler(ResourceInUseException.class)
	public ProblemDetail handleConflict(ResourceInUseException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT, ex.getMessage());
		problem.setTitle("Resource in use");
		return problem;
	}
	/**
	 * 502 - Externe API nicht erreichbar
	 */
	@ExceptionHandler(ExternalApiException.class)
	public ProblemDetail handleExternalApi(ExternalApiException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_GATEWAY, ex.getMessage());
		problem.setTitle("Upstream service unavailable");
		return problem;
	}
	/**
	 * 400 - Validierungsfehler bei @Valid
	 *
	 * wird ausgelöst, wenn ein Request-Body gegen @NotBlank, @Positive etc. verstößt
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(fieldError ->
				fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST, "Request validation failed"
		);
		problem.setTitle("Validation failed");
		problem.setProperty("fieldErrors", fieldErrors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
	}
	/**
	 * 500 - Fallback für alle anderen Fehlern und alles Unerwartete
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleAnyOther(Exception ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error"
		);
		problem.setTitle("Internal server error");
		problem.setProperty("error", ex.getClass().getSimpleName());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
	}
}
