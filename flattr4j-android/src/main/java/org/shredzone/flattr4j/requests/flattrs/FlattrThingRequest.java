package org.shredzone.flattr4j.requests.flattrs;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request flattrs a given thing on behalf of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class FlattrThingRequest extends BaseAuthenticatedFlattrRequest<FlattrResult> {

  private ThingId thingId;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param thingId
   *   the id of the thing to flattr
   */
  public FlattrThingRequest(String accessToken, ThingId thingId) {
    super(accessToken, FlattrResult.class);
    this.thingId = thingId;
  }

  @Override
  protected FlattrResult handleRequest(FlattrService service) throws FlattrException {
    return service.clickForResult(thingId);
  }
  
  

}
