package com.anirudh.de.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.YearlyBezahlungDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class YearlyTilgungServiceTests {

  private YearlyPlanImp yearlyPlanImp;

  @Before
  public void setUp() {
    MonatlichePlanImp monatlichePlanImp = new MonatlichePlanImp();
    yearlyPlanImp = new YearlyPlanImp(monatlichePlanImp);
  }

  @Test
  public void testTilgungPlanCreationHappyPath() throws TilgungPlanCreationException {
    Optional<List<YearlyBezahlungDTO>> plan = yearlyPlanImp.planErzeugen(createPlanRequest());
    assertThat("Plan not created", plan.isPresent(), is(true));
  }

  @Test(expected = TilgungPlanCreationException.class)
  public void fehlenderDarlehenTest() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setDarlehensbetrag(null);
    yearlyPlanImp.planErzeugen(planRequest);
  }

  @Test(expected = TilgungPlanCreationException.class)
  public void fehlenderTilgungsatsTest() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setTilgungsatz(null);
    yearlyPlanImp.planErzeugen(planRequest);
  }

  @Test
  public void fehlenderZinsbindungDauerTest() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setZinsbildung(null);
    Optional<List<YearlyBezahlungDTO>> plan = yearlyPlanImp.planErzeugen(planRequest);
    assertThat("Plan not created", plan.isPresent(), is(true));
  }

  @Test
  public void testTilgungPlanContent() throws TilgungPlanCreationException {
    Optional<List<YearlyBezahlungDTO>> plan = yearlyPlanImp.planErzeugen(createPlanRequest());
    assertThat("Plan not created", plan.isPresent(), is(true));
    assertThat("Plan not created properly.", plan.get(), is(expectedPlan()));
  }

  private AnfrageDTO createPlanRequest() {
    AnfrageDTO request = new AnfrageDTO();

    request.setDarlehensbetrag(new BigDecimal(100000));
    request.setSollzins(new BigDecimal(2.12));
    request.setTilgungsatz(new BigDecimal(2));
    request.setZinsbildung(1);
    return request;
  }

  private List<YearlyBezahlungDTO> expectedPlan() {
    List<YearlyBezahlungDTO> expected = new ArrayList<>();

    expected.add(new YearlyBezahlungDTO(
      "2020",
      format(new BigDecimal(2059.98)),
      format(new BigDecimal(880.39)),
      format(new BigDecimal(836.26)),
      format(new BigDecimal(99163.74))
    ));
    expected.add(new YearlyBezahlungDTO(
      "JAN - JULY 2021",
      format(new BigDecimal(2403.31)),
      format(new BigDecimal(1220.07)),
      format(new BigDecimal(1183.24)),
      format(new BigDecimal(97980.50))
    ));

    return expected;

  }

  private BigDecimal format(BigDecimal input) {
    return input.setScale(2, BigDecimal.ROUND_HALF_UP);
  }
}
