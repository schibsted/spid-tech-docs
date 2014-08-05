:introduction

Create vouchers for a voucher group. The vouchers themselves are **not**
returned, only an indication of success or failure. Failures indicate that the
voucher group is not available or that you are requesting more vouchers than the
limit on the group allows for. Information about failures can be found in the
error property of the [container](/endpoints/#response-container).

If the voucher group is shared (i.e. its `unique` property is `0`), this
endpoint will respond with a 404, as individual vouchers cannot be generated
from a shared voucher group.

:relevant-endpoints

POST /vouchers/group
POST /vouchers/handout/{voucherGroupId}
