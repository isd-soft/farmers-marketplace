<template>
  <div class="order">
    <Header class="navbar"></Header>
    <div class="main-container">
      <div class="main-orders-container">
        <div class="order-status-fitering-container">
          <div
            v-for="status in statuses"
            :key="status.id"
            class="order-staus-icons"
            :class="{ 'active-status': selectedStatus === status.type }"
            @click="setStatusFilter(status.type)"
          >
            <i :class="status.icon"></i>
            <p>{{ status.name }}</p>
          </div>
        </div>
        <div class="orders-container">
          <div class="card">
            <DataView :value="orders">
              <template #header>
                <Select
                  v-model="sortBy"
                  :options="sortOptions"
                  optionLabel="label"
                  placeholder="Sort By Price"
                  @change="fetchOrders"
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
                        v-for="(product, order) in order.itemsInOrder"
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
                              <p class="product-description">{{ product.productDescription }}</p>
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
    <Paginator
      style="margin-top: 30px"
      :rows="pageSize"
      :totalRecords="totalRecords"
      :first="currentPage * pageSize"
      @page="onPageChange"
    />
    <Footer class="footer"></Footer>
  </div>
</template>
<script>
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
import Paginator from 'primevue/paginator';
import Rating from 'primevue/rating'
export default {
  name: 'SearchProducts',
  components: {
    Header,
    Footer,
    InputText,
    DataView,
    Button,
    Select,
    Tag,
    Paginator,
    Rating,
  },
setup() {
  const statuses = ref([
    {id: null, type:null, name: "All", icon: "pi pi-list"},
    {id: 1, type:"PENDING", name: "Pending", icon: "pi pi-ellipsis-h"},
    {id: 2, type:"CONFIRMED", name: "Confirmed", icon: "pi pi-verified"},
    {id: 3, type:"INPROGRESS", name: "In Progress", icon: "pi pi-spinner-dotted"},
    {id: 4, type:"SHIPPED", name: "Shipped", icon: "pi pi-send"},
    {id: 5, type:"DELIVERED", name: "Delivered", icon: "pi pi-box"},
    {id: 6, type:"CANCELLED", name: "Cancelled", icon: "pi pi-times"},
  ]);
  const orders = ref([])
  const currentPage = ref(0);
  const selectedStatus = ref(null);
  const pageSize = ref(3);
  const totalRecords = ref(0);
  const sortOptions = ref([
    {label: "Sort by", value: null},
    {label: "Price Asc", value: "price_asc"},
    {label: "Price Desc", value: "price_desc"},
    {label: "Old to new", value: "date_asc"},
    {label: "New to old", value: "date_desc"},
  ]);
  const sortBy = ref("")
  const isHovered = ref(false)
  const modelValue = ref(4.5)
  const intPart = computed(() => Math.floor(modelValue.value))
  const decimalPart = computed(() => modelValue.value - Math.floor(modelValue.value))
  const isPartial = computed(() => decimalPart.value !== 0)
  const full = computed(() => `${decimalPart.value * 100}%`)
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
  const setStatusFilter = (status) => {
    selectedStatus.value = status;
    fetchOrders();
  };

  const goHome = () => {
    window.location.href = '/'
  }

  function getBase64Image(base64String, imageType = 'jpeg') {
    return `data:image/${imageType};base64,${base64String}`
  }

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
  const onPageChange = (event) => {
    currentPage.value = event.page;
    fetchOrders();
  }
  const fetchOrders = async () => {
    let sort = "";
    let dir = "";
    try {
      switch (sortBy.value.value) {
        case "price_asc":
          sort = "totalPrice"
          dir = "ASC"
          break;
        case "price_desc":
          sort = "totalPrice"
          dir = "DESC"
          break;
        case "date_asc":
          sort = "createdDate"
          dir = "ASC"
          break;
        case "date_desc":
          sort = "createdDate"
          dir = "DESC"
          break;
      }
      let url = `/order/management?`;
      if (selectedStatus.value) {
        url += `status=${selectedStatus.value}`;
      }
      url +=`&page=${currentPage.value}&size=${pageSize.value}&sort=${sort},${dir}`
      const response = await axiosInstance.get(url);
      console.log(response)
      orders.value = response.data.content;
      totalRecords.value = response.data.totalElements;
    } catch (error) {
    }
  }
  onMounted(() => {
    fetchOrders()
  });
  return {
    orders,
    onPageChange,
    pageSize,
    totalRecords,
    currentPage,
    sortOptions,
    sortBy,
    selectedStatus,
    fetchOrders,
    statuses,
    setStatusFilter,
    getBase64Image,
    isHovered,
    full,
    getSeverity,
    goHome,
    iconClass,
    onStarClick,
    toggleWishlist,
    intPart,
  }
},
}
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
  min-height: 50vh;
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
  gap: 2vh
}
.product-description {
  font-size: 0.9rem;
  height: max-content;
  max-width: 200px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
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
.product-content {
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
.active-status {
  color: #179739;
  font-weight: bold;
}
.footer {
  margin: 0;
  background-color: #fff;
  padding-top: 20px;
}
</style>
