:introduction

Create a new user with minimal data, normally used in mobile clients.

The email is globally unique in SPiD, and there can
only be one account associated with any given email address. To query
for the availability of an email address, see the
[email status endpoint](/endpoints/GET/email/{email}/status/).

:relevant-endpoints

POST /user
GET /terms
GET /email/{email}/status
