:title Server-to-server communication
:frontpage
:category how-tos
:aside
## Table of Contents

<spid-toc></spid-toc>

## See also

- [Client authentication](/authentication/#client-authentication)
- [Obtaining a server token](/authentication/#obtaining-a-server-token)
- [Token introspection](/token-introspection/)

:body

To enable server-to-server communication which requires authorization a number of steps have to be completed. Here is an
overview of the full flow using OAuth 2:

![Server-to-server communication flow](/images/server-to-server-comm.png)

The request to the protected resource is out of scope of this how-to, since it's dependent on the API published by
the resource server.

## Client: token request and response

The client starts by obtaining a "server access token" from SPiD, by making a token request using the grant type
`client_credentials`.

It's important to make sure the correct `scope` is requested. If the access token is to be used with a resource server
not belonging to the same merchant as the client, a "resource indicator" must be included to explicitly indicate the
resource server as the intended recipient of the token.
  
For a more detailed description of how to obtain a server access token, see
[Obtaining a server token](/authentication/#obtaining-a-server-token).


## Resource server: token introspection

When the resource server receives the request from the client, it must verify the included access token. To ensure the
protection of the resource, the resource server must make sure it is in the intended audience of the
access token, that the access token has not expired and that is has the correct scope associated with it.

Typically an access token is an opaque string to the resource server, but the associated authorization information can
be looked up by making a token introspection request to SPiD. The token introspection endpoint requires the resource
server to authenticate with its client credentials (see [Client authentication](/authentication/#client-authentication))
and include the access token in the request.
 
For a more detailed description of how to make a token introspection request, see
[Token introspection](/token-introspection/).
 
If the token is a JWT signed with a key trusted by the resource server, it can be inspected locally. When the token 
signature has been verified, the payload can be inspected to find the authorization data (audience, scope, etc.).
