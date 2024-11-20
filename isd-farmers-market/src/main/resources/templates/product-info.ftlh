<!DOCKTYPE html>
<html>
<body>
<h1>AgriGo!!!!!!!</h1>
<b>Title: </b> ${product.title}
<b>Title: </b> ${product.description}
<hr>
<h3>Delete product</h3>
<form action="/product/delete/${product.id}" method="post">
    <input type="submit" value = "DELETE">
</form>
</body>
</html>