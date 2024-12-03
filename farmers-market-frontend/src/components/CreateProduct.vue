<template>
  <div class="home">
  <Header class = "navbar"></Header>
  <div class="create-product-container">
    <h1 class="create-product-main-text">Create new product</h1>
    <form @submit="handleNewProduct" class="create-product-form">
      <FloatLabel variant="on">
        <InputText
          id="product-title"
          v-model="title"
          required
          class="create-product-input"
        />
        <label for="product-title">Title</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <InputText
          id="product-desc"
          v-model="description"
          required
          class="create-product-input"
        />
        <label for="product-desc">Description</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <Select
          id="product-category"
          v-if="categories.length > 0"
          v-model="category"
          :options="categories"
          optionLabel="label"
          optionValue="value"
          required
          class="create-product-input"
        />
        <label for="product-category">Category</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <Select
          v-model="unitType"
          :options="unitTypes"
          optionLabel="label"
          option-value="value"
          required
          class="create-product-input"
        />
        <label for="product-unit-type">Unit Type</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <InputNumber
          id="product-price"
          v-model="price"
          required
          min="1"
          class="create-product-input"
        />
        <label for="product-price">Price</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <InputNumber
          id="product-quantity"
          v-model="quantity"
          required
          min="0"
          class="create-product-input"
        />
        <label for="product-price">Quantity</label>
      </FloatLabel>

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
  </div>
  <Footer class = "footer"></Footer>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { required, maxLength, minLength, minValue, maxValue } from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";
import Header from "./Header.vue";
import Footer from "../components/Footer.vue";
import axios from "axios";
import axiosInstance from "@/utils/axiosInstance";
import InputText from "primevue/inputtext";
import FloatLabel from "primevue/floatlabel";
import InputNumber from "primevue/inputnumber";
import Select from "primevue/select";
import router from "@/router/index.js";

export default {
  name: "CreateProduct",
  components: {
    InputText,
    FloatLabel,
    InputNumber,
    Select,
    Header,
    Footer
  },
  setup() {
    const title = ref("");
    const description = ref("");
    const price = ref();
    const quantity = ref();
    const unitType = ref();
    const unitTypes = ref([]);
    const category = ref();
    const categories = ref([]);
    const selectedFiles = ref([]);

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
        reader.readAsDataURL(file); // Convert file to Base64
      });
    };

    onMounted(async () => {
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
        console.error("Error loading unit types or categories:", error);
      }
    });

    const handleNewProduct = async (event) => {
      event.preventDefault();
      try {
        console.log("Preparing images for submission...");
        const imagesBase64 = selectedFiles.value.map((file) => file.base64);
        console.log("Images Base64:", imagesBase64);

        console.log("Submitting product data...");
        const response = await axiosInstance.post("/product/create", {
          title: title.value,
          description: description.value,
          unitType: unitType.value,
          pricePerUnit: price.value,
          quantity: quantity.value,
          categoryId: category.value,
          imagesBase64: imagesBase64,
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
    };

    return {
      title,
      description,
      category,
      categories,
      unitType,
      unitTypes,
      price,
      quantity,
      selectedFiles,
      handleFileChange,
      removeFile,
      handleNewProduct,
      v$: useVuelidate(),

    };
  },
};
</script>

<style scoped>
.create-product-container {
  margin-left: 20px;
  margin-right: 20px;
  width: 900px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  row-gap: 12px;
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
.home{
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
  width: 100%;
  padding-top: 100px;
  justify-content: space-between;
  align-items: center;
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
