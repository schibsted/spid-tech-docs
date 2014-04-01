:introduction

# Direct Payment API

The Direct Payment API enables server to server API calls to charge or authorize
a payment onto an end-user's SPiD account. It can be used to perform recurring
payments where the client service handles the recurring logic for its
subscriptions (i.e., as opposed to using SPiD subscriptions). It can also be
used to perform single payments where the user doesn’t need to be redirected to
SPiD, like 1-click/in-app purchases.


## Basic functionality

The basic functionality provided by this endpoint will handle a payment request
containing the parameters needed to perform a direct purchase or authorization
in a user's SPiD account. For now, we only support credit card payments through
this API.

The billing request will create an order with corresponding order lines. The
order may also be tagged as a client would normally do through Paylinks, with
both their own client references (on the order and each order lines) and with
the tag parameter used in the normal SPiD purchase flows where the user is
involved.

## Preconditions and requirements

* Access to the endpoint. Only given to vetted implementations and use cases.
* The user to be charged must have a connection to the client.
* The user must have a valid credit card that is available for direct payments

If the user does not have a valid credit card or other valid payment methods for
direct payment, the client can optionally create a paylink and send the user to
SPiD to add a valid credit card into his account along with doing the initial
payment.

## Successful responses

Successful responses from this endpoint indicates that the transaction was
accepted by SPiD. It does not necessarily indicate that the transaction is
processed. For this reason, it is important to verify the status of the returned
order object. If the order status is pending (`"1"`), you should implement the
[callback functionality](/callbacks) in order to be notified when the order
status changes. Correctly implementing callbacks is essential to keep client
systems synchronised with SPiD.

:inline-types order-type order-status transaction-synced-status order-item transaction payment-options
