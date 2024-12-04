<template>
    <div class="card flex justify-center">
        <div>Revenue by category</div>
        <Chart type="polarArea" :data="chartData" :options="chartOptions" class="chart" />
    </div>
</template>

<script setup>
import { ref, watch } from "vue";
import axiosInstance from "@/utils/axiosInstance";
import Chart from "primevue/chart";

const selectedDate = ref(new Date());
const chartData = ref();
const chartOptions = ref();

const fetchChartData = async () => {
    const year = selectedDate.value.getFullYear();
    try {
        const response = await axiosInstance.get(`/performance/category/revenue-sum?year=${year}`);
        chartData.value = setChartData(response.data);
        chartOptions.value = setChartOptions();
    } catch (error) {
        console.error("Error fetching data", error);
    }
};

const setChartData = (revenuePerCategorySum) => {
    const categories = Object.keys(revenuePerCategorySum);
    const data = Object.values(revenuePerCategorySum);

    return {
        labels: categories,
        datasets: [
            {
                label: "Revenue by Category",
                data: data,
                backgroundColor: generateColors(categories.length),
            }
        ]
    };
};

const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        maintainAspectRatio: false,
        aspectRatio: 0.6,
        plugins: {
            legend: {
                labels: {
                    color: textColor
                }
            }
        },
        scales: {
            r: {
                grid: {
                    color: surfaceBorder
                }
            }
        }
    };
}

const generateColors = (count) => {
    return Array.from({ length: count }, () =>
        `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, 0.8)`

    );
};

watch(selectedDate, fetchChartData);

fetchChartData();
</script>

<style scoped>
.chart {
    width: 95%;
    height: 95%;
}

.card {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    font-size: 1.5rem;
    color: #334155;
    gap: 10px;
}
</style>
