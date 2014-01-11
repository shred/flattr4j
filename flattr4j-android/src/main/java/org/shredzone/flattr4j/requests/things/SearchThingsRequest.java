package org.shredzone.flattr4j.requests.things;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.SearchQuery;
import org.shredzone.flattr4j.model.SearchResult;
import org.shredzone.flattr4j.requests.base.BaseOpenSearchRequest;

/**
 * This request searchs for things matching the given search query
 * 
 * @author tuxbox
 *
 */
public class SearchThingsRequest extends BaseOpenSearchRequest<SearchResult> {

  /**
   * Instantiates the request
   * 
   * @param searchQuery
   *   the search query applied to the request
   * @param pageCount
   *   the page count
   * @param fullMode
   *   whether the request should be performed in full mode
   */
  public SearchThingsRequest(SearchQuery searchQuery, int pageCount, boolean fullMode) {
    super(searchQuery, pageCount, SearchResult.class, fullMode);
  }
  
  /**
   * Instantiates the request
   * 
   * @param searchQuery
   *  the search query applied to the request
   * @param pageCount
   *  the page count
   * @param pageSize
   *  the maximum number of entries on a result page 
   * @param fullMode
   *  whether to perform the request in full mode
   */
  public SearchThingsRequest(SearchQuery searchQuery, int pageCount, int pageSize, boolean fullMode) {
    super(searchQuery, pageCount, SearchResult.class, pageSize, fullMode);
  }

  @Override
  protected SearchResult handleRequest(OpenService service) throws FlattrException {
    return service.searchThings(getSearchQuery(), getPageCount(), getPageSize());
  }

}
