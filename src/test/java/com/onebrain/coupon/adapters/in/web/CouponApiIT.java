package com.onebrain.coupon.adapters.in.web;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponApiIT {

  @Autowired
  MockMvc mvc;

  @Test
  void create_shouldReturn201_andNormalizedCode() throws Exception {
    mvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  \"code\": \"ABC-123\",
                  \"description\": \"Promo\",
                  \"discountValue\": 0.8,
                  \"expirationDate\": \"2099-01-01T00:00:00Z\",
                  \"published\": false
                }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.code", is("ABC123")))
        .andExpect(jsonPath("$.status", is("ACTIVE")))
        .andExpect(jsonPath("$.redeemed", is(false)));
  }

  @Test
  void delete_shouldReturn204_andSecondDeleteShouldReturn409() throws Exception {
    String body = mvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  \"code\": \"ABC-123\",
                  \"description\": \"Promo\",
                  \"discountValue\": 0.8,
                  \"expirationDate\": \"2099-01-01T00:00:00Z\",
                  \"published\": true
                }
                """))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    String id = body.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");

    mvc.perform(delete("/coupon/{id}", id))
        .andExpect(status().isNoContent());

    mvc.perform(delete("/coupon/{id}", id))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error", is("COUPON_ALREADY_DELETED")));
  }

  @Test
  void create_shouldReturn400_whenExpirationDateIsPast() throws Exception {
    mvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  \"code\": \"ABC-123\",
                  \"description\": \"Promo\",
                  \"discountValue\": 0.8,
                  \"expirationDate\": \"2000-01-01T00:00:00Z\",
                  \"published\": false
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("BUSINESS_RULE_VIOLATION")));
  }
}
