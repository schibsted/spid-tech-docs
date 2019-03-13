:title POST /oauth/token

:aside

## See also

* [POST /oauth/register](/oauth/register/)
* [POST /oauth/ro](/oauth/ro/)
* [POST /oauth/introspect](/oauth/introspect/)
* [GET /oauth/userinfo](/oauth/userinfo/)
* [GET /oauth/jwks](/oauth/jwks/)
* [GET /oauth/authorize](/oauth/authorize/)
* [SchAcc Authentication](/authentication/)

:body

The `/oauth/token` endpoint is used to issue tokens for users and services.
"User tokens" are issued for users, while "client tokens" are issued
for services. In SchAcc there is also 2 more token variants called admin tokens
and admin client tokens, but these tokens are only used by internal
applications like Ambassador or SelfService.

SchAcc currently supports 5 grant_types:

* **client_credentials** to fetch a client token
* **password** to fetch a user token, given username and password
* **authorization_code** to exchange a one time code for a user token
* **refresh_token** to exchange a refresh token for a new user access token
* **urn:ietf:params:oauth:grant-type:jwt-bearer** to login with Facebook and Google+

All supported grant types require [client authentication](/authentication/).
Client authentication methods supported are via the `authorization` header in the form of
Basic Auth, and via `client_id` and `client_secret` as part of the POST body.
The recommended method is via the `authorization` header.

## Grant type Client credentials, for server tokens

Server tokens are generally more powerful and should be kept secret. In addition
to the parameters `grant_type` and `scope`, a third parameter called `resource`
is allowed, to support cross merchant introspection and set the intended
audience of the token. See [Specifying a resource indicator](http://techdocs.spid.no/authentication/).

### Request

```
POST /oauth/token
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials&scope=openid%20profile
```

### Response

```js
HTTP/1.1 200 OK
Content-Length: 2034
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "expires_in": 900,
    "scope": "openid profile",
    "token_type": "Bearer",
    "access_token": "eyJ0eXAiOi...Q5MffoMPvA",
}
```

## Grant type Password, for user tokens

This grant is mainly only used from native mobile apps, and is not permitted
by any other application.

### Request

```
POST /oauth/token
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

grant_type=password&scope=openid%20profile&username=john.doe%40example.com&password=qwerty
```

### Response

```js
HTTP/1.1 200 OK
Content-Length: 2034
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "expires_in": 3600,
    "scope": "openid profile",
    "token_type": "Bearer",
    "access_token": "eyJ0eXAi...sVAfIcA0",
    "refresh_token": "eyJ0eXAi...nom5mDrl-k",
    "id_token": "eyJ0eXAi...rEXE05y1Cg",
}
```

## Grant type Authorization code, for user tokens

The authorization code grant is the second step in the authorization flow for user.
A user logs in to SchAcc, and SchAcc will return the user to site with a code.
That code is then exchanged in backend for a user token.

### Request

```
POST /oauth/token
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=eyJ0eXAi...sVAfIcA0&redirect_uri=https%3A%2F%2Fexample.com
```

### Response

```js
HTTP/1.1 200 OK
Content-Length: 2034
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "expires_in": 3600,
    "scope": "openid profile",
    "token_type": "Bearer",
    "access_token": "eyJ0eXAi...sVAfIcA0",
    "refresh_token": "eyJ0eXAi...nom5mDrl-k",
    "id_token": "eyJ0eXAi...rEXE05y1Cg",
}
```

## Grant type Refresh token, for user tokens

The refresh token grant is used to fetch a new user access token, when the
current access token has expired. Access token lifetimes are typically shorter,
and refresh token lifetimes are typically longer.

### Request

```
POST /oauth/token
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token&refresh_token=eyJ0eXAi...nom5mDrl-k&scope=openid%20profile
```

### Response

```js
HTTP/1.1 200 OK
Content-Length: 2034
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "expires_in": 3600,
    "scope": "openid profile",
    "token_type": "Bearer",
    "access_token": "eyJ0eXAi...sVAfIcA1",
    "refresh_token": "eyJ0eXAi...nom5mDr1Cr",
    "id_token": "eyJ0eXAi...rEXE05y1Cg",
}
```

## Grant type JWT Bearer, for user tokens via Facebook and Google+

The JWT Bearer grant is used to fetch a user access token, by first logging
the user in via a third party, and then sending the third party token to SchAcc,
baked into a signed JWT.

### Request

```
POST /oauth/token
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=eyJ0eXAi...sQCjIc82
```

### Response

```js
HTTP/1.1 200 OK
Content-Length: 2034
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "expires_in": 3600,
    "token_type": "Bearer",
    "access_token": "eyJ0eXAi...sVAfIcA1",
    "refresh_token": "eyJ0eXAi...nom5mDr1Cr"
}
```

## Failure cases

* **400 Bad Request** <span class="faded">Malformed request, missing parameter,
or unsupported grant type, invalid client credentials, etc. See error response
for full description</span>
