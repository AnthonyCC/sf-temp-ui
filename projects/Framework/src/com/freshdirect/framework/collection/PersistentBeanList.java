package com.freshdirect.framework.collection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PersistentBeanI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * Abstract collection class for PersistentBean objects.
 *
 * Client needs to implement load(Connection).
 */
public abstract class PersistentBeanList<E extends PersistentBeanI> extends LocalObjectList<E> implements PersistentListI<E> {

	private static final long	serialVersionUID	= -8449586625863872684L;
	
	/** List of removed persistent beans, that need to be removed from database */
	private List<E> removedElements = new LinkedList<E>();

    /**
     * default constructor
     */
	public PersistentBeanList() {
		super();
	}

	/**
     * Copy constructor (shallow copy).
     * @param plist  */
	public PersistentBeanList(PersistentBeanList<? extends E> plist) {
		super(plist);
	}

	////////////////// isModified stuff //////////////////


	/**
	 * Check if the collection has been modified (including children).
	 *
	 * @return true if the collection needs to be persisted
	 */
	public boolean isModified() {

		if (removedElements.size()>0) {
			return true;
		}
		for (Iterator<E> i=this.iterator(); i.hasNext(); ) {
				PersistentBeanI pb = i.next();
				if ( pb.isModified() || pb.isAnonymous() ) {
					return true;
				}
			}

		return false;
	}

	////////////////// overriden add stuff //////////////////

    /**
     * Appends the specified element to the end of this list
     *
     * @param obj element to be appended to this list
     * @return  true
     */
	public synchronized boolean add(E obj) {
		boolean b = super.add(obj);
		return b;
	}

    /**
     * Inserts the specified element at the specified position in this list
     *
     * @param idx index at which the specified element is to be inserted
     * @param obj element to be inserted
     */
	public synchronized void add(int idx, E obj) {
		super.add(idx, obj);
	}

    /**
     * Appends all of the elements in the specified collection to the end of this list,
     * in the order that they are returned by the specified collection's iterator
     *
     * @param coll collection whose elements are to be added to this list
     * @return  true if this list changed as a result of the call
     */
	public synchronized boolean addAll(Collection<? extends E> coll) {
		boolean b = super.addAll(coll);
		return b;
	}

    /**
     * Inserts all of the elements in the specified collection into this list at the specified position
     *
     * @param idx index at which to insert first element from the specified collection
     * @param coll elements to be inserted into this list
     * @return true if this list changed as a result of the call
     */
	public synchronized boolean addAll(int idx, Collection<? extends E> coll) {
		boolean b = super.addAll(idx, coll);
		return b;
	}

	////////////////// overriden set stuff //////////////////

    /**
     * Replaces all of the elements of this list with the elements in the specified collection
     *
     * @param coll  the collection of new elements
     */
	public synchronized void set(Collection<? extends E> coll) {
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
	public synchronized E set(int idx, E obj) {
		E newBean = obj;
		E original = super.set(idx, newBean);
		if (original.hashCode()!=newBean.hashCode()) {
			// not the same, it was removed
			this.removedElements.add(original);
		}
		return original;
	}

    /**
     * Update an existing element, based on the primary key of the supplied model object.
     *
     * @param element the model to use to update the collection's corresponding element
     */
    public synchronized void update(ModelSupport element) {
		PersistentBeanI found = this.get( element.getPK() );
		if (found==null) {
			throw new CollectionException("Element not found, PK "+element.getPK());
		}
		found.setFromModel(element);
    }

	////////////////// overriden remove stuff //////////////////

    /** removes all elements from this list
     */
	public synchronized void clear() {
		removedElements.addAll( this );
		super.clear();
	}

    /**
     * @param idx
     * @return  */
	public synchronized E remove(int idx) {
		E removed = super.remove(idx);
		if (removed!=null) {
			removedElements.add(removed);
		}
		return removed;
	}

    /**
     * Removes the element at the specified position in this list
     *
     * @param idx the index of the element to removed
     * @return  the element previously at the specified position
     */
	public synchronized boolean remove(E obj) {
		boolean b = super.remove(obj);
		if (b) {
			removedElements.add(obj);
		}
		return b;
	}

	////////////////// deep copy //////////////////

	/**
     * Deep copy into a LocalObjectList of model objects.
     *
     * @return a deep copy of the list and its children
     */
	public LocalObjectList<ModelI> getModelList() {
		LocalObjectList<ModelI> lst = new LocalObjectList<ModelI>();
		lst.ensureCapacity( this.size() );
		for (Iterator<E> i=this.iterator(); i.hasNext(); ) {
			lst.add( i.next().getModel() );
		}
		return lst;
	}

	////////////////// persistence stuff //////////////////

	/**
     * Store the collection. (Sync to the DB)
     * @param conn the SQLConnection to use to save this object to the persistent store
     * @throws SQLException any problems writing this list to the persistent store
     */
	public synchronized void store(Connection conn) throws SQLException {
		this.syncToDatabase(conn);
	}

	/**
     * Create the collection. (Just syncs to the DB)
     * @param conn the SQLConnection to use to save this object to the persistent store
     * @throws SQLException any problems writing this list to the persistent store
     * @return the primary key of the newly created object
     */
	public PrimaryKey create(Connection conn) throws SQLException {
		this.syncToDatabase(conn);
		return null;
	}

	/**
     * Remove the collection. Removes every element, then syncs to the DB.
     * @param conn the SQLConnection to use to save this object to the persistent store
     * @throws SQLException any problems while removing this list from the persistent store
     */
	public synchronized void remove(Connection conn) throws SQLException {
		this.clear();
		this.syncToDatabase(conn);
	}

	/**
     * Synchronize changes to database.
     * @param conn the SQLConnection to use to save this object to the persistent store
     * @throws SQLException any problems while syncing to the persistent store
     */
	protected void syncToDatabase(Connection conn) throws SQLException {
		// remove
		for (Iterator<E> i=removedElements.iterator(); i.hasNext(); ) {
			PersistentBeanI bean = i.next();
			//LOGGER.debug("syncToDatabase remove "+bean);
			bean.remove(conn);
		}
		// store
		for (Iterator<E> i=this.iterator(); i.hasNext(); ) {
			PersistentBeanI bean = i.next();
				if (bean.isAnonymous()) {
					// new bean
					//LOGGER.debug("syncToDatabase create "+bean);
					bean.create(conn);
				} else if (bean.isModified()) {
					// changed bean
					//LOGGER.debug("syncToDatabase store "+bean);
					bean.store(conn);
				}
		}
		// cleanup
		removedElements.clear();
	}

}