:title P2P Checkout flow
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
- [Payment method flow] (/flows/payment-method-flow/)

:body

The checkout flow is used for p2p transfers. It requires a logged in user, so it will start of in the [auth flow](/flows/auth-flow/).

**URL Path**: /flow/p2p

## How it works
The checkout flow can be just a few steps, or it can be more. It depends on the current state of the user, client configuration, and query parameters.

It starts with the auth flow, and then appends the following steps.

* Just password
* Bank ID verification and P2P agreement check
* Choose payment method
* Enter payment details, only shown if user chooses new payment method
* Two Factor Authentication
* Process payment
* Receipt, optional, default enabled

Flow is initiated by path `/flow/p2p` and require query parameters `client_id`, `redirect_uri` and `response_type`. It also
requires the query parameter `paylink` which must be the paylink code for a valid paylink generated through the Paylink API.

The flow automatically use the login view. This can however be overridden by appending the parameter `signup=1` to the url, which will render the signup view instead.

## How it looks
Client teaser and Custom CSS is applied to all of the steps. See [Flows](/flows/flows/) for more info.