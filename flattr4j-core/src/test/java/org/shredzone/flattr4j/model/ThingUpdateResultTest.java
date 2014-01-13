package org.shredzone.flattr4j.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.shredzone.flattr4j.model.result.ThingUpdateResult;
import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

/**
 * @author tuxbox
 *
 */
@RunWith(Parameterized.class)
public class ThingUpdateResultTest {

  private String testId;
  private Message message;
  private String description;
  
  /**
   * 
   */
  public ThingUpdateResultTest(String testId, String message, String description) {
    super();
    this.testId = testId;
    this.message = Message.forMessage(message);
    this.description = description;
  }
  
  @Parameters
  public static List<Object[]> paramters() {
    return Arrays.asList( new Object[][]{
        {"1", "ok", "Thing was updated correctly" }
    });
  }
  
  @Test
  public void testModel() throws IOException {
    ThingUpdateResult thingUpdateResult = ModelGenerator.createThingUpdateResult(testId);
    ModelGenerator.assertThingUpdateResult(message, description, thingUpdateResult);
  }

}
