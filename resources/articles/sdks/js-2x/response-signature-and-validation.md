--------------------------------------------------------------------------------
:title Response signature and validation
:category analytics
:aside

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Events](/sdks/js-2x/events/)
- [API Docs](/sdks/js-2x/api-docs/)
- [Hosting](/sdks/js-2x/hosting/)
- [Best practices](/sdks/js-2x/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)

:body

The sig parameter can be used to verify that the response came from Schibsted account. This
can be done serverside by the client, using the client signature secret. Without
this secret, third parties cannot modify the `signed_request` string without
also invalidating its contents.

The `sig` parameter is a concatenation of an HMAC SHA-256 signature string, a dot
(.) and a base64url encoded JSON object (session). It looks like this:

```text
vlXgu64BQGFSQrY0ZcJBZASMvYvTHu9GQ0YM9rjPSso.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsIjAiOiJwYXlsb2FkIn0
```

In PHP, you can validate `sig` with:

```php
<?php
// $sign_secret = "a274de";
$expected_sig = hash_hmac('sha256', $container['data'], $client_sign_secret, true);

if (hash_equals($signature, $expected_sig)) {
    echo "Authenticity of data verified\n";
} else {
    echo "Authenticity of data cannot be verified. Someone is doing something naughty!\n";
}
```

Read more about [signed responses](/endpoints/#signed-responses).
