<template>
  <div class="home">
    <Header class="navbar"></Header>
    <Toast />
    <Card
      :style="{
        top: '10vh',
        width: 'auto',
        margin: '0 auto',
        maxWidth: '100%',
      }"
    >
      <template #content>
        <div v-if="isLoading" class="loading-container">
          <div class="loading-content" style="text-align: center; padding: 20px">
            <ProgressSpinner
              style="width: 80px; height: 80px"
              strokeWidth="5"
              animationDuration="1s"
            />
            <p style="margin-top: 20px; font-size: 1.3rem; font-weight: bold; color: #555">
              Loading, please wait...
            </p>
          </div>
        </div>

        <div v-else-if="hasError" class="error-message">
          <div class="error-content" style="text-align: center; padding: 20px">
            <i class="pi pi-exclamation-circle" style="font-size: 3rem; color: #d9534f"></i>
            <p style="margin-top: 15px; font-size: 1.3rem; font-weight: bold; color: #d9534f">
              Oops! Something went wrong. Please try again later.
            </p>
            <Button
              label="Go Back"
              class="p-button-secondary"
              icon="pi pi-arrow-left"
              style="margin-top: 15px"
              @click="$router.go(-1)"
            />
          </div>
        </div>

        <div v-else>
          <div class="product-page">
            <div class="product-content">
              <div class="product-gallery">
                <Galleria
                  :value="images"
                  :responsiveOptions="responsiveOptions"
                  :numVisible="5"
                  containerStyle="max-height: auto; margin:0%;"
                  :showItemNavigators="true"
                >
                  <template #item="slotProps">
                    <img
                      :src="slotProps.item.itemImageSrc"
                      :alt="slotProps.item.alt"
                      class="gallery-image"
                    />
                  </template>
                  <template #thumbnail="slotProps">
                    <img
                      :src="slotProps.item.thumbnailImageSrc"
                      :alt="slotProps.item.alt"
                      class="thumbnail-image"
                    />
                  </template>
                </Galleria>
              </div>

              <div class="product-details">
                <div class="product-name">{{ product.title || 'Product Name' }}</div>
                <div class="author-name">
                  <a :href="`/id${product.farmer.id}`">
                    {{ product.farmer.firstName || 'Farmer name' }}
                  </a>
                </div>
                <div class="product-cost">
                  <span v-if="product.discountPercents && product.discountPercents > 0">
                    <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 10px">
                      {{ product.pricePerUnit }} MDL
                    </s>
                    <span style="color: #007bff; font-size: 1.5rem">
                      {{
                        product.pricePerUnit * ((100 - product.discountPercents) / 100).toFixed(2)
                      }} MDL
                    </span>
                  </span>
                  <span v-else
                    ><span style="color: #007bff; font-size: 1.5rem">
                      {{ product.pricePerUnit }} MDL
                    </span>
                  </span>
                </div>

                <div class="product-rating">
                  <Rating v-model="product.rating" :stars="5" readonly />
                  <span class="reviews">{{ product.reviewCount || 0 }} reviews</span>
                </div>
                <div class="product-quantity">Quantity:</div>
                <p>Currently available {{ product.quantity }}</p>
                <div class="quantity-selector">
                  <InputNumber
                    v-model="quantity"
                    showButtons
                    buttonLayout="horizontal"
                    :inputStyle="{ width: '5em' }"
                    :min="1"
                    :max="product.quantity"
                  >
                    <template #incrementbuttonicon>
                      <span class="pi pi-plus" />
                    </template>
                    <template #decrementbuttonicon>
                      <span class="pi pi-minus" />
                    </template>
                  </InputNumber>
                  <span class="unit-type">{{ product.unitType }}</span>
                </div>
                <div class="button-with-heart">
                  <Button
                    class="add-to-cart-button"
                    @click="addToCart"
                    severity="warn"
                    style="
                      width: 12em;
                      color: white;
                      border: none;
                      display: inline-flex;
                      align-items: center;
                      justify-content: center;
                    "
                  >
                    {{ buttonText }}
                  </Button>
                  <Button
                    label="Schedule"
                    style="margin-left: 10px;"
                    @click="scheduleProduct(product.id)"
                  />

                  <i
                    :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
                    style="
                      font-size: 2.5rem;
                      cursor: pointer;
                      color: #179739;
                      margin-left: 10px;
                      vertical-align: middle;
                    "
                    @click="toggleWishlist"
                    :title="product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'"
                  />
                </div>
              </div>
            </div>
            <TabView>
              <TabPanel header="Details">
                <div class="tab-content">
                  <p>{{ product.description || 'No description' }}</p>
                </div>
              </TabPanel>

              <TabPanel header="Reviews">
                <CustomerReviews :id="id" :review-type="'product'" :canReview="product.canReview" />
              </TabPanel>

              <TabPanel header="Shipping">
                <div class="tab-content">
                  <p>{{ product.shipping_info || 'Shipping information coming soon.' }}</p>
                </div>
              </TabPanel>
            </TabView>
          </div>

        </div>
      </template>
    </Card>
    <Footer class="footer"></Footer>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/axiosInstance.js'
import noPhotoImg from '@/assets/noPhoto.png'
import Galleria from 'primevue/galleria'
import TabView from 'primevue/tabview'
import TabPanel from 'primevue/tabpanel'
import Rating from 'primevue/rating'
import Card from 'primevue/card'
import Button from 'primevue/button'
import InputNumber from 'primevue/inputnumber'
import ProgressSpinner from 'primevue/progressspinner'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import CustomerReviews from '@/components/CustomerReviews.vue'
import { isLoggedIn } from '@/shared/authState.js'
import Toast from 'primevue/toast'
import { useToast } from 'primevue/usetoast'
import router from "@/router/index.js";

export default {
  name: 'ProductPage',
  components: {
    CustomerReviews,
    Footer,
    Header,
    Galleria,
    Button,
    TabView,
    TabPanel,
    Rating,
    Card,
    InputNumber,
    ProgressSpinner,
    Toast,
  },
  props: ['id'],
  setup(props) {
    const buttonText = ref('Add to Cart')
    const product = ref({})
    const quantity = ref(1)
    const isAllReviewsLoaded = ref(false)
    const toast = useToast()
    const images = ref([])

    const responsiveOptions = ref([
      { breakpoint: '1024px', numVisible: 3 },
      { breakpoint: '600px', numVisible: 1 },
    ])

    const isLoading = ref(true)
    const hasError = ref(false)

    const fetchProduct = async () => {
      try {
        const response = await axiosInstance.get(`/product/${props.id}/page`)
        product.value = response.data
        console.log(product.value)

        if (response.data.images && response.data.images.length > 0) {
          images.value = response.data.images.map((img) => ({
            itemImageSrc: img.bytes.startsWith('data:image')
              ? img.bytes
              : `data:image/png;base64,${img.bytes}`,
            thumbnailImageSrc: img.bytes.startsWith('data:image')
              ? img.bytes
              : `data:image/png;base64,${img.bytes}`,
            alt: 'Product Image',
            title: 'Product Image',
          }))
        } else {
          images.value = [
            { itemImageSrc: noPhotoImg, thumbnailImageSrc: noPhotoImg, alt: 'No Photo Image' },
          ]
        }

        isLoading.value = false
      } catch (error) {
        console.error('Failed to load product:', error.message)
        hasError.value = true
        isLoading.value = false
      }
    }
    const scheduleProduct = (productId) => {
      router.push(`/schedule-order/${productId}`);
    }
    const toggleWishlist = async () => {
      if (!product.value.id) return
      try {
        if (product.value.isInWishlist) {
          await axiosInstance.delete(`/wishlist/${props.id}`)
        } else {
          await axiosInstance.post(`/wishlist/${props.id}`)
        }
        product.value.isInWishlist = !product.value.isInWishlist
      } catch (error) {
        console.error(
          `Failed to ${product.value.isInWishlist ? 'remove' : 'add'} product to/from wishlist:`,
          error.message,
        )
      }
    }

    const addToCart = async () => {
      if (!product.value.id || !quantity.value) {
        alert('Invalid product or quantity')
        return
      }
      if (!isLoggedIn.value) {
        window.location.href = '/login'
        return
      }

      const itemInCart = {
        productId: product.value.id,
        quantity: quantity.value,
      }
      try {
        const response = await axiosInstance.post('/cart', itemInCart)
        console.log('Added to cart:', response.data)

        toast.add({
          severity: 'success',
          summary: 'Item Added to cart',
          detail: 'This item was successfully added to cart.',
          life: 4000,
        })

        buttonText.value = 'Item Added'
        setTimeout(() => {
          buttonText.value = 'Add to Cart'
        }, 3000)
      } catch (error) {
        console.error('Error adding to cart:', error)

        if (error.response && error.response.status === 409) {
          toast.add({
            severity: 'warn',
            summary: 'Item Already in Cart',
            detail: 'This item is already in your cart.',
            life: 3000,
          })
        } else {
          toast.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to add item to cart. Please try again later.',
            life: 3000,
          })
        }
      }
    }

    onMounted(() => {
      fetchProduct()
    })

    return {
      buttonText,
      product,
      quantity,
      toast,
      images,
      responsiveOptions,
      isLoading,
      hasError,
      isAllReviewsLoaded,
      addToCart,
      toggleWishlist,
      scheduleProduct,
    }
  },
}
</script>

<style scoped>
.product-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 1rem;
}

.product-content {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
}

.product-gallery,
.product-details {
  flex: 1 1 100%;
  max-width: 100%;
}
@media (min-width: 1200px) {
  .product-gallery {
    width: 60em;
  }
}

@media (min-width: 768px) {
  .product-gallery {
    flex: 2;
    max-width: 100%;
  }

  .product-details {
    flex: 1;
  }

  .product-content {
    flex-wrap: nowrap;
  }
}

.product-details {
  flex: 1;
  padding: 10px 20px;
}

.product-name {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
}
.gallery-image {
  width: 100%;
  height: auto;
  aspect-ratio: 3 / 2;
  object-fit: cover;
}

.thumbnail-image {
  width: 100%;
  height: auto;
  max-width: 15em;
  aspect-ratio: 3 / 2;
  object-fit: cover;
  padding: 5%;
}
.product-cost {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-rating {
  margin-top: 15px;
  display: flex;
  align-items: center;
}

.reviews {
  margin-left: 10px;
  font-size: 1rem;
  color: #666;
}

.product-quantity {
  margin-top: 20px;
  font-size: 1.2rem;
  color: #333;
  font-weight: bold;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.unit-type {
  font-size: 1rem;
  color: #333;
}

.add-to-cart-button {
  margin-top: 20px;
  background-color: #169739;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-to-cart-button:hover {
  background-color: #12752c;
}

.footer {
  text-align: center;
  padding: 10px;
  bottom: 0;
  margin-top: auto;
}
.tab-content p {
  font-size: 1rem;
  color: #333;
}
.author-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #007bff;
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.author-name a {
  font-weight: bold;
  color: #007bff;
  text-decoration: none;
  transition: color 0.3s ease;
}

.author-name a:hover {
  color: #0056b3;
}

.author-name::before {
  content: "By ";
  color: #333;
  font-weight: normal;
  margin-right: 5px;
}

.home {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
}
</style>
