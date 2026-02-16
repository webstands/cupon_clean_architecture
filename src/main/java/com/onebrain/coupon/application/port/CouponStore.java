package com.onebrain.coupon.application.port;

import com.onebrain.coupon.domain.coupon.Coupon;
import com.onebrain.coupon.domain.coupon.CouponId;

import java.util.Optional;

/**
 * Port (interface) used by use-cases.
 * Implementations live in adapters (JPA, etc.).
 */
public interface CouponStore {
  Coupon save(Coupon coupon);
  Optional<Coupon> findById(CouponId id);
}
