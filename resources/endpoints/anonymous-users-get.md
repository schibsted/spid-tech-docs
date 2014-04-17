:introduction

Retrieve an anonymized list of users that was created or updated in the given
period.

Users that have been removed will be returned as an object with just the id and
`deleted` set to `true`.

**NB!** Do not use `until=NOW` with pagination, as the paginated sequence will
never end.

## Anonymous user data

The data returned from this endpoint is pulled from users' public profiles, and
except for the `uid` (which is the user's `userId`), no data can be used to
uniquely identify users. As such, this data is suitable for aggregating into
user segments.

Note that most clients can use the data returned to retrieve full user profiles
from the [GET /user/{userId} endpoint](/endpoints/GET/user/{userId}). The
purpose of the anonymized form of the returned data in this endpoint is that it
can be freely used in further calculations without fear of compromising your
user's identity - so long as the `uid` is removed first.

:see-also

GET /users
