:title Forgot password flow
:frontpage
:category flows
:aside
## Flows
- [About flows](/flows/flows/)
- [Auth/Login/Signup flow](/flows/auth-flow/)
- [Checkout flow](/flows/checkout-flow/)
- [Forgot password flow](/flows/password-flow/)
- [Profile update flow](/flows/profile-update-flow/)
- [P2P payment flow](/flows/p2p-checkout-flow/)
- [Withdraw flow](/flows/withdraw-checkout-flow/)

:body

The forgot password flow was mainly developed for native apps, to have a step right into the forgot password flow, without first showing the login page.
It also fixes the issue where forgot password could not be made when an user was logged in.

**URL Path**: /flow/password

## How it works
The flow consists of 3 or 4 steps. Before those steps there are however an invisible one where the current logged in user, if any, is logged out.
The flow follow these steps

* User, if any logged in, is logged out
* Step 1: The enter email view, where user requests a new password email
* Step 2: The email, which contains a link to be clicked
* Step 3: The reset password view, where a new password can be set
* Step 4: The confirmation view, where user gets a confirmation of the password change (optional, default disabled)
* User is redirected back to client / app, not logged in and without authorization code

The last step of that flow is however optional, and **not** shown by default.
To trigger step 4, you need to initiate the flow with the query parameter `confirmation=true`.

Flow is initiated by path `/flow/password` and require query parameters `client_id`, `redirect_uri`.
Optional query parameters include, among others, `confirmation`, `cancel_redirect_uri`.


## How it looks
Client teaser and Custom CSS is applied to all of the steps. See [Flows](/flows/flows/) for more info.