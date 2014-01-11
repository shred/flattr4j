package org.shredzone.flattr4j.requests.flattrs;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request flattrs the provided autoSubmission on behalf of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class FlattrAutoSubmissionRequest extends BaseAuthenticatedFlattrRequest<FlattrResult> {

  private AutoSubmission autoSubmission;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param autoSubmission
   *   the auto submission to flattr
   */
  public FlattrAutoSubmissionRequest(String accessToken, AutoSubmission autoSubmission) {
    super(accessToken, FlattrResult.class);
    this.autoSubmission = autoSubmission;
  }

  @Override
  protected FlattrResult handleRequest(FlattrService service) throws FlattrException {
    return service.clickForResult(autoSubmission);
  }

}
