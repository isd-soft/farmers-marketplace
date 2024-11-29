import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import HomePage from '@/components/HomePage.vue'
import OrderPage from '@/components/OrderPage.vue'
import ProductPage from "@/components/ProductPage.vue";
import WishlistPage from "@/components/WishlistPage.vue";
import CreateProduct from "@/components/CreateProduct.vue";
import UserPage from "@/components/UserPage.vue";
import SearchProductsPage from "@/components/SearchProductsPage.vue";
import OrderManagementPage from '@/components/OrderManagementPage.vue';
import SettingsPage from '@/components/SettingsPage.vue';

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
    path: '/orders',
    name: 'Orders',
    component: OrderPage,
  },
  {
    path: '/settings',
    name: 'Settings',
    component: SettingsPage,
  },
  {
    path: '/product/create',
    name: 'CreateProduct',
    component: CreateProduct,
  },
  {
    path: '/product',
    name: 'SearchProducts',
    component: SearchProductsPage,

  },
  {
    path: '/ordermanagement',
    name: 'Order Management',
    component: OrderManagementPage,

  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.name === 'Login') {
    document.body.classList.add('login-bg');
  } else {
    document.body.classList.remove('login-bg');
  }
  next();
})

export default router
