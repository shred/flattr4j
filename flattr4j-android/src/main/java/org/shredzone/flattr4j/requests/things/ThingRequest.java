package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * Request a specific thing 
 * 
 * @author tuxbox
 *
 */
public class ThingRequest extends BaseAuthenticatedFlattrRequest<Thing> {

  private ThingId thingId;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param thingId
   *   the id of the thing to be looked up
   */
  public ThingRequest(String accessToken, ThingId thingId) {
    super(accessToken, Thing.class);
    this.thingId = thingId;
  }
  
  @Override
  protected Thing handleRequest(FlattrService service) throws FlattrException {
    if( thingId == null ) {
      throw new FlattrException("ThingId must be provided");
    }
    return service.getThing(thingId);
  }

}
