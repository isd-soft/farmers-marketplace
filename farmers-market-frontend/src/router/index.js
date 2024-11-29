import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import HomePage from '@/components/HomePage.vue'
import ProductPage from "@/components/ProductPage.vue";
import WishlistPage from "@/components/WishlistPage.vue";
import CreateProduct from "@/components/CreateProduct.vue";
import UserPage from "@/components/UserPage.vue";
import SearchProductsPage from "@/components/SearchProductsPage.vue";
import OrderManagementPage from '@/components/OrderManagementPage.vue';
import MyProductsPage from "@/components/MyProductsPage.vue";
import UpdateProduct from "@/components/UpdateProduct.vue";

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
    path: '/product/create',
    name: 'CreateProduct',
    component: CreateProduct,
  },
  {
    path: '/product/update/:id',
    name: 'EditProduct',
    component: UpdateProduct,
    props: true,
  },
  {
    path: '/product',
    name: 'SearchProducts',
    component: SearchProductsPage,

  },
  {
    path: '/product/management',
    name: 'MyProductsPage',
    component: MyProductsPage,

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
})

export default router
