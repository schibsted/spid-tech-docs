:introduction

Charge or authorize a payment onto an end-user's SPiD account. Refer to
[the direct payment API introduction](/direct-payment-api/) for a details.

## Successful responses

Successful responses from this endpoint indicate that the transaction was
accepted by SPiD. It does not necessarily indicate that the transaction is
processed. For this reason, it is important to verify the status of the returned
order object. If the order status is pending (`"1"`), you should implement the
[callback functionality](/callbacks/) in order to be notified when the order
status changes. Correctly implementing callbacks is essential to keep client
systems synchronized with SPiD.
