package com.onebrain.coupon.adapters.out.persistence;

import com.onebrain.coupon.domain.coupon.CouponStatus;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "coupons")
public class JpaCouponEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, length = 6)
  private String code;

  @Column(nullable = false, length = 4000)
  private String description;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal discountValue;

  @Column(nullable = false)
  private Instant expirationDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private CouponStatus status;

  @Column(nullable = false)
  private boolean published;

  @Column(nullable = false)
  private boolean redeemed;

  @Column
  private Instant deletedAt;

  protected JpaCouponEntity() {
    // for JPA
  }

  public JpaCouponEntity(UUID id, String code, String description, BigDecimal discountValue, Instant expirationDate,
                         CouponStatus status, boolean published, boolean redeemed, Instant deletedAt) {
    this.id = id;
    this.code = code;
    this.description = description;
    this.discountValue = discountValue;
    this.expirationDate = expirationDate;
    this.status = status;
    this.published = published;
    this.redeemed = redeemed;
    this.deletedAt = deletedAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(BigDecimal discountValue) {
    this.discountValue = discountValue;
  }

  public Instant getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Instant expirationDate) {
    this.expirationDate = expirationDate;
  }

  public CouponStatus getStatus() {
    return status;
  }

  public void setStatus(CouponStatus status) {
    this.status = status;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  public boolean isRedeemed() {
    return redeemed;
  }

  public void setRedeemed(boolean redeemed) {
    this.redeemed = redeemed;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }
}
