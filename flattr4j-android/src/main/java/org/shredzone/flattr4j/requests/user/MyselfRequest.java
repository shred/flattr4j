package org.shredzone.flattr4j.requests.user;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request returns the user profile of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class MyselfRequest extends BaseAuthenticatedFlattrRequest<User> {

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   */
  public MyselfRequest(String accessToken) {
    super(accessToken, User.class);
  }

  @Override
  protected User handleRequest(FlattrService service) throws FlattrException {
    return service.getMyself();
  }

}
