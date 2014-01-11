package org.shredzone.flattr4j.model.result;

import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

/**
 * 
 * @author tuxbox
 *
 */
public interface ThingResult {

  /**
   * 
   * @return
   */
  public abstract String getId();
  
  /**
   * 
   * @return
   */
  public abstract Message getMessage();

  /**
   * 
   * @return
   */
  public abstract String getLocation();

  /**
   * 
   * @return
   */
  public abstract String getDescription();
  
}