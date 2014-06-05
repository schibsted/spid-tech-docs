:title Using SPiD as a payment provider
:aside

## On this page

- [Paylinks](#paylinks)
- [Direct payment](#direct-payment)

:body

SPiD can be used as a payent provider through a few different APIs. Below you
will find guides and working examples for three approaches, but it is
recommended to get a rough overview of each approach before diving in.

## Paylinks

With paylinks there is little setup, and you can have a working checkout process
in literally 10 minutes. With paylinks, there is no need to store products or
other data in SPiD - all you need is to inform SPiD via the REST API about the
contents of an order, and SPiD will give you back a checkout URL. The URL will
allow the user to pay and return to your site.

### When to use Paylinks

- Bla
- Bla bla
- Yada

## Direct payment

Direct payment allows for server-to-server API calls to charge or authorize
payments on a user's SPiD account. It can be used for recurring payments and
other payments where the user does not need to be sent through a checkout flow
on SPiD's pages.

If you want recurring payments to be handled fully automatically by SPiD, you
should look into [creating subscriptions](/endpoints/POST/product/) in SPiD.

### When to use direct payment

- Hmm
