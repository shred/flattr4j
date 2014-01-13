package org.shredzone.flattr4j.requests.subscriptions;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request pauses or resumes a currently (in)active subscription on behalf of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class PauseSubscriptionRequest extends BaseAuthenticatedFlattrRequest<FlattrResult> {

  private Message targetState;
  private ThingId thingId;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param thingId
   *   the id of the subscribed thing
   * @param targetState
   *   the target state of the subscription
   */
  public PauseSubscriptionRequest(String accessToken,ThingId thingId, Message targetState) {
    super(accessToken, FlattrResult.class);
    this.thingId = thingId;
    this.targetState = targetState;
  }

  @Override
  protected FlattrResult handleRequest(FlattrService service) throws FlattrException {
    return service.pauseSubscription(thingId, targetState);
  }

}
