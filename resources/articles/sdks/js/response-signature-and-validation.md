--------------------------------------------------------------------------------
:title Response signature and validation
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js/events/)
- [API Docs](/sdks/js/api-docs/)
- [Hosting](/sdks/js/hosting/)
- [Best practices](/sdks/js/best-practices/)


## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

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