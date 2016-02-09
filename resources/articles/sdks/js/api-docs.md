--------------------------------------------------------------------------------
:title API Docs
:category analytics
:aside

## At a glance

- [`SPiD`](#sdk)
    - [`SPiD.init`](#init)
    - [`SPiD.hasSession`](#has-session)
    - [`SPiD.hasProduct`](#has-product)
    - [`SPiD.hasSubscription`](#has-subscription)
    - [`SPiD.setTraits`](#set-traits)
    - [`SPiD.logout`](#logout)
    - [`SPiD.acceptAgreement`](#acceptAgreement)
- [`SPiD.event`](#event)
    - [`SPiD.event.subscribe`](#subscribe)
    - [`SPiD.event.unsubscribe`](#unsubscribe)
    - [`SPiD.event.fire`](#fire)
- [`SPiD.sessionCache`](#sessionCache)
    - [`SPiD.sessionCache.get`](#sessionCacheGet)
    - [`SPiD.sessionCache.set`](#sessionCacheSet)
    - [`SPiD.sessionCache.clear`](#sessionCacheClear)
- [`SPiD.Uri`](#spid_uri)
    - [`SPiD.Uri.login`](#get-login-uri)
    - [`SPiD.Uri.signup`](#get-signup-uri)
    - [`SPiD.Uri.logout`](#get-logout-uri)
    - [`SPiD.Uri.account`](#get-account-uri)
    - [`SPiD.Uri.purchaseHistory`](#get-purchase-history-uri)
    - [`SPiD.Uri.subscriptions`](#get-subscriptions-uri)
    - [`SPiD.Uri.products`](#get-products-uri)
    - [`SPiD.Uri.redeem`](#get-redeem-voucher-uri)
    - [`SPiD.Uri.purchaseProduct`](#get-purchase-product-uri)
    - [`SPiD.Uri.purchaseCampaign`](#get-purchase-campaign-uri)

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js/events/)
- [Response signature and validation](/sdks/js/response-signature-and-validation/)
- [Hosting](/sdks/js/hosting/)
- [Best practices](/sdks/js/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

<a id="api-docs"></a>

All arguments to the `*URI` functions are optional. When omitted, `client_id`
defaults to the `client_id` used in `SPiD.init`, and `redirect_uri` defaults to
`window.location`.

<a id="sdk"></a>
For the following examples, assume the following variable declarations are
available:

```js
var redirectUri = "http://example.com";
var clientId = "4321abc0000";
```


<a id="init"></a>
### SPiD.init

```js
SPiD.init(options)
```

Initialises the SDK. The `options` parameter must contain the properties `server` and `client_id`.

<a id="has-session"></a>
### SPiD.hasSession

```js
SPiD.hasSession(callback)
```

Checks the current session status, and call the provided callback with
two arguments the first being error and the second being the result.
Calling `hasSession` will trigger a number of events
(depending on the user status et cetera), to `SPiD.event` subscribers,
notably the `SPiD.sessionChange` event.

```js
SPiD.hasSession(, function (err, response) {
    // do something with the response, if !err ...
});
```

<a id="has-product"></a>
### SPiD.hasProduct

```js
SPiD.hasProduct(product_id, callback, force)
```

Checks if user has access to a product. Set `force` to `true` to force a
roundtrip (e.g., bypass the cache).
The callback will be called with two arguments the first being error
and the second an object, whose `result` property will be `true`
if the user has access to the product in question.

```js
SPiD.hasProduct(2134423, function (err, response) {
    response === {
        baseDomain: "sdk.dev"
        displayName: "Anna Andersson"
        expiresIn: 6967
        familyName: "Andersson"
        gender: "male"
        givenName: "Anna"
        id: "4ebb7ce59caf7c1f22000001"
        photo: "http://www.gravatar.com/avatar/ec32937c22d1a4b1474657b776d0f398?s=200"
        productId: 10010
        result: true
        serverTime: 1360342353
        sig: "layQctw_mMOAM5RJnr9nr7RgPv_0lZqVAojXBVpQSBY.eyJyZXN1bHQiOnRydWUsInNlcnZlclRpbWUiOjEzNjAzNDIzNTMsInByb2R1Y3RJZCI6MTAwMTAsInVzZXJTdGF0dXMiOiJjb25uZWN0ZWQiLCJ1c2VySWQiOjIyMDAwMjEsImlkIjoiNGViYjdjZTU5Y2FmN2MxZjIyMDAwMDAxIiwiZGlzcGxheU5hbWUiOiJKb2FraW0gV1x1MDBlNW5nZ3JlbiIsImdpdmVuTmFtZSI6IkpvYWtpbSIsImZhbWlseU5hbWUiOiJXXHUwMGU1bmdncmVuIiwiZ2VuZGVyIjoibWFsZSIsInBob3RvIjoiaHR0cDpcL1wvd3d3LmdyYXZhdGFyLmNvbVwvYXZhdGFyXC9lYzMyOTM3YzIyZDFhNGIxNDc0NjU3Yjc3NmQwZjM5OD9zPTIwMCIsImV4cGlyZXNJbiI6Njk2NywiYmFzZURvbWFpbiI6InNkay5kZXYiLCJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiJ9"
        userId: 2200021
        userStatus: "connected"
        agreementAccepted: true
    };
});
```

<a id="has-subscription"></a>
### SPiD.hasSubscription

```js
SPiD.hasSubscription(product_id, callback, force)
```

Checks if user has access to a subscription. Set `force` to `true` to force a
roundtrip (e.g., bypass the cache).
The callback will be called with two arguments the first being error
and the second an object, whose `result` property will be `true`
if the user has access to the product in question.

```js
SPiD.hasSubscription(324892374, function (err, response) {
    response === {
        baseDomain: "sdk.dev"
        displayName: "Anna Andersson"
        expiresIn: 7173
        familyName: "Andersson"
        gender: "male"
        givenName: "Anna"
        id: "4ebb7ce59caf7c1f22000001"
        photo: "http://www.gravatar.com/avatar/ec32937c22d1a4b1474657b776d0f398?s=200"
        productId: 10010
        result: true
        serverTime: 1360342380
        sig: "ynj1V3nZhRa3JBw9_Up97e5yX0PLkWM0bc6M2EcHmLs.eyJyZXN1bHQiOnRydWUsInNlcnZlclRpbWUiOjEzNjAzNDIzODAsInByb2R1Y3RJZCI6MTAwMTAsInVzZXJTdGF0dXMiOiJjb25uZWN0ZWQiLCJ1c2VySWQiOjIyMDAwMjEsImlkIjoiNGViYjdjZTU5Y2FmN2MxZjIyMDAwMDAxIiwic3Vic2NyaXB0aW9uSWQiOjE2NTU3MiwiZGlzcGxheU5hbWUiOiJKb2FraW0gV1x1MDBlNW5nZ3JlbiIsImdpdmVuTmFtZSI6IkpvYWtpbSIsImZhbWlseU5hbWUiOiJXXHUwMGU1bmdncmVuIiwiZ2VuZGVyIjoibWFsZSIsInBob3RvIjoiaHR0cDpcL1wvd3d3LmdyYXZhdGFyLmNvbVwvYXZhdGFyXC9lYzMyOTM3YzIyZDFhNGIxNDc0NjU3Yjc3NmQwZjM5OD9zPTIwMCIsImV4cGlyZXNJbiI6NzE3MywiYmFzZURvbWFpbiI6InNkay5kZXYiLCJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiJ9"
        subscriptionId: 165572
        userId: 2200021
        userStatus: "connected"
        agreementAccepted: true
    };
});
```

<a id="set-traits"></a>
### SPiD.setTraits

Sets the given traits.

```js
SPiD.setTraits(traits, callback)
```

<a id="logout"></a>
### SPiD.logout

Logs the user out, and calls the callback when done.

```js
SPiD.logout(function (data) {
    alert("See ya next time!");
});
```


<a id="acceptAgreement"></a>
### SPiD.acceptAgreement

Calls the acceptAgreement endpoint, and (if the acceptAgreement call was successful) re-calls hasSession,
clearing any cached value before doing so.

```js
SPiD.acceptAgreement(callback)
```

<a id="event"></a>

<a id="subscribe"></a>
### SPiD.event.subscribe

Register subscriber for event. The event handler function will be called
whenever the named event occurs.

```js
SPiD.event.subscribe("auth:login", function (data) {
    alert("Welcome!");
});
```

<a id="unsubscribe"></a>
### SPiD.event.unsubscribe

Remove previously subscribed function. Pass in the event name and the function
originally passed to `SPiD.event.subscribe`.

```js
var callback = function (data) {
    alert("Welcome!");
};

SPiD.event.subscribe("auth:login", callback);

// ...

SPiD.event.unsubscribe("auth:login", callback);
```

Note that it must be the same function reference, you cannot inline the function
in the `subscribe` call when you want to later unsubscribe it.

<a id="fire"></a>
### SPiD.event.fire

Fire an event. The first argument is the event name, additional arguments are
passed to subscribers. This function is used by the JS SDK to propagate events
to client subscribers, but is not recommended for external use.


<a id="sessionCache"></a>

<a id="sessionCacheGet"></a>
### SPiD.sessionCache.get

Gets the currently cached session object.

```js
SPiD.sessionCache.get();
```

<a id="sessionCacheSet"></a>
### SPiD.sessionCache.set

Sets the currently cached session object, with an optional expiration time.

```js
SPiD.sessionCache.set(value, exipresInMillis);
```

<a id="sessionCacheClear"></a>
### SPiD.sessionCache.clear

Clears the session cache. Use this when you want to force a request to the `hasSession` endpoint.

```js
SPiD.sessionCache.clear();
```


<a id="spid_uri"></a>
## SPiD.Uri
The `SPiD.Uri` module is separated from the main JS SDK, and is optional. It's assigned to the global variable `SPiD_Uri`
when using the vanilla JS variant. The `SPiD.Uri` module is also available as with AMD on CommonJS packaging.


<a id="get-login-uri"></a>
### SPiD.Uri.login

Returns the login URI.

```js
SPiD_Uri.login(); //=> "https://identity-pre.schibsted.com/flow/login?response_type=code&flow=signup&client_id=4321abc0000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.login(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/flow/login?response_type=code&flow=signup&client_id=abcdef&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-signup-uri"></a>
### SPiD.Uri.signup

Returns the signup URI.

```js
SPiD_Uri.signup(); //=> "https://identity-pre.schibsted.com/flow/signup?response_type=code&flow=signup&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.signup(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/flow/signup?response_type=code&flow=signup&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-logout-uri"></a>
### SPiD.Uri.logout

Returns the logout URI.

```js
SPiD_Uri.logout(); //=> "https://identity-pre.schibsted.com/logout?client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.logout(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/logout?client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-account-uri"></a>
### SPiD.Uri.account

Returns the account summary page URI.

```js
SPiD_Uri.account(); //=> "https://identity-pre.schibsted.com/account/summary?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.account(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/account/summary?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-purchase-history-uri"></a>
### SPiD.Uri.purchaseHistory

Returns the purchase history page URI.

```js
SPiD_Uri.purchaseHistory(); //=> "https://identity-pre.schibsted.com/account/purchasehistory?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.purchaseHistory(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/account/purchasehistory?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-subscriptions-uri"></a>
### SPiD.Uri.subscriptions

Returns the subscriptions page URI.

```js
SPiD_Uri.subscriptions(); //=> "https://identity-pre.schibsted.com/account/subscriptions?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.subscriptions(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/account/subscriptions?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-products-uri"></a>
### SPiD.Uri.products

Returns the products page URI.

```js
SPiD_Uri.products(); //=> "https://identity-pre.schibsted.com/account/products?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
SPiD_Uri.products(redirectUri, clientId); //=> "https://identity-pre.schibsted.com/account/products?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-redeem-voucher-uri"></a>
### SPiD.Uri.redeem

Returns the redeem voucher page URI. If `voucher_code` is supplied, the voucher
will automatically be redeemed when the URI is visited.

```js
SPiD_Uri.redeem();
//=> "https://identity-pre.schibsted.com/account/redeem?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var voucherCode = "12312314";
SPiD_Uri.redeem(voucherCode, redirectUri, clientId);
//=> "https://identity-pre.schibsted.com/account/redeem?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&voucher_code=12312314"
```

<a id="get-purchase-product-uri"></a>
### SPiD.Uri.purchaseHistory

Returns the purchase product page URI. If `product_id` is supplied, the URI will
lead to the "choose payment method" page, otherwise it will lead to the "choose
product" page.

```js
SPiD_Uri.purchaseHistory();
//=> "https://identity-pre.schibsted.com/flow/checkout?response_type=code&flow=payment&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var productId = "12312314";
SPiD_Uri.purchaseHistory(productId, redirectUri, clientId);
//=> "https://identity-pre.schibsted.com/flow/checkout?response_type=code&flow=payment&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&product_id=12312314"
```

<a id="get-purchase-product-uri"></a>
### SPiD.Uri.purchaseProduct

Returns the purchase product page URI. If `product_id` is supplied, the URI will
lead to the "choose payment method" page, otherwise it will lead to the "choose
product" page.

```js
SPiD_Uri.purchaseProduct();
//=> "https://identity-pre.schibsted.com/flow/checkout?response_type=code&flow=payment&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var productId = "12312314";
SPiD_Uri.purchaseProduct(productId, redirectUri, clientId);
//=> "https://identity-pre.schibsted.com/flow/checkout?response_type=code&flow=payment&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&product_id=12312314"
```

<a id="get-purchase-campaign-uri"></a>
### SPiD.Uri.purchaseCampaign(campaign_id, product_id, voucher_code redirect_uri, client_id)`

Get URI for purchase campaign page. If `campaign_id` is supplied, the URI will
take the user to the products in the campaign.

```js
SPiD_Uri.purchaseCampaign();
//=> "https://identity-pre.schibsted.com/flow/checkout?response_type=code&flow=payment&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var campaignId = "39843984";
var productId = "12312314";
var voucherCode = "89878966";
SPiD_Uri.purchaseCampaign(campaignId, productId, voucherCode, redirectUri, clientId);
//=> "https://identity-pre.schibsted.com/flow/checkout?response_type=code&flow=payment&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&campaign_id=39843984&product_id=12312314&voucher_code=89878966"
```
