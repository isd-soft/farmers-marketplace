<script setup>
import Toast from 'primevue/toast';
// import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div id="app">
  <Toast/>
      <!-- <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
      </nav> -->
  <RouterView />
  </div>
</template>


