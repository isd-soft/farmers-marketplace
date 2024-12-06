<template>
  <Header class="navbar"></Header>
  <div class="wishlist-page">
  <div class="wishlist-container">
    <Card class="wishlist-card" style="margin: 2rem auto; max-width: 100%">
      <template #content>
        <h2 class="wishlist-header">My Wishlist</h2>
        <div v-if="isLoading" class="loading-container">
          <ProgressSpinner
            style="width: 3rem; height: 3rem"
            strokeWidth="0.2rem"
            animationDuration="0.8s"
          />
          <p>Loading your wishlist...</p>
        </div>
        <div v-else-if="wishlist.length === 0">
          <p>No items in your wishlist yet.</p>
        </div>
        <div v-else class="wishlist-items">
          <div v-for="product in wishlist" :key="product.id" class="wishlist-item">
            <Card class="product-card" style="overflow: hidden">
              <template #header>
                <div class="header-content">
                  <img
                    alt="product image"
                    :src="product.image?.bytes ? 'data:image/png;base64,' + product.image.bytes : noPhotoImg"
                    class="product-image"
                  />
                  <router-link
                    :to="`/product/${product.id}`"
                    style="text-decoration: none; color: #3eb489"
                  >
                    <h3 class="product-title">{{ product.title }}</h3>
                  </router-link>
                </div>
              </template>
              <template #content>
                <div class="product-info">
                  <p class="product-price">
                    <span v-if="product.discountPercents && product.discountPercents > 0">
                      <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 1rem">
                        ${{ product.pricePerUnit }}
                      </s>
                      <span style="color: #007bff; font-size: 1.5rem">
                        ${{ discountedPrice(product.pricePerUnit, product.discountPercents) }}
                      </span>
                    </span>
                    <span v-else>
                      <span style="color: #007bff; font-size: 1.5rem">
                        ${{ product.pricePerUnit }}
                      </span>
                    </span>
                  </p>
                  <Rating
                    v-model="product.rating"
                    :stars="5"
                    readonly
                    style="margin-bottom: 1rem"
                  />
                </div>
              </template>
              <template #footer>
                <div class="footer-content">
                  <Button
                    label="Remove"
                    icon="pi pi-trash"
                    class="p-button-danger"
                    @click="removeFromWishlist(product.id)"
                  />
                </div>
              </template>
            </Card>
          </div>
        </div>
      </template>
    </Card>
  </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/axiosInstance.js'
import Rating from 'primevue/rating'
import Card from 'primevue/card'
import Button from 'primevue/button'
import ProgressSpinner from 'primevue/progressspinner'
import Header from '@/components/Header.vue'
import noPhotoImg from '@/assets/noPhoto.png'
import Footer from "@/components/Footer.vue";

export default {
  name: 'WishlistPage',
  components: {Footer, Header, Button, Rating, Card, ProgressSpinner },
  setup() {
    const isLoading = ref(true)
    const wishlist = ref([])

    const fetchWishlist = async () => {
      try {
        const response = await axiosInstance.get('/wishlist')
        wishlist.value = response.data
        console.log(wishlist.value)
      } catch (error) {
        console.error('Error fetching wishlist:', error)
      } finally {
        isLoading.value = false
      }
    }

    const discountedPrice = (price, discount) => price * ((100 - discount) / 100)

    const removeFromWishlist = async (id) => {
      try {
        await axiosInstance.delete(`/wishlist/${id}`)
        wishlist.value = wishlist.value.filter((product) => product.id !== id)
      } catch (error) {
        console.error('Error removing item from wishlist:', error)
      }
    }

    onMounted(() => {
      fetchWishlist()
    })

    return {
      isLoading,
      wishlist,
      discountedPrice,
      removeFromWishlist,
      noPhotoImg,
    }
  },
}

</script>

<style scoped>
.wishlist-page{
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
}

.wishlist-page > *:not(.footer) {
  margin-left: 5em;
  margin-right: 5em;
}
.wishlist-container {
  padding: 2rem;
  margin-top: 10vh;
}

.wishlist-header {
  text-align: center;
  margin-bottom: 2rem;
}

.loading-container {
  text-align: center;
  color: #555;
}

.wishlist-items {
  display: grid;
  gap: 2rem;
  width: 100%;
}
.wishlist-items {
  display: grid;
  grid-template-columns: repeat(3, 1fr);  /* 3 equal columns */
  gap: 2rem;
  width: 100%;
}

.wishlist-items > * {
  grid-column: span 1;
}

.wishlist-items:empty {
  display: none;  /* optional: hide the container if no items */
}


.wishlist-item {
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.product-card {
  padding: 1.5rem;
  width: 100%;
  display: flex;
  flex-direction: column; /* Ensures content is stacked vertically */
  justify-content: space-between; /* Ensure proper spacing */
  height: 100%; /* Ensures the card takes up the full height */
  text-align: center; /* Centers text within the card */
}

.header-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.product-title {
  margin: 0;
  font-size: 1.5rem;
}

.product-info {
  display: flex;
  flex-direction: column;
  align-items: center; /* Centers the content horizontally */
  justify-content: center; /* Centers the content vertically */
}

.product-price {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.product-image {
  width: 100%;
  height: 24em; /* Fixed height */
  object-fit: cover; /* Ensures the image maintains aspect ratio and covers the area */
}

.footer-content {
  display: flex;
  justify-content: center; /* Centers the footer content horizontally */
  align-items: center; /* Centers the button vertically */
}

@media (max-width: 64em) {
  /* 1024px and below (tablets) */
  .wishlist-items {
    grid-template-columns: repeat(2, 1fr); /* 2 items per row on medium screens */
  }
}

@media (max-width: 48em) {
  /* 768px and below (phones) */
  .wishlist-items {
    grid-template-columns: 1fr; /* 1 item per row on small screens */
  }
}
</style>
