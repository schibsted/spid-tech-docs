:introduction

This endpoint serves two purposes:

* Retrieve the terms for SPiD or the client
* Check whether a user has accepted the SPiD or client terms

To check whether a user has accepted the terms, provide an oauth token. If the
user has accepted all the terms, the returned text will be empty, but `accepted`
will be `true`.

## Accepting terms

When the user is signed up through [the API](/endpoints/POST/user_signup), terms
are automatically accepted, and the client must make sure to ask the user if
they accept the terms prior to calling this endpoint.

When users sign up through SPiD's own UI, they will be prompted to read and
accept the terms.
