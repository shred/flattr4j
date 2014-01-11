package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request creates a thing on behalf of the authenticated user. The authenticated user will be the resource owner of the thing.
 * 
 * @author tuxbox
 *
 */
public class CreateThingRequest extends BaseAuthenticatedFlattrRequest<ThingId> {

  private Submission submission;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param submission
   *   the submission
   */
  public CreateThingRequest(String accessToken, Submission submission) {
    super(accessToken, ThingId.class);
    this.submission = submission;
  }

  @Override
  protected ThingId handleRequest(FlattrService service) throws FlattrException {
    return service.create(submission);
  }

}
