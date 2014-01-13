package org.shredzone.flattr4j.requests.common;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.collection.LanguageList;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrRequest;

/**
 * The requests returns all languages supported by flattr
 * 
 * @author tuxbox
 *
 */
public class LanguagesRequest extends BaseOpenFlattrRequest<LanguageList> {

  /**
   * Instantiates the request
   */
  public LanguagesRequest() {
    super(LanguageList.class);
  }

  @Override
  protected LanguageList handleRequest(OpenService service) throws FlattrException {
    return new LanguageList(service.getLanguages());
  }

}
