:title Register

:aside

## Native mobile development

- [Overview](/mobile/overview)
- [Getting started](/mobile/mobile-development)
- Register
- [Login](/mobile/login)
- [Android](/sdks/android)
    - [Android sample apps](/sdks/android/sample-apps)
    - [API](/sdks/android/api)
- [iOS](/sdks/ios)
    - [iOS sample apps](/sdks/ios/sample-apps)
- [Access tokens](/mobile/access-tokens)
- [Reviews](/mobile/reviews)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices)
- [Migration](/mobile/migration)
- [Best practices](/mobile/best-practices)
- [FAQ](/mobile/faq)

## More about the Android SDK

- [API Docs](/sdks/android/api-docs/)
- [Javadoc](/sdks/android/javadoc)
- [Best practices](/sdks/android/best-practices/)
- [Reviews](/sdks/androis/reviews/)
- [OAuth for mobile clients](/oauth-authentication-on-mobile-devices)

:body

Before creating a user always check first to see if that user already exists. This is accomplished by checking to see whether the user's email address is already connected to a SPiD account using the  [/email/{email}/status](/endpoints/GET/email/%7Bemail%7D/status/) endpoint.

A user can be registered with SPiD using either the [/signup](/endpoints/POST/signup) or [/signup_jwt](/endpoints/POST/signup_jwt) endpoints. When a user registers for a SPiD account the [terms](/endpoints/GET/terms/) must be clearly visible. If they are not the app will be rejected upon review.

#### Signup code example

# :tabs

## :tab Android

Signing up is handled by the SPiDUserUtil utility class, using the signupWithCredentials method as shown below.

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
	
Note that the callback handler runs in a background process by default. If you need to do operations from the main thread you can use the SPiDUiThreadAuthenticationListener convenience class which is an empty implementation of the SPiDAuthenticationListener interface running the callbacks on the main thread on onCompleteAction() and onErrorAction().

To sign up using a Google+ or Facebook account refer to the examples below.

**Google**

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