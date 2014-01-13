package org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.Thing;

/**
 * Typed instance of an ArrayList for {@link Thing} objects
 * 
 * @author tuxbox
 * 
 */
public class ThingList extends ArrayList<Thing> {

  /**
   * 
   */
  private static final long serialVersionUID = 6000413334854959648L;

  /**
   * @see {@link ArrayList#ArrayList()}
   */
  public ThingList() {
    super();
  }

  /**
   * @see {@link ArrayList#ArrayList(int)}
   * @param capacity
   */
  public ThingList(final int capacity) {
    super(capacity);
  }

  /**
   * @see {@link ArrayList#ArrayList(Collection)}
   * @param collection
   */
  public ThingList(final Collection<? extends Thing> collection) {
    super(collection);
  }

}
