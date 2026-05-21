import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:5173', // to ced and alex --> change this as needed to wherever the proxy is
    },
  },
});
