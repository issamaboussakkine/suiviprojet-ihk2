/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        interface: ['Inter', 'sans-serif'],
      },
      colors: {
        slate: {
          50: '#F8FAFC',
          800: '#1E293B',
          900: '#0F172A',
        },
        blue: {
          500: '#3B82F6',
        }
      },
      borderRadius: {
        'xl': '12px',
      }
    },
  },
  plugins: [],
}
