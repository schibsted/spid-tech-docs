:title Varnish integration
:frontpage
:category api-integration
:body

Using Varnish and the [JavaScript SDK](/sdks/javascript/), clients can implement
paywalls with SPiD with relative ease and little custom server-side logic -
Varnish will handle the negotiation and make the authorization requests to SPiD.
Defering the authorization to Varnish means your server only needs to generate
any page once and can cache it until it is changed. There's no need to
regenerate pages for every unique user, as long as personalization is done with
JavaScript on the frontend.

This page describes a custom module built by Varnish Software for SPiD, and is a
paid product. If you are interested in this product,
[contact support](mailto:schibstedaccount@schibsted.com).

When using the Varnish SPiD payment wall, the high-level flow is as follows:

- A user visits your site
- The JavaScript SDK sets a cookie in the user's browser, linking them to their
  SPiD user account (provided that they are logged in)
- The user tries to access content behind a paywall
- Varnish uses the cookie to ask SPiD if the user has access to the content
- Varnish caches the response and allows/denies access (current and future ones)
  until the cache expires

Note that Varnish also has a
[generic paywall module](https://www.varnish-software.com/product/varnish-paywall)
that may be used with SPiD as well. This document **only** describes the
SPiD-specific paywall module.

## Implementing the Varnish SPiD paywall

Varnish Software will assist you with installing the Varnish module. The only
action required on your end to implement this feature is to make sure that
protected content includes the following headers:

```text
X-SP-Access-Control: subscription
X-SP-Pids: 55,98
X-SP-Auth-Failed: /sport/article34232.ece?preview=true
```

### X-SP-Access-Control

This is the header that decides whether the object being served is protected or
not. If it contains the phrase `subscription`, Varnish will assume the object is
protected, and go on to inspect the two other headers. If this header does not
contain `subscription`, the object is served directly.

### X-SP-Pids

This header contains the product IDs that the user needs access to in order to
view the content. It may be a single alphanumeric ID, or a comma-separated list
of IDs.

### X-SP-Auth-Failed

This is a URL where the user may be redirected if they do not have access to the
protected content. In the above example, the user will be redirected to a
preview of the article they are attempting to view if they don't have an active
subscription giving them access to the full article.

## Authorization cookie

When using the Varnish SPiD paywall, authorization requests are made by the
paywall module. Varnish uses the `SP_ID` cookie, which is encrypted and set by
the JavaScript SDK. When using the paywall module, you must also use the
JavaScript SDK, otherwise, users will not be recognized, and thus not given
access to anything.

The `SP_ID` cookie value may be retrieved from the API through the
[/user/{userId}/varnishId endpoint](/endpoints/GET/user/{userId}/varnishId/).

## Enabling Varnish

Varnish support is not enabled by default. It needs to be enable per client.
This setting can be enabled in Selfservice.
