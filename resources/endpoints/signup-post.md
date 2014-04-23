:introduction

Create a new user with minimal data, normally used in mobile clients.

The email is globally unique in SPiD, and there can
only be one account associated with any given email address. To query
for the availability of an email address, see the
[email status endpoint](/endpoints/GET/email/{email}/status).

When the user is signed up through this endpoint, terms are
automatically accepted, and the client must make sure to ask the user
if they accept [the terms](/endpoints/GET/terms/) prior to calling
this endpoint.

:relevant-endpoints

POST /user
GET /terms
GET /email/{email}/status
