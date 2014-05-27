:introduction

Get a one-time authentication code for the current user. This code can be given
to another client, who may use it to request an access token for the same user.

This normally comes into play when you've got:

* an app client on a mobile device
* a backend system on a server

The server needs to communicate with SPiD on behalf of the user, but the user
authenticates on the app, not the server.

Because of the security implications of having the app share its tokens or the
logged in user data with the backend server directly, the app must ask SPiD for
a one-time code corresponding to the authenticated user. After retrieving the
exchange code, the app may share this code with the backend, which then
authenticates directly with SPiD with the code and gets its own user access
token - thus keeping a high level of security and giving both apps and backends
full access to user data and the SPiD APIs.

Please note:

- Both clients must belong to the same merchant.
- The code expires after 30 seconds.

:example-params

type: code
