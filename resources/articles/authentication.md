:title SPiD Authentication

:frontpage
:category api-integration
:aside

## Prerequisites

In order to make use of this information, you need to know your:

- client ID
- API secret

You may also want to look into the [Getting Started](/getting-started/) guide.

:body

SPiD uses
[OAuth 2.0 (draft 11)](http://tools.ietf.org/html/draft-ietf-oauth-v2-11) for
authentication and authorization. With OAuth 2.0, you obtain an access token for
a user via a redirect to SPiD, then you use this token to perform authorized
requests on behalf of that user by including the `oauth_token` request parameter
with API requests:

```text
GET https://payment.schibsted.no/api/{version}/user/{userId}?oauth_token=...
```

## Token types

SPiD clients will typically use two kinds of tokens: user and server (see
[OAuth client credentials](http://tools.ietf.org/html/draft-ietf-oauth-v2-11#section-1.4.3).
A user token can only be used with requests for a certain set of API endpoints,
and will only be able to retrieve data related to a specific user. A server
token can be used with requests to most endpoints, and will be able to access
data for all users who have granted the client access to its data.

## Obtaining a user token

Obtaining user tokens by redirecting the user to SPiD to log in is covered in
depth in the [Implementing SSO guide](/implementing-sso/).

While it is not recommended, or even feasible, to manually handle user
passwords, it is possible to programatically obtain a user token given your
client id, API secret and a user's credentials. You may want to do this for
testing purposes (e.g. programatically testing an endpoint like
[/me](/endpoints/GET/me/) that only works with a user token).

A user token is obtained by requesting the `password` grant type from `/oauth/token`:

```sh
curl -X POST -d grant_type=password&\
                client_id=<CLIENT_ID>&\
                client_secret=<CLIENT_SECRET>&\
                redirect_uri=http://localhost&\
                username=<username>&\
                password=<password>\
            https://stage.payment.schibsted.no/oauth/token
```

You may have to quote the entire data string, depending on the user's email and
password.

When successful, this request will return a JSON object:

```js
{
  "access_token": "322c4c33a0bb327ea6a06d05fa37bf3613190499",
  "expires_in": 604800,
  "scope": null,
  "user_id": "938029",
  "is_admin": false,
  "refresh_token": "818518449498eb3e5d228e016461f1f148e91002",
  "server_time": 1399628771
}
```

The `access_token` may be used to make API requests on behalf of this user.

**NB!** This will only work if the user is already logged in. So, in order to
make use of this, you have to
[log in via SPiD the regular way](http://stage.payment.schibsted.no/login)
first.

## Obtaining a server token

`POST` your client credentials and a grant type of `client_credentials` to
obtain a server access token:

```text
POST https://payment.schibsted.no/oauth/token?\
     client_id=CLIENT_ID&\
     client_secret=CLIENT_SECRET&\
     grant_type=client_credentials
```

The returned access token can be used to make requests on behalf of your client:

```
GET https://schibsted.payment.no/api/2/users?oauth_token=...
```

For further details refer to
[OAuth 2.0 client credentials](http://tools.ietf.org/html/draft-ietf-oauth-v2-11#section-1.4.3).

## OAuth failures

In accordance with
[the OAuth specification](http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-3.1.2),
SPiD redirects the user back to the client. When an authentication error occurs,
SPiD redirect the user to a specific, client-provided, OAuth failure redirect
url. This is an optional feature, which means that if no special failure
redirect URI is provided, we will redirect failures to the same redirect URI
used in the Oauth flow, unless the failure is due to an invalid redirect uri
provided. If an invalid redirect is URI provided, we will default to the
client's default redirect URI.

SPiD's redirect scheme ensures that the user always has a way to return to the
client. Upon the user's return it is up to the client to decide what the error
is and how to handle it. The redirect will contain an error code:

```text
http://YOUR_REDIRECT_URL?error=invalid_client_id
```

All endpoints, except for the OAuth ones, return error responses as a JSON
object. The format is described in the OAuth2.0 spec. The spec describes a list
of errors that can occur and that the client may take into account (not every
error is going to be returned in the redirect, like token expiration).

### Official OAuth 2.0 errors

Refer to the spec for details

- `redirect_uri_mismatch`
- `invalid_client_credentials`
- `unauthorized_client`
- `access_denied`
- `invalid_request`
- `invalid_client_id`
- `unsupported_response_type`
- `invalid_scope`
- `invalid_grant`

Protected resource errors

- `invalid_token`
- `expired_token`
- `insufficient_scope`

Error when using a grant type that is not implemented:

- `unsupported_grant_type`

Recommended reading on
[OAuth protocol endpoints](http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-3).

## SDK support for easier implementation

SPiD provides API clients/SDKs for a number of languages that mostly abstract
away the OAuth details. The [getting started guide](/getting-started/) provides
an introduction.

## Best practices and security guidelines
This section contains a list of best practices and requirements that will be performed on integration and security reviews by SPiD.

### Client credentials handling
- Client credentials and redirect URIs must NOT be hardcoded anywhere, but made configurable so the same code can be used for both STAGE and PROD environments.
- Verify that the client configuration for the production environment doesn't use stage credentials or redirect URIs and vice versa. This is especially important before you perform a new release of your application.
- Client secrets are confidential and must be treated as such. They should NOT be committed to version control systems or publicly transmitted in the client’s organization or between unsecured systems and communication channels.

### Your application's Redirect URI must be valid and follow these security guidelines
- Previously defined and stored as a valid redirect uri for your client application
- Doesn't contain other internal or third party based redirect uris encoded in it
- Doesn't contain invalid characters (\s`!()[]{};',<>«»“”‘’@")
- Redirect endpoint is recommended to be behind SSL
- No external Javascript running on redirect URIs that will receive the OAuth code parameter

### Protect your local session, SPiD code and token
- SPiD code AND user token must NEVER be embedded in your application urls, passed beyond your application's redirect uri or stored in cookies.
- Your session ID should never be visible in an URL.
