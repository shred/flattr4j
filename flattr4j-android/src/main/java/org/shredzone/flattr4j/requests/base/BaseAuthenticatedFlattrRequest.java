package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;

/**
 * This base requests allows the querying of protected flattr resources using the provided access token
 * 
 * @author tuxbox
 *
 */
public abstract class BaseAuthenticatedFlattrRequest<RESULT> extends BaseFlattrRequest<RESULT, FlattrService> {

  private String accessToken;

  /**
   * Instantiates the request class
   * 
   * @param accessToken
   *   the access token to be used
   * @param clazz
   *   the class of the result type
   */
  public BaseAuthenticatedFlattrRequest(String accessToken, Class<RESULT> clazz) {
    super(clazz);
    this.accessToken = accessToken;
  }

  @Override
  protected FlattrService createService() {
    FlattrFactory factory = FlattrFactory.getInstance();
    return factory.createFlattrService(accessToken);
  }

}
