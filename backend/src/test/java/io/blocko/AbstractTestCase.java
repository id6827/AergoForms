package io.blocko;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringRunner;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public abstract class AbstractTestCase {
  protected final Logger logger = log;
}
