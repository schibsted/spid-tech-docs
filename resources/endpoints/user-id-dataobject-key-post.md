:introduction

Store data objects associated with this user. The data objects API is a
general-purpose key-value store for storing user meta data. Data objects can be
any kind of data. SPiD does not know assume any knowledge about the type or
purpose of the values, and any type information, de-serializing etc must be done
on the client.

:relevant-endpoints

GET /dataobjects
GET /user/{id}/dataobjects
GET /user/{id}/dataobject/{key}
DELETE /user/{id}/dataobject/{key}
