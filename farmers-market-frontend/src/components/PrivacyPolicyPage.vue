<template>
  <div class="home">
    <Header class="navbar"></Header>
    <Card title="Privacy Policy" class="privacy-card">
      <template #title>Privacy Policy</template>
      <template #content>
        <div class="card">
          <Accordion value="0">
            <!-- Information We Collect Section -->
            <AccordionPanel value="0">
              <AccordionHeader>1. Information We Collect</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  We collect information that you provide to us directly, such as your name, email address, and contact information when you create an account, make a purchase, or contact us for support.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- How We Use Your Information Section -->
            <AccordionPanel value="1">
              <AccordionHeader>2. How We Use Your Information</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  We use the information we collect to provide you with the best possible service. This includes processing your orders, communicating with you regarding your purchases, and sending updates or promotions related to our products and services.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- How We Protect Your Information Section -->
            <AccordionPanel value="2">
              <AccordionHeader>3. How We Protect Your Information</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  We take the security of your personal data seriously. Our platform uses encryption technology and secure servers to ensure your data is protected. We follow industry best practices to prevent unauthorized access, alteration, disclosure, or destruction of your information.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- Sharing Your Information Section -->
            <AccordionPanel value="3">
              <AccordionHeader>4. Sharing Your Information</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  We do not sell, trade, or otherwise transfer your personal information to third parties. We may share your data with trusted service providers who assist us in operating our platform, conducting business, or providing services to you, but only when necessary and under strict confidentiality agreements.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- Cookies Section -->
            <AccordionPanel value="4">
              <AccordionHeader>5. Cookies</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  Our website uses cookies to improve user experience, track visitors' preferences, and personalize content. You can control cookie settings in your browser; however, disabling cookies may limit certain functionality of our platform.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- Your Rights Section -->
            <AccordionPanel value="5">
              <AccordionHeader>6. Your Rights</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  You have the right to access, update, or delete your personal information at any time. If you would like to exercise any of these rights, please contact us at <strong>example@email.com</strong>.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- Changes to Privacy Policy Section -->
            <AccordionPanel value="6">
              <AccordionHeader>7. Changes to This Privacy Policy</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  We may update this Privacy Policy from time to time. When we do, we will post the revised policy on this page with the updated date. Please review this policy periodically to stay informed about how we are protecting your information.
                </p>
              </AccordionContent>
            </AccordionPanel>
            <!-- Contact Us Section -->
            <AccordionPanel value="7">
              <AccordionHeader>8. Contact Us</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  If you have any questions or concerns about our Privacy Policy, please contact us at <strong>example@email.com</strong>.
                </p>
              </AccordionContent>
            </AccordionPanel>
          </Accordion>
        </div>
      </template>
    </Card>
    <Footer class="footer"></Footer>
  </div>
</template>

<script setup>
import Card from 'primevue/card';
import Accordion from 'primevue/accordion';
import AccordionPanel from 'primevue/accordionpanel';
import AccordionHeader from 'primevue/accordionheader';
import AccordionContent from 'primevue/accordioncontent';
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
.privacy-card {
  width: 80%;
  background-color: #ffffff;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 1px 10px rgba(51, 65, 85, 0.3);
  padding: 20px;
}
@media (max-width: 380px) {
  .privacy-card {
    width: 90%;
    padding: 5px;
  }
}

.privacy-card p {
  line-height: 1.6;
  margin-bottom: 16px;
  color: #333;
}

.privacy-card h3 {
  margin-top: 24px;
  margin-bottom: 12px;
  color: #0056b3;
}

.card {
  width: 100%;
  padding: 10px;
}
.footer {
  margin: 0;
  padding-top: 20px;
  margin-top: 40px;
}
</style>
