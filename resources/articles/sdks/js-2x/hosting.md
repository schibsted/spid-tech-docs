--------------------------------------------------------------------------------
:title Hosting
:category analytics
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js-2x/events/)
- [Response signature and validation](/sdks/js-2x/response-signature-and-validation/)
- [API Docs](/sdks/js-2x/api-docs/)
- [Best practices](/sdks/js-2x/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

The SDK can be retrieved from Schibsted account CDN servers or your own servers.

## Local usage
To use a local copy, you can

* Download from [releases](https://github.com/schibsted/sdk-js/releases)
* Clone [repository](https://github.com/schibsted/sdk-js/) and run `grunt`
* Install the package [spid-sdk-js](https://www.npmjs.com/package/spid-sdk-js) via [npm](https://www.npmjs.com/)

## CDN hosting
All version of the SDK is available on our CDN servers.
The 2.x.x version are available as AMD, CommonJs and regular global variable version.

Files are available on http://cdn.spid.se/sdk/spid-sdk-{version_number}-{type}(.min).js
or https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-{version_number}-{type}(.min).js.

Examples

* http://cdn.spid.se/sdk/spid-sdk-2.1.0-amd.js
* http://cdn.spid.se/sdk/spid-sdk-2.1.0-amd.min.js
* http://cdn.spid.se/sdk/spid-sdk-2.1.0-commonjs2.js
* http://cdn.spid.se/sdk/spid-sdk-2.1.0-commonjs2.min.js
* http://cdn.spid.se/sdk/spid-sdk-2.1.0-var.js
* http://cdn.spid.se/sdk/spid-sdk-2.1.0-var.min.js

or

* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-2.1.0-amd.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-2.1.0-amd.min.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-2.1.0-commonjs2.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-2.1.0-commonjs2.min.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-2.1.0-var.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-sdk-2.1.0-var.min.js


There also an additional file including URI generating functions that is optional to use.

* http://cdn.spid.se/sdk/spid-uri-2.1.0-amd.js
* http://cdn.spid.se/sdk/spid-uri-2.1.0-commonjs2.js
* http://cdn.spid.se/sdk/spid-uri-2.1.0-var.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-uri-2.1.0-amd.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-uri-2.1.0-commonjs2.js
* https://d3iwtia3ndepsv.cloudfront.net/sdk/spid-uri-2.1.0-var.js

### Why https hosting is on cloudfront url

cdn.spid.se is just a CNAME for that cloudfront distribution.
You may use the same cloudfront url on http too.

To use cdn.spid.se with https, we need to use SNI, which is not supported in some browsers (read XP).
There are some alternative solutions, but for the time being the https hosting url are those.
Any changes to this will be announced properly.
