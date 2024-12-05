<template>
    <div class="card">
        <div>Revenue by product</div>
        <Chart type="doughnut" :data="chartData" :options="chartOptions" class="chart" />
    </div>
</template>

<script setup>
import { ref, watch } from "vue";
import axiosInstance from "@/utils/axiosInstance";
import Chart from "primevue/chart";

const props = defineProps({
    year: {
        type: Number,
        required: true,
    },
});

const chartData = ref();
const chartOptions = ref();

const fetchChartData = async () => {
    try {
        const response = await axiosInstance.get(`/performance/product/revenue-sum?year=${props.year}`);
        chartData.value = setChartData(response.data);
        chartOptions.value = setChartOptions();
    } catch (error) {
        console.error("Error fetching data", error);
    }
};

const setChartData = (performanceProjections) => {
    const products = performanceProjections.map(projection => projection.product);
    const data = performanceProjections.map(projection => projection.revenue);

    return {
        labels: products,
        datasets: [
            {
                label: "Revenue by Category",
                data: data,
                backgroundColor: generateColors(products.length),
            }
        ]
    };
};

const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');

    return {
        maintainAspectRatio: false,
        aspectRatio: 0.6,
        plugins: {
            legend: {
                labels: {
                    cutout: '60%',
                    color: textColor
                }
            }
        }
    };
};

const generateColors = (count) => {
    return Array.from({ length: count }, () =>
        `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, 0.8)`

    );
};

watch(
    () => props.year,
    () => {
        if (props.year) fetchChartData();
    }
);

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
