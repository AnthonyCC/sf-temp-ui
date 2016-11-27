package com.freshdirect.framework.collection;

import com.freshdirect.framework.core.*;

import java.util.*;

/**
 * Abstract collection class for DependentPersistentBean objects.
 * Represents a one-to-many relationship, where the children are responsible
 * for storing the reference to the parent.
 *
 * Client needs to implement load(Connection).
 *
 * @version $Revision$
 * @author $Author$
 */ 
public abstract class DependentPersistentBeanList<E extends DependentPersistentBeanI> extends PersistentBeanList<E> {

	private static final long	serialVersionUID	= 7633700635750134770L;
	
	/** Primary key of parent object
     */
	private PrimaryKey parentPK;
	
    /** default constructor
     */    
	public DependentPersistentBeanList() {
		super();
	}
	
    /** constructor that sets the parent's primary key
     * @param parentPK the parent's primary key
     */    
	public DependentPersistentBeanList(PrimaryKey parentPK) {
		super();
		this.setParentPK(parentPK);
	}
	
    /** sets the primary key of the parent object of the collection's children
     * @param parentPK the parent's primary key
     */    
	public void setParentPK(PrimaryKey parentPK) {
		this.parentPK = parentPK;
		if (!this.isEmpty()) {
			for (Iterator<E> i=this.iterator(); i.hasNext(); ) {
				applyParentPK(i.next()); 
			}
		}
	}
	
	/**
	 * Get the primary key of the parent object.
	 *
	 * @return the parent's primary key
	 */
	protected PrimaryKey getParentPK() {
		return this.parentPK;
	}
	
    /** private helper method to set the parent primary key of a dependent child bean
     * @param obj the dependent child object to set the parent primary key on
     * @return the child object after the primary key has been applied
     */    
	private E applyParentPK(E bean) {
		bean.setParentPK( this.parentPK );
		return bean;
	}
	
	////////////////// overridden add stuff //////////////////

    /** adds a dependent child object to this collection
     * @param obj the dependent child object to add
     * @return true
     */    
	public synchronized boolean add(E obj) {
		return super.add( applyParentPK(obj) );
	}

    /** adds a dependent child object at the specified index
     * @param idx the position in the collection to add the child object at
     * @param obj the child object to add
     */    
	public synchronized void add(int idx, E obj) {
		super.add(idx, applyParentPK(obj));
	}
	
    /** adds all of the dependent child objects in the collection to this collection
     * @param coll the collection of child objects to add
     * @return true if the list was modified
     */    
	public synchronized boolean addAll(Collection<? extends E> coll) {
		for (Iterator<? extends E> i=coll.iterator(); i.hasNext(); ) {
			applyParentPK( i.next() );
		}
		return super.addAll(coll);
	}

    /** adds all of the child objects in the supplied collection at the specified index
     * @param idx the position in the collection to add the children at
     * @param coll the collection of child objects to add
     * @return true if the list was modified
     */    
	public synchronized boolean addAll(int idx, Collection<? extends E> coll) {
		for (Iterator<? extends E> i=coll.iterator(); i.hasNext(); ) {
			applyParentPK( i.next() );
		}
		return super.addAll(idx, coll);
	}

	////////////////// overridden set stuff //////////////////

    /** replaces a child object at the specified index
     * @param idx the index at which to replace the child object
     * @param obj the new object to replace the previous object with
     * @return the child object previous at the specified position
     */    
	public synchronized E set(int idx, E obj) {
		return super.set(idx, applyParentPK(obj));
	}
	
	public synchronized void update(E element) {
		this.applyParentPK(element);
		super.update(element);
	}


	public static interface DependentFactory<T extends DependentPersistentBeanI> {
		public T createBeanFromModel(T model);		
	}

	public void setDependentsFromModel(DependentFactory<E> factory, Collection<E> col){

		if (this.isEmpty()) {
			List<E> persistentBeans = new ArrayList<E>();
			for (Iterator<E> i=col.iterator(); i.hasNext(); ) {
				E model = i.next();
				E pb = factory.createBeanFromModel(model);
				this.applyParentPK(pb);
				persistentBeans.add( pb );
			}
			this.set(persistentBeans);
			return;
		}

		Map<PrimaryKey,E> currentItems = new HashMap<PrimaryKey,E>(this.size());
		
		for (Iterator<E> i = this.iterator(); i.hasNext(); ) {
			E pb = i.next();
			currentItems.put(pb.getPK(), pb);
		}

		for (Iterator<E> i = col.iterator(); i.hasNext(); ) {
			E model = i.next();
			if (model.isAnonymous()) {
				E pb = factory.createBeanFromModel(model);
				this.applyParentPK(pb);
				this.add(pb);

			} else {
				PersistentBeanI pb = currentItems.get(model.getPK());
				
				if (pb!=null) {
					pb.setFromModel(model);
					currentItems.remove(model.getPK());
				}
			}
		}
		
		for(Iterator<E> i = currentItems.values().iterator(); i.hasNext(); ){
			PersistentBeanI pb = i.next();
			this.removeByPK(pb.getPK());
		}
		
	}

}