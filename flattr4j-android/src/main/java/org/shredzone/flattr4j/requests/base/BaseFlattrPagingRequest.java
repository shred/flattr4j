package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.OpenService;

/**
 * This class implements shared functionality for all flattr requests returning pageable results
 * 
 * @author tuxbox
 *
 */
public abstract class BaseFlattrPagingRequest<RESULT, ServiceType extends OpenService> extends BaseFlattrRequest<RESULT, ServiceType> {

  public static final int MAX_PAGE_SIZE_PER_QUERY = 30;

  private int pageSize;
  private int pageCount;
  private boolean fullMode;

  /**
   * Instantiates a paging request
   * 
   * @param pageCount
   *  the number of the page to be retrieved
   * @param clazz
   *  the clazz of the result
   * @param fullMode
   *  whether full mode should be used (e.g. for user objects)
   */
	protected BaseFlattrPagingRequest(int pageCount, Class<RESULT> clazz, boolean fullMode) {
	  this(pageCount, clazz, MAX_PAGE_SIZE_PER_QUERY, fullMode);
	}

	/**
	 * Instantiates a paging requests that allows a arbitrary page size definition
	 *  
	 * @param pageCount
	 *   the number of the page to be retrieved
	 * @param clazz
	 *   the clazz of the result
	 * @param pageSize
	 *   the number of entries on a page (values below 1 and greater than {@link #MAX_PAGE_SIZE_PER_QUERY} will be set to {@link #MAX_PAGE_SIZE_PER_QUERY}) 
	 * @param fullMode
	 *   whether full mode should be used (e.g. for user objects)
	 */
	protected BaseFlattrPagingRequest(int pageCount, Class<RESULT> clazz, int pageSize, boolean fullMode) {
	  super(clazz);
	  setPageCount(pageCount);
	  setPageSize(pageSize);
	  this.fullMode = fullMode;
	}

  /**
   * @return the pageSize
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * @param pageSize
   *  the pageSize to set, any value smaller than 1 and greater than {@link #MAX_PAGE_SIZE_PER_QUERY} will be set to {@link #MAX_PAGE_SIZE_PER_QUERY}
   */
  public void setPageSize(int pageSize) {
    if( pageSize < 1 || pageSize > MAX_PAGE_SIZE_PER_QUERY ) {
      this.pageSize = MAX_PAGE_SIZE_PER_QUERY;
    } else {
      this.pageSize = pageSize;
    }
  }

  /**
   * @return the pageCount
   */
  public int getPageCount() {
    return pageCount;
  }

  /**
   * @param pageCount the pageCount to set
   */
  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  /**
   * @return the fullMode
   */
  public boolean isFullMode() {
    return fullMode;
  }

  /**
   * @param fullMode the fullMode to set
   */
  public void setFullMode(boolean fullMode) {
    this.fullMode = fullMode;
  }
  
  @Override
  protected void configureService(ServiceType service) {
    service.setFullMode(isFullMode());
  }

}
