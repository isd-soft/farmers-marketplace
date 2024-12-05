<script setup>
// import { RouterLink, RouterView } from 'vue-router'
import Toast from 'primevue/toast';
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


