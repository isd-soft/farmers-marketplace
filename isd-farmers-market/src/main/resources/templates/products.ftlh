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
<form action="/product/create" method="post">
    Title <input type = "text" name = "title"><br><br>
    Description <input type = "text" name = "description"><br><br>
    Unit Type:
    <select name="unitType">
        <#list unitTypes as unitType>
            <option value="${unitType}">${unitType}</option>
        </#list>
    </select>
<#--    <br><br>-->
<#--    Category:-->
<#--    <select name="categoryId">-->
<#--        <#list categories as category>-->
<#--            <option value="${category.id}">${category.name}</option>-->
<#--        </#list>-->
<#--    </select>-->
    <br><br>
    Price <input type = "number" name = "pricePerUnit"><br><br>
    Quantity <input type = "number" name = "quantity"><br><br>
    <input type="submit" value = "ADD">
</form>
</body>
</html>