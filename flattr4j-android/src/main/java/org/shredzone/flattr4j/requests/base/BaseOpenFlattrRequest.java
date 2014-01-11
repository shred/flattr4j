package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.OpenService;

/**
 * This request class offers common functionality required to perform requests on unprotected / public flattr resources
 * 
 * @author tuxbox
 *
 */
public abstract class BaseOpenFlattrRequest<RESULT> extends BaseFlattrRequest<RESULT, OpenService> {

  /**
   * Instantiates the request
   * 
   * @param clazz
   *  the class of the result type
   */
  protected BaseOpenFlattrRequest(Class<RESULT> clazz) {
    super(clazz);
  }

  @Override
  protected OpenService createService() {
    FlattrFactory factory = FlattrFactory.getInstance();
    return factory.createOpenService();
  }

}
