<template>
  <Menubar :model="items" />
</template>
<script>
import Menubar from 'primevue/menubar';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'AdminMenu',
  components: {
    Menubar,
  },

  setup() {
    const router = useRouter();

    const items = ref([
      {
        label: 'Products',
        icon: 'pi pi-warehouse',
        command: () => router.push('/admin/products'),
      },
      {
        label: 'Orders',
        icon: 'pi pi-shopping-cart',
        command: () => router.push('/admin/orders'),
      },
      {
        label: 'Users',
        icon: 'pi pi-users',
        command: () => router.push('/admin/users'),
      },
      {
        label: 'Categories',
        icon: 'pi pi-list-check',
        command: () => router.push('/admin/categories'),
      },
      {
        label: 'Reviews',
        icon: 'pi pi-comment',
        items: [
          {
            label: 'Product Reviews',
            icon: 'pi pi-comment',
            command: () => router.push('/admin/productreviews'),
          },
          {
            label: 'Farmer Reviews',
            icon: 'pi pi-comment',
            command: () => router.push('/admin/farmerreviews'),
          },
        ],
      },
    ]);
    return {
      items,
    };
  },
};
</script>
