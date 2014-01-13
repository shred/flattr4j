package org.shredzone.flattr4j.requests.subscriptions;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request deletes a current subscription of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class DeleteSubscriptionRequest extends BaseAuthenticatedFlattrRequest<Void> {

  private ThingId thingId;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param thingId
   *   the id of the thing to unsubscribe of
   */
  public DeleteSubscriptionRequest(String accessToken, ThingId thingId) {
    super(accessToken, Void.class);
    this.thingId = thingId;
  }

  @Override
  protected Void handleRequest(FlattrService service) throws FlattrException {
    service.unsubscribe(thingId);
    return null;
  }

}
