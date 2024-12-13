<template>
  <div class="home">
    <Header class="navbar"></Header>
    <div class="content">
    <h1  class="main-texts">Schedule order</h1>
    <p class="main-texts" style="margin-top: 20px">When you schedule an order, it will be fulfilled each week on the day of the week and time of your choice.</p>
    <div class="main-container-schedule">
      <div class="product-container-schedule" style="min-width: 200px">
        <div class="product-inner-container-scheduler">
            <img
              style="cursor: pointer"
              @click="goToProductPage(product)"
              class="product-image-schedule"
              :src="getFirstImage(product.images)"
              :alt="product.title"
            />
          <div class="title-description-quantity-container-schedule" >
            <div class="title-price-schedule">
              <h3
                style="cursor: pointer"
                @click="goToProductPage(product)"
                class="product-title-text">{{ product.title }}</h3>
              <div class="product-cost">
          <span v-if="product.discountPercents && product.discountPercents > 0">
            <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 10px">
              ${{ product.pricePerUnit }}
            </s>
            <span style="color: #179739; font-size: 1.5rem">
              ${{ discountedPrice }}
            </span>
          </span>
                <span v-else>
            <span style="color: #179739; font-size: 1.5rem">
              ${{ product.pricePerUnit }}
            </span>
          </span>
              </div>
            </div>
              <p class="product-description">{{ product.description }}</p>
              <div class="quantity-container">
                <InputNumber
                  class="quantity-selector"
                  v-model="order.quantity"
                  min="1"
                  inputId="horizontal-buttons"
                  showButtons
                  buttonLayout="horizontal"
                  :step="1"
                  mode="decimal"
                  fluid
                >
                  <template #incrementbuttonicon>
                    <span
                      style="font-size: 10px; color: black"
                      class="pi pi-plus" />
                  </template>
                  <template #decrementbuttonicon>
                    <span
                      style="font-size: 10px; color: black"
                      class="pi pi-minus" />
                  </template>
                </InputNumber>
                <span v-if="v$.quantity.$error" class="error-message">Quantity in you order is required and must be minimum 1 maximum 1000</span>
              </div>
            <div style="display: flex; flex-direction: column; width: 100%; align-items: end">
            <span class="price-text-part"
            >Items: {{totalItemsPrice}} MDL</span
            >
              <span class="price-text-part"
              >+ Delivery {{deliveryPrice}} MDL</span
              >
              <span class="price-total-text"
              >Total: {{totalPrice}} MDL</span
              >
            </div>
          </div>
        </div>
      </div>
        <div class="product-container-schedule" style="min-width: 120px;">
          <div class="day-time-container">
            <h3 class="payment-details-text">Payment Details</h3>
            <div class="time-container">
            <div class="input-error">
            <FloatLabel variant="on" class="schedule-input-labels">
                <InputText class="schedule-input" id="first-name" v-model="payment.firstName" />
              <label for="first-name">First name</label>
            </FloatLabel>
              <span v-if="vv$.firstName.$error" class="error-message"> First name is required (1-50 characters).</span>
            </div>

            <div class="input-error">
            <FloatLabel variant="on" class="schedule-input-labels">
                <InputText class="schedule-input" id="last-name" v-model="payment.lastName" />
              <label for="last-name">Last name</label>
            </FloatLabel>
              <span v-if="vv$.lastName.$error" class="error-message"> Last name is required (1-50 characters).</span>
            </div>
            </div>
            <div class="time-container">
            <div class="input-error">
            <FloatLabel variant="on" class="schedule-input-labels">
                <InputNumber
                  id="card number"
                  class="schedule-input"
                  inputId="withoutgrouping"
                  :useGrouping="false"
                  v-model="payment.cardNumber"
                  require
                  @keydown="restrictCardInput"
                />
              <label for="card number">Card number</label>
            </FloatLabel>
              <span v-if="vv$.cardNumber.$error" class="error-message">Card number is required (16 digits).</span>
            </div>

            <div class="input-error">
            <FloatLabel variant="on" class="schedule-input-labels">
                <InputNumber class="schedule-input" id="cvv"  v-model="payment.cvv" required @keydown="restrictCVVInput"/>
              <label for="cvv">Cvv</label>
            </FloatLabel>
              <span v-if="vv$.cvv.$error" class="error-message">CVV is required (3 digits).</span>
            </div>
            </div>

            <div class="input-error">
            <FloatLabel variant="on" class="schedule-input-labels">
                <DatePicker class="schedule-input" id="valid-util" v-model="payment.validUntil" required />
              <label for="valid-util">Valid until</label>
            </FloatLabel>
                <span v-if="vv$.validUntil.$error" class="error-message">Valid Until is required.</span>
            </div>

          <p class="payment-details-text">Choose day and time for scheduled order.</p>
              <FloatLabel variant="on" class="schedule-input-labels">
              <Select
                class="schedule-input"
                id="delivery-type"
                v-model="selectedDeliveryType"
                :options="deliveryTypeValues"
                optionLabel="label"
                option-value="value"
                @change = "totalDeliveryPrice"
              />
                <label for="delivery-type">DeliveryType</label>
              </FloatLabel>
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
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import {computed, onMounted, reactive, ref} from "vue";
import useVuelidate from "@vuelidate/core";
import {required, minValue, maxValue, minLength, maxLength, numeric} from "@vuelidate/validators"
import { useRouter } from "vue-router";
import Select from "primevue/select";
import InputNumber from "primevue/inputnumber";
import Button from "primevue/button";
import FloatLabel from "primevue/floatlabel";
import Header from "./Header.vue";
import Footer from "../components/Footer.vue";
import Checkbox from "primevue/checkbox";
import axiosInstance from "@/utils/axiosInstance.js";
import 'primeicons/primeicons.css'
import DatePicker from "primevue/datepicker";

export default {
  name: "Planner",
  components: {DatePicker, Checkbox, FloatLabel, Select, InputNumber, Button, Header, Footer },
  props: ['id'],
  setup(props) {
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
    const product = ref({})
    const selectedDeliveryType = ref(null)
    const deliveryTypeValues = ref([])

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
        product.value.pricePerUnit *
        ((100 - (product.value.discountPercents || 0)) / 100)
      ).toFixed(2);
    });
    const totalItemsPrice = computed(() => {
      return (discountedPrice.value * order.quantity
      ).toFixed(2);
    });
    const deliveryPrice = ref(0);
    const totalDeliveryPrice = (async () => {
      console.log(selectedDeliveryType.value)
      console.log(product.value.farmer.email)
      const resp = await axiosInstance.get("/deliverytypes/price", {
        params: {
          farmerEmail: product.value.farmer.email,
          deliveryType: selectedDeliveryType.value
        }
      });
      deliveryPrice.value = resp.data;
    });
    const payment = reactive({
      firstName: "",
      lastName: "",
      cardNumber: null,
      cvv: null,
      validUntil: null,
    });

    const ruless = computed(() => ({
      firstName: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(50),
      },
      lastName: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(50),
      },
      cardNumber: {
        required,
        numeric,
        minLength: minLength(16),
        maxLength: maxLength(16),
      },
      cvv: {
        required,
        numeric,
        minLength: minLength(3),
        maxLength: maxLength(3),
      },
      validUntil: {
        required
      },
    }));
    function restrictCardInput(event) {
      const input = event.target.value;

      const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
      if (allowedKeys.includes(event.key)) {
        return;
      }

      if (input.length >= 16 && /^\d$/.test(event.key)) {
        event.preventDefault();
      }

      if (!/^\d$/.test(event.key)) {
        event.preventDefault();
      }
    }


    function restrictCVVInput(event) {
      const input = event.target.value;

      const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
      if (allowedKeys.includes(event.key)) {
        return;
      }

      if ((input.length >= 4 || (input.length >= 3 && /^\d$/.test(event.key)))) {
        event.preventDefault();
      }

      if (!/^\d$/.test(event.key)) {
        event.preventDefault();
      }
    }
    const vv$ = useVuelidate(ruless, payment);
    const totalPrice = computed(() => {
      return (Number(totalItemsPrice.value) + Number(deliveryPrice.value)).toFixed(2);
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
      await vv$.value.$validate();
      if (!v$.value.$error) {

        try {
          const response = await axiosInstance.post("/planned-order", {
            productId: props.id,
            quantity: order.quantity,
            dayOfWeek: order.dayOfWeek,
            time: `${order.hour}:${order.minute}:00`,
            deliveryType: selectedDeliveryType.value,
          });
          await router.push("/schedule-order/management");
        } catch (error) {
          console.error(error);
        }
      }
    }
    onMounted(async () => {
      try {
        const response = await axiosInstance.get(`/product/${props.id}`)
        product.value = response.data
        console.log(product.value.id)
      } catch (err) {
        console.error('Failed to fetch Product', err)
        await router.push(`/`);
      }
      try {
        const deliveryResponse = await axiosInstance.get('/delivery-types-enum')
        if (Array.isArray(deliveryResponse.data)) {
          deliveryTypeValues.value = deliveryResponse.data.map((type) => ({
            label: type,
            value: type,
          }));
          console.log("Unit types loaded:", deliveryTypeValues.value);
          selectedDeliveryType.value = deliveryTypeValues.value[0].value
        }
      } catch (err) {
        console.error('Failed to fetch Delivery Types or Cart Products', err)
      }
      await totalDeliveryPrice();
    })

    return {
      daysOfWeek,
      hours,
      minutes,
      product,
      saveSchedule,
      order,
      discountedPrice,
      totalItemsPrice,
      v$,
      selectedDeliveryType,
      deliveryTypeValues,
      totalPrice,
      totalDeliveryPrice,
      deliveryPrice,
      vv$,
      ruless,
      restrictCardInput,
      restrictCVVInput,
      payment,
    };
},
  methods: {
    getFirstImage(images) {
      if (Array.isArray(images) && images.length > 0 && images[0]?.bytes) {
        return `data:image/jpeg;base64,${images[0].bytes}`;
      }
      return "";
    },
    goToProductPage(product) {
      this.$router.push(`/product/${product.id}`);
    },
  },
}
</script>

<style scoped>
body{
  display: block !important;
}
.home{
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 120px;
  align-items: center;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.main-texts{
  width: 100%;
  text-align: center;
}
.content{
  min-height: 80vh;
  width: 80%;
}
.main-container-schedule {
  position: relative;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
  width: 100%;
}
@media (max-width: 380px) {
  .content{
    width: 90%;
  }
}
.product-container-schedule {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
@media (max-width: 500px) {
  .product-container-schedule {
    padding: 13px;
  }
}
.product-inner-container-scheduler {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
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
.price-text-part {
  font-size: 1rem;
  font-weight: 500;
  color: black;
}
.price-total-text {
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
  max-width: 200px;
  width: 9rem;
  min-width: 6rem;
  height: 20px;
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
.payment-input-container {
  display: flex;
  flex-direction: column;
  column-gap: 1vw;
  row-gap: 2vh;
}
.payment-details-text {
  font-weight: 500;
}
.input-group {
  width: 72vw;
  margin: 0 auto;
}
.error-message {
  color: red;
  font-size: 0.9rem;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}
</style>
