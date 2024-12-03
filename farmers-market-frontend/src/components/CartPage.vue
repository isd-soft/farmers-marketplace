<template>
  <div class="order">
    <Header class="navbar"></Header>
    <div class="main-container">
      <div @click="goHome">
        <div
          class="home-text"
          :class="{ 'green-color': isHovered }"
          @mouseover="isHovered = true"
          @mouseleave="isHovered = false"
        >
          Home
        </div>
      </div>
      <div class="main-orders-container">
        <div class="orders-container">
          <div class="card">
            <p class="title-text-cart">Cart</p>
            <DataView :value="cartProducts">
              <template #header> </template>
              <template #list="slotProps">
                <div class="flex flex-col order-container">

                  <div v-for="product in cartProducts" :key="product.productId">

                    <template>
                      <div class="card flex justify-center">
                        <Checkbox v-model="checked" binary />
                      </div>
                    </template>

                    <div
                      class="flex flex-col sm:flex-row sm:items-center p-6 gap-4 product-container"
                      :class="{
                        'border-t border-surface-200 dark:border-surface-700': product !== 0,
                      }"
                    >
                      <div class="md:w-40 relative product-image-title-container">
                        <img
                          class="block xl:block mx-auto rounded w-full product-image"
                          :src="getBase64Image(product.imageBase64, product.imageType)"
                          :alt="product.productTitle"
                        />
                        <div
                          class="absolute bg-black/70 rounded-border title-description-quantity-container"
                          style="left: 4px; top: 4px"
                        >
                          <div>
                            <h3 class="product-title-text">{{ product.productTitle }}</h3>
                            <p  class="product-paraghaph-text">{{ product.productDescription }}</p>
                          </div>

                          <div class="card flex flex-wrap gap-4 quantity-trash-container">
                            <div class="flex-auto ">
                                <InputNumber
                                  class="quantity-selector"
                                  v-model="product.quantity"
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
                            </div>
                            <i class="pi pi-trash" @click="removeItemFromCart(product.productId)"></i>
                          </div>
                        </div>

                        <div class="product-price-quantity-container">
                          <span class="text-xl font-semibold price-text"
                            >{{ product.pricePerUnit * product.quantity }} MDL</span
                          >
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </DataView>
          </div>
        </div>
        <div class="cart-pay-container">
          <h3>Your Order</h3>
          <div class="promo-code-text">
            <p>If you have an promocode, enter it here</p>
            <i class="pi pi-angle-down"></i>
          </div>
          <div onclick="myFunction()" class="card flex justify-center promo-code-container">
            <div class="promo-code-input">
              <InputText type="text" v-model="value" />
              <Button label="Submit" />
            </div>
          </div>
          <div>
            <h5>Subtotal</h5>
          </div>
          <div>
            <h3>Total</h3>
          </div>
          <div class="card flex justify-center">
            <Button label="Buy for" />
          </div>
        </div>
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>
<script setup>
import Header from '@/components/Header.vue'
import Footer from './Footer.vue'
import InputText from 'primevue/inputtext'
import { ref, onMounted, computed } from 'vue'
import axiosInstance from '@/utils/axiosInstance'
import DataView from 'primevue/dataview'
import Button from 'primevue/button'
import Select from 'primevue/select'
import Tag from 'primevue/tag'
import 'primeicons/primeicons.css'
import 'primeicons/primeicons.css'
import InputNumber from 'primevue/inputnumber'

const productQuantity = ref(1)
const cartProducts = ref([])
const value = ref(null)
const checked = ref(false)

const getSeverity = (product) => {
  switch (product.inventoryStatus) {
    case 'INSTOCK':
      return 'success'
    case 'LOWSTOCK':
      return 'warn'
    case 'OUTOFSTOCK':
      return 'danger'
    default:
      return null
  }
}

const goHome = () => {
  window.location.href = '/'
}

function getBase64Image(base64String, imageType = 'jpeg') {
  return `data:image/${imageType};base64,${base64String}`
}

function onPromoCodeClick(event, i) {
  let promoCodeContainer = document.querySelector('.promo-code-container')
  promoCodeContainer.classList.add('display-block')
}

onMounted(async () => {
  try {
    const response = await axiosInstance.get('/cart')
    cartProducts.value = response.data
    console.log(cartProducts.value)
  } catch (err) {
    console.error('Failed to fetch Cart Products', err)
  }
})

const removeItemFromCart = async (id) => {
  try {
    const response = await axiosInstance.delete(`/cart/${id}`);
    alert(response.data.message); // Alert or any user feedback you want to show
  } catch (error) {
    console.error('Error removing item from cart:', error);
    alert('Failed to remove item from cart. Please try again.');
  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.order {
  display: flex;
  flex-direction: column;
  gap: 0;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
}
.title-text-cart {
  font-size: 2rem;
}
.main-container {
  position: relative;
  margin-top: 80px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  gap: 5vh;
  padding: 6vh;
  width: 100%;
  height: max-content;
  background-color: #f2f2f2;
}
.main-orders-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 1vw;
  position: relative;
}
.home-text {
  color: #8e90a7;
  font-size: 0.9rem;
  font-weight: 300;
  cursor: pointer;
  position: absolute;
  left: 19vw;
  margin: 0;
  padding: 10px;
}

.home-text:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .home-text {
    left: 16vw;
  }
}
@media (max-width: 700px) {
  .home-text {
    left: 14vw;
  }
}

@media (max-width: 526px) {
  .home-text {
    left: 10vw;
  }
}

@media (max-width: 425px) {
  .home-text {
    left: 2vw;
  }
}
.cart-pay-container,
.orders-container {
  border-radius: 15px;
  background-color: #fff;
  padding: 30px;
  height: max-content;
  min-width: max-content;
}
.orders-container {
  width: 50%;
}
.cart-pay-container {
  position: relative;
  width: 15vw;
  width: 20%;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
  background-color: #fff;
  padding: 30px;
  border-radius: 15px;
}
.promo-code-text {
  display: flex;
  width: 90%;
  align-items: center;
  gap: 0.7vw;
  cursor: pointer;
}
.promo-code-container {
  display: none;
}
.display-block {
  display: block;
}
.promo-code-input {
  display: flex;
  gap: 1vw;
}
.promo-code-text p,
.promo-code-text i {
  font-size: 0.8rem;
}
.product-image {
  width: 7vw;
  max-width: 10vw;
  height: auto;
  border-radius: 10px;
}
.product-title-text{
  font-weight: 700;
  font-size: 1.7rem;
}
.product-paraghaph-text{
  font-size: 0.8rem;
}
.title-description-quantity-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.product-image-title-container {
  display: flex;
  gap: 2vw;
}
.product-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 2vh;
  padding: 1vh 1vh;
  border-radius: 15px;
  overflow: hidden;
  box-shadow:
    0 4px 6px rgba(0, 0, 0, 0.1),
    0 1px 3px rgba(0, 0, 0, 0.06);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.order-container {
  margin-top: 2vh;
  display: flex;
  flex-direction: column;
  gap: 4vh;
}
.price-text {
  font-size: 1.5rem;
  font-weight: 600;
  color: black;
}
.product-price-quantity-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.quantity-selector {
  width: 7vw;
  height: 2.5vh;
}
.quantity-trash-container{
  display: flex;
  align-items: center;
  gap: 0.5vw;
}

.p-inputnumber-button {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0.2rem;
}
.p-inputnumber-button .pi {
  font-size: 0.5rem;
  color: #000;
}
.pi-trash{
  cursor: pointer;
}
.pi-trash:hover{
  color: rgb(186, 0, 0);
}
.footer {
  margin: 0;
  background-color: #fff;
  padding: 20px;
}
</style>
