--------------------------------------------------------------------------------
:title Response signature and validation
:category analytics
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript-1.x/)
- [Events](/sdks/js-1.x/events/)
- [API Docs](/sdks/js-1.x/api-docs/)
- [Hosting](/sdks/js-1.x/hosting/)
- [Best practices](/sdks/js-1.x/best-practices/)

## See also

- [Behavior tracking with SPiD Pulse](/sdks/js-1.x/behavior-tracking-with-spid-pulse/)
- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

**Note: this documentation is for the 1.x versions of the Javascript SDK. The current version can be found
[here](/sdks/javascript/).**

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
