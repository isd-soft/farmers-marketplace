import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: LoginPage, // Render the Login component for this route
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),  // Replace process.env.BASE_URL with import.meta.env.BASE_URL
  routes,
})

export default router
