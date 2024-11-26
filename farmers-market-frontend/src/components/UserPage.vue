<template>
  <div class="user-page">
    <!-- Large Header Panel -->
    <div class="user-header">
      <img
        :src="user.isFarmer ? farmerAvatar : customerAvatar"
        alt="User Avatar"
        class="user-header-avatar"
      />
      <h1 class="user-name">{{ user.firstName + ' ' + user.lastName }}</h1>
    </div>

    <!-- TabView Below -->
    <TabView class="user-tabs">
      <!-- Product Reviews Tab -->
      <TabPanel header="Product Reviews">
        <div class="reviews-panel">
          <h2>Product Reviews</h2>
          <div v-if="productReviews.length > 0">
            <ul class="review-list">
              <li v-for="review in productReviews" :key="review.id" class="review-item">
                <Card>
                  <template #content>
                    <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                    <strong>{{ review.creator.firstName }} {{ review.creator.lastName }}</strong>
                    <p>{{ review.content }}</p>
                  </template>
                </Card>
              </li>
            </ul>
            <Button
              v-if="!isAllProductReviewsLoaded"
              label="Load More Reviews"
              class="p-button-outlined"
              @click="loadMoreProductReviews"
            />
          </div>
          <div v-else>
            <p>No reviews yet. Be the first to leave one!</p>
          </div>
        </div>
      </TabPanel>

      <!-- Customer Reviews Tab -->
      <TabPanel header="Farmer Reviews">
        <div class="reviews-panel">
          <h2>Customer Reviews</h2>
          <div v-if="customerReviews.length > 0">
            <ul class="review-list">
              <li v-for="review in customerReviews" :key="review.id" class="review-item">
                <Card>
                  <template #content>
                    <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                    <strong>{{ review.creator.firstName }} {{ review.creator.lastName }}</strong>
                    <p>{{ review.content }}</p>
                  </template>
                </Card>
              </li>
            </ul>
            <Button
              v-if="!isAllCustomerReviewsLoaded"
              label="Load More Reviews"
              class="p-button-outlined"
              @click="loadMoreCustomerReviews"
            />
          </div>
          <div v-else>
            <p>No reviews yet. Be the first to leave one!</p>
          </div>
        </div>
      </TabPanel>
    </TabView>
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
import farmerAvatar from '@/assets/noPhoto.png';
import customerAvatar from '@/assets/noPhoto.png';

export default {
  name: 'UserPage',
  components: {
    Card,
    Rating,
    TabView,
    TabPanel,
    Button,
  },
  props: ['id'],
  setup(props) {
    const user = ref({});
    const productReviews = ref([]);
    const customerReviews = ref([]);
    const isAllProductReviewsLoaded = ref(false);
    const isAllCustomerReviewsLoaded = ref(false);
    const loading = ref(true);

    // Fetch User Info
    const fetchUser = async () => {
      try {
        const response = await axiosInstance.get(`/users/${props.id}`);
        user.value = response.data;
        console.log(response.data);
      } catch (error) {
        console.error('Failed to fetch user:', error);
      }
    };

    // Fetch Product Reviews
    const fetchProductReviews = async () => {
      try {
        const response = await axiosInstance.get(`customer/${props.id}/reviews/product`,{
          params: { page: 0, pageSize: 5 },
        });
        productReviews.value = response.data;
      } catch (error) {
        console.error('Failed to fetch product reviews:', error);
      }
    };

    // Fetch Farmer Reviews
    const fetchFarmerReviews = async () => {
      try {
        const response = await axiosInstance.get(`/users/${props.id}/customer-reviews`,{
          params: { page: 0, pageSize: 5 },
        });
        customerReviews.value = response.data;
      } catch (error) {
        console.error('Failed to fetch farmer reviews:', error);
      }
    };

    const loadMoreProductReviews = async () => {
      // Add pagination logic here if needed
    };

    const loadMoreCustomerReviews = async () => {
      // Add pagination logic here if needed
    };

    onMounted(async () => {
      loading.value = true;
      await fetchUser();
      await fetchProductReviews();
      await fetchFarmerReviews();
      loading.value = false;
    });

    return {
      user,
      productReviews,
      customerReviews,
      loading,
      isAllProductReviewsLoaded,
      isAllCustomerReviewsLoaded,
      farmerAvatar,
      customerAvatar,
      loadMoreProductReviews,
      loadMoreCustomerReviews,
    };
  },
};
</script>

<style>
.user-page {
  width: 80%;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

.user-header {
  display: flex;
  align-items: center;
  padding: 2rem 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 2rem;
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

.reviews-panel {
  padding: 2rem 1rem;
}

.review-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.review-item {
  margin-bottom: 1.5rem;
}

h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}
</style>
