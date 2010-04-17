package com.freshdirect.framework.collection;

import java.util.*;

import com.freshdirect.framework.core.*;

/**
 * Collection of Model objects.  It assumes that all objects in
 * the list are local to the same VM as the list.
 */
public class LocalObjectList extends ArrayList implements ListI {

	/** default constructor.  creates an empty list.
     */
    public LocalObjectList() {
        super();
    }
    
    /** creates a list which is a shallow copy of the original.
     * @param mlist another <CODE>LocalObjectList</CODE> to copy
     */
    public LocalObjectList(LocalObjectList mlist) {
        super(mlist);
    }
    
    /** creates a list that contains all the elements of the supplied collection
     * @param models the models to add to the new list
     */
    public LocalObjectList(Collection models) {
        super(models);
    }
    
    ////////////////// get stuff //////////////////
    
    /** finds an object by its primary key
     * @param pk the primary key to search for
     * @return the object identified by the primary key
     */
    public Object get(PrimaryKey pk) {
        return get( indexOf(pk) );
    }
    
    /** gets a <CODE>List</CODE> of all of the elements of this list
     * @return a <CODE>List</CODE> of models
     */
    public List getAll() {
        return new ArrayList(this);
    }
    
    /**
     * @return  */
    public Enumeration enumeration() {
        return new Vector(this).elements();
    }
    
    ////////////////// add stuff //////////////////
    
    
    /**
     * Appends the specified element to the end of this list
     *
     * @param obj element to be appended to this list
     * @return  true
     */
    public synchronized boolean add(Object obj) {
        return super.add((ModelI)obj);
    }
    
    /**
     * Inserts the specified element at the specified position in this list
     *
     * @param idx index at which the specified element is to be inserted
     * @param obj element to be inserted
     */
    public synchronized void add(int idx, Object obj) {
        super.add(idx, (ModelI) obj);
    }
    
    /**
     * Appends all of the elements in the specified collection to the end of this list,
     * in the order that they are returned by the specified collection's iterator
     *
     * @param coll collection whose elements are to be added to this list
     * @return  true if this list changed as a result of the call
     */
    public synchronized boolean addAll(Collection coll) {
        // !!! enforce ModelI
        return super.addAll(coll);
    }
    
    /**
     * Inserts all of the elements in the specified collection into this list at the specified position
     *
     * @param idx index at which to insert first element from the specified collection
     * @param coll elements to be inserted into this list
     * @return true if this list changed as a result of the call
     */
    public synchronized boolean addAll(int idx, Collection coll) {
        // !!! enforce ModelI
        return super.addAll(idx, coll);
    }
    
    
    ////////////////// find stuff //////////////////
    
    /**
     * Returns true if this list contains an element identified by the primary key
     *
     * @param pk the primary key to search for
     * @return true if this list contains an element identified by the primary key
     */
    public boolean contains(PrimaryKey pk) {
        return (indexOf(pk) != -1);
    }
    
    /**
     * Returns the index in this list of the first occurrence of an element identified by the primary key,
     * or -1 if this list contains no such element
     *
     * @param pk the primary key to search for
     * @return  the index in this list of the first occurrence of the specified primary key, or -1 if this list contains no such element
     */
    public int indexOf(PrimaryKey pk) {
        if (pk!=null) {
            for (int i=0; i<this.size(); i++) {
                if (pk.equals( ((ModelI)this.get(i)).getPK() )) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the index in this list of the last occurrence of an element identified by the primary key,
     * or -1 if this list contains no such element
     *
     * @param pk the primary key to search for
     * @return  the index in this list of the last occurrence of the specified primary key, or -1 if this list contains no such element
     */
    public int lastIndexOf(PrimaryKey pk) {
        if (pk!=null) {
            for (int i=this.size()-1; i>=0; i--) {
                if (pk.equals( ((ModelI)this.get(i)).getPK() )) {
	                    return i;
	                }
            }
        }
        return -1;
    }
    
    ////////////////// set stuff //////////////////
    
    /**
     * Replaces all of the elements of this list with the elements in the specified collection
     *
     * @param coll  the collection of new elements
     */
    public synchronized void set(Collection coll) {
        this.clear();
        this.addAll(coll);
    }
    
    /**
     * Replaces the element at the specified position in this list with the specified element
     *
     * @param idx index of the element to replace
     * @param obj element to be stored at the specified position
     * @return the element previously at the specified position
     */
    public synchronized Object set(int idx, Object obj) {
        return super.set(idx, (ModelI) obj);
    }
    
    /**
     * Update an existing element, based on the primary key of the supplied model object.
     *
     * @param element the model to use to update the collection's corrspeonding element
     */
    public synchronized void update(ModelI element) {
	        int idx = this.indexOf(element.getPK());
	        if (idx==-1) {
	            throw new CollectionException("Element not found, PK "+element.getPK());
	        }
	        this.set(idx, element);
		}
    
    ////////////////// remove stuff //////////////////
    
    /** removes all elements from this list
     */
    public synchronized void clear() {
        super.clear();
    }
    
    /**
     * Removes the element at the specified position in this list
     *
     * @param idx the index of the element to removed
     * @return  the element previously at the specified position
     */
    public synchronized Object remove(int idx) {
        if ((idx < 0) || (idx >= this.size()))
            return null;
        else
            return super.remove(idx);
    }
    
    /**
     * Removes the element at the specified position in this list
     *
     * @param pk the primary key to search for
     * @return  the element identified the by supplied primary key
     */
    public Object removeByPK(PrimaryKey pk) {
        return remove(indexOf(pk));
    }
    
    /**
     * Removes the first occurrence in this list of the specified element
     *
     * @param obj element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public synchronized boolean remove(Object obj) {
        return super.remove((ModelI)obj);
    }
    
    /**
     * Removes from this list all the elements that are contained in the specified collection
     *
     * @param coll collection that defines which elements will be removed from this list
     * @return true if this list changed as a result of the call
     */
    public synchronized boolean removeAll(Collection coll) {
        return super.removeAll(coll);
    }
    
    
    /**
     * Retains only the elements in this list that are contained in the specified collection
     *
     * @param coll collection that defines which elements this set will retain
     * @return true if this list changed as a result of the call
     */
    public synchronized boolean retainAll(Collection coll) {
        return super.retainAll(coll);
    }
    
    ////////////////// comparsion, clone //////////////////
    
    
    
    /**
     * Compares the specified object with this list for equality.
     * Two lists are defined to be equal if they contain the same elements in the same order.
     *
     * @param obj the object to be compared for equality with this list
     * @return true if the specified object is equal to this list
     */
    public boolean equals(Object obj) {
        //
        // two ModelLists are equal if they contain the same objects
        // in the same order
    	if(obj == null)
    		return false;
    	
        if (!(obj instanceof LocalObjectList)) {
            return false;
        }
        LocalObjectList other = (LocalObjectList) obj;
        if (size() != other.size()) {
            return false;
        }
        Iterator myIter = iterator();
        Iterator otherIter = other.iterator();
        while (myIter.hasNext()) {
            if (!myIter.next().equals(otherIter.next())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Creates and returns a shallow copy of this list
     *
     * @return a shallow copy of this list
     */
    public Object clone() {
        return new LocalObjectList( this );
    }
    
}
