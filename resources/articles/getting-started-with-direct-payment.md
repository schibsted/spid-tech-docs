:title Getting started with Direct payment

:aside

## Prerequisites

In order to complete this guide, you need to know your:

- client ID
- API secret

You should also have gone through the [Getting Started](/getting-started/)
guide, and the [Single sign-on guide](/implementing-sso/) guide. It is
**strongly recommended** that you also have gone through the
[Paylinks guide](/getting-started-with-paylinks/), as your customers may not
always be able to complete payment without a paylink fallback.

## See also

- [Implementing Single sign-on with SPiD](/implementing-sso/)
- [Getting started with Paylinks](/getting-started-with-paylinks/)
- [Callbacks](/callbacks/)

:body

When you have completed this guide, you can sell products by providing your own
customized check-out experience, or charge users in the background (for
recurring payments). Your system will keep in sync with transactions via SPiD's
[callback facility](/callbacks/).

## Overview

This is the flow:

- The user logs in to SPiD single sign-on via your website.
- You build an order using the SPiD `userId`.
- You charge the user via the REST API.
    - If the charge fails, you generate a paylink and redirect the user to SPiD.
- You receive further updates on the generated order via SPiD callbacks.

We'll look at this in detail in the rest of the guide. If you prefer to just
dive in, take a look at [these working examples](#working-examples).

## Configure your application

These variables change between production and staging environments:

- Your client ID
- Your client secret
- Your client signature secret
- The base URL to SPiD
- Your base URL

How you choose to configure your application is up to you, but
these variables should not be hard-coded.

## Working examples

If you're unsure on certain details after reading this guide, do check
out these working examples:

- [Paylinks example for PHP](https://github.com/schibsted/spid-php-examples/tree/master/direct-payment)
- [Paylinks example for Java](https://github.com/schibsted/spid-java-examples/tree/master/direct-payment)
- [Paylinks example for Clojure](https://github.com/schibsted/spid-clj-examples/tree/master/direct-payment)
