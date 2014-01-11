package org.shredzone.flattr4j.model.result;

import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

public interface ThingResult {

  /* (non-Javadoc)
   * @see org.shredzone.flattr4j.model.result.FlattrResult#getMessage()
   */
  public abstract Message getMessage();

  /**
   * 
   * @return
   */
  public abstract String getLocation();

}