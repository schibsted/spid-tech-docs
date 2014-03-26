:introduction

This endpoint returns the currently logged in user's object. It requires an
active user session and a matching corresponding oauth token.

A session is only created when the user logs in through the web – not through
the native/Facebook login for mobile apps. Use the
[`/user/{id/userId}`](/endpoints/GET//user/{id/userId}) endpoint for those
cases.
