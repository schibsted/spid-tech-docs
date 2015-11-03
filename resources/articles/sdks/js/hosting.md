--------------------------------------------------------------------------------
:title Hosting
:category analytics
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js/events/)
- [Response signature and validation](/sdks/js/response-signature-and-validation/)
- [API Docs](/sdks/js/api-docs/)
- [Best practices](/sdks/js/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

By default, the SDK will be loaded from SPiD's servers. You may host it in your
own environment during testing and by request when in production. If the storage
option is set to `cookie`, the cookies will be created for your own domain.

Loading the JS SDK directly from SPiD is recommended for performance reasons.
This way, the SDK is cached across clients, meaning that users that have
previously visited other clients may not need to download the SDK for your site.

To use a local copy of the SDK, download the
[latest release from GitHub](https://github.com/schibsted/sdk-js/) or install it via NPM.