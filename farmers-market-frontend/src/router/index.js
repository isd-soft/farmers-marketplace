import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import HomePage from '@/components/HomePage.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
  },
  {
    path: '/product/:id',
    name: 'Product',
    component: ProductPage,
    props: true,
  },
  {
    path: '/wishlist',
    name: 'Wishlist',
    component: Wishlist,
  },
  {
    path: '/id:id',
    name: 'User',
    component: UserPage,
    props: true,
  },
  {
    path: '/',
    name: 'Home',
    component: HomePage,
  },
  {
    path: '/product/create',
    name: 'Product',
    component: CreateProduct,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
