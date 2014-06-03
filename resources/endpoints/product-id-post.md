:introduction

Update the product by `POST`-ing the fields to edit. Refer to
[`GET /api/2/product/{id}`](/endpoints/GET/product/{id}) for details on the
supported query parameters. Updating a product transparently creates a new
revision. Previous revisions can be accessed through the
[product revisions endpoint](/endpoints/GET/product/{productId}/revisions).

:relevant-endpoints

GET /product/{id}
GET /product/{productId}/revisions
