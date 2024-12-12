<template>
  <div class="home">
    <Header class="navbar"></Header>
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
                        class="product-image-title-container"
                      >
                        <img
                          class="product-image"
                          :src="getBase64Image(product.imageBase64, product.imageType)"
                          :alt="product.productTitle"
                        />
                        <div class="product-content" >
                          <div class="title-description-rating-container">
                              <h3 class="product-title-text">{{ product.productTitle }}</h3>
                              <p class="product-description">{{ product.productDescription }}</p>
                              <p class="product-quantity-type">{{ product.quantity }} {{ product.unitTypeShort }}</p>
                          </div>
                          <div class="second-product-content-section">
                            <p>Order Created On:</p>
                            <p class="order-date-text">{{formatOrderDate(order.createdDate) }}</p>
                          </div>
                        </div>
                      </div>
                      <div class="buttons-price-container">
                          <span class="text-xl font-semibold price-text"
                          >{{ order.totalPrice }} MDL</span>
                        <Button
                          v-if="order.orderStatus!=='DELIVERED' && order.orderStatus!=='CANCELLED'&& order.orderStatus!=='SHIPPED'"
                          label="Cancell order"
                          @click="cancellOrder(order)"
                          class="flex-auto md:flex-initial whitespace-nowrap update-button"
                        ></Button>
                        <Button
                          v-if="order.orderStatus==='PENDING'"
                          label="Confirm order"
                          @click="confirmOrder(order)"
                          class="flex-auto md:flex-initial whitespace-nowrap update-button"
                        ></Button>
                        <Button
                          v-if="order.orderStatus==='CONFIRMED'"
                          label="In progress"
                          @click="inProgressOrder(order)"
                          class="flex-auto md:flex-initial whitespace-nowrap update-button"
                        ></Button>
                        <Button
                          v-if="order.orderStatus==='INPROGRESS'"
                          label="Shipped"
                          @click="shippedOrder(order)"
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
    const cancellOrder = async (order) => {
      const response = await axiosInstance.put(`/order/${order.id}`,{
        orderStatus: "CANCELLED"
      })
      order.orderStatus = response.data.orderStatus
    }
    const confirmOrder = async (order) => {
      const response = await axiosInstance.put(`/order/${order.id}`,{
        orderStatus: "CONFIRMED"
      })
      order.orderStatus = response.data.orderStatus
    }
    const inProgressOrder = async (order) => {
      const response = await axiosInstance.put(`/order/${order.id}`,{
        orderStatus: "INPROGRESS"
      })
      order.orderStatus = response.data.orderStatus
    }
    const shippedOrder = async (order) => {
      const response = await axiosInstance.put(`/order/${order.id}`,{
        orderStatus: "SHIPPED"
      })
      order.orderStatus = response.data.orderStatus
    }

    function getBase64Image(base64String, imageType = 'jpeg') {
      return `data:image/${imageType};base64,${base64String}`
    }
    const onPageChange = (event) => {
      currentPage.value = event.page;
      fetchOrders();
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
        let url = `/order/farmer/management?`;
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
      intPart,
      cancellOrder,
      confirmOrder,
      inProgressOrder,
      shippedOrder,
    }
  }}
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
@media  (max-width: 1000px){
  .home {
    padding-top: 80px;
  }
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.main-orders-container {
  position: relative;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
  width: 80%;
}
@media (max-width: 380px) {
  .main-orders-container{
    width: 90%;
  }
}
.order-status-fitering-container,
.orders-container {
  flex-grow: 1;
  border-radius: 15px;
  background-color: #fff;
  padding: 30px;
  height: max-content;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.order-status-fitering-container {
  width: 10%;
  min-width: 100px;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
}
.orders-container {
  min-width: 250px;
  width: 70%;
}
@media (max-width: 500px) {
  .order-status-fitering-container,
  .orders-container {
    padding: 13px !important;
  }
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
  object-fit: cover;
}
.product-quantity-type{
  font-size: 0.9rem;
  font-weight: 600;
}
.title-description-rating-container {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  justify-content: space-between;
}
.second-product-content-section{
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  max-width: 250px;
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
  max-width: 200px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.product-image-title-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 20px;
}
.product-container {
  display: flex;
  flex-direction: column;
  gap: 2vh;
  padding: 3vh 3vh;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.product-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  flex-grow: 1;
}
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
  gap: 20px;
  flex-wrap: wrap;
}
.active-status {
  color: #179739;
  font-weight: bold;
}
.footer {
  margin: 0;
  padding-top: 20px;
}
</style>
