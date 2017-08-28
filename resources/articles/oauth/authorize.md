:title GET /oauth/authorize

:aside

## See also

* [POST /oauth/token](/oauth/token/)
* [POST /oauth/ro](/oauth/ro/)
* [POST /oauth/introspect](/oauth/introspect/)
* [GET /oauth/userinfo](/oauth/userinfo/)
* [GET /oauth/jwks](/oauth/jwks/)

:body

The `/oauth/authorize` is used to start an authentication flow in SPiD.
There currently is an authorize implementation in SPiD that redirects to the
regular login flow. This will be changed during the H2 2017. More info will come.

For now, see [Auth, Login, Signup and Signin flow](/flows/auth-flow/).
