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

      <!-- Display rating and review count only if the user is a farmer -->
      <div v-if="user.isFarmer" class="user-rating" >
        <Rating v-model="user.rating" readonly :stars="5" />
        <p>Based on {{ user.reviewCount }} Reviews</p>
      </div>
    </div>

    <div>
      <div v-if="user.isFarmer === true">
        <CustomerReviews :id="id" :review-type="'farmer'" />
      </div>
      <div v-else>
        <TabView class="user-tabs">
          <TabPanel header="Product Reviews">
            <div class="reviews-panel">
              <h2>Product Reviews</h2>
              <div v-if="productReviews.length > 0">
                <ul class="review-list">
                  <li v-for="review in productReviews" :key="review.id" class="review-item">
                    <Card>
                      <template #content>
                        <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                        <p>{{ review.content }}</p>
                        <p><strong>Product:</strong> {{ review.product.title }}</p>
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
              <div v-if="farmerReviews.length > 0">
                <ul class="review-list">
                  <li v-for="review in farmerReviews" :key="review.id" class="review-item">
                    <Card>
                      <template #content>
                        <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                        <p>{{ review.content }}</p>
                        <p>
                          <strong>Farmer:</strong> {{ review.farmer.firstName }}
                          {{ review.farmer.lastName }}
                        </p>
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
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/axiosInstance.js'
import Card from 'primevue/card'
import Rating from 'primevue/rating'
import TabView from 'primevue/tabview'
import TabPanel from 'primevue/tabpanel'
import Button from 'primevue/button'
import farmerAvatar from '@/assets/noPhoto.png'
import customerAvatar from '@/assets/noPhoto.png'
import CustomerReviews from '@/components/CustomerReviews.vue'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

export default {
  name: 'UserPage',
  components: {
    Footer,
    Header,
    CustomerReviews,
    Card,
    Rating,
    TabView,
    TabPanel,
    Button,
  },
  props: ['id'],
  setup(props) {
    const user = ref({})
    const productReviews = ref([])
    const farmerReviews = ref([])
    const newProductReview = ref({ customerId: props.id, rating: 0, content: '' })
    const newFarmerReview = ref({ customerId: props.id, rating: 0, content: '' })
    const isAllProductReviewsLoaded = ref(false)
    const isAllFarmerReviewsLoaded = ref(false)
    const loading = ref(true)

    const fetchUser = async () => {
      try {
        const response = await axiosInstance.get(`/users/${props.id}`)
        user.value = response.data
        console.log(user.value)
      } catch (error) {
        console.error('Failed to fetch user:', error)
      }
    }

    const fetchProductReviews = async (page = 0, pageSize = 5) => {
      try {
        const response = await axiosInstance.get(`customer/${props.id}/reviews/product`, {
          params: { page, pageSize },
        })
        if (response.data.length < pageSize) isAllProductReviewsLoaded.value = true
        productReviews.value.push(...response.data)
      } catch (error) {
        console.error('Failed to fetch product reviews:', error)
      }
    }

    const fetchFarmerReviews = async (page = 0, pageSize = 5) => {
      try {
        const response = await axiosInstance.get(`customer/${props.id}/reviews/farmer`, {
          params: { page, pageSize },
        })
        if (response.data.length < pageSize) isAllFarmerReviewsLoaded.value = true
        farmerReviews.value.push(...response.data)
      } catch (error) {
        console.error('Failed to fetch farmer reviews:', error)
      }
    }

    const submitProductReview = async () => {
      try {
        const response = await axiosInstance.post(
          '/customer/review/product',
          newProductReview.value,
        )
        productReviews.value.unshift(response.data)
        newProductReview.value = { customerId: props.id, rating: 0, content: '' }
      } catch (error) {
        console.error('Failed to submit product review:', error)
      }
    }

    const submitFarmerReview = async () => {
      try {
        const response = await axiosInstance.post('/customer/review/farmer', newFarmerReview.value)
        farmerReviews.value.unshift(response.data)
        newFarmerReview.value = { customerId: props.id, rating: 0, content: '' }
      } catch (error) {
        console.error('Failed to submit farmer review:', error)
      }
    }

    const loadMoreProductReviews = async () => {
      const page = Math.ceil(productReviews.value.length / 5)
      await fetchProductReviews(page, 5)
    }

    const loadMoreFarmerReviews = async () => {
      const page = Math.ceil(farmerReviews.value.length / 5)
      await fetchFarmerReviews(page, 5)
    }

    onMounted(async () => {
      loading.value = true
      await fetchUser()
      await fetchProductReviews()
      await fetchFarmerReviews()
      loading.value = false
    })

    return {
      user,
      productReviews,
      farmerReviews,
      newProductReview,
      newFarmerReview,
      loading,
      isAllProductReviewsLoaded,
      isAllFarmerReviewsLoaded,
      farmerAvatar,
      customerAvatar,
      submitProductReview,
      submitFarmerReview,
      loadMoreProductReviews,
      loadMoreFarmerReviews,
    }
  },
}
</script>
<style>
.user-page {
  width: 80%;
  margin: 0 auto;
  font-family: Arial, sans-serif;
  padding-top: 120px; /* Added to ensure the content does not overlap with the header */
  /* This padding pushes the page content down to avoid overlap */
}

.navbar {
  position: fixed; /* Fix the header at the top */
  width: 100%;
  top: 0;
  left: 0;
  z-index: 10; /* Ensures it stays on top */
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Adds shadow to distinguish from the content */
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

.footer {
  margin-top: 2rem;
}

</style>
