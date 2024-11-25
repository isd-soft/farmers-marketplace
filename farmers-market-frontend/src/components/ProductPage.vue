<template>
  <Header class="navbar"></Header>
  <Card :style="{ position: 'absolute', top: '10vh' }">
    <template #content>
      <div v-if="isLoading" class="loading-container">
        <div class="loading-content" style="text-align: center; padding: 20px">
          <ProgressSpinner
            style="width: 80px; height: 80px"
            strokeWidth="5"
            animationDuration="1s"
          />
          <p style="margin-top: 20px; font-size: 1.3rem; font-weight: bold; color: #555">
            Loading, please wait...
          </p>
        </div>
      </div>

      <div v-else-if="hasError" class="error-message">
        <div class="error-content" style="text-align: center; padding: 20px">
          <i class="pi pi-exclamation-circle" style="font-size: 3rem; color: #d9534f"></i>
          <p style="margin-top: 15px; font-size: 1.3rem; font-weight: bold; color: #d9534f">
            Oops! Something went wrong. Please try again later.
          </p>
          <Button
            label="Go Back"
            class="p-button-secondary"
            icon="pi pi-arrow-left"
            style="margin-top: 15px"
            @click="$router.go(-1)"
          />
        </div>
      </div>

      <div v-else>
        <div class="product-page">
          <!-- Product Section -->
          <div class="product-content">
            <!-- Galleria Component -->
            <div class="product-gallery">
              <Galleria
                :value="images"
                :responsiveOptions="responsiveOptions"
                :numVisible="5"
                containerStyle="max-height: auto; margin:0%;"
                :showItemNavigators="true"
              >
                <template #item="slotProps">
                  <img
                    :src="slotProps.item.itemImageSrc"
                    :alt="slotProps.item.alt"
                    class="gallery-image"
                  />
                </template>
                <template #thumbnail="slotProps">
                  <img
                    :src="slotProps.item.thumbnailImageSrc"
                    :alt="slotProps.item.alt"
                    class="thumbnail-image"
                  />
                </template>
              </Galleria>
            </div>

            <!-- Product Details -->
            <div class="product-details">
              <div class="product-name">{{ product.title || 'Product Name' }}</div>
              <div class="product-cost">
                <span v-if="product.discountPercents && product.discountPercents > 0">
                  <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 10px">
                    ${{ product.pricePerUnit }}
                  </s>
                  <span style="color: #007bff; font-size: 1.5rem">
                    ${{
                      product.pricePerUnit * ((100 - product.discountPercents) / 100).toFixed(2)
                    }}
                  </span>
                </span>
                <span v-else
                  ><span style="color: #007bff; font-size: 1.5rem">
                    ${{ product.pricePerUnit }}
                  </span>
                </span>
              </div>

              <div class="product-rating">
                <Rating v-model="product.rating" :stars="5" readonly />
                <span class="reviews">{{ product.reviewCount || 0 }} reviews</span>
              </div>
              <div class="product-quantity">Quantity:</div>
              <p>Currently available {{ product.quantity }}</p>
              <div class="quantity-selector">
                <InputNumber
                  v-model="quantity"
                  showButtons
                  buttonLayout="horizontal"
                  :inputStyle="{ width: '5em' }"
                  :min="1"
                  :max="product.quantity"
                >
                  <template #incrementbuttonicon>
                    <span class="pi pi-plus" />
                  </template>
                  <template #decrementbuttonicon>
                    <span class="pi pi-minus" />
                  </template>
                </InputNumber>
                <span class="unit-type">{{ product.unitType }}</span>
              </div>
              <div class="button-with-heart">
                <Button
                  class="add-to-cart-button"
                  @click="addToCart"
                  style="width: 12em; background-color: green; color: white; border: none; display: inline-flex; align-items: center; justify-content: center;"
                >
                  Add to Cart
                </Button>

                <i
                  :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
                  style="font-size: 2.5rem; cursor: pointer; color: red; margin-left: 10px; vertical-align: middle;"
                  @click="toggleWishlist"
                  :title="product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- Tabs Section -->
        <TabView>
          <TabPanel header="Details">
            <div class="tab-content">
              <p>{{ product.description || 'No description' }}</p>
            </div>
          </TabPanel>

          <TabPanel header="Reviews">
            <div class="tab-content">
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
                <Button @click="submitReview" style="background-color: green; width: 12em"
                  >Submit Review</Button
                >
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
                          <strong
                            >{{ review.creator.firstName }} {{ review.creator.lastName }}</strong
                          >
                          <p>{{ review.content }}</p>
                        </template>
                      </Card>
                    </li>
                  </ul>
                  <!-- Load More Button -->
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
          </TabPanel>

          <TabPanel header="Shipping">
            <div class="tab-content">
              <p>{{ product.shipping_info || 'Shipping information coming soon.' }}</p>
            </div>
          </TabPanel>
        </TabView>
      </div>
    </template>
  </Card>
</template>

<script>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/axiosInstance.js'
import bootsImg from '@/assets/boots.png'
import whatImg from '@/assets/what.jpg'
import Galleria from 'primevue/galleria'
import TabView from 'primevue/tabview'
import TabPanel from 'primevue/tabpanel'
import Rating from 'primevue/rating'
import Card from 'primevue/card'
import Button from 'primevue/button'
import InputNumber from 'primevue/inputnumber'
import ProgressSpinner from 'primevue/progressspinner'
import Header from '@/components/Header.vue'

export default {
  name: 'ProductPage',
  components: {
    Header,
    Galleria,
    Button,
    TabView,
    TabPanel,
    Rating,
    Card,
    InputNumber,
    ProgressSpinner,
  },
  props: ['id'],
  setup(props) {
    const product = ref({})
    const reviews = ref([])
    const newReview = ref({ productId: props.id, rating: 0, content: '' })
    const quantity = ref(1)
    const currentPage = ref(0) // Pagination state
    const pageSize = ref(5) // Page size
    const isAllReviewsLoaded = ref(false)

    const images = ref([
      { itemImageSrc: bootsImg, thumbnailImageSrc: bootsImg, alt: 'Boots Image' },
      { itemImageSrc: whatImg, thumbnailImageSrc: whatImg, alt: 'Farm Image' },
      { itemImageSrc: bootsImg, thumbnailImageSrc: bootsImg, alt: 'Boots Image' },
      { itemImageSrc: whatImg, thumbnailImageSrc: whatImg, alt: 'Farm Image' },
      { itemImageSrc: bootsImg, thumbnailImageSrc: bootsImg, alt: 'Boots Image' },
      { itemImageSrc: whatImg, thumbnailImageSrc: whatImg, alt: 'Farm Image' },
      { itemImageSrc: bootsImg, thumbnailImageSrc: bootsImg, alt: 'Boots Image' },
      { itemImageSrc: whatImg, thumbnailImageSrc: whatImg, alt: 'Farm Image' },
    ])

    const responsiveOptions = ref([
      { breakpoint: '1024px', numVisible: 3 },
      { breakpoint: '600px', numVisible: 1 },
    ])

    const isLoading = ref(true)
    const hasError = ref(false)

    const fetchProduct = async () => {
      try {
        const response = await axiosInstance.get(`/product/${props.id}/page`)
        console.log(response)
        product.value = response.data
        isLoading.value = false
      } catch (error) {
        console.error('Failed to load product:', error.message)
        hasError.value = true
        isLoading.value = false
      }
    }
    const toggleWishlist = async () => {
      if (!product.value.id) return

      try {
        if (product.value.isInWishlist) {
          await axiosInstance.delete(`/customer/wishlist/${props.id}`)
        } else {
          await axiosInstance.post(`/customer/wishlist/${props.id}`)
        }
        product.value.isInWishlist = !product.value.isInWishlist
      } catch (error) {
        console.error(
          `Failed to ${product.value.isInWishlist ? 'remove' : 'add'} product to/from wishlist:`,
          error.message,
        )
      }
    }
    const fetchReviews = async () => {
      try {
        const response = await axiosInstance.get(`/product/${props.id}/reviews`, {
          params: { page: currentPage.value, pageSize: pageSize.value },
        })
        console.log(response.data)
        console.log(currentPage.value * pageSize.value + response.data.content.length)

        reviews.value.push(...response.data.content)
        if (
          currentPage.value * pageSize.value + response.data.content.length ===
          response.data.totalElements
        ) {
          isAllReviewsLoaded.value = true
        }
        isLoading.value = false
      } catch (error) {
        console.error('Failed to load reviews:', error.message)
        hasError.value = true
        isLoading.value = false
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
        const response = await axiosInstance.post(`/customer/review/product`, newReview.value)
        reviews.value.unshift(response.data)
        newReview.value = { productId: props.id, rating: 0, content: '' }
      } catch (error) {
        console.error('Failed to submit review:', error.message)
      }
    }

    const addToCart = () => {
      console.log(
        `Added ${quantity.value} ${product.value.unitType} of ${product.value.name} to the cart.`,
      )
    }

    onMounted(() => {
      fetchProduct()
      fetchReviews()
    })

    return {
      product,
      reviews,
      newReview,
      quantity,
      images,
      responsiveOptions,
      isLoading,
      hasError,
      currentPage,
      pageSize,
      isAllReviewsLoaded,
      loadMoreReviews,
      addToCart,
      submitReview,
      toggleWishlist,
    }
  },
}
</script>

<style scoped>
.product-page {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.product-content {
  display: flex;
  width: 100%;
  max-width: 1200px;
  justify-content: space-between;
}

.product-gallery {
  flex: 2;
  margin-right: 20px;
}

.product-details {
  flex: 1;
  padding: 10px 20px;
}

.product-name {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
}
.gallery-image {
  width: 100%;
  height: auto;
  aspect-ratio: 3 / 2;
  object-fit: cover;
}

.thumbnail-image {
  width: 100%;
  height: auto;
  aspect-ratio: 3 / 2;
  object-fit: cover;
  padding: 5%;
}
.product-cost {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-rating {
  margin-top: 15px;
  display: flex;
  align-items: center;
}

.reviews {
  margin-left: 10px;
  font-size: 1rem;
  color: #666;
}

.product-quantity {
  margin-top: 20px;
  font-size: 1.2rem;
  color: #333;
  font-weight: bold;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.unit-type {
  font-size: 1rem;
  color: #333;
}

.add-to-cart-button {
  margin-top: 20px;
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-to-cart-button:hover {
  background-color: #0056b3;
}

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

.tab-content p {
  font-size: 1rem;
  color: #333;
}
</style>
