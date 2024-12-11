<template>
  <div class="admin-home">
    <Header class="navbar"></Header>
    <div class="admin-content">
      <AdminMenu></AdminMenu>
      <Toolbar class="mb-6">
        <template #start>
          <Button
            class="toolbar-buttons"
            label="Delete"
            icon="pi pi-trash"
            severity="danger"
            outlined
            @click="confirmDeleteSelected"
            :disabled="!selectedUsers || !selectedUsers.length"
            style="margin-right: 10px"
          />
          <Button
            class="toolbar-buttons"
            label="Admin"
            icon="pi pi-user-plus"
            severity="help"
            outlined
            @click="confirmUpgradeSelected"
            :disabled="!selectedUsers || !selectedUsers.length"
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
            @click="refreshUsers"
          />
        </template>
      </Toolbar>
      <DataTable
        ref="dt"
        v-model:filters="filters"
        v-model:selection="selectedUsers"
        :value="users"
        showGridLines
        dataKey="id"
        filterDisplay="menu"
        removableSort
        :globalFilterFields="[
          'firstName',
          'lastName',
          'email',
          'description',
          'address',
          'role',
          'phoneNumber',
        ]"
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
        <template #empty> No users found. </template>
        <template #loading>
          <i class="pi pi-spin pi-spinner" style="font-size: 0.7 rem; margin-right: 0.5rem"></i>
          Loading users' data. Please wait.
        </template>
        <Column selectionMode="multiple" style="width: 3rem"></Column>
        <Column field="id" header="ID" sortable></Column>
        <Column field="firstName" header="First Name" sortable>
          <template #body="{ data }">
            {{ data.firstName }}
          </template>
          <template #editor="{ data, field }">
            <InputText v-model="data[field]" fluid />
          </template>
        </Column>
        <Column field="lastName" header="Last Name" sortable>
          <template #body="{ data }">
            {{ data.lastName }}
          </template>
          <template #editor="{ data, field }">
            <InputText v-model="data[field]" fluid />
          </template>
        </Column>
        <Column field="email" header="Email" sortable>
          <template #body="{ data }">
            {{ data.email }}
          </template>
          <template #filter="{ filterModel }">
            <InputText v-model="filterModel.value" type="text" placeholder="Search by email" />
          </template>
          <template #editor="{ data, field }">
            <InputText v-model="data[field]" fluid />
          </template>
        </Column>
        <Column field="phoneNumber" header="Phone" sortable>
          <template #editor="{ data, field }">
            <InputText v-model="data[field]" fluid />
          </template>
        </Column>
        <Column field="description" header="Description" sortable>
          <template #editor="{ data, field }">
            <InputText
              v-model="data[field]"
              :disabled="data.role === 'Customer' || data.role === 'Admin'"
              fluid
            />
          </template>
        </Column>
        <Column field="address" header="Address" sortable>
          <template #body="{ data }">
            {{ data.address }}
          </template>
          <template #editor="{ data, field }">
            <InputText
              v-model="data[field]"
              :disabled="data.role === 'Customer' || data.role === 'Admin'"
              fluid
            />
          </template>
        </Column>
        <Column field="role" header="Role" :showFilterMatchModes="false" sortable>
          <template #body="{ data }">
            <Tag :value="data.role" :severity="getRoleColor(data.role)" />
          </template>
          <template #filter="{ filterModel }">
            <Multiselect
              v-model="filterModel.value"
              :options="roles.map((role) => role.value)"
              placeholder="Filter by role"
            >
              <template #option="slotProps">
                <Tag :value="slotProps.option" :severity="getRoleColor(slotProps.option)" />
              </template>
            </Multiselect>
          </template>
        </Column>
        <Column field="enabled" header="Enabled" :showFilterMatchModes="false" sortable>
          <template #body="{ data }">
            <Tag :value="data.enabled" :severity="getSeverity(data.enabled)" />
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
        <Column
          :rowEditor="true"
          style="width: 10%; min-width: 8rem"
          bodyStyle="text-align:center"
        ></Column>
      </DataTable>
    </div>

    <Dialog
      v-model:visible="deleteUsersDialog"
      :style="{ width: '450px' }"
      header="Confirm"
      :modal="true"
    >
      <div class="flex items-center gap-4">
        <i class="pi pi-exclamation-triangle !text-3xl" />
        <span v-if="user">Are you sure you want to delete the selected users?</span>
      </div>
      <template #footer>
        <Button label="No" icon="pi pi-times" text @click="deleteUsersDialog = false" />
        <Button label="Yes" icon="pi pi-check" text @click="deleteSelectedUsers" />
      </template>
    </Dialog>
    <Dialog
      v-model:visible="upgradeUsersDialog"
      :style="{ width: '450px' }"
      header="Confirm"
      :modal="true"
    >
      <div class="flex items-center gap-4">
        <i class="pi pi-exclamation-triangle !text-3xl" />
        <span v-if="user">Are you sure you want to upgrade the selected users?</span>
      </div>
      <template #footer>
        <Button label="No" icon="pi pi-times" text @click="upgradeUsersDialog = false" />
        <Button label="Yes" icon="pi pi-check" text @click="upgradeSelectedUsers" />
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

import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import Toolbar from 'primevue/toolbar';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup'; // optional
import Row from 'primevue/row'; // optional
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Multiselect from 'primevue/multiselect';
import Password from 'primevue/password';
import Select from 'primevue/select';
import FloatLabel from 'primevue/floatlabel';
import Tag from 'primevue/tag';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';

export default {
  name: 'Adminusers',
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
    const user = ref({});
    const users = ref([]);
    const selectedUsers = ref();
    const deleteUsersDialog = ref(false);
    const upgradeUsersDialog = ref(false);
    const editingRows = ref([]);

    const exportCSV = () => {
      dt.value.exportCSV();
    };

    const roleOptions = ref([
      { label: 'Customer', value: 'CUSTOMER' },
      { label: 'Farmer', value: 'FARMER' },
    ]);

    const roles = ref([{ value: 'Admin' }, { value: 'Farmer' }, { value: 'Customer' }]);
    const values = ref([true, false]);

    const refreshUsers = async () => {
      loading.value = true;
      fetchUsers();
    };
    const fetchUsers = async () => {
      try {
        const response = await axiosInstance.get('/users', {
          params: {
            roleParams: 'ALL',
          },
        });
        users.value = response.data.content;
        users.value.forEach((user) => {
          if (user.isAdmin) {
            user.role = 'Admin';
          } else if (user.isFarmer) {
            user.role = 'Farmer';
          } else {
            user.role = 'Customer';
          }
        });
        loading.value = false;
      } catch (error) {
        console.error(error);
      }
    };

    const confirmDeleteSelected = () => {
      deleteUsersDialog.value = true;
    };
    const confirmUpgradeSelected = () => {
      const allAreCustomers = selectedUsers.value.every((user) => !user.isAdmin && !user.isFarmer);
      if (!allAreCustomers) {
        toastAdd('error', 'Upgrade failed', 'Not all selected users are customers.');
        return;
      }
      upgradeUsersDialog.value = true;
    };

    const deleteSelectedUsers = async () => {
      try {
        await Promise.all(
          selectedUsers.value.map((user) => axiosInstance.delete(`/users/${user.id}`)),
        );

        users.value = users.value.filter((val) => !selectedUsers.value.includes(val));
        deleteUsersDialog.value = false;
        selectedUsers.value = null;
      } catch (error) {
        console.error(error);
      }
    };

    const upgradeSelectedUsers = async () => {
      try {
        await Promise.all(
          selectedUsers.value.map((user) =>
            axiosInstance.post(`/users/upgrade-to-admin/${user.id}`),
          ),
        );
        selectedUsers.value.forEach((selectedUser) => {
          const user = users.value.find((u) => u.id === selectedUser.id);
          if (user) {
            user.role = 'Admin';
          }
        });
        upgradeUsersDialog.value = false;
        selectedUsers.value = null;
      } catch (error) {
        console.error(error);
      }
    };

    const onRowEditSave = async (event) => {
      let { newData, index } = event;
      const updatedUser = event.data;
      try {
        const response = await axiosInstance.put(`/users/${updatedUser.id}`, {
          email: newData.email,
          firstName: newData.firstName,
          lastName: newData.lastName,
          phoneNumber: newData.phoneNumber,
          address: newData.role === 'Farmer' ? newData.address : null,
          description: newData.role === 'Farmer' ? newData.description : null,
        });
        users.value[index] = newData;
      } catch (error) {
        console.error(error);
      }
    };

    const getSeverity = (status) => {
      switch (status) {
        case true:
          return 'success';

        case false:
          return 'danger';
      }
    };
    const getRoleColor = (role) => {
      switch (role) {
        case 'Admin':
          return 'contrast';
        case 'Farmer':
          return 'warn';
        case 'Customer':
          return 'info';
      }
    };

    fetchUsers();

    const filters = ref({
      global: { value: null, matchMode: FilterMatchMode.CONTAINS },
      firstName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
      lastName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
      email: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
      address: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
      role: { value: null, matchMode: FilterMatchMode.IN },
      enabled: { value: null, matchMode: FilterMatchMode.IN },
    });
    return {
      dt,
      getSeverity,
      getRoleColor,
      filters,
      roles,
      values,
      users,
      loading,
      fetchUsers,
      confirmDeleteSelected,
      confirmUpgradeSelected,
      selectedUsers,
      deleteUsersDialog,
      upgradeUsersDialog,
      user,
      deleteSelectedUsers,
      upgradeSelectedUsers,
      roleOptions,
      onRowEditSave,
      editingRows,
      refreshUsers,
      exportCSV,
    };
  },
  components: {
    Tag,
    Header,
    Footer,
    Dialog,
    Button,
    Toolbar,
    AdminMenu,
    DataTable,
    Column,
    Row,
    ColumnGroup,
    IconField,
    InputIcon,
    Multiselect,
    FloatLabel,
    Password,
    Select,
  },
};
</script>
