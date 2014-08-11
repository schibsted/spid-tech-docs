:title Setting up subscriptions with direct payment

:aside

## Prerequisites

In order to complete this guide, you need to have completed these guides:

- [Getting Started](/getting-started/)
- [Implementing Single sign-on with SPiD](/implementing-sso/)

## See also

- [Callbacks](/callbacks/)
- [The Direct Payment API](/direct-payment-api/)
- [`POST` /user/{userId}/charge](/endpoints/POST/user/{userId}/charge/)

:body

When you have completed this guide, you can charge users in the background for
recurring payments.

Note that some interactions, like adding a credit card, must happen on SPiD or
even Payex' sites, for security reasons. Direct payment only works for users
with a valid credit card in their SPiD account.

## Overview

This is the flow:

- A user subscribes to some product
- At billing time, you prepare the order information
- You charge the user, using their `userId`, via the REST API.
- You (may) receive further updates on the generated order via [SPiD callbacks](/callbacks/).

We'll look at these steps in detail in the rest of the guide. If you prefer to
just dive in, take a look at [these working examples](#working-examples).

## The user signs up for a subscription

This guide presumes that you handle your own subscriptions. See
[the product endpoint](/endpoints/POST/product/) for fully automatic
subscriptions handled by SPiD.

The example code presumes a subscription and a list of users to charge for that
subscription, that you would run as a cron job or something similar. When the
example code is run, it uses the direct payment API to charge the users, and
prints a summary to the screen.

## Charging for the order

To charge for the order:

1. Prepare the billing information, and `POST` it to `/user/{userId}/charge`.
2. The API returns an order object.
3. Callbacks may update the generated order later (credit card transactions are
  asynchronous).
4. When the order is complete, grant the user access to the product(s).

When using direct payment this way, you are assuming all responsibility for
negotiating access to content. It is up to you how you achieve this, but you
should make sure to persist the subscription ID, or something to this effect,
such that the user will be granted access to the content the next time they try.

### Preparing the billing information

The required fields for a direct charge are:

- `requestReference` - Some unique reference for this order (typically, your order ID).
- `items` - A JSON array of order items.
- `hash` - A [verified hash](/verified-hash/) of the parameters in the request.

Each order item should have at least the following fields:

- `name` - So the customer knows what they're paying for.
- `price` - Price, including VAT, in "cents", e.g. NOK100 is represented as 10000.
- `vat` - VAT in percent * 100, 25% is represented as 2500.

And optionally `quantity`, if other than `1`.

### Building the order object

# :tabs

## :tab Java

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/RecurringPaymentProcessor.java" title="Subscription is an object with name, price and vat"/>
<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/RecurringPaymentProcessor.java" title="Create data to POST to /user/{userId}/charge"/>

Notice the use of `signParams` to add the verified hash to the params map. This
is a helper method that wraps functionality in the Java client library:

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/ApiClient.java" title="Creating the security helper"/>
<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/ApiClient.java" title="Signing parameters"/>

## :tab PHP

<spid-example lang="php" src="/direct-payment/processPayments.php" title="Subscription is an assoc array with name, price and vat"/>
<spid-example lang="php" src="/direct-payment/recurringPayments.php" title="Create data to POST to /user/{userId}/charge"/>

Notice the use of `$client->createHash($params)` to build the verified hash.
This feature is built into the SDK. The `$client` in this case is a regular
instance of the SDK:

<spid-example lang="php" src="/direct-payment/recurringPayments.php" title="Create SPiD client"/>

Also note that the `items` where passed in as an array - the PHP SDK will
automatically serialize any array parameter into a JSON string.

## :tab Clojure

<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Subscription is a map with name, price and vat"/>
<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Create data to POST to /user/{userId}/charge"/>

Notice the use of `spid/sign-params` to add the verified hash to the params map.
This feature is built into the Clojure client library.

# :/tabs

### Attempt direct payment

Having built the order object, we can attempt a direct charge. The request may
fail for a variety of reasons, the most important being that the customer does
not have a valid credit card stored in their account. In the example code, we
simply print an error to the console when this happens - in your production
systems, such an error should be taken seriously and processed further.

# :tabs

## :tab Java

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/RecurringPaymentProcessor.java" title="Attempting the direct payment"/>

## :tab PHP

<spid-example lang="php" src="/direct-payment/recurringPayments.php" title="Create SPiD client"/>
<spid-example lang="php" src="/direct-payment/recurringPayments.php" title="Attempting the direct payment"/>

## :tab Clojure

<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Attempting the direct payment"/>

# :/tabs

## Printing a summary

When the direct payment completes successfully, it will return an
[order object](/types/order/). We can use information from this object along
with things like [order status](/types/order-status/) and
[payment identifier type](/types/payment-id-type/) to create a summary.

# :tabs

## :tab Java

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/OrderFormatter.java" title="Preparing order data for the summary"/>
<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/RecurringPaymentProcessor.java" title="Charging the users, and printing a report"/>

## :tab PHP

<spid-example lang="php" repo="php" src="/direct-payment/recurringPayments.php" title="Preparing order data for the summary"/>
<spid-example lang="php" repo="php" src="/direct-payment/processPayments.php" title="Charging the users, and printing a report"/>

## :tab Clojure

<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Preparing order data for the summary"/>
<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Charging the users, and printing a report"/>

# :/tabs

## Staying up to date

Depending on how the user is charged, the order may not be complete when the API
call finishes. For this reason, it is important that you implement
[the callback facility](/callbacks/), which will keep you posted about all your
orders.

If a direct payment is made with the `purchaseFlow` parameter set to
`"AUTHORIZE"`, the user's card will only be authorized for the provided amount.
You must then later either
[`capture`](/endpoints/POST/order/{orderId}/capture/) or
[`credit`](/endpoints/POST/order/{orderId}/credit/) the order. See
[the direct payment API](/direct-payment-api/) for full details.

## Working examples

If you're unsure on certain details after reading this guide, do check
out these working examples:

- [Direct payment example for PHP](https://github.com/schibsted/spid-php-examples/tree/master/direct-payment)
- [Direct payment example for Java](https://github.com/schibsted/spid-java-examples/tree/master/direct-payment)
- [Direct payment example for Clojure](https://github.com/schibsted/spid-clj-examples/tree/master/direct-payment)
