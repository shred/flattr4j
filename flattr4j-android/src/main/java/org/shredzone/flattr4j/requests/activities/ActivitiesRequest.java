package org.shredzone.flattr4j.requests.activities;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity.Type;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.model.collection.ActivityList;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrRequest;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * {@link SpiceRequest} implementation that requests the activities of a flattr user
 * 
 * @author tuxbox
 *
 */
public class ActivitiesRequest extends BaseOpenFlattrRequest<ActivityList> {

  private UserId userId;
  private Type type;

  /**
   * Instantiates an ActivitiesRequest that returns all outgoing activities of a user
   * 
   * @param userId
   *  the usedId of which the activities should be looked up
   */
  public ActivitiesRequest(UserId userId) {
    this(userId, Type.OUTGOING);
  }
  
  /**
   * Instantiates the ActivitiesRequest for a given user and specified activity type 
   * 
   * @param userId
   *  the usedId of which the activities should be looked up
   * @param type
   *  the type of activities to be returned
   */
  public ActivitiesRequest(UserId userId, Type type) {
    super(ActivityList.class);
    this.userId = userId;
    this.type = type;
  }

  @Override
  protected ActivityList handleRequest(OpenService service) throws FlattrException {
    return new ActivityList(service.getActivities(userId, type));
  }

}
