package org.shredzone.flattr4j.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.shredzone.flattr4j.model.result.ThingResult;
import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

/**
 * Unit test of the {@link ThingResult} class.
 * 
 * @author tuxbox
 *
 */
@RunWith(Parameterized.class)
public class ThingResultTest {

  private String id;
  private Message message;
  private String description;
  private String location;

  public ThingResultTest(String id, String message, String description, String location) {
    super();
    this.id = id;
    this.message = Message.forMessage(message);
    this.description = description;
    this.location = location;
  }
  
  @Parameters
  public static List<Object[]> paramters() {
    return Arrays.asList( new Object[][]{
        {"431547", "ok", "Thing was created successfully", "https://api.flattr.com/rest/v2/things/431547"}
    });
  }
  
  @Test
  public void testModel() throws IOException {
    ThingResult thingResult = ModelGenerator.createThingResult(id);
    ModelGenerator.assertThingResult(id, message, location, description, thingResult);
  }

}
