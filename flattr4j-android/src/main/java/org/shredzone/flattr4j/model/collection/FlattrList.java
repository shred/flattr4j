 package src.main.java.org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.Flattr;

/**
 * Typed instance of an ArrayList for {@link Flattr} objects
 * 
 * @author tuxbox
 * 
 */
public class FlattrList extends ArrayList<Flattr> {

  /**
   * 
   */
  private static final long serialVersionUID = 745588630781916160L;

  /**
   * @see {@link ArrayList#ArrayList()}
   */
  public FlattrList() {
    super();
  }

  /**
   * @see {@link ArrayList#ArrayList(int)}
   * @param capacity
   */
  public FlattrList(final int capacity) {
    super(capacity);
  }

  /**
   * @see {@link ArrayList#ArrayList(Collection)}
   * @param collection
   */
  public FlattrList(final Collection<? extends Flattr> collection) {
    super(collection);
  }

}
