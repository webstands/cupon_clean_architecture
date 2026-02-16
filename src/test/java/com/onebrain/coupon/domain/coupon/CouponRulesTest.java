package com.onebrain.coupon.domain.coupon;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;

class CouponRulesTest {

  private final Clock fixedClock = Clock.fixed(Instant.parse("2026-02-13T00:00:00Z"), ZoneOffset.UTC);

  @Test
  void create_shouldRemoveSpecialCharactersAndKeepSixChars() {
    Coupon coupon = Coupon.create(
        CouponId.newId(),
        "ABC-123",
        "Summer promo",
        new BigDecimal("0.8"),
        Instant.parse("2026-12-31T23:59:59Z"),
        false,
        fixedClock
    );

    assertThat(coupon.getCode().value()).isEqualTo("ABC123");
  }

  @Test
  void create_shouldFail_whenNormalizedCodeIsNotExactlySixChars() {
    assertThatThrownBy(() -> Coupon.create(
        CouponId.newId(),
        "A-1",
        "Desc",
        new BigDecimal("0.8"),
        Instant.parse("2026-12-31T23:59:59Z"),
        false,
        fixedClock
    )).isInstanceOf(DomainRuleViolation.class)
      .hasMessageContaining("exactly 6");
  }

  @Test
  void create_shouldFail_whenDiscountValueLessThanMinimum() {
    assertThatThrownBy(() -> Coupon.create(
        CouponId.newId(),
        "ABC-123",
        "Desc",
        new BigDecimal("0.49"),
        Instant.parse("2026-12-31T23:59:59Z"),
        false,
        fixedClock
    )).isInstanceOf(DomainRuleViolation.class)
      .hasMessageContaining("at least 0.5");
  }

  @Test
  void create_shouldFail_whenExpirationDateIsInPast() {
    assertThatThrownBy(() -> Coupon.create(
        CouponId.newId(),
        "ABC-123",
        "Desc",
        new BigDecimal("0.5"),
        Instant.parse("2025-01-01T00:00:00Z"),
        false,
        fixedClock
    )).isInstanceOf(DomainRuleViolation.class)
      .hasMessageContaining("past");
  }

  @Test
  void delete_shouldBeSoft_andNotAllowSecondDelete() {
    Coupon coupon = Coupon.create(
        CouponId.newId(),
        "ABC-123",
        "Desc",
        new BigDecimal("0.5"),
        Instant.parse("2026-12-31T23:59:59Z"),
        false,
        fixedClock
    );

    coupon.softDelete(fixedClock);

    assertThat(coupon.getStatus()).isEqualTo(CouponStatus.DELETED);
    assertThat(coupon.getDeletedAt()).isNotNull();

    assertThatThrownBy(() -> coupon.softDelete(fixedClock))
        .isInstanceOf(CouponAlreadyDeleted.class);
  }
}
