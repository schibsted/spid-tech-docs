:introduction

Create a new [paylink](/paylink-api/).

The `items` parameter should be a JSON array of objects. Each object should
contain at least the following required fields:

- `description`
- `price` (total price including vat)
- `vat` (fraction × 10000, for instance 25% would be 2500)

Additionally, you may use the following optional fields:

- `clientItemReference`
- `productId`
- `ogpUri`
- `currency`
- `quantity`
- `type`

See the [paylink item object specification](/types/paylink-item/) for further
details on these fields.

:relevant-types paylink-item

:example-params

items: [{\\"description\\":\\"Star Wars IV\\",\\"price\\":9900,\\"vat\\":2500},{\\"description\\":\\"Star Wars V\\",\\"price\\":9900,\\"vat\\":2500},{\\"description\\":\\"Star Wars VI\\",\\"price\\":9900,\\"vat\\":2500}]
