package com.onebrain.coupon.adapters.in.web;

import com.onebrain.coupon.application.usecase.CreateCoupon;
import com.onebrain.coupon.application.usecase.CreateCouponCommand;
import com.onebrain.coupon.application.usecase.DeleteCoupon;
import com.onebrain.coupon.application.usecase.GetCoupon;
import com.onebrain.coupon.domain.coupon.Coupon;
import com.onebrain.coupon.domain.coupon.CouponId;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupon")
public class CouponController {

  private final CreateCoupon createCoupon;
  private final GetCoupon getCoupon;
  private final DeleteCoupon deleteCoupon;

  public CouponController(CreateCoupon createCoupon, GetCoupon getCoupon, DeleteCoupon deleteCoupon) {
    this.createCoupon = createCoupon;
    this.getCoupon = getCoupon;
    this.deleteCoupon = deleteCoupon;
  }

  @PostMapping
  public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
    Coupon created = createCoupon.handle(
        new CreateCouponCommand(
            request.code(),
            request.description(),
            request.discountValue(),
            request.expirationDate(),
            request.publishedOrFalse()
        )
    );

    CouponResponse response = toResponse(created);
    return ResponseEntity
        .created(URI.create("/coupon/" + response.id()))
        .body(response);
  }

  @GetMapping("/{id}")
  public CouponResponse getById(@PathVariable("id") UUID id) {
    Coupon coupon = getCoupon.handle(new CouponId(id));
    return toResponse(coupon);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    deleteCoupon.handle(new CouponId(id));
    return ResponseEntity.noContent().build();
  }

  private static CouponResponse toResponse(Coupon coupon) {
    return new CouponResponse(
        coupon.getId().value(),
        coupon.getCode().value(),
        coupon.getDescription(),
        coupon.getDiscountValue(),
        coupon.getExpirationDate(),
        coupon.getStatus(),
        coupon.isPublished(),
        coupon.isRedeemed()
    );
  }
}
