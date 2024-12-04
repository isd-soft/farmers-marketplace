<template>
    <div class="card">
        <div>Category Revenue Trends</div>
        <Chart type="line" :data="chartData" :options="chartOptions" class="chart" />
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
        const response = await axiosInstance.get(
            `/performance/order/product-count?year=${year}`
        );
        chartData.value = setChartData(response.data);
        chartOptions.value = setChartOptions();
    } catch (error) {
        console.error("Error fetching data", error);
    }
};

const generateRandomColor = () => {
    const random = () => Math.floor(Math.random() * 255);
    return `rgb(${random()}, ${random()}, ${random()})`;
};

const setChartData = (ordersByMonth) => {
    const allMonths = [
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    ];

    const filledOrdersByMonth = allMonths.reduce((acc, month) => {
        acc[month] = ordersByMonth[month] || {};
        return acc;
    }, {});

    const productCategories = new Set();
    Object.values(filledOrdersByMonth).forEach((data) => {
        Object.keys(data).forEach((category) => productCategories.add(category));
    });

    const colorMap = {};
    Array.from(productCategories).forEach((category) => {
        colorMap[category] = generateRandomColor();
    });

    const datasets = Array.from(productCategories).map((category) => ({
        label: category,
        data: allMonths.map((month) => filledOrdersByMonth[month][category] || 0),
        fill: false,
        borderColor: colorMap[category],
        tension: 0.4,
    }));

    return {
        labels: allMonths,
        datasets,
    };
};

const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
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
            x: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            }
        }
    }
};

onMounted(() => {
    fetchChartData();
});
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
