<template>
    <div class="card">
        <div>Category Revenue Trends</div>
        <Chart type="line" :data="chartData" :options="chartOptions" class="chart" />
    </div>
</template>

<script setup>
import { ref, watch} from "vue";
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
        const response = await axiosInstance.get(
            `/performance/month/category-revenue?year=${props.year}`
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

const setChartData = (categoryTrends) => {
    const allMonths = [
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    ];

    const trendsByCategory = {};
    categoryTrends.forEach(({ category, month, revenue }) => {
        if (!trendsByCategory[category]) {
            trendsByCategory[category] = {};
        }
        trendsByCategory[category][allMonths[month - 1]] = revenue; 
    });

    const colorMap = {};
    const datasets = Object.keys(trendsByCategory).map((category) => {
     
        if (!colorMap[category]) {
            colorMap[category] = generateRandomColor();
        }

        return {
            label: category,
            data: allMonths.map((month) => trendsByCategory[category][month] || 0), 
            fill: false,
            borderColor: colorMap[category],
            tension: 0.4,
        };
    });

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
