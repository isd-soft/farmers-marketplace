<template>
  <div>
    <img src="@/assets/farm.png" width="300px" height="300px">
    <button @click="sendAuthenticatedRequest">Test Authenticated Request</button>
  </div>
</template>

<script>
import axiosInstance from "@/utils/axiosInstance";
import { useRouter } from "vue-router";

export default {
  name: "HomePage",
  created() {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      console.log("no token");
      this.router.push("/login");
    }
  },
  methods: {
    async sendAuthenticatedRequest() {
      try {
        const response = await axiosInstance.get("/test/customer");
        console.log("Response:", response.data);
        alert("Authenticated request successful!");
      } catch (error) {
        console.error("Error during authenticated request:", error.response?.data || error.message);
        alert("Authenticated request failed: " + (error.response?.data?.message || error.message));
      }
    },
  },
  setup() {
    return {
      router: useRouter(),
    };
  },
};
</script>
