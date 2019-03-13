:title Assets
:frontpage
:category api-integration
:body

Will be available in version `2.71.1`

In order to support a more flexible solution for identity based access management, not tied to complex product structures, Assets are new solution that will enable clients to specify what the user has access to themselves, without any explicit requirements from SchAcc.

Via `POST /user/{id}/asset/{assetId}` endpoint client is able to create (or modify) access to asset for user. 

- {id} can be user_id or UUID
- {assetId} must be non-numeric identifier see more [assetId type](/types/asset-id/)


## Use cases

Use Cases for handling access checks for NON-SchAcc assets are many:

- check if you have access to a specific purchased e-book
- check if you have purchased a Finn-ad addon
- check if you have paid for an account upgrade 
- check if you have access to a restricted feature (like p2p enabling or disabling users on the client)
- check if you have access to some product not currently defined in the SchAcc product structure because the product structure doesn’t match the client’s product structure requirements
- check if you have access to a specific news article

:relevant-endpoints

GET /user/{id}/asset/{assetId}
POST /user/{id}/asset/{assetId}
DELETE /user/{id}/asset/{assetId}
GET /user/{id}/assets/{assetIds}
GET /user/{id}/assets
GET /asset/{id}/users/count
