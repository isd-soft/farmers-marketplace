<template>
  <div class="home">
    <Header class="navbar"></Header>
    <!-- Banner -->
    <main class="main-content">
      <div class="carousel-container">
        <Carousel :value="images" :numVisible="1" :numScroll="1" circular :autoplayInterval="6000">
          <template #item="slotProps">
            <img :src="slotProps.data" alt="Banner Image" class="carousel-image" />
          </template>
        </Carousel>
      </div>

      <!-- Info Section -->

      <div class="info-guide-container">
        <h2>
          Five-step journey of a product from the farmer to the customer through the marketplace
        </h2>
      </div>

      <!-- Carousel Products -->
      <div class="card flex flex-col items-center">
        <div class="guide-card" data-aos="fade-left">
          <div class="card-margin"></div>
          <div class="card-content">
            <img class="guide-img" src="@/assets/home_page_resources/guide-img1.png" alt="" />
            <div>
              <h4 class="card-title">Farming and Production</h4>
              <p class="card-description">
                The farmer grows high-quality produce, ensuring it meets the standards for freshness
                and quality.
              </p>
            </div>
          </div>
        </div>

        <div class="guide-card" data-aos="fade-right">
          <div class="card-content card2-content">
            <div class="reversed-text-guide">
              <h4 class="card-title">Listing on the Platform</h4>
              <p class="card-description">
                The farmer lists their product on the marketplace app, making it available for
                potential buyers.
              </p>
            </div>
            <img class="guide-img" src="@/assets/home_page_resources/guide-img2.png" alt="" />
          </div>
          <div class="card-margin card2-margin-color"></div>
        </div>

        <div class="guide-card" data-aos="fade-left">
          <div class="card-margin card3-margin-color"></div>
          <div class="card-content card3-content">
            <img class="guide-img" src="@/assets/home_page_resources/guide-img3.png" alt="" />
            <div>
              <h4 class="card-title">Customer Search and Match</h4>
              <p class="card-description">
                A customer in Chisinau, tired of standard store produce, searches for local,
                high-quality products and finds the farmer's listing.
              </p>
            </div>
          </div>
        </div>

        <div class="guide-card" data-aos="fade-right">
          <div class="card-content card4-content">
            <div class="reversed-text-guide">
              <h4 class="card-title">Order Creation</h4>
              <p class="card-description">
                The customer places an order, and the system processes the transaction, coordinating
                the delivery of the product.
              </p>
            </div>
            <img class="guide-img" src="@/assets/home_page_resources/guide-img4-2.png" alt="" />
          </div>
          <div class="card-margin card4-margin-color"></div>
        </div>

        <div class="guide-card" data-aos="fade-left">
          <div class="card-margin card5-margin-color"></div>
          <div class="card-content card5-content">
            <img class="guide-img" src="@/assets/home_page_resources/guide-img5-2.png" alt="" />
            <div>
              <h4 class="card-title">Completion and Satisfaction</h4>
              <p class="card-description">
                The customer receives the fresh produce, and the farmer receives payment; both
                parties are pleased with the transaction.
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- Carousel Products -->
      <div class="categories-section">
        <div v-for="category in categories" :key="category.id" class="category-carousel">
          <h2 class="category-title">{{ category.title }}</h2>
          <Carousel
            :value="productsByCategory[category.id]"
            :numVisible="5"
            :numScroll="1"
            :responsiveOptions="responsiveOptions"
            circular
            :autoplayInterval="autoplayInterval"
          >
            <template #item="slotProps">
              <div
                class="border border-surface-200 dark:border-surface-700 rounded m-2 p-4 product-content"
              >
                <img
                  :src="getBase64Image(slotProps.data.image.bytes, slotProps.data.imageType)"
                  :alt="slotProps.data.title"
                  class="w-full rounded product-image"
                />
                <p class="product-title">{{ slotProps.data.title }}</p>
                <div class="flex justify-between price-wishlist-container">
                  <p class="text-xl">{{ slotProps.data.pricePerUnit }} MDL</p>
                  <i
                    :class="slotProps.data.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
                    class="heart-icon"
                    @click.stop="toggleWishlist(slotProps.data)"
                    :title="
                      slotProps.data.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'
                    "
                  />
                </div>
              </div>
            </template>
          </Carousel>
        </div>
      </div>
    </main>
    <Footer class="footer"></Footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from './Header.vue'
import Footer from '../components/Footer.vue'
import Carousel from 'primevue/carousel'
import axiosInstance from '@/utils/axiosInstance'
import banner1 from '@/assets/home_page_resources/banner1.png'
import banner2 from '@/assets/home_page_resources/banner2.png'
import banner3 from '@/assets/home_page_resources/banner3.png'
import AOS from 'aos'
import 'aos/dist/aos.css'

const autoplayInterval = ref(4000)
const images = ref([banner1, banner2, banner3])
const productsByCategory = ref({})
const categories = ref([])
const aosOffset = ref(300)
const router = useRouter()
const responsiveOptions = ref([
  { breakpoint: '1400px', numVisible: 2, numScroll: 1 },
  { breakpoint: '1199px', numVisible: 3, numScroll: 1 },
  { breakpoint: '767px', numVisible: 2, numScroll: 1 },
  { breakpoint: '575px', numVisible: 1, numScroll: 1 },
])

function getBase64Image(base64String, imageType = 'jpeg') {
  return `data:image/${imageType};base64,${base64String}`
}
const toggleWishlist = async (product) => {
  if (!product.id) return
  try {
    if (product.isInWishlist) {
      await axiosInstance.delete(`/wishlist/${product.id}`)
    } else {
      await axiosInstance.post(`/wishlist/${product.id}`)
    }
    product.isInWishlist = !product.isInWishlist
  } catch (error) {
    console.error(
      `Failed to ${product.isInWishlist ? 'remove' : 'add'} product to/from wishlist:`,
      error.message,
    )
  }
}

const fetchProductsByCategory = async (categoryId) => {
  try {
    const response = await axiosInstance.get(`/product/getByCategory?categoryId=${categoryId}`)
    //   params: { categoryId: categoryId }
    productsByCategory.value[categoryId] = response.data
    console.log(
      `Fetched products for category ${categoryId}:`,
      productsByCategory.value[categoryId],
    )
  } catch (error) {
    console.error(`Failed to fetch products for category ${categoryId}:`, error)
  }
}

AOS.init({
  once: false,
  duration: 1000,
  offset: 100,
})

const handleResize = () => {
  if (window.innerWidth < 768) {
    autoplayInterval.value = 0
  } else {
    autoplayInterval.value = 4000
  }
}

onMounted(async () => {
  AOS.init({
    once: false,
    duration: 1500,
    offset: 700,
  })
  handleResize()
  window.addEventListener('resize', handleResize)
  try {
    const categoryResponse = await axiosInstance.get(`/category`)
    categories.value = categoryResponse.data
    console.log('Fetched categories:', categoryResponse.data)

    categories.value.forEach(async (category) => {
      fetchProductsByCategory(category.id)
    })
  } catch (error) {
    console.error('Failed to initialize categories and products:', error)
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

router.afterEach(() => {
  AOS.refresh()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=EB+Garamond:ital,wght@0,400..800;1,400..800&family=Funnel+Display:wght@300..800&family=Lavishly+Yours&family=League+Script&family=Lexend:wght@100..900&family=Onest:wght@100..900&family=Oswald:wght@200..700&family=Quicksand:wght@300..700&family=Rouge+Script&display=swap');
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.home {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  padding-top: 80px;
  color: #334155;
}
.main-content {
  width: 100%;
  flex: 1;
  min-height: 100vh;
}
.footer {
  text-align: center;
  padding: 10px;
  bottom: 0;
}
.info-guide-container {
  width: max-content;
  min-height: 10vh;
  border-bottom: 3px dashed #179739;
  display: flex;
  align-items: center;
  margin: 10vh auto;
}
.info-guide-container * {
  font-size: 1.8rem;
  text-align: center;
  padding: 2vw, 2vh;
  font-family: 'Lexend', sans-serif;
  font-weight: 900;
  color: #334155;
}

.carousel-container {
  width: 100%;
  height: auto;
}
.carousel-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  display: block;
}

.categories-section {
  padding: 5vh;
}
.category-carousel {
  margin-bottom: 40px;
}
.category-title {
  font-size: 1.5rem;
  margin-bottom: 10px;
  font-weight: bold;
  color: #334155;
  padding-left: 3vw;
}
.product-image {
  width: 100%;
  height: 18vh;
  object-fit: cover;
}
.product-content {
  width: 90%;
  margin: 2vh;
  border-radius: 6px;
  padding: 1.5vh;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
  color: #334155;
}

.product-title {
  font-weight: 700;
  font-size: 1.2rem;
}
.text-xl {
  font-weight: 600;
  font-size: 1.3rem;
  color: black;
}
.price-wishlist-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.items-center {
  margin: 5vh 6vw;
  display: flex;
  flex-direction: column;
  gap: 3vh;
}
.guide-card {
  width: 45vw;
  height: 22vh;
  display: flex;
  gap: 1vw;
}
.guide-card:nth-child(even) {
  margin-left: auto;
}

.card-margin {
  background-color: #179739;
  width: 1.5vw;
  border-radius: 5px;
}
.card2-margin-color {
  background-color: #bbdb2a;
}
.card3-margin-color {
  background-color: #ffdd00;
}
.card4-margin-color {
  background-color: #ff7b00;
}
.card5-margin-color {
  background-color: #ff4000;
}

.card-content {
  border: 3px solid #179739;
  border-radius: 7px;
  display: flex;
  gap: 2vw;
  justify-content: center;
  align-items: center;
  padding: 1vh;
}
.card2-content {
  border: 3px solid #bbdb2a;
}
.card3-content {
  border: 3px solid #ffdd00;
}
.card4-content {
  border: 3px solid #ff7b00;
}
.card5-content {
  border: 3px solid #ff4000;
}

.reversed-text-guide {
  text-align: right;
}

.guide-img {
  width: 11vw;
  height: auto;
}
.card-title {
  font-weight: 900;
  font-size: 1.7rem;
  font-family: 'Lexend', sans-serif;
  font-optical-sizing: auto;
  font-style: normal;
}
.heart-icon {
  color: #179739;
  border: 1px solid #8cdaa1;
  padding: 0.5rem;
  border-radius: 5px;
}

/* ANIMATION */
[data-aos] {
  transition-property: transform, opacity;
}

[data-aos] {
  transition-duration: 1s;
  transition-timing-function: ease-in-out;
}

[data-aos='fade-left'] {
  transform: translateX(-350px);
  opacity: 0;
}

[data-aos='fade-right'] {
  transform: translateX(350px);
  opacity: 0;
}

[data-aos].aos-animate {
  transform: translateX(0);
  opacity: 1;
}

@media screen and (max-width: 1200px) {
  .info-guide-container * {
    font-size: 1.3rem;
  }
  .guide-card {
    width: 70%;
    flex-direction: column;
    height: auto;
    gap: 2vh;
  }

  .card-content {
    flex-direction: column;
    text-align: center;
  }

  .guide-img {
    width: 30%;
  }
  .categories-section {
    padding: 3vh;
  }

  .category-title {
    font-size: 1.3rem;
    padding-left: 5vw;
  }

  .product-content {
    width: 90%;
  }

  .product-image {
    height: 20vh;
  }
}

@media screen and (max-width: 768px) {
  .info-guide-container * {
    font-size: 0.8rem;
  }
  .carousel-container {
    padding: 2vh;
  }

  .guide-card {
    flex-direction: column;
    align-items: center;
  }

  .card-title {
    font-size: 1.4rem;
  }

  .categories-section {
    padding: 2vh;
  }

  .product-title {
    font-size: 1rem;
  }

  .price-wishlist-container {
    flex-direction: column;
    gap: 0.5rem;
  }
}

@media screen and (max-width: 575px) {
  .info-guide-container {
    width: 90%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 2vh auto;
  }

  .info-guide-container * {
    font-size: 0.8rem;
    max-width: 100%;
    text-align: center;
  }
  .guide-card {
    gap: 1vh;
  }

  .card-content {
    padding: 1rem;
  }

  .categories-section {
    padding: 1vh;
  }

  .carousel-container {
    padding: 1vh;
  }

  .category-title {
    font-size: 1.3rem;
    padding-left: 12vw;
  }
  .price-wishlist-container {
    display: flex;
  }

  .product-content {
    padding: 1rem;
  }

  .product-title {
    font-size: 1.1rem;
  }

  .text-xl {
    font-size: 1rem;
  }
}
</style>
