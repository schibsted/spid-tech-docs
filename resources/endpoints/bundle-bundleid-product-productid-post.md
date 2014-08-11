:introduction

Add a product to a bundle. Adding a product to a bundle creates a bundle item.
Price, VAT and sorting order are properties of this object (i.e., not the
product itself - the product is not affected in any way by this operation). If
the product has already been added to the bundle, another `POST` will update it.

:relevant-types bundle-type

:relevant-endpoints

POST /product
DELETE /bundle/{bundleId}/product/{productId}
