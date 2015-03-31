--------------------------------------------------------------------------------
:title Android SDK
:frontpage
:category
:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- Android
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Mobile review checklist](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

The Android SDK is built on top of the backend SDK to provide access to functions with minimal effort from 
the developer. Connecting to the SPiD can be done either natively, using a webview, browser or connecting via
Facebook or Google+.

#### SPiDClient

All communication with SPiD goes through the SPiDClient class, which is implemented as a singleton. The singleton instance is instantiated 
automatically, but before it can be used it must be configured with a call to <code>SPiDClient.configure(SPiDConfiguration config)</code>.

The following fields in the configuration must be set:

- `clientID` The client's id
- `clientSecret` The client's private secret
- `appURLScheme` The app URL scheme
- `serverURL` The SPiD server to connect to
- `context` The application context

An example configuration how to configure the SPiDClient would be

```
SPiDConfiguration config = new SPiDConfigurationBuilder()
        .clientID("your-client-id")
        .clientSecret("your-client-secret")
        .appURLScheme("spidmobile_<your-client-id>")
        .serverURL("your-spidserver-url")
        .context(getApplicationContext())
        .build();
SPiDClient.getInstance().configure(config);
```

## Connecting to SPiD

The recommended way to connect to SPiD is to use a browser, see explanation [why](/mobile/oauth-authentication-on-mobile-devices/).
However it can be done a number of ways, which is explained in greater detail below.

#### Connecting via browser

TODO: Add flow diagrams?

#### Connecting via webview

#### Connecting via native

#### Connecting via Google+

#### Connecting via Facebook
