package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;

/**
 * This base request allows the querying of protected, paging flattr resources using the provided access token
 * 
 * @author tuxbox
 *
 */
public abstract class BaseAuthenticatedFlattrPagingRequest<RESULT> extends BaseFlattrPagingRequest<RESULT, FlattrService> {

  private String accessToken;

  /**
   * Instantiates the authenticated paging request
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param clazz
   *   the class of the result type 
   * @param fullMode
   *   whether full mode should be used, e.g. for user objects
   */
  protected BaseAuthenticatedFlattrPagingRequest(String accessToken, int pageCount, Class<RESULT> clazz, boolean fullMode) {
    super(pageCount, clazz, fullMode);
    this.accessToken = accessToken;
  }
  
  /**
   * Instantiates the authenticated paging request
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param clazz
   *   the class of the result type 
   * @param pageSize
   *   the max. number of entries on a page
   */
  protected BaseAuthenticatedFlattrPagingRequest(String accessToken, int pageCount, Class<RESULT> clazz, int pageSize) {
    this(accessToken, pageCount, clazz, pageSize, false);
  }
  
  /**
   * Instantiates the authenticated paging request
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param clazz
   *   the class of the result type
   * @param pageSize
   *   the number of entries on a page 
   * @param fullMode
   *   whether full mode should be used, e.g. for user objects
   */
  protected BaseAuthenticatedFlattrPagingRequest(String accessToken, int pageCount, Class<RESULT> clazz, int pageSize, boolean fullMode) {
    super(pageCount, clazz, pageSize, fullMode);
    this.accessToken = accessToken;
  }

  @Override
  protected FlattrService createService() {
    FlattrFactory factory = FlattrFactory.getInstance();
    return factory.createFlattrService(accessToken);
  }

}
