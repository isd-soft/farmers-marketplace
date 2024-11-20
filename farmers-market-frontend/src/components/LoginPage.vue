<template>
  <img src="@/assets/farm.png" width="300px" height="300px">
  <div class="login-container">
    <div class="form-toggle">
      <button @click="showLoginForm" :class="{ active: isLogin }">Login</button>
      <button @click="showRegisterForm" :class="{ active: !isLogin }">Register</button>
    </div>

    <!-- Login Form -->
    <form v-if="isLogin" @submit.prevent="handleLoginSubmit">
      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="text"
          id="email"
          v-model="email"
          :class="{'is-invalid': errors.email}"
          placeholder="Enter your email"
          required
        />
        <span v-if="errors.email" class="error">{{ errors.email }}</span>
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
        <label>Register as: </label>
        <label>
          <input
            type="radio"
            value="CUSTOMER"
            v-model="roleType"
          />
          Customer
        </label>
        <label>
          <input
            type="radio"
            value="FARMER"
            v-model="roleType"
            required
          />
          Farmer
        </label>
      </div>
      <div class="form-group">
        <label for="register-email">Email</label>
        <input
          type="text"
          id="register-email"
          v-model="email"
          :class="{'is-invalid': errors.email}"
          placeholder="Enter your email"
          required
        />
        <span v-if="errors.email" class="error">{{ errors.email }}</span>
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
      <div class="form-group">
        <label for="register-firstname">First name</label>
        <input
          type="text"
          id="register-firstname"
          v-model="firstName"
          placeholder="Enter your first name"
          required
        />
      </div>
      <div class="form-group">
        <label for="register-lastname">Last name</label>
        <input
          type="text"
          id="register-lastname"
          v-model="lastName"
          placeholder="Enter your last name"
          required
        />
      </div>
      <div class="form-group">
        <label for="register-phone">Phone nr.</label>
        <input
          type="text"
          id="register-phone"
          v-model="phoneNumber"
          placeholder="Enter your phone nr."
          required
        />
      </div>


      <!-- Address field visible only if farmer is selected -->
      <div v-if="roleType === 'FARMER'" class="form-group">
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

      <div v-if="roleType === 'FARMER'" class="form-group">
        <label for="register-description">Description</label>
        <input
          type="text"
          id="register-description"
          v-model="description"
          :class="{'is-invalid': errors.description}"
          placeholder="Enter your description"
        />
        <span v-if="errors.description" class="error">{{ errors.description }}</span>
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
import { useRouter} from "vue-router";
import axiosInstance from "@/utils/axiosInstance";

export default {
  name: "LoginPage",
  setup() {
    const email = ref("");
    const password = ref("");
    const confirmPassword = ref("");
    const roleType = ref("CUSTOMER");
    const firstName = ref("");
    const lastName = ref("");
    const phoneNumber = ref("");
    const address = ref("");
    const description = ref("");
    const errors = ref({
      email: "",
      password: "",
      confirmPassword: "",
      address: "",
      description: "",
    });
    const loading = ref(false);
    const isLogin = ref(true);
    const router = useRouter();

    const showLoginForm = () => {
      isLogin.value = true;
      errors.value = { email: "", password: "", confirmPassword: "" };
    };

    const showRegisterForm = () => {
      isLogin.value = false;
      errors.value = { email: "", password: "", confirmPassword: "" };
    };

    const validateLoginForm = () => {
      errors.value = { email: "", password: "" };
      let isValid = true;

      if (!email.value) {
        errors.value.email = "email is required";
        isValid = false;
      }

      if (!password.value) {
        errors.value.password = "Password is required";
        isValid = false;
      }

      return isValid;
    };

    const validateRegisterForm = () => {
      errors.value = {
        email: "",
        password: "",
        confirmPassword: "",
        address: "",
        description: "",
      };
      let isValid = true;

      if (!email.value) {
        errors.value.email = "email is required";
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

      if (!firstName.value) {
        errors.value.firstName = "First name is required";
        isValid = false;
      }

      if (!lastName.value) {
        errors.value.lastName = "Last name is required";
        isValid = false;
      }

      if (!phoneNumber.value) {
        errors.value.phoneNumber = "Phone number is required";
        isValid = false;
      }

      if (roleType.value === "farmer" && !address.value) {
        errors.value.address = "Address is required for farmers";
        isValid = false;
      }

      if (roleType.value === "farmer" && !description.value) {
        errors.value.description = "Description is required for farmers";
        isValid = false;
      }

      return isValid;
    };

    const handleLoginSubmit = async () => {
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");

      if (!validateLoginForm()) return;

      loading.value = true;
      try {
        const response = await axiosInstance.post("/auth/login", {
          email: email.value,
          password: password.value,
        });

        const { accessToken, refreshToken } = response.data;
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);

        console.log("Access Token:", localStorage.getItem("accessToken"));
        console.log("Refresh Token:", localStorage.getItem("refreshToken"));
        alert("Login successful");
        router.push("/" );
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
          email: email.value,
          password: password.value,
          firstName: firstName.value,
          lastName: lastName.value,
          phoneNumber: phoneNumber.value,
          address: roleType.value === "FARMER" ? address.value : null,
          description: roleType.value === "FARMER" ? description.value : null,
          roleType: roleType.value,
        });

        console.log("Registration successful:", response.data);
        alert("Registration successful! Please log in.");
        showLoginForm();
      } catch (error) {
        console.error("Registration error:", error.response?.data || error.message);
        alert("Registration failed: " + (error.response?.data?.message || error.message));
      } finally {
        loading.value = false;
      }
    };

    return {
      email,
      password,
      confirmPassword,
      firstName,
      lastName,
      phoneNumber,
      roleType,
      address,
      description,
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
