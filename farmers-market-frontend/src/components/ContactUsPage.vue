<template>
  <div class="page-container">
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
.page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  height: max-content;
  align-items: center;
  justify-content: center;
}

.contact-card {
  width: 100%;
  max-width: 800px;
  background-color: #ffffff;
  border: 1px solid #d9d9d9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.contact-card p {
  line-height: 1.6;
  margin-bottom: 16px;
  color: #333;
}
</style>
