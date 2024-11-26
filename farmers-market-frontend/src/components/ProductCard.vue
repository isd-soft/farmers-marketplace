<template>
  <div class="product-card" @click="goToProductPage">

    <img
      v-if="product.images && product.images.length > 0"
      :src="getFirstImage(product.images)"
      alt="Product image"
      class="product-image"
    />
    <div class="product-info">
      <h3 class="product-title">{{ product.title }}</h3>
      <p class="product-description">
        {{ product.description }}
      </p>
      <div class="product-price">
        <span>{{ product.pricePerUnit }} MDL / {{ product.unitType }}</span>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "ProductCard",
  props: {
    product: {
      type: Object,
      required: true,
    },
  },

  methods: {
    goToProductPage() {
      this.$router.push(`/product/${this.product.id}`);
    },
    getFirstImage(images) {
      if (images.length > 0) {
        const firstImage = images[0];
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
