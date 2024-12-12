<template>
  <TabView class="reviews-tabs">
    <TabPanel header="Product Reviews">
      <div class="reviews-panel">
        <h2>Product Reviews</h2>
        <div style="margin-top: 15px" v-if="productReviews.length > 0">
          <ul class="review-list">
            <li v-for="review in productReviews" :key="review.id" class="review-item">
              <Card style="  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);">
                <template #content>
                  <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                  <p>{{ review.content }}</p>
                  <div class="author-name" :data-prefix="'Product:'">
                    <a :href="`/product/${review.product.id}`">
                      {{ review.product.title }}
                    </a>
                  </div>
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
          <p>No product reviews yet. Be the first to leave one!</p>
        </div>
      </div>
    </TabPanel>

    <TabPanel header="Farmer Reviews">
      <div class="reviews-panel">
        <h2>Farmer Reviews</h2>
        <div style="margin-top: 15px" v-if="farmerReviews.length > 0">
          <ul class="review-list">
            <li v-for="review in farmerReviews" :key="review.id" class="review-item">
              <Card style="  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);">
                <template #content>
                  <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                  <p>{{ review.content }}</p>
                  <div class="author-name" :data-prefix="'Farmer:'">
                    <a :href="`/id${review.farmer.id}`">
                      {{ review.farmer.firstName }} {{ review.farmer.lastName }}
                    </a>
                  </div>
                </template>
              </Card>
            </li>
          </ul>
          <Button
            v-if="!isAllFarmerReviewsLoaded"
            label="Load More Reviews"
            class="p-button-outlined"
            @click="loadMoreFarmerReviews"
          />
        </div>
        <div v-else>
          <p>No farmer reviews yet. Be the first to leave one!</p>
        </div>
      </div>
    </TabPanel>
  </TabView>
</template>

<script>
import { ref, onMounted } from 'vue';
import axiosInstance from '@/utils/axiosInstance.js';
import TabView from "primevue/tabview";
import TabPanel from "primevue/tabpanel";
import Button from "primevue/button";
import Rating from "primevue/rating";
import Card from "primevue/card";

export default {
  name: 'ReviewsSection',
  props: ['userId'],

  components: {
    Card,
    Rating,
    TabView,
    TabPanel,
    Button,
  },
  setup(props) {
    const productReviews = ref([]);
    const farmerReviews = ref([]);
    const isAllProductReviewsLoaded = ref(false);
    const isAllFarmerReviewsLoaded = ref(false);

    const fetchProductReviews = async (page = 0, pageSize = 5) => {
      try {
        const response = await axiosInstance.get(`/reviews/customers/${props.userId}/product-reviews`, {
          params: { page, size: pageSize },
        });
        productReviews.value.push(...response.data.content);
        if (productReviews.value.length >= response.data.totalElements) {
          isAllProductReviewsLoaded.value = true;
        }
      } catch (error) {
        console.error('Failed to fetch product reviews:', error);
      }
    };

    const fetchFarmerReviews = async (page = 0, pageSize = 5) => {
      try {
        const response = await axiosInstance.get(`/reviews/customers/${props.userId}/farmer-reviews`, {
          params: { page, size: pageSize },
        });
        farmerReviews.value.push(...response.data.content);
        if (farmerReviews.value.length >= response.data.totalElements) {
          isAllFarmerReviewsLoaded.value = true;
        }
        console.log(response.data);
      } catch (error) {
        console.error('Failed to fetch farmer reviews:', error);
      }
    };

    const loadMoreProductReviews = async () => {
      const page = Math.ceil(productReviews.value.length / 5);
      await fetchProductReviews(page, 5);
    };

    const loadMoreFarmerReviews = async () => {
      const page = Math.ceil(farmerReviews.value.length / 5);
      await fetchFarmerReviews(page, 5);
    };

    onMounted(async () => {
      await fetchProductReviews();
      await fetchFarmerReviews();
    });

    return {
      productReviews,
      farmerReviews,
      isAllProductReviewsLoaded,
      isAllFarmerReviewsLoaded,
      loadMoreProductReviews,
      loadMoreFarmerReviews,
    };
  },
};
</script>

<style scoped>
.reviews-tabs {
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

.author-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #007bff;
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.author-name a {
  font-weight: bold;
  color: #007bff;
  text-decoration: none;
  transition: color 0.3s ease;
}

.author-name a:hover {
  color: #0056b3;
}

.author-name::before {
  content: attr(data-prefix) " ";
  color: #333;
  font-weight: normal;
  margin-right: 5px;
}

.review-item {
  margin-bottom: 1.5rem;
}

</style>
