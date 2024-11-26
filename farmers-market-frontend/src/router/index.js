import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import HomePage from '@/components/HomePage.vue'
<<<<<<< HEAD
import ProductPage from '@/components/ProductPage.vue'
import UserPage from '@/components/UserPage.vue'
import Wishlist from '@/components/WishlistPage.vue'
import OrderPage from '@/components/OrderPage.vue'
=======
import ProductPage from "@/components/ProductPage.vue";
import WishlistPage from "@/components/WishlistPage.vue";
import CreateProduct from "@/components/CreateProduct.vue";
import UserPage from "@/components/UserPage.vue";
import SearchProductsPage from "@/components/SearchProductsPage.vue";
>>>>>>> dev

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
    component: WishlistPage,
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
<<<<<<< HEAD
    path: '/orders',
    name: 'Orders',
    component: OrderPage,
=======
    path: '/product/create',
    name: 'CreateProduct',
    component: CreateProduct,
  },
  {
    path: '/product',
    name: 'SearchProducts',
    component: SearchProductsPage,

>>>>>>> dev
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
