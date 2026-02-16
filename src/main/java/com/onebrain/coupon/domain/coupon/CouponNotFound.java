package com.onebrain.coupon.domain.coupon;

import java.util.UUID;

public class CouponNotFound extends RuntimeException {
  public CouponNotFound(UUID id) {
    super("Coupon not found: " + id);
  }
}
