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
                  placeholder="Sort by"
                  @change="fetchOrders"
                />
              </template>
              <template #list="slotProps">
                <div class="flex flex-col order-container">
                  <div v-for="order in orders" :key="order.id">
                    <p>{{ order.orderStatus }}</p>
                    <div
                      class="flex flex-col sm:flex-row sm:items-center p-6 gap-4 product-container"
                    >
                      <div
                        v-for="product in order.itemsInOrder"
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
                            <div class="first-product-content-section">
                              <h3 class="product-title-text">{{ product.productTitle }}</h3>
                              <p class="product-description">{{ product.productDescription }}</p>
                              <p class="product-quantity-type">{{ product.quantity }} {{ product.unitTypeShort }}</p>
                            </div>
                          </div>
                          <div class="second-product-content-section">
                            <p>Order Created On:</p>
                            <p class="order-date-text">{{formatOrderDate(order.createdDate) }}</p>
                          </div>

                          <i
                            :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
                            class="icons"
                            @click.stop="toggleWishlist(product)"
                            :title="product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'"
                          />

                        </div>
                      </div>
                        <div class="flex flex-col md:items-end gap-8 buttons-price-container">
                          <span class="text-xl font-semibold price-text"
                            >{{ order.totalPrice }} MDL</span>
                            <Button
                              v-if="order.orderStatus!=='DELIVERED' && order.orderStatus!=='CANCELLED'&& order.orderStatus!=='PENDING'"
                              icon="pi pi-check"
                              label="Order Recieved"
                              @click="orderDelivered(order)"
                              class="flex-auto md:flex-initial whitespace-nowrap update-button"
                            ></Button>
                          <Button
                            v-if="order.orderStatus==='DELIVERED'"
                            label="Leave a comment"
                            @click="leaveComment(order)"
                            class="flex-auto md:flex-initial whitespace-nowrap update-button"
                          ></Button>
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
import axiosInstance from '@/utils/axiosInstance'
import DataView from 'primevue/dataview'
import Button from 'primevue/button'
import Select from 'primevue/select'
import 'primeicons/primeicons.css'
import Rating from 'primevue/rating'
import Paginator from 'primevue/paginator';
import router from "@/router/index.js";

export default {
  name: 'SearchProducts',
  components: {
    Header,
    Footer,
    InputText,
    DataView,
    Button,
    Select,
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

//   const isHovered = ref(false)

// const modelValue = ref(4.5)
// const intPart = computed(() => Math.floor(modelValue.value))
// const decimalPart = computed(() => modelValue.value - Math.floor(modelValue.value))
// const isPartial = computed(() => decimalPart.value !== 0)
// const full = computed(() => `${decimalPart.value * 100}%`)


function toastAdd(severity, summary, detail, life = 2000) {
  toast.add({
    severity: severity,
    summary: summary,
    detail: detail,
    life: life,
  })
}


function formatOrderDate(dateString) {
  const options = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }
  return new Date(dateString).toLocaleDateString(undefined, options)
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


  const setStatusFilter = (status) => {
    selectedStatus.value = status;
    fetchOrders();
  };


function getBase64Image(base64String, imageType = 'jpeg') {
  return `data:image/${imageType};base64,${base64String}`
}

const leaveComment = async (order) =>{
  await router.push(`/id${order.farmer.id}`);
}
  const toggleWishlist = async (itemInCart) => {
    if (!itemInCart) return;
    try {
      if (itemInCart.isInWishlist) {
        await axiosInstance.delete(`/wishlist/${itemInCart.productId}`);
        orders.value.forEach((order) => {
          order.itemsInOrder.forEach((item) => {
            if (item.productId === itemInCart.productId) {
              item.isInWishlist = false;
            }
          });
        });
      } else {
        await axiosInstance.post(`/wishlist/${itemInCart.productId}`);
        orders.value.forEach((order) => {
          order.itemsInOrder.forEach((item) => {
            if (item.productId === itemInCart.productId) {
              item.isInWishlist = true;
            }
          });
        });
      }
    } catch (error) {
      console.error(
        `Failed to ${
          itemInCart.isInWishlist ? "remove" : "add"
        } product to/from wishlist:`,
        error.message
      );
    }
  };
 const onPageChange = (event) => {
    currentPage.value = event.page;
    fetchOrders();
  }
  const orderDelivered = async (order) => {
    const response = await axiosInstance.put(`/order/receive/${order.id}`)
    order.orderStatus = response.data.orderStatus
  }
  const fetchOrders = async () => {
    let sort = "createdDate";
    let dir = "DESC";
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
    toastAdd,
    onPageChange,
    formatOrderDate,
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
    toggleWishlist,
    intPart,
    orderDelivered,
    leaveComment,
  }
}}
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
}
.main-orders-container {
  margin: 0 auto;
  width: 80%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 1vw;
  position: relative;
}
.order-status-fitering-container,
.orders-container {
  border-radius: 15px;
  background-color: #fff;
  padding: 30px;
  height: max-content;
  min-width: max-content;
  width: 55%;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.order-status-fitering-container {
  width: 20%;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
  background-color: #fff;
  padding: 30px;
  border-radius: 15px;
}
.orders-container {
  width: 80%;
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
  height: 12vh;
  border-radius: 10px;
  min-width: 100px;
}
.product-quantity-type{
  font-size: 0.9rem;
  font-weight: 600;
}
.title-description-rating-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.first-product-content-section{
  width: 15vw;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.second-product-content-section{
  width: 15vw;
}
.order-date-text{
  font-weight: 600;
}
.product-title-text {
  font-size: 1.5rem;
}
.product-description {
  font-size: 0.7rem;
  height: max-content;
  max-width: 15vw;
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
  /* justify-content: flex-end; */
  gap: 2vh;
  padding: 3vh 3vh;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.product-content {
  display: flex;
  justify-content: space-between;
  width: 100%;
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
  justify-content: flex-end;
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
  padding-top: 20px;
}
.icons{
  font-size: 20px;
  cursor: pointer;
  color: #179739;
}
</style>
