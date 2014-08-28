:title Cancel redirects
:frontpage
:category api-integration
:body

When a user cancels a login attempt, or a purchase flow, SPiD can redirect them
to specific "cancel" URL. These "cancel redirect" URLs afford the client more
fine-grained flow control. If a cancel redirect URL is not provided, the default
(and required) redirect URL will be used instead.

## Ways to provide cancel redirects in SPiD

The cancel redirect URL may be provided dynamically in the initial request to
SPiD using the query parameter `cancel_redirect_uri`. The value of this
parameter must be a valid, URL encoded, redirection url pointing back to the
client. We will validate that the domain matches the registered domain for the
client as well.

The cancel redirect URL may also be established in advance. When that is the
case, it will always be used, regardless of the `cancel_redirect_uri` parameter.
This behavior is a security feature, designed to prevent phising.

## Cancel response

When SPiD redirects the user to the cancel redirect URL, it will add a query
parameter, `spid_page`. The parameter contains the name of the page where the
user aborted the process. Possible values are:

- authenticate
- choose payment
- choose product
- forgotpassword
- login
- required fields
- signup
- terms
- verify
