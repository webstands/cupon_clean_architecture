package com.onebrain.coupon.domain.coupon;

import java.util.UUID;

public class CouponAlreadyDeleted extends RuntimeException {
  public CouponAlreadyDeleted(UUID id) {
    super("Coupon is already deleted: " + id);
  }
}
