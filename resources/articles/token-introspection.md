:title Token Introspection

:frontpage
:category api-integration
:aside
## Table of Contents

<spid-toc></spid-toc>

## Prerequisites

In order to make use of this information, you need to know your:

- client ID
- client secret


## See also

- [Obtaining tokens](/authentication/#obtaining-a-server-token)

:body

As a resource server publishing APIs protected by OAuth, you need to verify the tokens
received from clients. For JWT access tokens issued by SPiD, this can be done in two ways:

1. By a request to SPiD's introspection endpoint `/oauth/introspect`.
1. By verifying the token signature and content locally.

Non-JWT access tokens can only be introspected by sending a request to SPiD.

## Token introspection request

The introspection endpoint is protected by the same [client authentication](/authentication/#client-authentication) as
the token endpoint. A resource server is only allowed to introspect tokens which are intended for itself, or for another
client belonging to the same merchant.

To allow a resource server to introspect a token issued to a client not belonging to the same merchant as the resource
server, the client must make sure to [specify a "resource indicator" in the token request](/authentication/).

```sh
curl -X POST -H "Authorization: Basic NGU4NDYzNTY5Y2FmN2NhMDE5MDAwMDA3OmZvb2Jhcg"\
                     -d token=<access token>
                     https://identity-pre.schibsted.com/oauth/introspect
```

When successful, this request will return a JSON object:

```json
{
  "active": true,
  "scope": "profile email",
  "client_id": "4e8463569caf7ca019000007",
  "token_type": "Bearer",
  "exp": 1487169930,
  "now": 1487166577
}
```

For further details refer to
[OAuth 2.0 Token Introspection](https://tools.ietf.org/html/rfc7662).


## Local token introspection

The JWT access token is signed asymmetrically, and SPiD publishes the set of valid public keys at the
endpoint `/oauth/jwks`.

To verify the token locally, follow these steps:

1. Fetch the [JSON Web Key Set (JWKS)](https://tools.ietf.org/html/rfc7517#section-5) containing all valid keys from 
   SPiD `/oauth/jwks`.
1. Look at the JWS header to find the key id in the [`kid` parameter](https://tools.ietf.org/html/rfc7515#section-4.1.4).
1. Find the key with the matching key id in the JWKS, and use it to 
   [verify the signature of the JWT token](https://tools.ietf.org/html/rfc7515#section-5.2).
1. Verify that the claims in the payload match what you expect (the correct scope, audience, etc.).


A list of libraries in different programming languages that implements JWS can be found on https://jwt.io.
