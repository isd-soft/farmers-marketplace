<template>
  <div class="error-page">
    <h1 class="error-big-text">404</h1>
    <h2 class="error-medium-text">oops, nothing was found</h2>
    <p class="error-small-text">
      The page you are looking for might have been recently removed, or you don't have the rights to
      accecss it
    </p>
    <Button label="Go Home" icon="pi pi-home" severity="success" @click="goHome" variant="text" />
  </div>
</template>
<script>
import Button from 'primevue/button';

export default {
  name: 'ErrorPage',
  setup() {
    const goHome = () => {
      window.location.href = '/';
    };
    return {
      goHome,
    };
  },

  components: {
    Button,
  },
};
</script>
<style scoped>
.error-big-text {
  font-family: 'Kanit', sans-serif;
  font-size: 180px;
  font-weight: 400;
  margin: 0px;
  margin-top: -100px;
  background: linear-gradient(
    300deg,
    rgba(1, 42, 16, 1) 0%,
    rgba(34, 197, 94, 1) 45%,
    rgba(199, 245, 216, 1) 100%
  );
  background-clip: text;
  color: transparent;
  text-align: center;
}
.error-medium-text {
  font-size: 33px;
  font-weight: 20;
  margin-top: -50px;
  margin-bottom: 1vh;
  text-transform: uppercase;
  letter-spacing: 3px;
  text-align: center;
}
.error-small-text {
  font-size: 16px;
  font-weight: 20;
  margin-top: 0px;
  margin-bottom: 4vh;
  text-align: center;
}
.error-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 80%;
}
</style>
