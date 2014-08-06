:introduction

Hands out a voucher to user. Assumes that vouchers for the voucher group has
been generated upfront, and that there are available vouchers that are neither
redeemed, handed out, nor expired. The voucher that is given to the user has its
status changed to "handed out".

Failure means a voucher was not available. More info on failure may be found in
the error value of the [container](/endpoints/#response-container).

:relevant-types voucher voucher-group

:relevant-endpoints

GET /me/vouchers
GET /voucher/{voucherCode}
POST /vouchers/generate/{voucherGroupId}
POST /vouchers/group
GET /vouchers/group/{voucherGroupId}
POST /vouchers/group/{voucherGroupId}
GET /vouchers/groups
POST /vouchers/handout/{voucherGroupId}
