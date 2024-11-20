<!DOCKTYPE html>
<html>
<body>
<h1>AgriGo!!!!!!!</h1>
<#list products as product>
    <div>
        <p><b>${product.title}</b>${product.pricePerUnit} <a href = "/product/${product.id}"> More info</a></p>
    </div>
</#list>
<hr>
<h3>Create product</h3>
<form action="/product/create" method="post" enctype="multipart/form-data">
    Title <input type="text" name="title"><br><br>
    Description <input type="text" name="description"><br><br>
    Unit Type:
    <select name="unitType">
        <#list unitTypes as unitType>
            <option value="${unitType}">${unitType}</option>
        </#list>
    </select><br><br>

    Price <input type="number" name="pricePerUnit"><br><br>
    Quantity <input type="number" name="quantity"><br><br>
    Discount <input type="number" name="discountPercents"><br><br>

    <label for="file-input">Add Photos:</label>

    <input type="file" id="file-input" name="newImages" multiple accept="image/*"><br><br>

    <div id="image-preview"></div>

    <input type="submit" value="ADD">
</form>

<script>
    const fileInput = document.getElementById("file-input");
    const imagePreview = document.getElementById("image-preview");

    fileInput.addEventListener("change", () => {
        const files = fileInput.files;

        Array.from(files).forEach((file) => {
            if (file.type.startsWith("image/")) {
                const reader = new FileReader();

                reader.onload = (event) => {
                    const container = document.createElement("div");
                    container.classList.add("image-container");

                    const img = document.createElement("img");
                    img.src = event.target.result;
                    img.alt = file.name;

                    const deleteButton = document.createElement("button");
                    deleteButton.textContent = "X";
                    deleteButton.addEventListener("click", () => {
                        container.remove();
                    });

                    container.appendChild(img);
                    container.appendChild(deleteButton);
                    imagePreview.appendChild(container);
                };

                reader.readAsDataURL(file);
            }
        });
    });
</script>
<style>
    #image-preview {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        margin-top: 10px;
    }
    .image-container {
        position: relative;
        display: inline-block;
    }
    .image-container img {
        max-width: 150px;
        max-height: 150px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }
    .image-container button {
        position: absolute;
        top: 5px;
        right: 5px;
        background-color: red;
        color: white;
        border: none;
        border-radius: 50%;
        cursor: pointer;
    }
</style>
</body>
</html>