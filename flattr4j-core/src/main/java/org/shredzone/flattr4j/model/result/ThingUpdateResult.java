package org.shredzone.flattr4j.model.result;

import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

/**
 * @author iulius
 *
 */
public interface ThingUpdateResult {

  /**
   * 
   * @return
   */
  public abstract Message getMessage();
  
  /**
   * 
   * @return
   */
  public abstract String getDescription();
  
}
