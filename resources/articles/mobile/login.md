:title Login

:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- Login
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Access tokens](/mobile/access-tokens/)
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Migration](/mobile/migration/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

To login you first acquire an access token from the /oauth/token endpoint which is used to get a one time code from the [/oauth/exchange](/endpoints/POST/oauth/exchange/) endpoint.

This code can be given to another client, who may use it to request an access token for the same user. This is how you can give your backend server an access token which identifies your app and authorize to make requests on your behalf.

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

### Grant type: code

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

### Grant type: session

The process described above allows your backend to communicate on behalf of a
user logged into the device. You might also need to generate a session for the
user in a webview layer of a native mobile application. You do that with this
endpoint as well, setting the `type` to `session` (`RequestType.SESSION` in the Android SDK).

Example flow:

```sequence-diagram
Webview->Native App: Asks for a native logged in user by listening for an authenticated page request (/login or /signup)
Note right of Webview: User could also login natively
Native App->SPiD API: POST /oauth/exchange
Note left of SPiD API: clientId=9876\ntype=session\noauth_token=94daab
Note right of SPiD API: SPiD generates a onetime code\nthat expires within 1 minute
SPiD API->Native App: Returns onetime code
Native App->Webview: Sends onetime code to webview
Webview->SPiD Web: Redirects user to /auth/session/{onetimecode}
Note right of SPiD Web: SPiD generates a session based on\nthe user this code belongs to
SPiD Web->Webview: Returns a SPiD session cookie and redirects user to redirect uri provided
```

How you authenticate your app in the login step to /oauth/token as shown above depends on what grant type you use. The grant types supported by SPiD are:

# :tabs

## :tab Android

#### password
Use the password grant to log in using username and password.
See `SPiDUserCredentialTokenRequest`

#### authorization_code
Use the authorization_code grant to log in using a an authorization code.
See `SPiDCodeTokenRequest`

#### client_credentials
Use the authorization_code grant to log in using a client's id and client secret.
See `SPiDClientTokenRequest`

#### urn:ietf:params:oauth:grant-type:jwt-bearer
The jwt bearer grant type is used when logging in using a third party such as Facebook or Google.
See `SPiDGooglePlusTokenRequest` or `SPiDFacebookTokenRequest`

#### refresh_token
The refresh_token grant is used when you have an access token that is about to expire and you want to refresh it.
See `SPiDRefreshTokenRequest`

For complete examples see the [sample apps](/sdks/android/sample-apps/)

## :tab iOS

#### password
Use the password grant to log in using username and password.
See `SPiDUserCredentialTokenRequest`

#### authorization_code
Use the authorization_code grant to log in using a an authorization code.
See `SPiDCodeTokenRequest`

#### client_credentials
Use the authorization_code grant to log in using a client's id and client secret.
See `SPiDClientTokenRequest`

#### urn:ietf:params:oauth:grant-type:jwt-bearer
The jwt bearer grant type is used when logging in using a third party such as Facebook or Google.
See `SPiDGooglePlusTokenRequest` or `SPiDFacebookTokenRequest`

#### refresh_token
The refresh_token grant is used when you have an access token that is about to expire and you want to refresh it.
See `SPiDRefreshTokenRequest`

For complete examples see the [sample apps](/sdks/ios/sample-apps/)

# :/tabs