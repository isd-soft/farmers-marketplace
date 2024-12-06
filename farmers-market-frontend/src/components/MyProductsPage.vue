<template>
  <div class="home">
    <Header class="navbar"></Header>
    <div style="width: calc(100% - 40px); max-width: 80%;display: flex; flex-direction: row; justify-content: space-between; gap: 20px">
    <h1>My products</h1>
    <Button style="max-width: 200px; flex-grow: 1" @click="goToCreateProduct">Create new product</Button>
    </div>
    <table class="products-table">
      <thead>
      <tr>
        <th>Product</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Orders</th>
        <th>Discount</th>
        <th class="actions">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="product in products" :key="product.id">
        <td>
          <div  class="title-image" @click="goToProductPage(product)">
            <div class="image-container">
            <img
              v-if="product.image"
              :src="getFirstImage(product.image)"
              alt="Product image"
              class="product-image"
            />
              <div v-else class="no-image">
                No Image
              </div>
            </div>
            {{ product.title }}
          </div>
        </td>
        <td>{{ product.pricePerUnit }}/{{ product.unitTypeShort }}</td>
        <td>{{ product.quantity }}</td>
        <td>{{ product.orders }}</td>
        <td>
        <div>
          <Button
            v-if="!product.discountPercents"
            @click="setDiscountProduct(product)"
            style="width: 20px; height: 20px; padding: 0; border-style: none; font-size: 14px; line-height: 0px; display: flex; justify-content: center; align-items: center;"
          >+</Button>
          <div class="actions-container-disc" v-else>
            {{ product.discountPercents }}%
            <i class="pi pi-pencil action-icon" @click="setDiscountProduct(product)"></i>
            <i class="pi pi-trash action-icon" @click="removeDiscount(product)"></i>
          </div>
        </div>
        </td>
        <td class="actions">
          <div class="actions-container">
            <i @click="editProduct(product)" class="pi pi-pencil action-icon"></i>
            <i v-if="product.orders<1" @click="confirmDelete(product)" class="pi pi-trash action-icon"></i>
            <ToggleButton
              style="font-size: 10px; max-width: fit-content"
              v-model="product.visible"
              :onLabel="'Visible'"
              :offLabel="'Invisible'"
              @change="toggleActivity(product, product.visible)"
            />
          </div></td>
      </tr>
      </tbody>
    </table>
    <h3 style="margin-top: 50px; margin-bottom: 50px; text-align: center; width: 1200px" v-if="products.length===0">Looks like you have no products yet</h3>

    <Dialog style="flex-grow: 1; max-width: 500px" v-model:visible="showDiscountDialog" modal header="Set Discount" :position="'top'">
      <div class="popup-content">
        <p v-if="selectedProduct && selectedProduct.discountPercents > 0">
          Current discount: {{ selectedProduct.discountPercents }}%
        </p>
        <p v-else>No discount applied.</p>
        <InputText v-model="newDiscount.discountPercents" placeholder="Enter discount value (%)" style="margin-top: 10px;" />
        <span v-if="v$.discountPercents.$error" class="error-message">
        Discount is required and must be minimum 1 and maximum 99</span>
        <div class="popup-actions">
          <Button @click="cancelDiscount" class="popup-actions-button">Cancel</Button>
          <Button @click="applyDiscount" class="popup-actions-button">Apply</Button>
        </div>
      </div>
    </Dialog>

    <Dialog style="flex-grow: 1; max-width: 500px" v-model:visible="showConfirmation" modal header="Delete product" :position="'top'">
      <div class="popup-content">
        <p>Are you sure you want to delete the product "{{ productToDelete?.title }}"?</p>
        <div class="popup-actions">
          <Button @click="cancelDelete" class="popup-actions-button">Cancel</Button>
          <Button @click="deleteProduct" class="popup-actions-button">Yes, Delete</Button>
        </div>
      </div>
    </Dialog>


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
import {ref, onMounted, watch, computed, reactive} from "vue";
import axiosInstance from "@/utils/axiosInstance.js";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import Select from "primevue/select";
import ProductCard from "@/components/ProductCard.vue";
import {useRoute} from "vue-router";
import Paginator from 'primevue/paginator';
import { PrimeIcons } from '@primevue/core/api';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import InputText from "primevue/inputtext";
import useVuelidate from "@vuelidate/core";
import {required, minValue, maxValue} from "@vuelidate/validators"
import router from "@/router/index.js";
import ToggleButton from "primevue/togglebutton";
export default {
  name: 'SearchProducts',
  components: {
    ToggleButton,
    ProductCard,
    Header,
    Footer,
    Select,
    Paginator,
    PrimeIcons,
    Dialog,
    Button,
    InputText,
  },
  setup() {
    const products = ref([]);
    const route = useRoute();
    const currentPage = ref(0);
    const pageSize = ref(6);
    const totalRecords = ref(0);

    const showConfirmation = ref(false);
    const productToDelete = ref(null);
    const showDiscountDialog = ref(false);
    const selectedProduct = ref(null);
    const newDiscount = reactive({discountPercents : null});
    const rules = computed(() => ({
      discountPercents: { required, minValue: minValue(1), maxValue: maxValue(99) },
    }));
    const v$ = useVuelidate(rules, newDiscount);
    const confirmDelete = (product) => {
      productToDelete.value = product;
      showConfirmation.value = true;
    };

    const deleteProduct = async () => {
      if (productToDelete.value) {
        try {
          await axiosInstance.delete(`/product/${productToDelete.value.id}`);
          products.value = products.value.filter(
            (p) => p.id !== productToDelete.value.id
          );
          productToDelete.value = null;
          showConfirmation.value = false;
        } catch (error) {
          console.error('Failed to delete product:', error.message);
        }
      }
    };

    const toggleActivity = async (product, visible) => {
      console.log(visible)
      try {
        const response = await axiosInstance.put(
          `/product/visible/${product.id}`,
          { visible: visible }
        );
      } catch (error) {
        console.error('Failed to update activity state:', error.message);
      }
    };
    const cancelDelete = () => {
      productToDelete.value = null;
      showConfirmation.value = false;
    };
    const setDiscountProduct = (product) => {
      selectedProduct.value = product;
      v$.value.$reset();
      newDiscount.discountPercents = product.discountPercents ||'';
      showDiscountDialog.value = true;
    };

    const applyDiscount = async () => {
        await v$.value.$validate();
        if(!v$.value.$error) {
          if (selectedProduct.value) {
            try {
              const discountValue = parseInt(newDiscount.discountPercents, 10);
              await axiosInstance.put(`/product/discount/${selectedProduct.value.id}`, {discountPercents : discountValue});
              selectedProduct.value.discountPercents = discountValue;
              showDiscountDialog.value = false;
            } catch (error) {
              console.error("Failed to apply discount:", error.message);
            }
          }
        }
    };
    const removeDiscount = async (product) => {
      try {
        await axiosInstance.put(`/product/discount/${product.id}`, { discountPercents: 0 });
        product.discountPercents = 0;
      } catch (error) {
        console.error("Failed to remove discount:", error.message);
      }
    };
    const cancelDiscount = () => {
      showDiscountDialog.value = false;
      selectedProduct.value = null;
      newDiscount.discountPercents.value = "";
    };

    const fetchProducts = async () => {
      try {
        let url = `/product/management?page=${currentPage.value}&size=${pageSize.value}`;
        const response = await axiosInstance.get(url);
        console.log(response)
        products.value = response.data.content;
        totalRecords.value = response.data.totalElements;
      } catch (error) {
      }
    }
    const onPageChange = (event) => {
      currentPage.value = event.page;
      fetchProducts();
    }
    const fetchUserData = async () => {
      try {
        const response = await axiosInstance.get(`/current-user/`);
        const currentUser = response.data;
        if (!currentUser.isFarmer) {
          await router.push(`/`);
        }
      } catch (error) {
        await router.push(`/`);
      }
    };
    onMounted(() => {
      fetchUserData()
      fetchProducts()
    });
    return {
      products,
      fetchProducts,
      onPageChange,
      pageSize,
      totalRecords,
      currentPage,
      confirmDelete,
      cancelDelete,
      deleteProduct,
      showConfirmation,
      productToDelete,
      showDiscountDialog,
      selectedProduct,
      newDiscount,
      setDiscountProduct,
      applyDiscount,
      cancelDiscount,
      removeDiscount,
      toggleActivity,
      v$,
    }
  },
  methods: {
    getFirstImage(image) {
      if (image) {
        const firstImage = image;
        return `data:image/jpeg;base64,${firstImage.bytes}`;
      }
      return "";
    },
    goToCreateProduct() {
      this.$router.push(`/product/create`);
    },
    editProduct(product) {
      this.$router.push(`/product/update/${product.id}`);
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
  cursor: pointer;
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
.error-message {
  color: red;
  font-size: 0.9rem;
}
</style>
