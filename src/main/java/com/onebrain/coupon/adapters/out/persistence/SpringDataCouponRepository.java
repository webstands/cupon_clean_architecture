package com.onebrain.coupon.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataCouponRepository extends JpaRepository<JpaCouponEntity, UUID> {
}
