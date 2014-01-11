package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.result.ThingResult;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrRequest;

/**
 * This request checks whether the given url or autosubmission is a thing on flattr
 * 
 * @author tuxbox
 *
 */
public class CheckThingRequest extends BaseOpenFlattrRequest<ThingResult> {

  private String url;
  private AutoSubmission autoSubmission;

  /**
   * Instantiate the request
   * 
   * @param url
   *   the url to check
   */
  public CheckThingRequest(String url) {
    super(ThingResult.class);
    this.url = url;
  }

  /**
   * Instantiate the request
   * 
   * @param autoSubmission
   *   the auto submission object to check
   */
  public CheckThingRequest(AutoSubmission autoSubmission) {
    super(ThingResult.class);
    this.autoSubmission = autoSubmission;
  }

  @Override
  protected ThingResult handleRequest(OpenService service) throws FlattrException {
    if( url == null && autoSubmission == null ) {
      throw new FlattrException("Either url or AutoSubmission must be provided");
    }
    ThingResult result;
    if( url != null ) {
      result = service.checkThingExistsByUrl(url);
    } else {
      result = service.checkThingExistsBySubmission(autoSubmission);
    }
    return result;
  }

}
