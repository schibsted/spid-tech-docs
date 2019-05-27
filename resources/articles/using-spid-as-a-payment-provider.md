:title Using Schibsted account as a payment provider
:aside

## On this page

- [Paylinks](#paylinks)

:body

Schibsted account can be used as a payment provider in two main ways:

### 1. Schibsted account as your product catalog

Schibsted account offers a rich data model in which you can store all your products,
subscriptions and bundles. These may be created
[through the API](/endpoints/POST/product/), or through the product
administration interface in [Ambassador](/ambassador/). The benefit of this approach is that
non-developers can work directly with the product catalog.

When storing your products in Schibsted account, you have access to all payment options:

1. Fully automatic subscriptions (Schibsted account charges subscribers at appropriate times)
2. Direct payment - for recurring payments (when not using automatic
   subscriptions)
3. Paylinks - Send customers directly to checkout with one or more products
4. Full e-commerce solution in Schibsted account, where users can choose from your product
   catalog and check out

### 2. Keeping your own product catalog

Storing products in Schibsted account is entirely optional, and you may choose not to do so,
for any number of reasons. If you prefer to keep your own product catalog, you
may still use two different means of charging users:

1. Create orders directly
2. Paylinks - Send customers directly to checkout with one or more products

Below you will find guides for implementing both paylinks and direct charge.
Both of these may be used regardless of where your products are stored, but the
guides presume no products stored in Schibsted account. The differences for a product catalog
in Schibsted account are noted where appropriate.

## Paylinks

With paylinks, you prepare the product(s) the user wants to buy and inform Schibsted account
of the impending purchase. Schibsted account will return a URL that will take the user to the
check out process for the selected products. Products may or may not be stored
in Schibsted account.

- [Guide: Getting started with Schibsted account Paylinks](/getting-started-with-paylinks/)
- [Getting started with Payment Platform](https://confluence.schibsted.io/display/SPDEV/The+Order+object#TheOrderobject-New/Create)
