package com.onebrain.coupon.infra.exception;

import com.onebrain.coupon.domain.coupon.CouponAlreadyDeleted;
import com.onebrain.coupon.domain.coupon.CouponNotFound;
import com.onebrain.coupon.domain.coupon.DomainRuleViolation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(DomainRuleViolation.class)
  public ResponseEntity<Map<String, Object>> handleDomainRuleViolation(DomainRuleViolation ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
        "error", "BUSINESS_RULE_VIOLATION",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(CouponNotFound.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(CouponNotFound ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
        "error", "COUPON_NOT_FOUND",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(CouponAlreadyDeleted.class)
  public ResponseEntity<Map<String, Object>> handleAlreadyDeleted(CouponAlreadyDeleted ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
        "error", "COUPON_ALREADY_DELETED",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> fields = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fields.put(fe.getField(), fe.getDefaultMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
        "error", "INVALID_REQUEST",
        "message", "Request validation failed",
        "fields", fields
    ));
  }
}
