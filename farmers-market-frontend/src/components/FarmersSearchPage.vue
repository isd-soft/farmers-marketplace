<template>
  <div class="farmers-search-page">
    <Header class="navbar"></Header>
    <div class="main-container-farmers">
      <div class="search-filters">
        <FloatLabel variant="on" class="search-farmers-input">
          <InputText
            id="farmer-name"
            v-model="searchQ"
            class="search-farmers-input-inner"
            @input="fetchFarmers"
          />
          <label for="farmer-name">Search by Name</label>
        </FloatLabel>
        <FloatLabel variant="on" class="search-farmers-input">
          <Select
            id="farmer-sort"
            v-model="sortBy"
            :options="sortOptions"
            optionLabel="label"
            optionValue="value"
            @change="fetchFarmers"
            class="search-farmers-input-inner"
          />
          <label for="farmer-sort">Sort by</label>
        </FloatLabel>
      </div>

      <div class="farmers-list">
        <div v-if="farmers.length > 0">
        <div
          v-for="farmer in farmers"
          :key="farmer.id"
          class="farmer-card"
        >
          <Card>
            <template #content>
              <router-link :to="`/id${farmer.id}`" class="profile-link">
                <div class="farmer-profile">
                  <img :src="farmerPFP" alt="Profile Picture" class="farmer-avatar" />
                  <div class="farmer-details">
                    <p><b style="font-size: 1.2em">{{ farmer.firstName }} {{ farmer.lastName }}</b> </p>
                    <Rating
                      v-model="farmer.rating"
                      readonly
                      :stars="5"
                      :style="{
          '--p-rating-icon-size': '1.2rem'
          }"/>
                  </div>
                </div>
              </router-link>
            </template>
          </Card>
        </div>
        </div>
        <div v-else>
          <h1 style="text-align: center; margin-top: 50px; margin-bottom: 50px">No farmers found for your search.</h1>
        </div>
      </div>

      <Paginator
        style="margin-top: 30px"
        :rows="pageSize"
        :totalRecords="totalRecords"
        :first="currentPage * pageSize"
        @page="onPageChange"
      />
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import { ref, onMounted, watch } from "vue";
import axiosInstance from "@/utils/axiosInstance.js";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import Select from "primevue/select";
import Paginator from "primevue/paginator";
import { useRouter } from "vue-router";
import farmerPFP from '@/assets/farmer.png';
import Card from 'primevue/card'
import Rating from 'primevue/rating'

export default {
  name: "SearchFarmers",
  components: {
    Rating,
    Header,
    Footer,
    Select,
    Card,
    Paginator,
  },

  setup() {
    const farmers = ref([]);
    const searchQ = ref("");
    const currentPage = ref(0);
    const pageSize = ref(6);
    const totalRecords = ref(0);
    const sortOptions = ref([
      { label: "Sort by", value: null },
      { label: "Rating", value: "rating_desc" },
      { label: "Name", value: "name_asc" },
      { label: "Registration Date", value: "register_date" },

    ]);
    const sortBy = ref();
    const router = useRouter();

    const fetchFarmers = async () => {
      let sort = "";
      let dir = "";
      switch (sortBy.value) {
        case "rating_desc":
          sort = "rating";
          dir = "DESC";
          break;
        case "name_asc":
          sort = "firstName";
          dir = "ASC";
          break;
        case "register_date":
          sort = "createdDate";
          dir = "DESC";
          break;
        }
      try {
        const url = `/users?search=${searchQ.value}&page=${currentPage.value}&size=${pageSize.value}&sort=${sort},${dir}&roleParams=FARMERS`;
        const response = await axiosInstance.get(url);
        farmers.value = response.data.content;
        totalRecords.value = response.data.totalElements;
      } catch (error) {
        console.error("Error fetching farmers:", error);
      }
    };

    const onPageChange = (event) => {
      currentPage.value = event.page;
      fetchFarmers();
    };

    onMounted(() => {
      fetchFarmers();
    });

    watch(searchQ, () => fetchFarmers());

    return {
      farmers,
      fetchFarmers,
      searchQ,
      onPageChange,
      pageSize,
      totalRecords,
      currentPage,
      sortOptions,
      sortBy,
      farmerPFP
    };
  },
};
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
.farmers-search-page{
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 120px;
  align-items: center;
}
.main-container-farmers {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 80%;
  min-height: 100vh;
}
@media (max-width: 380px) {
  .main-container-farmers{
    width: 90%;
  }
}
.search-farmers-input{
  flex-grow: 1 !important;
}
.search-farmers-input-inner{
  min-width: 12em;
  max-width: none;
  width: 100%;
  height: 2.5em;
}
.search-filters {
  width:100%;
  padding-bottom: 2em;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  row-gap: 1em;
  column-gap: 2em;
  align-content: space-between;
}

.farmers-list {
  display: block;
  gap: 20px;
}

.farmer-card {
  padding: 15px;
  border-radius: 8px;
  width: 70em;
  margin-bottom: 20px;
}

.farmer-profile {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.farmer-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-link {
  text-decoration: none;
  color: #007bff;
  font-weight: bold;
  transition: color 0.3s ease;
}

.profile-link:hover {
  color: #0056b3;
}

.farmer-details p {
  margin: 5px 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: #007bff;
  display: flex;
  align-items: center;
  margin-top: 10px;
}
</style>
