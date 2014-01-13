package org.shredzone.flattr4j.requests.flattrs;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.collection.FlattrList;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrPagingRequest;

/**
 * This request returns the list of flattrs of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class MyFlattrsRequest extends BaseAuthenticatedFlattrPagingRequest<FlattrList> {
  
  /**
   * Instantiates the object
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   */
  public MyFlattrsRequest(String accessToken, int pageCount){
    this(accessToken, pageCount, false);
  }
  
  /**
   * Instantiates the object
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public MyFlattrsRequest(String accessToken, int pageCount, boolean fullMode){
    super(accessToken, pageCount, FlattrList.class, fullMode);
  }
  
  /**
   * Instantiates the object
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   */
  public MyFlattrsRequest(String accessToken, int pageCount, int pageSize) {
    this(accessToken, pageCount, pageSize, false);
  }

  /**
   * Instantiates the object
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public MyFlattrsRequest(String accessToken, int pageCount, int pageSize, boolean fullMode) {
    super(accessToken, pageCount, FlattrList.class, pageSize, fullMode);
  }

  @Override
  protected FlattrList handleRequest(FlattrService service) throws FlattrException {
    return new FlattrList(service.getMyFlattrs(getPageSize(), getPageCount()));
  }

}
