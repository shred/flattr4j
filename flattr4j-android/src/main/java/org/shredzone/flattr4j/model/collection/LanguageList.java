package src.main.java.org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.Language;

/**
 * Typed instance of an ArrayList for {@link LanguageList} objects
 * 
 * @author tuxbox
 * 
 */
public class LanguageList extends ArrayList<Language> {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 5513669048340313550L;

	/**
	   * @see {@link ArrayList#ArrayList()}
	   */
	  public LanguageList() {
	    super();
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(int)}
	   * @param capacity
	   */
	  public LanguageList(final int capacity) {
	    super(capacity);
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(Collection)}
	   * @param collection
	   */
	  public LanguageList(final Collection<? extends Language> collection) {
	    super(collection);
	  }
	
}
