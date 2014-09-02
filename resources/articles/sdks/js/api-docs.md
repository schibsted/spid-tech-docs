--------------------------------------------------------------------------------
:title API Docs
:category analytics
:aside

## At a glance

- [`VGS`](#vgs)
    - [`VGS.getLoginURI`](#get-login-uri)
    - [`VGS.getSignupURI`](#get-signup-uri)
    - [`VGS.getLogoutURI`](#get-logout-uri)
    - [`VGS.getAccountURI`](#get-account-uri)
    - [`VGS.getPurchaseHistoryURI`](#get-purchase-history-uri)
    - [`VGS.getSubscriptionsURI`](#get-subscriptions-uri)
    - [`VGS.getProductsURI`](#get-products-uri)
    - [`VGS.getRedeemVoucherURI`](#get-redeem-voucher-uri)
    - [`VGS.getPurchaseProductURI`](#get-purchase-product-uri)
    - [`VGS.getPurchaseCampaignURI`](#get-purchase-campaign-uri)
    - [`VGS.getLoginStatus`](#get-login-status)
    - [`VGS.hasProduct`](#has-product)
    - [`VGS.hasSubscription`](#has-subscription)
- [`VGS.Auth`](#auth)
    - [`VGS.Auth.logout`](#logout)
- [`VGS.Event`](#event)
    - [`VGS.Event.subscribers`](#subscribers)
    - [`VGS.Event.subscribe`](#subscribe)
    - [`VGS.Event.unsubscribe`](#unsubscribe)
    - [`VGS.Event.monitor`](#monitor)
    - [`VGS.Event.clear`](#clear)
    - [`VGS.Event.fire`](#fire)


## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js/events/)
- [Response signature and validation](/sdks/js/response-signature-and-validation/)
- [Hosting](/sdks/js/hosting/)
- [Best practices](/sdks/js/best-practices/)

## See also

- [Behavior tracking with SPiD Pulse](/sdks/js/behavior-tracking-with-spid-pulse/)
- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

<a id="api-docs"></a>

All arguments to the `*URI` functions are optional. When omitted, `client_id`
defaults to the `client_id` used in `VGS.init`, and `redirect_uri` defaults to
`window.location`.

<a id="vgs"></a>
For the following examples, assume the following variable declarations are
available:

```js
var redirectUri = "http://example.com";
var clientId = "4321abc0000";
```

<a id="get-login-uri"></a>
### VGS.getLoginURI

Returns the login URI.

```js
VGS.getLoginURI(); //=> "https://stage.payment.schibsted.no/login?response_type=code&flow=signup&client_id=4321abc0000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getLoginURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/login?response_type=code&flow=signup&client_id=abcdef&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-signup-uri"></a>
### VGS.getSignupURI

Returns the signup URI.

```js
VGS.getSignupURI(); //=> "https://stage.payment.schibsted.no/signup?response_type=code&flow=signup&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getSignupURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/signup?response_type=code&flow=signup&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-logout-uri"></a>
### VGS.getLogoutURI

Returns the logout URI.

```js
VGS.getLogoutURI(); //=> "https://stage.payment.schibsted.no/logout?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getLogoutURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/logout?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-account-uri"></a>
### VGS.getAccountURI

Returns the account summary page URI.

```js
VGS.getAccountURI(); //=> "https://stage.payment.schibsted.no/account/summary?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getAccountURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/account/summary?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-purchase-history-uri"></a>
### VGS.getPurchaseHistoryURI

Returns the purchase history page URI.

```js
VGS.getPurchaseHistoryURI(); //=> "https://stage.payment.schibsted.no/account/purchasehistory?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getPurchaseHistoryURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/account/purchasehistory?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-subscriptions-uri"></a>
### VGS.getSubscriptionsURI

Returns the subscriptions page URI.

```js
VGS.getSubscriptionsURI(); //=> "https://stage.payment.schibsted.no/account/subscriptions?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getSubscriptionsURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/account/subscriptions?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-products-uri"></a>
### VGS.getProductsURI

Returns the products page URI.

```js
VGS.getProductsURI(); //=> "https://stage.payment.schibsted.no/account/products?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"
VGS.getProductsURI(redirectUri, clientId); //=> "https://stage.payment.schibsted.no/account/products?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com"
```

<a id="get-redeem-voucher-uri"></a>
### VGS.getRedeemVoucherURI

Returns the redeem voucher page URI. If `voucher_code` is supplied, the voucher
will automatically be redeemed when the URI is visited.

```js
VGS.getRedeemVoucherURI();
//=> "https://stage.payment.schibsted.no/account/redeem?response_type=code&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var voucherCode = "12312314";
VGS.getRedeemVoucherURI(voucherCode, redirectUri, clientId);
//=> "https://stage.payment.schibsted.no/account/redeem?response_type=code&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&voucher_code=12312314"
```

<a id="get-purchase-product-uri"></a>
### VGS.getPurchaseProductURI

Returns the purchase product page URI. If `product_id` is supplied, the URI will
lead to the "choose payment method" page, otherwise it will lead to the "choose
product" page.

```js
VGS.getPurchaseProductURI();
//=> "https://stage.payment.schibsted.no/auth/start?response_type=code&flow=payment&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var productId = "12312314";
VGS.getPurchaseProductURI(productId, redirectUri, clientId);
//=> "https://stage.payment.schibsted.no/auth/start?response_type=code&flow=payment&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&product_id=12312314"
```

<a id="get-purchase-campaign-uri"></a>
### VGS.getPurchaseCampaignURI(campaign_id, product_id, voucher_code redirect_uri, client_id)`

Get URI for purchase campaign page. If `campaign_id` is supplied, the URI will
take the user to the products in the campaign.

```js
VGS.getPurchaseCampaignURI();
//=> "https://stage.payment.schibsted.no/auth/start?response_type=code&flow=payment&client_id=52f8e3d9efd04bb749000000&redirect_uri=http%3A%2F%2Flocalhost"

var campaignId = "39843984";
var productId = "12312314";
var voucherCode = "89878966";
VGS.getPurchaseCampaignURI(campaignId, productId, voucherCode, redirectUri, clientId);
//=> "https://stage.payment.schibsted.no/auth/start?response_type=code&flow=payment&client_id=4321abc0000&redirect_uri=http%3A%2F%2Fexample.com&campaign_id=39843984&product_id=12312314&voucher_code=89878966"
```

<a id="get-login-status"></a>
### VGS.getLoginStatus

```js
VGS.getLoginStatus(callback, force)
```

The callback is called with the response object, just like event subscribers.

To improve the performance of your application, not every call to check the
status of the user will result in a request to SPiD's servers. Where possible,
the response is cached. The first time in the current browser session that
`VGS.getLoginStatus` is called, or the JS SDK is initialized with
`status: true`, the response object will be cached by the SDK. Subsequent
calls to `VGS.getLoginStatus` will return data from this cached response.

This may cause problems if the user has logged in to (or out of) SPiD since the
last full session lookup. To get around this, you call `VGS.getLoginStatus` with
the second parameter set to `true` to force a roundtrip to SPiD - effectively
refreshing the cache of the response object.

```js
VGS.getLoginStatus(function(response) {
    // this will be called when the roundtrip to SPiD has completed
}, true);
```

If you call `VGS.getLoginStatus` on every page load, be careful not to force
every call, as it will significantly increase the number of requests to SPiD's
servers, and thus decrease the performance of your application and potentially
trigger rate limiting.

While you can call `VGS.getLoginStatus` any time (for example, when the user
tries to take an important action), most applications need to know the user's
status as soon as possible after the page loads. In this case, rather than
calling `VGS.getLoginStatus` explicitly, it is possible to check the user's
status by setting `status: true` when you call `VGS.init`.

If you check the user's status through `VGS.init`, you can subscribe to the
`auth.login` event. The response object passed to subscribers of this event is
identical to that which would be returned by calling `VGS.getLoginStatus`
explicitly.

<a id="has-product"></a>
### VGS.hasProduct

```js
VGS.hasProduct(product_id, callback, force)
```

Checks if user has access to a product. Set `force` to `true` to force a
roundtrip (e.g., bypass the cache). The callback will be called with an object,
whose `result` property will be `true` if the user has access to the product in
question.

```js
VGS.hasProduct(2134423, function (response) {
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
    };
});
```

<a id="has-subscription"></a>
### VGS.hasSubscription

```js
VGS.hasSubscription(product_id, callback, force)
```

Checks if user has access to a subscription. Set `force` to `true` to force a
roundtrip (e.g., bypass the cache). The callback will be called with an object,
whose `result` property will be `true` if the user has access to the product in
question.

```js
VGS.hasSubscription(324892374, function (response) {
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
    };
});
```

<a id="auth"></a>
<a id="logout"></a>
### VGS.Auth.logout

Logs the user out, and calls the callback when done.

```js
VGS.Auth.logout(function (data) {
    alert("See ya next time!");
});
```

<a id="event"></a>
<a id="subscribers"></a>
### VGS.Event.subscribers

Returns an object of all event subscribers, with event names as keys, and arrays
of functions (e.g. event handlers) as values.

```js
VGS.Event.subscribers()
// { auth.login: [function (data) {...}],
//   auth.logout: [function (data) {...}],
//   auth.sessionChange: [function (data) {...}],
//   VGS.loginException: [function () {...}]
// }
```

<a id="subscribe"></a>
### VGS.Event.subscribe

Register subscriber for event. The event handler function will be called
whenever the named event occurs.

```js
VGS.Event.subscribe("auth:login", function (data) {
    alert("Welcome!");
});
```

<a id="unsubscribe"></a>
### VGS.Event.unsubscribe

Remove previously subscribed function. Pass in the event name and the function
originally passed to `VGS.Event.subscribe`.

```js
var callback = function (data) {
    alert("Welcome!");
};

VGS.Event.subscribe("auth:login", callback);

// ...

VGS.Event.unsubscribe("auth:login", callback);
```

Note that it must be the same function reference, you cannot inline the function
in the `subscribe` call when you want to later unsubscribe it.

<a id="monitor"></a>
### VGS.Event.monitor

Monitor an event for some period of time. The callback is invoked immediately
when `monitor` is called, and then every time the event fires. When the callback
returns a truthy value, the monitor subscription is canceled. If the callback
returns a truthy value on its first execution, it will never be subscribed to
the event.

```js
// Listen to the initial monitor call and the following four events,
// then stop listening

var calls = 0;
VGS.Event.monitor("auth:login", function (data) {
    console.log(data);
    calls++;
    return calls === 5;
});
```

<a id="clear"></a>
### VGS.Event.clear

Clear all subscribers for an event.

```js
VGS.Event.clear("auth:login");
```

<a id="fire"></a>
### VGS.Event.fire

Fire an event. The first argument is the event name, additional arguments are
passed to subscribers. This function is used by the JS SDK to propagate events
to client subscribers, but is not recommended for external use.
