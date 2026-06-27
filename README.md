# Nutrition Tracker

Eine REST-API zum Erfassen von Lebensmitteln und Mahlzeiten mit automatischer
Nährwertberechnung. Produkte können per Barcode aus der externen
**Open Food Facts**-API importiert werden.

Projektaufgabe Webapplikation in *WebEngineering 2*, DHBW Ravensburg 
Campus Friedrichshafen (TIA25)

---

## Motivation

Eine ausreichende Eiweißzufuhr ist beim Sport und mit zunehmendem Alter wichtig,
gerät im Alltag aber leicht aus dem Blick. Der Nutrition Tracker rechnet pro
Mahlzeit das enthaltene Protein aus – damit ich täglich sehe, wo man steht;
Kalorien und Kohlenhydrate bilden eine sinnvolle Zusatzinfo.

---

## Inhalt

- [Motivation](#motivation)
- [Funktionsumfang](#funktionsumfang)
- [Architektur](#architektur)
- [Datenmodell](#datenmodell)
- [Technologie-Stack](#technologie-stack)
- [Drittanbieter-API: Open Food Facts](#drittanbieter-api-open-food-facts)
- [Lokales Starten](#lokales-starten)
- [API-Endpunkte](#api-endpunkte)
- [Tests](#tests)
- [Projektstruktur](#projektstruktur)
- [Danksagung](#danksagung)

---

## Funktionsumfang

- Vollständige **CRUD**-Operationen für die Entitäten `Brand`, `Product` und `Meal`
- Automatische **Nährwertberechnung** pro Mahlzeit (Kalorien, Eiweiß, Kohlenhydrate)
  auf Basis der enthaltenen Produkte und Mengen
- **Barcode-Import**: Produkte werden über die Open Food Facts-API nachgeschlagen
  und können direkt in die Datenbank übernommen werden
- **Eingabevalidierung** mit Bean Validation (`@NotBlank`, `@Positive`, `@Valid` …)
- **Globale Fehlerbehandlung** mit sinnvollen HTTP-Statuscodes im
  Problem-Detail-Format (RFC 9457)
- **Volltextsuche** nach Name (case-insensitive) für Brands, Produkte und Mahlzeiten
- **OpenAPI/Swagger**-Dokumentation der gesamten API

---

## Architektur

Die Anwendung folgt der vorgegebenen Drei-Schichten-Architektur. Aktuell ist das
Backend inklusive Datenbank und Drittanbieter-Anbindung umgesetzt; das React-Frontend
konsumiert die hier dokumentierte REST-API.

```
┌──────────┐    REST    ┌──────────────┐   REST   ┌─────────────────┐
│ FRONTEND │ ◄────────► │   BACKEND    │ ◄──────► │ OPEN FOOD FACTS │
│ (React)  │            │ (Spring Boot)│          │  (externe API)  │
└──────────┘            │      │       │          └─────────────────┘
                        │   ┌──▼───┐   │
                        │   │  H2  │   │
                        │   └──────┘   │
                        └──────────────┘
```

Innerhalb des Backends ist eine klassische **Schichtenarchitektur** umgesetzt.
Jede Anfrage durchläuft die Schichten von oben nach unten:

```
Controller  →  Service  →  Repository  →  Datenbank
   │             │
   │             └─ Geschäftslogik, Auflösen von Referenzen, Exceptions
   │
   └─ HTTP, Statuscodes, Validierung; mappt über DTO + Mapper
```

- **Controller** nehmen HTTP-Anfragen entgegen, validieren die Eingabe und geben
  Antworten zurück. Sie enthalten keine Geschäftslogik.
- **Service** kapselt die Geschäftslogik und wirft typisierte Exceptions
  (z. B. `ResourceNotFoundException`).
- **Repository** (Spring Data JPA) übernimmt den Datenbankzugriff.
- **DTOs + Mapper** trennen die nach außen sichtbaren Datenstrukturen von den
  internen JPA-Entitäten.

---

## Datenmodell

Vier Entitäten in Beziehung zueinander:

```
Brand 1 ───── N Product 1 ───── N MealItem N ───── 1 Meal
```

| Entität    | Beschreibung                                                            |
|------------|-------------------------------------------------------------------------|
| `Brand`    | Marke/Hersteller eines Produkts (z. B. „Bauer")                         |
| `Product`  | Ein Lebensmittel mit Nährwerten pro 100 g; gehört zu einer `Brand`      |
| `Meal`     | Eine Mahlzeit (Kategorie + Name) mit mehreren Positionen                 |
| `MealItem` | Verbindungsobjekt zwischen `Meal` und `Product`; speichert die Menge (g) |

`MealItem` ist die Join-Entität: Sie hält die Menge in Gramm und berechnet daraus
die anteiligen Nährwerte. Eine `Meal` summiert die Werte all ihrer `MealItem`s.

---

## Technologie-Stack

| Komponente       | Technologie                                  |
|------------------|----------------------------------------------|
| Sprache          | Java 21                                       |
| Framework        | Spring Boot 4.0.5                             |
| Persistenz       | Spring Data JPA + Hibernate                   |
| Datenbank        | H2 (In-Memory)                                |
| Validierung      | Spring Boot Starter Validation (Jakarta Bean Validation) |
| API-Doku         | springdoc-openapi (Swagger UI)               |
| Boilerplate      | Lombok                                        |
| Build-Tool       | Maven (mit Maven Wrapper)                     |
| Tests            | JUnit 5, Spring Boot Test, Mockito           |

---

## Drittanbieter-API: Open Food Facts

[Open Food Facts](https://world.openfoodfacts.org/) ist eine freie, offene
Lebensmittel-Datenbank. Die API wird **ohne API-Key** genutzt.

**Wofür?** Über den Barcode (EAN) eines Produkts ruft das Backend Produktname,
Marke und Nährwerte ab und kann das Produkt direkt in die eigene Datenbank
importieren.

**Beispiel-Aufruf der externen API:**

```
GET https://world.openfoodfacts.org/api/v2/product/{barcode}?fields=product_name,brands,nutriments
```

Die Basis-URL und Timeouts sind in `src/main/resources/application.properties`
konfiguriert (`openfoodfacts.api.base-url`, `…connect-timeout-ms`,
`…read-timeout-ms`) und können dort ohne Code-Änderung angepasst werden.

Ist ein Barcode bei Open Food Facts nicht vorhanden, antwortet die eigene API mit
`404`; ist der externe Dienst nicht erreichbar, mit `502`.

---

## Lokales Starten

### Voraussetzungen

- **JDK 21** (oder neuer)
- **Git**
- Kein separates Maven nötig – der Maven Wrapper (`mvnw`) ist im Repository enthalten.

### Schritte

```bash
# 1. Repository klonen
git clone https://github.com/KatrinSchaake/nutrition.git
cd nutrition

# 2. Anwendung starten
#    Windows:
mvnw.cmd spring-boot:run
#    Linux / macOS:
./mvnw spring-boot:run
```

Beim Start legt ein `DataInitializer` automatisch Beispiel-Daten an
(einige Marken, Produkte und Mahlzeiten), sofern die Datenbank leer ist.

### Zugriffspunkte

| Was              | URL                                            |
|------------------|------------------------------------------------|
| API-Basis        | `http://localhost:8080/api`                    |
| Swagger UI       | `http://localhost:8080/swagger-ui.html`        |
| Health-Check     | `http://localhost:8080/api/health`             |
| H2-Konsole       | `http://localhost:8080/h2-console`             |

**H2-Konsole** – Anmeldedaten:

- JDBC URL: `jdbc:h2:mem:nutrition`
- Benutzer: `SA`
- Passwort: *(leer)*

> Hinweis: Die H2-Datenbank ist In-Memory. Beim Neustart der Anwendung gehen alle
> Daten verloren und die Beispiel-Daten werden neu angelegt.

---

## API-Endpunkte

### Brands – `/api/brands`

| Methode | Pfad                      | Beschreibung                |
|---------|---------------------------|-----------------------------|
| GET     | `/api/brands`             | Alle Marken                 |
| GET     | `/api/brands/{id}`        | Eine Marke                  |
| GET     | `/api/brands/search?name=`| Suche nach Name             |
| POST    | `/api/brands`             | Marke anlegen               |
| PUT     | `/api/brands/{id}`        | Marke ersetzen              |
| DELETE  | `/api/brands/{id}`        | Marke löschen               |

### Products – `/api/products`

| Methode | Pfad                         | Beschreibung                    |
|---------|------------------------------|---------------------------------|
| GET     | `/api/products`              | Alle Produkte (optional `?brandId=`) |
| GET     | `/api/products/{id}`         | Ein Produkt                     |
| GET     | `/api/products/search?name=` | Suche nach Name                 |
| POST    | `/api/products`              | Produkt anlegen                 |
| PUT     | `/api/products/{id}`         | Produkt ersetzen                |
| DELETE  | `/api/products/{id}`         | Produkt löschen                 |

### Meals – `/api/meals`

| Methode | Pfad                          | Beschreibung                       |
|---------|-------------------------------|------------------------------------|
| GET     | `/api/meals`                  | Alle Mahlzeiten                    |
| GET     | `/api/meals/{id}`             | Eine Mahlzeit mit Nährwertsummen   |
| GET     | `/api/meals/category?category=` | Mahlzeiten einer Kategorie       |
| GET     | `/api/meals/search?name=`     | Suche nach Name                    |
| POST    | `/api/meals`                  | Mahlzeit anlegen                   |
| PUT     | `/api/meals/{id}`             | Mahlzeit ersetzen                  |
| DELETE  | `/api/meals/{id}`             | Mahlzeit löschen                   |

### Barcode (Open Food Facts) – `/api/barcode`

| Methode | Pfad                          | Beschreibung                               |
|---------|-------------------------------|--------------------------------------------|
| GET     | `/api/barcode/{code}`         | Produkt bei Open Food Facts nachschlagen (ohne Speichern) |
| POST    | `/api/barcode/{code}/import`  | Produkt nachschlagen **und** in die DB importieren |

### Fehlerformat

Fehler werden einheitlich als Problem Detail (RFC 9457) zurückgegeben:

| Status | Bedeutung                                             |
|--------|-------------------------------------------------------|
| 400    | Validierungsfehler (z. B. fehlender Pflichtwert)      |
| 404    | Ressource nicht gefunden                              |
| 409    | Ressource wird noch referenziert (Löschen blockiert)  |
| 502    | Externe API (Open Food Facts) nicht erreichbar        |

---

## Tests

Ausführen aller Tests:

```bash
# Windows
mvnw.cmd test
# Linux / macOS
./mvnw test
```

Die Test-Suite deckt mehrere Testarten ab:

| Testart           | Klasse                       | Prüft                                      |
|-------------------|------------------------------|--------------------------------------------|
| Unit              | `MealItemTest`, `MealTest`   | Nährwertberechnung und Summenbildung       |
| Unit (Mapping)    | `OpenFoodFactsMapperTest`    | Umwandlung der API-Antwort, null-Behandlung |
| Integration (JPA) | `ProductRepositoryTest`      | Derived Query (Namenssuche)                |
| Web (MVC)         | `BrandControllerTest`        | Validierung und HTTP-Statuscodes           |
| Smoke             | `NutritionApplicationTests`  | Anwendungskontext startet fehlerfrei       |

---

## Projektstruktur

```
src/main/java/de/dhbwravensburg/remoso/nutrition/
├── config/        DataInitializer, Konfiguration der externen API
├── controller/    REST-Controller
├── dto/           Request-/Response-Objekte (inkl. openfoodfacts/)
├── exception/     Eigene Exceptions + GlobalExceptionHandler
├── mapper/        Umwandlung zwischen Entitäten und DTOs
├── model/         JPA-Entitäten
├── repository/    Spring-Data-JPA-Repositories
└── service/       Geschäftslogik
```

---

## Danksagung

Diese Anwendung nutzt Daten von [**Open Food Facts**](https://world.openfoodfacts.org/),
einer freien, von Freiwilligen aufgebauten Lebensmittel-Datenbank. Die Datenbank
steht unter der [Open Database License (ODbL)](https://opendatacommons.org/licenses/odbl/),
die einzelnen Inhalte unter der Database Contents License.

Vielen Dank an die Open-Food-Facts-Community für die Bereitstellung dieser offenen Daten.

---

## Autorin

Katrin Schaake · TIA25 · DHBW Ravensburg Campus Friedrichshafen

