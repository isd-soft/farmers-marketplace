import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import HomePage from '@/components/HomePage.vue'
import OrderPage from '@/components/OrderPage.vue'
import CartPage from '@/components/CartPage.vue'
import ProductPage from "@/components/ProductPage.vue";
import WishlistPage from "@/components/WishlistPage.vue";
import CreateProduct from "@/components/CreateProduct.vue";
import UserPage from "@/components/UserPage.vue";
import SearchProductsPage from "@/components/SearchProductsPage.vue";
import OrderManagementPage from '@/components/OrderManagementPage.vue';
import MyProductsPage from "@/components/MyProductsPage.vue";
import UpdateProduct from "@/components/UpdateProduct.vue";
import SettingsPage from '@/components/SettingsPage.vue';
import MessagingPage from '@/components/MessagingPage.vue'
import ScheduleOrder from "@/components/ScheduleOrder.vue";
import UpdateScheduledOrder from "@/components/UpdateScheduledOrder.vue";
import MyScheduledOrders from "@/components/MyScheduledOrders.vue";
import ServerInfoPage from '@/components/ServerInfoPage.vue'

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
    name: 'OrderPage',
    component: OrderPage,
  },
  {
    path: '/schedule-order/:id',
    name: 'ScheduleOrder',
    component: ScheduleOrder,
    props: true,
  },
  {
    path: '/schedule-order/management',
    name: 'MyScheduledOrders',
    component: MyScheduledOrders,
  },
  {
    path: '/schedule-order/update/:id',
    name: 'UpdateScheduledOrder',
    component: UpdateScheduledOrder,
    props: true,
  },
  {
    path: '/cart',
    name: 'CartPage',
    component: CartPage,
  },
  {
    path: '/ordermanagement',
    name: 'Order Management',
    component: OrderManagementPage,
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
  {
    path: '/messages',
    name: 'Messages Page',
    component: MessagingPage
  },
  {
    path: '/server-info',
    name: 'Server Info Page',
    component: ServerInfoPage,
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
