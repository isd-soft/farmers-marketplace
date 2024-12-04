<template>
    <div class="card">
        <div>Monthly revenue</div>
        <Chart type="bar" :data="chartData" :options="chartOptions" class="chart" />
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
            `/performance/order/monthly-revenue?year=${props.year}`
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

const setChartData = (ordersByMonth) => {
    const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    const data = months.map(month => ordersByMonth[month] || 0);

    return {
        labels: months,
        datasets: [
            {
                label: "Total Revenue",
                backgroundColor: "#7e00d8",
                borderColor: "#7e00d8",
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
        aspectRatio: 0.8,
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
                    color: textColorSecondary,
                    font: {
                        weight: 500
                    }
                },
                grid: {
                    display: false,
                    drawBorder: false
                }
            },
            y: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false
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
