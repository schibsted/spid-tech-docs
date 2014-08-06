:introduction

Create a new [voucher group](/types/voucher-group/). A voucher group is a
template from which vouchers can be generated, it describes what kind of access
its vouchers grant.

Once a voucher group is created, you must
[generate individual vouchers](/endpoints/POST/vouchers/generate/{voucherGroupId}/)
to hand out.

In order to use this endpoint, your client must have a `voucher_prefix`. This
field is set by SPiD administrators, if it is not set, you will get an error
like "Set generator used, but no client voucher prefix set". If this happens,
contact support.

:relevant-types voucher-group

:relevant-endpoints

GET /me/vouchers
POST /voucher_handout
GET /voucher/{voucherCode}
POST /vouchers/generate/{voucherGroupId}
GET /vouchers/group/{voucherGroupId}
POST /vouchers/group/{voucherGroupId}
GET /vouchers/groups
POST /vouchers/handout/{voucherGroupId}
