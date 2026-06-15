import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// In dev we proxy /api to the Spring Boot backend so the SPA talks to a same-origin
// path and we avoid CORS entirely. Override the target with VITE_BACKEND_ORIGIN.
const backendOrigin = process.env.VITE_BACKEND_ORIGIN ?? "http://localhost:8080";

export default defineConfig({
  plugins: [react()],
  server: {
    host: "127.0.0.1",
    proxy: {
      "/api": {
        target: backendOrigin,
        changeOrigin: true,
      },
    },
  },
});
