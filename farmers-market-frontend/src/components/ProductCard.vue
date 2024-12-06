<template>
  <div class="product-card" @click="goToProductPage">
    <td>
      <div class="image-container">
        <img
          v-if="product.image"
          :src="getFirstImage(product.image)"
          alt="Product image"
          class="product-image"
        />
        <div v-else class="no-image">No Image</div>
      </div>
    </td>
    <div class="product-info">
      <h3 class="product-title">{{ product.title }}</h3>
      <p class="product-description">{{ product.description }}</p>
      <div class="product-cost-icons">
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
              ${{ product.priczzePerUnit }}
            </span>
          </span>
        </div>
        <div class="product-icons">
          <i id="addToCart" @click="addToCart" class="pi pi-shopping-cart cart-icon icons" ></i>
          <i
            :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
            class="icons"
            @click.stop="toggleWishlist"
            :title="product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {computed, ref} from "vue";
import axiosInstance from "@/utils/axiosInstance.js";
import {isLoggedIn} from "@/shared/authState.js";

export default {
  name: "ProductCard",
  props: {
    product: {
      type: Object,
      required: true,
    },
  },
  setup(props) {
    const quantity = props.product.quantity
    const toggleWishlist = async () => {
      if (!props.product.id) return;
      try {
        if (props.product.isInWishlist) {
          await axiosInstance.delete(`/customer/wishlist/${props.product.id}`);
        } else {
          await axiosInstance.post(`/customer/wishlist/${props.product.id}`);
        }
        props.product.isInWishlist = !props.product.isInWishlist;
      } catch (error) {
        console.error(
          `Failed to ${
            props.product.isInWishlist ? "remove" : "add"
          } product to/from wishlist:`,
          error.message
        );
      }
    };
    const addToCart = async () => {
      if (!props.product.id || !quantity.value) {
        alert('Invalid product or quantity')
        return
      }
      if (!isLoggedIn.value) {
        window.location.href = "/login";
        return;
      }

      const itemInCart = {
        productId: props.product.id,
        quantity: quantity.value,
      }

      try {
        const response = await axiosInstance.post('/cart', itemInCart)
        console.log('Added to cart:', response.data)

        buttonText.value = 'Item Added';
        setTimeout(() => {
          buttonText.value = 'Add to Cart';
        }, 3000);

      } catch (error) {
        console.error('Error adding to cart:', error)
      }
    }

    const discountedPrice = computed(() => {
      return (
        props.product.pricePerUnit *
        ((100 - (props.product.discountPercents || 0)) / 100)
      ).toFixed(2);
    });

    return {
      toggleWishlist,
      discountedPrice,
      addToCart,
      quantity,
    };
  },
  methods: {
    goToProductPage() {
      this.$router.push(`/product/${this.product.id}`);
    },
    getFirstImage(image) {
      if (image) {
        return `data:image/jpeg;base64,${image.bytes}`;
      }
      return "";
    },
  },
};
</script>

<style scoped>
.product-card {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
  max-width: 387px;
  vertical-align: central;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 1px 15px rgba(51, 65, 85, 0.3);
}

.image-container {
  width: 100%;
  height: 250px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
}

.product-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
}

.no-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  horiz-align: center;
  align-content: center;
  background-color: #17973930;
  font-size: 14px;
  font-weight: bold;
  text-align: center;
}

.product-info {
  padding: 15px;
}

.product-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 10px;
}

.product-description {
  font-size: 14px;
  color: #555;
  margin: 0 0 10px;
  height: 45px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.icons{
  font-size: 20px;
  cursor: pointer;
  color: #179739;
}
.product-cost-icons{
  display: flex;
  flex-direction: row;
  gap: 10px;
  justify-content: space-between;
}
.product-icons{
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-self: center;
}
</style>
