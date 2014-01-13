package org.shredzone.flattr4j.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

/**
 * Unit test of the {@link FlattrResult} class.
 * 
 * @author tuxbox
 *
 */
@RunWith(Parameterized.class)
public class FlattrResultTest {

  private Message message;
  private String description;
  private String thingResource;
  private String thingLink;
  private String thingId;
  private String thingFlattrs;
  private String thingUrl;
  private String thingTitle;
  private String thingImage;

  /**
   * 
   */
  public FlattrResultTest(String message, String description, String thingResource, String thingLink, String thingId, String thingFlattrs, String thingUrl, String thingTitle, String thingImage) {
    super();
    this.message = Message.forMessage(message);
    this.description = description;
    this.thingResource = thingResource;
    this.thingLink = thingLink;
    this.thingId = thingId;
    this.thingFlattrs = thingFlattrs;
    this.thingUrl = thingUrl;
    this.thingTitle = thingTitle;
    this.thingImage = thingImage;
  }
  
  @Parameters
  public static List<Object[]> parameters() {
    return Arrays.asList(new Object[][]{
        { "ok", "Thing was successfully flattred", "https://api.flattr.com/rest/v2/things/423405", "https://flattr.com/thing/423405", "423405", "3", "http://blog.flattr.net/2011/10/api-v2-beta-out-whats-changed/", "API v2 beta out - what's changed?", "https://flattr.com/thing/image/4/2/3/4/0/5/medium.png" }
    });
  }
  
  @Test
  public void testModel() throws IOException {
    FlattrResult flattrResult = ModelGenerator.createFlattrResult(thingId);
    ModelGenerator.assertFlattrResult(message, description, thingResource, thingLink, thingId, thingFlattrs, thingUrl, thingTitle, thingImage, flattrResult);
  }

}
