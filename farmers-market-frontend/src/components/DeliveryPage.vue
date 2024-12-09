<template>
  <div class="page-container">
    <Header class="navbar"></Header>
    <div class="delivery-page">
      <div class="delivery-container">
        <Card class="delivery-card" style="margin: 2rem auto; max-width: 100%">
          <template #content>
            <h2 class="delivery-header">Available Delivery Options</h2>
            <div v-if="isLoading" class="loading-container">
              <ProgressSpinner
                style="width: 3rem; height: 3rem"
                strokeWidth="0.2rem"
                animationDuration="0.8s"
              />
              <p>Loading delivery options...</p>
            </div>
            <div v-else-if="deliveryTypes.length === 0">
              <p>No delivery options available at the moment.</p>
            </div>
            <div v-else class="delivery-items">
              <div v-for="delivery in deliveryTypes" :key="delivery.id" class="delivery-item">
                <Card class="delivery-option-card" style="overflow: hidden">
                  <template #header>
                    <div class="header-content">
                      <h3 class="delivery-title">{{ delivery.type }}</h3>
                    </div>
                  </template>
                  <template #content>
                    <div class="delivery-info">
                      <p class="delivery-price">
                        Price: <span style="color: #007bff; font-size: 1.5rem">{{ delivery.price }} MDL</span>
                      </p>
                      <p v-if="delivery.existsForUser">
                        This delivery option is available for your area.
                      </p>
                      <p v-else>
                        Unfortunately, this delivery option is not available for your area at this time.
                      </p>
                    </div>
                  </template>
                </Card>
              </div>
            </div>
          </template>
        </Card>
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/axiosInstance.js'
import Card from 'primevue/card'
import ProgressSpinner from 'primevue/progressspinner'
import Header from '@/components/Header.vue'
import Footer from "@/components/Footer.vue"

const isLoading = ref(true)
const deliveryTypes = ref([])

// Fetching available delivery options from the backend
const fetchDeliveryTypes = async () => {
  try {
    const response = await axiosInstance.get('/deliverytypes')
    deliveryTypes.value = response.data
  } catch (error) {
    console.error('Error fetching delivery options:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchDeliveryTypes()
})

</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
  align-items: center;
  justify-content: flex-start;
  padding-top: 8em;
}

.delivery-card {
  width: 100%;
  max-width: 800px;
  background-color: #ffffff;
  border: 1px solid #d9d9d9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.delivery-header {
  font-size: 2rem;
  margin-bottom: 16px;
}

.delivery-items {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.delivery-option-card {
  width: 100%;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.delivery-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #007bff;
}

.delivery-info {
  margin-top: 16px;
}

.delivery-price {
  font-size: 1.2rem;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
</style>
