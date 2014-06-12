:title Getting started with Direct payment

:aside

## Prerequisites

In order to complete this guide, you need to have completed these guides:

- [Getting Started](/getting-started/)
- [Implementing Single sign-on with SPiD](/implementing-sso/)
- [Getting started with Paylinks](/getting-started-with-paylinks/)

Paylinks are **strongly recommended** as a fallback for direct payment,
otherwise you will not be able to charge users without a valid credit card in
SPiD.

## See also

- [Callbacks](/callbacks/)
- [The Direct Payment API](/direct-payment-api/)
- [`POST` /user/{userId}/charge](/endpoints/POST/user/{userId}/charge/)

:body

When you have completed this guide, you can sell products by providing your own
customized check-out experience, or charge users in the background (for
recurring payments).

Note that some interactions, like adding a credit card, must happen on SPiD or
even Payex' sites, for security reasons. Direct payment only works for users
with a valid credit card in their SPiD account.

## Overview

This is the flow:

- The user logs in to SPiD single sign-on via your website.
- You build an order.
- You charge the user, using their `userId`, via the REST API.
    - If the charge fails, you generate a paylink and redirect the user to SPiD.
- You (may) receive further updates on the generated order via [SPiD callbacks](/callbacks/).

We'll look at these steps in detail in the rest of the guide. If you prefer to
just dive in, take a look at [these working examples](#working-examples).

## Starting point

Start this guide with the application you developed in the
[implementing single sign-on guide](/implementing-sso/), or something similar.
The app should have a `/` route that offers the user a login URL, a
`/create-session` endpoint that logs the user into SPiD and creates an
application local session, and finally a `/logout` route that ends the session
and logs the user out of SPiD.

We will not repeat the code for creating paylinks in this guide, refer to the
[working examples](#working-examples) or the
[paylink guide](/getting-started-with-paylinks/) for details.

## The user chooses products on your website

In the example, we're keeping it simple with three products - each with an input
field to specify quantity:

<spid-example lang="html" repo="clj" src="/direct-payment/resources/index.html" title="Keeping product choices simple"/>

## Charging for the order

To charge for the order:

- Build an `order` object, and `POST` it to `/user/{userId}/charge`
- `POST` fails
    - Build a paylink
    - Redirect the user to SPiD
- `POST` succeeds
    - The user is returned with an order
    - Callbacks may update the generated order later (credit card transactions are asynchronous)
    - When the order is complete, grant the user access to the product(s)

To charge for the order, the user must be logged in, and you must have their
`userId`. The [working example apps](#working-examples) all enforce login before
allowing the user to check out.

### The order object

The required fields for an order are:

- `requestReference` - Some unique reference for this order (typically, your order ID)
- `items` - A JSON array of order items, as a string
- `hash` - A [verified hash](/verified-hash/) of the parameters in the request

Each order item should have at least the following fields:

- `quantity`
- `name`
- `price` - Price, including VAT, in "cents", e.g. NOK100 is represented as 10000
- `vat` - VAT in percent * 100, 25% is represented as 2500

### :tabs Building the order object

#### :tab Java

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/ShopController.java" title="The entirety of our product catalog right here"/>
<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/ShopController.java" title="Create data to POST to /user/{userId}/charge"/>

Notice the use of `signParams` to add the verified hash to the params map. This
is helper method that wraps functionality in the Java client library:

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/BaseController.java" title="Creating the security helper"/>
<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/BaseController.java" title="Signing parameters"/>

#### :tab PHP

<spid-example lang="php" src="/direct-payment/checkout.php" title="The entirety of our product catalog right here"/>
<spid-example lang="php" src="/direct-payment/checkout.php" title="Create data to POST to /user/{userId}/charge"/>

Notice the use of `$client->createHash($params)` to build the verified hash.
This feature is built into the SDK. The `$client` in this case is a regular
instance of the SDK:

<spid-example lang="php" src="/direct-payment/checkout.php" title="Create SPiD client"/>

Also note that the `items` where passed in as an array - the PHP SDK will
automatically serialize any array parameter into a JSON string.

#### :tab Clojure

<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="The entirety of our product catalog right here"/>
<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Create data to POST to /user/{userId}/charge"/>

Notice the use of `spid/sign-params` to add the verified hash to the params map.
This feature is built into the Clojure client library.

### :/tabs

Having built the order object, we can attempt a direct charge. The request may
fail for a variety of reasons, the most important being that the customer does
not have a valid credit card stored in their account. When this happens, we fall
back to creating a paylink and redirecting the user. Once redirected, the user
will be prompted to add a credit card, ensuring your next attempt at a direct
charge for this user is likely to succeed.

### :tabs Attempt direct payment

#### :tab Java

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/ShopController.java" title="Attempting the direct payment, with a Paylink fallback"/>

The user was previously stored in the session by the login action (refer to
[the SSO guide](/implementing-sso/)).

#### :tab PHP

<spid-example lang="php" src="/direct-payment/checkout.php" title="Attempting the direct payment, with a Paylink fallback"/>

The user was previously stored in the session by the login script (refer to
[the SSO guide](/implementing-sso/)).

<spid-example lang="php" src="/direct-payment/checkout.php" title="Retrieving the user"/>

#### :tab Clojure

<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Attempting the direct payment, with a Paylink fallback"/>

The user was previously stored in the session by the login script (refer to
[the SSO guide](/implementing-sso/)).

### :/tabs

## Displaying a receipt

When the direct payment completes successfully, it will return an
[order object](/types/order/). We can use information from this object along
with things like [order status](/types/order-status/) and
[payment identifier type](/types/payment-id-type/) to create receipt for
the user.

### :tabs Receipt

#### :tab Java

<spid-example lang="java" src="/direct-payment/src/main/java/no/spid/examples/ShopController.java" title="Preparing order data for the receipt view"/>
<spid-example lang="html" repo="java" src="/direct-payment/src/main/resources/templates/receipt.html" title="Simple receipt"/>

#### :tab PHP

<spid-example lang="php" repo="php" src="/direct-payment/receipt.php" title="Simple receipt"/>

#### :tab Clojure

<spid-example lang="clj" src="/direct-payment/src/spid_clojure_direct_payment_example/core.clj" title="Extracting order data for the receipt view"/>
<spid-example lang="html" repo="clj" src="/direct-payment/resources/receipt.html" title="Simple receipt"/>

### :/tabs

## Staying up to date

Depending on how you charge the user, the order may not be complete when the API
call finishes. For this reason, it is important that you implement
[the callback facility](/callbacks/), which will keep you posted about all your
orders.

If a direct payment is made with the `purchaseFlow` parameter set to
`"AUTHORIZE"`, the user's card will only be authorized for the provided amount.
You must then later either
[`capture`](/endpoints/POST/order/%7BorderId%7D/capture/) or
[`credit`](/endpoints/POST/order/%7BorderId%7D/credit/) the order. See
[the direct payment API](/direct-payment-api/) for full details.

## Working examples

If you're unsure on certain details after reading this guide, do check
out these working examples:

- [Direct payment example for PHP](https://github.com/schibsted/spid-php-examples/tree/master/direct-payment)
- [Direct payment example for Java](https://github.com/schibsted/spid-java-examples/tree/master/direct-payment)
- [Direct payment example for Clojure](https://github.com/schibsted/spid-clj-examples/tree/master/direct-payment)
