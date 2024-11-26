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
          <div class="order-staus-icons" >
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
                  @change="onSortChange($event)"
                />
              </template>
              <template #list="slotProps">
                <div class="flex flex-col">
                  <div v-for="order in orders" :key="order.id">
                    <div
                      class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                      :class="{
                        'border-t border-surface-200 dark:border-surface-700': index !== 0,
                      }"
                    >
                      <div class="md:w-40 relative">
                        <img
                          class="block xl:block mx-auto rounded w-full"
                          :src="`https://primefaces.org/cdn/primevue/images/product/`"
                          :alt="order.products"
                        />
                        <div
                          class="absolute bg-black/70 rounded-border"
                          style="left: 4px; top: 4px"
                        >
                          <!-- <Tag :value="item.inventoryStatus" :severity="getSeverity(item)"></Tag> -->
                        </div>
                      </div>
                      <div
                        class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6"
                      >
                        <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                          <div>
                            <!-- <span
                              class="font-medium text-surface-500 dark:text-surface-400 text-sm"
                              >{{ item.category }}</span
                            > -->
                            <!-- <div class="text-lg font-medium mt-2">{{ item.name }}</div> -->
                          </div>
                          <div class="bg-surface-100 p-1" style="border-radius: 30px">
                            <div
                              class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                              style="
                                border-radius: 30px;
                                box-shadow:
                                  0px 1px 2px 0px rgba(0, 0, 0, 0.04),
                                  0px 1px 2px 0px rgba(0, 0, 0, 0.06);
                              "
                            >
                              <!-- <span class="text-surface-900 font-medium text-sm">{{
                                item.rating
                              }}</span> -->
                              <i class="pi pi-star-fill text-yellow-500"></i>
                            </div>
                          </div>
                        </div>
                        <div class="flex flex-col md:items-end gap-8">
                          <!-- <span class="text-xl font-semibold">${{ item.price }}</span> -->
                          <div class="flex flex-row-reverse md:flex-row gap-2">
                            <Button icon="pi pi-heart" class="green-color" outlined></Button>
                            <!-- <Button
                              icon="pi pi-shopping-cart"
                              label="Buy Now"
                              :disabled="item.inventoryStatus === 'OUTOFSTOCK'"
                              class="flex-auto md:flex-initial whitespace-nowrap green-color-background"
                            ></Button> -->
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
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/axiosInstance' // request with back-end and db
import DataView from 'primevue/dataview'
import Button from 'primevue/button'
import Select from 'primevue/select'
import Tag from 'primevue/tag'
import 'primeicons/primeicons.css'

const orders = ref([])
const sortKey = ref()
const sortOrder = ref()
const sortField = ref()
const sortOptions = ref([
  { label: 'Price High to Low', value: '!price' },
  { label: 'Price Low to High', value: 'price' },
])

const onSortChange = (event) => {
  const value = event.value.value
  if (value.startsWith('!')) {
    sortOrder.value = -1
    sortField.value = value.substring(1)
  } else {
    sortOrder.value = 1
    sortField.value = value
  }
  sortKey.value = event.value
}

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

const isHovered = ref(false)

onMounted(async () => {//onmounted when page loades, display the method inside, async waits for the request
  try {
    const response = await axiosInstance.get('/order');//send a request to server.
    orders.value = response.data;
    console.log(orders.value);
  } catch (err) {
    console.error('Failed to fetch orders', err);
  }
});

</script>

<style scoped>
*{
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.order {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
}
.main-container {
  position: fixed;
  margin-top: 80px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  gap: 5vh;
  padding: 6vh;
  width: 100%;
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
  color: #8E90A7;
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
}
.order-status-fitering-container {
  position: relative; 
  width: 15vw;
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
.green-color-background {
  background-color: #179739;
}
.green-color {
  color: #179739;
}
.footer {
  position: fixed;
  bottom: 0;
  background-color: #fff;
  padding: 20px;
}
</style>
