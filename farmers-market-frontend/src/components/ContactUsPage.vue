<template>
  <div class="home">
    <Header class="navbar"></Header>
    <Card title="Contact Us" class="contact-card">
      <template #title>Contact Us</template>
      <template #content>
        <p>
          Thank you for visiting the Farmer Marketplace! We’re here to help and would love to hear from you.
          If you have any questions, feedback, or need assistance, please don't hesitate to reach out.
        </p>
        <p>
          Whether you're a customer looking for fresh produce or a farmer who wants to join our platform,
          we're ready to assist you. Our team is dedicated to providing excellent service and ensuring
          that your experience is smooth and enjoyable.
        </p>
        <p>
          You can contact us via email at <strong>example@email.com</strong>.
          We aim to respond within 24-48 hours.
        </p>
        <p>
          Feel free to also check our FAQ section or browse our support articles for quick answers to
          common questions. We are always happy to help!
        </p>
        <p>
          Thank you for being part of our community, and we look forward to hearing from you soon!
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
.contact-card {
  width: 80%;
  background-color: #ffffff;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
  padding: 20px;
}
@media (max-width: 380px) {
  .contact-card{
    width: 90%;
    padding: 5px;
  }
}
.contact-card p {
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
