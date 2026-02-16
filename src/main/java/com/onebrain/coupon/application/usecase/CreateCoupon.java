package com.onebrain.coupon.application.usecase;

import com.onebrain.coupon.application.port.CouponStore;
import com.onebrain.coupon.domain.coupon.Coupon;
import com.onebrain.coupon.domain.coupon.CouponId;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Objects;

/**
 * User intention: create a coupon.
 */
@Component
public class CreateCoupon {
  private final CouponStore store;
  private final Clock clock;

  public CreateCoupon(CouponStore store, Clock clock) {
    this.store = Objects.requireNonNull(store, "store is required");
    this.clock = Objects.requireNonNull(clock, "clock is required");
  }

  public Coupon handle(CreateCouponCommand cmd) {
    Objects.requireNonNull(cmd, "command is required");
    Coupon coupon = Coupon.create(
        CouponId.newId(),
        cmd.code(),
        cmd.description(),
        cmd.discountValue(),
        cmd.expirationDate(),
        cmd.published(),
        clock
    );
    return store.save(coupon);
  }
}
