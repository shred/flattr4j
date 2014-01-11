package org.shredzone.flattr4j.requests.user;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrRequest;

/**
 * This request returns the (public) user profile of the given user id 
 * 
 * @author tuxbox
 *
 */
public class UserRequest extends BaseOpenFlattrRequest<User> {

  private UserId userId;

  /**
   * Instantiates the request
   * 
   * @param userId
   *   the id of the user to request
   */
  public UserRequest(UserId userId) {
    super(User.class);
    this.userId = userId;
  }

  @Override
  protected User handleRequest(OpenService service) throws FlattrException {
    return service.getUser(userId);
  }

}
