:title Service-to-service auth

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

- [Obtaining tokens](/oauth/token/)
- [Token introspection](/token-introspection/)

:body

In addition to the user auth facilities provided by the platform, SchAcc also provides a mechanism for
when two services want to communicate directly with each other. In these situations, a service
needs to authenticate and authorize the other services which access its protected resources. We refer
to this as service-to-service (henceforth S2S) auth.

S2S auth in SchAcc is made up of three basic steps:

1. A client [obtains a client token](/oauth/token/) via
a `client_credentials` grant.
1. The client makes a request to the server hosting the protected resource, known as the "resource server", passing the newly acquired token as a
[bearer token](https://tools.ietf.org/html/rfc6750).
1. The resource server [introspects](/token-introspection/) the token it receives and validates the properties
in the introspected token. If it is valid, the request is allowed to proceed. If it is not, the
request is rejected.

![Server-to-server communication flow](/images/server-to-server-comm.png)

The following sections break down these high-level steps in more detail.

## Obtaining a token

Before making a call to a resource server, the calling client first needs to [obtain a token](/oauth/token/).
For this, a `client_credentials` grant is made using the caller's client ID and secret. Additionally,
the caller should specify the resource trying to be accessed via the `resource`
parameter and any scopes required via the `scope` parameter:

```sh
curl -X POST -H "Authorization: Basic NGU4NDYzNTY5Y2FmN2NhMDE5MDAwMDA3OmZvb2Jhcg" \
-d 'resource=https://service.schibsted.com/messages' -d 'scope=read:messages write:messages' \
-d 'grant_type=client_credentials' https://login.schibsted.com/oauth/token
```

For further details on resource indicators, please refer to
[Resource Indicators for OAuth 2.0, draft 2](https://tools.ietf.org/html/draft-campbell-oauth-resource-indicators-02).

***Note:*** currently the value passed as the resource needs to match the domain registered in SchAcc's
[self service](/mobile/selfservice/) tool for the resource server.

The scopes passed will help the receiving service authorize the call, and can be thought of as a set of
permissions or policies that the service will allow. Thus, the scopes present in the token help the
called service determine if the request actions should be permitted. The exact scopes required is
determined by the resource server and is outside the scope of this document.

**Note:** currently the scopes that a client may request may only be managed by the administrators of
the SchAcc platform. In order to have scopes added to your client [contact support](mailto:schibstedaccount@schibsted.com).
In the future, management of scopes will be possible via the [self service](/mobile/selfservice/) tool.

## Send the token in the request

Next, when making the request to the resource server, the caller simply needs to include the freshly
acquired token in the Authorization header as a [bearer token](https://tools.ietf.org/html/rfc6750):

```sh
curl -H 'Authorization: Bearer _your_token_here' https://service.schibsted.com/messages/123
```

## Validate the token

The resource server receives the token in `Authorization` header and must validate it in order to decide
whether or not to allow the call. The first step in doing so is to [introspect the token](/token-introspection/)
in order to get accessed to the claims it carries. Introspecting the token will provide a result similar
to the following:

```json
{
  "scope": "read:messages",
  "client_id": "4e8463569caf7ca019000007",
  "token_type": "Bearer",
  "exp": 1487169930,
  "iat": 1487166577,
  "iss": "https://login.schibsted.com",
  "aud": ["https://service.schibsted.com/messages"]
}
```

Once the resource server has introspected the token, it's important to validate the claims as follows:

1. Has the token expired? This is determined by checking the `exp` claim of the token. When performing
remote introspection this is handled automatically as the introspection request will return `false`
as the value of the `active` property.
1. Is the token issued by the expected issuer? If the resource server is using https://login.schibsted.com as
its identity provider, it should only trust tokens with that URL in the `iss` claim.
1. Does the token have the expected audience (determined by inspecting the `aud` claim, which is an array)?
This is used to check if the token was intended for this service or not.
1. Does the token have the expected scopes. The `scope` property contains a space-separated list of
the scopes carried by the token. For each resource/action provided by the service, the implementers
should decide on a set of scopes that are required to perform that action and ensure that the token
carries those scopes.

If all of the above checks pass, the resource server should let the call through. If the `Authorization`
header is missing or incorrect, or if the token has expired, the service should return a status of 
`401 Unauthorized` to indicate to the calling service that it's time to fetch a new token. If the issuer,
audience or scopes are incorrect/insufficient, the service should return a `403 Forbidden` to indicate
that the token was understood, but the permissions are not sufficient to complete the call.

Often services support multiple clients/tenants, and the `client_id` parameter can be used to determine
which one is calling the service. It is a recommended best practice for services to maintain their
own IDs for their tenants (for use in DB tables, etc.) and simply map from the SchAcc `client_id` to their
own IDs. This adds a layer of insulation in case a tenant's `client_id` changes (e.g. if they delete
their SchAcc client and create a new one).
