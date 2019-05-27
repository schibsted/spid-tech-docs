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
- [Payment method flow](/flows/payment-method-flow/)

:body

This flow lets the user communicate a payment method id to you for such purposes as direct charge (where you create the orders). The user will be asked to add a new payment method or select on they have already stored in Schibsted account, and the ID will be returned to you as part of the redirect uri.

It requires a logged in user, so it will start of in the [auth flow](/flows/auth-flow/).

**URL Path**: /flow/payment

# How it works

Flow is initiated by visiting the path `/flow/payment` with the required query parameters `client_id`, `redirect_uri` and `response_type`.  The optional parameters are: `methods` and `subscription_id`.

The `methods` parameter determines which payment methods that should be available. Valid values can be found in the [this list](/types/order/#payment-options).

### Examples

#### Credit card only
`https://login.schibsted.com/flow/payment?methods=2&client_id=[...]&redirect_uri=[...]&response_type=code`

#### Credit card and SMS
`https://login.schibsted.com/flow/payment?methods=6&client_id=[...]&redirect_uri=[...]&response_type=code`


### Updating Schibsted account subscriptions

This flow can also be used to manage the payment method of Schibsted account subscriptions. In this case there is no need to act on the outgoing payment method id, but the subscription id (preferrably the "original subscription id") must be added as a flow start parameter (so Schibsted account will know which subscription to update) as a `subscription_id` parameter.

### Example

`https://login.schibsted.com/flow/payment?methods=6&subscription_id=123&client_id=[...]&redirect_uri=[...]&response_type=code`

## Results

Like all Schibsted account flows, the user will, upon successful completion of the flow, be sent to the `redirect_uri` along with a `code` parameter (that must be consumed as per all flows to identify the returning user), but also with an added parameter `payment_id` which is id of the payment method the user selected.

