:title POST /oauth/introspect

:aside

## See also

* [POST /oauth/token](/oauth/token/)
* [POST /oauth/ro](/oauth/ro/)
* [GET /oauth/userinfo](/oauth/userinfo/)
* [GET /oauth/jwks](/oauth/jwks/)
* [GET /oauth/authorize](/oauth/authorize/)
* [Server to Server Auth](/s2s-auth/)
* [Token introspection](/token-introspection/)

:body

The `/oauth/register` endpoint is used to register a new client under a merchant. The use case
for this endpoint is mainly dynamically registered clients for s2s auth.
Dynamic client registration is defined in [RFC7591](https://tools.ietf.org/html/rfc7591).

The bearer token in the request must have scope `spid:merchants/<merchant>|write` and token `client_id` must belong to that `<merchant>`.

## Request

```
POST /oauth/register
Authorization: Bearer eyJ0eXAiOiJK...jUaR-nZOx5MGg
Content-Type: application/json

{
    "client_name": "Client example app",
    "client_uri": "https://client.example.com",
    "scope": "prefix:auth|write",
    "redirect_uris": [
        "https://client.example.com/redirect1",
        "https://client.example.com/redirect2"
    ]
}
```

* **client_name**, required
* **client_uri**, required
* **scope**, optional, space separate string of scopes. All scopes must start with preregistered merchant prefix.
* **redirect_uri**, optional, list of valid URLs.

## Response

```js
HTTP/1.1 200 OK
Content-Length: 622
Content-Type: application/json
Date: Mon, 29 Feb 2016 13:37:00 GMT

{
  "client_uri" : "https://client.example.com",
  "mutual_tls_sender_constrained_access_tokens" : false,
  "client_secret_expires_at" : 0,
  "scope" : "prefix:auth|write openid profile email phone address offline_access",
  "redirect_uris" : [
    "https://client.example.com/redirect1",
    "https://client.example.com/redirect2"
  ],
  "client_id_issued_at" : 1511189545,
  "client_secret" : "QziV0bJzW-lWGvrcVG8yRO3JZ5vQPMplv7K2WCsjyyk",
  "client_name" : "Client example app",
  "client_id" : "5a12ec29714026dbd1c8c7be"
}
```

### Failure cases

* **400 Bad Request** <span class="faded">invalid_client_metadata, given data is invalid, or scope does not start with preregistered merchant prefix</span>
* **400 Bad Request** <span class="faded">invalid_redirect_uri, given redirects are non URIs</span>
* **401 Unauthorized** <span class="faded">invalid_token, token is invalid or missing</span>
* **403 Forbidden** <span class="faded">insufficient_scope, token is missing required scope</span>
