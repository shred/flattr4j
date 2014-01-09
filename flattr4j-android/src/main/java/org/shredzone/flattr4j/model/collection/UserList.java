package src.main.java.org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.User;

/**
 * Typed instance of an ArrayList for {@link User} objects
 * 
 * @author tuxbox
 * 
 */
public class UserList extends ArrayList<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7002076715632092875L;

	  /**
	   * @see {@link ArrayList#ArrayList()}
	   */
	  public UserList() {
	    super();
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(int)}
	   * @param capacity
	   */
	  public UserList(final int capacity) {
	    super(capacity);
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(Collection)}
	   * @param collection
	   */
	  public UserList(final Collection<? extends User> collection) {
	    super(collection);
	  }
}
