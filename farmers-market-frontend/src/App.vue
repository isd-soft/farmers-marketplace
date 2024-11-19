<script setup>
// import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
      <!-- <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
      </nav> -->
  <RouterView />
</template>

<style scoped>
body {
  display: flex;
  justify-content: center; /* Horizontally center */
  align-items: center; /* Vertically center */
  height: 100vh; /* Make sure the parent has full viewport height */
  margin: 0; /* Remove default margin */
}
</style>
