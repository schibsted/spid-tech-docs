:title Using SPiD as a payment provider
:aside

## On this page

- [Paylinks](#paylinks)
- [Direct payment](#direct-payment)

:body

SPiD can be used as a payent provider in a few different ways. This page
contains guides and working examples for two approaches, and it is recommended
to get a rough overview of both approaches before diving in.

## Paylinks

Paylinks require little to no setup, and you can have a working checkout process
in literally 10 minutes. There is no need to store products or other data in
SPiD. All you need to do is to inform SPiD about the contents of an order via
the REST API, and SPiD will generate a checkout URL. The URL will allow the user
to pay and return to your site.

### When to use Paylinks

- When you want to offer all possible payment options
- When you are comfortable handling your own product database

[Guide: Gettting started with paylinks](/getting-started-with-paylinks/)

## Direct payment

Direct payment allows for server-to-server API calls to charge or authorize
payments on a user's SPiD account. It can be used for recurring payments where
the user does not need to be sent through a checkout flow on SPiD's pages.

If you want recurring payments to be handled fully automatically by SPiD, you
should look into [creating subscriptions](/endpoints/POST/product/) in SPiD.

### When to use direct payment

- Recurring payments
- When you want to handle recurring logic yourself

[Guide: Gettting started with direct payment](/getting-started-with-direct-payment/)
