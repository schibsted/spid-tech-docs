:introduction

Get a one-time authentication code for the current user. This code can be given
to another client, who may use it to request an access token for the same user.

This normally comes into play when you've got:

* an app client on a mobile device
* a backend system on a server

The server needs to communicate with Schibsted account on behalf of the user, but the user
authenticates on the app, not the server.

Because of the security implications of having the app share its tokens or the
logged in user data with the backend server directly, the app must ask Schibsted account for
a one-time code corresponding to the authenticated user. After retrieving the
exchange code, the app may share this code with the backend, which then
authenticates directly with Schibsted account with the code and gets its own user access
token - thus keeping a high level of security and giving both apps and backends
full access to user data and the Schibsted account APIs.

Please note:

- Both clients must belong to the same merchant.
- The code expires after 30 seconds.

Example of an authentication sequence between a native device app and its Backend API:

```sequence-diagram
Device->Schibsted account Web: Redirect user to login
Schibsted account Web->Device: Return oauth code in redirect url: ?code=123abc
Device->Schibsted account Web: POST /oauth/token
Note left of Schibsted account Web: grant_type=authorization_code\nredirect_uri=client_app_scheme://spid/login\nclient_id=1234\nclient_secret=abcd\ncode=123abc
Schibsted account Web->Device: Return access token
Note right of Device: access_token=94daab\nexpires_in=3600\nuser_id=1001
Device->Schibsted account API: Ask for a onetime exchange code for Client Backend: POST /oauth/exchange
Note left of Schibsted account API: (NOTE: Using the backend's client id)\nclientId=9876\ntype=code\noauth_token=94daab
Schibsted account API->Device: Return a onetime exchange code (valid for 30 seconds)
Note right of Device: code=456def
Device->Client Backend: Send Schibsted account's onetime exchange code
Client Backend->Schibsted account Web: POST /oauth/token
Note right of Schibsted account Web: grant_type=authorization_code\nredirect_uri=http://your-client-url.no\nclient_id=9876\nclient_secret=ghijkl\ncode=456def
Schibsted account Web->Client Backend: Return access token
Note left of Client Backend: access_token=b79ii5\nexpires_in=3600\nuser_id=1001
Client Backend->Schibsted account API: GET /user/1001 or /me
Schibsted account API->Client Backend: Returns user object
Client Backend->Client Backend: Register user if not exist internally
Client Backend->Device: Return internal client API token for currently logged in user
Note left of Device: Multiple API calls between\nDevice and Backend:
Device->Client Backend: Device requests Client Backend using internal client API token
Client Backend->Device: Client Backend responds
Note left of Device: When logging out:
Device->Schibsted account Web: GET /logout
Schibsted account Web->Device: HTTP 200 OK
Device->Client Backend: Logout Backend using internal client API token
Client Backend->Device: OK
```

### Exchange type: session

The process described above allows your backend to communicate on behalf of a
user logged into the device. You might also need to generate a session for the
user in a webview layer of a native mobile application. You do that with this
endpoint as well, setting the `type` to `session`.

Example flow:

```sequence-diagram
Webview->Native App: Asks for a native logged in user by listening for an authenticated page request (/login or /signup)
Note right of Webview: User could also login natively
Native App->Schibsted account API: POST /oauth/exchange
Note left of Schibsted account API: clientId=9876\ntype=session\noauth_token=94daab
Note right of Schibsted account API: Schibsted account generates a onetime code\nthat expires within 1 minute
Schibsted account API->Native App: Returns onetime code
Native App->Webview: Sends onetime code to webview
Webview->Schibsted account Web: Redirects user to /session/{onetimecode}
Note right of Schibsted account Web: Schibsted account generates a session based on\nthe user this code belongs to
Schibsted account Web->Webview: Returns a Schibsted account session cookie and redirects user to redirect uri provided
```

:example-params

type: code
