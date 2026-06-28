import { useEffect, useState } from "react"
import type { Meal} from "../types"
import { getMeals } from "../api/nutritionApi"

type MealListProps = {
    onSelect: (id: number) => void
    refreshKey: number
}

function MealList({ onSelect, refreshKey }: MealListProps) {
    const [meals, setMeals] = useState<Meal[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let ignore = false

        getMeals()
            .then((data) => {
                if (!ignore) {
                    setMeals(data)
                    setError(null)
                }
            })
            .catch((error) => {
                if (!ignore) {
                    setError(error instanceof Error ? error.message : 'Da ist was schief gelaufen')
                }
            })
            .finally(() => {
                if (!ignore) {
                    setLoading(false)
                }
            })
        return () => {
            ignore = true
        }
        }, [refreshKey])

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
                    <button className="meal-item-button" onClick={() => onSelect(meal.id)}>
                        <strong>{meal.name}</strong> ({meal.category}){' '}
                        - {meal.totalProtein.toFixed(1)} g Protein
                    </button>
                </li>
            ))}
        </ul>
    )
}

export default MealList
