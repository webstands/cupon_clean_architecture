package com.onebrain.coupon.adapters.in.web;

import com.onebrain.coupon.domain.coupon.CouponStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    Instant expirationDate,
    CouponStatus status,
    boolean published,
    boolean redeemed
) {
}
