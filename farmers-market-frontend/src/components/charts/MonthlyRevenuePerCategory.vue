<template>
    <div class="card">
        <div>Monthly Revenue by Category</div>
        <Chart type="bar" :data="chartData" :options="chartOptions" class="chart" />
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axiosInstance from "@/utils/axiosInstance";
import Chart from "primevue/chart";

const selectedDate = ref(new Date());
const chartData = ref();
const chartOptions = ref();

const fetchChartData = async () => {
    const year = selectedDate.value.getFullYear();
    try {
        const response = await axiosInstance.get(`/performance/order/monthly-revenue-category?year=${year}`);
        chartData.value = setChartData(response.data);
        chartOptions.value = setChartOptions();
    } catch (error) {
        console.error("Error fetching data", error);
    }
};

const setChartData = (monthlyRevenueByCategory) => {
    const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    const categories = Object.keys(monthlyRevenueByCategory);

    const datasets = categories.map((category) => ({
        label: category,
        backgroundColor: generateColor(),
        data: months.map((month) => monthlyRevenueByCategory[category][month] || 0),
    }));

    return {
        labels: months,
        datasets: datasets,
    };
};

const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        maintainAspectRatio: false,
        plugins: {
            legend: {
                labels: {
                    color: textColor,
                },
            },
        },
        scales: {
            x: {
                stacked: true,
                ticks: {
                    color: textColorSecondary,
                },
                grid: {
                    color: surfaceBorder,
                },
            },
            y: {
                stacked: true,
                ticks: {
                    color: textColorSecondary,
                },
                grid: {
                    color: surfaceBorder,
                },
            },
        },
    };
};

const generateColor = () => {
    return `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, 0.8)`;
};

onMounted(fetchChartData);
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