<template>
  <div class="performance">
    <Header class="navbar"></Header>

    <div class="main-content">
      <div class="delimiter">
        <div class="title">Total revenue: {{ totalRevenue }} MDL</div>
        <DatePicker v-model="selectedYear" view="year" dateFormat="yy" class="year-picker" />
      </div>
      
      <div class="top-content">
        <Card class="card top-card">
          <YearlyRevenuePerCategoryChart :year="selectedYear.getFullYear()" />
        </Card>
        <Card class="card top-card">
          <YearlyRevenuePerProductChart :year="selectedYear.getFullYear()" />
        </Card>
        <Card class="card top-card">
          <MonthlyRevenueChart :year="selectedYear.getFullYear()" />
        </Card>
      </div>

      <div class="bottom-content">
        <Card class="card bottom-card">
          <MonthlyRevenuePerCategory :year="selectedYear.getFullYear()" />
        </Card>
        <Card class="card bottom-card">
          <CategoryRevenueTrends :year="selectedYear.getFullYear()" />
        </Card>
      </div>
    </div>
    <Footer class="footer"></Footer> 
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import axiosInstance from "@/utils/axiosInstance";
import MonthlyRevenueChart from './charts/MonthlyRevenueChart.vue';
import Header from './Header.vue';
import Footer from './Footer.vue';
import YearlyRevenuePerCategoryChart from "./charts/YearlyRevenuePerCategoryChart.vue";
import YearlyRevenuePerProductChart from "./charts/YearlyRevenuePerProductChart.vue";
import CategoryRevenueTrends from "./charts/CategoryRevenueTrends.vue";
import MonthlyRevenuePerCategory from "./charts/MonthlyRevenuePerCategory.vue";
import DatePicker from "primevue/datepicker";

const totalRevenue = ref(null);
const selectedYear = ref(new Date());

watch(selectedYear, (newDate) => {
  if (!(newDate instanceof Date)) {
    selectedYear.value = new Date(newDate);
  }
  fetchTotalRevenue(); 
});

const fetchTotalRevenue = async () => {
  const year = selectedYear.value instanceof Date
    ? selectedYear.value.getFullYear()
    : new Date(selectedYear.value).getFullYear();
  try {
    const response = await axiosInstance.get(
      `/performance/revenue?year=${year}`
    );
    totalRevenue.value = response.data; 
  } catch (error) {
    console.error("Error fetching total revenue:", error);
    totalRevenue.value = 0; 
  }
};

onMounted(() => {
  fetchTotalRevenue();
});
</script>

<style scoped>
.performance {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100vh;
  overflow-x: hidden;
  overflow-y: auto;
  padding-top: 100px;
  gap: 40px;
}

.delimiter {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #334155;
}

.title {
  font-size: 2rem;
  font-weight: bold;
  color: #334155;
}

.year-picker {
  background-color: white;
  color: #179739;
  border-radius: 5px;
  padding: 5px;
}

.main-content {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100vh;
  max-width: 80%;
  gap: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding-bottom: 20px;
}

.top-content {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  height: 70vh;
}

.top-card {
  width: 33%;
  height: 40vh;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}

.bottom-content {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  height: 100vh;
}

.bottom-card {
  width: 50%;
  height: 55vh;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}

.card {
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.total-revenue {
  text-align: center;
}

.revenue-value {
  font-size: 4.5rem;
  font-weight: bold;
  color: #179739;
}

.revenue-description {
  font-size: 1.5rem;
  color: #334155;
}

.main-content .footer {
  padding-left: 0;
  padding-right: 0;
}
</style>
