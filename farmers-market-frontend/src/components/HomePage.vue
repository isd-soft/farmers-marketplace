<template>
  <div class="home">
    <Header class = "navbar"></Header>
    
  <!-- Banner -->
  <main class="main-content">
    <div class="carousel-container">
      <Carousel :value="images" :numVisible="1" :numScroll="1" circular :autoplayInterval="6000">
            <template #item="slotProps">
              <img :src="slotProps.data" alt="Banner Image" class="carousel-image" />
            </template>
        </Carousel>
    </div>
</main>

    <Footer class = "footer"></Footer>
  </div>
</template>

<script>
import Header from "./Header.vue"; 
import Footer from "../components/Footer.vue"; 
import Carousel from "primevue/carousel";
import banner1 from "@/assets/banner1.png";
import banner2 from "@/assets/banner2.png";
import banner3 from "@/assets/banner3.png";

import { useRouter } from "vue-router";

export default {
  name: "HomePage",
  components: {
  Header,
  Footer,
  Carousel,
},
 
  data() {
  return {
    images: [banner1, banner2, banner3],
  };
},

  setup() {
    return {
      router: useRouter(),
    };
  },
};
</script>
<style scoped>

.home{
  display: flex;
  flex-direction: column;
  min-height: 100vh; 
  overflow-x: hidden; 
  width: 100%;
  padding-top: 80px;
}

.main-content {
  width: 100%;
  flex:1;
}

.footer {
  text-align: center;
  padding: 10px;
}
.carousel-container {
  width:100%;      
  height: auto;  
}

.carousel-image {
  width: 100%;      
  height: auto;       
  object-fit: cover;  
  display: block;    
}


</style>




 
  
