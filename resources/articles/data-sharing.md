:title Data sharing options between API clients
:frontpage
:category how-tos
:aside
## Table of Contents

<spid-toc></spid-toc>

## See also

- [SPiD Authentication](/authentication/)
- [The Filters parameter](/endpoints/#filters)

:body

SPiD supports data sharing between API clients in SPiD in two ways:

* sharing data by applying the merchant filter when retrieving data
* sharing data by allowing a client to perform API calls on behalf of another client

Below is a detailed description of these two approaches and a description of use cases when these may apply.

## Applying the merchant filter
Many API endpoints have support for the [filters parameter](/endpoints/#filters). By applying the **merchant** filter
to these endpoints SPiD will return data in a merchant context instead of a client context. This means that we will
return all data that belongs the to the merchant the calling client belongs to, and not only data belonging to the
calling client.

An example where this functionality may be useful is when you have two different API clients, one mobile application
and one webservice. Users may signup or login on both clients. In order to retrieve all users and logins for both
clients you may use the merchant filter on the [/users](/endpoints/GET/users/) and [/logins](/endpoints/GET/logins/)
endpoint.

Here are some of the APIs with merchant filter support:

* [GET /users](/endpoints/GET/users/)
* [GET /user/{userId}/logins](/endpoints/GET/user/{userId}/logins/)
* [GET /logins](/endpoints/GET/logins/)
* [GET /orders](/endpoints/GET/orders/)
* [GET /user/{userId}/product/{productId}](/endpoints/GET/user/{userId}/product/{productId}/)
* [GET /dataobjects](/endpoints/GET/dataobjects/)

## Performing API calls on behalf of another client
Some endpoints don't support merchant level access and some use cases require that a client may perform some API
calls on behalf of another client within the same merchant. The SPiD platform supports this by enabling data sharing
between clients. This has to be configured by SPiD. See detailed information on how to enable it below.

### How to perform API calls on behalf of another client
The client performing the API calls need to authenticate itself as usual. The only difference is that this client
needs to pass the actual client_id of the client they are retrieving data from, or performing API calls on behalf of,
with the parameter **contextClientId** in all API calls.

SPiD will verify that both clients has access to call the endpoint and also validate that the token used is valid for
the endpoint in question. The contextClientId needs to have access to the resources reflected by the endpoint as well,
meaning that you can't access users, orders or other data that is NOT accessible by the client used as context.

Getting data is simple, but POSTING data can be a little troublesome in the current version of the platform since you
actually have to pass the contextClientId variable as a GET parameter. Meaning you have to append the parameter to the
endpoint URL like:
```
HTTP POST /user/100001/product/12345?contextClientId=4cf36ius78dywe8h0000
```

![Performing API calls on behalf of another client](/images/data-sharing-between-clients.png)


### In order to create this setup for your clients you need to:
(Remember that this needs to be set up in our stage/pre environment first!)

1. Request a backend API client via Selfservice - make sure to choose which endpoint groups you want this client to
be able to access.
2. Send an e-mail to support@spid.no stating:
Which end points your main client (client id needed) will enable for data sharing and the client name and client_id
of the backend client that has been created and needs access.
Which endpoints the backend client will need access to.
3. Implement the functions, and when you are ready with it contact support@spid.no to access the implementation checklist.
4. If everything is fine - request the backend client in the production environment.
