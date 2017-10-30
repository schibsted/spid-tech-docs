:title GET /oauth/userinfo

:aside

## See also

* [POST /oauth/token](/oauth/token/)
* [POST /oauth/ro](/oauth/ro/)
* [POST /oauth/introspect](/oauth/introspect/)
* [GET /oauth/jwks](/oauth/jwks/)
* [GET /oauth/authorize](/oauth/authorize/)

:body

The `/oauth/userinfo` endpoint allows an application to fetch user data in
OpenID [Standard Claims](http://openid.net/specs/openid-connect-core-1_0.html#StandardClaims)
format, as defined by the [OpenID Connect specification](http://openid.net/specs/openid-connect-core-1_0.html).

What data is returned depends on the scopes in the token. There are 4 scopes that you may request, in addition to the
required `openid` scope, when getting a token:

* `profile`
* `email`
* `phone`
* `address`
 
The scopes are requested as part of the authorization code
flow, the password grant flow, or the `oauth/ro` flow.

## Request

```
GET /oauth/userinfo
Authorization: Bearer eyJ0eXAiOiJK...jUaR-nZOx5MGg
```

## Response

```js
HTTP/1.1 200 OK
Content-Length: 764
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3",
    "name": "John Doe",
    "given_name": "John",
    "family_name": "Doe",
    "preferred_username": "johnnyDoey",
    "picture": "https://secure.gravatar.com/avatar/a41dadb0ace224188c7b830116dc2f5c?s=200",
    "gender": "male",
    "birthdate": "1984-04-01",
    "locale": "en-US",
    "updated_at": 1503667376,
    "email": "john.doe@example.com",
    "email_verified": true,
    "phone_number": "+155555555",
    "phone_number_verified": false,
    "address": {
        "formatted": "1 Roadster st.",
        "street_address": "1 Roadster st., 1111, The Moon, US",
        "locality": "The Moon",
        "postal_code": "1111",
        "country": "US"
    }
}
```

### Failure cases

* **401 Unauthorized** <span class="faded">Invalid, expired or malformed token</span>
* **403 Forbidden** <span class="faded">Missing 'openid' scope in token</span>

### Scopes

Scopes can be combined into space separated strings. To get all fields, you need to ask for
`openid profile email phone address`.

**Scope: openid**

```js
{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3"
}
```

**Scope: profile**

```js
{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3",
    "name": "John Doe",
    "given_name": "John",
    "family_name": "Doe",
    "preferred_username": "johnnyDoey",
    "picture": "https://secure.gravatar.com/avatar/a41dadb0ace224188c7b830116dc2f5c?s=200",
    "gender": "male",
    "birthdate": "1984-04-01",
    "locale": "en-US",
    "updated_at": 1503667376
}
```

**Scope: email**

```js
{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3",
    "email": "john.doe@example.com",
    "email_verified": true
}
```

**Scope: phone**

```js
{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3",
    "phone_number": "+155555555",
    "phone_number_verified": false
}
```

**Scope: address**

```js
{
    "sub": "5d75167d-8841-5072-89cb-985915e2dbb3",
    "address": {
        "formatted": "1 Roadster st.",
        "street_address": "1 Roadster st., 1111, The Moon, US",
        "locality": "The Moon",
        "postal_code": "1111",
        "country": "US"
    }
}
```
