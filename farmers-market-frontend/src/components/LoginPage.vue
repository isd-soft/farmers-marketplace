<template>
  <div class="login-container">

    <Tabs v-model:value="activeTab">
      <TabList>
        <Tab value="0">Login</Tab>
        <Tab value="1">Register</Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0">
    <!-- Login Form -->
    <form>
      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="email"
            v-model="data.email"
            :class="['input-style']"
            required
          />
          <label for="email">Email</label>
        </FloatLabel>
        <span v-if="v$.email.$error" class="error-message">Email is required and must be in a valid email
          format</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <Password
            id="password"
            v-model="data.password"
            :feedback="false"
            :class="['input-style']"
            :inputStyle="{'width': '100%'}"
            required
          />
          <label for="password">Password</label>
        </FloatLabel>
        <span v-if="v$.password.$error" class="error-message">Password is required and must be at least 8
          characters long.</span>
          <span v-if="loginError" class="error-message">{{ loginError }}</span>
      </div>

      <ThemedButton
        @click = "submitForm"
        class="login-btn"
        label="Login"/>
    </form>
    </TabPanel>
    <TabPanel value="1">
    <!-- Register Form -->
    <form>
      <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
        <SelectButton
          v-model="data.roleType"
          :options="roleOptions"
          optionLabel="label"
          optionValue="roleType"
        />
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-email"
            v-model="data.email"
            :class="['input-style']"
            required
          />
          <label for="register-email">Email</label>
        </FloatLabel>
        <span v-if="v$.email.$error" class="error-message">Email is required and must be in a valid email
          format</span>
          <span v-if="registerError" class="error-message">{{ registerError }}</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <Password
            id="register-password"
            v-model="data.password"
            :class="['input-style']"
            :inputStyle="{'width': '100%'}"
            :feedback="false"
            required
          />
          <label for="register-password">Password</label>
        </FloatLabel>
        <span v-if="v$.password.$error" class="error-message">Password is required and must be at least 8
          characters long.</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <Password
            id="register-confirm-passwordv"
            v-model="data.confirmPassword"
            :class="['input-style']"
            :inputStyle="{'width': '100%'}"
            :feedback="false"
            required
          />
          <label for="register-confirm-password">Confirm Password</label>
        </FloatLabel>
        <span v-if="v$.confirmPassword.$error" class="error-message">Passwords do not match.</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-firstname"
            v-model="data.firstName"
            :class="'input-style'"
            required
          />
          <label for="register-firstname">First name</label>
        </FloatLabel>
        <span v-if="v$.firstName.$error" class="error-message"> First name is required and must be between 1
          and 50 characters</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-lastname"
            v-model="data.lastName"
            :class="'input-style'"
            required
          />
          <label for="register-lastname">Last name</label>
        </FloatLabel>
        <span v-if="v$.lastName.$error" class="error-message"> Last name is required and must be between 1 and
          50 characters</span>
      </div>

      <div class="form-item">
        <FloatLabel variant="on">
          <InputText
            id="register-phone"
            v-model="data.phoneNumber"
            :class="'input-style'"
            required
          />
          <label for="register-phone">Phone nr.</label>
        </FloatLabel>
        <span v-if="v$.phoneNumber.$error" class="error-message"> Phone number is required and must contain at
          least 10 digits</span>
      </div>

      <!-- Address field visible only if farmer is selected -->
      <div class="form-item" v-if="data.roleType === 'FARMER'">
        <FloatLabel variant="on">
          <InputText
            id="register-address"
            v-model="data.address"
            :class="['input-style']"
          />
          <label for="register-address">Address</label>
        </FloatLabel>
        <span v-if="v$.address.$error" class="error-message">Address must be between 5 and 100
          characters</span>
      </div>

      <div class="form-item" v-if="data.roleType === 'FARMER'">
        <FloatLabel variant="on">
          <InputText
            id="register-description"
            v-model="data.description"
            :class="['input-style']"
          />
          <label for="register-description">Description</label>
        </FloatLabel>
        <span v-if="v$.description.$error && data.roleType === 'FARMER'" class="error-message">
          Description is required for farmers.
        </span>
      </div>

      <ThemedButton
        @click = "submitForm"
        class="login-btn"
        label="Register"/>
    </form>
    </TabPanel>
    </TabPanels>
    </Tabs>
  </div>
</template>
<script>
import { computed, reactive, ref } from "vue";
import { minLength, required, email, numeric, maxLength, sameAs } from "@vuelidate/validators"
import axiosInstance from "@/utils/axiosInstance";
// eslint-disable-next-line no-unused-vars
import { isLoggedIn } from '@/shared/authState';

import Password from 'primevue/password';
import Tabs from 'primevue/tabs';
import TabList from 'primevue/tablist';
import Tab from 'primevue/tab';
import TabPanels from 'primevue/tabpanels';
import TabPanel from 'primevue/tabpanel';
import useVuelidate from "@vuelidate/core";
import { useRouter } from "vue-router";


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
  data() {
    return{
      loginError: null,
      registerError: null,
    };
  },
  methods: {
    async submitForm() {
      if (this.activeTab === "0") {
        this.loginError = null;
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        await this.v$.email.$validate();
        await this.v$.password.$validate();
        if (!this.v$.email.$error && !this.v$.password.$error) {
          try {
            const response = await axiosInstance.post("/auth/login", {
            email: this.data.email,
            password: this.data.password,
            });
            const { accessToken, refreshToken } = response.data;
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            isLoggedIn.value = true;
            this.router.push('/');
          } catch (error) {
            if (
              error.response &&
              error.response.status === 401
              && error.response.data.detail === 'INVALID_CREDENTIALS'
            ) {
              this.loginError = "Incorrect Username and Password combination";
            } else {
              this.loginError = "An error occurred, please try again later.";
            }
          }
        }

      } else if (this.activeTab === "1") {
        const isValid = await this.v$.$validate();
        if(isValid) {
          try {
            const response = await axiosInstance.post("/auth/register", {
              email: this.data.email,
              password: this.data.password,
              firstName: this.data.firstName,
              lastName: this.data.lastName,
              phoneNumber: this.data.phoneNumber,
              address: this.data.roleType === "FARMER" ? this.data.address : null,
              description: this.data.roleType === "FARMER" ? this.data.description : null,
              roleType: this.data.roleType,
            });

            this.activeTab = "0";
          } catch (error) {
            if (error.response && error.response.status === 409) {
              this.registerError = "Email is already in use";
            } else {
              console.error("Registration error:", error.response?.data || error.message);
            }
          }
        }
      };
    },
  },
  setup () {
    const router = useRouter();
    const roleOptions = [
      { label: "Customer", roleType: "CUSTOMER" },
      { label: "Farmer", roleType: "FARMER" },
      ];
    const defaultRole = "CUSTOMER";
    const activeTab = ref("0");

    const data = reactive({
      email: "",
      password: "",
      confirmPassword: "",
      roleType: defaultRole,
      firstName: "",
      lastName: "",
      phoneNumber: "",
      address: "",
      description: "",
    });

    const rules = computed(() => ({
      email: {
        required,
        email,
      },
      firstName: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(50),
      },
      lastName: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(50),
      },
      phoneNumber: {
        required,
        numeric,
        minLength: minLength(10),
      },
      address: {
        required: data.roleType === "FARMER" ? required : false,
        minLength: minLength(5),
        maxLength: maxLength(100),
      },
      description: {
        required: data.roleType === "FARMER" ? required : false,
        maxLength: maxLength(255),
      },
      password: {
        required,
        minLength: minLength(8),
        maxLength: maxLength(50),
      },
      confirmPassword: {
        required,
        sameAs: sameAs(data.password)
      },
    }));

    const v$ = useVuelidate(rules, data);
    return {
      router,
      activeTab,
      defaultRole,
      roleOptions,
      data,
      v$,
    }
  },
}
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

.error-message {
  color: red;
  font-size: 0.8rem;
}

button.login-btn {
  margin-top: 20px;
  width: 100%;
  padding: 10px;
}
</style>
