package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.result.ThingUpdateResult;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request updates the given thing owned by the authenticated user on behalf of her
 * 
 * @author tuxbox
 *
 */
public class UpdateThingRequest extends BaseAuthenticatedFlattrRequest<ThingUpdateResult> {

  private Thing thing;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param thing
   *   the thing to update
   */
  public UpdateThingRequest(String accessToken, Thing thing) {
    super(accessToken, ThingUpdateResult.class);
    this.thing = thing;
  }

  @Override
  protected ThingUpdateResult handleRequest(FlattrService service) throws FlattrException {
    return service.updateForResult(thing);
  }

}
