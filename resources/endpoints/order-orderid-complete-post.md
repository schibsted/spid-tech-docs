:introduction

Completes an order. The order must be in an authorized state and not fully
captured. Any remaining uncaptured order items will be expired, and will not be
a part of the final order.

When the order has been successfully completed, capturing further order items is
not possible. This endpoint finalizes the order and generates a receipt for the
captured items.
