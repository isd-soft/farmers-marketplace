<template>
    <div class="card">
        <Chart type="bar" :data="chartData" :options="chartOptions" class="chart" />
    </div>
</template>

<script setup>
import { ref, watch } from "vue";
import Chart from "primevue/chart";
import DatePicker from "primevue/datepicker";
import axiosInstance from "@/utils/axiosInstance";

const selectedDate = ref(new Date());
const chartData = ref();
const chartOptions = ref();

const fetchChartData = async () => {
    const year = selectedDate.value.getFullYear();
    try {
        const response = await axiosInstance.get(`/performance/order/monthly-count?year=${year}`);
        chartData.value = setChartData(response.data);
        chartOptions.value = setChartOptions();
    } catch (error) {
        console.error("Error fetching data", error);
    }
};

watch(selectedDate, fetchChartData);

fetchChartData();

const setChartData = (performanceProjections) => {
    // Map month numbers to names
    const months = [
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December"
    ];

    // Initialize revenue data with 0 for all months
    const data = new Array(12).fill(0);

    performanceProjections.forEach(projection => {
        const monthIndex = parseInt(projection.month, 10) - 1; // Convert to 0-based index
        const revenue = projection.revenue || 0;
        if (monthIndex >= 0 && monthIndex < 12) {
            data[monthIndex] = revenue; // Set revenue for the correct month
        }
    });

    return {
        labels: months,
        datasets: [
            {
                label: "Monthly Revenue",
                backgroundColor: "#00B5D8",
                borderColor: "#00B5D8",
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
}
</style>
