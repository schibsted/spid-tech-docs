:title Register

:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Self Service](/mobile/selfservice/)
- Register
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Mobile review checklist](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

Before creating a user always check first to see if that user already exists. This is accomplished by checking to see whether the user's email address is already connected to a SPiD account using the  [/email/{email}/status](/endpoints/GET/email/{email}/status/) endpoint.

A user can be registered with SPiD using either the [/signup](/endpoints/POST/signup/) or [/signup_jwt](/endpoints/POST/signup_jwt/) endpoints. When a user registers for a SPiD account the [terms](/endpoints/GET/terms/) must be clearly visible. **If they are not the app will be rejected upon review.**

#### Signup code example

# :tabs

## :tab Android

Signing up is handled by the SPiDUserUtil utility class, using the signupWithCredentials method as shown below.

	SPiDUserUtil.signupWithCredentials(final String email, final String password, final SPiDAuthorizationListener authorizationListener)

    SPiDUserUtil.signupWithCredentials("example.address@example.com", "MyPassword", new SPiDAuthenticationListener {    
    	@Override
    	public void onComplete() {
        	Log.i(TAG, "signupWithCredentials completed successfully");
    	}

    	@Override
    	public void onError(final Exception exception) {
        	Log.w(TAG, "signupWithCredentials failed", exception);
    	}
	}
	
Note that the callback handler runs in a background process by default. If you need to do operations on the main thread you can use the SPiDUiThreadAuthenticationListener convenience class which is an empty implementation of the SPiDAuthenticationListener interface running the callbacks on the main thread on onCompleteAction() and onErrorAction().

To sign up using a Google+ or Facebook account refer to the examples below.

**Google**

To use Google+ login you first need to [enable the Google+ API](https://developers.google.com/+/mobile/android/getting-started) in the Google Developer Console.

Then register your usage of Google Play Services in your app by adding the following to your AndroidManifest.xml

        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />

Once Google+ login is enabled for your app a new SPiD user can be registered using Google+ with the following code:

	SPiDUserUtil.signupWithGooglePlus(final String appId, final String googlePlusToken, final SPiDAuthorizationListener authorizationListener)

	SPiDUserUtil.signupWithGooglePlus(getPackageName(), token, new SPiDAuthenticationListener() {
    	@Override
    	public void onComplete() {
    		Log.i(TAG, "signupWithGooglePlus completed successfully");
		}

    	@Override
    	public void onError(Exception exception) {
    		Log.w(TAG, "Exception when attempting to creating user from Google Plus token", exception);
    	}
    });
    
If you have a user logged in to SPiD and want to associate that user's Google+ account to his/her SPiD account you can call the following method:

	SPiDUserUtil.attachGooglePlusAccount(final String appId, final String googlePlusToken, final SPiDAuthorizationListener authorizationListener)

    SPiDUserUtil.attachGooglePlusAccount(getPackageName(), token, new SPiDAuthenticationListener() {
    	@Override
        public void onComplete() {
    		Log.i(TAG, "User associated with Google+ account");
        }

        @Override
        public void onError(Exception exception) {
    		Log.w(TAG, "Exception when attempting to associate user with Google Plus account", exception);
        }
    });
    
**Facebook**

To add Facebook login to your app first enable it on developers.facebook.com, a detailed tutorial can be found [here](https://developers.facebook.com/docs/android/login-with-facebook/v2.2).

Before adding code make sure you register your Facebook application id in your AndroidManifest.xml (with your facebook_app_id as a string your strings.xml file).

        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />

The following code snippet shows an example of how to sign up a user to SPiD using Facebook

	SPiDUserUtil.signupWithFacebook(final String appId, final String facebookToken, final Date expirationDate, final SPiDAuthorizationListener authorizationListener)

    Session facebookSession = Session.getActiveSession();
    SPiDUserUtil.signupWithFacebook(facebookSession.getApplicationId(), facebookSession.getAccessToken(),
    	facebookSession.getExpirationDate(), new SPiDAuthenticationListener() {
    		@Override
            public void onComplete() {
    			Log.i(TAG, "signupWithGooglePlus completed successfully");
            }

            @Override
            public void onError(Exception exception) {
            	Log.w(TAG, "Exception when attempting to creating user from Facebook account", exception);
            }
        });
    }
    
To associate a logged in user's SPiD account to his/her Facebook account:

	SPiDUserUtil.attachFacebookAccount(final String appId, final String facebookToken, final Date expirationDate, final SPiDAuthorizationListener authorizationListener)

    Session session = Session.getActiveSession();    
    SPiDUserUtil.attachFacebookAccount(session.getApplicationId(), session.getAccessToken(), session.getExpirationDate(),
    		new SPiDAuthenticationListener() {

        @Override
        public void onComplete() {
    		Log.i(TAG, "User associated with Facebook account");
        }

        @Override
        public void onError(Exception exception) {
    		Log.w(TAG, "Exception when attempting to associate user with Facebook account", exception);
        }
    });

## :tab iOS

TODO: Add iOS code examples

# :/tabs