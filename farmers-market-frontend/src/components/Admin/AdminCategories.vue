<template>
  <div class="admin-home">
    <Header class="navbar"></Header>

    <div class="admin-content">
      <AdminMenu></AdminMenu>
      <Toolbar class="mb-6">
        <template #start>
          <Button
            label="New"
            icon="pi pi-plus"
            class="toolbar-buttons"
            severity="success"
            @click="openNew"
            outlined
            style="margin-right: 10px"
          />
          <Button
            label="Delete"
            icon="pi pi-trash"
            severity="danger"
            outlined
            @click="confirmDeleteSelected"
            class="toolbar-buttons"
            :disabled="!selectedCategories || !selectedCategories.length"
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
            @click="refreshCategories"
          />
        </template>
      </Toolbar>
      <DataTable
        ref="dt"
        :value="categories"
        removableSort
        dataKey="id"
        v-model:filters="filters"
        v-model:selection="selectedCategories"
        filterDisplay="menu"
        :loading="loading"
        editMode="row"
        @row-edit-save="onRowEditSave"
        v-model:editingRows="editingRows"
        :pt="{
          table: { style: 'min-width: 50rem' },
          column: {
            bodycell: ({ state }) => ({
              style: state['d_editing'] && 'padding-top: 0.75rem; padding-bottom: 0.75rem',
            }),
          },
        }"
      >
        <template #empty> No categories found. </template>
        <template #loading>
          <i class="pi pi-spin pi-spinner" style="font-size: 0.7 rem; margin-right: 0.5rem"></i>
          Loading categories' data. Please wait.
        </template>
        <Column selectionMode="multiple" style="width: 3rem"></Column>
        <Column field="id" header="ID" sortable></Column>
        <Column field="title" header="Title" sortable :showFilterMatchModes="false">
          <template #editor="{ data, field }">
            <InputText v-model="data[field]" fluid />
          </template>
          <template #body="{ data }">
            {{ data.title }}
          </template>
        </Column>
        <Column field="nrOfItems" header="Items" sortable></Column>
        <Column
          :rowEditor="true"
          style="width: 10%; min-width: 8rem"
          bodyStyle="text-align:center"
        ></Column>
      </DataTable>
    </div>
    <Dialog
      v-model:visible="categoryDialog"
      :style="{ width: '450px' }"
      header="Category Details"
      :modal="true"
    >
      <div class="flex flex-col gap-6">
        <InputText
          id="categorytitle"
          v-model.trim="category.title"
          required="true"
          autofocus
          :invalid="submitted && !category.name"
          fluid
        />
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" text @click="hideDialog" />
        <Button
          :label="saving ? 'Saving...' : 'Save'"
          :icon="saving ? 'pi pi-spin pi-spinner' : 'pi pi-check'"
          :disabled="saving"
          @click="saveCategory"
        />
      </template>
    </Dialog>

    <Dialog
      v-model:visible="deleteCategoriesDialog"
      :style="{ width: '450px' }"
      header="Confirm"
      :modal="true"
    >
      <div class="flex items-center gap-4">
        <i class="pi pi-exclamation-triangle !text-3xl" />
        <span v-if="category">Are you sure you want to delete the selected categories?</span>
      </div>
      <template #footer>
        <Button label="No" icon="pi pi-times" text @click="deleteCategoriesDialog = false" />
        <Button label="Yes" icon="pi pi-check" text @click="deleteSelectedCategories" />
      </template>
    </Dialog>
    <Footer class="footer"></Footer>
  </div>
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
import { FilterMatchMode } from '@primevue/core/api';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';

export default {
  name: 'AdminCategories',
  setup() {
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
    const saving = ref(false);
    const filters = ref({
      global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });
    const exportCSV = () => {
      dt.value.exportCSV();
    };

    const selectedCategories = ref();

    const category = ref({});
    const categories = ref([]);
    const editingRows = ref([]);
    const submitted = ref(false);
    const categoryDialog = ref(false);
    const deleteCategoriesDialog = ref(false);

    const openNew = () => {
      category.value = {};
      submitted.value = false;
      categoryDialog.value = true;
    };
    const hideDialog = () => {
      categoryDialog.value = false;
      submitted.value = false;
    };
    const onRowEditSave = async (event) => {
      let { newData, index } = event;
      const updatedCategory = event.data;
      try {
        const response = await axiosInstance.put(`/category/${updatedCategory.id}`, {
          title: newData.title,
        });
        categories.value[index] = newData;
      } catch (error) {
        console.error(error);
      }
    };
    const saveCategory = async () => {
      if (!category.value.title) {
        submitted.value = true;
        return;
      }
      saving.value = true;

      try {
        const response = await axiosInstance.post('/category', {
          title: category.value.title,
        });
        if (response.data) {
          categories.value.push({ ...response.data, nrOfItems: 0 });
        }
        categoryDialog.value = false;
        category.value = {};
      } catch (error) {
        console.error(error);
      } finally {
        saving.value = false;
      }
    };
    const refreshCategories = async () => {
      loading.value = true;
      fetchCategories();
    };
    const fetchCategories = async () => {
      try {
        const response = await axiosInstance.get('/category/stats');
        categories.value = response.data;
        loading.value = false;
      } catch (error) {
        console.error(error);
      }
    };

    const confirmDeleteSelected = () => {
      const allAreEmpty = selectedCategories.value?.every((category) => category.nrOfItems === 0);
      if (!allAreEmpty) {
        toastAdd('error', 'Deletion failed', 'Not all selected categories are empty.');
        return;
      }
      deleteCategoriesDialog.value = true;
    };

    const deleteSelectedCategories = async () => {
      try {
        await Promise.all(
          selectedCategories.value.map((category) =>
            axiosInstance.delete(`/category/${category.id}`),
          ),
        );

        categories.value = categories.value.filter(
          (val) => !selectedCategories.value.includes(val),
        );
        deleteCategoriesDialog.value = false;
        selectedCategories.value = null;
      } catch (error) {
        console.error(error);
      }
    };

    fetchCategories();

    return {
      saving,
      dt,
      exportCSV,
      onRowEditSave,
      hideDialog,
      saveCategory,
      openNew,
      confirmDeleteSelected,
      deleteSelectedCategories,
      deleteCategoriesDialog,
      filters,
      categoryDialog,
      categories,
      category,
      submitted,
      loading,
      selectedCategories,
      editingRows,
      refreshCategories,
    };
  },
  components: {
    Header,
    AdminMenu,
    DataTable,
    Column,
    ColumnGroup,
    Row,
    Toolbar,
    Button,
    InputText,
    Dialog,
    InputIcon,
    IconField,
    Footer,
  },
};
</script>
<style scoped>
.footer {
  margin: 0;
  padding: 20px;
}
</style>
