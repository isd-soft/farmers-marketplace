<template>
  <div class="admin-home">
    <Toast />
    <Header class="navbar"></Header>
    <div class="admin-content">
      <AdminMenu></AdminMenu>
      <Toolbar class="mb-6">
        <template #start>
          <Button
            class="toolbar-buttons"
            label="Restore"
            icon="pi pi-trash"
            severity="success"
            outlined
            @click="confirmRestoreSelected"
            :disabled="!selectedProducts || !selectedProducts.length"
            style="margin-right: 10px"
          />
          <Button
            class="toolbar-buttons"
            label="Delete"
            icon="pi pi-trash"
            severity="danger"
            outlined
            @click="confirmDeleteSelected"
            :disabled="!selectedProducts || !selectedProducts.length"
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
            @click="refreshProducts"
          />
        </template>
      </Toolbar>
      <DataTable
        ref="dt"
        :value="products"
        removableSort
        :loading="loading"
        dataKey="id"
        v-model:selection="selectedProducts"
        v-model:filters="filters"
        filterDisplay="menu"
        :globalFilterFields="['id', 'title', 'description']"
      >
        <template #empty> No products found. </template>
        <template #loading>
          <i class="pi pi-spin pi-spinner" style="font-size: 0.7 rem; margin-right: 0.5rem"></i>
          Loading products' data. Please wait.
        </template>
        <Column selectionMode="multiple" style="width: 3rem"></Column>
        <Column field="id" header="ID" sortable></Column>
        <Column field="title" header="Title" sortable></Column>
        <Column field="description" header="Description" sortable></Column>
        <Column field="pricePerUnit" header="Price" sortable></Column>
        <Column field="visible" header="Visibility" :showFilterMatchModes="false" sortable>
          <template #body="{ data }">
            <Tag :value="data.visible" :severity="getSeverity(data.visible)" />
          </template>
          <template #filter="{ filterModel }">
            <Multiselect
              v-model="filterModel.value"
              :options="values"
              placeholder="Filter by confirmation"
            >
              <template #option="slotProps">
                <Tag :value="slotProps.option" :severity="getSeverity(slotProps.option)" />
              </template>
            </Multiselect>
          </template>
        </Column>
        <Column field="quantity" header="In Stock" sortable>
          <template #body="slotProps">
            <Badge
              :value="slotProps.data.quantity"
              :severity="stockSeverity(slotProps.data)"
            /> </template
        ></Column>
        <Column header="Image">
          <template #body="slotProps">
            <Image
              :src="getFirstImage(slotProps.data.image)"
              alt="Product Image"
              class="admin-product-image"
              preview
            /> </template
        ></Column>
      </DataTable>
    </div>
    <Dialog
      v-model:visible="deleteProductsDialog"
      :style="{ width: '450px' }"
      header="Confirm"
      :modal="true"
    >
      <div class="flex items-center gap-4">
        <i class="pi pi-exclamation-triangle !text-3xl" />
        <span v-if="product">Are you sure you want to delete the selected products?</span>
      </div>
      <template #footer>
        <Button label="No" icon="pi pi-times" text @click="deleteProductsDialog = false" />
        <Button
          :label="deleting ? 'Deleting...' : 'Yes'"
          :icon="deleting ? 'pi pi-spin pi-spinner' : 'pi pi-check'"
          :disabled="deleting"
          @click="deleteSelectedProducts"
        />
      </template>
    </Dialog>
    <Dialog
      v-model:visible="restoreProductsDialog"
      :style="{ width: '450px' }"
      header="Confirm"
      :modal="true"
    >
      <div class="flex items-center gap-4">
        <i class="pi pi-exclamation-triangle !text-3xl" />
        <span v-if="product">Are you sure you want to restore the selected products?</span>
      </div>
      <template #footer>
        <Button label="No" icon="pi pi-times" text @click="restoreProductsDialog = false" />
        <Button
          :label="restoring ? 'Restoring...' : 'Yes'"
          :icon="restoring ? 'pi pi-spin pi-spinner' : 'pi pi-check'"
          :disabled="restoring"
          @click="restoreSelectedProducts"
        />
      </template>
    </Dialog>
    <Footer class="footer"></Footer>
  </div>
</template>
<script>
import axiosInstance from '@/utils/axiosInstance.js';
import AdminMenu from './AdminMenu.vue';
import { ref } from 'vue';
import { FilterMatchMode } from '@primevue/core/api';

import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

import InputIcon from 'primevue/inputicon';
import IconField from 'primevue/iconfield';
import Image from 'primevue/image';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Toolbar from 'primevue/toolbar';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';
import Row from 'primevue/row';
import Badge from 'primevue/badge';
import Tag from 'primevue/tag';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import Multiselect from 'primevue/multiselect';

export default {
  name: 'AdminProducts',
  setup() {
    const deleting = ref(false);
    const restoring = ref(false);

    const toast = useToast();

    function toastAdd(severity, summary, detail, life = 2000) {
      toast.add({
        severity: severity,
        summary: summary,
        detail: detail,
        life: life,
      });
    }

    const dt = ref();
    const loading = ref(true);

    const values = ref([true, false]);

    const product = ref({});

    const products = ref([]);

    const selectedProducts = ref();
    const deleteProductsDialog = ref(false);
    const restoreProductsDialog = ref(false);

    const filters = ref({
      global: { value: null, matchMode: FilterMatchMode.CONTAINS },
      visible: { value: null, matchMode: FilterMatchMode.IN },
    });

    const exportCSV = () => {
      dt.value.exportCSV();
    };

    const refreshProducts = async () => {
      loading.value = true;
      fetchProducts();
    };

    const fetchProducts = async () => {
      try {
        const response = await axiosInstance.get('/product/adminall');
        products.value = response.data.content;
        loading.value = false;
      } catch (error) {
        console.error(error);
      }
    };
    const getFirstImage = (image) => {
      if (image) {
        const firstImage = image;
        return `data:image/jpeg;base64,${firstImage.bytes}`;
      }
      return '';
    };

    const confirmDeleteSelected = () => {
      const allVisible = selectedProducts.value.every((product) => product.visible);
      if (!allVisible) {
        toastAdd('error', 'Removing Item Failed', 'Not all selected products are visible.');
        return;
      }
      deleteProductsDialog.value = true;
    };

    const confirmRestoreSelected = () => {
      const allNotVisible = selectedProducts.value.every((product) => !product.visible);
      if (!allNotVisible) {
        toastAdd('error', 'Removing Item Failed', 'Not all selected products are invisible.');
        return;
      }
      restoreProductsDialog.value = true;
    };

    const deleteSelectedProducts = async () => {
      deleting.value = true;
      try {
        await Promise.all(
          selectedProducts.value.map((product) =>
            axiosInstance.put(`/product/visible/${product.id}`, { visible: false }),
          ),
        );

        products.value = products.value.map((product) => {
          if (selectedProducts.value.some((sel) => sel.id === product.id)) {
            return { ...product, visible: false };
          }
          return product;
        });

        deleteProductsDialog.value = false;
        selectedProducts.value = null;
      } catch (error) {
        console.error(error);
      } finally {
        deleting.value = false;
      }
    };

    const restoreSelectedProducts = async () => {
      restoring.value = true;
      try {
        await Promise.all(
          selectedProducts.value.map((product) =>
            axiosInstance.put(`/product/visible/${product.id}`, { visible: true }),
          ),
        );
        products.value = products.value.map((product) => {
          if (selectedProducts.value.some((sel) => sel.id === product.id)) {
            return { ...product, visible: true };
          }
          return product;
        });

        restoreProductsDialog.value = false;
        selectedProducts.value = null;
      } catch (error) {
        console.error(error);
      } finally {
        restoring.value = false;
      }
    };

    const stockSeverity = (data) => {
      if (data.quantity < 20) return 'danger';
      else if (data.quantity > 20 && data.quantity < 50) return 'warn';
      else return 'success';
    };

    const getSeverity = (status) => {
      switch (status) {
        case true:
          return 'success';

        case false:
          return 'danger';
      }
    };

    fetchProducts();

    return {
      deleting,
      restoring,
      confirmRestoreSelected,
      restoreProductsDialog,
      restoreSelectedProducts,
      dt,
      product,
      deleteProductsDialog,
      fetchProducts,
      getFirstImage,
      confirmDeleteSelected,
      deleteSelectedProducts,
      products,
      loading,
      selectedProducts,
      filters,
      refreshProducts,
      stockSeverity,
      exportCSV,
      getSeverity,
      values,
    };
  },

  components: {
    Multiselect,
    Tag,
    Badge,
    Header,
    Footer,
    AdminMenu,
    DataTable,
    Column,
    Row,
    ColumnGroup,
    Dialog,
    Button,
    Toolbar,
    Image,
    IconField,
    InputIcon,
  },
};
</script>
<style>
.admin-home {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  gap: 5vh;
}
.admin-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2em;
  margin-top: 80px;
  padding: 6vh;
  width: 80%;
}
.admin-product-image {
  width: 50px;
  height: 50px;
  overflow: hidden;
  object-fit: fill;
  border-radius: 5px;
}
.refresh-button {
  width: 50px;
  height: 50px;
}
.toolbar-buttons {
  height: 40px !important;
  width: 90px !important;
}
.searchbar {
  width: 100% !important;
  max-width: 100% !important;
}
</style>
<style scoped>
.footer {
  padding: 20px;
}
</style>
