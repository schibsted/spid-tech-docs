--------------------------------------------------------------------------------
:title Hosting
:category analytics
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript-1x/)
- [Events](/sdks/js-1x/events/)
- [Response signature and validation](/sdks/js-1x/response-signature-and-validation/)
- [API Docs](/sdks/js-1x/api-docs/)
- [Best practices](/sdks/js-1x/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

**Note: this documentation is for the 1.x versions of the JavaScript SDK. The current version can be found
[here](/sdks/javascript/).**

By default, the SDK will be loaded from Schibsted account's servers. You may host it in your
own environment during testing and by request when in production. If the cookie
option is set to `true`, the cookies will be created for your own domain.

Loading the JS SDK directly from Schibsted account is recommended for performance reasons.
This way, the SDK is cached across clients, meaning that users that have
previously visited other clients may not need to download the SDK for your site.

To use a local copy of the SDK, download the
[latest version from GitHub](https://github.com/schibsted/sdk-js/).
Note that both the unminified and minified versions bundle the
[JSON2.js](https://github.com/douglascrockford/JSON-js) script. If you do not
need it (e.g., you don't need support for <= IE7), download the source code and
build it yourself. Refer to the
[JS SDK Readme](https://github.com/schibsted/sdk-js) for how to do this.
