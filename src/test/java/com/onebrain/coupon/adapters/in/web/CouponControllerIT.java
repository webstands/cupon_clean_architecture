package com.onebrain.coupon.adapters.in.web;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void create_shouldReturn201_andNormalizedCode() throws Exception {
    String body = """
        {
          "code": "ABC-123",
          "description": "Promo",
          "discountValue": 0.8,
          "expirationDate": "2099-01-01T00:00:00Z",
          "published": true
        }
        """;

    mockMvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.code").value("ABC123"))
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.published").value(true))
        .andExpect(jsonPath("$.redeemed").value(false));
  }

  @Test
  void delete_shouldBe204_andSecondDeleteShouldBe409() throws Exception {
    String body = """
        {
          "code": "ABC-123",
          "description": "Promo",
          "discountValue": 0.8,
          "expirationDate": "2099-01-01T00:00:00Z",
          "published": false
        }
        """;

    String response = mockMvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    String id = response.replaceAll(".*\"id\"\s*:\s*\"([^\"]+)\".*", "$1");

    mockMvc.perform(delete("/coupon/{id}", id))
        .andExpect(status().isNoContent());

    mockMvc.perform(delete("/coupon/{id}", id))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error").value("COUPON_ALREADY_DELETED"));

    // soft delete means data still exists
    mockMvc.perform(get("/coupon/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("DELETED"));
  }
}
