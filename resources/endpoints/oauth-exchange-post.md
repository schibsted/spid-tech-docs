:introduction

A client may exchange a onetime authentication code bound to a user which the
client already has a token for. This code can be given to another client that
belongs to the same merchant in order for that client to receive an access token
for the same user.

### Use Cases

A typical use case is when you have two clients:

* An app client running on a mobile device
* A backend system that runs on a server

The app needs to use APIs on the backend server, but the user authenticates on
the app, not the server. In order for the server to be able to authenticate the
same user and provide the app with the user's client specific data, the server
needs to find out which user is logged in the app and maybe even communicate
with SPiD on behalf of that very same user.

Because of the security implications of having the app share its tokens or the
logged in user data with the backend server directly, the app must ask SPiD for
a onetime code corresponding to the authenticated user. After retrieving the
exchange code, the app may share this code with the backend, which then
authenticates directly with SPiD with the code and gets its own user access
token - thus keeping a high level of security and giving both apps and backends
full access to user data and the SPiD APIs.
