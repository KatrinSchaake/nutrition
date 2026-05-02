## Welcome to my nutrition App

This is a App to provide informations about different 
products on the market by using the Open Food Facts API.

### Main entities are:

Product: The core entity (date from the API: name, barcode, calories, protein)

Brand: The brand (a manufacturer can have many products, 
1:n relationship with Product entity)

Meal: A meal (e.g., "Fitness BreakfastF") consisting of multiple products 
(n:m to Product entity)

ConsumptionLog: A diarx entry (When did you eat which product/meal 
and in what quantity?)