import { useState } from 'react'
import type { Product } from '../types'
import { lookupBarcode, importBarcode } from '../api/nutritionApi'

type BarcodeImportProps = {
    onImported: () => void
}

function BarcodeImport({ onImported }: BarcodeImportProps) {
    const [code, setCode] = useState('')
    const [preview, setPreview] = useState<Product | null>(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    // Barcode bei Open Food Facts nachschlagen (noch nicht speichern)
    async function handleLookup() {
        setLoading(true)
        setError(null)
        setPreview(null)
        try {
            const product = await lookupBarcode(code)
            setPreview(product)
        } catch {
            setError('Produkt nicht gefunden oder API nicht erreichbar')
        } finally {
            setLoading(false)
        }
    }

    // Das gefundene Produkt in DB importieren
    async function handleImport() {
        setLoading(true)
        setError(null)
        try {
            await importBarcode(code)
            setPreview(null)
            setCode('')
            onImported() // Eltern: Produktliste neu laden
        } catch {
            setError('Import fehlgeschlagen')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="barcode-import">
            <h3>Produkt per Barcode importieren</h3>

            <div className="barcode-input">
                <input
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                    placeholder="Barcode (z.B. 4002334113032)"
                />
                <button onClick={handleLookup} disabled={loading || code.trim() === ''}>
                    {loading ? 'Suche ...' : 'Suchen'}
                </button>
            </div>

            {error && <p className="error">{error}</p>}

            {preview && (
                <div className="barcode-preview">
                    <p>
                        <strong>{preview.name}</strong>
                        {preview.brand ? ` (${preview.brand.name})` : ''}
                    </p>
                    <p>
                        {preview.caloriesKcal.toFixed(0)} kcal ·{' '}
                        {preview.protein.toFixed(1)} g Protein ·{' '}
                        {preview.carbs.toFixed(1)} g Kohlenhydrate
                        <span className="per-100g"> (pro 100 g)</span>
                    </p>
                    <button onClick={handleImport} disabled={loading}>
                        {loading ? 'Importiere ...' : 'In meine Produkte übernehmen'}
                    </button>
                </div>
            )}
        </div>
    )
}

export default BarcodeImport