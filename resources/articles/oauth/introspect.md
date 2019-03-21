:title POST /oauth/introspect

:aside

## See also

* [POST /oauth/token](/oauth/token/)
* [POST /oauth/register](/oauth/register/)
* [POST /oauth/ro](/oauth/ro/)
* [GET /oauth/userinfo](/oauth/userinfo/)
* [GET /oauth/jwks](/oauth/jwks/)
* [GET /oauth/authorize](/oauth/authorize/)
* [Server to Server Auth](/s2s-auth/)
* [Token introspection](/token-introspection/)

:body

The `/oauth/introspect` endpoint is used to verify a token and to translate it into claims.
Introspection is defined in [RFC 7662](https://tools.ietf.org/html/rfc7662).

## Request

```
POST /oauth/introspect
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

token=eyJ0eXAiOiJK...jUaR-nZOx5MGg
```

## Response

### Request with valid, non-expired token

```js
HTTP/1.1 200 OK
Content-Length: 622
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3",
    "aud": [
        "http://example.com"
    ],
    "scope": "profile address email phone",
    "iss": "https://login.schibsted.com/",
    "active": true,
    "token_type": "Bearer",
    "exp": 1503928222,
    "iat": 1503924622,
    "client_id": "4e8463569caf7ca019000007",
    "jti": "45e73931-fcd6-4260-b5eb-51286c781e53"
}
```

### Request with invalid token

Invalid token can be an expired token, a token not intended for the introspecting
client, or a non JWT token.

```js
HTTP/1.1 200 OK
Content-Length: 230
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "active": false
}
```

### Failure cases

* **400 Bad Request** <span class="faded">Missing token</span>
* **401 Unauthorized** <span class="faded">Invalid client credentials</span>
