<template>
  <div class="order">
    <Header class="navbar"></Header>
    <Toast />
    <div class="main-container">
      <div class="main-orders-container">
        <div class="orders-container">
          <p class="title-text-cart">Cart</p>

          <DataView :value="cartProducts">
            <template #header> </template>
            <template #list="slotProps">
              <div
                class="flex flex-col order-container"
                v-for="product in cartProducts"
                :key="product.productId"
              >
                <div class="md:w-40 relative product-container">
                  <div class="product-image-title-container">
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
                        <p class="product-description">{{ product.productDescription }}</p>
                      </div>

                      <div class="card flex flex-wrap gap-4 quantity-trash-container">
                        <InputNumber
                          class="quantity-selector"
                          v-model="product.quantity"
                          inputId="horizontal-buttons"
                          showButtons
                          buttonLayout="horizontal"
                          :step="1"
                          mode="decimal"
                          fluid
                          @input="updateCart(product)"
                        >
                          <template #incrementbuttonicon>
                            <span class="pi pi-plus"></span>
                          </template>
                          <template #decrementbuttonicon>
                            <span class="pi pi-minus"></span>
                          </template>
                        </InputNumber>

                        <p class="unit-type-text">{{ product.unitType }}</p>
                        <i class="pi pi-trash" @click="removeItemFromCart(product.id)"></i>
                      </div>
                    </div>
                  </div>

                  <div class="product-price-quantity-container">
                    <div class="product-prices-container">
                      <span style="text-decoration: line-through"
                        >{{ product.pricePerUnit * product.quantity }} MDL</span
                      >
                      <span class="text-xl font-semibold price-text">
                        {{
                          product.pricePerUnit *
                          product.quantity *
                          (1 - product.discountPercents / 100)
                        }}
                        MDL
                      </span>
                    </div>

                    <div :class="severityClass(product)" class="stock-availability">
                      <i :class="iconClass(product)" class="icon"></i>
                      <span>{{ getSeverity(product) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </DataView>
        </div>
        <div class="cart-pay-container">
          <div class="order-summary-container">
            <h3 class="order-summary-price-text">Order Summary</h3>
            <div class="all-cart-price-container">
              <h3 class="total-price-text">Total</h3>
              <p class="total-price-value">{{ calculateTotalPrice() }} MDL</p>
            </div>

            <div class="card flex justify-center">
              <Button class="button-buy-for" @click="addProductsToOrder()">{{
                buttonBuyFor
              }}</Button>
            </div>
          </div>

          <div class="payment-methods-container">
            <p>Payment Methods</p>
            <div class="payment-images">
              <img src="@/assets/cart_page_resources/visa.png" alt="visa" />
              <img src="@/assets/cart_page_resources/mastercard.png" alt="mastercard" />
              <img src="@/assets/cart_page_resources/paypal.png" alt="paypal" />
              <img src="@/assets/cart_page_resources/apple pay.png" alt="paypal" />
            </div>
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
import { ref, onMounted, computed } from 'vue'
import axiosInstance from '@/utils/axiosInstance'
import DataView from 'primevue/dataview'
import Button from 'primevue/button'
import 'primeicons/primeicons.css'
import 'primeicons/primeicons.css'
import InputNumber from 'primevue/inputnumber'
import { isLoggedIn } from '@/shared/authState.js'
import Toast from 'primevue/toast'
import { useToast } from 'primevue/usetoast'

const cartProducts = ref([])
const toast = useToast()
let totalPrice = ref(0)
let buttonBuyFor = ref('Buy for')

function calculateTotalPrice() {
  totalPrice = 0
  cartProducts.value.forEach((p) => {
    totalPrice += p.pricePerUnit * p.quantity * (1 - p.discountPercents / 100)
  })
  buttonBuyFor.value = `Buy for ${totalPrice.toFixed(2)} MDL`
  return totalPrice
}

function getSeverity(product) {
  const quantity = product.totalProductQuantity

  if (quantity > 50) {
    return 'INSTOCK'
  } else if (quantity > 0 && quantity <= 50) {
    return 'LOWSTOCK'
  } else if (quantity === 0) {
    return 'OUTOFSTOCK'
  } else {
    return null
  }
}

function iconClass(product) {
  const severity = this.getSeverity(product)
  switch (severity) {
    case 'INSTOCK':
      return 'pi pi-check-circle'
    case 'LOWSTOCK':
      return 'pi pi-exclamation-circle'
    case 'OUTOFSTOCK':
      return 'pi pi-times-circle'
    default:
      return ''
  }
}
function severityClass(product) {
  const severity = this.getSeverity(product)
  switch (severity) {
    case 'INSTOCK':
      return 'in-stock'
    case 'LOWSTOCK':
      return 'low-stock'
    case 'OUTOFSTOCK':
      return 'out-of-stock'
    default:
      return 'unknown'
  }
}

function getBase64Image(base64String, imageType = 'jpeg') {
  return `data:image/${imageType};base64,${base64String}`
}

function loginValidation() {
  if (!isLoggedIn.value) {
    window.location.href = '/login'
    return
  }
}

onMounted(async () => {
  try {
    const response = await axiosInstance.get('/cart')
    cartProducts.value = response.data
    console.log('Cart', cartProducts.value)
  } catch (err) {
    console.error('Failed to fetch Cart Products', err)
  }
})

// const updateCart = async (id) => {
  // try {
  //   console.log('dscd', id)
  //   const response = await axios.put(`/cart/${product.id}`, {
  //     quantity: product.quantity,
  //   })
  //   console.log('Cart updated:', response.data)
  // } catch (error) {
  //   console.error('Error updating cart:', error)
  // }
// }

const updateCart = async (product) => {
  if (!product.id) {
    console.error('Invalid product for updateCart');
    return;
  }
  try {
    const response = await axiosInstance.put(`/cart/${product.id}`, {
      quantity: product.quantity,
    });
    console.log('Cart updated:', response.data);
  } catch (error) {
    console.error('Error updating cart:', error);
  }
};

const removeItemFromCart = async (id) => {
  console.log(id)
  if (!id) {
    alert('Invalid product')
    return
  }
  loginValidation()
  try {
    const response = await axiosInstance.delete(`/cart/${id}`)
    cartProducts.value = cartProducts.value.filter((p) => p.id !== id)
  } catch (error) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Failed to remove item from cart. Please try again.',
      life: 3000,
    })
  }
}

console.log(cartProducts.value)
const addProductsToOrder = async () => {
  if (cartProducts.value.length === 0) {
    toast.add({
      severity: 'error',
      summary: 'Empty Cart',
      detail: 'Your cart is empty. Add items before proceeding.',
      life: 3000,
    })
    return
  }
  loginValidation()
  try {
    const response = await axiosInstance.post(`/order`)
    cartProducts.value = []
    buttonBuyFor.value = 'Buy for'

    toast.add({
      severity: 'success',
      summary: 'Order Created',
      detail: 'Your order has been successfully created!',
      life: 3000,
    })

    console.log('Order Response', response.data)
  } catch (error) {
    console.error('Order Creation Failed', error)

    toast.add({
      severity: 'error',
      summary: 'Order Creation Failed',
      detail: 'An error occurred while creating your order. Please try again.',
      life: 3000,
    })
  }
}
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
  height: max-content;
  width: 100%;
}
.title-text-cart {
  font-size: 2rem;
}
.main-container {
  margin: 0 auto;
  width: 80%;
  position: relative;
  margin-top: 80px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  gap: 5vh;
  padding: 6vh 0;
  height: max-content;
}
.main-orders-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 1vw;
  position: relative;
}
.order-summary-container,
.orders-container {
  border-radius: 15px;
  padding: 30px;
  height: max-content;
  min-width: max-content;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.orders-container {
  width: 70%;
}
.cart-pay-container {
  position: relative;
  width: 30%;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
  border-radius: 15px;
}
.order-summary-container {
  display: flex;
  flex-direction: column;
  gap: 2vh;
}
.order-summary-price-text,
.total-price-value {
  font-weight: 700;
  font-size: 1.2rem;
}
.total-price-text {
  font-size: 1.2rem;
}
.button-buy-for {
  width: 100%;
  font-size: 1.1rem;
}
/* PAYMENT */
.payment-methods-container {
  padding: 0 30px;
}
.all-cart-price-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.payment-images {
  display: flex;
  gap: 1vw;
  align-items: center;
}
.payment-images img {
  width: 2.5vw;
  height: auto;
}
/* PRODUCT */
.product-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2vh;
  padding: 1vh 1vh;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
}
.product-check-box {
  width: min-content;
}
.product-image {
  min-width: 100px;
  width: 7vw;
  height: 11vh;
  object-fit: cover;
  max-width: 10vw;
  border-radius: 10px;
}
.product-title-text {
  font-weight: 700;
  font-size: 1.4rem;
}
.product-description {
  font-size: 0.7rem;
  height: max-content;
  max-width: 28vw;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.title-description-quantity-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 1.5vh;
}
.product-image-title-container {
  display: flex;
  gap: 2vw;
  align-items: flex-start;
}
.unit-type-text {
  font-size: 0.7rem;
}
.product-price-quantity-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 1.5vh;
  margin-right: 1vw;
}
.product-prices-container {
  display: flex;
  align-items: center;
  gap: 1vh;
}
.stock-availability {
  padding: 0.6vh 0.6vw;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
  border-radius: 30px;
  color: white;
  font-size: 0.8rem;
}
.icon {
  font-size: 0.8rem;
  margin-right: 0.3vw;
}
.in-stock {
  background-color: #28a745;
}
.low-stock {
  background-color: #ffc107;
}
.out-of-stock {
  background-color: #dc3545;
}
.unknown {
  background-color: #6c757d;
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

.quantity-selector {
  width: 9rem;
  min-width: 6rem;
  height: 2.5vh;
  flex-shrink: 0;
}

.quantity-trash-container {
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
.pi-trash {
  cursor: pointer;
}
.pi-trash:hover {
  color: rgb(186, 0, 0);
}

.footer {
  width: 100%;
  background-color: #fff;
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
  .main-container {
    padding: 0;
    /* display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center; */
  }
  .main-orders-container {
    display: flex;
    flex-direction: column;
  }
  .title-text-cart {
    font-size: 1.3rem;
  }
  .product-image-title-container {
    display: flex;
    flex-direction: column;
  }
  .product-title-text {
    font-size: 10px;
  }
  .home-text {
    left: 2vw;
  }
  /* .product-image{
    width: 100px;
  } */
}
</style>
