:title SPiD Authentication

:frontpage
:category api-integration
:aside
## Table of Contents

<spid-toc></spid-toc>


## Prerequisites

In order to make use of this information, you need to know your:

- client ID
- client secret

You may also want to look into the [Getting Started](/getting-started/) guide.

## See also

- [Explaining how login flows work](/login-flows/)
- [Explaining how logout flows work](/logout-flows/)
- [Token introspection](/token-introspection/)

:body

SPiD uses
[OAuth 2.0 (draft 11)](http://tools.ietf.org/html/draft-ietf-oauth-v2-11) for
authentication and authorization. With OAuth 2.0, you obtain an access token for
a user via a redirect to SPiD, then you use this token to perform authorized
requests on behalf of that user by including it with API requests in either
 
- the HTTP Authorization Header (recommended):
        
        GET /api/{version}/user/{userId} HTTP/1.1
        Host: identity-pre.schibsted.com
        Authorization: Bearer <token>
    
- the `oauth_token` request parameter:
    
        GET /api/{version}/user/{userId}?oauth_token=<token> HTTP/1.1
        Host: identity-pre.schibsted.com

## Token types

SPiD clients will typically use two kinds of tokens: user and server (see
[OAuth client credentials](http://tools.ietf.org/html/draft-ietf-oauth-v2-11#section-1.4.3)).
A user token can only be used with requests for a certain set of API endpoints,
and will only be able to retrieve data related to a specific user. A server
token can be used with requests to most endpoints, and will be able to access
data for all users who have granted the client access to its data.

## Client authentication

SPiD allows clients to authenticate in two ways:

- Using [HTTP basic authentication](https://tools.ietf.org/html/rfc2617#section-2) 
  in the authorization request header field, `base64(<CLIENT_ID>:<CLIENT_SECRET>)`
  (recommended):
    
        POST /oauth/token HTTP/1.1
        Host: identity-pre.schibsted.com
        Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
        Content-Type: application/x-www-form-urlencoded
    
        grant_type=client_credentials

- Including the credentials in the request body parameters:

        POST /oauth/token HTTP/1.1
        Host: identity-pre.schibsted.com
        Content-Type: application/x-www-form-urlencoded
        
        grant_type=client_credentials&client_id=s6BhdRkqt3&client_secret=gX1fBat3bV


## Obtaining a user token

Obtaining user tokens by redirecting the user to SPiD to log in is covered in
depth in the [Implementing SSO guide](/implementing-sso/).

While it is not recommended, or even feasible, to manually handle user
passwords, it is possible to programatically obtain a user token given your
client id, client secret and a user's credentials. You may want to do this for
testing purposes (e.g. programatically testing an endpoint like
[/me](/endpoints/GET/me/) that only works with a user token).

A user token is obtained by using the `password` grant type at the token endpoint
`/oauth/token`:

```sh
curl -X POST -H "Authorization: Basic <client credentials>"\
             -d grant_type=password&\
                redirect_uri=http://localhost&\
                username=<username>&\
                password=<password>\
            https://identity-pre.schibsted.com/oauth/token
```

You may have to quote the entire data string, depending on the username and
password.

When successful, this request will return a JSON object:

```json
{
  "access_token": "322c4c33a0bb327ea6a06d05fa37bf3613190499",
  "expires_in": 604800,
  "scope": "profile email",
  "user_id": "938029",
  "is_admin": false,
  "refresh_token": "818518449498eb3e5d228e016461f1f148e91002",
  "server_time": 1399628771
}
```

The `access_token` may be used to make API requests on behalf of this user.

## Obtaining a server token

`POST` your client credentials and a grant type of `client_credentials` to
obtain a server access token:

```sh
curl -X POST -H "Authorization: Basic <client credentials>"\
             -d grant_type=client_credentials
            https://identity-pre.schibsted.com/oauth/token
```

The returned access token can be used to make requests on behalf of your client:

```sh
curl -H "Authorization: Bearer <token>" https://identity-pre.schibsted.com/api/2/users
```

For further details refer to
[OAuth 2.0 client credentials](http://tools.ietf.org/html/draft-ietf-oauth-v2-11#section-1.4.3).

### Specifying a resource indicator

To use a token with a resource server when doing server-to-server authentication it might be
necessary to explicitly indicate the resource server as the intended audience of the token.

To indicate which resource server a token is intended to be used at, include a "resource indicator"
via the `resource` request parameter:

```sh
curl -X POST -H "Authorization: Basic <client credentials>"\
             -d grant_type=client_credentials&\
                resource=<resource indicator>
            https://identity-pre.schibsted.com/oauth/token
```

Support for this parameter is limited to server tokens.

For further details refer to 
[Resource Indicators for OAuth 2.0, draft 2](https://tools.ietf.org/html/draft-campbell-oauth-resource-indicators-02).

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

Other errors, not from the authorization endpoint, are returned as a JSON object in the response. 
The format is described in the OAuth2.0 spec. The spec describes a list
of errors that can occur and that the client may take into account.

### Official OAuth 2.0 errors

Refer to [the spec](http://tools.ietf.org/html/draft-ietf-oauth-v2-11) for details on what these error codes mean.

- **redirect_uri_mismatch**: The redirection URI provided does not match a pre-registered redirection URI stored in SPiD.
- **unauthorized_client**: The client is not authorized to request an authorization code using this method or authorization grant type.
- **access_denied**: The resource owner or authorization server denied the request.
- **invalid_request**: The request is missing a required parameter, includes an invalid parameter value,
includes a parameter more than once, or is otherwise malformed.
- **invalid_client_id**: Client authentication failed (e.g. unknown client, no client authentication included, or unsupported
authentication method).
- **unsupported_response_type**: The authorization server does not support obtaining an authorization code using this method.
- **invalid_scope**: The requested scope is invalid, unknown, malformed, or exceeds the scope granted by the resource owner.
- **invalid_grant**: The provided authorization grant (e.g. authorization code, resource owner credentials) or refresh token is
invalid, expired, revoked, does not match the redirection URI used in the authorization request, or was issued to
another client.

Protected resource errors

- **invalid_token**:
The access token provided is expired, revoked, malformed, or invalid for other reasons. The client MAY request a new
access token and retry the protected resource request.
- **expired_token**:
The access token provided has expired. The client is expected to be able to handle the response and request a new
access token using the refresh token issued with the expired access token
- **insufficient_scope**:
The request requires higher privileges than provided by the access token.

Error when using a grant type that is not implemented:

- **unsupported_grant_type**

### Error response example
When a request is unsuccessful, the platform will return an error JSON object. Below is an example of
what is returned when your application is using an invalid access token:

```json
{
  "error": "invalid_token",
  "error_code": 401,
  "type": "OAuthException",
  "error_description": "401 Unauthorized access! Reason: \"invalid_token\" (OAuth realm: \"Service\", Scope: \"\")"
}
```

Recommended reading on
[OAuth protocol endpoints](http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-3).

## API access control flow explained
This is a complete overview of how SPiD processes an API request and when your client application may
expect to receive error responses and the reasons it happened:
![API access control flow](/images/api-access-control-flow.png)

## SDK support for easier implementation

SPiD provides API clients/SDKs for a number of languages that mostly abstract
away the OAuth details. See how to get started [server-side](/getting-started/) or for
[mobile devices](/mobile/overview/).

## Best practices and security guidelines
This section contains a list of best practices and requirements that will be performed on integration and security reviews by SPiD.

### Client credentials handling
- Client credentials and redirect URIs must NOT be hardcoded anywhere, but made configurable so the same code can be used for both STAGE/PRE and PROD environments.
- Verify that the client configuration for the production environment doesn't use stage credentials or redirect URIs and vice versa. This is especially important before you perform a new release of your application.
- Client secrets are confidential and must be treated as such. They should NOT be committed to version control systems or publicly transmitted in the client’s organization or between unsecured systems and communication channels such as instant messages or emails.

### Your application's Redirect URI must be valid and follow these security guidelines
- Previously defined and stored as a valid redirect uri for your client application
- We don't recommend using IP addresses as these change all the time (unless they are static)
- Doesn't contain other internal or third party based redirect uris encoded in it
- Doesn't contain invalid characters (\s`!()[]{};',<>«»“”‘’@")
- Redirect endpoint is required to be behind SSL (https, not http)
- No external JavaScript running on redirect URIs that will receive the OAuth code parameter

The client SHOULD NOT include any third-party scripts (e.g. third-
   party analytics, social plug-ins, ad networks) in the redirection
   endpoint response.  Instead, it SHOULD extract the credentials from
   the URI and redirect the user-agent again to another endpoint without
   exposing the credentials (in the URI or elsewhere).  If third-party
   scripts are included, the client MUST ensure that its own scripts
   (used to extract and remove the credentials from the URI) will
   execute first.

### Protect your local session, SPiD code and token
- SPiD code AND user token must NEVER be embedded in your application urls, passed beyond your application's redirect uri or stored in cookies.
- Your session ID should never be visible in an URL.

### Mobile development
- For best practices specific to mobile development visit the [mobile development](/mobile/overview/) page.