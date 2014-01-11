package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.model.SearchQuery;

/**
 * Provides common functionality required to perform an authenticated search request on the flattr API
 * 
 * @author iulius
 *
 */
public abstract class BaseAuthenticatedSearchRequest<RESULT> extends BaseAuthenticatedFlattrPagingRequest<RESULT> {

  private SearchQuery searchQuery;

  /**
   * Instantiates the request
   * 
   * @param searchQuery
   *   the search query to be performed
   * @param accessToken
   *   the access token to be used
   * @param pageCount
   *   the page count
   * @param clazz
   *   the class of the result type
   * @param pageSize
   *   the maximum number of entries of a result page
   * @param fullMode
   *   whether full mode should be applied
   */
  protected BaseAuthenticatedSearchRequest(SearchQuery searchQuery, String accessToken, int pageCount, Class<RESULT> clazz, int pageSize, boolean fullMode) {
    super(accessToken, pageCount, clazz, pageSize, fullMode);
    this.searchQuery = searchQuery;
  }

  /**
   * @return the searchQuery
   */
  public SearchQuery getSearchQuery() {
    return searchQuery;
  }

  /**
   * @param searchQuery the searchQuery to set
   */
  public void setSearchQuery(SearchQuery searchQuery) {
    this.searchQuery = searchQuery;
  }

}
