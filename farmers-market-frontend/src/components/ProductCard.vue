<template>
  <div class="product-card" @click="goToProductPage">

    <img
      v-if="product.image"
      :src="getFirstImage(product.image)"
      alt="Product image"
      class="product-image"
    />
    <div class="product-info">
      <h3 class="product-title">{{ product.title }}</h3>
      <p class="product-description">
        {{ product.description }}
      </p>
        <div class="product-cost">
                <span v-if="product.discountPercents && product.discountPercents > 0">
                  <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 10px">
                    ${{ product.pricePerUnit }}
                  </s>
                  <span style="color: #007bff; font-size: 1.5rem">
                    ${{
                      product.pricePerUnit * ((100 - product.discountPercents) / 100).toFixed(2)
                    }}
                  </span>
                </span>
          <span v-else
          ><span style="color: #007bff; font-size: 1.5rem">
                    ${{ product.pricePerUnit }}
                  </span>
                </span>
          <i
            :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
            style="font-size: 1rem; cursor: pointer; color: red; margin-left: 10px; vertical-align: middle;"
            @click="toggleWishlist"
            :title="product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'"
          />
        </div>
      </div>
  </div>
</template>

<script>

import axiosInstance from "@/utils/axiosInstance.js";

export default {
  name: "ProductCard",
  props: {
    product: {
      type: Object,
      required: true,
    },
    setup(props){
      const toggleWishlist = async () => {
        if (!this.product.value.id) return

        try {
          if (this.product.value.isInWishlist) {
            await axiosInstance.delete(`/customer/wishlist/${props.id}`)
          } else {
            await axiosInstance.post(`/customer/wishlist/${props.id}`)
          }
          this.product.value.isInWishlist = !this.product.value.isInWishlist
        } catch (error) {
          console.error(
            `Failed to ${this.product.value.isInWishlist ? 'remove' : 'add'} product to/from wishlist:`,
            error.message,
          )
        }
      }
      return {
        toggleWishlist,
      }
    },
  },


  methods: {
    goToProductPage() {
      this.$router.push(`/product/${this.product.id}`);
    },
    getFirstImage(image) {
      if (image) {
        const firstImage = image;
        return `data:image/jpeg;base64,${firstImage.bytes}`;
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
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
  max-width: 387px;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.2);
}

.product-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
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

.product-price {
  font-size: 16px;
  font-weight: bold;
}
</style>
