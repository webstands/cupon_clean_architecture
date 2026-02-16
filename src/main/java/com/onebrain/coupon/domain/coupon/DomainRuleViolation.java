package com.onebrain.coupon.domain.coupon;


public class DomainRuleViolation extends RuntimeException {
  public DomainRuleViolation(String message) {
    super(message);
  }
}
