:introduction

Revoke a user's access to a product. **NB!** While using `GET` with this
endpoint will also provide information about access to subscriptions, `DELETE`
can only be used to revoke access to products, not subscriptions.

:relevant-endpoints

GET /user/{userId}/products
GET /user/{userId}/product/{productId}
POST /user/{userId}/product/{productId}
