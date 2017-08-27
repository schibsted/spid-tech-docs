:introduction

Credit an order. Both full and partial credits are supported through order items
crediting:

* Credit the whole order (or remaining of `captured - credited`)
* Credit single order items, one at the time.
* Credit a partial amount of a single order item, up to the total captured item amount.

To perform a full credit of the order, you only need to provide the
`description` request parameter.

To perform a partial credit, provide the `orderItemId` request parameter to
specify which order item to credit, and optionally the `amount` request
parameter to specify how much to credit. If `amount` is not provided, the order
item will be credited in full.

Regardless of the type of credit, the `notifyUser` request parameter may be used
to specify whether or not the user should be notified about the credit
transaction (`1` for yes, `0` for no).
