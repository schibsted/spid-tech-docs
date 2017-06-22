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
received from clients. For tokens issued by SPiD, this can be done by a request to SPiD's introspection endpoint
`/oauth/introspect`.

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
