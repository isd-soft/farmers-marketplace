<template>
  <div class="home">
    <Header class = "navbar"></Header>
    <main class="main-content">

      <div class="order-status-fitering-container">
          <div class="order-staus-icons" >
            <i class="pi pi-list"></i>
            <p>All</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-ellipsis-h"></i>
            <p>Pending (2)</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-verified"></i>
            <p>Confirmed</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-spinner-dotted"></i>
            <p>In Progress</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-send"></i>
            <p>Shipped</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-box"></i>
            <p>Delivered</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-times"></i>
            <p>Cancelled</p>
          </div>
          <div class="order-staus-icons">
            <i class="pi pi-check"></i>
            <p>Completed</p>
          </div>
        </div>
    </main>

    <Footer class = "footer"></Footer>
  </div>

</template>
<script>
import Header from './Header.vue';
import Footer from './Footer.vue';

export default {
  name: "OrderManagementPage",
  components: {
  Header,
  Footer,
  }
}
</script>
<style>
.order-status-fitering-container,
.orders-container {
  border-radius: 15px;
  background-color: #fff;
  padding: 30px;
  height: max-content;
  min-width: max-content;
}
.order-status-fitering-container {
  position: relative;
  width: 15vw;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
  background-color: #fff;
  padding: 30px;
  border-radius: 15px;
}
.orders-container {
  width: 45vw;
}
.order-staus-icons {
  display: flex;
  gap: 1vw;
  align-items: center;
  cursor: pointer;
}
.order-staus-icons i {
  font-size: 1rem;
}
.home{
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  overflow-x: hidden;
  width: 100%;
  padding-top: 80px;
}

.main-content {
  width: 100%;
  flex:1;
}

.footer {
  text-align: center;
  padding: 10px;
}
</style>
