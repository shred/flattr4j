package org.shredzone.flattr4j.requests.common;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.collection.CategoryList;
import org.shredzone.flattr4j.requests.base.BaseOpenFlattrRequest;

/**
 * Request returns all categories listed on flattr
 * 
 * @author tuxbox
 *
 */
public class CategoriesRequest extends BaseOpenFlattrRequest<CategoryList> {

  /**
   * Instantiates the request
   */
  public CategoriesRequest() {
    super(CategoryList.class);
  }

  @Override
  protected CategoryList handleRequest(OpenService service) throws FlattrException {
    return new CategoryList(service.getCategories());
  }

}
