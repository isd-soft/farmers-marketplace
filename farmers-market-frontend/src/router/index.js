import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/components/LoginPage.vue';
import HomePage from '@/components/HomePage.vue';
import OrderPage from '@/components/OrderPage.vue';
import CartPage from '@/components/CartPage.vue';
import ProductPage from '@/components/ProductPage.vue';
import WishlistPage from '@/components/WishlistPage.vue';
import UserPage from '@/components/UserPage.vue';
import SearchProductsPage from '@/components/SearchProductsPage.vue';
import OrderManagementPage from '@/components/OrderManagementPage.vue';
import MyProductsPage from '@/components/MyProductsPage.vue';
import UpdateProduct from '@/components/UpdateProduct.vue';
import SettingsPage from '@/components/SettingsPage.vue';
import MessagingPage from '@/components/MessagingPage.vue';
import CreateProduct from '@/components/CreateProduct.vue';
import ScheduleOrder from '@/components/ScheduleOrder.vue';
import UpdateScheduledOrder from '@/components/UpdateScheduledOrder.vue';
import MyScheduledOrders from '@/components/MyScheduledOrders.vue';
import ServerInfoPage from '@/components/ServerInfoPage.vue';
import PerformancePage from '@/components/PerformancePage.vue';
import AboutUsPage from '@/components/AboutUsPage.vue';
import ContactUsPage from '@/components/ContactUsPage.vue';
import FAQPage from '@/components/FAQPage.vue';
import PrivacyPolicyPage from '@/components/PrivacyPolicyPage.vue';
import FarmersSearchPage from '@/components/FarmersSearchPage.vue';
import DealsPage from '@/components/DealsPage.vue';
import AdminProducts from '@/components/Admin/AdminProducts.vue';
import AdminOrders from '@/components/Admin/AdminOrders.vue';
import AdminUsers from '@/components/Admin/AdminUsers.vue';
import AdminCategories from '@/components/Admin/AdminCategories.vue';
import AdminProductReviews from '@/components/Admin/AdminProductReviews.vue';
import AdminFarmerReviews from '@/components/Admin/AdminFarmerReviews.vue';
import ErrorPage from '@/components/ErrorPage.vue';

const isAdmin = () => {
  return localStorage.getItem('userRole') === 'ADMIN';
};

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
    component: MessagingPage,
  },
  {
    path: '/server-info',
    name: 'Server Info Page',
    component: ServerInfoPage,
  },
  {
    path: '/performance',
    name: 'Performance',
    component: PerformancePage,
  },
  {
    path: '/about',
    name: 'About Us',
    component: AboutUsPage,
  },
  {
    path: '/contact',
    name: 'Contact Us',
    component: ContactUsPage,
  },
  {
    path: '/faq',
    name: 'FAQ',
    component: FAQPage,
  },
  {
    path: '/privacy',
    name: 'Privacy Policy',
    component: PrivacyPolicyPage,
  },
  {
    path: '/farmers-search',
    name: 'Farmers Search',
    component: FarmersSearchPage,
  },
  {
    path: '/deals',
    name: 'Deals',
    component: DealsPage,
  },
  {
    path: '/admin/products',
    name: 'Product Administration',
    component: AdminProducts,
    meta: { requiresAdmin: true },
  },
  {
    path: '/admin/orders',
    name: 'Order Administration',
    component: AdminOrders,
    meta: { requiresAdmin: true },
  },
  {
    path: '/admin/users',
    name: 'Users Administration',
    component: AdminUsers,
    meta: { requiresAdmin: true },
  },
  {
    path: '/admin/categories',
    name: 'Category Administration',
    component: AdminCategories,
    meta: { requiresAdmin: true },
  },
  {
    path: '/admin/productreviews',
    name: 'Product Review Administration',
    component: AdminProductReviews,
    meta: { requiresAdmin: true },
  },
  {
    path: '/admin/farmerreviews',
    name: 'Farmer Reiew Administration',
    component: AdminFarmerReviews,
    meta: { requiresAdmin: true },
  },
  {
    path: '/404',
    name: 'Error',
    component: ErrorPage,
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/404',
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAdmin && !isAdmin()) {
    next({ name: 'Error' });
  } else {
    next();
  }
});

router.beforeEach((to, from, next) => {
  if (to.name === 'Login') {
    document.body.classList.add('login-bg');
  } else {
    document.body.classList.remove('login-bg');
  }
  next();
});

export default router;
