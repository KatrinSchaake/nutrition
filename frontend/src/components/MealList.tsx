import { useEffect, useState } from "react"
import type { Meal} from "../types.ts"

function MealList() {
    const [meals, setMeals] = useState<Meal[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let ignore = false

        async function load() {
            try {
                setLoading(true)
                const response = await fetch('/api/meals')
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}`)
                }
                const data: Meal[] = await response.json()
                if (!ignore) {
                    setMeals(data)
                    setError(null)
                }
            } catch (error) {
                if (!ignore) {
                    setError(error instanceof Error ? error.message : 'Unbekannter Fehler')
                }
            } finally {
                if (!ignore) {
                    setLoading(false)
                }
            }
        }
        load()
        return () => {
            ignore = true
        }
    }, [])

    if (loading) {
        return <p>Lade Mahlzeiten ...</p>
    }
    if (error) {
        return <p className="error">Fehler beim Laden: {error}</p>
    }

    return (
        <ul className="meal-list">
            {meals.map((meal) => (
                <li key={meal.id}>
                    <strong>{meal.name}</strong> ({meal.category}){' '}
                    - {meal.totalProtein.toFixed(1)} g Protein
                </li>
            ))}
        </ul>
    )
}

export default MealList
