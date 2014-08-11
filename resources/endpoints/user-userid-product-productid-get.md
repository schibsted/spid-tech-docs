:introduction

Check if a specific user has access to a product. Since version 2.9, the product
id may be either the actual product ID, or an alias. When an alias is used, the
`merchant` filter must also be used.

Users gain access to products either through purchase or by receiving them as
gifts.

:relevant-endpoints

GET /user/{userId}/products
DELETE /user/{userId}/product/{productId}
POST /user/{userId}/product/{productId}
