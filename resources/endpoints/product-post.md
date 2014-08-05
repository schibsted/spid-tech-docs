:introduction

Create a new product.

**NB!** When using this API to create subscriptions (`type` is `2`), the
`subscriptionPeriod` parameter is **required**.

A product can not be both a bundle and a subscription. Bundles have to be
regular products.

:relevant-endpoints

GET /product/{id}
