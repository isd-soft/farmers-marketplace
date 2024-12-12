<template>
  <div class="home">
  <Header class="navbar"></Header>
  <div class="wishlist-page">
    <h1 style="text-align: center;">My wishlist</h1>
        <div v-if="isLoading" class="loading-container">
          <ProgressSpinner
            style="width: 3rem; height: 3rem"
            strokeWidth="0.2rem"
            animationDuration="0.8s"
          />
          <p>Loading your wishlist...</p>
        </div>
        <div v-else-if="wishlist.length === 0">
          <h1 style="text-align: center; margin-top: 50px; margin-bottom: 50px">No items in your wishlist yet.</h1>
        </div>
    <div class="products-grid">
      <ProductCard
        v-for="product in wishlist"
        :key="product.id"
        :product="product"
      />
    </div>
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
import ProductCard from "@/components/ProductCard.vue";

export default {
  name: 'WishlistPage',
  components: {ProductCard, Footer, Header, Button, Rating, Card, ProgressSpinner },
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

    onMounted(() => {
      fetchWishlist()
    })

    return {
      isLoading,
      wishlist,
      noPhotoImg,
    }
  },
}

</script>

<style scoped>
body{
  display: block !important;
}
.home{
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 120px;
  align-items: center;
}
.wishlist-page{
  position: relative;
  display: flex;
  flex-direction: column;
  width: 80%;
  min-height: 80vh;
}


.loading-container {
  text-align: center;
  color: #555;
}


.wishlist-items > * {
  grid-column: span 1;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 30px;
  width:100%;
}
@media (max-width: 380px) {
  .products-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  }
}
@media (max-width: 1000px) {
  .products-grid {
    gap: 20px !important;
  }
  .wishlist-page{
    width: 90%;
  }
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
