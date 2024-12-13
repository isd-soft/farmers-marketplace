<template>
  <div class="admin-home">
    <Header class="navbar"></Header>
    <div class="admin-content">
      <AdminMenu></AdminMenu>
      <Toolbar class="mb-6">
        <template #start>
          <Button
            label="Delete"
            icon="pi pi-trash"
            severity="danger"
            class="toolbar-buttons"
            outlined
            @click="confirmDeleteSelected"
            :disabled="!selectedProductReviews || !selectedProductReviews.length"
          />
        </template>
        <template #center>
          <IconField>
            <InputIcon>
              <i class="pi pi-search" />
            </InputIcon>
            <InputText
              v-model="filters['global'].value"
              placeholder="Keyword Search"
              class="searchbar"
            />
          </IconField>
        </template>
        <template #end>
          <Button
            icon="pi pi-external-link"
            label="Export"
            text
            severity="secondary"
            @click="exportCSV($event)"
          />
          <Button
            class="refresh-button"
            icon="pi pi-refresh"
            text
            severity="secondary"
            @click="refreshProductReviews"
          />
        </template>
      </Toolbar>
      <DataTable
        ref="dt"
        :value="productReviews"
        removableSort
        showGridLines
        dataKey="id"
        v-model:filters="filters"
        v-model:selection="selectedProductReviews"
        filterDisplay="menu"
        :loading="loading"
      >
        <template #empty> No product reviews found. </template>
        <template #loading>
          <i class="pi pi-spin pi-spinner" style="font-size: 0.7 rem; margin-right: 0.5rem"></i>
          Loading products' review data. Please wait.
        </template>
        <Column selectionMode="multiple" style="width: 3rem"></Column>
        <Column field="id" header="ID" sortable></Column>
        <Column field="rating" header="Rating" sortable>
          <template #body="slotProps">
            <Rating v-model="slotProps.data.rating" readonly />
          </template>
        </Column>
        <Column field="content" header="Review Content"></Column>
        <Column field="creator.email" header="Creator">
          <template #body="slotProps">
            <Button
              variant="text"
              v-model="slotProps.data.creator.email"
              :label="slotProps.data.creator.email"
              @click="goToUserPage(slotProps.data.creator.id)"
            />
          </template>
        </Column>
        <Column field="productTitle" header="Product Title" :showFilterMatchModes="false">
          <template #body="slotProps">
            <Button
              variant="text"
              v-model="slotProps.data.productTitle"
              :label="slotProps.data.productTitle"
              @click="goToProductPage(slotProps.data.productId)"
            />
          </template>
          <template #filter="{ filterModel }">
            <InputText v-model="filterModel.value" type="text" placeholder="Search by Product" />
          </template>
        </Column>
      </DataTable>
    </div>

    <Dialog
      v-model:visible="deleteProductReviewDialog"
      :style="{ width: '450px' }"
      header="Confirm"
      :modal="true"
    >
      <div class="flex items-center gap-4">
        <i class="pi pi-exclamation-triangle !text-3xl" />
        <span v-if="productReview"
          >Are you sure you want to delete the selected product review?</span
        >
      </div>
      <template #footer>
        <Button label="No" icon="pi pi-times" text @click="deleteProductReviewDialog = false" />
        <Button
          :label="deleting ? 'Deleting...' : 'Yes'"
          :icon="deleting ? 'pi pi-spin pi-spinner' : 'pi pi-check'"
          :disabled="deleting"
          @click="deleteSelectedProductReviews"
        />
      </template>
    </Dialog>
    <Footer class="footer"></Footer>
  </div>
</template>
<script>
import axiosInstance from '@/utils/axiosInstance.js';
import AdminMenu from './AdminMenu.vue';

import InputText from 'primevue/inputtext';
import Dialog from 'primevue/dialog';

import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Button from 'primevue/button';
import Toolbar from 'primevue/toolbar';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';
import Row from 'primevue/row';
import Rating from 'primevue/rating';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

import { FilterMatchMode } from '@primevue/core/api';

export default {
  name: 'AdminReviews',
  setup() {
    const dt = ref();

    const router = useRouter();

    const deleting = ref(false);

    const loading = ref(true);
    const filters = ref({
      global: { value: null, matchMode: FilterMatchMode.CONTAINS },
      productTitle: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
    });
    const exportCSV = () => {
      dt.value.exportCSV();
    };

    const selectedProductReviews = ref();

    const productReview = ref({});
    const productReviewDialog = ref(false);
    const deleteProductReviewDialog = ref(false);

    const goToProductPage = (productId) => {
      router.push(`/product/${productId}`);
    };

    const fetchProductReviews = async () => {
      try {
        const response = await axiosInstance.get('/reviews/allproducts');

        productReviews.value = response.data;
        loading.value = false;
      } catch (error) {
        console.error(error);
      }
    };

    const refreshProductReviews = async () => {
      loading.value = true;
      fetchProductReviews();
    };

    const goToUserPage = (userId) => {
      router.push(`/id${userId}`);
    };

    const confirmDeleteSelected = () => {
      deleteProductReviewDialog.value = true;
    };

    const deleteSelectedProductReviews = async () => {
      deleting.value = true;
      try {
        await Promise.all(
          selectedProductReviews.value.map((productReview) =>
            axiosInstance.delete(`/reviews/products/${productReview.id}`),
          ),
        );

        productReviews.value = productReviews.value.filter(
          (val) => !selectedProductReviews.value.includes(val),
        );
        deleteProductReviewDialog.value = false;
        selectedProductReviews.value = null;
      } catch (error) {
        console.error(error);
      } finally {
        deleting.value = false;
      }
    };

    const productReviews = ref([]);
    fetchProductReviews();

    return {
      deleting,
      refreshProductReviews,
      exportCSV,
      dt,
      confirmDeleteSelected,
      deleteSelectedProductReviews,
      deleteProductReviewDialog,
      filters,
      productReviewDialog,
      productReviews,
      productReview,
      loading,
      selectedProductReviews,
      router,
      goToProductPage,
      goToUserPage,
    };
  },
  components: {
    IconField,
    InputIcon,
    Header,
    Footer,
    AdminMenu,
    DataTable,
    Column,
    ColumnGroup,
    Row,
    Toolbar,
    Button,
    InputText,
    Dialog,
    Rating,
  },
};
</script>
<style scoped>
.footer {
  margin: 0;
  padding: 20px;
}
</style>
