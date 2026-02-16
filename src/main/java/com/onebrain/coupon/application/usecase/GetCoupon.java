package com.onebrain.coupon.application.usecase;

import com.onebrain.coupon.application.port.CouponStore;
import com.onebrain.coupon.domain.coupon.Coupon;
import com.onebrain.coupon.domain.coupon.CouponId;
import com.onebrain.coupon.domain.coupon.CouponNotFound;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * User intention: read a coupon by id.
 */
@Component
public class GetCoupon {
  private final CouponStore store;

  public GetCoupon(CouponStore store) {
    this.store = Objects.requireNonNull(store, "store is required");
  }

  public Coupon handle(CouponId id) {
    Objects.requireNonNull(id, "id is required");
    return store.findById(id).orElseThrow(() -> new CouponNotFound(id.value()));
  }
}
