:title Passwordless Login
:frontpage
:category api-integration

:relevant-endpoints

POST /signin

:aside

## See also

- [Signin flow](/flows/auth-flow/)

:body

The basic idea is that instead of using a password to authenticate each user, a temporary secret code is sent to him/her over an e-mail.

It’s almost as if the backend server makes up a temporary, one-use password each time a user wants to log in.

### Configuration

There is no required configuration for this functionality.

### Passwordless Login

#### Links
* Norway - [https://payment.schibsted.no/flow/signin](https://payment.schibsted.no/flow/signin)

* Sweden - [https://login.schibsted.com/flow/signin](https://login.schibsted.com/flow/signin)

#### Description

Here is a complete overview of Passwordless Login functionality:

![Signin](/images/signin.png)

The Passwordless Login flow consists of these elements:

* an API endpoint for triggering a passwordless login flow
* user receives an email with a link (30 minutes expiration)
* once the user press the link: 
    * the user is logged in
    * if terms & agreements acceptance is needed, the user is presented with the acceptance step, otherwise this is skipped
* when logged in state is successful, the user is redirected back to the client provided and validated redirectUrl
