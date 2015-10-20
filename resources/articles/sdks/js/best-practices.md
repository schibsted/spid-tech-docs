--------------------------------------------------------------------------------
:title Best practices
:category analytics
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js/events/)
- [Response signature and validation](/sdks/js/response-signature-and-validation/)
- [API Docs](/sdks/js/api-docs/)
- [Hosting](/sdks/js/hosting/)

## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

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
