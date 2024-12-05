<template>
    <div class="card">
        <div>Monthly revenue</div>
        <Chart type="line" :data="chartData" :options="chartOptions" class="chart" />
    </div>
</template>

<script setup>
import { ref, watch } from "vue";
import Chart from "primevue/chart";
import axiosInstance from "@/utils/axiosInstance";

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
        const response = await axiosInstance.get(
            `/performance/month/revenue?year=${props.year}`
        );
        chartData.value = setChartData(response.data);
        chartOptions.value = setChartOptions();
    } catch (error) {
        console.error("Error fetching data", error);
    }
};

watch(
    () => props.year,
    () => {
        if (props.year) fetchChartData();
    }
);

fetchChartData();

const setChartData = (performanceProjections) => {
    const documentStyle = getComputedStyle(document.documentElement);
    const months = [
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December"
    ];

    const data = new Array(12).fill(0);

    performanceProjections.forEach(projection => {
        const monthIndex = projection.month - 1; 
        const revenue = projection.revenue || 0;
        if (monthIndex >= 0 && monthIndex < 12) {
            data[monthIndex] = revenue; 
        }
    });

    return {
        labels: months,
        datasets: [
            {
                label: "Monthly Revenue",
                fill: true,
                borderColor: documentStyle.getPropertyValue('--p-cyan-600'),
                tension: 0.4,
                backgroundColor: 'rgba(0, 181, 216, 0.2)',
                data: data
            }
        ]
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
    };
}
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
}
</style>
