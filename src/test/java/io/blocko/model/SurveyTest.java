package io.blocko.model;

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import io.blocko.AbstractTestCase;

public class SurveyTest extends AbstractTestCase {
  private Survey survey;

  @Before
  public void setUp() {
    final String question = "Are you a developer?";
    final LocalDateTime startTime = LocalDateTime.now();
    final LocalDateTime endTime = LocalDateTime.now();
    final Answer answer = new Answer("YES", BigInteger.TEN);
    final Answer otherAnswer = new Answer("NO", BigInteger.ONE);
    survey = new Survey(question, startTime, endTime, answer, otherAnswer);
    logger.info("INITIALIZE() - {}", survey);
  }

  @Test
  public void testMax() {
    assertEquals(BigInteger.TEN, survey.max().getCount());
  }

  @Test
  public void testMin() {
    assertEquals(BigInteger.ONE, survey.min().getCount());
  }

  @Test
  public void testAggregate() {
    assertEquals(BigInteger.valueOf(11L), survey.aggregate().getCount());
  }

}
