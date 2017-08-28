:title GET /oauth/jwks

:aside

## See also

* [POST /oauth/token](/oauth/token/)
* [POST /oauth/ro](/oauth/ro/)
* [POST /oauth/introspect](/oauth/introspect/)
* [GET /oauth/userinfo](/oauth/userinfo/)
* [GET /oauth/authorize](/oauth/authorize/)

:body

SPiD publish the public RSA keys as a JSON Web Key (JWK) set. These web keys are
used to sign tokens, to enable applications to verify the signature and validity
of token without making a request to [/oauth/introspect](/oauth/introspect/).

The JWK and JWKS formats are defined by [RFC 7517](https://tools.ietf.org/html/rfc7517).

A list of libraries in different programming languages that implements
JWS can be found on [https://jwt.io](https://jwt.io).

## Request

```
GET /oauth/jwks
```

## Response

```js
HTTP/1.1 200 OK
Content-Length: 3952
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "keys": [
        {
            "kty": "RSA",
            "alg": "RS256",
            "kid": "91553059-9dc7-4612-9be9-ea38b7c6a128",
            "use": "sig",
            "n": "uUjYpiIg...MNEb7LJQ",
            "e": "AQAB"
        },
        {
            "kty": "RSA",
            "alg": "RS256",
            "kid": "43455776-8c74-4147-8635-8af3479358a5",
            "use": "sig",
            "n": "rCfVl0by...seOoZFnQ",
            "e": "AQAB"
        }
    ]
}
```
