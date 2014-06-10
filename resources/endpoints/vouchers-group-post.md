:introduction

Create a new [voucher group](/types/voucher-group/). A voucher group is a
template from which vouchers can be generated, it describes what kind of access
its vouchers grant.

Once a voucher group is created, you must
[generate individual vouchers](/endpoints/POST/vouchers/generate/{voucherGroupId}/)
to hand out.

:relevant-types voucher-group

:relevant-endpoints

POST /vouchers/generate/{voucherGroupId}
POST /vouchers/handout/{voucherGroupId}
POST /vouchers/group/{voucherGroupId}
