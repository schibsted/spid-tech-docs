:title Auth, Login, Signup and Signin flow
:frontpage
:category flows
:aside
## Flows
- [About flows](/flows/flows/)
- [Auth/Login/Signup/Signin flow](/flows/auth-flow/)
- [Checkout flow](/flows/checkout-flow/)
- [Forgot password flow](/flows/password-flow/)
- [Profile update flow](/flows/profile-update-flow/)
- [P2P payment flow](/flows/p2p-checkout-flow/)
- [Withdraw flow](/flows/withdraw-checkout-flow/)

:body

The auth flow is used for login and signup. It has 4 different initiation urls, and contains several steps based on client configuration.
You can initiate the flow by either using path

* /flow/login, to render a login view
* /flow/signup, to render a signup view
* /flow/auth, to render the either signup or login, based on if we recognize the user or not (recommended)
* /flow/signin, to render a signin view and let the user login without credentials

**URL Path**: /flow/auth, /flow/login, /flow/signup, /flow/signin,

## How it works
The login flow can be just one step, or it can be more. It depends on the current state of the user, and client configuration.

* Step 1: Login or Signup
* Step 2: Verify step, only shown if user is unverified
* Step 3: Two factor authentication, only shown if client configuration require it
* Step 4: Accept agreement, only shown if user has not accepted the latest agreement
* Connect to client, an invisible step where user gets connected to client
* Step 5: Required fields step, only shown if client require fields user have not filled
* Step 6: Verify phone step, only shown if client configuration require it
* Step 7: Notifications step, not currently in use

Flow is initiated by path `/flow/auth`, `/flow/login` or `/flow/signup` and require query parameters `client_id`, `redirect_uri` and `response_type`.
Optional query parameters include, among others, `cancel_redirect_uri`.

### Signin flow
Sign-in flow is a slight exception. It lets user to log in without providing his/hers credentials by using an unique, one at a time, valid for 30 minutes token.

* Step 1: Signin view with e-mail input OR [correct API endpoint](/endpoints/POST/signin/)
* Step 2: Receiving e-mail
* Step 3: Accessing confirmation page
* Step 4: Verify step, only shown if user is unverified
* Step 5: Accept agreement, only shown if user has not accepted the latest agreement
* Connect to client, an invisible step where user gets connected to client
* Step 6: Required fields step, only shown if client require fields user have not filled
* Step 7: Verify phone step, only shown if client configuration require it


## How it looks
Client teaser and Custom CSS is applied to all of the steps. See [Flows](/flows/flows/) for more info.
