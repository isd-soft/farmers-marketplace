<template>
  <div class="page-container">
    <Header class="navbar"></Header>
    <Card title="Frequently Asked Questions" class="faq-card">
      <template #title>Frequently Asked Questions</template>
      <template #content>
        <div class="card">
          <Accordion value="0">
            <!-- Question 1 -->
            <AccordionPanel value="0">
              <AccordionHeader>1. How do I create an account?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  To create an account, simply click on the "Sign Up" button at the top of the page. You will need to provide your name, email address, and a password. After registration, you'll receive a confirmation email to verify your account.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 2 -->
            <AccordionPanel value="1">
              <AccordionHeader>2. How can I reset my password?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  If you've forgotten your password, click on the "Forgot Password" link on the login page. Enter your email address, and we’ll send you a link to reset your password. Follow the instructions in the email to create a new password.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 3 -->
            <AccordionPanel value="2">
              <AccordionHeader>3. How do I place an order?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  To place an order, browse our products and select the items you wish to purchase. Once you've added them to your cart, proceed to checkout, where you'll enter your shipping details and payment information. After completing the checkout, you will receive an order confirmation email.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 4 -->
            <AccordionPanel value="3">
              <AccordionHeader>4. What payment methods do you accept?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  We accept all major credit cards, including Visa, MasterCard, and American Express. Additionally, we offer payment via PayPal for added convenience.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 5 -->
            <AccordionPanel value="4">
              <AccordionHeader>5. How long will it take to receive my order?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  Delivery times depend on your location. Typically, orders are processed within 1-2 business days. Standard shipping usually takes 5-7 business days, while expedited shipping options may arrive in 2-3 business days.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 6 -->
            <AccordionPanel value="5">
              <AccordionHeader>6. Can I modify or cancel my order?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  Once an order is placed, we begin processing it immediately. If you need to make changes or cancel your order, please contact us within 24 hours. After that, we may not be able to accommodate any changes or cancellations.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 7 -->
            <AccordionPanel value="6">
              <AccordionHeader>7. Do you offer international shipping?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  Yes, we do offer international shipping to select countries. During checkout, you will be able to see if we ship to your country and the associated shipping costs.
                </p>
              </AccordionContent>
            </AccordionPanel>

            <!-- Question 8 -->
            <AccordionPanel value="7">
              <AccordionHeader>8. How can I contact customer support?</AccordionHeader>
              <AccordionContent>
                <p class="m-0">
                  If you have any questions or issues, you can contact our customer support team via email at <strong>support@example.com</strong>. Our team is available Monday through Friday from 9:00 AM to 5:00 PM.
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
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
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

.faq-card {
  width: 100%;
  max-width: 800px;
  background-color: #ffffff;
  border: 1px solid #d9d9d9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.faq-card p {
  line-height: 1.6;
  margin-bottom: 16px;
  color: #333;
}

.faq-card h3 {
  margin-top: 24px;
  margin-bottom: 12px;
  color: #0056b3;
}

.card {
  width: 100%;
  padding: 10px;
}
</style>
