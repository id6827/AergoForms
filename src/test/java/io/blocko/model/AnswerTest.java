package io.blocko.model;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import io.blocko.AbstractTestCase;

public class AnswerTest extends AbstractTestCase {
  Answer answer;

  @Before
  public void setUp() {
    answer = new Answer(Mockito.anyString(), BigInteger.ONE);
    logger.info("INITIALIZE() - {}", answer);
  }

  @Test
  public void testInc() {
    assertEquals(BigInteger.valueOf(2L), answer.inc());
    logger.info("INC() - {}", answer.getCount());
  }

  @Test
  public void testDec() {
    assertEquals(BigInteger.ZERO, answer.dec());
    logger.info("DEC() - {}", answer.getCount());
  }
}
