:introduction

This endpoint serves two purposes:

* Retrieve the terms for SPiD or the client
* Check whether a user has accepted the SPiD or client terms

To check whether a user has accepted the terms, provide an oauth token. If the
user has accepted all the terms, the returned text will be empty, but `accepted`
will be `true`.
