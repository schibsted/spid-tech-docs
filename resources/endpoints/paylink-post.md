:introduction

Create a new [paylink](/paylink-api/).

The `items` parameter should be a JSON array of objects. Each object should
contain at least the following required fields:

- `description`
- `price`
- `vat`

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
