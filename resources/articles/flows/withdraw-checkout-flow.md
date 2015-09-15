:title Withdraw Checkout flow
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

The withdraw checkout flow is used for withdrawing money. It requires a logged in user, so it will start of in the [auth flow](/flows/auth-flow/).

**URL Path**: /flow/withdraw

## How it works
The withdraw checkout flow can be just a few steps, or it can be more. It depends on the current state of the user, client configuration, and query parameters.

It starts with the auth flow, and then appends the following steps.

* Enter withdraw amount
* Choose payment method
* Enter payment details, only shown if user chooses new payment method
* Process payment
* Receipt, optional, default enabled

Flow is initiated by path `/flow/withdraw` and require query parameters `client_id`, `redirect_uri` and `response_type`.

The flow automatically use the login view. This can however be overridden by appending the parameter `signup=1` to the url, which will render the signup view instead.

## How it looks
Client teaser and Custom CSS is applied to all of the steps. See [Flows](/flows/flows/) for more info.