package com.onebrain.coupon.domain.coupon;

import java.util.Objects;

/**
 * Coupon code must be exactly 6 alphanumeric characters.
 * Special characters are accepted in input but removed before persistence/response.
 */
public record CouponCode(String value) {
  public static final int STANDARD_LENGTH = 6;

  public CouponCode {
    Objects.requireNonNull(value, "code is required");
    if (value.isBlank()) {
      throw new DomainRuleViolation("code is required");
    }
    if (value.length() != STANDARD_LENGTH) {
      throw new DomainRuleViolation("code must have exactly " + STANDARD_LENGTH + " characters");
    }
    if (!value.matches("^[A-Za-z0-9]{" + STANDARD_LENGTH + "}$")) {
      throw new DomainRuleViolation("code must be alphanumeric");
    }
  }

  public static CouponCode fromRaw(String raw) {
    Objects.requireNonNull(raw, "code is required");
    String normalized = raw.replaceAll("[^A-Za-z0-9]", "");
    return new CouponCode(normalized);
  }
}
