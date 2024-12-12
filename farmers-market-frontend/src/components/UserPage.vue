<template>
  <div class="home">
  <Header class="navbar"></Header>
    <div class="content">
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
    <div class="user-info">
    <div class="user-header">
      <img
        :src="user.isFarmer ? farmerAvatar : customerAvatar"
        alt="User Avatar"
        class="user-header-avatar"
      />
      <h1 class="user-name">{{ user.firstName + ' ' + user.lastName }}</h1>

      <div v-if="user.isFarmer" style="margin-left: 0!important;; margin-top: 0!important; ">
        <Rating
          class="rating"
          v-model="user.rating"
          readonly
          :stars="5"
          :style="{
        '--p-rating-icon-size': '1.5rem'
      }"
        />
        <p v-if="user.reviewCount>0">Based on {{ user.reviewCount }} Reviews</p>
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
      <TabView class="tabview-panels">
        <TabPanel header="Farmer Reviews" class="tabview-panels">
          <CustomerReviews :id="id" :review-type="'farmer'" :canReview="user.canReview " />
        </TabPanel>

        <TabPanel header="Products" class="tabview-panels">
          <h2 class="header">Farmer Products</h2>
          <div >
            <div v-if="farmerProducts.length === 0">
              <p>No farmer products available.</p>
            </div>
            <div class="products-grid">
              <ProductCard
                v-for="product in farmerProducts"
                :key="product.id"
                :product="product"
              />
            </div>
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
body {
  display: block !important;
}
.home {
  display: flex;
  flex-direction: column;
  width: 100%!important;
  max-width: 100%!important;
  padding-top: 120px;
  align-items: center;
}
.content{
  min-height: 80vh;
  width: 80%;
}
@media (max-width: 380px) {
  .content{
    width: 90%;
  }
  .products-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr))!important;
  }
}
@media (max-width: 600px) {
  .user-header-avatar {
    width: 5rem!important;
  }
  .user-name {
    font-size: 1.3rem!important;
    font-weight: 500!important;
  }
   .rating{
  --p-rating-icon-size: '1.3rem'!important;
  }
}
@media (max-width: 1000px) {
  .products-grid {
    gap: 20px !important;
  }
}
.user-info {
  width: 100%;
}
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 30px;
  width:100%;
  justify-content: center;
  align-content: start;
}
.user-header {
  display: flex;
  gap: 2rem;
  flex-direction: row;
  flex-wrap: wrap;
  text-align: left;
  padding: 1.5rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 2rem;
  align-items: center;
  justify-content: end;
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
.tabview-panels {
  padding: 0!important;
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
  width: 6rem;
  height: auto;
  border-radius: 50%;
}

.user-name {
  font-size: 1.8rem;
  font-weight: bold;
  flex-grow: 1;
}

.load-more-button {
  margin-top: 2rem;
  width: 100%;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}

</style>
