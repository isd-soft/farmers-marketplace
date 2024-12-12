<template>
  <div class="navbar">
    <Menubar :model="items" class="menubar">
      <template #start>
        <img src="@/assets/logo.png" alt="Logo" class="logo" @click="goHome" />
      </template>

      <template #end>
        <div class="right-section">
          <div class="search-bar" v-if="!isSearchPage">
            <InputText @keydown.enter="search" v-model="searchQ" class="input-search" />
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
          <Button
            @click="goToCart"
            class="cart button"
            label="Cart"
            severity="secondary"
            variant="text"
          >
            <i class="pi pi-shopping-cart cart-icon"></i>
            <span class="cart-text">Cart</span>
          </Button>
        </div>
      </template>
    </Menubar>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed, onUnmounted } from 'vue';
import { isLoggedIn } from '@/shared/authState';
import axiosInstance, { getUserId } from '@/utils/axiosInstance';
import Menubar from 'primevue/menubar';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { useRoute, useRouter } from 'vue-router';
import { nextTick } from 'vue';

const hiddenItems = ref([]);
const searchQ = ref('');
let currentUser = null;
const items = ref([ {icon: 'pi pi-tags', label: "Products", command: () => search()}, {icon: 'pi pi-users', label: "Farmers", command: () => goToFarmersSearch()}]);
const updateItems = () => {
  const windowWidth = window.innerWidth;
  if (windowWidth < 965) {
    moveToAccountMenu('Categories');
  } else {
    moveToItems('Categories');
  }
  if (windowWidth < 1035) {
    moveToAccountMenu("Products");
  } else {
    moveToItems("Products");
  }
  if (windowWidth < 1170) {
    moveToAccountMenu("Farmers");
  } else {
    moveToItems("Farmers");
  }
};

const updateAccountMenu = () => {
  const windowWidth = window.innerWidth;
  const account = accountMenu.value.find((menu) => menu.label === 'Account');

  if (windowWidth <= 960) {
    if (account) {
      if (!account.items || account.items.length === 0) {
        accountMenu.value = [...account.items];
      }
    }
  } else {
    const isAccountPresent = accountMenu.value.some((menu) => menu.label === 'Account');
    if (!isAccountPresent) {
      accountMenu.value = [
        {
          label: 'Account',
          icon: 'pi pi-user',
          items: accountMenu.value,
        },
      ];
    }
  }
};

const moveToAccountMenu = (label) => {
  const itemIndex = items.value.findIndex((item) => item.label === label);
  if (itemIndex !== -1) {
    const item = items.value[itemIndex];
    const account = accountMenu.value.find((menu) => menu.label === 'Account');

    if (account) {
      const isAlreadyInAccountMenu = account.items.some(
        (existingItem) => existingItem.label === item.label,
      );
      if (!isAlreadyInAccountMenu) {
        hiddenItems.value.push(item);
        account.items.push(item);
        items.value.splice(itemIndex, 1);
      }
    }
  }
};

const moveToItems = (label) => {
  const hiddenIndex = hiddenItems.value.findIndex((item) => item.label === label);
  if (hiddenIndex !== -1) {
    const item = hiddenItems.value[hiddenIndex];
    const account = accountMenu.value.find((menu) => menu.label === 'Account');
    if (account && account.items.some((existingItem) => existingItem.label === item.label)) {
      items.value.push(item);
      account.items = account.items.filter((existingItem) => existingItem.label !== label);
      hiddenItems.value.splice(hiddenIndex, 1);
    }
  }
};

const accountMenu = ref([
  {
    label: 'Account',
    icon: 'pi pi-user',
    items: [
      { label: 'My page', icon: 'pi pi-home', command: () => goToMyPage() },
      { label: 'Orders', icon: 'pi pi-shopping-cart', command: () => goToOrders() },
      { label: 'Messages', icon: 'pi pi-envelope', command: () => goToMessages() },
      { label: 'Scheduled Orders', icon: 'pi pi-clock', command: () => goToScheduledOrders() },
      { label: 'Wishlist', icon: 'pi pi-heart', command: () => goToFavorites() },
      { label: 'Settings', icon: 'pi pi-cog', command: () => goToSettings() },
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

    items.value = [
      {
        label: 'Categories',
        items: categories.map((category) => ({
          label: category.title,
          command: () => goToCategory(category.id),
        })),
      },
      ...items.value,
    ];

    await nextTick();

    updateItems();
    updateAccountMenu();
  } catch (error) {
    console.error('Ошибка при загрузке категорий:', error);
  }
};
const fetchUserData = async () => {
  const middleIndex = Math.floor(accountMenu.value[0].items.length / 2);
  try {
    const response = await axiosInstance.get(`/current-user/`);
    currentUser = response.data;

    if (currentUser.isAdmin) {
      accountMenu.value[0].items = accountMenu.value[0].items.filter(
        (item) => !['Orders', 'Scheduled Orders', 'Wishlist'].includes(item.label),
      );
      accountMenu.value[0].items.splice(0, 0,
        {
          label: 'Server Info',
          icon: 'pi pi-exclamation-triangle',
          command: () => goToServerInfo()
        },
      );
      accountMenu.value[0].items.splice(0, 0, {
        label: 'Admin Panel',
        icon: 'pi pi-microchip',
        command: () => goToAdmin(),
      });
    }

    if (currentUser.isFarmer) {
      accountMenu.value[0].items.splice(1, 0, {
        label: 'My sales',
        icon: 'pi pi-credit-card',
        command: () => goToMySales(),
      }),
        accountMenu.value[0].items.splice(1, 0, {
          label: 'Products',
          icon: 'pi pi-clipboard',
          command: () => goToMyProducts(),
        });

      accountMenu.value[0].items.splice(middleIndex, 0, {
        label: 'Performance',
        icon: 'pi pi-chart-line',
        command: () => goToPerformance(),
      });
    }
    await nextTick();
    updateAccountMenu();
  } catch (error) {
    console.error(error);
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

const goToMyPage = () => {
  window.location.href = '/id' + currentUser.id;
};

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
};

const goToAdmin = () => {
  window.location.href = '/admin/products';
};

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
  localStorage.removeItem('userRole');
  window.location.href = '/login';
};

watch(isLoggedIn, (newValue) => {
  console.log('Login state changed:', newValue);
});
const handleResize = () => {
  updateItems();
  updateAccountMenu();
};
onMounted(async () => {
  try {
    await fetchUserData();

    if (!isSearchPage.value) {
      await fetchCategories();
    }

    updateItems();
    updateAccountMenu();

    window.addEventListener('resize', handleResize);
  } catch (error) {
    console.error(error);
  }
});
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.navbar {
  position: fixed;
  display: flex;
  justify-content: center;
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
  width: 80%;
  z-index: 1000;
  border-radius: 0;
  height: 80px;
  justify-content: center;
  padding: 0;
}
@media (max-width: 380px) {
  .menubar {
    width: 90%;
  }
  .menubar img.logo {
    width: 50px !important;
  }
}

.menubar img.logo {
  width: 60px;
  height: auto;
  cursor: pointer;
  margin-right: 10px;
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

.farmer.button {
  display: inline-flex;
  align-items: center;
}

.farmer.button:hover .farmer-icon,
.farmer.button:hover .farmer-text
 {
  color: #179739 !important;
  background-color: transparent !important;
}

.farmer.button .farmer-icon,
.farmer.button .farmer-text
 {
  color: #334155 !important;
}

@media (max-width: 740px) {
  .input-search {
    display: none;
  }
}
</style>
<style>
.p-menubar-mobile .p-menubar-root-list {
  margin-left: -40px;
  width: 190px !important;
  top: auto !important;
}
</style>
