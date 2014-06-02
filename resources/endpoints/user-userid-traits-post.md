:introduction

Add traits for user. Traits are used for Mixpanel analytics integration. Refer
to the [Mixpanel analytics integration guide](/mixpanel-integration-guide/) for
more information on the data format of the traits JSON-object and other aspects
of the integration.

When `POST`-ing to this endpoint, property names in the JSON object that have
previously been added to the user will be overwritten. Pre-existing properties
that are not part of the `POST` will not be removed or overwritten. This means
that you can send only the properties you want to add or update. There is no
need to re-`POST` existing traits.

:relevant-endpoints

GET /user/{userId}/traits
DELETE /user/{userId}/trait/{trait}
