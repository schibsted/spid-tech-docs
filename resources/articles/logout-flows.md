:title Explaining how logout flows works
:frontpage
:category how-tos
:aside
## Table of Contents

<spid-toc></spid-toc>

## See also

- [GET /logout](/endpoints/GET/logout/)
- [Logging out via the JavaScript SDK](/sdks/js-2x/api-docs/#logout)
- [Explaining how login flows work](/login-flows/)

:body

There are 3 different types of logout functionality available in the platform. Depending on your client application 
type and integration with Schibsted account, you may need to use one or several approaches. Below is an explanation of each logout 
type.

## Logging out of Schibsted account via the JavaScript SDK
Logging out via the JavaScript SDK deletes the current session for the logged in user based on the session cookie sent
in the logout request. After the logout is performed on Schibsted account the SDK will trigger 3 [events](/sdks/js-2x/events/#available-sdk-events) 
telling your application that the user has been logged out:
#### User logs out

- `SPiD.logout`
- `SPiD.statusChange`
- `SPiD.sessionChange`

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

It is also recommended to send user through logout redirect flow, as Safari users might not be fully logged out.

## Logging out of Schibsted account via the redirect flow
Logging out via the redirect flow, by redirecting the user to /logout in Schibsted account, will delete the current session for the 
logged in user based on the session cookie sent in the logout request.

You must provide the **client_id** as GET parameter, to make sure correct context is used.

You may provide **redirect_uri** as GET parameter and Schibsted account will redirect the user to that url after performing the logout procedure. This parameter must be previously defined and stored as a valid redirect uri for your client application. It must also be url encoded. 

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

## Use cases
Mobile applications or other applications that explicitly login the user via login APIs will not create a session in Schibsted account.
These types of applications should use the [logout endpoint](/endpoints/GET/logout/).

Applications that use the redirect login flow should use the redirect logout flow in order to actively logout the 
current user session. If your application also uses the JavaScript SDK, it is optional to use the logout functionality 
in the SDK in order to make the logout experience smoother by not requiring the user agent to perform redirects.
 
Providing the logged in user's oauth token to logout is recommended, but not required, as your application may want to
perform API calls on behalf of the user after logout. This depend on what backend functionality your application needs.
Usually it is recommended that all backend API calls are performed used a server token, where your application 
authenticates itself in order to perform API calls to SPiD. This simplifies your token handling logic and helps you
avoid handling a mixture of tokens in your backend business logic.
