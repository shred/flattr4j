package org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.Subscription;

/**
 * Typed instance of an ArrayList for {@link Subscription} objects
 * 
 * @author tuxbox
 * 
 */
public class SubscriptionList extends ArrayList<Subscription> {

  /**
   * 
   */
  private static final long serialVersionUID = 3027936396982108573L;

  /**
   * @see {@link ArrayList#ArrayList()}
   */
  public SubscriptionList() {
    super();
  }
  
  /**
   * @see {@link ArrayList#ArrayList(int)}
   * @param capacity
   */
  public SubscriptionList(final int capacity) {
	  super(capacity);
  }

  /**
   * @see {@link ArrayList#ArrayList(Collection)}
   * @param collection
   */
  public SubscriptionList(final Collection<? extends Subscription> collection) {
    super(collection);
  }

}
