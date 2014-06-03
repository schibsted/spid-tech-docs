:title The Bundle API

:relevant-endpoints

POST /bundle/{bundleId}/product/{productId}
DELETE /bundle/{bundleId}/product/{productId}

:aside

## Relevant type definitions

- [Bundle](/types/bundle/)

:body

Bundles allow for linking products together and to sell them as one unit. The
bundle itself can be considered a product as well, and most API endpoints that
operate on products can operate on bundles. When a user is given access to a
bundle, they will automtically gain access to all the contained products.

When products are added to bundles, they are referred to by the API as bundle
items.

## Pricing

There are three pricing options for bundles

#### Top level

Set one price for the bundle as a whole

#### Item level

Set individual prices on items in the context of the bundle (does not change the
price of these products when sold separately). The total price of the bundle
will be the sum of the price of each bundle item.

#### Automatic

By omitting price for both the bundle and its items, a price will be
automatically calculated based on the standard prices for every bundle item.

## Display

Through the `hide_items` flag on bundles, you control whether or not individual
bundle items are displayed to the user on the product overview, when purchasing,
in receipts etc. When showing items, their display order may be controlled with
the bundle items' `order` attribute.

## Dynamic bundles

Bundles are dynamic by default, meaning that future changes to the bundle will
affect all users, including those who have had access to the bundle prior to the
change.
