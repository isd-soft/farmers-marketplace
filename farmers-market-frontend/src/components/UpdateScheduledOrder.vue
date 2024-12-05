<template>
  <div class="home">
    <Header class="navbar"></Header>
    <h1 style="max-width: 80%">Schedule order</h1>
    <p style="max-width: 80%; margin-top: 20px; text-align: center">When you schedule an order, it will be fulfilled each week on the day of the week and time of your choice.</p>
    <div class="main-container-schedule">
      <div class="product-container-schedule" style="min-width: 260px">
        <div class="product-inner-container-scheduler">
          <img
            class="product-image-schedule"
            v-if="getFirstImage(plannedOrder.product.images)"
            :src="getFirstImage(plannedOrder.product.images)"
            :alt="plannedOrder.product.title"
          />
          <div class="title-description-quantity-container-schedule" >
            <div class="title-price-schedule">
              <h3 class="product-title-text">{{ plannedOrder.product.title }}</h3>
              <div class="product-cost">
          <span v-if="plannedOrder.product.discountPercents && plannedOrder.product.discountPercents > 0">
            <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 10px">
              ${{ plannedOrder.product.pricePerUnit }}
            </s>
            <span style="color: #179739; font-size: 1.5rem">
              ${{ discountedPrice }}
            </span>
          </span>
                <span v-else>
            <span style="color: #179739; font-size: 1.5rem">
              ${{ plannedOrder.product.pricePerUnit }}
            </span>
          </span>
              </div>
              <span class="price-text"
              >{{totalPrice}} MDL</span
              >
            </div>
            <p class="product-description">{{ plannedOrder.product.description }}</p>
            <div class="quantity-container">
              <InputNumber
                class="quantity-selector"
                v-model="order.quantity"
                inputId="horizontal-buttons"
                showButtons
                buttonLayout="horizontal"
                :step="1"
                mode="decimal"
                fluid
              >
                <template #incrementbuttonicon>
                  <span class="pi pi-plus" />
                </template>
                <template #decrementbuttonicon>
                  <span class="pi pi-minus" />
                </template>
              </InputNumber>
              <span v-if="v$.quantity.$error" class="error-message">Quantity in you order is required and must be minimum 1 maximum 1000</span>
            </div>
          </div>
        </div>
      </div>
      <div class="product-container-schedule" style="min-width: 230px; max-width: 450px">
        <div class="day-time-container">
          <p>Choose day and time for scheduled order.</p>
          <div class="input-error">
            <FloatLabel variant="on" class="schedule-input-labels">
              <Select
                class="schedule-input"
                id="day-of-week"
                v-model="order.dayOfWeek"
                :options="daysOfWeek"
                optionLabel="label"
                optionValue="value"
              />
              <label for="day-of-week">Day of week</label>
            </FloatLabel>
            <span v-if="v$.dayOfWeek.$error" class="error-message">Day of week in your order is required</span>
          </div>
          <div class="time-container">
            <div class="input-error">
              <FloatLabel variant="on" class="schedule-input-labels">
                <Select
                  class="schedule-input"
                  id="hour"
                  v-model="order.hour"
                  :options="hours"
                  optionLabel="label"
                  optionValue="value"
                />
                <label for="hour">Hour</label>
              </FloatLabel>
              <span v-if="v$.hour.$error" class="error-message">Hour in your order is required</span>
            </div>
            <div class="input-error">
              <FloatLabel variant="on" class="schedule-input-labels">
                <Select
                  class="schedule-input"
                  id="minute"
                  v-model="order.minute"
                  :options="minutes"
                  optionLabel="label"
                  optionValue="value"
                />
                <label for="minute">Minute</label>
              </FloatLabel>
              <span v-if="v$.minute.$error" class="error-message">Minute in your order is required</span>
            </div>
          </div>
          <Button
            label="Save"
            @click="saveSchedule"
          />
        </div>
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import {computed, onMounted, reactive, ref} from "vue";
import useVuelidate from "@vuelidate/core";
import {required, minValue, maxValue, minLength, maxLength} from "@vuelidate/validators"
import { useRouter } from "vue-router";
import Select from "primevue/select";
import InputNumber from "primevue/inputnumber";
import Button from "primevue/button";
import FloatLabel from "primevue/floatlabel";
import Header from "./Header.vue";
import Footer from "../components/Footer.vue";
import Checkbox from "primevue/checkbox";
import axiosInstance from "@/utils/axiosInstance.js";
import axios from "axios";
import router from "@/router/index.js";

export default {
  name: "Planner",
  components: {Checkbox, FloatLabel, Select, InputNumber, Button, Header, Footer },
  props: ['id'],
  setup(props) {
    const plannedOrder = ref({
      product: {
        title: '',
        pricePerUnit: 0,
        discountPercents: 0,
        images: [],
        description: '',
      },
    });
    const order = reactive({
      quantity: 1,
      dayOfWeek: null,
      hour: null,
      minute: null,
    });
    const rules = computed(() => ({
      quantity: { required, minValue: minValue(1), maxValue: maxValue(1000) },
      dayOfWeek: { required},
      hour: { required },
      minute: { required},
    }));

    const v$ = useVuelidate(rules, order);
    const router = useRouter();

    const daysOfWeek = ref([
      { label: "Monday", value: "MONDAY" },
      { label: "Tuesday", value: "TUESDAY" },
      { label: "Wednesday", value: "WEDNESDAY" },
      { label: "Thursday", value: "THURSDAY" },
      { label: "Friday", value: "FRIDAY" },
      { label: "Saturday", value: "SATURDAY" },
      { label: "Sunday", value: "SUNDAY" },
    ]);
    const discountedPrice = computed(() => {
      return (
        plannedOrder.value.product.pricePerUnit *
        ((100 - (plannedOrder.value.product.discountPercents || 0)) / 100)
      ).toFixed(2);
    });
    const totalPrice = computed(() => {
      return (discountedPrice.value * order.quantity
      ).toFixed(2);
    });
    const hours = ref([
      { label: "0", value: "00" },
      { label: "1", value: "01" },
      { label: "2", value: "02" },
      { label: "3", value: "03" },
      { label: "4", value: "04" },
      { label: "5", value: "05" },
      { label: "6", value: "06" },
      { label: "7", value: "07" },
      { label: "8", value: "08" },
      { label: "9", value: "09" },
      { label: "10", value: "10" },
      { label: "11", value: "11" },
      { label: "12", value: "12" },
      { label: "13", value: "13" },
      { label: "14", value: "14" },
      { label: "15", value: "15" },
      { label: "16", value: "16" },
      { label: "17", value: "17" },
      { label: "18", value: "18" },
      { label: "19", value: "19" },
      { label: "20", value: "20" },
      { label: "21", value: "21" },
      { label: "22", value: "22" },
      { label: "23", value: "23" },
    ]);
    const minutes = ref([
      { label: "00", value: "00" },
      { label: "10", value: "10" },
      { label: "20", value: "20" },
      { label: "30", value: "30" },
      { label: "40", value: "40" },
      { label: "50", value: "50" },
    ]);

    const saveSchedule = async (event) => {
      event.preventDefault();
      await v$.value.$validate();
      if (!v$.value.$error) {

        try {
          const response = await axiosInstance.put(`/planned-order/update/${props.id}`, {
            quantity: order.quantity,
            dayOfWeek: order.dayOfWeek,
            time: `${order.hour}:${order.minute}:00`,
          });
          await router.push("/schedule-order/management");
        } catch (error) {
          console.error(error);
        }
      }
    }
    onMounted(async () => {
      try {
        const response = await axiosInstance.get(`/planned-order/${props.id}`)
        plannedOrder.value = response.data
        console.log(plannedOrder.value.id)
        order.quantity = plannedOrder.value.quantity;
        order.dayOfWeek = plannedOrder.value.dayOfWeek;
        const [hour, minute] = plannedOrder.value.time.split(":");
        order.hour = hour;
        order.minute = minute;
      } catch (err) {
        console.error('Failed to fetch Planned Order', err)
        await router.push(`/`);
      }
    })

    return {
      daysOfWeek,
      hours,
      minutes,
      plannedOrder,
      saveSchedule,
      order,
      discountedPrice,
      totalPrice,
      v$,
    };
  },
  methods: {
    getFirstImage(images) {
      if (Array.isArray(images) && images.length > 0 && images[0]?.bytes) {
        return `data:image/jpeg;base64,${images[0].bytes}`;
      }
      return "";
    },
  },
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.home {
  display: flex;
  flex-direction: column;
  gap: 0;
  justify-content: space-between;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
}
.main-container-schedule {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
  max-width: 80%;
  width: calc(100% - 40px);
}
.product-inner-container-scheduler {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%;
  justify-content: space-between;
  flex-grow: 1;
  gap: 20px;
}
.product-description {
  font-size: 0.9rem;
  height: 70px;
  max-width: 200px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.product-image-schedule {
  max-width: 120px;
  min-width: 60px;
  width: 100%;
  height: 80px;
  object-fit: cover;
  border-radius: 10px;
}
.product-title-text {
  font-weight: 700;
  font-size: 1.7rem;
}
.title-description-quantity-container-schedule {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  gap: 8px;
}
.product-container-schedule {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.price-text {
  font-size: 1.5rem;
  font-weight: 600;
  color: black;
}
.title-price-schedule {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-between;
}
.quantity-selector {
  width: 9rem;
  min-width: 6rem;
  height: 2.5vh;
  flex-shrink: 0;
}
.day-time-container{
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  gap: 20px;
}
.time-container{
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-content: space-between;
  flex-grow: 1;
  width: 100%;
  gap: 20px;
}
.schedule-input-labels{
  flex-grow: 1 !important;
}
.schedule-input{
  flex-grow: 1;
  min-width: 150px;
  max-width: none;
  width: 100%;
}
.input-error{
  display: flex;
  flex-direction: column;
  flex-grow: 1 !important;
  gap: 5px;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}
</style>
