package org.shredzone.flattr4j.requests.flattrs;

import java.util.List;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.model.collection.FlattrList;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrPagingRequest;

/**
 * This requests returns a list of flattrs associated to a user or thing
 * 
 * @author tuxbox
 *
 */
public class FlattrsRequest extends BaseOpenFlattrPagingRequest<FlattrList> {

  private ThingId thing;
  private UserId user;

  /**
   * Instantiates the request
   * 
   * @param thing
   *   the thing of which the flattr should be returned
   * @param pageCount
   *   the page count
   */
  public FlattrsRequest(ThingId thing, int pageCount){
    this(thing, pageCount, false);
  }

  /**
   * Instantiates the request
   * 
   * @param thing
   *   the thing of which the flattr should be returned
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public FlattrsRequest(ThingId thing, int pageCount, boolean fullMode) {
    super(pageCount, FlattrList.class, fullMode);
    this.thing = thing;
  }

  /**
   * Instantiates the request
   * 
   * @param user
   *   the user of who the flattr should be returned
   * @param pageCount
   *   the page count
   */
  public FlattrsRequest(UserId user, int pageCount) {
    this(user, pageCount, false);
  }
  
  /**
   * Instantiates the request
   * 
   * @param user
   *   the user of who the flattr should be returned
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public FlattrsRequest(UserId user, int pageCount, boolean fullMode) {
    super(pageCount, FlattrList.class, fullMode);
    this.user = user;
  }

  /**
   * Instantiates the request
   * 
   * @param thingId
   *   the thing id of which the flattr should be returned
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   */
  public FlattrsRequest(ThingId thing, int pageCount, int pageSize){
    this(thing, pageCount, pageSize, false);
  }
  
  /**
   * Instantiates the request
   * 
   * @param thingId
   *   the thing id of which the flattr should be returned
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public FlattrsRequest(ThingId thing, int pageCount, int pageSize, boolean fullMode){
    super(pageCount, FlattrList.class, pageSize, fullMode);
    this.thing = thing;
  }
  
  /**
   * Instantiates the request
   * 
   * @param user
   *   the user of who the flattr should be returned
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   */
  public FlattrsRequest(UserId user, int pageCount, int pageSize) {
    this(user, pageCount, pageSize, false);
  }
  
  /**
   * Instantiates the request
   * 
   * @param user
   *   the user of who the flattr should be returned
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public FlattrsRequest(UserId user, int pageCount, int pageSize, boolean fullMode){
    super(pageCount, FlattrList.class, pageSize, fullMode);
    this.user = user;
  }

  @Override
  protected FlattrList handleRequest(OpenService service) throws FlattrException {
    List<Flattr> flattrs;
    if( user == null && thing == null ) {
      throw new FlattrException("Either a user or a thing needs to be provided to query a flattr list!");
    }
    if( user != null ) {
      flattrs = service.getFlattrs((UserId)user, getPageSize(), getPageCount());
    } else {
      flattrs = service.getFlattrs((ThingId)thing, getPageSize(), getPageCount());
    }
    return new FlattrList(flattrs);
  }
  
}
