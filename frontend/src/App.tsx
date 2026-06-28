import { useEffect, useState } from "react"
import type { Meal } from "./types"
import { getMeal} from "./api/nutritionApi"
import MealList from "./components/MealList"
import MealDetail from "./components/MealDetail"
import './App.css'

function App() {
    const [selectedId, setSelectedId] = useState<number | null>(null)
    const [selectedMeal, setSelectedMeal] = useState<Meal | null>(null)

    // Wenn sich die Auswahl ändert: die Detail-Daten laden
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

    return (
      <div className="app">
        <header>
          <h1>Nutrition Tracker</h1>
          <p>Die Proteine im Blick</p>
        </header>
        <main>
          <h2>Meine Mahlzeiten</h2>
          <MealList onSelect={setSelectedId} />
            {selectedMeal && <MealDetail meal={selectedMeal} />}
        </main>
      </div>
  )
}

export default App