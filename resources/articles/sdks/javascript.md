--------------------------------------------------------------------------------
:title JavaScript SDK
:frontpage
:aside

## On this page

- [Loading and initialization](#loading-and-initializing)
- [Events](#events)
- [Response signature and validation](#response-signature-and-validation)
- [API Docs](#api-docs)
- [Hosting](#hosting)
- [Best practices](#best-practices)


## See also

- [Getting started with the server-side API](/getting-started/)

:body

The JavaScript SDK is different from the other SDKs in that it only provides
information about the user's authentication/authorization status. It is not a
general purpose SDK to make arbitrary requests against the API. A Node SDK is
being developed to fill this role for server-side JavaScript.

## Loading and initializing

The following code will load and initialize the JavaScript SDK with the most
common options. Replace `YOUR_CLIENT_ID`, `SPID_JSSDK_URI` and `SPID_SERVER_URI`
with the appropriate values.

`SPID_SERVER_URI` is dependent on the SPiD server you will be using.

#### Production servers

- https://payment.schibsted.no for Norwegian clients
- https://payment.schibsted.se for Swedish clients

#### Stage servers
- https://stage.payment.schibsted.no for Norwegian stage clients
- https://stage.payment.schibsted.se for Swedish stage clients

`SPID_JSSDK_URI` will normally point to SPiD's servers. Read more about this
under [hosting](#hosting). When using the SDK live from SPiD, the URL typically
looks like `https://payment.schibsted.(se|no)/js/spid-sdk-{version}.min.js`,
e.g.
[https://payment.schibsted.no/js/spid-sdk-1.7.1.min.js](https://payment.schibsted.no/js/spid-sdk-1.7.1.min.js).

### Synchronously loading the JS SDK

Place the following code right before the closing `</body>` tag on your page:

```html
<script type="text/javascript" src="SPID_JSSDK_URI"></script>
<script type="text/javascript">
// Add event subscribers
VGS.Event.subscribe('auth.login', function(data) { console.log(data); });
VGS.Event.subscribe('auth.logout', function(data) { console.log(data); });
VGS.Event.subscribe('auth.sessionChange', function(data) { console.log(data); });

//Initiate SDK
VGS.init({
    client_id: "YOUR_CLIENT_ID",
    server: "SPID_SERVER_URI"
    // Additional initialization (See below for a full list of available initialization options)
});
</script>
```

### Asynchronously loading the JS SDK

By loading the SPiD SDK asynchronously it will not block loading other elements
on your page. The function assigned to window.vgsAsyncInit is run as soon as the
SDK source has finished loading. Any code you want to run after the SDK is
loaded should be placed within this function. Place the following code right
before the closing `</body>` tag.

```html
<script>
window.vgsAsyncInit = function() {
    // Add event subscribers
    VGS.Event.subscribe('auth.login', function(data) { console.log(data); });
    VGS.Event.subscribe('auth.logout', function(data) { console.log(data); });
    VGS.Event.subscribe('auth.sessionChange', function(data) { console.log(data); });

    //Initiate SDK
    VGS.init({
        client_id: "YOUR_CLIENT_ID",
        server: "SPID_SERVER_URI"
        // Additional initialization (See below for a full list of available initialization options)
    });
};

// Load the SDK's source Asynchronously
(function(d){
    var js, id = 'spid-jssdk', ref = d.getElementsByTagName('script')[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement('script'); js.id = id; js.async = true;
    js.src = "SPID_JSSDK_URI";
    ref.parentNode.insertBefore(js, ref);
}(document));
</script>
```

Since version 1.5.0, the SDK is loaded minified by default, for optimal network
performance.

### Initialization options

##### client_id

Your client ID. **Required**

##### server

URL to the SPiD server. **Required**

##### cookie

Set to `true` to enable cookie support. Default value is `true`.

##### logging

Set to `true` to enable logging. Default value is `false`. When logging is
enabled, the SDK will output debug information to the console. NOTE: it uses
`console.log()` so your browser needs to support it, otherwise an error will be
thrown.

##### session

If you want to handle/save the session data on your own, the cookie option can
be turned off and the session re-initialized by inserting a saved session into
the session option. This way, you can cache and process the session in your
preferred way. This is disabled by default.

##### status

Status is `false` by default, which means that the session is initialized only
when necessary, otherwise cached by the cookie (if `true`) or session options.
If status is `true`, a session check will be done every time the SDK is
initialized, returning a fresh session state on each page request. Use with
care.

##### prod

Set to `true` to fetch from the live production server. Defaults to `true`.
Typically, you only want to include this option (set to `false`) for debugging
purposes.

##### varnish_expiration

Varnish cookie expiration, in seconds. Defaults to the same as the session
expiration.

##### timeout

This is the default connection timeout in milliseconds used when waiting for
response by the SPiD servers. Defaults to 5 seconds (5000 milliseconds).

##### refresh_timeout

This option specifies how often the session is refreshed and retrieved from
SPiD, in milliseconds. Default is every 15 minutes (900000 milliseconds). This
is configurable down to 1 minute (60000 milliseconds). We encourage clients to
use the default unless it is necessary to change it, like in a single-page app
where page refreshes are very infrequent. A fresh session can also be retrieved
on demand by calling `VGS.getLoginStatus()` with the `forced` option set to
`true`.

##### cache

By default the SDK will cache responses from SPiD for functions that check
product and subscription access (`hasProduct`, `hasSubscription`). It uses the
`refresh_timeout` value for invalidating the cache. This is default `true` and
the purpose is to minimize requests to SPiD and performance gains. These
responses rarely change.

##### cache_notloggedin

By default the SDK will store the logged in user session in cookie. With this
option set to `true`, it will also store the not logged in 'session', to avoid
requests on every page load for non logged-in users. However, the effect of this
is that when a user logs in, they will not get an updated session until the
cookie expires, which is set to the `refresh_timeout` option. To use this
option, you will need to manually call `VGS.getLoginStatus()` when the user
comes back to your site after login.

##### track_throttle

For use with tracker. A float between 0 and 1. Default value is 1.

## Events

Subscribing to the authentication events fired by the JS SDK means your
application will be notified if the user's session state changes. This is
important because the session state may change due to user interactions beyond
your application's control. The only way your application can be notified of
these changes is through subscribing to these events.

Using `VGS.Event.subscribe("event name", callback)` attaches an event handler
that will be invoked when the event fires. When an event is fired all
subscribers to the event will be notified and given the response object.

[The example repository](https://github.com/schibsted/spid-js-examples/tree/master/getting-started)
contains a fully working example. Open index.html in your browser, and it will
reveal your own login status, and allow you to log in/out. The JavaScript
powering this example looks like the following.

```javascript
VGS.Event.subscribe("auth.login", function (data) { console.log("auth.login", data); });
VGS.Event.subscribe("auth.logout", function (data) { console.log("auth.logout", data); });

VGS.Event.subscribe("auth.sessionChange", function (data) {
    console.log("auth.sessionChange", data);
    var output = document.getElementById("spid");

    if (!data.session) {
        output.innerHTML = "Welcome. Please <a href=\"" + VGS.getLoginURI() + "\">log in</a>";
    } else {
        output.innerHTML = "Welcome <a href=\"" + VGS.getAccountURI() + "\">" +
            data.session.displayName + "</a>" +
            " <a href=\"" + VGS.getLogoutURI() + "\">Log out</a>";
    }
});

VGS.init({
    client_id: "52f8e3d9efd04bb749000000",
    server: "stage.payment.schibsted.no",
    prod: false
});
```

The `prod` property should be set to `true` (or omitted) when running against
the production server.

### Available SDK events are:

<table class="table table-hover">
  <thead>
    <tr>
      <th>Name</th>
      <th>Fired when ...</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>auth.notLoggedin</code></td>
      <td>There is no session on page load, user is not logged in</td>
    </tr>
    <tr>
      <td><code>auth.statusChange</code></td>
      <td>Page loads, status changes from unknown to Connected or notConnected</td>
    </tr>
    <tr>
      <td><code>auth.login</code></td>
      <td>
        <ul>
          <li>The user logs in somewhere else</li>
          <li>Page loads, user is already logged in</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td><code>auth.logout</code></td>
      <td>User logs out (either by you or another site (SPiD, other client).</td>
    </tr>
    <tr>
      <td><code>auth.userChange</code></td>
      <td>User in session has changed to another user</td>
    </tr>
    <tr>
      <td><code>auth.sessionInit</code></td>
      <td>Session is successfully initiated for the first time, on page load</td>
    </tr>
    <tr>
      <td><code>auth.sessionChange</code></td>
      <td>Always. This is a wrapping event, which is fired as a result of all other events that changes the session.</td>
    </tr>
    <tr>
      <td><code>auth.visitor</code></td>
      <td>SPiD identifies the current visitor. Yields a unique visitor id that can be used to track the user even when not logged in. Used in analytics (Mixpanel) tracking, etc</td>
    </tr>
    <tr>
      <td><code>VGS.error</code></td>
      <td>An error occurred. Like communication timeouts, invalid responses or abuse (too many requests in a short time period).</td>
    </tr>
  </tbody>
</table>

Depending on the user's actions, multiple events may be fired.

#### Page loads, user logged in

- `auth.login`
- `auth.statusChange`
- `auth.sessionInit`
- `auth.sessionChange`

#### Page loads, user not logged in
- `auth.notLoggedin`
- `auth.sessionChange`

#### Polling, and there is no change

- `auth.sessionChange`

The change in session are the fields `clientTime` and `serverTime` for
synchronization between client and server.

#### User logs out

- `auth.logout`
- `auth.statusChange`
- `auth.sessionChange`

#### User logs out, and logs in with another user account

- `auth.logout`
- `auth.userChange`
- `auth.login`
- `auth.sessionChange`

### Response Object

The response object is different, whether user is logged in or not.

Response, when logged in

```js
{
    session: {
        baseDomain: "sdk.dev"
        clientTime: 1360329602301
        displayName: "Anna Andersson"
        expiresIn: 7138
        familyName: "Andersson"
        gender: "undisclosed"
        givenName: "Anna"
        id: "50604a7ddcb114ed0e000004"
        photo: "https://secure.gravatar.com/avatar/ec32937c22d1a4b1474657b776d0f398?s=200"
        result: true
        serverTime: 1360329601
        userId: 270177
        userStatus: "connected"
        sig: "9toJgvXfPgk-5W2162sD8ueHzZ8Ya1ibBWvELv-I-lk.eyJyZXN1bHQiOnRydWUsInNlcnZlclRpbWUiOjEzNjAzMjk2MDEsInVzZXJTdGF0dXMiOiJjb25uZWN0ZWQiLCJ1c2VySWQiOjI3MDE3NywiaWQiOiI1MDYwNGE3ZGRjYjExNGVkMGUwMDAwMDQiLCJkaXNwbGF5TmFtZSI6IkpvYWtpbSBXYW5nZ3JlbiIsImdpdmVuTmFtZSI6IkpvYWtpbSIsImZhbWlseU5hbWUiOiJXYW5nZ3JlbiIsImdlbmRlciI6InVuZGlzY2xvc2VkIiwicGhvdG8iOiJodHRwczpcL1wvc2VjdXJlLmdyYXZhdGFyLmNvbVwvYXZhdGFyXC9lYzMyOTM3YzIyZDFhNGIxNDc0NjU3Yjc3NmQwZjM5OD9zPTIwMCIsImV4cGlyZXNJbiI6NzEzOCwiYmFzZURvbWFpbiI6InNkay5kZXYiLCJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiJ9"
    },
    status: "connected"
}
```

Response, when not logged in

```js
{
    session: null,
    status: "unknown"
}
```

## Response signature and validation

The sig parameter can be used to verify that the response came from SPiD. This
can be done serverside by the client, using the client signature secret. Without
this secret, third parties cannot modify the `signed_request` string without
also invalidating its contents.

The `sig` parameter is a concatenation of an HMAC SHA-256 signature string, a dot
(.) and a base64url encoded JSON object (session). It looks like this:

```text
vlXgu64BQGFSQrY0ZcJBZASMvYvTHu9GQ0YM9rjPSso.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsIjAiOiJwYXlsb2FkIn0
```

Read more about [signed responses](/endpoints/#signed-responses).

--------------------------------------------------------------------------------
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

:heading API Docs
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

## Hosting

By default, the SDK will be loaded from SPiD's servers. You may host it in your
own environment during testing and by request when in production. If the cookie
option is set to `true`, the cookies will be created for your own domain.

Loading the JS SDK directly from SPiD is recommended for performance reasons.
This way, the SDK is cached across clients, meaning that users that have
previously visited other clients may not need to download the SDK for your site.

To use a local copy of the SDK, download the
[latest version from GitHub](https://github.com/schibsted/sdk-js/tree/master/dist).
Note that both the unminified and minified versions bundle the
[JSON2.js](https://github.com/douglascrockford/JSON-js) script. If you do not
need it (e.g., you don't need support for <= IE7), download the source code and
build it yourself. Refer to the
[JS SDK Readme](https://github.com/schibsted/sdk-js) for how to do this.

## Best practices

#### Loading the Javascript SDK
You can load the SDK either synchronously or asynchronously. Depending on your use case, page size and functionality requirements, how you load the SDK may affect how the user perceives their logged in state. Pages with a lot of elements and external javascript may cause the loading and initiation of the SDK to be slow and the user may perceive himself as NOT logged in during that period. We suggest that, if you are experiencing this, to load the SDK synchronously and before all other unnecessary requests for page resources.

#### Initializing the Javascript SDK
The SDK has many initialization options that directly affect how the SDK behaves in regards to cookies, caching, timeouts and session state handling. Deciding on the correct options and values is extremely important given the use cases you are trying to solve for your application. 

There are some best practices and gotchas in regards to these use cases:

### Hybrid mobile apps
Hybrid mobile apps are native mobile applications that have implemented the native SPiD login API for authentication and serves authenticated content that uses the JS SDK. This solution requires the backend solution that serves this content to know who is logged in. It also implements product access checks that use the JS SDK. 

This implementation doesn't support the auto-logged-in functionality built-in by SPiD in the SDK between each time the user start the app, meaning that after each application start, the application must exchange a session token via the authentication API and then initiate a session for that user in SPiD. Suggested JS SDK initialization for this type of applications are:
##### cache_notloggedin should be set to FALSE
Because the user wont be served this content without having a session, there is no need to cache the logged out state.
### Apps with Varnish support
Apps that have implemented the varnish product access check functionality. This feature uses the JS SDK to set a custom Varnish cookie for the logged in user in the client's domain. This cookie is then sent on every request and intercepted by Varnish, which in turn performs product access checks for the user through a custom SPiD API endpoint. Suggested JS SDK initialization for this type of applications are:
##### cookie: TRUE & varnish_expiration: 28800
This settings enables cookie support and sets the varnish cookie expiration to around 8 hours, which supports basic usage of a paywall enabled application.
