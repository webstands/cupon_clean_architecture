package com.onebrain.coupon.domain.coupon;

import java.util.Objects;
import java.util.UUID;

public record CouponId(UUID value) {
  public CouponId {
    Objects.requireNonNull(value, "id is required");
  }

  public static CouponId newId() {
    return new CouponId(UUID.randomUUID());
  }

  public static CouponId fromString(String raw) {
    return new CouponId(UUID.fromString(raw));
  }
}
