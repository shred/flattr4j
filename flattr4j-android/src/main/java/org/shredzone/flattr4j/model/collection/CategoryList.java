package org.shredzone.flattr4j.model.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.shredzone.flattr4j.model.Category;

/**
 * Typed instance of an ArrayList for {@link Category} objects
 * 
 * @author tuxbox
 * 
 */
public class CategoryList extends ArrayList<Category> {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -297303644975255616L;

	/**
	   * @see {@link ArrayList#ArrayList()}
	   */
	  public CategoryList() {
	    super();
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(int)}
	   * @param capacity
	   */
	  public CategoryList(final int capacity) {
	    super(capacity);
	  }

	  /**
	   * @see {@link ArrayList#ArrayList(Collection)}
	   * @param collection
	   */
	  public CategoryList(final Collection<? extends Category> collection) {
	    super(collection);
	  }
	
}
