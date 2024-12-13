<template>
  <div class="admin-home">
    <Header class="navbar"></Header>
    <div class="admin-content">
      <AdminMenu></AdminMenu>
      <Toolbar class="mb-6">
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
            class="refresh-button"
            icon="pi pi-refresh"
            text
            severity="secondary"
            @click="refreshOrders"
          />
        </template>
      </Toolbar>
      <DataTable
        editMode="row"
        :value="orders"
        removableSort
        :loading="loading"
        dataKey="id"
        v-model:expandedRows="expandedRows"
        v-model:editingRows="editingRows"
        v-model:filters="filters"
        :globalFilterFields="['id', 'title', 'description']"
        tableStyle="min-width: 60rem"
        @row-edit-save="onRowEditSave"
      >
        <template #empty> No orders found. </template>
        <template #loading>
          <i class="pi pi-spin pi-spinner" style="font-size: 0.7 rem; margin-right: 0.5rem"></i>
          Loading orders' data. Please wait.
        </template>
        <Column expander style="width: 5rem" />
        <Column field="id" header="ID" sortable></Column>
        <Column header="Customer">
          <template #body="slotProps">
            <Button
              variant="text"
              v-model="slotProps.data.customer.email"
              :label="slotProps.data.customer.email"
              @click="goToUserPage(slotProps.data.customer.id)"
            />
          </template>
        </Column>
        <Column field="deliveryTypeFarmer.type" header="Delivery Type" sortable></Column>
        <Column field="totalDeliveryPrice" header="Total Delivery Price" sortable></Column>
        <Column field="totalItemsPrice" header="Total Item Price" sortable></Column>
        <Column field="totalPrice" header="Total Price" sortable></Column>
        <Column field="orderStatus" header="Order Status" sortable>
          <template #editor="{ data, field }">
            <Select
              v-model="data[field]"
              :options="orderStatusOptions"
              optionLabel="label"
              optionValue="value"
              class="w-full"
              placeholder="Select a status"
            >
            </Select>
          </template>
        </Column>
        <Column
          :rowEditor="true"
          style="width: 10%; min-width: 8rem"
          bodyStyle="text-align:center"
        ></Column>

        <template #expansion="slotProps">
          <DataTable :value="slotProps.data.itemsInOrder" removableSort>
            <Column field="productId" header="Product ID" sortable></Column>
            <Column field="productTitle" header="Title" sortable></Column>
            <Column field="rating" header="Rating" sortable>
              <template #body="slotProps">
                <Rating v-model="slotProps.data.rating" readonly /> </template
            ></Column>
            <Column field="pricePerUnit" header="Price" sortable></Column>
            <Column field="quantity" header="Quantity" sortable></Column>
            <Column header="Total">
              <template #body="{ data }">
                {{ (data.quantity * data.pricePerUnit).toFixed(2) }}
              </template>
            </Column>
            <Column headerStyle="width:4rem">
              <template #body="{ data }">
                <Button icon="pi pi-search" @click="goToProductPage(data.productId)" />
              </template>
            </Column>
          </DataTable>
        </template>
      </DataTable>
    </div>
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

import Rating from 'primevue/rating';
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

import Select from 'primevue/select';

import { useRouter } from 'vue-router';

export default {
  name: 'AdminProducts',
  setup() {
    const router = useRouter();

    const loading = ref(true);

    const product = ref({});

    const orders = ref([]);

    const expandedRows = ref({});

    const editingRows = ref([]);

    const filters = ref({
      global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });

    const orderStatusOptions = [
      { label: 'Pending', value: 'PENDING' },
      { label: 'Confirmed', value: 'CONFIRMED' },
      { label: 'In Progress', value: 'INPROGRESS' },
      { label: 'Shipped', value: 'SHIPPED' },
      { label: 'Delivered', value: 'DELIVERED' },
      { label: 'Cancelled', value: 'CANCELLED' },
    ];

    const refreshOrders = async () => {
      loading.value = true;
      fetchOrders();
    };

    const onRowEditSave = async (event) => {
      let { newData, index } = event;
      const updatedOrder = event.newData;
      try {
        await axiosInstance.put(`/order/${updatedOrder.id}`, {
          orderStatus: updatedOrder.orderStatus,
        });
        orders.value[index] = newData;
      } catch (error) {
        console.error(error);
      }
    };

    const goToProductPage = (productId) => {
      router.push(`/product/${productId}`);
    };

    const goToUserPage = (userId) => {
      router.push(`/id${userId}`);
    };

    const fetchOrders = async () => {
      try {
        const response = await axiosInstance.get('/order');
        orders.value = response.data;
        loading.value = false;
      } catch (error) {
        console.error(error);
      }
    };

    fetchOrders();

    return {
      goToUserPage,
      onRowEditSave,
      editingRows,
      expandedRows,
      product,
      fetchOrders,
      orders,
      loading,
      filters,
      refreshOrders,
      goToProductPage,
      orderStatusOptions,
    };
  },

  components: {
    Rating,
    Select,
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
<style scoped>
.footer {
  margin: 0;
  padding: 20px;
}
</style>
