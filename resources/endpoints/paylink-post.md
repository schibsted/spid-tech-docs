:introduction

Create a new [paylink](/paylink-api/).

The `items` parameter should be a JSON array of objects. Each object should
contain at least the following required fields:

- `description`
- `price` (total price including VAT, in cents - 10000 is 100,-)
- `vat` (fraction × 10000, for instance 25% would be 2500)

These fields may be omitted if the optional field `productId` is provided. This will make the item inherit `description`, `price`, `vat` from the product.
Example: if only `description` and `productId` is provided, the item will not inherit `description` from the product.

Additionally, you may use the following optional fields:

- `clientItemReference`
- `productId`
- `ogpUri`
- `currency`
- `quantity`
- `name`

See the [paylink item object specification](/types/paylink-item/) for further
details on these fields.

The `paymentOptions` parameter defines allowed payment methods for Paylink. It can be provided in two formats - as:

- integer bitmask ([bitmask format specification](/types/payment-options/))
- JSON array of objects ([array format specification](/types/payment-options-array/)) `available since SchAcc version 2.111.0`


:relevant-types paylink-item

:relevant-endpoints

GET /paylink/{paylinkId}
POST /paylink/{paylinkId}/lock
POST /paylink/{paylinkId}/unlock
POST /paylink/{paylinkId}/use
POST /paylink
DELETE /paylink/{paylinkId}

:example-params

items: [{\\"name\\":\\"Episode\\",\\"description\\":\\"Star Wars IV\\",\\"price\\":9900,\\"vat\\":2500},{\\"description\\":\\"Star Wars V\\",\\"price\\":9900,\\"vat\\":2500},{\\"description\\":\\"Star Wars VI\\",\\"price\\":9900,\\"vat\\":2500}]
paymentOptions: [{\\"type\\":\\"PAYEX_CC\\"},{\\"type\\":\\"KLARNA\\"},{\\"type\\":\\"PROVIDED\\",\\"subType\\":\\"Vouchers\\",\\"url\\":\\"https://external-url.com\\",\\"name\\":\\"Vouchers\\",\\"imageUrl\\":\\"https://external-url.com/vouchers-icon.jpeg\\",\\"description\\":\\"Vouchers allow you to pay with voucher code.\\"}]
