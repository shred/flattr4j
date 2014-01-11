package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.collection.ThingList;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrPagingRequest;

/**
 * This request lists the things owned by the authenticated user on flattr
 * 
 * @author tuxbox
 *
 */
public class MyThingsRequest extends BaseAuthenticatedFlattrPagingRequest<ThingList> {

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public MyThingsRequest(String accessToken, int pageCount, boolean fullMode) {
    super(accessToken, pageCount, ThingList.class, fullMode);
  }

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on a result page
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public MyThingsRequest(String accessToken, int pageCount, int pageSize, boolean fullMode) {
    super(accessToken, pageCount, ThingList.class, pageSize, fullMode);
  }

  @Override
  protected ThingList handleRequest(FlattrService service) throws FlattrException {
    return new ThingList(service.getMyThings());
  }
  
}
