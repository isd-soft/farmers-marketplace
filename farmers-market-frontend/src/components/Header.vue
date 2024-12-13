<template>
  <div class="navbar">
    <Menubar :model="items" class="menubar">
      <template #start>
        <img src="@/assets/logo.png" alt="Logo" class="logo" @click="goHome" />
      </template>

      <template #end>
        <div class="right-section">
          <div class="search-bar">
            <InputText v-model="searchQuery" />
            <Button @click="search" class="search-button">
              <i class="pi pi-search"></i>
            </Button>
          </div>
          <Menubar v-if="isLoggedIn" :model="accountMenu" class="menubar-item"></Menubar>
          <Button
            v-if="!isLoggedIn"
            @click="goToLogin"
            class="login button"
            label="Login"
            severity="primary"
            variant="text"
          ></Button>
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
import { ref, onMounted, watch } from 'vue';
import { isLoggedIn } from '@/shared/authState';
import axiosInstance from '@/utils/axiosInstance';
import Menubar from 'primevue/menubar';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

const searchQuery = ref('');

const items = ref([
  { label: 'Deals' },
  { label: "What's New" },
  { label: 'Delivery' },
]);

const accountMenu = ref([
  {
    label: 'Account',
    icon: 'pi pi-user',
    items: [
      { label: 'Orders', icon: 'pi pi-shopping-cart', command: () => goToOrders() },
      { label: 'Wishlist', icon: 'pi pi-heart', command: () => goToFavorites() },
      { label: 'Settings', icon: 'pi pi-cog', command: () => goToSettings() },
      { label: 'Logout', icon: 'pi pi-sign-out', command: () => logout() },
    ],
  },
]);

const fetchCategories = async () => {
  try {
    const response = await axiosInstance.get('/category');
    const categories = response.data;
    const categoryItems = categories.map(category => ({
      label: category.title,
      command: () => goToCategory(category.id),
    }));

    items.value = [{ label: 'Categories', items: categoryItems }, ...items.value];
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

const goHome = () => {
  window.location.href = '/';
};

const search = () => {
  console.log('Searching for:', searchQuery.value);
};

const goToLogin = () => {
  window.location.href = '/login';
};

const goToCart = () => {
  window.location.href = '/cart';
};

const goToOrders = () => {
  window.location.href = '/orders';
};

const goToFavorites = () => {
  window.location.href = '/wishlist';
};

const goToSettings = () => {
  window.location.href = '/settings';
};

const goToCategory = (categoryId) => {
  window.location.href = `/category/${categoryId}`;
};

const logout = () => {
  console.log('Logging out...');
  localStorage.removeItem('accessToken');
  window.location.href = '/login';
};

// Watch for changes in login state and react accordingly
watch(isLoggedIn, (newValue) => {
  console.log('Login state changed:', newValue);
});

onMounted(() => {
  fetchCategories();
});
</script>


<style scoped>
.menubar {
  display: flex;
  align-items: center;
  background-color: white;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  border-radius: 0;
  height: 80px;
}

.menubar img.logo {
  width: 60px;
  height: auto;
  cursor: pointer;
  margin-left: 40px;
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
  margin-right: 40px;
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
.login.button{
  color: #179739 !important;
  background-color: transparent !important;
}

.cart.button .cart-icon,
.cart.button .cart-text,
.login.button:hover{
  color: #334155 !important;
}

</style>
