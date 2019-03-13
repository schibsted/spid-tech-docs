:introduction

Get a one-time authentication code for the current user. This code can be given
to another client, who may use it to request an access token for the same user.

This normally comes into play when you've got:

* an app client on a mobile device
* a backend system on a server

The server needs to communicate with SchAcc on behalf of the user, but the user
authenticates on the app, not the server.

Because of the security implications of having the app share its tokens or the
logged in user data with the backend server directly, the app must ask SchAcc for
a one-time code corresponding to the authenticated user. After retrieving the
exchange code, the app may share this code with the backend, which then
authenticates directly with SchAcc with the code and gets its own user access
token - thus keeping a high level of security and giving both apps and backends
full access to user data and the SchAcc APIs.

Please note:

- Both clients must belong to the same merchant.
- The code expires after 30 seconds.

Example of an authentication sequence between a native device app and its Backend API:

```sequence-diagram
Device->SchAcc Web: Redirect user to login
SchAcc Web->Device: Return oauth code in redirect url: ?code=123abc
Device->SchAcc Web: POST /oauth/token
Note left of SchAcc Web: grant_type=authorization_code\nredirect_uri=client_app_scheme://spid/login\nclient_id=1234\nclient_secret=abcd\ncode=123abc
SchAcc Web->Device: Return access token
Note right of Device: access_token=94daab\nexpires_in=3600\nuser_id=1001
Device->SchAcc API: Ask for a onetime exchange code for Client Backend: POST /oauth/exchange
Note left of SchAcc API: (NOTE: Using the backend's client id)\nclientId=9876\ntype=code\noauth_token=94daab
SchAcc API->Device: Return a onetime exchange code (valid for 30 seconds)
Note right of Device: code=456def
Device->Client Backend: Send SchAcc's onetime exchange code
Client Backend->SchAcc Web: POST /oauth/token
Note right of SchAcc Web: grant_type=authorization_code\nredirect_uri=http://your-client-url.no\nclient_id=9876\nclient_secret=ghijkl\ncode=456def
SchAcc Web->Client Backend: Return access token
Note left of Client Backend: access_token=b79ii5\nexpires_in=3600\nuser_id=1001
Client Backend->SchAcc API: GET /user/1001 or /me
SchAcc API->Client Backend: Returns user object
Client Backend->Client Backend: Register user if not exist internally
Client Backend->Device: Return internal client API token for currently logged in user
Note left of Device: Multiple API calls between\nDevice and Backend:
Device->Client Backend: Device requests Client Backend using internal client API token
Client Backend->Device: Client Backend responds
Note left of Device: When logging out:
Device->SchAcc Web: GET /logout
SchAcc Web->Device: HTTP 200 OK
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
Native App->SchAcc API: POST /oauth/exchange
Note left of SchAcc API: clientId=9876\ntype=session\noauth_token=94daab
Note right of SchAcc API: SchAcc generates a onetime code\nthat expires within 1 minute
SchAcc API->Native App: Returns onetime code
Native App->Webview: Sends onetime code to webview
Webview->SchAcc Web: Redirects user to /session/{onetimecode}
Note right of SchAcc Web: SchAcc generates a session based on\nthe user this code belongs to
SchAcc Web->Webview: Returns a SchAcc session cookie and redirects user to redirect uri provided
```

:example-params

type: code
