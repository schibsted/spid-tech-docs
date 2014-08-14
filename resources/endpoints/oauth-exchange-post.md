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

Example of an authentication sequence between a native device app and its Backend API:

```sequence-diagram
Device->SPiD Web: Redirect user to login
SPiD Web->Device: Return oauth code in redirect url: ?code=123abc
Device->SPiD Web: POST /oauth/token
Note left of SPiD Web: grant_type=authorization_code\nredirect_uri=client_app_scheme://spid/login\nclient_id=1234\nclient_secret=abcd\ncode=123abc
SPiD Web->Device: Return access token
Note right of Device: access_token=94daab\nexpires_in=3600\nuser_id=1001
Device->SPiD API: Ask for a onetime exchange code for Client Backend: POST /oauth/exchange
Note left of SPiD API: (NOTE: Using the backend's client id)\nclientId=9876\ntype=code\noauth_token=94daab
SPiD API->Device: Return a onetime exchange code (valid for 30 seconds)
Note right of Device: code=456def
Device->Client Backend: Send SPiD's onetime exchange code
Client Backend->SPiD Web: POST /oauth/token
Note right of SPiD Web: grant_type=authorization_code\nredirect_uri=http://your-client-url.no\nclient_id=9876\nclient_secret=ghijkl\ncode=456def
SPiD Web->Client Backend: Return access token
Note left of Client Backend: access_token=b79ii5\nexpires_in=3600\nuser_id=1001
Client Backend->SPiD API: GET /user/1001 or /me
SPiD API->Client Backend: Returns user object
Client Backend->Client Backend: Register user if not exist internally
Client Backend->Device: Return internal client API token for currently logged in user
Note left of Device: Multiple API calls between\nDevice and Backend:
Device->Client Backend: Device requests Client Backend using internal client API token
Client Backend->Device: Client Backend responds
Note left of Device: When logging out:
Device->SPiD Web: GET /logout
SPiD Web->Device: HTTP 200 OK
Device->Client Backend: Logout Backend using internal client API token
Client Backend->Device: OK
```

:example-params

type: code
