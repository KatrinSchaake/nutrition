// entspricht ProductResponse aus Backend
export interface Product {
    id: number
    barcode: string
    name: string
    brand: Brand | null
    caloriesKcal: number
    protein: number
    carbs: number
}
// entspricht BrandResponse
export interface Brand {
    id: number
    name: string
    countries: string | null
}
// entspricht MealItemResponse
export interface MealItem {
    id: number
    product: Product
    amountGrams: number
    totalCalories: number
    totalProtein: number
    totalCarbs: number
}
// entspricht MealResponse
export interface Meal {
    id: number
    category: string
    name: string
    lastModified: string
    items: MealItem[]
    totalCalories: number
    totalProtein: number
    totalCarbs: number
}