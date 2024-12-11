<template>
  <Header class="navbar"></Header>

  <Toast group="bc">
    <template #message="slotProps">
      <div class="custom-toast-container">
        <div class="custom-toast-content">
          <span class="custom-toast-summary">{{ slotProps.message.summary }}</span>
          <div class="custom-toast-detail">{{ slotProps.message.detail }}</div>
          <Button
            size="big"
            label="View Messages"
            severity="success"
            @click="redirectToMessages"
            class="custom-toast-button"
          />
        </div>
      </div>
    </template>
  </Toast>

  <div class="user-page">
    <div class="user-info">
    <div class="user-header">
      <img
        :src="user.isFarmer ? farmerAvatar : customerAvatar"
        alt="User Avatar"
        class="user-header-avatar"
      />
      <h1 class="user-name">{{ user.firstName + ' ' + user.lastName }}</h1>

      <div v-if="user.isFarmer" style="margin-left: 2em; margin-top: 1.5em" class="user-rating">
        <Rating
          v-model="user.rating"
          readonly
          :stars="5"
          :style="{
        '--p-rating-icon-size': '2rem'
      }"
        />
        <p>Based on {{ user.reviewCount }} Reviews</p>
      </div>
    </div>

    <Button
      v-if="user.isFarmer && user.canMessage"
      label="Send Message"
      icon="pi pi-envelope"
      style="width: 15%; margin-bottom: 1%;min-width: 8em"
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
            style="max-width: 12em;"
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
        <TabPanel header="Farmer Reviews">
          <CustomerReviews :id="id" :review-type="'farmer'" :canReview="user.canReview" />
        </TabPanel>

        <TabPanel header="Products">
          <h2 class="header">Farmer Products</h2>
          <div class="products-panel">
            <div v-if="farmerProducts.length === 0">
              <p>No farmer products available.</p>
            </div>
            <ProductCard
              v-for="product in farmerProducts"
              :key="product.id"
              :product="product"
              class="product-card"
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
import ReviewsSection from '@/components/ReviewsSection.vue';
import { useToast } from 'primevue/usetoast'
import Toast from 'primevue/toast'

export default {
  name: 'UserPage',
  components: {
    ProductCard,
    Footer,
    Header,
    CustomerReviews,
    ReviewsSection,
    Card,
    Rating,
    Toast,
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
    const toast = useToast();

    const fetchUser = async () => {
      try {
        const response = await axiosInstance.get(`/users/${props.id}`);
        user.value = response.data;
      } catch (error) {
        console.error('Failed to fetch user:', error);
      }
    };

    const fetchFarmerProducts = async (page = 0, pageSize = 6) => {
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
      const page = Math.ceil(farmerProducts.value.length / 6);
      await fetchFarmerProducts(page, 6);
    };
    const redirectToMessages = () => {
      router.push('/messages');
    };
    const sendMessage = async () => {
      try {
        await axiosInstance.post('/messaging/user', {
          farmerId: user.value.id,
          content: messageContent.value,
        });

        toast.add({
          severity: 'success',
          summary: 'Message Sent',
          detail: 'Your message was sent successfully!',
          life: 4000,
          group: 'bc'
        });

        showDialog.value = false;
        messageContent.value = '';
      } catch (error) {
        console.error('Failed to send message:', error);
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to send your message. Please try again later.',
          life: 4000,
        });
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
      redirectToMessages
    };
  },
};
</script>

<style scoped>
.user-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  padding-top: 80px;
}

.user-info {
  margin: 0 2rem;
}

.user-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 2rem;
  text-align: center;
}
.custom-toast-container {
  display: flex;
  flex-direction: column;
  padding: 1rem 1.5rem;
  border-radius: 8px;
  font-family: Arial, sans-serif;
  font-size: 1rem;
  color: #333;
  position: relative;
}

.custom-toast-summary {
  font-weight: bold;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.custom-toast-detail {
  font-size: 1rem;
  color: #666;
}

.custom-toast-container .p-toast-close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 1.5rem;
  background-color: transparent;
  border: none;
  cursor: pointer;
}

.custom-toast-button {
  margin-top: 1rem;
  padding: 0.5rem 1.5rem;
  font-weight: bold;
}

.user-header-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 1rem;
}

.user-name {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.user-rating {
  margin-top: 0.5rem;
}

.products-panel {
  padding: 1rem;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1.5rem;
}

.product-card {
  width: 100%;
  max-width: 16em;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.load-more-button {
  margin-top: 2rem;
  width: 100%;
}

@media (min-width: 768px) {
  .user-info {
    margin: 0 6rem;
  }

  .user-header {
    flex-direction: row;
    text-align: left;
  }

  .user-header-avatar {
    width: 120px;
    height: 120px;
    margin-right: 2rem;
    margin-bottom: 0;
  }

  .user-name {
    font-size: 2.5rem;
  }

  .user-rating {
    margin-top: 1.5rem;
    margin-left: 2em;
  }

  .products-panel {
    justify-content: flex-start;
    gap: 2rem;
  }
}

@media (min-width: 1024px) {
  .user-info {
    margin: 0 12rem;
  }
}


</style>
