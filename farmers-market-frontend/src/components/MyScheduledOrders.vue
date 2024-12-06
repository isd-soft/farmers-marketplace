<template>
  <div class="home">
    <Header class="navbar"></Header>
    <div style="width: calc(100% - 40px); max-width: 80%;display: flex; flex-direction: row; justify-content: space-between; gap: 20px">
      <h1>My scheduled orders</h1>
    </div>
    <table class="products-table">
      <thead>
      <tr>
        <th>Product</th>
        <th>Scheduled time</th>
        <th>Total price</th>
        <th>Quantity</th>
        <th>Active</th>
        <th class="actions">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="plannedOrder in plannedOrders" :key="plannedOrder.id">
        <td>
          <div  style="cursor: pointer" class="title-image" @click="goToProductPage(plannedOrder.product)">
            <div class="image-container">
              <img
                v-if="getFirstImage(plannedOrder.product.images)"
                :src="getFirstImage(plannedOrder.product.images)"
                alt="Product image"
                class="product-image"
              />
              <div v-else class="no-image">
                No Image
              </div>
            </div>
            {{ plannedOrder.product.title }}
          </div>
        </td>
        <td>{{plannedOrder.dayOfWeek.toLocaleLowerCase()}} {{plannedOrder.time}}</td>
        <td>{{ getTotalPrice(plannedOrder.product, plannedOrder.quantity) }}</td>
        <td>{{ plannedOrder.quantity }}</td>
        <td>
          <ToggleButton
            v-model="plannedOrder.active"
            :onLabel="'On'"
            :offLabel="'Off'"
            @change="toggleActivity(plannedOrder, plannedOrder.active)"
          />
        </td>
        <td class="actions">
          <div class="actions-container">
            <i @click="editPlannedOrder(plannedOrder)" class="pi pi-pencil action-icon"></i>
            <i @click="confirmDelete(plannedOrder)" class="pi pi-trash action-icon"></i>
          </div></td>
      </tr>
      </tbody>
    </table>
    <h3 style="margin-top: 50px; margin-bottom: 50px; text-align: center; width: 1200px" v-if="plannedOrders.length===0">Looks like you have no scheduled orders yet</h3>

    <Dialog style="flex-grow: 1; max-width: 500px" v-model:visible="showConfirmation" modal header="Delete scheduled order" :position="'top'">
      <div class="popup-content">
        <p>Are you sure you want to delete
          the scheduled order for product "{{plannedOrderToDelete.product.title}}"
          for {{plannedOrderToDelete.dayOfWeek.toLocaleLowerCase()}} {{plannedOrderToDelete.time}}?</p>
        <div class="popup-actions">
          <Button @click="cancelDelete" class="popup-actions-button">Cancel</Button>
          <Button @click="deletePlannedOrder" class="popup-actions-button">Yes, Delete</Button>
        </div>
      </div>
    </Dialog>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import {ref, onMounted, watch, computed, reactive} from "vue";
import axiosInstance from "@/utils/axiosInstance.js";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import Select from "primevue/select";
import ProductCard from "@/components/ProductCard.vue";
import Paginator from 'primevue/paginator';
import { PrimeIcons } from '@primevue/core/api';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import InputText from "primevue/inputtext";
import useVuelidate from "@vuelidate/core";
import {required, minValue, maxValue} from "@vuelidate/validators"
import ToggleButton from 'primevue/togglebutton';
export default {
  name: 'MyPlannedOrders',
  components: {
    ProductCard,
    Header,
    Footer,
    Select,
    Paginator,
    PrimeIcons,
    Dialog,
    Button,
    InputText,
    ToggleButton,
  },
  setup() {
    const plannedOrders = ref([]);

    const showConfirmation = ref(false);
    const plannedOrderToDelete = ref(null);
    const newDiscount = reactive({discountPercents : null});
    const rules = computed(() => ({
      discountPercents: { required, minValue: minValue(1), maxValue: maxValue(99) },
    }));
    const v$ = useVuelidate(rules, newDiscount);
    const confirmDelete = (plannedOrder) => {
      plannedOrderToDelete.value = plannedOrder;
      showConfirmation.value = true;
    };

    const toggleActivity = async (plannedOrder, active) => {
      console.log(active)
      try {
        const response = await axiosInstance.put(
          `/planned-order/active/${plannedOrder.id}`,
          { active: active }
        );
        plannedOrder.active = response.data.active;
      } catch (error) {
        console.error('Failed to update activity state:', error.message);
      }
    };

    const deletePlannedOrder = async () => {
      if (plannedOrderToDelete.value) {
        try {
          await axiosInstance.delete(`/planned-order/${plannedOrderToDelete.value.id}`);
          plannedOrders.value = plannedOrders.value.filter(
            (p) => p.id !== plannedOrderToDelete.value.id
          );
          plannedOrderToDelete.value = null;
          showConfirmation.value = false;
        } catch (error) {
          console.error('Failed to delete planned order:', error.message);
        }
      }
    };

    const cancelDelete = () => {
      plannedOrderToDelete.value = null;
      showConfirmation.value = false;
    };

    const fetchPlannedOrders = async () => {
      try {
        let url = `/planned-order/management`;
        const response = await axiosInstance.get(url);
        console.log(response)
        plannedOrders.value = response.data;
      } catch (error) {
      }
    }
    onMounted(() => {
      fetchPlannedOrders()
    });
    return {
      plannedOrders,
      fetchPlannedOrders,
      confirmDelete,
      cancelDelete,
      deletePlannedOrder,
      showConfirmation,
      plannedOrderToDelete,
      toggleActivity,
      v$,
    }
  },
  methods: {
    getTotalPrice(product, quantity){
      return (((product.pricePerUnit *((100 - (product.discountPercents || 0)) / 100))
          .toFixed(2))* quantity.toFixed(2));
    },
      getFirstImage(images) {
        if (Array.isArray(images) && images.length > 0 && images[0]?.bytes) {
          return `data:image/jpeg;base64,${images[0].bytes}`;
        }
        return "";
      },
    editPlannedOrder(plannedOrder) {
      this.$router.push(`/schedule-order/update/${plannedOrder.id}`);
    },
    goToProductPage(product) {
      this.$router.push(`/product/${product.id}`);
    },
  },
}
</script>
<style>
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

.products-table {
  width: calc(100% - 40px);
  max-width: 80%;
  border-collapse: collapse;
  margin-top: 40px;
}

.products-table th, .products-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
.action-icon{
  cursor: pointer;
}
.products-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}
.actions{
  text-align: center !important;
}
.actions-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 20px;
}
.actions-container-disc {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 20px;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}
.image-container {
  width: 50px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
  border-radius: 5px;
}

.product-image {
  width: 50px;
  height: 40px;
  object-fit: cover;
  border-radius: 5px;
}

.no-image {
  width: 50px;
  height: 40px;
  object-fit: cover;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  horiz-align: center;
  align-content: center;
  background-color: #17973930;
  font-size: 10px;
  text-align: center;
  border-radius: 5px;
}
.title-image{
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;
}

.popup-content {
  display: flex;
  flex-direction: column;
  text-align: center;
}
.popup-actions{
  display: flex;
  flex-direction: row;
  gap: 20px;
  margin-top: 20px;
  justify-content: center;
}
.popup-actions-button{
  max-width: 150px;
  flex-grow: 1;
}
</style>
