package org.shredzone.flattr4j.requests.things;

import java.util.Collection;
import java.util.List;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.model.collection.ThingList;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrPagingRequest;

/**
 * This requests lists the things linked to a user or identified by the given thing id collection
 * 
 * @author tuxbox
 *
 */
public class ThingsRequest extends BaseOpenFlattrPagingRequest<ThingList> {

  private UserId userId;
  private Collection<ThingId> thingIds;

  /**
   * Instantiates the request
   * 
   * @param userId
   *   the user who's things should be listed
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public ThingsRequest(UserId userId, int pageCount, boolean fullMode) {
    super(pageCount, ThingList.class, fullMode);
    this.userId = userId;
  }

  /**
   * Instantiates the request
   * 
   * @param userId
   *   the user who's things should be listed
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public ThingsRequest(UserId userId, int pageCount, int pageSize, boolean fullMode) {
    super(pageCount, ThingList.class, pageSize, fullMode);
    this.userId = userId;
  }

  /**
   * Instantiates the request
   * 
   * @param thingIds
   *   the ids of the things to be returned
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public ThingsRequest(Collection<ThingId> thingIds, int pageCount, boolean fullMode) {
    super(pageCount, ThingList.class, fullMode);
    this.thingIds = thingIds;
  }
  
  /**
   * Instantiates the request
   * 
   * @param thingIds
   *   the ids of the things to be returned
   * @param pageCount
   *   the page count
   * @param pageSize
   *   the maximum number of entries on the result page
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public ThingsRequest(Collection<ThingId> thingIds, int pageCount, int pageSize, boolean fullMode) {
    super(pageCount, ThingList.class, pageSize, fullMode);
    this.thingIds = thingIds;
  }
  
  @Override
  protected ThingList handleRequest(OpenService service) throws FlattrException {
    if( userId == null && thingIds == null ) {
      throw new FlattrException("The used id or the list of things need to be specified!");
    }
    List<Thing> things;
    if( userId == null ) {
      things = service.getThings(userId, getPageSize(), getPageCount());
    } else {
      things = service.getThings(thingIds);
    }
    return new ThingList(things);
  }

}
