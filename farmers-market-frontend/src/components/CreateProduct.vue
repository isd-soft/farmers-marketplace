<template>
  <div class="home">
  <Header class = "navbar"></Header>
  <div class="create-product-container">
    <h1 class="create-product-main-text">Create new product</h1>
    <form @submit="handleNewProduct" class="create-product-form">
      <FloatLabel variant="on">
        <InputText
          id="product-title"
          v-model="product.title"
          class="create-product-input"
        />
        <label for="product-title">Title</label>
      </FloatLabel>
      <span v-if="v$.title.$error" class="error-message">
        Product title is required and must be between 1 and 80 characters</span>

      <FloatLabel variant="on">
        <InputText
          id="product-desc"
          v-model="product.description"
          class="create-product-input"
        />
        <label for="product-desc">Description</label>
      </FloatLabel>
      <span v-if="v$.description.$error" class="error-message">
        Product description is required and must be between 1 and 1000 characters</span>

      <FloatLabel variant="on">
        <Select
          id="product-category"
          v-model="product.categoryId"
          :options="categories"
          optionLabel="label"
          optionValue="value"
          class="create-product-input"
        />
        <label for="product-category">Category</label>
      </FloatLabel>
      <span v-if="v$.categoryId.$error" class="error-message">
        Product category is required</span>

      <FloatLabel variant="on">
        <Select
          v-model="product.unitType"
          :options="unitTypes"
          optionLabel="label"
          option-value="value"
          class="create-product-input"
        />
        <label for="product-unit-type">Unit Type</label>
      </FloatLabel>
      <span v-if="v$.unitType.$error" class="error-message">
        Product unit type is required</span>

      <FloatLabel variant="on">
        <InputNumber
          id="product-price"
          v-model="product.pricePerUnit"
          class="create-product-input"
        />
        <label for="product-price">Price</label>
      </FloatLabel>
      <span v-if="v$.pricePerUnit.$error" class="error-message">
        Product price is required and must be minimum 1</span>

      <FloatLabel variant="on">
        <InputNumber
          id="product-quantity"
          v-model="product.quantity"
          class="create-product-input"
        />
        <label for="product-price">Quantity</label>
      </FloatLabel>
      <span v-if="v$.quantity.$error" class="error-message">
        Product quantity is required and must be minimum 0</span>

      <div>
        <label for="file-uploader" class="file-uploader">
          <input
            id="file-uploader"
            type="file"
            accept="image/*"
            multiple
            @change="handleFileChange"
            :disabled="selectedFiles.length >= 5"
          />
          <span v-if="selectedFiles.length < 5">Upload Images (max 5)</span>
          <span v-else>Maximum images reached</span>
        </label>

        <div class="image-preview-container">
          <div
            v-for="(file, index) in selectedFiles"
            :key="index"
            class="image-preview"
          >
            <img :src="file.preview" :alt="file.name" />
            <button type="button" @click="removeFile(index)">Remove</button>
          </div>
        </div>
      </div>

      <ThemedButton
        class="create-product-button"
        type="submit"
        label="Create product"
      />
    </form>
    <Dialog
      v-model:visible="showDeliveryDialog"
      style="flex-grow: 1; max-width: 500px;"
      :position="'top'"
      header="Missing Delivery Types"
      modal
      :closable="false"
    >
      <p>You must add all possible delivery types before publishing a product.</p>
        <Button
          class="popup-actions-button"
          @click="navigateToSettings"
        >
          Go to Settings
        </Button>
    </Dialog>
  </div>
  <Footer class = "footer"></Footer>
  </div>
</template>

<script>
import {ref, onMounted, computed, reactive} from "vue";
import useVuelidate from "@vuelidate/core";
import { minLength, required, maxLength, minValue} from "@vuelidate/validators"
import Header from "./Header.vue";
import Footer from "../components/Footer.vue";
import axios from "axios";
import axiosInstance from "@/utils/axiosInstance";
import InputText from "primevue/inputtext";
import FloatLabel from "primevue/floatlabel";
import InputNumber from "primevue/inputnumber";
import Select from "primevue/select";
import router from "@/router/index.js";
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';

export default {
  name: "CreateProduct",
  components: {
    InputText,
    FloatLabel,
    InputNumber,
    Select,
    Header,
    Footer,
    Dialog,
    Button,
  },
  setup() {
    const product = reactive({
      title: "",
      description: "",
      unitType: null,
      pricePerUnit: null,
      quantity: null,
      categoryId: null,
    });
    const selectedFiles = ref([]);
    const unitTypes = ref([]);
    const categories = ref([]);
    const showDeliveryDialog = ref(false);
    let currentUser = null;

    const rules = computed(() => ({
      title: { required, minLength: minLength(1), maxLength: maxLength(80) },
      description: { required, minLength: minLength(1), maxLength: maxLength(1000) },
      unitType: { required },
      pricePerUnit: { required, minValue: minValue(1) },
      quantity: { required, minValue: minValue(0) },
      categoryId: { required },
    }));

    const v$ = useVuelidate(rules, product);

    const handleFileChange = async (event) => {
      const files = event.target.files;
      if (!files) return;

      const filesArray = Array.from(files);
      if (selectedFiles.value.length + filesArray.length > 5) {
        alert("Maximum of 5 images allowed.");
        return;
      }

      for (const file of filesArray) {
        try {
          const base64 = await convertFileToBase64(file);
          selectedFiles.value.push({
            file,
            name: file.name,
            preview: base64,
            base64,
          });
          console.log(`File ${file.name} converted to Base64 successfully.`);
        } catch (error) {
          console.error(`Error converting file ${file.name} to Base64:`, error);
        }
      }
    };

    const removeFile = (index) => {
      console.log(`Removing file at index ${index}`);
      selectedFiles.value.splice(index, 1);
    };

    const convertFileToBase64 = (file) => {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => resolve(e.target.result);
        reader.onerror = (e) => reject(e);
        reader.readAsDataURL(file);
      });
    };

    onMounted(async () => {
      try {
        const response = await axiosInstance.get(`/current-user/`);
        currentUser = response.data;
        if (!currentUser.isFarmer) {
          await router.push(`/`);
        }
      } catch (error) {
        await router.push(`/`);
      }
      try {
        const response = await axiosInstance.get("/unit-types");
        if (Array.isArray(response.data)) {
          unitTypes.value = response.data.map((type) => ({
            label: type,
            value: type,
          }));
          console.log("Unit types loaded:", unitTypes.value);
        } else {
          console.error("Unexpected unit types format:", response.data);
        }

        console.log("Fetching categories...");
        const categoriesResponse = await axiosInstance.get("/category");
        if (categoriesResponse.data && categoriesResponse.data.length > 0) {
          categories.value = categoriesResponse.data.map((category) => ({
            label: category.title,
            value: category.id,
          }));
          console.log("Categories loaded:", categories.value);
        } else {
          console.error("Unexpected categories format:", categoriesResponse.data);
        }
      } catch (error) {
      }

      try {
        console.log("Fetching user delivery types...");
        const response = await axiosInstance.get("/deliverytypes/current-farmer");
        const userDeliveryTypes = response.data;

        console.log("Fetching all delivery types...");
        const deliveryTypesResponse = await axiosInstance.get("/delivery-types-enum");
        const allDeliveryTypes = deliveryTypesResponse.data;

        const userHasAllDeliveryTypes = allDeliveryTypes.every((type) =>
          userDeliveryTypes.includes(type)
        );

        if (!userHasAllDeliveryTypes) {
          console.warn("User is missing delivery types. Showing dialog...");
          showDeliveryDialog.value = true;

        }
      } catch (error) {
        console.error("Error checking delivery types:", error.response?.data || error.message);
        await router.push("/");
      }

      });
    const handleNewProduct = async (event) => {
      event.preventDefault();
      await v$.value.$validate();
      if(!v$.value.$error) {
        try {
          console.log("Preparing images for submission...");
          const imagesBase64 = selectedFiles.value.map((file) => file.base64);
          console.log("Images Base64:", imagesBase64);

          console.log("Submitting product data...");
          const response = await axiosInstance.post("/product/create", {
            imagesBase64: imagesBase64,
            ...product,
          });

          console.log("Product created successfully:", response.data);
          const createdProductId = response.data.id;
          await router.push(`/product/${createdProductId}`);
        } catch (error) {
          console.error("Error creating product:", error.response?.data || error.message);
          alert(
            "Creating new product failed: " +
            (error.response?.data?.message || error.message)
          );
        }
      }
    };

    return {
      product,
      categories,
      unitTypes,
      handleFileChange,
      removeFile,
      handleNewProduct,
      v$,
      selectedFiles,
      showDeliveryDialog,
    };
  },
  methods: {
    navigateToSettings() {
      this.$router.push(`/settings`);
    },
  },
};
</script>

<style scoped>
body{
  display: block !important;
}
.home{
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 120px;
  align-items: center;
}
.popup-actions-button{
  margin-top: 20px;
  margin-left: auto;
  margin-right: auto;
  max-width: 150px;
  flex-grow: 1;
}
.create-product-container {
  width: 80%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  row-gap: 12px;
}
@media (max-width: 380px) {
  .create-product-container{
    width: 90%;
  }
}
.create-product-form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  row-gap: 20px;
}
.create-product-input{
  width:100%;
}
.create-product-button{
  width: 200px;
  margin-left: auto;
  margin-right: auto;
}
.file-uploader {
  cursor: pointer;
  display: inline-block;
  margin-bottom: 10px;
}

.file-uploader input[type="file"] {
  display: none;
}

.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.image-preview {
  position: relative;
  width: 100px;
  height: 100px;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 5px;
}

.image-preview button {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  border-radius: 50%;
  padding: 2px 5px;
  cursor: pointer;
}
.footer{
  text-align: center;
  padding: 10px;
  margin-top: 50px;
  bottom: 0;
}
.create-product-main-text{
text-align: center;
}
.error-message {
  color: red;
  font-size: 0.9rem;
}
</style>
