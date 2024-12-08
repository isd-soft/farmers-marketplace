<template>
  <div class="order">
    <Header class="navbar"></Header>
    <div class="main-container">
      <div class="main-orders-container">
        <div class="order-status-fitering-container">
          <!-- Order status filtering icons here -->
        </div>
        <div class="orders-container">
          <div class="card">
            <DataView :value="orders" :sortOrder="sortOrder" :sortField="sortField">
              <template #header>
                <Select
                  v-model="sortKey"
                  :options="sortOptions"
                  optionLabel="label"
                  placeholder="Sort By Price"
                  @change="onSortChange"
                />
              </template>
              <template #list="slotProps">
                <div class="flex flex-col order-container">
                  <!-- Iterate over orders -->
                  <div v-for="order in orders" :key="order.id" class="order-item">
                    <!-- Iterate over products within each order -->
                    <div
                      v-for="product in order.itemsInOrder"
                      :key="product.id"
                      class="flex flex-col sm:flex-row sm:items-center p-6 gap-4 product-container"
                      :class="{ 'border-t border-surface-200 dark:border-surface-700': product !== 0 }"
                    >
                      <div class="md:w-40 relative product-image-title-container">
                        <img
                          class="block xl:block mx-auto rounded w-full product-image"
                          :src="getBase64Image(product.imageBase64, product.imageType)"
                          :alt="product.productTitle"
                        />
                        <div
                          class="absolute bg-black/70 rounded-border product-content"
                          style="left: 4px; top: 4px"
                        >
                          <div class="title-description-rating-container">
                            <div>
                              <h3 class="product-title-text">{{ product.productTitle }}</h3>
                              <p class="product-description">{{ product.productDescription }}</p>
                              <p>{{ product.quantity }} {{ product.unitType }}</p>
                              <!-- Display order creation date -->
                              <p>Order Created On: {{ formatOrderDate(order.createdDate) }}</p>
                            </div>
                          </div>
                          <div>
                            <Button
                              class="heart-button wishlist-icon"
                              outlined
                              :class="product.isInWishlist ? 'pi pi-heart-fill' : 'pi pi-heart'"
                              @click="toggleWishlist(product)"
                              :title="product.isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'"
                            ></Button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </DataView>
          </div>
        </div>
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>