package org.shredzone.flattr4j.requests.flattrs;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.requests.base.BaseAuthenticatedFlattrRequest;

/**
 * This request flattrs the given url on behalf of the authenticated user
 * 
 * @author tuxbox
 *
 */
public class FlattrUrlRequest extends BaseAuthenticatedFlattrRequest<FlattrResult> {

  private String url;

  /**
   * Instantiates the request
   * 
   * @param accessToken
   *   the access token
   * @param url
   *   the url to flattr
   */
  public FlattrUrlRequest(String accessToken, String url) {
    super(accessToken, FlattrResult.class);
    this.url = url;
  }

  @Override
  protected FlattrResult handleRequest(FlattrService service) throws FlattrException {
    return service.clickForResult(url);
  }

}
