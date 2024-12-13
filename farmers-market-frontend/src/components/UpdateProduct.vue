<template>
  <div class="home">
    <Header class = "navbar"></Header>
    <div class="create-product-container">
      <h1 class="create-product-main-text">Edit product</h1>
      <form @submit="handleEditProduct" class="create-product-form">
        <h1 class="create-product-main-text">{{ product.title }}</h1>
        <FloatLabel variant="on">
          <InputText
            id="product-desc"
            v-model="productToSave.description"
            required
            class="create-product-input"
          />
          <label for="product-desc">Description</label>
        </FloatLabel>
        <span v-if="v$.description.$error" class="error-message">
        Product description is required and must be between 1 and 1000 characters</span>
        <FloatLabel variant="on">
          <Select
            v-model="productToSave.unitType"
            :options="unitTypes"
            optionLabel="label"
            option-value="value"
            required
            class="create-product-input"
          />
          <label for="product-unit-type">Unit Type</label>
        </FloatLabel>
        <span v-if="v$.unitType.$error" class="error-message">
        Product unit type is required</span>

        <FloatLabel variant="on">
          <InputNumber
            id="product-price"
            v-model="productToSave.pricePerUnit"
            required
            min="1"
            class="create-product-input"
          />
          <label for="product-price">Price</label>
        </FloatLabel>
        <span v-if="v$.pricePerUnit.$error" class="error-message">
        Product price is required and must be minimum 1</span>

        <FloatLabel variant="on">
          <InputNumber
            id="product-quantity"
            v-model="productToSave.quantity"
            required
            min="0"
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
            <span class="p-button" v-if="selectedFiles.length < 5">Upload Images (max 5)</span>
            <span class="p-button" v-if="selectedFiles.length >= 5" style="opacity: 60%">Maximum images reached</span>
          </label>

          <div class="image-preview-container">
            <div
              v-for="(file, index) in selectedFiles"
              :key="index"
              class="image-preview"
            >
              <img :src="file.preview" :alt="file.name" />
              <button type="button" @click="removeFile(index)"><i class="pi pi-trash action-icon"></i></button>
            </div>
          </div>
        </div>

        <ThemedButton
          class="create-product-button"
          type="submit"
          label="Save"
        />
      </form>
    </div>
    <Footer class = "footer"></Footer>
  </div>
  <Toast />
</template>
<script>
import { ref, onMounted, computed, reactive } from "vue";
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
import ToastService from "primevue/toastservice";
import { useToast } from "primevue/usetoast";
import imageCompression from "browser-image-compression";

export default {
  name: "EditProduct",
  components: {
    InputText,
    FloatLabel,
    InputNumber,
    Select,
    Header,
    Footer,
    ToastService,
  },
  props: ['id'],
  setup(props) {
    const product = ref({})
    const unitTypes = ref([]);
    const selectedFiles = ref([]);

    const productToSave = reactive({
      description: "",
      unitType: null,
      pricePerUnit: null,
      quantity: null,
    });
    const toast = useToast();
    const rules = computed(() => ({
      description: { required, minLength: minLength(1), maxLength: maxLength(1000) },
      unitType: { required },
      pricePerUnit: { required, minValue: minValue(1) },
      quantity: { required, minValue: minValue(0) },
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
        if (file.size > 10 * 1024 * 1024) {
          toast.add({
            severity: "error",
            summary: "File too large",
            detail: "Image must be less than 10 MB",
            life: 3000,
          });
          continue;
        }
        try {
          const compressedFile = await compressImage(file);
          const base64 = await convertFileToBase64(compressedFile);

          selectedFiles.value.push({
            file: compressedFile,
            name: compressedFile.name,
            preview: base64,
            base64,
          });
          console.log(`File ${compressedFile.name} processed successfully.`);
        } catch (error) {
          console.error(`Error processing file ${file.name}:`, error);
        }
      }
    };

    const removeFile = (index) => {
      console.log(`Removing file at index ${index}`);
      selectedFiles.value.splice(index, 1);
    };
    const compressImage = async (file) => {
      const options = {
        maxSizeMB: 1,
        maxWidthOrHeight: 1920,
        useWebWorker: true,
      };
      return await imageCompression(file, options);
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
        console.log(props.id)
        const response = await axiosInstance.get(`/product/${props.id}`)
        console.log(response)
        product.value = response.data;

        productToSave.description = product.value.description;
        productToSave.pricePerUnit = product.value.pricePerUnit;
        productToSave.quantity = product.value.quantity;
        productToSave.unitType = product.value.unitType;

        if (product.value.images && Array.isArray(product.value.images)) {
          selectedFiles.value = product.value.images.map((image) => ({
            base64: `data:image/jpeg;base64,${image.bytes}`,
            preview: `data:image/jpeg;base64,${image.bytes}`,
            name: image.name || "Existing Image",
          }));
        }
      } catch (error) {
        console.error('Failed to load product:', error.message)
      }
      try {
        const response = await axiosInstance.get(`/current-user/`);
        const currentUser = response.data;
        if (!currentUser || !product || currentUser.id !== product.value.farmer.id) {
          console.log('Redirecting: user is not the owner of the product');
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
      } catch (error) {
      }
    });

    const handleEditProduct = async (event) => {
      event.preventDefault();
      await v$.value.$validate();
        if(!v$.value.$error) {
          try {
            console.log("Preparing images for submission...");
            const imagesBase64 = selectedFiles.value.map((file) => file.base64);
            console.log("Images Base64:", imagesBase64);

            console.log("Submitting product data...");
            const response = await axiosInstance.put(`/product/update/${props.id}`, {
              imagesBase64: imagesBase64,
              ...productToSave,
            });

            console.log("Product updated successfully:", response.data);
            const createdProductId = response.data.id;
            await router.push(`/product/${createdProductId}`);
          } catch (error) {
            console.error("Error updating product:", error.response?.data || error.message);
            alert(
              "Updating product failed: " +
              (error.response?.data?.message || error.message)
            );
          }
        }
    };

    return {
      product,
      productToSave,
      unitTypes,
      selectedFiles,
      handleFileChange,
      removeFile,
      handleEditProduct,
      v$,
    };
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
.create-product-container {
  width: 80%;
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  min-height: 80vh;
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
</style>
