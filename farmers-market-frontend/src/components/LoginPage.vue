<template>
  <div class="login-container">
    <div class="form-toggle">
      <button @click="showLoginForm" :class="{ active: isLogin }">Login</button>
      <button @click="showRegisterForm" :class="{ active: !isLogin }">Register</button>
    </div>

    <!-- Login Form -->
    <form v-if="isLogin" @submit.prevent="handleLoginSubmit">
      <div class="form-group">
        <label for="username">Username</label>
        <input
          type="text"
          id="username"
          v-model="username"
          :class="{'is-invalid': errors.username}"
          placeholder="Enter your username"
          required
        />
        <span v-if="errors.username" class="error">{{ errors.username }}</span>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          type="password"
          id="password"
          v-model="password"
          :class="{'is-invalid': errors.password}"
          placeholder="Enter your password"
          required
        />
        <span v-if="errors.password" class="error">{{ errors.password }}</span>
      </div>

      <button type="submit" :disabled="loading" class="login-btn">
        <span v-if="loading">Logging in...</span>
        <span v-else>Login</span>
      </button>
    </form>

    <!-- Register Form -->
    <form v-if="!isLogin" @submit.prevent="handleRegisterSubmit">
      <div class="form-group">
        <div class="form-group">
        <label>Register as: </label>
        <label>
          <input
            type="radio"
            value="buyer"
            v-model="userType"
          />
          Buyer
        </label>
        <label>
          <input
            type="radio"
            value="farmer"
            v-model="userType"
            required
          />
          Farmer
        </label>
      </div>
        <label for="register-username">Username</label>
        <input
          type="text"
          id="register-username"
          v-model="username"
          :class="{'is-invalid': errors.username}"
          placeholder="Enter your username"
          required
        />
        <span v-if="errors.username" class="error">{{ errors.username }}</span>
      </div>

      <div class="form-group">
        <label for="register-password">Password</label>
        <input
          type="password"
          id="register-password"
          v-model="password"
          :class="{'is-invalid': errors.password}"
          placeholder="Enter your password"
          required
        />
        <span v-if="errors.password" class="error">{{ errors.password }}</span>
      </div>

      <div class="form-group">
        <label for="register-confirm-password">Confirm Password</label>
        <input
          type="password"
          id="register-confirm-password"
          v-model="confirmPassword"
          :class="{'is-invalid': errors.confirmPassword}"
          placeholder="Confirm your password"
          required
        />
        <span v-if="errors.confirmPassword" class="error">{{ errors.confirmPassword }}</span>
      </div>

      <!-- Address field visible only if farmer is selected -->
      <div v-if="userType === 'farmer'" class="form-group">
        <label for="register-address">Address</label>
        <input
          type="text"
          id="register-address"
          v-model="address"
          :class="{'is-invalid': errors.address}"
          placeholder="Enter your address"
        />
        <span v-if="errors.address" class="error">{{ errors.address }}</span>
      </div>

      <button type="submit" :disabled="loading" class="login-btn">
        <span v-if="loading">Registering...</span>
        <span v-else>Register</span>
      </button>
    </form>
  </div>
</template>
<script>
import { ref } from "vue";
import axiosInstance from "@/utils/axiosInstance";

export default {
  name: "LoginPage",
  setup() {
    const username = ref("");
    const password = ref("");
    const confirmPassword = ref("");
    const userType = ref("buyer");
    const address = ref("");
    const errors = ref({
      username: "",
      password: "",
      confirmPassword: "",
      address: "",
    });
    const loading = ref(false);
    const isLogin = ref(true);

    const showLoginForm = () => {
      isLogin.value = true;
      errors.value = { username: "", password: "", confirmPassword: "" };
    };

    const showRegisterForm = () => {
      isLogin.value = false;
      errors.value = { username: "", password: "", confirmPassword: "" };
    };

    const validateLoginForm = () => {
      errors.value = { username: "", password: "" };
      let isValid = true;

      if (!username.value) {
        errors.value.username = "Username is required";
        isValid = false;
      }

      if (!password.value) {
        errors.value.password = "Password is required";
        isValid = false;
      }

      return isValid;
    };

    const validateRegisterForm = () => {
      errors.value = { username: "", password: "", confirmPassword: "", address: "" };
      let isValid = true;

      if (!username.value) {
        errors.value.username = "Username is required";
        isValid = false;
      }

      if (!password.value) {
        errors.value.password = "Password is required";
        isValid = false;
      }

      if (password.value !== confirmPassword.value) {
        errors.value.confirmPassword = "Passwords do not match";
        isValid = false;
      }

      if (userType.value === "farmer" && !address.value) {
        errors.value.address = "Address is required for farmers";
        isValid = false;
      }

      return isValid;
    };

    const handleLoginSubmit = async () => {
      if (!validateLoginForm()) return;

      loading.value = true;
      try {
        const response = await axiosInstance.post("/auth/login", {
          username: username.value,
          password: password.value,
        });

        const { token } = response.data;
        localStorage.setItem("jwt", token);
        console.log(token);
        alert("Login successful");
      } catch (error) {
        console.error("Login error:", error.response?.data || error.message);
        alert("Login failed: " + (error.response?.data?.message || error.message));
      } finally {
        loading.value = false;
      }
    };

    const handleRegisterSubmit = async () => {
      if (!validateRegisterForm()) return;

      loading.value = true;
      try {
        const response = await axiosInstance.post("/auth/register", {
          username: username.value,
          password: password.value,
          userType: userType.value,
          address: userType.value === "farmer" ? address.value : null,
        });
        console.log(response);
        alert("Registration successful");
      } catch (error) {
        alert("Registration failed: " + (error.response?.data?.message || error.message));
      } finally {
        loading.value = false;
      }
    };

    return {
      username,
      password,
      confirmPassword,
      userType,
      address,
      errors,
      loading,
      isLogin,
      showLoginForm,
      showRegisterForm,
      handleLoginSubmit,
      handleRegisterSubmit,
    };
  },
};
</script>

<style scoped>
.login-container {
  width: 300px;
  padding: 20px;
  border: 1px solid #ccc;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
}

.form-toggle {
  display: flex;
  width: 100%;
  margin-bottom: 20px;
}

button {
  flex: 1;
  padding: 15px 0;
  border: none;
  cursor: pointer;
  background-color: #f1f1f1;
  font-size: 16px;
}

button.active {
  background-color: #537d8d;
  color: white;
}

.form-group {
  margin-bottom: 15px;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border: 1px solid #ccc;
}

input.is-invalid {
  border-color: red;
}

.error {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

button.login-btn {
  width: 100%;
  padding: 10px;
  background-color: #537d8d;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

button.login-btn:disabled {
  background-color: #ccc;
}
</style>
