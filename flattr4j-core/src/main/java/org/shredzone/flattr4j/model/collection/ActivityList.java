package src.main.java.org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.Activity;

/**
 * Typed instance of an ArrayList for {@link Activity} objects
 * 
 * @author tuxbox
 * 
 */
public class ActivityList extends ArrayList<Activity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2418944901572130168L;

	  /**
	   * @see {@link ArrayList#ArrayList()}
	   */
	  public ActivityList() {
	    super();
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(int)}
	   * @param capacity
	   */
	  public ActivityList(final int capacity) {
	    super(capacity);
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(Collection)}
	   * @param collection
	   */
	  public ActivityList(final Collection<? extends Activity> collection) {
	    super(collection);
	  }
	
	
}
