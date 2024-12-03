<template>
  <div class="order">
    <Header class="navbar"></Header>
    <div class="main-container">
      <div @click="goHome">
        <div
          class="home-text"
          :class="{ 'green-color': isHovered }"
          @mouseover="isHovered = true"
          @mouseleave="isHovered = false"
        >
          Home
        </div>
      </div>
      <div class="main-orders-container">
        <div class="order-status-fitering-container">
          <div class="order-staus-icons">
            <i class="pi pi-list"></i>
            <p>All</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-ellipsis-h"></i>
            <p>Pending (2)</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-verified"></i>
            <p>Confirmed</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-spinner-dotted"></i>
            <p>In Progress</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-send"></i>
            <p>Shipped</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-box"></i>
            <p>Delivered</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-times"></i>
            <p>Cancelled</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-check"></i>
            <p>Completed</p>
          </div>
        </div>
        <div class="orders-container">
          <div class="card">
            <DataView :value="orders" :sortOrder="sortOrder" :sortField="sortField">
              <template #header>
                <Select
                  v-model="sortKey"
                  :options="sortOptions"
                  optionLabel="label"
                  placeholder="Sort By Price"
                  @change="onSortChange"
                />
              </template>
              <template #list="slotProps">
                <div class="flex flex-col order-container">
                  <div v-for="order in orders" :key="order.id">
                    <div
                      class="flex flex-col sm:flex-row sm:items-center p-6 gap-4 product-container"
                      :class="{
                        'border-t border-surface-200 dark:border-surface-700': order !== 0,
                      }"
                    >
                      <div
                        v-for="(product, order) in order.products"
                        :key="product.id"
                        class="md:w-40 relative product-image-title-container"
                      >
                        <img
                          class="block xl:block mx-auto rounded w-full product-image"
                          :src="getBase64Image(product.imageBase64, product.imageType)"
                          :alt="product.productTitle"
                        />
                        <div
                          class="absolute bg-black/70 rounded-border product-content"
                          style="left: 4px; top: 4px"
                        >
                          <div class="title-description-rating-container">
                            <div>
                              <h2>{{ product.productTitle }}</h2>
                              <p>{{ product.productDescription }}</p>
                            </div>

                            <div :class="'stars-container'">
                              <span
                                :key="i"
                                v-for="i in 5"
                                @click="onStarClick($event, i)"
                                :class="['p-rating-icon', iconClass(i)]"
                                :style="{ '--full': i === intPart + 1 ? full : '100%' }"
                              ></span>
                              <span class="text-surface-900 font-medium text-sm">{{
                                product.rating
                              }}</span>
                              <!-- <Rating v-model="product.rating" :stars="5" :cancel="false" :readonly="true" />
                                <span class="text-surface-900 font-medium text-sm">{{ product.rating }}</span> -->
                            </div>
                          </div>

                          <div>
                            <Button
                              class="heart-button wishlist-icon"
                              outlined
                              
                              :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
                              @click="toggleWishlist(product)"
                              :title="
                                product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'
                              "
                            >
                            </Button>
                          </div>
                        </div>
                      </div>

                      <div
                        class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6"
                      >
                        <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                          <div class="bg-surface-100 p-1" style="border-radius: 30px">
                            <div
                              class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                              style="
                                border-radius: 30px;
                                box-shadow:
                                  0px 1px 2px 0px rgba(0, 0, 0, 0.04),
                                  0px 1px 2px 0px rgba(0, 0, 0, 0.06);
                              "
                            ></div>
                          </div>
                          <div class="bg-surface-100 p-1" style="border-radius: 30px"></div>
                        </div>
                        <div class="flex flex-col md:items-end gap-8 buttons-price-container">
                          <span class="text-xl font-semibold price-text"
                            >{{ order.totalPrice }} MDL</span
                          >
                          <div class="flex flex-row-reverse md:flex-row gap-2 buttons-container">
                            <!-- <div class="wishlist-container">
                        
                            </div> -->
                            <!-- icon="pi pi-heart" -->

                            <Button
                              icon="pi pi-user-edit"
                              label="Update Order Status"
                              :disabled="order.id === 'OUTOFSTOCK'"
                              class="flex-auto md:flex-initial whitespace-nowrap update-button"
                            ></Button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </DataView>
          </div>
        </div>
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>
<script setup>
import Header from '@/components/Header.vue'
import Footer from './Footer.vue'
import InputText from 'primevue/inputtext'
import { ref, onMounted, computed } from 'vue'
import axiosInstance from '@/utils/axiosInstance' // request with back-end and db
import DataView from 'primevue/dataview'
import Button from 'primevue/button'
import Select from 'primevue/select'
import Tag from 'primevue/tag'
import 'primeicons/primeicons.css'
import Rating from 'primevue/rating'

const orders = ref([])
const sortKey = ref()
const sortOrder = ref()
const sortField = ref()
const sortOptions = ref([
  { label: 'Price High to Low', value: '!price' },
  { label: 'Price Low to High', value: 'price' },
])

// const onSortChange = (event) => {
//   const value = event.value.value
//   if (value.startsWith('!')) {
//     sortOrder.value = -1
//     sortField.value = value.substring(1)
//   } else {
//     sortOrder.value = 1
//     sortField.value = value
//   }
//   sortKey.value = event.value
// }
const onSortChange = (event) => {
  const value = event.value.value

  if (value.startsWith('!')) {
    sortOrder.value = -1
    sortField.value = 'orderTotalPrice' // Sorting by orderTotalPrice
  } else {
    sortOrder.value = 1
    sortField.value = 'orderTotalPrice' // Sorting by orderTotalPrice
  }

  sortKey.value = event.value
}

// const onSortChange = (event) => {
//   const value = event.value.value
//   if (value.startsWith('!')) {
//     sortOrder.value = -1
//     sortField.value = value.substring(1) // Remove the "!" to get the field name
//   } else {
//     sortOrder.value = 1
//     sortField.value = value
//   }
//   sortKey.value = event.value

//   // Apply sorting to the orders array
//   if (sortField.value === 'orderTotalPrice') {
//     orders.value.sort((a, b) => {
//       if (sortOrder.value === 1) {
//         return a.orderTotalPrice - b.orderTotalPrice // Price Low to High
//       } else {
//         return b.orderTotalPrice - a.orderTotalPrice // Price High to Low
//       }
//     })
//   }
// }

const getSeverity = (product) => {
  switch (product.inventoryStatus) {
    case 'INSTOCK':
      return 'success'
    case 'LOWSTOCK':
      return 'warn'
    case 'OUTOFSTOCK':
      return 'danger'
    default:
      return null
  }
}

const goHome = () => {
  window.location.href = '/'
}

function getBase64Image(base64String, imageType = 'jpeg') {
  return `data:image/${imageType};base64,${base64String}`
}

const isHovered = ref(false)

const modelValue = ref(4.5)
const intPart = computed(() => Math.floor(modelValue.value))
const decimalPart = computed(() => modelValue.value - Math.floor(modelValue.value))
const isPartial = computed(() => decimalPart.value !== 0)
const full = computed(() => `${decimalPart.value * 100}%`)

function iconClass(i) {
  if (i <= modelValue.value) return 'pi pi-star' // Full star
  if (isPartial.value && i === intPart.value + 1) return 'pi pi-star partial' // Half star
  return 'pi pi-star-o' // Empty star
}

function onStarClick(event, i) {
  console.log(`Star ${i} clicked!`, event)
  modelValue.value = i
}

const toggleWishlist = async (product) => {
  if (!product.id) return

  try {
    if (product.isInWishlist) {
      await axiosInstance.delete(`/customer/wishlist/${product.id}`)
    } else {
      await axiosInstance.post(`/customer/wishlist/${product.id}`)
    }
    product.isInWishlist = !product.isInWishlist
  } catch (error) {
    console.error(
      `Failed to ${product.isInWishlist ? 'remove' : 'add'} product to/from wishlist:`,
      error.message,
    )
  }
}
onMounted(async () => {
  //onmounted when page loades, display the method inside, async waits for the request
  try {
    const response = await axiosInstance.get('/order') // Send request to server.
    orders.value = response.data // Assign response data to orders.
    console.log(orders.value)
  } catch (err) {
    console.error('Failed to fetch orders', err)
  }
})
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.order {
  display: flex;
  flex-direction: column;
  gap: 0;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
}
.main-container {
  position: relative;
  margin-top: 80px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  gap: 5vh;
  padding: 6vh;
  width: 100%;
  height: max-content;
  background-color: #f2f2f2;
}
.main-orders-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 1vw;
  position: relative;
}
.home-text {
  color: #8e90a7;
  font-size: 0.9rem;
  font-weight: 300;
  cursor: pointer;
  position: absolute;
  left: 19vw;
  margin: 0;
  padding: 10px;
}

.home-text:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .home-text {
    left: 16vw;
  }
}
@media (max-width: 700px) {
  .home-text {
    left: 14vw;
  }
}

@media (max-width: 526px) {
  .home-text {
    left: 10vw;
  }
}

@media (max-width: 425px) {
  .home-text {
    left: 2vw;
  }
}
.order-status-fitering-container,
.orders-container {
  border-radius: 15px;
  background-color: #fff;
  padding: 30px;
  height: max-content;
  min-width: max-content;
  width: 55%;
}
.order-status-fitering-container {
  width: 15%;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
  background-color: #fff;
  padding: 30px;
  border-radius: 15px;
}
.orders-container {
  width: 45vw;
}
.order-staus-icons {
  display: flex;
  gap: 1vw;
  align-items: center;
  cursor: pointer;
}
.order-staus-icons i {
  font-size: 1rem;
}

.product-image {
  width: 10vw;
  max-width: 10vw;
  height: auto;
  border-radius: 10px;
}

.title-description-rating-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-image-title-container {
  display: flex;
  gap: 2vw;
}
.product-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2vh;
  padding: 3vh 3vh;
  border-radius: 15px;
  overflow: hidden;
  box-shadow:
    0 4px 6px rgba(0, 0, 0, 0.1),
    0 1px 3px rgba(0, 0, 0, 0.06);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.product-content{
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 1rem; 
}
/* .heart-button {
  border: none;
  background: transparent;
}

.heart-button .pi {
  font-size: 1.5rem;
  color: #ff6b6b;
}

.heart-button.heart-selected .pi {
  color: #ff0000;
}

.heart-button:hover .pi {
  color: #ff4040;
} */
.order-container {
  margin-top: 2vh;
  display: flex;
  flex-direction: column;
  gap: 4vh;
}

.price-text {
  font-size: 1.5rem;
  font-weight: 600;
  color: black;
}

/* .stars {
  display: inline-block;
  font-size: 20px;
  color: #ffd700; /* Gold color for the stars 
}

.star {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-size: cover;
  background-image: url('path_to_your_star_image.svg'); /* Full star image 
}

/* Full star - 100% gold 
/* Full star - 100% gold 
.star.full {
  background-image: url('@/assets/star_full.svg'); /* Adjust path based on your project 
}

/* Half star - 50% gold 
.star.half {
  background-image: url('@/assets/star_half.svg'); /* Adjust path based on your project 
}

/* Empty star - transparent or gray 
.star.empty {
  background-image: url('@/assets/star_empty.svg'); /* Adjust path based on your project 
}  */

.stars-container {
  display: flex;
  gap: 4px;
}

.p-rating-icon {
  display: inline-block;
  font-size: 20px;
  color: #ffd700; /* Gold color for stars */
}

.p-rating-icon.partial {
  background: linear-gradient(to right, #ffd700 var(--full), #ccc var(--full));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.p-rating-icon.pi-star {
  color: #ffd700;
}

.p-rating-icon.pi-star-o {
  color: #ccc;
}
.heart-button {
  border: 1px solid #179739;
  color: #179739;
}
.heart-button:hover {
  border: 1px solid #0c4b1d;
}
.update-button {
  background-color: #179739;
}
.update-button:hover {
  background-color: #ffffff;
}
.buttons-price-container {
  display: flex;
  flex-direction: column;
  text-align: right;
  align-items: flex-end;
  gap: 2vh;
}
.buttons-container {
  display: flex;
  align-items: flex-end;
  gap: 1vw;
}
.footer {
  margin: 0;
  background-color: #fff;
  padding: 20px;
}
</style>
