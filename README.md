## Welcome to my nutrition App

This is a App to provide information about different 
products on the market by using the Open Food Facts API.

Make a GET request to the Get A Product By Barcode endpoint.
https://world.openfoodfacts.net/api/v2/product/{barcode}

### Main entities are:

Product: The core entity (date from the API: name, barcode, calories, protein)

Brand  ──< Product

Brand: The brand (a manufacturer can have many products, 
1:n relationship with Product entity)

Brand  ──< Product >──< Meal

Meal: A meal (e.g., "Fitness BreakfastF") consisting of multiple products 
(n:m to Product entity)

Meal 1──< MealItem >──1 Product

ConsumptionLog: A diary entry (When did you eat which product/meal 
and in what quantity?)

Brand  ──< Product      (1:n)   @OneToMany / @ManyToOne
Product >──< Meal       (n:m)   über MealItem als Zwischentabelle
Meal   ──< MealItem     (1:n)   @OneToMany / @ManyToOne
MealItem >──1 Product   (n:1)   @ManyToOne

### Search for product w/ nutriments

https://world.openfoodfacts.net/api/v2/search?code=4072700802525&fields=product_name,nutriments,brands

### For multiple searching

https://world.openfoodfacts.org/api/v2/search?code=4072700802525,4003490035305,4002334111496&fields=code,product_name

### Licensing

The Open Food Facts database is available under the Open Database License.
https://opendatacommons.org/licenses/odbl/1-0/
