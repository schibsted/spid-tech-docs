--------------------------------------------------------------------------------
:title JavaScript SDK
:frontpage
:category
:aside

## Table of Contents

<spid-toc></spid-toc>

## Read more about the JavaScript SDK

- [Events](/sdks/js-1.x/events/)
- [Response signature and validation](/sdks/js-1.x/response-signature-and-validation/)
- [API Docs](/sdks/js-1.x/api-docs/)
- [Hosting](/sdks/js-1.x/hosting/)
- [Best practices](/sdks/js-1.x/best-practices/)

## See also
- [Documentation for the current JavaScript SDK](/sdks/javascript/)
- [Behavior tracking with SPiD Pulse](/sdks/js-1.x/behavior-tracking-with-spid-pulse/)
- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body
**Note: this documentation is for the 1.x versions of the Javascript SDK. The current version can be found
[here](/sdks/javascript/).**

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
- https://login.schibsted.com for Swedish/International clients

#### Stage/Pre servers
- https://identity-pre.schibsted.com for stage/pre clients

`SPID_JSSDK_URI` will normally point to SPiD's servers. Read more about this
under [hosting](/sdks/js-1.x/hosting/). When using the SDK live from SPiD, the URL typically
looks like `https://login.schibsted.com/js/spid-sdk-{version}.min.js`,
e.g.
[https://login.schibsted.com/js/spid-sdk-1.7.9.min.js](https://login.schibsted.com/js/spid-sdk-1.7.9.min.js).
Go to [GitHub](https://github.com/schibsted/sdk-js/) to check what is the latest version.

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

## Checking and accepting agreements

The response session object contains a boolean field called `agreementAccepted`. If this field is false, you can make the user issue a request to `ajax/acceptAgreement` by pressing a button. If the `acceptAgreement` request is successful, the `auth.sessionChange` will fire, and the event response data will include the updated value for the `agreementAccepted` field.

```html
<script type="text/javascript" src="SPID_JSSDK_URI"></script>
<script type="text/javascript">
 VGS.Event.subscribe('auth.sessionChange', function(data) {
    var sess = data.session || {};
    if (sess.defaultAgreementAccepted && sess.clientAgreementAccepted) {
        return;
    }
    // show SPiD summary and/or client summary depending on booleans
});

$('.acceptButton').click(function() {
    var id = VGS.guid();
    VGS.callbacks[id] = function(data) { VGS.getLoginStatus(null, true); };
    VGS.Ajax.send('ajax/acceptAgreement.js?callback='+id);
});

//Initiate SDK
VGS.init({
    client_id: "YOUR_CLIENT_ID",
    server: "SPID_SERVER_URI"
    // Additional initialization (See below for a full list of available initialization options)
});
</script>
```

## Initialization options

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
During development you should set this to `false`, as it controls which session
cluster to use. Remember to set to `true` or remove this flag before deploying
to production.

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

##### track_anon_opt_out

By default the SPiD pulse part of the sdk will track anonymous users as well as logged
in users. If you want to disable tracking of anonymous users set this to `true`.
_This setting requires the pulse module._

##### track_custom_data

The SPiD pulse accepts custom data as a stringified JSON object. This data is sent to
the pulse server and stored into the database in raw format.
Example value: '{ "articleId": 1234, "section": "sport/football" }'
Default is `null`.
_This setting requires the pulse module._


## Auto-login usecase

This is a simple overview of Single Sign On using JS SDK, explaining the complete process between the client
service (yellow) and SPiD (blue).

![Single Sign On using JS SDK](/images/simple-sso-js-usecase.png)