:introduction

Returns a list of vouchers for the user making the request. This endpoint is
only usable with a `user` access token.

:relevant-types voucher

:relevant-endpoints

POST /voucher_handout
GET /voucher/{voucherCode}
POST /vouchers/generate/{voucherGroupId}
POST /vouchers/group
GET /vouchers/group/{voucherGroupId}
POST /vouchers/group/{voucherGroupId}
GET /vouchers/groups
POST /vouchers/handout/{voucherGroupId}
