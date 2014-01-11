package org.shredzone.flattr4j.requests.subscriptions;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.collection.SubscriptionList;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request list all of the authenticated users subscriptions
 * 
 * @author tuxbox
 *
 */
public class MySubscriptionsRequest extends BaseAuthenticatedFlattrRequest<SubscriptionList> {
  
  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   */
  public MySubscriptionsRequest(String accessToken) {
    super(accessToken, SubscriptionList.class);
  }

  @Override
  protected SubscriptionList handleRequest(FlattrService service) throws FlattrException {
    return new SubscriptionList(service.getMySubscriptions());
  }

}
