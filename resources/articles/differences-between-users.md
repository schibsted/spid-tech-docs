:title Differences between verified users, registered users and users that have accepted a client agreement
:frontpage
:category how-tos
:aside
## See also

- [POST /user](/endpoints/POST/user/)
- [POST /signup](/endpoints/POST/signup/)
- [POST /signup_jwt](/endpoints/POST/signup_jwt/)

:body

## Registered users
Registered users means users that were imported or signed up via the Schibsted account signup page or were created via our APIs 
by a client.

## Verified users
Verified users means users that have verified their email by going through the email verification process. 
This process is required by both users that have signed up or have been imported to Schibsted account.

## What does it mean when a user has accepted a client's agreement?
When signing up via the signup page in Schibsted account, it is required that user's actively accept the terms of use of Schibsted account 
and the client they are signing into. This is done by checking a checkbox. All registered users that signs up through 
this process have accepted an agreement. It doesn't mean they are verified yet. To become verified they must go through 
the email verification process.

When signing up via SPID's signup APIs, the user agreement is automatically accepted. It is required that 
clients informs users of this on the signup page or mobile application.

When creating users via the POST /user API, the user agreement is not automatically accepted. This is done after 
the email verification process.

**There is no correlation between a user having a verified status and the acceptance of a client agreement.** 
