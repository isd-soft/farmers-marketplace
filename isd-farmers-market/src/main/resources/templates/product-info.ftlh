<!DOCKTYPE html>
<html>
<body>
<h1>AgriGo!!!!!!!</h1>
<b>Title: </b> ${product.title}
<b>Title: </b> ${product.description}
<hr>
<div>
    <#list product.images as image>
        <img src="data:${image.contentType};base64,${image.base64Image}"
             alt="${image.name}"
             style="max-width: 200px; max-height: 200px; margin: 10px;">
    </#list>

</div>
<h3>Delete product</h3>
<form action="/product/delete/${product.id}" method="post">
    <input type="submit" value = "DELETE">
</form>
</body>
</html>