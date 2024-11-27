<template>
  <div class="home">
    <Header class = "navbar"></Header>
    <div class="search-filters">
    <FloatLabel variant="on">
      <InputText
        id="product-desc"
        v-model="searchQ"
        class="search-products-input"
        @input="fetchProducts"
      />
      <label for="product-desc">Search</label>
    </FloatLabel>
    <FloatLabel variant="on">
      <Select
        id="product-category"
        v-if="categories.length > 0"
        v-model="category"
        :options="categories"
        optionLabel="label"
        optionValue="value"
        @change="fetchProducts"
        class="search-products-input"
      />
      <label for="product-category">Category</label>
    </FloatLabel>
    </div>
    <div class="products-grid">
      <ProductCard
        v-for="product in products"
        :key="product.id"
        :product="product"
      />
    </div>
  <Footer class = "footer"></Footer>
  </div>
</template>
<script>
import {ref, onMounted, watch} from "vue";
  import axiosInstance from "@/utils/axiosInstance.js";
  import Header from "@/components/Header.vue";
  import Footer from "@/components/Footer.vue";
  import Select from "primevue/select";
  import ProductCard from "@/components/ProductCard.vue";
  import {useRoute} from "vue-router";
  export default {
    name: 'SearchProducts',
    components: {
      ProductCard,
      Header,
      Footer,
      Select
    },
    setup() {
      const products = ref([])
      const categories = ref([])
      const category = ref()
      const searchQ = ref("");
      const route = useRoute();
      const fetchProducts = async () => {
        try {
          let url = `/product?search=${searchQ.value}`;
          if (category.value) {
            url += `&category=${category.value}`;
          }
          const response = await axiosInstance.get(url);
          console.log(response)
          products.value = response.data
        } catch (error) {
          console.error('Failed to load products:', error.message)
        }
      }
      const fetchCategories = async () => {
        try {
          const response = await axiosInstance.get('/category');
          if (response.data && response.data.length > 0) {
            categories.value = [
              { label: "All", value: null },
              ...response.data.map((category) => ({
                label: category.title,
                value: category.id,
              })),
            ];
          }
          console.log("Categories:", categories.value);
        } catch (error) {
          console.error('Failed to load categories:', error.message)
        }
      }
      onMounted(() => {
        searchQ.value = route.query.search || "";
        category.value = route.query.category
          ? parseInt(route.query.category, 10)
          : null;
        fetchProducts()
        fetchCategories()
      });
      watch(() => route.query.search, (newSearchQuery) => {
        searchQ.value = newSearchQuery || "";
        fetchProducts();
      });
      watch(() => route.query.category, (newCategory) => {
        category.value = newCategory ? parseInt(route.query.category, 10)
          : null;
        fetchProducts();
      });
      return {
        products,
        fetchProducts,
        categories,
        category,
        fetchCategories,
        searchQ,
      }
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
  padding-top: 100px;
  margin-left: 20px;
  margin-right: 20px;
  align-items: center;
}
.products-grid {
  width: 1200px;
  display: grid !important;
  grid-template-columns: repeat(auto-fit, minmax(290px, 1fr)) !important;
  gap: 20px;
  margin-top: 20px;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}
.search-products-input{
  width:100%;
  min-width: 300px;
}
.search-filters{
  flex-grow: 1;
  display: flex;
  flex-direction: row;
  width: 1200px;
  gap: 20px;
}
</style>
