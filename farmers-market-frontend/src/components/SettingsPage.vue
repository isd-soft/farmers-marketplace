<template>
  <div class="settings-page">
    <Header class="navbar"></Header>
    <main class="main-content">
      <div>
        <h1 class="p-card-title">User Settings</h1>
      </div>

      <!-- Password (Edit Mode) -->
      <Card class="custom-card">
        <template #content>
          <div v-if="!isLoading">
            <div v-if="editProfile">
              <div class="profile">
                <div class="profile-field">
                  <label for="firstName" class="field-name">First Name:</label>
                  <InputText v-model="user.firstName" id="firstName" class="field-value" size="small" />
                  <span v-if="v$.firstName.$error" class="error-message"> First name is required and must be between 1
                    and 50 characters</span>
                  <label for="lastName" class="field-name">Last Name:</label>
                  <InputText v-model="user.lastName" id="lastName" class="field-value" size="small" />
                  <span v-if="v$.lastName.$error" class="error-message"> Last name is required and must be between 1 and
                    50 characters</span>
                </div>
                <div class="profile-field">
                  <label for="email" class="field-name">Email:</label>
                  <InputText v-model="user.email" id="email" class="field-value" size="small" />
                  <span v-if="v$.email.$error" class="error-message">Email is required and must be in a valid email
                    format</span>
                </div>
                <div class="profile-field">
                  <label for="phoneNumber" class="field-name">Phone Number:</label>
                  <InputText v-model="user.phoneNumber" id="phoneNumber" class="field-value" size="small" />
                  <span v-if="v$.phoneNumber.$error" class="error-message"> Phone number is required and must contain at
                    least 10 digits</span>
                </div>

                <!-- show if user is a farmer -->
                <div v-if="user.isFarmer" class="profile-field">
                  <label for="description" class="field-name">Description:</label>
                  <InputText v-model="user.description" id="description" class="field-value" size="small" />
                  <span v-if="v$.description.$error" class="error-message">Description must be less than 255
                    characters</span>
                </div>

                <div v-if="user.isFarmer" class="profile-field">
                  <label for="address" class="field-name">Address:</label>
                  <InputText v-model="user.address" id="address" class="field-value" size="small" />
                  <span v-if="v$.address.$error" class="error-message">Address must be between 5 and 100
                    characters</span>
                </div>
              </div>
            </div>
            <div v-else>

              <!-- Profile (View Mode) -->
              <div class="profile-field">
                <span class="field-name">Full Name:</span>
                <span class="field-value">{{ user.firstName }} {{ user.lastName }}</span>
              </div>
              <div class="profile-field">
                <span class="field-name">Email:</span>
                <span class="field-value">{{ user.email }}</span>
              </div>
              <div class="profile-field">
                <span class="field-name">Phone Number:</span>
                <span class="field-value">{{ user.phoneNumber }}</span>
              </div>

              <!-- show if user is a farmer -->
              <div v-if="isFarmer" class="profile-field">
                <span class="field-name">Description:</span>
                <span class="field-value">{{ user.description }}</span>
              </div>

              <div v-if="isFarmer" class="profile-field">
                <span class="field-name">Address:</span>
                <span class="field-value">{{ user.address }}</span>
              </div>
            </div>
          </div>
          <div v-else>Loading...</div>
        </template>
        <template #footer>
          <Button v-if="!editProfile" label="Edit Profile" icon="pi pi-pencil" @click="toggleEditProfile"></Button>
          <div class="button">
            <Button v-if="editProfile" label="Save Changes" icon="pi pi-save" @click="submitForm"
              class="save-button"></Button>
          </div>
        </template>
      </Card>

      <div>
        <h1 class="p-card-title">Password</h1>
      </div>

      <!-- Password (View Mode) -->
      <Card v-if="!editPassword" class="custom-card">
        <template #content>
          <div class="password-info">
            <div class="profile-field">
              <span class="field-name">Password:</span>
              <span class="field-value">********</span>
            </div>
            <div class="hint">
              For security reasons, your password is hidden.
            </div>
          </div>
        </template>
        <template #footer>
          <Button label="Change Password" icon="pi pi-key" @click="editPassword = !editPassword"></Button>
        </template>
      </Card>

      <!-- Password (Edit Mode) -->
      <Card v-if="editPassword" class="custom-card">
        <template #content>
          <div class="password-change-form">
            <div class="profile-field">
              <label for="oldPassword" class="field-name">Old Password:</label>
              <InputText v-model="passwordData.oldPassword" id="oldPassword" type="password" class="field-value"
                size="small" />
              <span v-if="vPassword$.oldPassword.$error" class="error-message">Old password is required</span>
            </div>
            <div class="profile-field">
              <label for="newPassword" class="field-name">New Password:</label>
              <InputText v-model="passwordData.newPassword" id="newPassword" type="password" class="field-value"
                size="small" />
              <span v-if="vPassword$.newPassword.$error" class="error-message">New password is required, must be at
                least 8 characters</span>
            </div>
            <div class="profile-field">
              <label for="confirmPassword" class="field-name">Confirm New Password:</label>
              <InputText v-model="passwordData.confirmPassword" id="confirmPassword" type="password" class="field-value"
                size="small" />
              <span v-if="vPassword$.confirmPassword.$error" class="error-message">Confirmation password is required and
                must match the new password</span>
            </div>
          </div>
        </template>
        <template #footer>
          <Button label="Save Changes" icon="pi pi-save" @click="savePassword" class="save-button"></Button>
        </template>
      </Card>
    </main>
    <Footer class="footer"></Footer>
  </div>
</template>

<script>
import Card from "primevue/card";
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import Header from "./Header.vue";
import Footer from "../components/Footer.vue";
import axiosInstance from "@/utils/axiosInstance";
import { getUserId } from "@/utils/axiosInstance";
import useVuelidate from "@vuelidate/core";
import { minLength, required, email, numeric, maxLength, sameAs } from "@vuelidate/validators"
import { reactive, computed } from "vue";

export default {
  name: "SettingsPage",
  components: {
    Header,
    Footer,
    Button,
    Card,
    InputText,
  },

  setup() {
    const user = reactive({
      firstName: "",
      lastName: "",
      email: "",
      phoneNumber: "",
      description: "",
      address: "",
      isFarmer: false,
    });

    const passwordData = reactive({
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    });

    const rules = computed(() => ({
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
      email: {
        required,
        email,
      },
      phoneNumber: {
        required,
        numeric,
        minLength: minLength(10),
      },
      address: {
        minLength: minLength(5),
        maxLength: maxLength(100),
      },
      description: {
        maxLength: maxLength(255),
      },
    }));

    const passwordRules = computed(() => ({
      oldPassword: {
        required,
      },
      newPassword: {
        required,
        minLength: minLength(8),
        maxLength: maxLength(50),
      },
      confirmPassword: {
        required,
        sameAs: sameAs(passwordData.newPassword)
      },
    }));

    const v$ = useVuelidate(rules, user);
    const vPassword$ = useVuelidate(passwordRules, passwordData);

    return {
      user,
      passwordData,
      v$,
      vPassword$,
    };
  },

  data() {
    return {
      isLoading: true,
      editProfile: false,
      editPassword: false,
      userId: null,
    };
  },

  created() {
    this.fetchUserData();
  },

  methods: {
    toggleEditProfile() {
      if (!this.editProfile) {
        Object.assign(this.user, { ...this.user });
        this.v$.$reset();
      }
      this.editProfile = !this.editProfile;
    },

    async fetchUserData() {
      this.isLoading = true;
      this.userId = getUserId();
      if (!this.userId) {
        console.error("Unable to fetch user data: User ID is null");
        this.isLoading = false;
        return;
      }
      try {
        const response = await axiosInstance.get(`/users/${this.userId}`);
        Object.assign(this.user, response.data);
        this.isFarmer = response.data.isFarmer;
      } catch (error) {
        console.error("Error fetching user data:", error.response?.data || error.message);
      } finally {
        this.isLoading = false;
      }
    },

    async submitForm() {
      console.log("Validating form...");
      await this.v$.$validate();
      console.log("Validation result:", this.v$.$error ? "Failed" : "Passed");

      if (!this.v$.$error) {
        try {
          console.log("Calling saveProfile...");
          await this.saveProfile();
          this.editProfile = false;
          this.v$.$reset();
        } catch (error) {
          console.error("Error saving profile:", error);
        }
      }
    },

    async saveProfile() {
      if (!this.userId) return;
      this.isLoading = true;

      const updatedProfile = { ...this.user };

      console.log('Updated profile:', updatedProfile);
      try {
        const response = await axiosInstance.put(`/users`, updatedProfile);
        Object.assign(this.user, { ...this.user });
        console.log(response.data);
      } catch (error) {
        console.error("Error updating user data:", error.response?.data || error.message);
      } finally {
        this.isLoading = false;
      }
    },

    async savePassword() {
      await this.vPassword$.$validate();
      if (this.vPassword$.$error) {
        return;
      }

      const updatedPassword = {
        id: this.userId,
        oldPassword: this.passwordData.oldPassword,
        password: this.passwordData.newPassword,
      };

      try {
        const response = await axiosInstance.put('/auth/password', updatedPassword);
        this.passwordData.oldPassword = "";
        this.passwordData.newPassword = "";
        this.passwordData.confirmPassword = "";
        this.vPassword$.$reset();
        this.editPassword = false;
        console.log(response.data);
        alert("Password updated successfully!");
      } catch (error) {
        if (error.response?.data?.message) {
          console.error("Error updating password:", error.response.data.message);
          alert(error.response.data.message);
        } else {
          console.error("Error updating password:", error.message);
          alert("An unexpected error occurred. Please try again.");
        }
      }
    },
  },
};
</script>

<style scoped>
.error-message {
  color: red;
  font-size: 0.9rem;
}

.settings-page {
  font-family: var(--font-family, 'Nunito', 'Helvetica', Arial, sans-serif);
  color: var(--text-color, #495057);
  min-height: 100vh;
  overflow-x: hidden;
  overflow-y: auto;
  width: 100%;
  padding-top: 200px;
  justify-content: center;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.profile {
  display: flex;
  flex-direction: column;
  gap: 1.5vh;
}

.password-change-form {
  display: flex;
  flex-direction: column;
  gap: 1.5vh;
}

.p-card-title {
  font-size: 2rem;
  font-weight: bold;
  margin: 0;
}

.p-card-subtitle {
  font-size: 1.25rem;
  font-weight: normal;
  margin: 0;
  color: var(--text-secondary-color, #6c757d);
}

.custom-card {
  width: 800px;
  padding: 20px;
  margin: 20px auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  font-family: inherit;
}

.profile-info {
  font-size: 1rem;
}

.profile-field {
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.field-name {
  font-weight: bold;
}

.button {
  padding-top: 5px;
}

.footer {
  text-align: center;
  padding: 10px;
  bottom: 0;
  margin-top: auto;
}
</style>
