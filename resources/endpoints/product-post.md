:introduction

Create a new product, a subscription or a bundle. All of these are technically
products, so which one you end up depends on the data you pass when creating it.

## Subscriptions

To create a subscription, set the `type` parameter to `2`. You must also specify
the `subscriptionPeriod`, which is required for subscriptions. Additionally, you
may provide any or all of the optional parameters:

- `subscriptionRenewPrice`
- `subscriptionRenewPeriod`
- `subscriptionAutoRenew`
- `subscriptionAutoRenewLockPeriod`
- `subscriptionAutoRenewDisabled`
- `subscriptionGracePeriod`
- `subscriptionEmailReceiptLimit`
- `subscriptionFinalEndDate`
- `subscriptionSurveyUrl`

The `bundle` parameter may not be used with subscriptions.

## Bundles

Set the [`bundle` parameter](/types/bundle/) to `1` or `2` to make the product a
bundle. When doing so, `type` **may not** be set to subscription. When creating
bundles, you may optionally provide the `hideItems` parameter. When set to `1`,
the bundle will be communicated as one product, and the individual products in
the bundle will not be highlighted in any way.

## Products

Most of the time, you want to set [`type`](/types/product-type/) to `1` when
creating plain products.

:relevant-endpoints

GET /products
GET /product/{id}
POST /product/{id}
POST /bundle/{bundleId}/product/{productId}
POST /user/{userId}/subscription
