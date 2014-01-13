package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This requests deletes a given thing of the authenticated user on behalf of her from flattr
 * 
 * @author tuxbox
 *
 */
public class DeleteThingRequest extends BaseAuthenticatedFlattrRequest<Void> {

  private ThingId thingId;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param thingId
   *   the id of the thing to delete
   */
  public DeleteThingRequest(String accessToken, ThingId thingId) {
    super(accessToken, Void.class);
    this.thingId = thingId;
  }

  @Override
  protected Void handleRequest(FlattrService service) throws FlattrException {
    service.delete(thingId);
    return null;
  }

}
