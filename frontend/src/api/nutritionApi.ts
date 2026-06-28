import type { Meal} from "../types.ts"

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