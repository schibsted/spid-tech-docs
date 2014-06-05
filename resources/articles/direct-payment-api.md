:title Direct Payment API

:aside

## Relevant endpoints

- [`POST` /user/{userId}/charge](/endpoints/POST/user/{userId}/charge/)
- [`GET` /user/{userId}/preferences/payment](/endpoints/GET/user/{userId}/preferences/payment/)
- [`POST` /order/{orderId}/capture](/endpoints/POST/order/{orderId}/capture/)
- [`POST` /order/{orderId}/credit](/endpoints/POST/order/{orderId}/credit/)
- [`POST` /order/{orderId}/cancel](/endpoints/POST/order/{orderId}/cancel/)
- [`POST` /paylink](/endpoints/POST/paylink/)

:body

The Direct Payment API enables server-to-server API calls to charge or authorize
payments onto an end-user's SPiD account. It can be used to perform recurring
payments where the client service handles the recurring logic for its
subscriptions (i.e., as opposed to using SPiD subscriptions). It can also be
used to perform single payments where the user doesn't need to be redirected to
SPiD, like 1-click/in-app purchases.

## Basic functionality

[The `/user/{userId}/charge` endpoint](/endpoints/POST/user/{userId}/charge/)
can be used to create a request to perform a direct purchase or authorization in
a user's SPiD account. For now, only credit card payments are supported through
this API.

Billing requests will create an order with corresponding order lines. The order
may be tagged in the same way as when using [paylinks](/paylink-api/): client
references for the order and/or order lines, as well as the
[tag parameter](http://localhost:3000/tracking-parameters/) used in regular SPiD
purchase flows.

## Preconditions and requirements

* Access to the endpoint. Only given to vetted implementations and use cases.
* The user to be charged must have a connection to the client.
* The user must have a valid credit card that is available for direct payments

If the user does not have a valid credit card, the client can optionally create
a [paylink](/paylink-api/) and send the user to SPiD. Here they will be prompted
to add a valid credit card to their account while making the payment.

## Callbacks

Because transactions are handled asynchronously it is **strongly recommended**
to implement SPiD's [callback functionality](/callbacks/) when working with
payments. This way your client can stay up to date on transaction statuses.

## Purchase flows

In the following diagrams, "SPiD API" is the REST API, and "SPiD" is SPiD in the
browser - the UI your users will see when entering credit cards, logging in etc.

### Direct

Set `purchaseFlow` to `DIRECT` to use this flow. It is the default flow (when
`purchaseFlow` is omitted).

Click for bigger version

[![Sequence diagram for direct charges](/images/direct_payment_api_flow_direct.png)](/images/direct_payment_api_flow_direct.png)

### Authorize

Set `purchaseFlow` to `AUTHORIZE` to use this flow.

Click for bigger version

[![Sequence diagram for authorization](/images/direct_payment_api_flow_authorize.png)](/images/direct_payment_api_flow_authorize.png)

### Failures with new paylink option

This diagram illustrates the recommended strategy for direct payment failures.
Because the client never really knows if a user has a valid payment identifier
or whether other direct payment preconditions have been met, a retry may be
attempted with a paylink. When a failure occurs, the client can create a paylink
via the [Paylink endpoint](/endpoints/POST/paylink/), which will create a
purchase URL for the same products to be charged. This link must be communicated
to the user so that they may complete the purchase via the ordinary redirect
payment flow (e.g. with a redirect or as a button/link). As the user completes
checkout in SPiD, they will need to add a credit card, which will be charged
directly next time a direct payment is attempted.

[![Sequence diagram with failures and paylinks](/images/direct_payment_paylink.png)](/images/direct_payment_paylink.png)
