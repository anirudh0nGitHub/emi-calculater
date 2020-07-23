package com.anirudh.de.mvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class TilgungPlanRESTTests {

  @Autowired
  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void tilgungsMonthlyPlanErfolg() throws Exception {
    mockMvc
      .perform(post("/api/plan/monthly")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n"
          + "    \"darlehensbetrag\": 100000,\n"
          + "    \"sollzins\": 2,\n"
          + "    \"tilgungsatz\": 2,\n"
          + "    \"zinsbildung\": 10\n"
          + "}")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.[1].zinsen",
        is(166.67d)));
  }

  @Test
  public void tilgungsMonthlyPlanWrongRequestFormat() throws Exception {
    mockMvc
      .perform(post("/api/plan/monthly")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n"
          + "    \"darlehensbetrag\": \"ertert\",\n"
          + "    \"sollzins\": 2,\n"
          + "    \"tilgungsatz\": 2,\n"
          + "    \"zinsbildung\": 10\n"
          + "}")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$.errorMsg",
        containsString("darlehensbetrag")));
  }

  @Test
  public void tilgungsYearlyPlanErfolg() throws Exception {
    mockMvc
      .perform(post("/api/plan/yearly")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n"
          + "    \"darlehensbetrag\": 100000,\n"
          + "    \"sollzins\": 2,\n"
          + "    \"tilgungsatz\": 2,\n"
          + "    \"zinsbildung\": 10\n"
          + "}")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.monthlyRate",
        is(333.33d)))
      .andExpect(jsonPath("$.restSchuld",
        is(77880.46d)))
      .andExpect(jsonPath("$.yearlyBezahlungDTOS[1].zinsen",
        is(1964.69d)))
      .andExpect(jsonPath("$.yearlyBezahlungDTOS[1].year",
        is("2021")))
      .andExpect(jsonPath("$.yearlyBezahlungDTOS[1].tilgung",
        is(2035.27d)))
      .andExpect(jsonPath("$.yearlyBezahlungDTOS[1].restSchuld",
        is(97128.63d)));

  }

  @Test
  public void tilgungsYearlyPlanWrongRequestFormat() throws Exception {
    mockMvc
      .perform(post("/api/plan/yearly")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n"
          + "    \"darlehensbetrag\": \"ertert\",\n"
          + "    \"sollzins\": 2,\n"
          + "    \"tilgungsatz\": 2,\n"
          + "    \"zinsbildung\": 10\n"
          + "}")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$.errorMsg",
        containsString("darlehensbetrag")));
  }
}
