:title Explaining how login flows work
:frontpage
:category how-tos
:aside

## See also

- [JavaScript SDK](/sdks/javascript/)
- [Explaining how logout flows work](/logout-flows/)

:body

The auto-login works from ANY site in the Schibsted account ecosystem. Meaning that the user is logged in to Schibsted account,
not a specific site. If the user has a connection to that site upon visiting it, and have accepted the
terms of use for that site, then the Schibsted Account responds with who is logged in.

As an example:
<div class="mod warning">
  <div class="bd">
    <p class="mhm">
      I have a Schibsted account account and visit Finn.no, I am automatically logged in to Schibsted account and since I have
      accepted the terms of Finn before, I automatically get a session on Finn and can access my Finn account.
    </p>
    <p class="mhm">
      I then visit VG.no. Upon which I haven't used before or still haven't accepted their terms of use.
      I am still logged in on Schibsted account (or auto-logged-in if my session has timed out), but this time the
      Schibsted Account doesn't say who is logged in.
    </p>
  </div>
</div>

Users are automagically logged in to Schibsted account, but not on all services using Schibsted account.
The reasons for that can be:

* The user doesn't have a relationship with that site. Meaning that the user has never used it or have never
accepted the terms of use for that service yet. Once that happens, the user will be automatically logged in
on that service (depending on the integration type employed by the site itself). This is "by design" as it is
a requirement that users accept that their data is shared with that service before the service can access the
user's information.
* Users may not be auto-logged in if they explicitly log out from Schibsted account **OR** if they uncheck the "remember me"
checkbox when logging into Schibsted account. This is by design.

There are two kinds of integration between Schibsted account and client services. One uses the JavaScript SDK, which has
the auto-login functionality required to identify and show who is logged in. The other depends on the redirect
login flow, where the user needs to click a login button in order to be logged in.

When client services uses the redirect login flow and send the user to Schibsted account for login, we trigger one of these 3 flows:

* If Schibsted account recognize the user and the user chose to be remembered:
    1. Auto-login the user
    2. Ask the user to accept site B terms of use (if the user haven't accepted site B's TOS yet)
* If Schibsted account recognize the user and the user chose NOT to be remembered:
    1. Ask the user to login (if user doesn't have a session)
    2. Ask the user to accept site B terms of use (if the user haven't accepted site B's TOS yet)
* If Schibsted account doesn't recognize the user (Schibsted account cookie not found):
    1. Ask the user to sign up (signup requires user to accept Schibsted account and site B terms of use)

This is a complete overview of Single Sign On using JS SDK, explaining the complete process between the client
service (orange), the JS SDK (white) and the Schibsted Account (blue).
![Single Sign On complete overview using JS SDK, cookies and redirects](/images/sso-usecases.png)

Here is a complete overview of how Schibsted account handles the login/signup process internally using the redirect login flow:

![Signup & Login flows](/images/signup-login-flows.png)
