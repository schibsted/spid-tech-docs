:title Payment method flow
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
- [Payment method flow] (/flows/payment-method-flow)

:body

The payment method flow can be used adding new payment methods and changing paymet method for subscriptions. It requires a logged in user, so it will start of in the [auth flow](/flows/auth-flow/).

**URL Path**: /flow/payment

# How it works

Flow is initiated by visiting the path `/flow/payment` with the required query parameters `client_id`, `redirect_uri` and `response_type`. When the flow ends it will redirect the user back to the `redirect_uri` with a added parameter `payment_id` which is id of the payment method the user selected. 
The optional parameters are: `methods` and `subscription_id`. The `methods` parameter determines which payment methods that should be available. Valid values can be found in the [this list](/types/order/#payment-options). To update the payment method for a specific subscription you can supply the id of the subscription in the `subscription_id` parameter.



### Examples

`https://login.schibsted.com/flow/payment?methods=2&client_id=[...]&redirect_uri=[...]&response_type=code`
`https://login.schibsted.com/flow/payment?methods=6&client_id=[...]&redirect_uri=[...]&response_type=code`
`https://login.schibsted.com/flow/payment?methods=6&subscription_id=123&client_id=[...]&redirect_uri=[...]&response_type=code`
