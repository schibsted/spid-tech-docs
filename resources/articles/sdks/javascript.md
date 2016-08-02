--------------------------------------------------------------------------------
:title JavaScript SDK
:frontpage
:category
:aside

## Table of Contents

<spid-toc></spid-toc>

## Read more about the JavaScript SDK

- [Events](/sdks/js/events/)
- [Response signature and validation](/sdks/js/response-signature-and-validation/)
- [API Docs](/sdks/js/api-docs/)
- [Hosting](/sdks/js/hosting/)
- [Best practices](/sdks/js/best-practices/)

## See also
- [Documentation for the 1.x javascript SDK](/sdks/javascript-1x/)
- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

**Note: this documentation is for the current version of the Javascript SDK. The old 1.x version can be found
[here](/sdks/javascript-1x/).**

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

`SPID_JSSDK_URI` will normally point to SPiD's CDN or your own hosting. Read more about this
under [hosting](/sdks/js/hosting/).

### SDK variants

The SDK is currently built in three distribution variants: one for users using the *AMD* module format, one for
*CommonJS* users, and one for vanilla JS users. The third SDK variant will set a global variable named `SPiD`
(note the capitalisation).

### Synchronously loading the JS SDK

Place the following code right before the closing `</body>` tag on your page:

```html
<script type="text/javascript" src="SPID_JSSDK_URI"></script>
<script type="text/javascript">
// Add event subscribers
SPiD.event.subscribe('SPiD.login', function(data) { console.log(data); });
SPiD.event.subscribe('SPiD.logout', function(data) { console.log(data); });
SPiD.event.subscribe('SPiD.sessionChange', function(data) { console.log(data); });

//Initiate SDK
SPiD.init({
    client_id: "YOUR_CLIENT_ID",
    server: "SPID_SERVER_URI"
    // Additional initialization (See below for a full list of available initialization options)
});
// Check session
SPiD.hasSession();

</script>
```

### Asynchronously loading the JS SDK

By loading the SPiD SDK asynchronously it will not block loading other elements
on your page. The function assigned to `window.asyncSPiD` is run as soon as the
SDK source has finished loading. Any code you want to run after the SDK is
loaded should be placed within this function. Place the following code right
before the closing `</body>` tag.

```html
<script>
window.asyncSPiD = function() {
    // Add event subscribers
    SPiD.event.subscribe('SPiD.login', function(data) { console.log(data); });
    SPiD.event.subscribe('SPiD.logout', function(data) { console.log(data); });
    SPiD.event.subscribe('SPiD.sessionChange', function(data) { console.log(data); });

    //Initiate SDK
    SPiD.init({
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

The response session object contains two boolean fields called `defaultAgreementAccepted` and `clientAgreementAccepted`.
If one of those fields is __false__, you can have the SDK issue a call to the relevant endpoint (i.e. `ajax/acceptAgreement`) when,
for example, the user presses a button. If the `acceptAgreement` request is successful, the `auth.sessionChange`
will fire, and the event response data will include the updated values for the `defaultAgreementAccepted` and `clientAgreementAccepted` fields.

```html
<script type="text/javascript" src="SPID_JSSDK_URI"></script>
<script type="text/javascript">
 SPiD.event.subscribe('SPiD.sessionChange', function(data) {
    var sess = data.session || {};
    if (sess.defaultAgreementAccepted && sess.clientAgreementAccepted) {
        return;
    }
    // show SPiD summary and/or client summary depending on booleans
});

$('.acceptButton').click(function() {
    SPiD.acceptAgreement(function (){ alert('Agreement accepted'});
});

//Initiate SDK
SPiD.init({
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

##### logging

Set to `true` to enable logging. Default value is `false`. When logging is
enabled, the SDK will output debug information to the console. NOTE: it uses
`console.log()` so your browser needs to support it, otherwise an error will be
thrown.

##### useSessionCluster

Controls which session endpoint to use. Defaults to true. During development you should consider setting this to false.
Remember to set to true or remove this flag before deploying to production. This config option used to be called ”prod”.

##### varnish_expiration

Varnish cookie expiration, in seconds. Defaults to the same as the session expiration.

##### setVarnishCookie

Boolean flag to determine if a varnish cookie should be set. Varnish cookie will be set unless this is set to false.

##### timeout

This is the default connection timeout in milliseconds used when waiting for response by the SPiD servers.
Defaults to 5 seconds (5000 milliseconds).

##### refresh_timeout

This option specifies how often the session is refreshed and retrieved from SPiD, in milliseconds.
Default is every 15 minutes (900000 milliseconds). This is configurable down to 1 minute (60000 milliseconds).
We encourage clients to use the default unless it is necessary to change it, like in a single-page app
where page refreshes are very infrequent.

##### cache

By default the SDK will cache responses from SPiD for functions that check product and subscription access
(hasProduct, hasSubscription). It uses the refresh_timeout value for invalidating the cache.
This is default true and the purpose is to minimize requests to SPiD and performance gains.
These responses rarely change.

##### storage

Set to ’cookie’ to store the session as a cookie. Set to ’localstorage’ to store the session in local storage.
Set to `false` to disable session storage altogether. The default value is ’localstorage’.

## Auto-login usecase

This is a simple overview of Single Sign On using JS SDK, explaining the complete process between the client
service (yellow) and SPiD (blue).

![Single Sign On using JS SDK](/images/simple-sso-js-usecase.png)
