package org.shredzone.flattr4j.model.result;

import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.result.impl.FlattrResultImpl.Message;

/**
 * 
 * @author iulius
 *
 */
public interface FlattrResult extends ThingId {

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

  /**
   * @return the thing
   */
  public abstract Thing getThing();

}