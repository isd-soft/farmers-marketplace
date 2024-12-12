<template>
  <div class="home">
    <Header class="navbar"></Header>
    <div class="content">
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
    </div>
    <Footer class="footer"></Footer>
  </div>
</template>

<script setup>
import Card from 'primevue/card';
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<style scoped>
body{
  display: block !important;
}
.home{
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 120px;
  align-items: center;
}
.content{
  min-height: 80vh;
  width: 80%;
}
.about-card {
  width: 100%;
  background-color: #ffffff;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
  padding: 20px;
}
@media (max-width: 380px) {
  .about-card{
    padding: 5px;
  }
  .content{
    width: 90%;
  }
}

.about-card p {
  line-height: 1.6;
  margin-bottom: 16px;
  color: #333;
}
.footer {
  margin: 0;
  padding-top: 20px;
  margin-top: 40px;
}
</style>
