:title Using SPiD as a payment provider
:aside

## On this page

- [Paylinks](#paylinks)
- [Direct payment](#direct-payment)

:body

SPiD can be used as a payment provider in two main ways:

### 1. SPiD as your product catalog

SPiD offers a rich data model in which you can store all your products,
subscriptions and bundles. These may be created
[through the API](/endpoints/POST/product/), or through the product
administration interface in Ambassador. The benefit of this approach is that
non-developers can work directly with the product catalog.

When storing your products in SPiD, you have access to all payment options:

1. Fully automatic subscriptions (SPiD charges subscribers at appropriate times)
2. Direct payment - for recurring payments (when not using automatic
   subscriptions)
3. Paylinks - Send customers directly to checkout with one or more products
4. Full e-commerce solution in SPiD, where users can choose from your product
   catalog and check out

### 2. Keeping your own product catalog

Storing products in SPiD is entirely optional, and you may choose not to do so,
for any number of reasons. If you prefer to keep your own product catalog, you
may still use two different means of charging users:

1. Direct charge - for recurring payments
2. Paylinks - Send customers directly to checkout with one or more products

Below you will find guides for implementing both paylinks and direct charge.
Both of these may be used regardless of where your products are stored, but the
guides presume no products stored in SPiD. The differences for a product catalog
in SPiD are noted where appropriate.

## Paylinks

With paylinks, you prepare the product(s) the user wants to buy and inform SPiD
of the impending purchase. SPiD will return a URL that will take the user to the
check out process for the selected products. Products may or may not be storedm
in SPiD.

[Guide: Getting started with paylinks](/getting-started-with-paylinks/)

## Direct payment

Direct payment allows for server-to-server API calls to charge or authorize
payments on a user's SPiD account. It can be used for recurring payments where
the user does not need to be sent through a checkout flow on SPiD's pages.

If you want recurring payments to be handled fully automatically by SPiD, you
should look into [creating subscriptions](/endpoints/POST/product/) in SPiD.

[Guide: Getting started with direct payment](/getting-started-with-direct-payment/)
