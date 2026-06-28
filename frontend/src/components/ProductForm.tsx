import { useState } from 'react'
import type { ProductInput } from '../types'
import { createProduct } from '../api/nutritionApi'

type ProductFormProps = {
    onCreated: () => void
}

function ProductForm({ onCreated }: ProductFormProps) {
    const [name, setName] = useState('')
    const [barcode, setBarcode] = useState('')
    const [caloriesKcal, setCaloriesKcal] = useState(0)
    const [protein, setProtein] = useState(0)
    const [carbs, setCarbs] = useState(0)

    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState<string | null>(null)

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setSubmitting(true)
        setError(null)

        const product: ProductInput = {
            name,
            barcode,
            brandId: null, // manuell angelegte Produkte ohne Marke
            caloriesKcal,
            protein,
            carbs,
        }

        try {
            await createProduct(product)
            // Formular zurücksetzen
            setName('')
            setBarcode('')
            setCaloriesKcal(0)
            setProtein(0)
            setCarbs(0)
            onCreated() // Eltern: Produktliste neu laden
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Fehler beim Speichern')
        } finally {
            setSubmitting(false)
        }
    }

    return (
        <form className="product-form" onSubmit={handleSubmit}>
            <h3>Produkt ohne Barcode anlegen</h3>
            {error && <p className="error">{error}</p>}

            <label>
                Name
                <input
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="z.B. Banane, Apfel, Pommes"
                    required
                />
            </label>

            <label>
                Barcode (optional)
                <input
                    value={barcode}
                    onChange={(e) => setBarcode(e.target.value)}
                    placeholder="optional"
                />
            </label>

            <label>
                Kalorien (kcal / 100 g)
                <input
                    type="text"
                    inputMode="decimal"
                    value={caloriesKcal === 0 ? '' : caloriesKcal}
                    onChange={(e) => setCaloriesKcal(Number(e.target.value))}
                    placeholder=""
                />
            </label>

            <label>
                Protein (g / 100 g)
                <input
                    type="text"
                    inputMode="decimal"
                    value={protein === 0 ? '' : protein}
                    onChange={(e) => setProtein(Number(e.target.value))}
                    placeholder=""
                />
            </label>

            <label>
                Kohlenhydrate (g / 100 g)
                <input
                    type="text"
                    inputMode="decimal"
                    value={carbs === 0 ? '' : carbs}
                    onChange={(e) => setCarbs(Number(e.target.value))}
                    placeholder=""
                />
            </label>

            <button type="submit" disabled={submitting}>
                {submitting ? 'Speichere ...' : 'Produkt anlegen'}
            </button>
        </form>
    )
}

export default ProductForm