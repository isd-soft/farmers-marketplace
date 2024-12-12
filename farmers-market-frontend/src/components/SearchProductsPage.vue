<template>
  <div class="home">
    <Header class = "navbar"></Header>
    <div class="main-container-products">
    <div class="search-filters">
    <FloatLabel variant="on" class="search-products-input">
      <InputText
        id="product-desc"
        v-model="searchQ"
        class="search-products-input-inner"
        @input="fetchProducts"
      />
      <label for="product-desc">Search</label>
    </FloatLabel>
    <FloatLabel variant="on" class="search-products-input">
      <Select
        id="product-category"
        v-model="category"
        :options="categories"
        optionLabel="label"
        optionValue="value"
        @change="fetchProducts"
        class="search-products-input-inner"
      />
      <label for="product-category">Category</label>
    </FloatLabel >
      <FloatLabel variant="on" class="search-products-input">
        <Select
          id="product-sort"
          v-model="sortBy"
          :options="sortOptions"
          optionLabel="label"
          optionValue="value"
          @change="fetchProducts"
          class="search-products-input-inner"
        />
        <label for="product-sort">Sort by</label>
      </FloatLabel>
    </div>
      <div v-if="products.length > 0">
    <div class="products-grid">
      <ProductCard
        v-for="product in products"
        :key="product.id"
        :product="product"
      />
    </div>
      </div>
      <div v-else>
        <h1 style="text-align: center; margin-top: 50px; margin-bottom: 50px">No products found for your search.</h1>
      </div>
    <Paginator
      style="margin-top: 30px"
      :rows="pageSize"
      :totalRecords="totalRecords"
      :first="currentPage * pageSize"
      @page="onPageChange"
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
  import Paginator from 'primevue/paginator';
  export default {
    name: 'SearchProducts',
    components: {
      ProductCard,
      Header,
      Footer,
      Select,
      Paginator
    },

    setup() {
      const products = ref([])
      const categories = ref([])
      const category = ref()
      const searchQ = ref("");
      const route = useRoute();
      const currentPage = ref(0);
      const pageSize = ref(6);
      const totalRecords = ref(0);
      const sortOptions = ref([
        { label: "Sort by", value: null },
        { label: "Price Asc", value: "price_asc" },
        { label: "Price Desc", value: "price_desc" },
        { label: "Rating Asc", value: "rating_asc" },
        { label: "Rating Desc", value: "rating_desc" },
      ]);
      const sortBy = ref()
      const fetchProducts = async () => {
        let sort = "";
        let dir = "";
        try {
          switch (sortBy.value){
            case "price_asc":
              sort = "pricePerUnit"
              dir = "ASC"
              break;
            case "price_desc":
              sort = "pricePerUnit"
              dir = "DESC"
              break;
            case "rating_asc":
              sort = "rating"
              dir = "ASC"
              break;
            case "rating_desc":
              sort = "rating"
              dir = "DESC"
              break;
          }
          let url = `/product?search=${searchQ.value}&page=${currentPage.value}&size=${pageSize.value}&sort=${sort},${dir}`;
          if (category.value) {
            url += `&category=${category.value}`;
          }
          const response = await axiosInstance.get(url);
          console.log(response)
          products.value = response.data.content;
          totalRecords.value = response.data.totalElements;
        } catch (error) {
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
        }
      }
      const onPageChange = (event) => {
        currentPage.value = event.page;
        fetchProducts();
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
        onPageChange,
        pageSize,
        totalRecords,
        currentPage,
        sortOptions,
        sortBy,
      }
    },
  }
</script>
<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
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
.main-container-products{
  position: relative;
  display: flex;
  flex-direction: column;
  width: 80%;
  min-height: 80vh;
}
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 30px;
  width:100%;
  justify-content: center;
  align-content: start;
}
@media (max-width: 380px) {
  .products-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  }
  .search-products-input-inner {
    min-width: 240px !important;
  }
  .main-container-products{
    width: 90%;
  }
}
@media (max-width: 1000px) {
  .products-grid {
    gap: 20px !important;
  }
  .search-filters{
    column-gap: 20px !important;
  }
}

.search-products-input{
  flex-grow: 1 !important;
}
.search-products-input-inner{
  min-width: 300px;
  max-width: none;
  width: 100%;
  height: 40px;
}
.search-filters{
  width:100%;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  row-gap: 20px;
  column-gap: 30px;
  align-content: space-between;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}
</style>
