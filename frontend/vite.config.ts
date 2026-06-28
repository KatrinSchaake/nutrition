import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Alles unter /api geht an ds Spring-Boot-Backend
      '/api': 'http://localhost:8080',
    },
  },
})
