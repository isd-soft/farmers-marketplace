<template>
  <div class="create-product-container">
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
        class="create-product-input"
      />
        <label for="product-price">Price</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <InputNumber
          id="product-discount"
          v-model="discount"
          class="create-product-input"
        />
        <label for="product-discount">Discount</label>
      </FloatLabel>

      <FloatLabel variant="on">
        <InputNumber
          id="product-quantity"
          v-model="quantity"
          required
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
          <div v-for="(file, index) in selectedFiles" :key="index" class="image-preview">
            <img :src="file.preview" :alt="file.name" />
            <button type="button" @click="removeFile(index)">Remove</button>
          </div>
        </div>

      </div>

      <ThemedButton
        class="create-product-button"
        type="submit"
        label="Create product"/>
    </form>
  </div>
</template>
<script>
import { ref, onMounted  } from "vue";
import axios from 'axios';
import axiosInstance from "@/utils/axiosInstance";
import InputText from "primevue/inputtext";
import FloatLabel from "primevue/floatlabel";
import InputNumber from 'primevue/inputnumber';
import Select from 'primevue/select';
import FileUpload from 'primevue/fileupload';

export default {
  name: "CreateProduct",
  components: {
    InputText,
    FloatLabel,
    InputNumber,
    Select,
    FileUpload
  },
  setup() {
    const title = ref("");
    const description = ref("");
    const price = ref();
    const discount = ref();
    const quantity = ref();
    const unitType = ref();
    const unitTypes = ref([]);
    const category = ref();
    const categories = ref([]);
    const selectedFiles = ref([]);
    const handleFileChange = (event) => {
      const files = event.target.files;
      if (files) {
        const filesArray = Array.from(files);
        if (selectedFiles.value.length + filesArray.length > 5) {
          alert("Maximum of 5 images allowed.");
          return;
        }
        filesArray.forEach((file) => {
          const reader = new FileReader();
          reader.onload = (e) => {
            selectedFiles.value.push({
              file,
              name: file.name,
              preview: e.target.result,
            });
          };
          reader.readAsDataURL(file);
        });
      }
    };
        const removeFile = (index) => {
          selectedFiles.value.splice(index, 1);
        };


    onMounted(async () => {
      try {
        const response = await axios.get('http://localhost:8080/unit-types');
        if (Array.isArray(response.data)) {
          unitTypes.value = response.data.map(type => ({ label: type, value: type }));
        } else {
          console.error('Unexpected response format: expected an array', response.data);
        }
        const categoriesResponse = await axios.get('http://localhost:8080/category');
        if (categoriesResponse.data && categoriesResponse.data.length > 0) {
          categories.value = categoriesResponse.data.map(category => ({
            label: category.title,
            value: category.id,
          }));
        } else {
          console.error('Unexpected response format for categories: expected an array', categoriesResponse.data);
        }
      } catch (error) {
        console.error('Error loading unit types:', error);
      }

    });

    const handleNewProduct = async () => {
      try {
        const imagesBase64 = await Promise.all(
          selectedFiles.value.map(file => convertFileToBase64(file))
        );
        console.log("Base64 images:", imagesBase64);
        debugger;
        const response = await axiosInstance.post("/product/create", {
          title: title.value,
          description: description.value,
          unitType: unitType.value,
          pricePerUnit: price.value,
          discountPercents: discount.value,
          quantity: quantity.value,
          categoryId: category.value,
          images: imagesBase64,
        });

        console.log("Created product:", response.data);
      } catch (error) {
        console.error("Creating new product error:", error.response?.data || error.message);
        alert("Creating new product failed: " + (error.response?.data?.message || error.message));
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
      discount,
      quantity,
      selectedFiles,
      handleFileChange,
      removeFile,
      handleNewProduct,
    };
  },
}
function convertFileToBase64(file) {
    const reader = new FileReader();
    const blob = new Blob([file], {type: file.type});

    reader.onload = function () {
      console.log(reader.result);
    };

    reader.onerror = function (error) {
      console.log('Error: ', error);
    };

    reader.readAsDataURL(blob);
}
</script>
<style scoped>
.create-product-container {
  margin-left: 20px;
  margin-right: 20px;
  margin-top: 70px;
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
</style>
