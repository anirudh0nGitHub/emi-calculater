package com.anirudh.de.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.MonatlicheBezahlungDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class MonthlyTilgungServiceTests {

  private MonatlichePlanImp monatlicheBerechnungImp;

  @Before
  public void setUp() {
    monatlicheBerechnungImp = new MonatlichePlanImp();
  }

  @Test
  public void testTilgungPlanCreationHappyPath() throws TilgungPlanCreationException {
    Optional<List<MonatlicheBezahlungDTO>> plan = monatlicheBerechnungImp.planErzeugen(createPlanRequest());
    assertThat("Plan not created", plan.isPresent(), is(true));
  }

  @Test(expected = TilgungPlanCreationException.class)
  public void fehlenderDarlehenTest() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setDarlehensbetrag(null);
    monatlicheBerechnungImp.planErzeugen(planRequest);
  }

  @Test(expected = TilgungPlanCreationException.class)
  public void fehlenderTilgungsatsTest() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setTilgungsatz(null);
    monatlicheBerechnungImp.planErzeugen(planRequest);
  }

  @Test
  public void fehlenderZinsbindungDauerTest() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setZinsbildung(null);
    Optional<List<MonatlicheBezahlungDTO>> plan = monatlicheBerechnungImp.planErzeugen(planRequest);
    assertThat("Plan not created", plan.isPresent(), is(true));
  }

  @Test
  public void testTilgungPlanContent() throws TilgungPlanCreationException {
    Optional<List<MonatlicheBezahlungDTO>> plan = monatlicheBerechnungImp.planErzeugen(createPlanRequest());
    assertThat("Plan not created", plan.isPresent(), is(true));
    assertThat("Plan not created properly.", plan.get(), is(expectedPlan()));
  }

  @Test
  public void testVolltilgung() throws TilgungPlanCreationException {
    AnfrageDTO planRequest = createPlanRequest();
    planRequest.setDarlehensbetrag(new BigDecimal(10000));
    planRequest.setTilgungsatz(new BigDecimal(10));
    planRequest.setZinsbildung(10);

    Optional<List<MonatlicheBezahlungDTO>> planOpt = monatlicheBerechnungImp.planErzeugen(planRequest);

    assertThat("Plan not created", planOpt.isPresent(), is(true));

    List<MonatlicheBezahlungDTO> plan = planOpt.get();
    assertThat("Plan not created properly.", plan.get(plan.size() - 1).getRestSchuld(), is(format(new BigDecimal(0))));
  }

  private AnfrageDTO createPlanRequest() {
    AnfrageDTO request = new AnfrageDTO();

    request.setDarlehensbetrag(new BigDecimal(100000));
    request.setSollzins(new BigDecimal(2.12));
    request.setTilgungsatz(new BigDecimal(2));
    request.setZinsbildung(1);
    return request;
  }

  private List<MonatlicheBezahlungDTO> expectedPlan() {
    List<MonatlicheBezahlungDTO> expected = new ArrayList<>();

    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2020, 7, 31),
      new BigDecimal(100000).setScale(0, BigDecimal.ROUND_HALF_UP),
      new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP),
      new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2020, 8, 31),
      format(new BigDecimal(99833.34)),
      format(new BigDecimal(176.67)),
      format(new BigDecimal(166.66)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2020, 9, 30),
      format(new BigDecimal(99666.38)),
      format(new BigDecimal(176.37)),
      format(new BigDecimal(166.96)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2020, 10, 31),
      format(new BigDecimal(99499.13)),
      format(new BigDecimal(176.08)),
      format(new BigDecimal(167.25)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2020, 11, 30),
      format(new BigDecimal(99331.58)),
      format(new BigDecimal(175.78)),
      format(new BigDecimal(167.55)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2020, 12, 31),
      format(new BigDecimal(99163.74)),
      format(new BigDecimal(175.49)),
      format(new BigDecimal(167.84)),
      format(new BigDecimal(343.33))
    ));
    //nÄCHSTES jAHR
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 1, 31),
      format(new BigDecimal(98995.60)),
      format(new BigDecimal(175.19)),
      format(new BigDecimal(168.14)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 2, 28),
      format(new BigDecimal(98827.16)),
      format(new BigDecimal(174.89)),
      format(new BigDecimal(168.44)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 3, 31),
      format(new BigDecimal(98658.42)),
      format(new BigDecimal(174.59)),
      format(new BigDecimal(168.74)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 4, 30),
      format(new BigDecimal(98489.39)),
      format(new BigDecimal(174.30)),
      format(new BigDecimal(169.03)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 5, 31),
      format(new BigDecimal(98320.06)),
      format(new BigDecimal(174.00)),
      format(new BigDecimal(169.33)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 6, 30),
      format(new BigDecimal(98150.43)),
      format(new BigDecimal(173.70)),
      format(new BigDecimal(169.63)),
      format(new BigDecimal(343.33))
    ));
    expected.add(new MonatlicheBezahlungDTO(
      LocalDate.of(2021, 7, 31),
      format(new BigDecimal(97980.50)),
      format(new BigDecimal(173.40)),
      format(new BigDecimal(169.93)),
      format(new BigDecimal(343.33))
    ));

    return expected;

  }

  private BigDecimal format(BigDecimal input) {
    return input.setScale(2, BigDecimal.ROUND_HALF_UP);
  }
}
