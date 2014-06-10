:introduction

Get
[generated voucher codes](/endpoints/POST/vouchers/generate/{voucherGroupId}/).
The codes that are returned in this endpoint gets their status changed to
"handed out". They will not be returned again by repeated calls to this
endpoint. Handed out vouchers are in limbo until used. The person or system that
hands out vouchers must be responsible for remembering them or making sure they
are used. See the [separate endpoint](/endpoints/POST/voucher_handout/) for
automatically giving a voucher to a user.

:relevant-types voucher

:relevant-endpoints

POST /vouchers/generate/{voucherGroupId}
POST /voucher_handout
