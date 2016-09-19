:title Update profile flow
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

The update profile flow is used for editing your profile details. It requires a logged in user, so it will start of in the [auth flow](/flows/auth-flow/).

**URL Path**: /flow/profile

## How it works

The profile flow can be just a single steps, or it can be more. It depends on the current state of the user, client configuration, and query parameters.

It starts with the auth flow, and then appends the following step.

* Update profile details

It then redirects to the redirect_uri provided in the initiation.

Flow is initiated by path `/flow/profile` and require query parameters `client_id`, `redirect_uri` and `response_type`.
Optional query parameter is `fields`, which supports the following values, in a comma seperated list:

 * `full_name`
 * `display_name`
 * `gender`
 * `birthday`
 * `locale`
 * `address`

### As a shorhand, these tags include a set of fields

 * `basic` :  full_name, display_name, gender, birthday
 * `full` : full_name, display_name, gender, birthday, locale, address
 * `names` :  full_name, display_name

### Examples

`https://login.schibsted.com/flow/profile?fields=names,locale&client_id=345[...]`
`https://login.schibsted.com/flow/profile?fields=full&client_id=345[...]`

## How it looks

![User Profile flow](/images/user-profile-update-flow.png)
