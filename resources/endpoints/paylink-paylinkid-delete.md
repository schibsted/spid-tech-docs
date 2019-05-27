:introduction

Mark [paylink](/paylink-api/) as deleted. This will effectively disable the
paylink, but it will not be physically deleted from Schibsted account.

:relevant-endpoints

GET /paylink/{paylinkId}
POST /paylink/{paylinkId}/lock
POST /paylink/{paylinkId}/unlock
POST /paylink/{paylinkId}/use
POST /paylink
DELETE /paylink/{paylinkId}

