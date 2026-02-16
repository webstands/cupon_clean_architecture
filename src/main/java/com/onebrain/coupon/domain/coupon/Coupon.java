package com.onebrain.coupon.domain.coupon;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class Coupon {

  private final CouponId id;
  private final CouponCode code;
  private final String description;
  private final BigDecimal discountValue;
  private final Instant expirationDate;

  private CouponStatus status;
  private boolean published;
  private boolean redeemed;
  private Instant deletedAt;

  private Coupon(CouponId id, CouponCode code, String description, BigDecimal discountValue, Instant expirationDate, boolean published) {
    this.id = Objects.requireNonNull(id, "id is required");
    this.code = Objects.requireNonNull(code, "code is required");
    this.description = requireText(description, "description is required");
    this.discountValue = requireDiscount(discountValue);
    this.expirationDate = Objects.requireNonNull(expirationDate, "expirationDate is required");
    this.status = CouponStatus.ACTIVE;
    this.published = published;
    this.redeemed = false;
  }

  public static Coupon create(CouponId id, String rawCode, String description, BigDecimal discountValue, Instant expirationDate, boolean published, Clock clock) {
    Objects.requireNonNull(clock, "clock is required");
    if (expirationDate == null) {
      throw new DomainRuleViolation("expirationDate is required");
    }
    Instant now = clock.instant();
    if (expirationDate.isBefore(now)) {
      throw new DomainRuleViolation("expirationDate cannot be in the past");
    }

    CouponCode code = CouponCode.fromRaw(rawCode);
    return new Coupon(id, code, description, discountValue, expirationDate, published);
  }

  /**
   * Reconstruct a domain object from persisted state.
   * (Used by repository adapters; still enforces domain invariants.)
   */
  public static Coupon restore(
      CouponId id,
      String normalizedCode,
      String description,
      BigDecimal discountValue,
      Instant expirationDate,
      CouponStatus status,
      boolean published,
      boolean redeemed,
      Instant deletedAt
  ) {
    Coupon coupon = new Coupon(id, new CouponCode(normalizedCode), description, discountValue, expirationDate, published);
    coupon.status = Objects.requireNonNull(status, "status is required");
    coupon.redeemed = redeemed;
    coupon.deletedAt = deletedAt;
    return coupon;
  }

  public void softDelete(Clock clock) {
    Objects.requireNonNull(clock, "clock is required");
    if (this.status == CouponStatus.DELETED) {
      throw new CouponAlreadyDeleted(this.id.value());
    }
    this.status = CouponStatus.DELETED;
    this.deletedAt = clock.instant();
  }

  public CouponId getId() {
    return id;
  }

  public CouponCode getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public BigDecimal getDiscountValue() {
    return discountValue;
  }

  public Instant getExpirationDate() {
    return expirationDate;
  }

  public CouponStatus getStatus() {
    return status;
  }

  public boolean isPublished() {
    return published;
  }

  public boolean isRedeemed() {
    return redeemed;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  private static String requireText(String value, String message) {
    if (value == null || value.isBlank()) {
      throw new DomainRuleViolation(message);
    }
    return value;
  }

  private static BigDecimal requireDiscount(BigDecimal value) {
    if (value == null) {
      throw new DomainRuleViolation("discountValue is required");
    }
    if (value.compareTo(new BigDecimal("0.5")) < 0) {
      throw new DomainRuleViolation("discountValue must be at least 0.5");
    }
    return value;
  }
}
