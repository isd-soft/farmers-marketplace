<template>
  <img src="@/assets/farm.png" width="300px" height="300px">
  <div class="login-container">

    <Tabs value="0">
      <TabList>
        <Tab value="0">Login</Tab>
        <Tab value="1">Register</Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0">
    <!-- Login Form -->
    <form @submit.prevent="handleLoginSubmit">
      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="email"
            v-model="email"
            :class="['input-style', {'is-invalid': errors.email}]"
            required
          />
          <label for="email">Email</label>
        </FloatLabel>
        <span v-if="errors.email" class="error">{{ errors.email }}</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <Password
            id="password"
            v-model="password"
            :feedback="false"
            :class="['input-style', {'is-invalid': errors.password}]"
            :inputStyle="{'width': '100%'}"
            required
          />
          <label for="password">Password</label>
        </FloatLabel>
        <span v-if="errors.password" class="error">{{ errors.password }}</span>
      </div>

      <ThemedButton
        type="submit"
        :disabled="loading"
        :loading="loading"
        class="login-btn"
        label="Login"/>
    </form>
    </TabPanel>
    <TabPanel value="1">
    <!-- Register Form -->
    <form @submit.prevent="handleRegisterSubmit">
      <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
        <SelectButton
          v-model="roleType"
          :options="roleOptions"
          optionLabel="label"
          optionValue="roleType"
        />
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-email"
            v-model="email"
            :class="['input-style', {'is-invalid': errors.email}]"
            required
          />
          <label for="register-email">Email</label>
        </FloatLabel>
        <span v-if="errors.email" class="error">{{ errors.email }}</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <Password
            id="register-password"
            v-model="password"
            :class="['input-style', {'is-invalid': errors.password}]"
            :inputStyle="{'width': '100%'}"
            :feedback="false"
            required
          />
          <label for="register-password">Password</label>
        </FloatLabel>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <Password
            id="register-confirm-password"
            v-model="confirmPassword"
            :class="['input-style', {'is-invalid': errors.confirmPassword}]"
            :inputStyle="{'width': '100%'}"
            :feedback="false"
            required
          />
          <label for="register-confirm-password">Confirm Password</label>
        </FloatLabel>
        <span v-if="errors.confirmPassword" class="error">{{ errors.confirmPassword }}</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-firstname"
            v-model="firstName"
            :class="'input-style'"
            required
          />
          <label for="register-firstname">First name</label>
        </FloatLabel>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-lastname"
            v-model="lastName"
            :class="'input-style'"
            required
          />
          <label for="register-lastname">Last name</label>
        </FloatLabel>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-phone"
            v-model="phoneNumber"
            :class="'input-style'"
            required
          />
          <label for="register-phone">Phone nr.</label>
        </FloatLabel>
      </div>

      <!-- Address field visible only if farmer is selected -->
      <div class="form-item" v-if="roleType === 'FARMER'">
        <FloatLabel variant="on">
          <InputText
            id="register-address"
            v-model="address"
            :class="['input-style', {'is-invalid': errors.address}]"
          />
          <label for="register-address">Address</label>
          </FloatLabel>
        <span v-if="errors.address" class="error">{{ errors.address }}</span>
      </div>

      <div class="form-item" v-if="roleType === 'FARMER'">
        <FloatLabel variant="on">
          <InputText
            id="register-description"
            v-model="description"
            :class="['input-style', {'is-invalid': errors.description}]"
          />
          <label for="register-description">Description</label>
        </FloatLabel>
        <span v-if="errors.description" class="error">{{ errors.description }}</span>
      </div>

      <ThemedButton
        type="submit"
        :disabled="loading"
        :loading="loading"
        class="login-btn"
        label="Register"/>
    </form>
    </TabPanel>
    </TabPanels>
    </Tabs>
  </div>
</template>
<script>
import { ref } from "vue";
import { useRouter} from "vue-router";
import axiosInstance from "@/utils/axiosInstance";


import Password from 'primevue/password';
import Tabs from 'primevue/tabs';
import TabList from 'primevue/tablist';
import Tab from 'primevue/tab';
import TabPanels from 'primevue/tabpanels';
import TabPanel from 'primevue/tabpanel';


export default {
  name: "LoginPage",
  components: {
    Tabs,
    Tab,
    TabList,
    TabPanel,
    TabPanels,
    Password,
  },
  setup() {
    const email = ref("");
    const password = ref("");
    const confirmPassword = ref("");
    const roleType = ref("CUSTOMER");
    const roleOptions = ref([
      { label: "Customer", roleType: "CUSTOMER" },
      { label: "Farmer", roleType: "FARMER" },
    ]);
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
    const router = useRouter();

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
      roleOptions,
      roleType,
      address,
      description,
      errors,
      loading,
      handleLoginSubmit,
      handleRegisterSubmit,
    };
  },
};
</script>

<style scoped>
.login-container {
  width: 300px;
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
}

.input-style {
  width: 100%;
}

.form-item {
  padding-top: 10px;
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
  margin-top: 20px;
  width: 100%;
  padding: 10px;
}
</style>
