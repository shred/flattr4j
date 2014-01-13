package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.OpenService;

/**
 * Performs a paging request to unprotected public flattr resources
 * 
 * @author tuxbox
 *
 */
public abstract class BaseOpenFlattrPagingRequest<RESULT> extends BaseFlattrPagingRequest<RESULT, OpenService> {

  /**
   * Instantiates the request
   * 
   * @param pageCount
   *   the page count
   * @param clazz
   *   the class of the result type
   * @param fullMode
   *   whether full mode should be applied
   */
  protected BaseOpenFlattrPagingRequest(int pageCount, Class<RESULT> clazz, boolean fullMode) {
    super(pageCount, clazz, fullMode);
  }
  
  /**
   * Instantiates the request
   * 
   * @param pageCount
   *   the page count
   * @param clazz
   *   the class of the result type
   * @param pageSize
   *   the maximum number of entries on a result page
   * @param fullMode
   *   whether full mode should be applied
   */
  protected BaseOpenFlattrPagingRequest(int pageCount, Class<RESULT> clazz, int pageSize, boolean fullMode) {
    super(pageCount, clazz, pageSize, fullMode);
  }

  @Override
  protected OpenService createService() {
    FlattrFactory factory = FlattrFactory.getInstance();
    return factory.createOpenService();
  }

}
