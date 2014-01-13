package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.model.SearchQuery;

/**
 * The base request to perform search requests on unprotected, public flattr resources
 * 
 * @author tuxbox
 *
 */
public abstract class BaseOpenSearchRequest<RESULT> extends BaseOpenFlattrPagingRequest<RESULT> {

  private SearchQuery searchQuery;

  /**
   * Instantiates the request
   * 
   * @param searchQuery
   *  the search query to perform
   * @param pageCount
   *  the page count
   * @param clazz
   *  the class of the result type
   * @param fullMode
   *  whether the request should be performed in full mode
   */
  public BaseOpenSearchRequest(SearchQuery searchQuery, int pageCount, Class<RESULT> clazz, boolean fullMode) {
    super(pageCount, clazz, fullMode);
    this.searchQuery = searchQuery;
  }

  /**
   * Instantiates the base request
   * 
   * @param searchQuery
   *  the search query to perform
   * @param pageCount
   *  the page count
   * @param clazz
   *  the class of the result type
   * @param pageSize
   *  the number of entries of the result page
   * @param fullMode
   *  whether the request should be performed in full mode
   */
  public BaseOpenSearchRequest(SearchQuery searchQuery, int pageCount, Class<RESULT> clazz, int pageSize, boolean fullMode) {
    super(pageCount, clazz, pageSize, fullMode);
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
