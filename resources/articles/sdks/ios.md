--------------------------------------------------------------------------------
:title iOS SDK
:frontpage
:category
:aside

## Native web development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Self Service](/mobile/selfservice/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- iOS
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Mobile review checklist](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

The iOS SPiD SDK is built on top of the backend SDK to provide access to functions with minimal effort from 
the developer. Connecting to the SPiD can be done either natively, using a WebView, browser redirect or connecting via
Facebook.

#### SPiDClient

SPiDClient is a singleton used in the SDK to store information about the client using the SDK. Credentials such as client id, client secret as well as the app's url scheme and which server is being used is stored in the SPiDClient. After a user logs in the access token is also accessible in the SPiDClient, stored encrypted in the keychain and loaded automatically on app start if there is one available.

To initialize and setup the SPiDClient singleton call

	SPiDClient.setClientID:(NSString *)clientID
    	clientSecret:(NSString *)clientSecret
    	appURLScheme:(NSString *)appURLSchema
    	serverURL:(NSURL *)serverURL

Your client id and app url scheme can be found after creating your client in [self service](http://techdocs.spid.no/selfservice/access/). After creating your client you can set your client secret. Be sure to treat this secret securely and do not send it in plain text on insecure channels such as e-mails.

This must be done before the SDK can be used, and can only be called once. After the SPiDClient has been configured you can use the SPiDTokenRequest class to send login requests to SPiD.

#### SPiDTokenRequest

Each supported grant type has its own method call in SPiDTokenRequest as shown below. Calling these methods creates a SPiDTokenRequest but does not execute it.

Grant type **password**

        @param username The username
        @param password The password
        @param completionHandler Called on token request completion or error
        @return The SPiDTokenRequest, call startRequest to execute

        + (instancetype)userTokenRequestWithUsername:(NSString *)username password:(NSString *)password completionHandler:(void (^)(SPiDError *error))completionHandler;

Grant type **authorization_code**


        @param code The authorization code
        @param completionHandler Called on token request completion or error
        @return The SPiDTokenRequest, call startRequest to execute

        + (instancetype)userTokenRequestWithCode:(NSString *)code completionHandler:(void (^)(SPiDError *))completionHandler;
    
Grant type **urn:ietf:params:oauth:grant-type:jwt-bearer**

   
        @param appId Facebook appID
        @param facebookToken Facebook access token
        @param expirationDate Expiration date for the facebook token
        @param completionHandler Called on token request completion or error
        @return The SPiDTokenRequest, call startRequest to execute

        + (instancetype)userTokenRequestWithFacebookAppID:(NSString *)appId facebookToken:(NSString *)facebookToken expirationDate:(NSDate *)expirationDate completionHandler:(void (^)(SPiDError *))completionHandler;
    
Grant type **refresh_token**


        @param completionHandler Called on token request completion or error
        @return The SPiDTokenRequest, call startRequest to execute

        + (instancetype)refreshTokenRequestWithCompletionHandler:(void (^)(SPiDError *))completionHandler;
        
The supplied completion handler is called when the request completes. If the request was successful the SPiDError used as parameter is nil, if an error occurred the SPiDError includes information on what went wrong. Always check if the request was successful or not before proceeding. When the user successfully logs in the access token is available in SPiDClient and stored in the keychain automatically.

To execute a SPiDTokenRequest call ```startRequest```.

For detailed examples with source check the [example apps](/sdks/ios/sample-apps/).