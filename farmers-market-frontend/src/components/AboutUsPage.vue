<template>
  <div class="page-container">
    <Header class="navbar"></Header>
    <Card title="About Us" class="about-card">
      <template #title>About Us</template>
      <template #content>
        <p>
          Welcome to the Farmer Marketplace! Our platform connects local farmers
          with consumers, fostering a community of fresh, organic, and locally
          sourced produce. We are dedicated to supporting sustainable agriculture
          and helping farmers reach a wider audience. Our mission is to promote
          healthier eating habits by making it easier for people to access fresh
          food straight from the farm.
        </p>
        <p>
          By joining our marketplace, you’re not just buying food; you’re
          supporting farmers, reducing your carbon footprint, and making a positive
          impact on the environment. Our platform is designed to bring together
          the best of local agriculture and connect you with producers who care
          about the quality of their crops.
        </p>
        <p>
          Explore our platform to discover the freshest products directly from
          the source. Whether you’re looking for fruits, vegetables, or artisanal
          goods, we have something for everyone. Your trust in us helps build a
          better food system, one that values sustainability and supports the
          hardworking individuals who make it all possible.
        </p>
        <p>
          We’re more than just a marketplace – we are a community of like-minded
          individuals who believe in the power of local food systems. Join us today
          and help shape the future of food!
        </p>
      </template>
    </Card>
    <Footer class="footer"></Footer>
  </div>
</template>

<script setup>
import Card from 'primevue/card';
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
  align-items: center; /* Center content horizontally */
  justify-content: center; /* Center content vertically */
}

.about-card {
  width: 100%;
  max-width: 800px;
  background-color: #ffffff;
  border: 1px solid #d9d9d9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.about-card p {
  line-height: 1.6;
  margin-bottom: 16px;
  color: #333;
}
</style>
