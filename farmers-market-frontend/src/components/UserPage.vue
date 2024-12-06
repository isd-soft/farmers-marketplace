<template>
  <Header class="navbar"></Header>

  <div class="user-page">
    <div class="user-header">
      <img
        :src="user.isFarmer ? farmerAvatar : customerAvatar"
        alt="User Avatar"
        class="user-header-avatar"
      />
      <h1 class="user-name">{{ user.firstName + ' ' + user.lastName }}</h1>

      <div v-if="user.isFarmer" class="user-rating">
        <Rating v-model="user.rating" readonly :stars="5" />
        <p>Based on {{ user.reviewCount }} Reviews</p>
      </div>
    </div>

    <Button
      v-if="user.isFarmer && user.canMessage"
      label="Send Message"
      icon="pi pi-envelope"
      style="width: 15%; margin-bottom: 1%"
      class="p-button-rounded p-button-success"
      @click="showDialog = true"
    />
    <Dialog
      header="Send a Message"
      v-model:visible="showDialog"
      :modal="true"
      :closable="true"
      style="width: 40vw; max-width: 600px;"
    >
      <div>
        <textarea
          v-model="messageContent"
          placeholder="Write your message here..."
          class="p-inputtextarea p-component"
          rows="5"
          style="width: 100%; margin-bottom: 1rem; resize: none;"
        ></textarea>

        <div class="dialog-footer" style="text-align: right;">
          <Button
            label="Send"
            icon="pi pi-check"
            class="p-button-success"
            @click="sendMessage"
            style="max-width: 150px;"
          />
          <Button
            label="Cancel"
            icon="pi pi-times"
            class="p-button-secondary"
            @click="showDialog = false"
          />
        </div>
      </div>
    </Dialog>
    <div v-if="user.isFarmer">
    <TabView class="user-tabs">
        <TabPanel header="Farmer Reviews" v-if="user.isFarmer">
          <CustomerReviews :id="id" :review-type="'farmer'" />
        </TabPanel>

        <TabPanel header="Products" v-if="user.isFarmer">
          <h2 class="header">Farmer Products</h2>
          <div class="products-panel">
            <div v-if="farmerProducts.length === 0">
              <p>No farmer products available.</p>
            </div>
            <ProductCard
              v-for="product in farmerProducts"
              :key="product.id"
              :product="product"
            />
            <Button
              v-if="!isAllProductsLoaded"
              label="Load More Products"
              class="p-button-outlined load-more-button"
              @click="loadMoreProducts"
            />
            </div>
        </TabPanel>
    </TabView>
    </div>
    <div v-else>
      <ReviewsSection :userId="id" />
    </div>
    <Footer class="footer"></Footer>
  </div>

</template>

<script>
import { ref, onMounted } from 'vue';
import axiosInstance from '@/utils/axiosInstance.js';
import Card from 'primevue/card';
import Rating from 'primevue/rating';
import TabView from 'primevue/tabview';
import TabPanel from 'primevue/tabpanel';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import farmerAvatar from '@/assets/farmer.png';
import customerAvatar from '@/assets/customer.png';
import CustomerReviews from '@/components/CustomerReviews.vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import noPhotoImg from '@/assets/noPhoto.png';
import router from "@/router/index.js";
import ProductCard from "@/components/ProductCard.vue";
import ReviewsSection from '@/components/ReviewsSection.vue'; // Import ReviewsSection component

export default {
  name: 'UserPage',
  components: {
    ProductCard,
    Footer,
    Header,
    CustomerReviews,
    ReviewsSection, // Register ReviewsSection component
    Card,
    Rating,
    TabView,
    TabPanel,
    Button,
    Dialog,
  },
  props: ['id'],
  setup(props) {
    const user = ref({});
    const farmerProducts = ref([]);
    const isAllProductsLoaded = ref(false);
    const messageContent = ref('');
    const showDialog = ref(false);

    const fetchUser = async () => {
      try {
        const response = await axiosInstance.get(`/users/${props.id}`);
        user.value = response.data;
      } catch (error) {
        console.error('Failed to fetch user:', error);
      }
    };

    const fetchFarmerProducts = async (page = 0, pageSize = 5) => {
      try {
        const response = await axiosInstance.get(`/product/farmer/${props.id}/products`, {
          params: { page, size: pageSize },
        });
        farmerProducts.value.push(...response.data.content);
        if (farmerProducts.value.length >= response.data.totalElements) {
          isAllProductsLoaded.value = true;
        }
      } catch (error) {
        console.error('Failed to fetch farmer products:', error);
      }
    };

    const loadMoreProducts = async () => {
      const page = Math.ceil(farmerProducts.value.length / 5);
      await fetchFarmerProducts(page, 5);
    };

    const sendMessage = async () => {
      try {
        await axiosInstance.post('/messaging/user', {
          farmerId: user.value.id,
          content: messageContent.value,
        });
        alert('Message sent successfully!');
        showDialog.value = false;
        messageContent.value = '';
      } catch (error) {
        console.error('Failed to send message:', error);
      }
    };

    onMounted(async () => {
      await fetchUser();
      if (user.value.isFarmer) {
        await fetchFarmerProducts();
      }
    });

    return {
      user,
      farmerProducts,
      isAllProductsLoaded,
      messageContent,
      showDialog,
      sendMessage,
      farmerAvatar,
      customerAvatar,
      noPhotoImg,
      loadMoreProducts,
    };
  },
};
</script>

<style>
.user-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  padding-top: 80px;
}

.user-page > *:not(.footer) {
  margin-left: 5em;
  margin-right: 5em;
}

.navbar {
  position: fixed;
  width: 100%;
  top: 0;
  left: 0;
  z-index: 10;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-header {
  display: flex;
  align-items: center;
  padding: 2rem 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 2rem;
}
.user-rating {
  margin-left: 2em;
}
.user-header-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-right: 2rem;
}

.user-name {
  font-size: 2.5rem;
  font-weight: bold;
  margin: 0;
}

.user-tabs {
  font-size: 1.2rem;
}

.products-panel {
  padding: 2rem 1rem;
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.load-more-button {
  margin-top: 1rem;
  display: block;
  width: 100%;
}

.footer {
  margin-top: 2rem;
}
</style>
