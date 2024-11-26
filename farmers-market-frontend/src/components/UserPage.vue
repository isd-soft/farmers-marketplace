<template>
  <div v-if="loading" class="loading-container">
    <p>Loading...</p>
  </div>

  <div v-else>
    <h1>wat</h1>
    <div class="product-details">
      <p>
        <strong>User:</strong> {{ user.firstName + ' ' + user.lastName }}
      </p>
      <p>
        <strong>Farmer:</strong> {{ user.isFarmer }}
      </p>
    </div>


  </div>
</template>
<script>
import { ref, onMounted } from 'vue';
export default {
  props: ['id'],
  setup(props){
    const loading = ref(true);
    const user = ref(null);
    const fetchUser = async () => {
      try{
        const response = await axiosInstance.get(`/users/${props.id}`);
        user.value = response.data;
        console.log("USER GOT");
        console.log(response.data);
      }
      catch(error){
        console.log("ERROR GETTING USER", error);
      }
      finally {
        loading.value = false;
      }
    }
    onMounted(() => {
      fetchUser();
    })
    return {
      loading,
      user
    };
  }
}
import axiosInstance from '@/utils/axiosInstance.js';
</script>
