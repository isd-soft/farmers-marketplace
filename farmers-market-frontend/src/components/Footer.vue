<template>
  <div class="footer">
    <div class="footer-container">
      <div class="footer-section">
        <img src="@/assets/logo.png" alt="Logo" class="footer-logo" @click="goHome" />
        <p class="footer-description">Your trusted platform for quality products and services.</p>
      </div>

      <div class="footer-section">
        <h3 class="footer-title">Quick Links</h3>
        <ul class="footer-links">
          <li><a href="/about">About Us</a></li>
          <li><a href="/contact">Contact Us</a></li>
          <li><a href="/faq">FAQ</a></li>
          <li><a href="/privacy">Privacy Policy</a></li>
        </ul>
      </div>

      <div class="footer-section">
        <h3 class="footer-title">Stay Connected</h3>
        <div class="social-icons">
          <a href="#" class="social-icon"><i class="pi pi-facebook"></i></a>
          <a href="#" class="social-icon"><i class="pi pi-twitter"></i></a>
          <a href="#" class="social-icon"><i class="pi pi-instagram"></i></a>
          <a href="#" class="social-icon"><i class="pi pi-linkedin"></i></a>
        </div>
      </div>

      <div class="footer-section">
        <h3 class="footer-title">Subscribe</h3>
        <div class="subscribe">
          <InputText v-model="email" placeholder="Your email address" />
          <button @click="subscribe" class="subscribe-button">Subscribe</button>
        </div>
      </div>
    </div>
  </div>
</template>

  <script setup>
  import { ref } from "vue";
  import InputText from 'primevue/inputtext';

  const email = ref("");

  const goHome = () => {
    window.location.href = '/';
  };

  const subscribe = () => {
    console.log("Subscribed with email:", email.value);
    email.value = "";
  };
  </script>

  <style scoped>
 .footer {
  display: flex;
  justify-content: center; 
  align-items: flex-start;
  background-color: var(--p-gray-100);
  width: 100%; 
  color: var(--p-gray-700);
  border-top: 1px solid var(--p-gray-300);
}

.footer-container {
  width: 80%; 
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 20px;
  margin-top: 10px;
}

.footer-section {
  flex: 1;
  text-align: center;
  max-width: 100%;
}

.footer-logo {
  width: 80px;
  cursor: pointer;
}

.footer-description {
  margin-top: 10px;
  font-size: 14px;
  line-height: 1.6;
}

.footer-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin-bottom: 5px;
}

.footer-links li:hover {
  color: #179739;
}

.footer-links a {
  text-decoration: none;
  color: var(--p-gray-700);
  transition: color 0.3s;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 10px;
}

.social-icon {
  color: var(--p-gray-700);
  font-size: 20px;
  transition: color 0.3s;
}

.subscribe {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.subscribe .p-inputtext {
  width: 200px;
}

.subscribe-button {
  background-color: #179739;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.social-icon:hover {
  color: #179739;
}

.subscribe-button:hover {
  background-color: #334155;
}
 </style>


