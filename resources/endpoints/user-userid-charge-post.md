:introduction

Charge or authorize a payment onto an end-user's SPiD account. 

The `items` parameter should be a JSON array of objects. There is two basic ways
of providing order items: by referencing a product stored in SPiD, or by
providing all the product data inline.

### Products stored in SPiD

When selling products stored in SPiD, each order item should contain the
following fields:

- `name`
- `productId`

### Products not stored in SPiD

When selling products unknown to SPiD, each order item must provide the
necessary details:

- `name`
- `description` - a short text displayed in receipts
- `price` - total price including VAT, in "cents" (10000 is 100,-)
- `vat` - fraction × 10000, for instance 25% would be 2500

### Optional order item fields

An order item may also use the following optional fields:

- `clientItemReference`
- `type`
- `quantity`

See the [order item object specification](/types/order-item/) for further
details on these fields.

## Successful responses

Successful responses from this endpoint indicate that the transaction was
accepted by SPiD. It does not necessarily indicate that the transaction is
processed. For this reason, it is important to verify the status of the returned
order object. If the order status is pending (`"1"`), you should implement the
[callback functionality](/callbacks/) in order to be notified when the order
status changes. Correctly implementing callbacks is essential to keep client
systems synchronized with SPiD.
