:introduction

The Simple Object Data API, or SODA, is an API extension enabling storage of
simple JSON objects where a unique key can be associated with a client-provided
JSON object.

By making an HTTP GET to this endpoint, you can retrieve previously
[stored](/endpoints/POST/{type}/{id}/do/{key}) data objects.

:relevant-endpoints

POST /{type}/{id}/do/{key}
POST /{type}/{id}/{subtype}/{subid}/do/{key}
GET /{type}/{id}/{subtype}/{subid}/do/{key}
