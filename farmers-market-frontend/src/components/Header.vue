<template>
  <div class="navbar">
    <Menubar :model="items" class="menubar">
      <template #start>
        <img src="@/assets/logo.png" alt="Logo" class="logo" @click="goHome" />
      </template>

      <template #end>
        <div class="right-section">
          <div class="search-bar" v-if="!isSearchPage">
            <InputText @keydown.enter="search" v-model="searchQ"/>
            <Button @click="search" class="search-button">
              <i class="pi pi-search"></i>
            </Button>
          </div>
          <Menubar v-if="isLoggedIn" :model="accountMenu" class="menubar-item"></Menubar>
          <Button v-if="!isLoggedIn" @click="goToLogin" class="login button" label="Login" severity="primary"
            variant="text"></Button>
          <Button @click="goToCart" class="cart button" label="Cart" severity="secondary" variant="text">
            <i class="pi pi-shopping-cart cart-icon"></i>
            <span class="cart-text">Cart</span>
          </Button>
        </div>
      </template>
    </Menubar>
  </div>
</template>

<script setup>
import {ref, onMounted, watch, computed} from 'vue';
import { isLoggedIn } from '@/shared/authState';
import axiosInstance, {getUserId} from '@/utils/axiosInstance';
import Menubar from 'primevue/menubar';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import {useRoute, useRouter} from "vue-router";

const searchQ = ref('');
let currentUser = null;

const items = ref([
  { label: 'Deals' },
  { label: "What's New" },
  { label: 'Delivery' , command: () => goToDeliveries() },
]);

const accountMenu = ref([
  {
    label: 'Account',
    icon: 'pi pi-user',
    items: [
      { label: 'Orders', icon: 'pi pi-shopping-cart', command: () => goToOrders() },
      { label: 'Messages', icon: 'pi pi-envelope', command: () => goToMessages() },
      { label: 'Farmers Search', icon: 'pi pi-search', command: () => goToFarmersSearch()  },
      { label: 'Scheduled Orders', icon: 'pi pi-clock', command: () => goToScheduledOrders() },
      { label: 'Wishlist', icon: 'pi pi-heart', command: () => goToFavorites() },
      { label: 'Settings', icon: 'pi pi-cog', command: () => goToSettings() },
      { label: 'Server Info', icon: 'pi pi-exclamation-triangle', command: () => goToServerInfo() },
      { label: 'Logout', icon: 'pi pi-sign-out', command: () => logout() },
    ],
  },
]);
const route = useRoute();
const router = useRouter();
const isSearchPage = computed(() => route.name === 'SearchProducts');
const search = () => {
  router.push({ name: 'SearchProducts', query: { search: searchQ.value } });
};
const fetchCategories = async () => {
    try {
      const response = await axiosInstance.get('/category');
      const categories = response.data;
      const categoryItems = categories.map(category => ({
        label: category.title,
        command: () => goToCategory(category.id),
      }));

      items.value = [{label: 'Categories', items: categoryItems}, ...items.value];
    } catch (error) {
    }
};
const fetchUserData = async () => {
  const middleIndex = Math.floor(accountMenu.value[0].items.length / 2);
  try {
    const response = await axiosInstance.get(`/current-user/`);
    currentUser = response.data;
    if (currentUser.isFarmer) {
      accountMenu.value[0].items.unshift({
        label: 'My sales',
        icon: 'pi pi-credit-card',
        command: () => goToMySales(),
      }),
      accountMenu.value[0].items.unshift({
        label: 'Products',
        icon: 'pi pi-clipboard',
        command: () => goToMyProducts(),
      }),
      accountMenu.value[0].items.splice(middleIndex, 0,{
        label: 'Performance',
        icon: 'pi pi-chart-line',
        command: () => goToPerformance(),
      });
    }
  } catch (error) {
  }
};

const goHome = () => {
  window.location.href = '/';
};

const goToLogin = () => {
  window.location.href = '/login';
};

const goToCart = () => {
  window.location.href = '/cart';
};
const goToMyProducts = () => {
  window.location.href = '/product/management';
};
const goToMySales = () => {
  window.location.href = '/ordermanagement';
};

function goToDeliveries() {
  window.location.href = '/delivery';
}

function goToFarmersSearch() {
  window.location.href = '/farmers-search';
}
const goToOrders = () => {
  window.location.href = '/orders';
};
const goToServerInfo = () => {
  window.location.href = '/server-info';
};
const goToScheduledOrders = () => {
  window.location.href = '/schedule-order/management';
};

const goToMessages = () => {
  window.location.href = '/messages';
}

const goToFavorites = () => {
  window.location.href = '/wishlist';
};

const goToSettings = () => {
  window.location.href = '/settings';

};

const goToCategory = (categoryId) => {
  router.push({ name: 'SearchProducts', query: { category: categoryId } });
};

const goToPerformance = () => {
  window.location.href = '/performance';
};

const logout = () => {
  console.log('Logging out...');
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  window.location.href = '/login';
};

watch(isLoggedIn, (newValue) => {
  console.log('Login state changed:', newValue);
});

onMounted(() => {

  fetchUserData();
  if("!isSearchPage") {
    fetchCategories();
  }
});
</script>


<style scoped>
.navbar {
  position: fixed;
  width: 100%;
  top: 0;
  left: 0;
  z-index: 10;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.menubar {
  display: flex;
  background-color: white;
  border-color: white;
  width: 100%;
  max-width: 80%;
  z-index: 1000;
  border-radius: 0;
  height: 80px;
  justify-content: center;
}

.menubar img.logo {
  width: 60px;
  height: auto;
  cursor: pointer;
  margin-right: 50px;
}

.search-bar {
  display: flex;
  align-items: center;
  position: relative;
}

.search-bar .p-inputtext {
  width: 250px;
  height: 40px;
  padding-right: 40px;
  font-size: 16px;
}

.search-button {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  right: 5px;
  height: 30px;
  width: 30px;
  color: #334155;
}

.search-button i {
  font-size: 15px;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.menubar-item {
  background: none;
  border: none;
  display: inline-block;
  cursor: pointer;
  font-size: 15px;
}

.cart.button {
  display: inline-flex;
  align-items: center;
}

.cart.button:hover .cart-icon,
.cart.button:hover .cart-text,
.login.button {
  color: #179739 !important;
  background-color: transparent !important;
}

.cart.button .cart-icon,
.cart.button .cart-text,
.login.button:hover {
  color: #334155 !important;
}
</style>
