import { useEffect, useState } from 'react'
import type { Meal } from './types'
import { getMeal } from './api/nutritionApi'
import MealList from './components/MealList'
import MealDetail from './components/MealDetail'
import MealForm from './components/MealForm'
import './App.css'

function App() {
    const [selectedId, setSelectedId] = useState<number | null>(null)
    const [selectedMeal, setSelectedMeal] = useState<Meal | null>(null)
    const [refreshKey, setRefreshKey] = useState(0)

    // Wenn sich Auswahl ändert: die Detail-Daten laden
    useEffect(() => {
        if (selectedId === null) {
            setSelectedMeal(null)
            return
        }
        let ignore = false
        getMeal(selectedId)
            .then((data) => {
                if (!ignore) setSelectedMeal(data)
            })
            .catch(() => {
                if (!ignore) setSelectedMeal(null)
            })
        return () => {
            ignore = true
        }
    }, [selectedId])

    // Wird nach Anlegen aufgerufen: Liste neu laden
    function handleCreated() {
        setRefreshKey((k) => k + 1)
    }

    return (
        <div className="app">
            <header>
                <h1>Nutrition Tracker</h1>
                <p>Die Proteine im Blick</p>
            </header>
            <main>
                <MealForm onCreated={handleCreated} />

                <h2>Meine Mahlzeiten</h2>
                <MealList onSelect={setSelectedId} refreshKey={refreshKey} />

                {selectedMeal && <MealDetail meal={selectedMeal} />}
            </main>
        </div>
    )
}

export default App