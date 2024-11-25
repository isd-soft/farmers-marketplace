<template>
  <div class="wishlist-container">
    <Card class="wishlist-card" style="margin: 20px auto; max-width: 100%;">
      <template #content>
        <h2 class="wishlist-header">My Wishlist</h2>
        <div v-if="isLoading" class="loading-container">
          <ProgressSpinner
            style="width: 50px; height: 50px;"
            strokeWidth="3"
            animationDuration="0.8s"
          />
          <p>Loading your wishlist...</p>
        </div>
        <div v-else-if="wishlist.length === 0">
          <p>No items in your wishlist yet.</p>
        </div>
        <div v-else class="wishlist-items">
          <div v-for="product in wishlist" :key="product.id" class="wishlist-item">
            <Card class="product-card">
              <template #header>
                <h3 class="product-title">{{ product.title }}</h3>
              </template>
              <template #content>
                <div class="product-info">
                  <p class="product-price">
                    <span v-if="product.discountPercents && product.discountPercents > 0">
                  <s style="color: #a0a0a0; font-size: 1.2rem; margin-right: 10px;">
                    ${{ product.pricePerUnit }}
                  </s>
                  <span style="color: #007bff; font-size: 1.5rem;">
                    ${{ (product.pricePerUnit)*(((100-product.discountPercents) / 100)).toFixed(2) }}
                  </span>
                </span>
                    <span v-else><span style="color: #007bff; font-size: 1.5rem;">
                    ${{ product.pricePerUnit }}
                  </span>
                </span>
                  </p>
                  <Rating v-model="product.rating" :stars="5" readonly style="margin-bottom: 10px;" />
                </div>
              </template>
              <template #footer>
                <Button
                  label="Remove"
                  icon="pi pi-trash"
                  class="p-button-danger"
                  @click="removeFromWishlist(product.id)"
                />
              </template>
            </Card>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import axiosInstance from "@/utils/axiosInstance.js";
import Galleria from "primevue/galleria";
import TabView from "primevue/tabview";
import TabPanel from "primevue/tabpanel";
import Rating from "primevue/rating";
import Card from "primevue/card";
import Button from "primevue/button";
import ProgressSpinner from "primevue/progressspinner";

export default {
  name: "WishlistPage",
  components: { Button, Rating, Card, ProgressSpinner },
  setup() {
    const isLoading = ref(true);
    const wishlist = ref([]);

    const fetchWishlist = async () => {
      try {
        const response = await axiosInstance.get("customer/wishlist");
        wishlist.value = response.data; // Replace this with your API response structure
      } catch (error) {
        console.error("Error fetching wishlist:", error);
      } finally {
        isLoading.value = false;
      }
    };

    const discountedPrice = (price, discount) => price * ((100 - discount) / 100);

    const removeFromWishlist = async (id) => {
      try {
        await axiosInstance.delete(`customer/wishlist/${id}`);
        wishlist.value = wishlist.value.filter((product) => product.id !== id);
      } catch (error) {
        console.error("Error removing item from wishlist:", error);
      }
    };

    onMounted(() => {
      fetchWishlist();
    });

    return {
      isLoading,
      wishlist,
      discountedPrice,
      removeFromWishlist,
    };
  },
};
</script>

<style scoped>
.wishlist-container {
  padding: 20px;
}

.wishlist-header {
  text-align: center;
  margin-bottom: 20px;
}

.loading-container {
  text-align: center;
  color: #555;
}

.wishlist-items {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.wishlist-item {
  box-sizing: border-box;
}

.product-card {
  padding: 20px;
  width: 100%; /* Make sure card takes up full width of the item */
}

.product-title {
  margin: 0;
  font-size: 1.5rem;
}

.product-info {
  margin-top: 10px;
}

.product-price {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

@media (max-width: 1024px) {
  .wishlist-item {
    flex: 1 1 calc(50% - 20px); /* 2 items per row on medium screens */
    max-width: calc(50% - 20px);
  }
}

@media (max-width: 768px) {
  .wishlist-item {
    flex: 1 1 100%; /* 1 item per row on small screens */
    max-width: 100%;
  }
}
</style>
