:title Migrating users to SPiD
:frontpage
:category self-service
:aside

## Table of Contents

<spid-toc></spid-toc>

:body

Congratulations on choosing to migrate users using the new SPiD Automated Migration Procedure (AMP).

## Summary

By using these guidelines, you will be able to implement a migration service and thereby start migrating users to SPiD.
This migration service is then used by SPiD in order to import users to the SPiD platform. In other words, you build a
service allowing SPiD access to all users in your system.

Every time a new user logs in through SPiD via your client Id, SPiD will make a request to your migration service to
import data you have for that user.

Below you will find documentation on how your migration service should work.

An illustration of how your migration service will be used by the SPiD platform, showing an example request and
response.

![Migration service](/images/migration-service.png)

A sample migration service implementation can be found on GitHub: https://github.com/schibsted/fake-migration-service.git

## Terminology

**Migration service**: What you implement in order to expose users to SPiD platform.
**Migration client**: The HTTP client used by SPiD platform to make requests to migration service.
**Migration user**: The JSON object sent in the response from migration service.
**Migration specification**: An object used by the migration client to make requests.
**UserData service**: A part of the SPiD platform where the Migration client lives.

## Migration service specification

Your migration service:

* must accept `GET` requests
* must accept “email” query parameter with a URL encoded value
* must respond with status code 404 if an email is not linked to a user
* must respond with a valid UTF-8 encoded JSON object representation of the user, called MigrationUser, and status code
200
* must respond within 2 seconds
* must be highly-available

## Migration specification

A migration specification is a set of values that tells the migration client:

* what endpoint to make the request to.
* what header key value to include in the request to your migration service.

You will tell us what endpoint and header key value to use by using the SPiD Self-Serve page and defining a migration
specification for your client Id. You can define the same migration for several client Ids.
The header key value will be included in every request to the migration service and can be used for authentication.

## The MigrationUser object

Below is  a JSON schema of the MigrationUser. An example of a MigrationUser can be seen in the illustration above and
in the example below.

Please note that:

* only the email field is required, all other fields can be omitted.
* invalid enum values will cause the migration to fail, while invalid locale, timezone, phone numbers and photo
will be removed by the migration client.
* all times, except birthday, are in ISO8601 RFC3339 UTC format.
* should a field not be available, please omit it from the JSON response.
* all data should be UTF-8 encoded.

```json
{
    "id": "urn:jsonschema:com:schibsted:spt:identity:userdataservice:models:MigrationUser",
    "properties": {
        "birthday": {
            "type": "string",
            "format": "DATE_TIME"
        },
        "displayName": {
            "type": "string"
        },
        "sex": {
            "type": "string",
            "enum": [
                "UNDISCLOSED",
                "FEMALE",
                "MALE"
            ]
        },
        "homePhone": {
            "type": "string",
            "description": "Format: +46761234567"
        },
        "fullName": {
            "type": "string"
        },
        "photo": {
            "type": "string",
            "description": "URL"
        },
        "timeZone": {
            "type": "string",
            "description": "Any accepted Java TimeZone ID"
        },
        "locale": {
            "type": "string",
            "description": "Any accepted Java Locale ID"
        },
        "userId": {
            "type": "string",
            "description": "The id of the user in your system",
        },
        "mobilePhone": {
            "type": "string",
            "description": "Format: +46761234567"
        },
        "createdTime": {
            "type": "integer",
            "format": "UTC_MILLISEC"
        },
        "email": {
            "type": "string",
            "required": true
        }
    }
}

```

## Example
Below is an example request / response for migrating the user `alex@dot.com` from your migration service endpoint
`https://migrate.example.com/user.php`.

Migration specification:

**endpoint:**   https://migrate.example.com/user.php
**header key:** X-Auth-Migrate
**header value:**   2C749A8E-4D07-4586-B4D3-F75C02F81342

Request from migration client to migration service:

```
GET /user.php?email=alex%40dot.com HTTP/1.1
Host: migrate.example.com
Content-Type: application/json
X-Auth-Migrate: 2C749A8E-4D07-4586-B4D3-F75C02F81342
```

Response body from migration service to migration client:

```json
{
  "email":          "alex@dot.com",
  "birthday":       "1956-12-15",
  "userId":         "56121585968",
  "displayName":    "elliem",
  "fullName":       "Alex Zander",
  "mobilePhone":    "+46761234567",
  "homePhone":      "+46812345678",
  "photo":          "http://img.com/alex.jpg",
  "createdTime":    "2015-08-06T12:10:36.339Z",
  "timeZone":       "Europe/Stockholm",
  "sex":            "FEMALE",
  "locale":         "sv_SE"
}
```

## Migration for the user
The migration for the user is initiated from the login view of the [Auth flow](/flows/auth-flow/),
forgot password view of the [Auth flow](/flows/auth-flow/), or the [Forgot password flow](/flows/password-flow/).

The login flow migration is done in 4 steps in total.

1.  The user logs in,
2.  verifies her email address,
3.  sets a new password and
4.  accepts the agreement.

The forgot password flow is the same flow as the common forgot password flow

1.  The users enter her email,
2.  clicks the link in the recieved mail,
3.  sets a new password and verifies that email in one step,
4.  logs in and accepts agreement.

**Note:** The [Checkout flow](/flows/checkout-flow/) starts with the [Auth flow](/flows/auth-flow/) in is thus covered by migration.
