:title POST /oauth/introspect

:aside

## See also

* [POST /oauth/token](/oauth/token)
* [POST /oauth/ro](/oauth/ro)
* [GET /oauth/userinfo](/oauth/userinfo)
* [GET /oauth/jwks](/oauth/jwks)
* [GET /oauth/authorize](/oauth/authorize)
* [Server to Server Auth](server-to-server-authentication/)
* [Token introspection](/token-introspection/)

:body

The `oauth/introspect` endpoint is used to verify token and to translate it into claims.
It is defined in [RFC-7662](https://tools.ietf.org/html/rfc7662).

A token can only be introspected by a client within the same merchant, or by a client
who is the intended audience, meaning the service domain of the introspecting client
must be in the `aud` claim array. For further information on cross merchant introspection,
see [Specifying a resource indicator](http://techdocs.spid.no/authentication/).


