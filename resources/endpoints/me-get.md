:introduction

This endpoint returns the currently logged in user's object. It requires an
active user session and a matching corresponding oauth token.

A session is only created when the user logs in through the web – not through
the native/Facebook login for mobile apps. Use the
[`/user/{userId}`](/endpoints/GET/user/{userId}) endpoint for those
cases.

This means that you can also use `/me` to check if the user has logged in
through the SPiD web site.

:relevant-endpoints

GET /user/{userId}
