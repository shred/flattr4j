package org.shredzone.flattr4j.requests.subscriptions;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request toggles the status of the subscription on behalf of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class ToggleSubscriptionRequest extends BaseAuthenticatedFlattrRequest<FlattrResult> {

  private ThingId thingId;

  /**
   * Instantiates the object
   * 
   * @param accessToken
   *   the access token 
   * @param thingId
   *   the id of the thing to toggle the subscription status
   */
  public ToggleSubscriptionRequest(String accessToken, ThingId thingId) {
    super(accessToken, FlattrResult.class);
    this.thingId = thingId;
  }

  @Override
  protected FlattrResult handleRequest(FlattrService service) throws FlattrException {
    return service.toggleSubscriptionForResult(thingId);
  }

}
