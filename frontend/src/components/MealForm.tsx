import { useEffect, useState } from 'react'
import type { Product, MealInput, MealItemInput } from '../types'
import { getProducts, createMeal } from '../api/nutritionApi'

type MealFormProps = {
    onCreated: () => void
}

function MealForm({ onCreated }: MealFormProps) {
    // Produkte für Auswahl-Dropdowns
    const [products, setProducts] = useState<Product[]>([])

    // Formularfelder
    const [category, setCategory] = useState('')
    const [name, setName] = useState('')
    const [items, setItems] = useState<MealItemInput[]>([])

    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState<string | null>(null)

    // Produkte einmal laden
    useEffect(() => {
        let ignore = false
        getProducts()
            .then((data) => {
                if (!ignore) setProducts(data)
            })
            .catch(() => {
                if (!ignore) setError('Produkte konnten nicht geladen werden')
            })
        return () => {
            ignore = true
        }
    }, [])

    // eine leere Position hinzufügen
    function addItem() {
        setItems((prev) => [...prev, { productId: 0, amountGrams: 0 }])
    }

    // eine Position wieder entfernen
    function removeItem(index: number) {
        setItems((prev) => prev.filter((_, i) => i !== index))
    }

    // ein Feld einer best. Pos. ändern
    function updateItem(index: number, field: keyof MealItemInput, value: number) {
        setItems((prev) =>
            prev.map((item, i) => (i === index ? { ...item, [field]: value } : item)),
        )
    }

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setSubmitting(true)
        setError(null)

        const meal: MealInput = { category, name, items }

        try {
            await createMeal(meal)
            // Formular zurücksetzen
            setCategory('')
            setName('')
            setItems([])
            onCreated() // Eltern-Komponente: Liste neu laden
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Fehler beim Speichern')
        } finally {
            setSubmitting(false)
        }
    }

    return (
        <form className="meal-form" onSubmit={handleSubmit}>
            <h3>Neue Mahlzeit</h3>
            {error && <p className="error">{error}</p>}

            <label>
                Kategorie
                <input
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    required
                />
            </label>

            <label>
                Name
                <input
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </label>

            <h4>Positionen</h4>
            {items.map((item, index) => (
                <div key={index} className="form-item">
                    <select
                        value={item.productId}
                        onChange={(e) => updateItem(index, 'productId', Number(e.target.value))}
                    >
                        <option value={0}>– Produkt wählen –</option>
                        {products.map((p) => (
                            <option key={p.id} value={p.id}>
                                {p.name}
                            </option>
                        ))}
                    </select>

                    <input
                        type="number"
                        value={item.amountGrams}
                        onChange={(e) => updateItem(index, 'amountGrams', Number(e.target.value))}
                        placeholder="Gramm"
                    />

                    <button type="button" onClick={() => removeItem(index)}>
                        ✕
                    </button>
                </div>
            ))}

            <button type="button" onClick={addItem}>
                + Position hinzufügen
            </button>

            <button type="submit" disabled={submitting}>
                {submitting ? 'Speichere ...' : 'Mahlzeit anlegen'}
            </button>
        </form>
    )
}

export default MealForm