
import './App.css'
import MealList from "./components/MealList.tsx";

function App() {
  return (
      <div className="app">
        <header>
          <h1>Nutrition Tracker</h1>
          <p>Die Proteine im Blick</p>
        </header>
        <main>
          <h2>Meine Mahlzeiten</h2>
          <MealList />
        </main>
      </div>
  )
}

export default App