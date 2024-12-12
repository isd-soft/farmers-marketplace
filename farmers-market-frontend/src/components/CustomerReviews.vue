<template>
  <Toast />
  <div>
    <div class="add-review" v-if="isAllowedToReview">
      <h3>Leave a Review</h3>
      <div>
        <Rating id="reviewRating" v-model="newReview.rating" :stars="5" @blur="v$.rating.$touch" />
        <span v-if="v$.rating.$error" class="error-message"> Rating is required. </span>
      </div>
      <textarea
        id="reviewTextarea"
        v-model="newReview.content"
        rows="6"
        placeholder="Write your review..."
        style="margin-top: 1em; width: 30em"
        @blur="v$.content.$touch"
      ></textarea>
      <span
        v-if="v$.content.$error"
        class="error-message"
        style="display: block; margin-top: 0.5em"
      >
        Review must be between 10 and 300 characters.
      </span>
      <Button @click="submitReview" style="background-color: green; width: 12em; margin: 1em">
        Submit Review
      </Button>
    </div>

    <div class="reviews-section">
      <div v-if="reviews.length > 0">
        <ul class="review-list">
          <li v-for="review in reviews" :key="review.id" class="review-item">
            <Card>
              <template #content>
                <Rating v-model="review.rating" readonly :stars="5" />
                <div class="author-name" :data-prefix="'Farmer:'">
                  <a :href="`/id${review.creator.id}`">
                    {{ review.creator.firstName }} {{ review.creator.lastName }}
                  </a>
                </div>
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
import { computed, onMounted, ref } from 'vue'
import Rating from 'primevue/rating'
import Card from 'primevue/card'
import Button from 'primevue/button'
import axiosInstance from '@/utils/axiosInstance.js'
import { required, minLength, maxLength, minValue } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import Toast from 'primevue/toast'
import { useToast } from 'primevue/usetoast'

export default {
  name: 'CustomerReviews',
  components: {
    Toast,
    Rating,
    Card,
    Button,
  },
  props: ['id', 'reviewType', 'canReview'],
  setup(props) {
    const reviews = ref([])
    const isAllowedToReview = ref(props.canReview)
    const newReview = ref({
      [`${props.reviewType}Id`]: props.id,
      rating: 0,
      content: '',
    })
    const toast = useToast();

    const currentPage = ref(0)
    const pageSize = ref(5)
    const isAllReviewsLoaded = ref(false)

    const fetchReviews = async () => {
      try {
        const reviewEndpoint =
          props.reviewType === 'product'
            ? `/reviews/products/${props.id}`
            : `/reviews/farmers/${props.id}`

        const response = await axiosInstance.get(reviewEndpoint, {
          params: { page: currentPage.value, size: pageSize.value },
        })
        reviews.value.push(...response.data.content)
        if (
          currentPage.value * pageSize.value + response.data.content.length ===
          response.data.totalElements
        ) {
          isAllReviewsLoaded.value = true
        }
      } catch (error) {
        console.error('Failed to load reviews:', error.message)
      }
    }

    const rules = computed(() => ({
      rating: { required, minValue: minValue(1) },
      content: { required, minLength: minLength(10), maxLength: maxLength(300) },
    }))

    const v$ = useVuelidate(rules, newReview)

    const loadMoreReviews = () => {
      currentPage.value += 1
      fetchReviews()
    }

    const submitReview = async () => {
      v$.value.$touch()
      if (v$.value.$invalid) {
        console.error('Validation failed:', v$.value.$errors)
        return
      }

      try {
        const reviewEndpoint =
          props.reviewType === 'product' ? `/reviews/products` : `/reviews/farmers`

        const response = await axiosInstance.post(reviewEndpoint, newReview.value)
        reviews.value.unshift(response.data)
        isAllowedToReview.value = false
        toast.add({
          severity: 'success',
          summary: 'Review added',
          detail: 'Thank you for review!',
          life: 4000,
        })
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
      isAllowedToReview,
      v$,
    }
  },
}
</script>

<style scoped>
.add-review {
  padding: 1em;
}

.add-review h3 {
  font-size: 1.5rem;
  font-weight: bold;
}

.add-review textarea {
  width: 30%;
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
  content: 'Author: ';
  color: #333;
  font-weight: normal;
  margin-right: 5px;
}
</style>
