import type { Meal } from '../types'

type MealDetailProps = {
    meal: Meal
}

function MealDetail({ meal }: MealDetailProps) {
    return (
        <div className="meal-detail">
            <h3>{meal.name}</h3>
            <p className="meal-category">{meal.category}</p>

            {/* Nährwert-Übersicht der ganzen Mahlzeit */}
            <div className="meal-totals">
                <span className="total-protein">
                    {meal.totalProtein.toFixed(1)} g Protein</span>
                <span>{meal.totalCalories.toFixed(0)} kcal</span>
                <span>{meal.totalCarbs.toFixed(1)} g Kohlenhydrate</span>
            </div>

            {/* Die einzelnen Positionen */}
            <h4>Positionen</h4>
            <ul className="item-list">
                {meal.items.map((item) => (
                    <li key={item.id}>
                        {item.product.name} – {item.amountGrams} g
                        {' '}({item.totalProtein.toFixed(1)} g Protein)
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default MealDetail