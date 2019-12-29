<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Sign Up</title>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<form method="post">
    <input type="text" placeholder="Name" name="name">
    <input type="text" placeholder="Price" name="price">
    <input type="submit" placeholder="Add">
</form>
<#list products as product>
<div>
    <div>Артикул&ensp;&ensp;&ensp;Бренд&ensp;&ensp;&ensp;Цена</div>
    <div>&ensp;&ensp;&ensp;${product.id}&ensp;&ensp;&ensp;&ensp;&ensp;${product.name}&ensp;&ensp;&ensp;&ensp;${product.price}₽</div>
    <hr>
</div>
</#list>
</body>
</html>