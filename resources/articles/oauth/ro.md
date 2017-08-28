:title POST /oauth/ro

:aside

## See also

* [POST /oauth/token](/oauth/token/)
* [POST /oauth/introspect](/oauth/introspect/)
* [GET /oauth/userinfo](/oauth/userinfo/)
* [GET /oauth/jwks](/oauth/jwks/)
* [GET /oauth/authorize](/oauth/authorize/)
* [POST /passwordless/start](/endpoints/passwordless/start/)

:body

The `/oauth/ro` endpoint is used to authenticate a user via a challange code.
The flow is started by calling the [/passwordless/start](/endpoints/passwordless/start/)
endpoint and then calling the `/oauth/ro` endpoint with `passwordless_token` received from
[/passwordless/start](/endpoints/passwordless/start/) and challenge `code` entered by user.

## Request

```
POST /oauth/ro
Authorization: Basic NGU4NDYz...OmZvb2Jhcg==
Content-Type: application/x-www-form-urlencoded

grant_type=passwordless&scope=openid%20profile&passwordless_token=12...bd&code=123456
```

## Response

```js
HTTP/1.1 200 OK
Content-Length: 2534
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
    "expires_in": 3600,
    "scope": "openid profile",
    "token_type": "Bearer",
    "access_token": "eyJ0eXAi...LbXBPTs",
    "refresh_token": "eyJ0eXAi...z_r6_wo",
    "id_token": "eyJ0eXAi...G9S_5LQ"
}
```

### Failure cases

* **400 Bad Request** <span class="faded">Malformed request, missing parameter, or unsupported grant type</span>
* **401 Unauthorized** <span class="faded">Invalid client credentials</span>
