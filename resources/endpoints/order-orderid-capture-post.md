:introduction

Capture an amount on a specific order.

After performing an authorization (example: paylink with purchaseFlow
authorize), the client typically ensures that the customer will receive the
item/service they ordered, e.g. waits until the goods have been shipped, before
performing a capture. The capture actually charges the customer with the
authorized amount.

Both full and partial capture is supported:

* Capture the whole order
* Capture single order items, one at the time
* Capture a partial amount of a single order item, up to the total item amount

To perform a full capture of the order, you only need to provide the
`description` request parameter.

To perform a partial capture, provide the `orderItemId` request parameter to
specify which order item to capture, and optionally the `amount` request
parameter to specify how much to capture. If `amount` is not provided, the order
item will be captureed in full.
