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
                :class="{ 'out-of-stock-product': isOutOfStock(product) }"
              >
                <div class="md:w-40 relative product-container">
                  <div class="product-image-title-container">
                    <img
                      class="block xl:block mx-auto rounded w-full product-image"
                      :class="{ 'overflow-image': getSeverity(product) === 'OUTOFSTOCK' }"
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
                          :disabled="isOutOfStock(product)"
                          :min="1"
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
                      <span v-html="displayDiscountedPrice(product)"></span>
                      <span class="text-xl font-semibold price-text">
                        {{
                          isOutOfStock(product)
                            ? 'Unavailable'
                            : (
                                product.pricePerUnit *
                                product.quantity *
                                (1 - product.discountPercents / 100)
                              ).toFixed(2)
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

            <div class="card flex justify-center delivery-type-container">
              <h3>Delivery Type</h3>
              <Select
                v-model="selectedDeliveryType"
                :options="deliveryTypeValues"
                optionLabel="name"
                placeholder="Select a Delivery Type"
                class="w-full md:w-56 delivery-type-select"
                @change="onDeliveryTypeChange"
              />
            </div>
            <h3 class="payment-details-text">Payment Details</h3>
            <div class="payment-input-container">
              <InputGroup class="input-group">
                <label for="text" class="font-bold block mb-2">First Name</label>
                <InputText v-model="firstName" placeholder="John" />
              </InputGroup>

              <InputGroup class="input-group">
                <label for="text" class="font-bold block mb-2"> Last Name </label>
                <InputText v-model="lastName" placeholder="Doe" />
              </InputGroup>

              <InputGroup class="input-group">
                <label for="number" class="font-bold block mb-2"> Card Number </label>
                <InputNumber
                  class="card-input"
                  inputId="withoutgrouping"
                  :useGrouping="false"
                  v-model="cardNumber"
                  placeholder="5574-6698-8877-7699"
                  required
                />
              </InputGroup>

              <InputGroup class="input-group">
                <label for="text" class="font-bold block mb-2"> CVV </label>
                <InputNumber class="card-input" v-model="cvv" placeholder="***" required />
              </InputGroup>

              <InputGroup class="input-group">
                <label for="text" class="font-bold block mb-2"> Valid Until </label>
                <DatePicker v-model="validUntil" placeholder="10/05" required />
              </InputGroup>
            </div>

            <div class="all-cart-price-container">
              <h3 class="total-price-text">Total</h3>
              <p class="total-price-value">{{ totalCartPrice }} MDL</p>
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
import { ref, onMounted, computed, watch } from 'vue'
import axiosInstance from '@/utils/axiosInstance'
import DataView from 'primevue/dataview'
import Button from 'primevue/button'
import 'primeicons/primeicons.css'
import 'primeicons/primeicons.css'
import InputNumber from 'primevue/inputnumber'
import { isLoggedIn } from '@/shared/authState.js'
import Toast from 'primevue/toast'
import { useToast } from 'primevue/usetoast'
import Select from 'primevue/select'
import DatePicker from 'primevue/datepicker'

const cart = ref([])
const cartProducts = ref([])
const deliveryTypes = ref([])
const toast = useToast()
let totalPrice = ref(0)
let buttonBuyFor = ref('Buy for')
const selectedDeliveryType = ref(null)
const deliveryTypeValues = ref([])
const firstName = ref('')
const lastName = ref('')
const cardNumber = ref(null)
const cvv = ref(null)
const validUntil = ref(null)

const totalCartPrice = computed(() => {
  const productTotal = cartProducts.value.reduce((total, product) => {
    if (!isOutOfStock(product)) {
      total += product.pricePerUnit * product.quantity * (1 - product.discountPercents / 100)
    }
    return total
  }, 0)

  const totalWithDelivery = (productTotal + cart.value.totalPriceOfDelivery).toFixed(2)
  buttonBuyFor.value = `Buy for ${totalWithDelivery} MDL`
  return totalWithDelivery
})

function toastAdd(severity, summary, detail, life = 2000) {
  toast.add({
    severity: severity,
    summary: summary,
    detail: detail,
    life: life,
  })
}
function isOutOfStock(product) {
  return product.totalProductQuantity === 0
}

function displayDiscountedPrice(product) {
  const { pricePerUnit, quantity, discountPercents } = product
  const originalPrice = (pricePerUnit * quantity).toFixed(2)

  if (discountPercents > 0) {
    return `
      <span>
        <span style="text-decoration: line-through">${originalPrice} MDL</span>
      </span>`
  }
  return ``
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
  const severity = getSeverity(product)
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
  const severity = getSeverity(product)
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

// ON MOUNTED DELIVERY TYPES
onMounted(async () => {
  try {
    const deliveryResponse = await axiosInstance.get('/deliverytypes')
    deliveryTypes.value = deliveryResponse.data

    if (deliveryTypes.value.length > 0) {
      deliveryTypeValues.value = deliveryTypes.value.map((type) => ({
        name: type.type,
        value: type.type,
      }))

      selectedDeliveryType.value = deliveryTypeValues.value[0]
      await fetchCartItems(deliveryTypeValues.value[0].value)
    }
  } catch (err) {
    console.error('Failed to fetch Delivery Types or Cart Products', err)
  }
})

const fetchCartItems = async (deliveryType) => {
  try {
    const response = await axiosInstance.get('/cart', {
      params: { deliveryTypes: deliveryType },
    })

    cart.value = response.data
    cartProducts.value = response.data.itemInCartDTOS || []
    console.log('Cart Response:', cart.value)
  } catch (error) {
    console.error('Failed to fetch Cart Products', error)
  }
}

const onDeliveryTypeChange = async () => {
  if (selectedDeliveryType.value) {
    await fetchCartItems(selectedDeliveryType.value.name)
  }
}

const updateCart = async (product) => {
  if (!product.id) {
    console.error('Invalid product for updateCart')
    return
  }
  try {
    const response = await axiosInstance.put(`/cart/${product.id}`, {
      quantity: product.quantity,
    })
    console.log('Cart updated:', response.data)
  } catch (error) {
    console.error('Error updating cart:', error)
  }
}

const removeItemFromCart = async (id) => {
  if (!id) {
    alert('Invalid product')
    return
  }
  loginValidation()
  try {
    const response = await axiosInstance.delete(`/cart/${id}`)
    cartProducts.value = cartProducts.value.filter((p) => p.id !== id)
  } catch (error) {
    toastAdd('error', 'Removing Item Failed', 'Failed to remove item from cart. Please try again')
  }
}

const addProductsToOrder = async () => {
  const outOfStockProducts = cartProducts.value.filter((product) => isOutOfStock(product))

  if (cartProducts.value.length === 0) {
    toastAdd('error', 'Empty Cart', 'Your cart is empty. Add items before proceeding.')
    return
  }

  if (outOfStockProducts.length > 0) {
    toastAdd(
      'error',
      'Order Creation Failed',
      'Remove the product(s) that are out of stock before proceeding.',
    )
    return
  }

  if (!cardNumber.value || !cvv.value || !validUntil.value) {
    toastAdd(
      'error',
      'Incomplete Information',
      'Please fill in all the required payment details before proceeding.',
    )
    let inputGroups = document.getElementsByClassName('input-group')
    for (let inputGroup of inputGroups) {
      let input = inputGroup.querySelector('.input')
      if (input && (input.value === '' || input.value == null)) {
        input.classList.add('highlight-error')
      }
    }
    return
  }

  let inputGroups = document.getElementsByClassName('input-group')
  for (let inputGroup of inputGroups) {
    let input = inputGroup.querySelector('.input')
    if (input && input.value) {
      input.classList.remove('highlight-error')
    }
  }

  loginValidation()

  try {
    console.log(cart.value.totalPriceOfProducts)
    const response = await axiosInstance.post(`/order`, {
      totalPriceOfProducts: cart.value.totalPriceOfProducts,
      totalPriceOfDelivery: cart.value.totalPriceOfDelivery,
      totalPrice: cart.value.totalPrice,
      deliveryTypeFarmer: cart.value.deliveryTypeFarmer,
    })
    cartProducts.value = []
    buttonBuyFor.value = 'Buy for '
    cart.value.totalPriceOfDelivery = 0;

    toastAdd('success', 'Order Created', 'Your order has been successfully created!')
    console.log('Order Response', response.data)
  } catch (error) {
    console.error('Order Creation Failed', error)
    toastAdd(
      'error',
      'Order Creation Failed',
      'An error occurred while creating your order. Please try again.',
    )
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
  width: 50%;
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
.delivery-type-container {
  display: flex;
  justify-content: space-between;
}
.delivery-type-select {
  width: 12vw;
}
.total-price-text {
  font-size: 1.2rem;
}
.button-buy-for {
  width: 100%;
  font-size: 1.1rem;
}
.payment-input-container {
  display: grid;
  grid-template-columns: auto auto;
  column-gap: 1vw;
  row-gap: 2vh;
}
.payment-details-text {
  font-weight: 500;
}
.input-group {
  display: flex;
  flex-direction: column;
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
  width: 7vw;
  height: 11vh;
  object-fit: cover;
  border-radius: 10px;
}
.overflow-image {
  opacity: 0.3;
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
.highlight-error {
  border: 2px solid red;
  border-radius: 8px;
}

.footer {
  /* width: 100%; */
  margin: 0;
  padding-top: 20px;
}
@media (max-width: 768px) {
  .main-container {
    width: 90%;
  }
  .main-orders-container {
    display: flex;
    flex-direction: column;
    gap: 5vh;
    width: 100%;
  }
  .orders-container {
    width: 100%;
    padding: 7vw;
  }
  .order-container {
    width: 100%;
  }
  .product-container {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
  }
  .product-image-title-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    flex: 1;
  }
  .product-description {
    font-size: 0.7rem;
    max-width: 58vw;
  }
  .product-image {
    width: 100%;
    height: 15vh;
  }

  .product-price-quantity-container {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: space-between;
    min-width: 100%;
  }

  .quantity-trash-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 1vh;
  }
  .stock-availability {
    padding: 0.5vh 2.6vw;
  }
  .order-summary-container {
    width: 100%;
  }
  .payment-input-container {
    display: flex;
    flex-direction: column;
    gap: 2vh;
    padding: 0;
    margin: 0;
    justify-content: flex-start;
    width: 100%;
  }
  .delivery-type-container {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .delivery-type-select {
    min-width: 50vw;
  }
  .input-group {
    width: 72vw;
    margin: 0 auto;
  }
  .title-text-cart {
    font-size: 1.3rem;
  }
  .payment-images {
    display: flex;
    gap: 1vh;
  }
  .payment-images img {
    width: 15vw;
    height: auto;
  }
}
</style>
