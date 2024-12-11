<template>
  <div class="admin-home">
    <Header class="navbar"></Header>
    <div class="admin-content">
      <AdminMenu></AdminMenu>
      <Toolbar class="mb-6">
        <template #start>
          <Button
            label="Delete"
            class="toolbar-buttons"
            icon="pi pi-trash"
            severity="danger"
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
            @click="refreshReviews"
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
        <template #empty> No farmer reviews found. </template>
        <template #loading>
          <i class="pi pi-spin pi-spinner" style="font-size: 0.7 rem; margin-right: 0.5rem"></i>
          Loading farmers' review data. Please wait.
        </template>
        <Column selectionMode="multiple" style="width: 3rem"></Column>
        <Column field="id" header="ID" sortable></Column>
        <Column field="rating" header="Rating" sortable>
          <template #body="slotProps">
            <Rating v-model="slotProps.data.rating" readonly />
          </template>
        </Column>
        <Column field="content" header="Content" sortable></Column>
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
        <Column field="farmer.email" header="Farmer">
          <template #body="slotProps">
            <Button
              variant="text"
              v-model="slotProps.data.farmer.email"
              :label="slotProps.data.farmer.email"
              @click="goToUserPage(slotProps.data.farmer.id)"
            />
          </template>
        </Column>
      </DataTable>
    </div>
    <Footer class="footer"></Footer>
  </div>

  <Dialog
    v-model:visible="deleteProductReviewDialog"
    :style="{ width: '450px' }"
    header="Confirm"
    :modal="true"
  >
    <div class="flex items-center gap-4">
      <i class="pi pi-exclamation-triangle !text-3xl" />
      <span v-if="productReview">Are you sure you want to delete the selected farmer review?</span>
    </div>
    <template #footer>
      <Button label="No" icon="pi pi-times" text @click="deleteProductReviewDialog = false" />
      <Button label="Yes" icon="pi pi-check" text @click="deleteSelectedProductReviews" />
    </template>
  </Dialog>
</template>
<script>
import axiosInstance from '@/utils/axiosInstance.js';
import AdminMenu from './AdminMenu.vue';

import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

import InputText from 'primevue/inputtext';
import Dialog from 'primevue/dialog';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Button from 'primevue/button';
import Toolbar from 'primevue/toolbar';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';
import Row from 'primevue/row';
import { ref } from 'vue';
import Rating from 'primevue/rating';
import { FilterMatchMode } from '@primevue/core/api';
import { useRouter } from 'vue-router';

export default {
  name: 'AdminFarmerReviews',
  setup() {
    const router = useRouter();

    const dt = ref();

    const loading = ref(true);

    const selectedProductReviews = ref();

    const productReview = ref({});
    const productReviewDialog = ref(false);
    const deleteProductReviewDialog = ref(false);

    const filters = ref({
      global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });

    const goToUserPage = (userId) => {
      router.push(`/id${userId}`);
    };

    const exportCSV = () => {
      dt.value.exportCSV();
    };

    const refreshReviews = async () => {
      loading.value = true;
      fetchProductReviews();
    };

    const fetchProductReviews = async () => {
      try {
        const response = await axiosInstance.get('/reviews/allfarmers');
        productReviews.value = response.data;
        loading.value = false;
      } catch (error) {
        console.error(error);
      }
    };

    const confirmDeleteSelected = () => {
      deleteProductReviewDialog.value = true;
    };

    const deleteSelectedProductReviews = async () => {
      try {
        await Promise.all(
          selectedProductReviews.value.map((productReview) =>
            axiosInstance.delete(`/reviews/farmers/${productReview.id}`),
          ),
        );

        productReviews.value = productReviews.value.filter(
          (val) => !selectedProductReviews.value.includes(val),
        );
        deleteProductReviewDialog.value = false;
        selectedProductReviews.value = null;
      } catch (error) {
        console.error(error);
      }
    };

    const productReviews = ref([]);
    fetchProductReviews();

    return {
      dt,
      exportCSV,
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
      goToUserPage,
      refreshReviews,
    };
  },
  components: {
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
    InputIcon,
    IconField,
  },
};
</script>
<style scoped>
.footer {
  margin: 0;
  padding: 20px;
}
</style>
