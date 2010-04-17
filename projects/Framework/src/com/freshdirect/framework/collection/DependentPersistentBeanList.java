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
public abstract class DependentPersistentBeanList extends PersistentBeanList {

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
			for (Iterator i=this.iterator(); i.hasNext(); ) {
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
     * @return the child object after theprimary key has been applied
     */    
	private DependentPersistentBeanI applyParentPK(Object obj) {
		DependentPersistentBeanI bean = (DependentPersistentBeanI) obj;
		bean.setParentPK( this.parentPK );
		return bean;
	}
	
	////////////////// overriden add stuff //////////////////

    /** adds a dependent child object to this collection
     * @param obj the dependent child object to add
     * @return true
     */    
	public synchronized boolean add(Object obj) {
		return super.add( applyParentPK(obj) );
	}

    /** adds a dependent child object at the specified index
     * @param idx the position in the collection to add the child object at
     * @param obj the child object to add
     */    
	public synchronized void add(int idx, Object obj) {
		super.add(idx, applyParentPK(obj));
	}
	
    /** adds all of the dependent child objects in the collection to this collection
     * @param coll the collectionof child objects to add
     * @return true if the list was modified
     */    
	public synchronized boolean addAll(Collection coll) {
		for (Iterator i=coll.iterator(); i.hasNext(); ) {
			applyParentPK( i.next() );
		}
		return super.addAll(coll);
	}

    /** adds all of the child objects in the supplied collection at the specified index
     * @param idx the position in the collection to add the children at
     * @param coll the collection of child objects to add
     * @return true if the list was modified
     */    
	public synchronized boolean addAll(int idx, Collection coll) {
		for (Iterator i=coll.iterator(); i.hasNext(); ) {
			applyParentPK( i.next() );
		}
		return super.addAll(idx, coll);
	}

	////////////////// overriden set stuff //////////////////

    /** replaces a child object at the specified index
     * @param idx the index at which to replace the child object
     * @param obj the new object to replace the previous object with
     * @return the child object previous at the specified position
     */    
	public synchronized Object set(int idx, Object obj) {
		return super.set(idx, applyParentPK(obj));
	}
	
	public synchronized void update(ModelSupport element) {
		this.applyParentPK(element);
		super.update(element);
	}


	public static interface DependentFactory {
		public DependentPersistentBeanI createBeanFromModel(ModelI model);		
	}

	public void setDependentsFromModel(DependentFactory factory, Collection col){

		if (this.isEmpty()) {
			List persistentBeans = new ArrayList();
			for (Iterator i=col.iterator(); i.hasNext(); ) {
				ModelI model = (ModelI) i.next();
				DependentPersistentBeanI pb = factory.createBeanFromModel(model);
				this.applyParentPK(pb);
				persistentBeans.add( pb );
			}
			this.set(persistentBeans);
			return;
		}

		Map currentItems = new HashMap(this.size());
		
		for (Iterator i = this.iterator(); i.hasNext(); ) {
			PersistentBeanI pb = (PersistentBeanI)i.next();
			currentItems.put(pb.getPK(), pb);
		}

		for (Iterator i = col.iterator(); i.hasNext(); ) {
			ModelI model = (ModelI)i.next();
			if (model.isAnonymous()) {
				DependentPersistentBeanI pb = factory.createBeanFromModel(model);
				this.applyParentPK(pb);
				this.add(pb);

			} else {
				PersistentBeanI pb = (PersistentBeanI)currentItems.get(model.getPK());
				
				if (pb!=null) {
					pb.setFromModel(model);
					currentItems.remove(model.getPK());
				}
			}
		}
		
		for(Iterator i = currentItems.values().iterator(); i.hasNext(); ){
			PersistentBeanI pb = (PersistentBeanI)i.next();
			this.removeByPK(pb.getPK());
		}
		
	}

}