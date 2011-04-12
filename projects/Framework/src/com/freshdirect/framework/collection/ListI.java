package com.freshdirect.framework.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * a collection class that extends java.util.List.  It adds some additional
 * functionality.  It allows objects to be found by their primary key.
 * Individual elements can be updated by passing the collection a model.
 * The elements can be returned as an enumeration for EJB 1.0 compatibility.
 */ 
public interface ListI<E extends ModelI> extends Serializable, List<E> {

	/**
     * Find element by primary key.
     * @param pk the primary key
     * @return the located object
     */
	public Object get(PrimaryKey pk);

    /**
     * indicated whether the collection contains an object represented
     * by a primary key.
     * @param pk the primary key to search for
     * @return true, if the collection contains an object identified
     * by the primary key.
     */    
	public boolean contains(PrimaryKey pk);

    /** return the first position in the collection of the first object
     * represented by the primary key.
     * @param pk the primary key to search for
     * @return the position of the object represented by the primary key
     * within the collection or -1 if the primary key cannot
     * be located.
     */    
	//public int indexOf(PrimaryKey pk);

    /** return the first position in the collection of the first object
     * represented by the primary key.
     * @param pk the primary key to search for
     * @return the position of the object represented by the primary key
     * within the collection or -1 if the primary key cannot
     * be located.
     */    
	//public int lastIndexOf(PrimaryKey pk);

    /** replaces the elements of this collection with the elements of
     * the supplied collection
     * @param coll the new elements to be held by this collection
     */    
	public void set(Collection<? extends E> c);

    /** locates an object within the collectoin based on the model's
     * primary key and sets it properties from the model
     * @param element the model to use to update a collection element
     */    
	public void update(E element);

    /** removes an element from the collection that are identified by
     * this primary key
     * @param pk the primary key to search for
     * @return the removed element
     */    
	public Object removeByPK(PrimaryKey pk);
	
	/**
     * For EJB 1.0 compatibility, returns the elements of this
     * collection as an enumeration
     * @return an enumeration of the elements of this collection
     */
    //public Enumeration enumeration();

  
}