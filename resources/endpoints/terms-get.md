:introduction

This endpoint serves two purposes:

* Retrieve the terms for SchAcc or the client
* Check whether a user has accepted the SchAcc or client terms

To check whether a user has accepted the terms, provide an oauth token. If the
user has accepted all the terms, the returned text will be empty, but `accepted`
will be `true`.

## Accepting terms

When the user is signed up through [the API](/endpoints/POST/signup/), terms
are automatically accepted, and the client must make sure to ask the user if
they accept the terms prior to calling this endpoint.

When users sign up through SchAcc's own UI, they will be prompted to read and
accept the terms.

:relevant-endpoints

POST /user
POST /signup
