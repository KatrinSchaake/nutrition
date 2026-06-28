import type { Meal, Product, MealInput } from '../types'

const BASE = '/api'

// generische Hilfsfunktion: prüft Antwort und gibt sie typisiert zurück
async function handle<T>(response: Response): Promise<T> {
    if (!response.ok) {
        throw new Error(`${response.status}`)
    }
    return (await response.json()) as T
}
// alle Mahlzeiten laden
export function getMeals(): Promise<Meal[]> {
    return fetch(`${BASE}/meals`).then((res) => handle<Meal[]>(res))
}
// eine einzelne Mahlzeit laden
export function getMeal(id: number): Promise<Meal> {
    return fetch(`${BASE}/meals/${id}`).then((res) => handle<Meal>(res))
}

// Alle Produkte laden (für die Auswahl im Formular)
export function getProducts(): Promise<Product[]> {
    return fetch(`${BASE}/products`).then((res) => handle<Product[]>(res))
}

// Eine neue Mahlzeit anlegen
export function createMeal(meal: MealInput): Promise<Meal> {
    return fetch(`${BASE}/meals`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(meal),
    }).then((res) => handle<Meal>(res))
}

// Produkt bei Open Food Facts nachschlagen (ohne Speichern)
export function lookupBarcode(code: string): Promise<Product> {
    return fetch(`${BASE}/barcode/${code}`).then((res) => handle<Product>(res))
}

// Produkt per Barcode in die DB importieren
export function importBarcode(code: string): Promise<Product> {
    return fetch(`${BASE}/barcode/${code}/import`, {
        method: 'POST',
    }).then((res) => handle<Product>(res))
}