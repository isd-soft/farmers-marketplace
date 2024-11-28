<template>
  <div>
    <!-- Add Review Form -->
    <div class="add-review">
      <h3>Leave a Review</h3>
      <Rating v-model="newReview.rating" :stars="5" />
      <textarea
        v-model="newReview.content"
        rows="6"
        placeholder="Write your review..."
        style="margin-top: 1em"
      ></textarea>
      <Button
        @click="submitReview"
        style="background-color: green; width: 12em">
        Submit Review
      </Button>
    </div>

    <!-- Reviews Section -->
    <div class="reviews-section">
      <h3>Customer Reviews</h3>
      <div v-if="reviews.length > 0">
        <ul class="review-list">
          <li v-for="review in reviews" :key="review.id" class="review-item">
            <Card>
              <template #content>
                <Rating v-model="review.rating" :readOnly="true" :stars="5" />
                <strong>
                  {{ review.creator.firstName }} {{ review.creator.lastName }}
                </strong>
                <p>{{ review.content }}</p>
              </template>
            </Card>
          </li>
        </ul>
        <Button
          v-if="!isAllReviewsLoaded"
          label="Load More Reviews"
          class="p-button-text"
          style="margin-top: 20px"
          @click="loadMoreReviews"
        />
      </div>
      <div v-else>
        <p>No reviews yet. Be the first to leave one!</p>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import Rating from 'primevue/rating'
import Card from 'primevue/card'
import Button from 'primevue/button'
import axiosInstance from '@/utils/axiosInstance.js'

export default {
  name: 'CustomerReviews',
  components: {
    Rating,
    Card,
    Button,
  },
  props: ['id', 'reviewType'],
  setup(props) {
    const reviews = ref([])

    const newReview = ref({
      [`${props.reviewType}Id`]: props.id,
      rating: 0,
      content: ''
    })

    const currentPage = ref(0)
    const pageSize = ref(5)
    const isAllReviewsLoaded = ref(false)

    const fetchReviews = async () => {
      try {
        const response = await axiosInstance.get(`/${props.reviewType}/${props.id}/reviews`, {
          params: { page: currentPage.value, size: pageSize.value },
        })
        reviews.value.push(...response.data.content)
        if (currentPage.value * pageSize.value + response.data.content.length === response.data.totalElements) {
          isAllReviewsLoaded.value = true
        }
      } catch (error) {
        console.error('Failed to load reviews:', error.message)
      }
    }

    const loadMoreReviews = () => {
      currentPage.value += 1
      fetchReviews()
    }

    const submitReview = async () => {
      if (!newReview.value.rating || !newReview.value.content.trim()) {
        console.error('Rating and content are required.')
        return
      }
      try {
        const response = await axiosInstance.post(`customer/review/${props.reviewType}`, newReview.value)
        reviews.value.unshift(response.data)
        newReview.value = {
          [`${props.reviewType}Id`]: props.id,
          rating: 0,
          content: ''
        }
      } catch (error) {
        console.error('Failed to submit review:', error.message)
      }
    }

    onMounted(() => {
      fetchReviews()
    })

    return {
      reviews,
      newReview,
      currentPage,
      pageSize,
      isAllReviewsLoaded,
      loadMoreReviews,
      submitReview,
    }
  },
}
</script>

<style scoped>
.add-review {
  margin-bottom: 20px;
}

.add-review h3 {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.add-review textarea {
  width: 100%;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.add-review button {
  background-color: #007bff;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-review button:hover {
  background-color: #0056b3;
}

.reviews-section {
  margin-top: 20px;
}

.review-list {
  list-style: none;
  padding: 0;
}

.review-item {
  margin-bottom: 15px;
}

.review-item strong {
  font-weight: bold;
}
</style>
