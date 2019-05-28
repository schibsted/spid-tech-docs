:title Explaining how logout flows works
:frontpage
:category how-tos
:aside
## Table of Contents

<spid-toc></spid-toc>

## See also

- [GET /logout](/endpoints/GET/logout/)
- [Logging out via the JavaScript SDK](https://github.com/schibsted/account-sdk-browser#logging-out)
- [Explaining how login flows work](/login-flows/)

:body

There are two different types of logout functionality available in the platform. Depending on your client application 
type and integration with Schibsted account, you may need to use one or several approaches. Below is an explanation of each logout 
type.

## Logging out of Schibsted account via the JavaScript SDK
[Logging out via the JavaScript SDK](https://schibsted.github.io/account-sdk-browser/Identity.html#logout)
deletes the current session for the logged in user based on the session cookie sent
in the logout request. After the logout is performed on Schibsted account the SDK will trigger a number of
[events](https://schibsted.github.io/account-sdk-browser/Identity.html#event:logout) 
telling your application that the user has been logged out.

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

## Logging out of Schibsted account via the redirect flow
Logging out via the redirect flow, by redirecting the user to `/logout` in Schibsted account, will delete the current session for the 
logged in user based on the session cookie sent in the logout request.

You must provide the **client_id** as GET parameter, to make sure correct context is used.

You may provide **redirect_uri** as GET parameter and Schibsted account will redirect the user to that url after performing the logout procedure. This parameter must be previously defined and stored as a valid redirect uri for your client application. It must also be url encoded. 

It is then up to your application to perform session cleaning and logout procedures needed to actively log out the user
from your application.

## Use cases
If your application uses the JavaScript SDK it should use the logout functionality that SDK. If you're manually
performing the redirect login flow (which should be avoided), use the redirect logout flow in order to actively logout
the current user session. 
