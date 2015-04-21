:title Self-service authentication
:frontpage
:category self-service
:body


In order to access self-service. You will have to be an authenticated SPiD user. You will also need to be added to the self-service access control list for your company. This is nothing you as a user currently can achieve by yourself and you will have to turn to SPiD support (support@spid.no) in order to gain access.

### Quick facts

#### Prod environments

**COM PROD**: [http://login.schibsted.com/selfservice](http://login.schibsted.com/selfservice)

**SE PROD**: [http://payment.schibsted.se/selfservice](http://payment.schibsted.se/selfservice)

**NO PROD**: [http://payment.schibsted.no/selfservice](http://payment.schibsted.no/selfservice)

#### Stage environments
**SE STAGE**: [http://stage.payment.schibsted.se/selfservice](http://stage.payment.schibsted.se/selfservice)

**NO STAGE**: [http://stage.payment.schibsted.no/selfservice](http://stage.payment.schibsted.no/selfservice)

## Gaining access to self-service

As mentioned above you need to send an email to the SPiD support in order to gain access. Before the support personnel have recieved your request there are a few things you should think about.

Self-service is a very powerful tool that could potentially make the logins for all the companys clients to stop working so always be very selective in what and how many people gains access to the self-service system.

When you are ready to send the email, include the following:

1. Your email address which is registered to your SPiD account, this **must** be an email from your workplace. We're not going to give you access with a gmail or hotmail address!
2. CC your closest manager, boss or chief that can confirm that you should be given access to this system. The person giving consent should of course have some kind of responsibility for the technical aspects of your clients. Preferably a person that has used SPiD before.
3. Specify which company / companies you should get access to.
4. Specify which enviroments you should get access to.

When we recieve the mail and handle your request you should be able to logon to self-service.

### Help! I JUST started out and I don't know about environments?!

Take a chill pill and relax. Do your company currently have clients in production that uses SPiD?
Ask your manager which environment they are under, or simply try to login to the clients then you will know what environment they should go under.

If the answer to the question is "no" and you just started out, you should get access to SE Stage since there is no COM Stage (as of this writing).



## Becoming a "VIP" in SPiD

Since you are now basically a VIP you will notice that you can't still access the SPiD self-service system from the links above. It tells you to enter some kind of weird code. This code is a two factor authentication and can be setup from you account pages.

In order to access self-service you will need an app for you mobile phone or desktop that can handle TOTP-codes (Timed-based One Time Passwords). Our suggestion is Google Authenticator but there is loads of other apps out there for free.

Simply visit the account pages and under /account/two-factor-authentication you will find further instructions on how to activate. When this is finished, you can login with your newly setup authenticator by clicking on the correct link above!

Congratulations you are now a "VIP" user in SPiD ;)