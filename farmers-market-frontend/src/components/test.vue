<template>
  <div v-animateonscroll="{ enterClass: 'animate-fadein', leaveClass: 'animate-fadeout' }" class="my-animated-element">
    <span>Hello, I animate on scroll!</span>
  </div>
</template>

<script setup>
import AnimateOnScroll from 'primevue/animateonscroll';
</script>

<style scoped>
.my-animated-element {
  height: 100px;
  background-color: lightcoral;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>