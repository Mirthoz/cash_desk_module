package com.example.cash_desk;

import com.example.cash_desk.enums.Operations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CashControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnUnauthorizedForInvalidApiKey() throws Exception {
    // Act & Assert
    mockMvc.perform(post("/api/v1/cash-operation")
            .header("FIB-X-AUTH", "invalidApiKey")
            .param("operation", Operations.DEPOSIT.name())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"currency\":\"BGN\",\"whole\":600,\"cents\":0}"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("Invalid API key")));
  }
}