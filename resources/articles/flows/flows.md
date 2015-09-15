:title Flows
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

### Customization

- [Client Teaser](/selfservice/client-teaser-documentation/)
- [Customize CSS](/selfservice/customize-css/)

:body

SPiD flows are separated into different flows, that can be trigged by you by using different urls.
The login flow for instance, is triggered with an url path starting with /flow/login or /flow/auth.

## Intro
We developed our new flow engine to

* reduce redirects in the login flow,
* update design, to reduce the SPiD branding,
* give more control to the client over design,
* and to make it more maintainable and extendable by us in development.

## How it looks
During the development of the new flows, we worked with a prototype, that we've made available to you.
It can be used to get a sense of how it looks and works, and give you an idea how you can style the flow yourself.
This prototype is available at http://preview.spid.se. Each row is a variant of a flow found in SPiD.

## Customization
Customization to the design can be made in Self Service. You are able to edit

* background color and text color outside the frame,
* the teaser view to the left,
* and the text shown below the login box.

If you do not configure a teaser, you will get a slimmed down view throughout the whole flow, which looks like this

![Slim view](/images/client_teaser/missing_teaser.png)