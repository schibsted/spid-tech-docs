:title Explaining how logout flows works
:frontpage
:category how-tos
:aside
## Table of Contents

<spid-toc></spid-toc>

## See also

- [GET /logout](/endpoints/GET/logout/)
- [Logging out via the JavaScript SDK](/sdks/js/api-docs/#logout)
- [Explaining how login flows work](/login-flows/)

:body

There are 3 different types of logout functionality available in the platform. Depending on your client application 
type and integration with SPiD, you may need to use one or several approaches. Below is an explanation of each logout 
type.

## Logging out of SPiD via the Javascript SDK
Logging out via the Javascript SDK deletes the current session for the logged in user based on the session cookie sent
in the logout request. After the logout is performed on SPiD the SDK will trigger 3 [events](/sdks/js/events/#available-sdk-events) 
telling your application that the user has been logged out:
#### User logs out

- `auth.logout`
- `auth.statusChange`
- `auth.sessionChange`

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

## Logging out of SPiD via the redirect flow
Logging out via the redirect flow, by redirecting the user to /logout in SPiD, will delete the current session for the 
logged in user based on the session cookie sent in the logout request.

You must provide the **client_id** as GET parameter, to make sure correct context is used.

You may provide **redirect_uri** as GET parameter and SPiD will redirect the user to that url after performing the logout procedure. This parameter must be previously defined and stored as a valid redirect uri for your client application. It must also be url encoded. 

You may also provide the **oauth_token** via GET parameter, owned the currently logged in user and SPiD will revoke the token, meaning no API requests will be allowed using that same token afterwards. 

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

## Logging out of SPiD via the API
Logging out via the API doesn't delete any sessions on any devices used by the user, except the applications used by
the user utilising the provided oauth token sent to the [logout endpoint](/endpoints/GET/logout/). 
The API is meant to invalidate the current user token used by your application, thus effectively logging the user out 
from your application. The SPiD platform will revoke the token, meaning no API requests will be allowed using that 
same token afterwards.

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

## Use cases
Mobile applications or other applications that explicitly login the user via login APIs will not create a session in SPiD.
These types of applications should use the [logout endpoint](/endpoints/GET/logout/).

Applications that use the redirect login flow should use the redirect logout flow in order to actively logout the 
current user session. If your application also uses the Javascript SDK, it is optional to use the logout functionality 
in the SDK in order to make the logout experience smoother by not requiring the user agent to perform redirects.
 
Providing the logged in user's oauth token to logout is recommended, but not required, as your application may want to
perform API calls on behalf of the user after logout. This depend on what backend functionality your application needs.
Usually it is recommended that all backend API calls are performed used a server token, where your application 
authenticates itself in order to perform API calls to SPiD. This simplifies your token handling logic and helps you
avoid handling a mixture of tokens in your backend business logic.
