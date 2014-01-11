package org.shredzone.flattr4j.requests.activities;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity.Type;
import org.shredzone.flattr4j.model.collection.ActivityList;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * {@link SpiceRequest} implementation which returns my flattr activities
 * @author iulius
 *
 */
public class MyActivitiesRequest extends BaseAuthenticatedFlattrRequest<ActivityList> {

  private Type type;

  /**
   * Instantiates a request which returns my outgoing activities
   * 
   * @param accessToken
   *  the access token
   */
  public MyActivitiesRequest(String accessToken) {
    this(accessToken, Type.OUTGOING);
  }

  /**
   * Instantiates a request which returns my activities of the given type 
   * 
   * @param accessToken
   *  the access token
   * @param type
   *  the type of activities to be returned
   */
  public MyActivitiesRequest(String accessToken, Type type) {
    super(accessToken, ActivityList.class);
    this.type = type;
  }

  @Override
  protected ActivityList handleRequest(FlattrService service) throws FlattrException {
    return new ActivityList(service.getMyActivities(type));
  }

}
