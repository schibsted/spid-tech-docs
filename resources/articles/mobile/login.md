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
- [Mobile review checklist](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
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


To acquire the access token you can use one of two OAuth flows; authorization code grant or implicit grant. Authorization code grant is used to obtain both access tokens and refresh tokens. Implicit grant is used to obtain access tokens (it does not support refresh tokens) and is typically implemented in a browser or webview using a scripting language such as Javascript.

For authorization code grant there are several different ways to authenticate the client. The different grant types used in SPiD are

* **password** Authenticate by username and password
* **client_credentials** Authenticate by client id and client secret
* **authorization_code** Authenticate by code
* **urn:ietf:params:oauth:grant-type:jwt-bearer** Used for third party token bearers. In SPiD’s case this is for Facebook or Google+ (Google+ login is only available for Android)
* **refresh_token** Used when you want to refresh your access token after it has expired

### Connecting using Facebook or Google+

If you want to use Facebook or Google+ to connect to SPiD use the urn:ietf:params:oauth:grant-type:jwt-bearer grant type. In addition to what you need for normal authentication you need to specify which third party provider you want to use for your login as well as identify your app to that provider. This is done through the use of a JSON web token, JWT. If you’re using the SPiD SDKs to develop your app you won’t have to create the JWT or specify the grant type as we take of it for you. See the code below for examples how to login using Facebook or Google+.

## Facebook

Before you get started you need to register your app for a Facebook application id at [developers.facebook.com](http://developers.facebook.com/).

# :tabs

## :tab Android

	// Session is a class from the Facebook API
	
	Session session = Session.getActiveSession();
	SPiDConfiguration config = SPiDClient.getInstance().getConfig();
	SPiDFacebookTokenRequest tokenRequest;
	try {
		tokenRequest = new SPiDFacebookTokenRequest(session.getApplicationId(), session.getAccessToken(), session.getExpirationDate(), new LoginListener());
            tokenRequest.execute();
    } catch (SPiDException e) {
            dismissLoadingDialog();
            Toast.makeText(config.getContext(), "Error creating login request", Toast.LENGTH_LONG).show();
    }
        
## :tab iOS

    SPiDTokenRequest *request = [SPiDTokenRequest
            userTokenRequestWithFacebookAppID:[FBSession activeSession].appID
                                facebookToken:[FBSession activeSession].accessTokenData.accessToken
                               expirationDate:[FBSession activeSession].accessTokenData.expirationDate
                            completionHandler:^(SPiDError *tokenError) {
                                if (tokenError) {
                                    if (tokenError.code == SPiDOAuth2UnknownUserErrorCode) {
                                        UIAlertView *alertView = [[UIAlertView alloc]
                                                initWithTitle:@"User does not exist"
                                    }
                                }
                            }
                                                
# :/tabs

## Google+

# :tabs

## :tab Android

First register your app in the Google Developers Console. A detailed guide of the steps that need to be taken can be found [here](https://developers.google.com/+/mobile/android/getting-started).

This example is simplified for brevity, for a working app using Google+ to register new SPiD users and logging in see the SPiDGooglePlusApp in the Android SPiD repository.

	// First we get a token from Google
	String token = GoogleAuthUtil.getToken(activity, Plus.AccountApi.getAccountName(googleApiClient), "oauth2:" + MainActivity.GOOGLE_PLUS_SCOPES);

    final SPiDGooglePlusTokenRequest tokenRequest = new SPiDGooglePlusTokenRequest(getPackageName(), token, new SPiDAuthorizationListener() {
    	@Override
        public void onComplete() {
        	SPiDLogger.log("SPiD login successful, access token received: " + SPiDClient.getInstance().getAccessToken().getAccessToken());
			// Switch to a login screen
        }

## :tab iOS

Not yet implemented for iOS.

# :/tabs

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
