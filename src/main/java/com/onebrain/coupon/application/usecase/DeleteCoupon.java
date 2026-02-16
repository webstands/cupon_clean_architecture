package com.onebrain.coupon.application.usecase;

import com.onebrain.coupon.application.port.CouponStore;
import com.onebrain.coupon.domain.coupon.Coupon;
import com.onebrain.coupon.domain.coupon.CouponId;
import com.onebrain.coupon.domain.coupon.CouponNotFound;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Objects;

/**
 * User intention: delete (soft delete) a coupon.
 */
@Component
public class DeleteCoupon {
  private final CouponStore store;
  private final Clock clock;

  public DeleteCoupon(CouponStore store, Clock clock) {
    this.store = Objects.requireNonNull(store, "store is required");
    this.clock = Objects.requireNonNull(clock, "clock is required");
  }

  public void handle(CouponId id) {
    Objects.requireNonNull(id, "id is required");
    Coupon coupon = store.findById(id).orElseThrow(() -> new CouponNotFound(id.value()));
    coupon.softDelete(clock);
    store.save(coupon);
  }
}
