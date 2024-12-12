  <template>
  <div class="deals-page">
  <Header/>
  <div class="carousels">
    <h2>Deals Above 50% Off</h2>
    <Carousel :value="productDeals.discountedAbove50Percent.content" :numVisible="3" :numScroll="1" class="custom-carousel">
      <template #item="slotProps">
        <div class="product-card">
          <img :src="toBase64Image(slotProps.data.image.bytes)" alt="Product Image" />
          <h3>{{ slotProps.data.title }}</h3>
          <p>{{ slotProps.data.description }}</p>
          <span class="price">{{ slotProps.data.pricePerUnit | currency }}</span>
          <span class="discount">{{ slotProps.data.discountPercents }}% Off</span>
        </div>
      </template>
    </Carousel>

    <h2>Deals Above 30% Off</h2>
    <Carousel :value="productDeals.discountedAbove30Percent.content" :numVisible="3" :numScroll="1" class="custom-carousel">
      <template #item="slotProps">
        <div class="product-card">
<!--          <img :src="toBase64Image(slotProps.data.image.bytes)" alt="Product Image" />-->
          <h3>{{ slotProps.data.title }}</h3>
          <p>{{ slotProps.data.description }}</p>
          <span class="price">{{ slotProps.data.pricePerUnit | currency }}</span>
          <span class="discount">{{ slotProps.data.discountPercents }}% Off</span>
        </div>
      </template>
    </Carousel>

    <h2>Deals Above 15% Off</h2>
    <Carousel :value="productDeals.discountedAbove15Percent.content" :numVisible="3" :numScroll="1" class="custom-carousel">
      <template #item="slotProps">
        <div class="product-card">
<!--          <img :src="toBase64Image(slotProps.data.image.bytes)" alt="Product Image" />-->
          <h3>{{ slotProps.data.title }}</h3>
          <p>{{ slotProps.data.description }}</p>
          <span class="price">{{ slotProps.data.pricePerUnit | currency }}</span>
          <span class="discount">{{ slotProps.data.discountPercents }}% Off</span>
        </div>
      </template>
    </Carousel>

    <h2>Deals Above 5% Off</h2>
    <Carousel :value="productDeals.discountedAbove5Percent.content" :numVisible="3" :numScroll="1" class="custom-carousel">
      <template #item="slotProps">
        <div class="product-card">
<!--          <img :src="toBase64Image(slotProps.data.image.bytes)" alt="Product Image" />-->
          <h3>{{ slotProps.data.title }}</h3>
          <p>{{ slotProps.data.description }}</p>
          <span class="price">{{ slotProps.data.pricePerUnit | currency }}</span>
          <span class="discount">{{ slotProps.data.discountPercents }}% Off</span>
        </div>
      </template>
    </Carousel>
  </div>
  <Footer/>
  </div>
</template>
<script>
import { ref, onMounted } from 'vue';
import axiosInstance from '@/utils/axiosInstance'; // Import the axiosInstance
import Carousel from 'primevue/carousel'; // Import the Carousel component
import Footer from '@/components/Footer.vue'; // Import the Footer component
import Header from '@/components/Header.vue'; // Import the Header component
import Button from 'primevue/button'; // Import the Button component
import Rating from 'primevue/rating'; // Import the Rating component
import Card from 'primevue/card'; // Import the Card component
import ProgressSpinner from 'primevue/progressspinner'; // Import the ProgressSpinner component

export default {
  name: 'ProductDealsPage',
  components: {
    Carousel,
    Footer,
    Header,
    Button,
    Rating,
    Card,
    ProgressSpinner,
  },
  setup() {
    const productDeals = ref({
      discountedAbove50Percent: [],
      discountedAbove30Percent: [],
      discountedAbove15Percent: [],
      discountedAbove5Percent: []
    });

    const fetchProductDeals = async () => {
      try {
        const response = await axiosInstance.get('/product/deals');
        if (response.data) {
          productDeals.value = response.data;
        }
      } catch (error) {
        console.error('Error fetching product deals:', error);
      }
    };

    const toBase64Image = (bytes) => {
      return `data:image/jpeg;base64,${btoa(
        new Uint8Array(bytes).reduce((data, byte) => data + String.fromCharCode(byte), '')
      )}`;
    };

    onMounted(() => {
      fetchProductDeals();
    });

    return {
      productDeals,
      toBase64Image
    };
  }
};
</script>

<style scoped>
.deals-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
}
.carousels {
  margin-top: 6em;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  font-size: 1.5em;
  color: #333;
}

.custom-carousel {
  margin-bottom: 40px;
}

.product-card {
  text-align: center;
  background-color: #fff;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.product-card img {
  max-width: 100%;
  height: auto;
  margin-bottom: 10px;
  border-radius: 4px;
}

.product-card h3 {
  font-size: 1.2em;
  color: #333;
  margin-bottom: 5px;
}

.product-card p {
  font-size: 0.9em;
  color: #666;
  margin-bottom: 10px;
}

.price {
  font-size: 1em;
  color: #2ecc71;
  font-weight: bold;
  margin-right: 10px;
}

.discount {
  font-size: 0.9em;
  color: #e74c3c;
}
</style>
