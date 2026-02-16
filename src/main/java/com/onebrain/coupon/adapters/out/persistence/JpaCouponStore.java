package com.onebrain.coupon.adapters.out.persistence;

import com.onebrain.coupon.application.port.CouponStore;
import com.onebrain.coupon.domain.coupon.Coupon;
import com.onebrain.coupon.domain.coupon.CouponId;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaCouponStore implements CouponStore {

  private final SpringDataCouponRepository repo;

  public JpaCouponStore(SpringDataCouponRepository repo) {
    this.repo = repo;
  }

  @Override
  public Coupon save(Coupon coupon) {
    JpaCouponEntity entity = toEntity(coupon);
    JpaCouponEntity saved = repo.save(entity);
    return toDomain(saved);
  }

  @Override
  public Optional<Coupon> findById(CouponId id) {
    return repo.findById(id.value()).map(this::toDomain);
  }

  private JpaCouponEntity toEntity(Coupon coupon) {
    return new JpaCouponEntity(
        coupon.getId().value(),
        coupon.getCode().value(),
        coupon.getDescription(),
        coupon.getDiscountValue(),
        coupon.getExpirationDate(),
        coupon.getStatus(),
        coupon.isPublished(),
        coupon.isRedeemed(),
        coupon.getDeletedAt()
    );
  }

  private Coupon toDomain(JpaCouponEntity entity) {
    return Coupon.restore(
        new CouponId(entity.getId()),
        entity.getCode(),
        entity.getDescription(),
        entity.getDiscountValue(),
        entity.getExpirationDate(),
        entity.getStatus(),
        entity.isPublished(),
        entity.isRedeemed(),
        entity.getDeletedAt()
    );
  }
}
