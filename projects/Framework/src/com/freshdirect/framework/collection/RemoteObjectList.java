/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Vector;
import java.rmi.RemoteException;
import javax.ejb.*;
import java.sql.*;

import com.freshdirect.framework.core.*;

/**
 * an abstract class to extend to maintain a list of remotely reachable objects.
 * this list supports lazy lookup of the remote objects from a list of primary keys.
 * methods which accept objects or collections as parameters can accept either
 * primary keys or remote objects
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class RemoteObjectList implements PersistentListI {
    
    /** the home interface of the remote objects managed by this list
     */
    private EJBHome     home                = null;
    
    /** the list of primary keys of the remote objects
     */
    private ArrayList   pkList              = null;
    /** the list of remote objects.  it may contain nulls as placeholders for objects that have not been
     * found via the home interface yet.
     */
    private ArrayList   entityList          = null;
    
    /** a backup of the primary key list to restore from if exceptions
     * are encountered while modifying itself.
     */
    private ArrayList   pkListBackup        = null;
    /** a backup copy of the remote object list to rollback to if any
     * exceptions are encountered while modifying this list
     * internally.
     */
    private ArrayList   entityListBackup    = null;
    
    /** indicates this list has been modified and needs to be sync'ed
     * to the persistent store
     */
    private boolean     modified            = false;
    
    /**
     * primary key of the parent object
     */
    private PrimaryKey  parentPK            = null;
    
    /**
     * takes care of persisting this list
     */
    private PersistentReferences refManager = null;

    
    
    //////////////////////////// constructors ////////////////////////////////
    
    /** creates a remote object list
     *
     */
    public RemoteObjectList() {
        super();
        this.home = null;
        this.pkList = new ArrayList();
        this.entityList = new ArrayList();
        this.refManager = createReferenceManager();
    }
    
    /** creates a remote object list
     *
     */
    public RemoteObjectList(PrimaryKey pk) {
        this();
        this.refManager.setParentId(pk.getId());
    }
    
    /** creates a shallow copy of a remote object list
     * @param remoteList the remote object list to copy
     */
    protected RemoteObjectList(RemoteObjectList remoteList) {
        super();
        this.home = remoteList.getHome();
        this.pkList = new ArrayList(remoteList.getPkList());
        this.entityList = new ArrayList(remoteList.getEntityList());
        this.refManager = getReferenceManager();
        PersistentReferences pRefs = remoteList.getReferenceManager();
        this.refManager = new PersistentReferences(pRefs.getTableName(), pRefs.getParentFieldName(), pRefs.getRefFieldName());
    }
    
    /////////////////////////// constructor assistant ///////////////////////////
    
    /**
     * template method to create a reference manager to take of the underlying
     * persistence of remote lists
     */
    protected abstract PersistentReferences createReferenceManager();
    
    /**
     * sets the parent id of the encapsulated reference manager
     */
    public void setParentPK(PrimaryKey pk) {
        this.parentPK = pk;
        this.refManager.setParentId(pk.getId());
    }
    
    public PrimaryKey getParentPK() {
        return this.parentPK;
    }
    
    /////////////////////////// setter for home interface ////////////////////////////////
    
    /** sets the home interface used to find remote child elements
     * @param ejbhome the home used to find child elements
     */
    public void setEJBHome(EJBHome ejbhome) {
        this.home = ejbhome;
    }
    
    //////////////////////// protected getters used by copy constructor /////////////////////
    
    /** gets the home interface for the remote objects managed by this list
     * @return the home interface
     */
    protected EJBHome getHome() {
        return this.home;
    }
    
    /** a list of the primary keys that identify the remote objects to be managed by this list
     * @return the primary key list
     */
    protected ArrayList getPkList() {
        return this.pkList;
    }
    
    /** gets the list of remote objects managed by this list
     * @return the list of remote objects
     */
    protected ArrayList getEntityList() {
        return this.entityList;
    }
    
    /** get the reference manager for objects managed by this list
     * @return the reference manager
     */
    protected PersistentReferences getReferenceManager() {
        return this.refManager;
    }
    
    ////////////////////// protected helper methods ////////////////////////
    
    /** uses the list's home interface to find remote objects from their primary key.
     * this method must be implemented by users of this class.
     * @return the located remote object
     * @param pk the primayr key to use to locate the remote object
     * @throws RemoteException any system problems encountered while locating a remote object
     * @throws FinderException any problems encountered finding the remote object, including not being able to find the remote object
     */
    protected abstract EntityBeanRemoteI findEntity(PrimaryKey pk) throws RemoteException, FinderException;
    
    /** locates all remote objects managed by this list that haven't
     * been located yet.
     */
    protected synchronized void ensureEntitiesAreLoaded() {
        if (!this.entityList.contains(null)) return;
        try {
            for (int i=0;i<this.pkList.size();i++) {
                if (this.entityList.get(i) == null)
                    this.entityList.set(i, findEntity((PrimaryKey) this.pkList.get(i)));
            }
        } catch (RemoteException re) {
            throw new CollectionException(re);
        } catch (FinderException fe) {
            throw new CollectionException(fe);
        }
    }
    
    /** locates a collection of remote objects from a collection of primary keys
     * @param pks the collection of primary keys identifying the objects to locate
     * @throws RemoteException any system level problems encountered while looking up the remote objects
     * @throws FinderException any problems encountered while locating the remote objects
     * @return the collection of remote objects
     */
    protected Collection getEntities(Collection pks) throws RemoteException, FinderException {
        ArrayList entities = new ArrayList();
        Iterator iter = pks.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            entities.add(findEntity((PrimaryKey)obj));
        }
        return entities;
    }
    
    /** gets a collection of primary keys from a collection of remote objects
     * @param entities the collection of remote objects to get the primary keys from
     * @throws RemoteException any system level problems encountered while fetching the keys of the remote objects
     * @return the collection of primary keys
     */
    protected Collection getPrimaryKeys(Collection entities) throws RemoteException {
        Iterator iter = entities.iterator();
        ArrayList pks = new ArrayList();
        while (iter.hasNext()) {
            EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) iter.next();
            pks.add(ejbremote.getPrimaryKey());
        }
        return pks;
    }
    
    /** a convenience method to create a set of placeholders for remote objects that haven't been located up yet
     * @param size the number of placeholders needed
     * @return a Collection containing the specified number of nulls
     */
    protected static Collection getListOfNulls(int size) {
        ArrayList nada = new ArrayList();
        for (int i=0;i<size;i++) nada.add(null);
        return nada;
    }
    
    
    
    //////////////////// methods that help a RemoteObjectList recover from exceptions ///////////////////////
    
    /** saves the internal state of the list into a set of backup lists
     */
    protected void makeBackup() {
        this.pkListBackup = new ArrayList(this.pkList);
        this.entityListBackup = new ArrayList(this.entityList);
    }
    
    /** restores the internal state of the remote list from the backup lists
     */
    protected void restoreFromBackup() {
        this.pkList = new ArrayList(this.pkListBackup);
        this.entityList = new ArrayList(this.entityListBackup);
        this.pkListBackup = null;
        this.entityListBackup = null;
    }
    
    
    /////////////////////////////// deep copy ////////////////////////////////////////
    
    /**
     * Deep copy into a LocalObjectList of model objects.
     *
     * @return a deep copy of the list and its children
     */
	public LocalObjectList getModelList() {
        ensureEntitiesAreLoaded();
		LocalObjectList lst = new LocalObjectList();
		lst.ensureCapacity( this.size() );
        try {
            for (Iterator i=this.entityList.iterator(); i.hasNext(); ) {
                lst.add( ((EntityBeanRemoteI)i.next()).getModel() );
            }
            return lst;
        } catch (RemoteException re) {
            throw new CollectionException(re, "Unable to get a model from a remote object");
        }
	}
    
    
    
    //////////////////////////////// persistence methods /////////////////////////////
    
    /**
     * Check if the collection has been modified
     *
     * @return true if the collection needs to be persisted
     */
    public boolean isModified() {
        return this.modified;
    }
    
    /**
     * sets the dirty flag
     */
    protected void setModified() {
        this.modified = true;
    }
    
    /**
     * clears the dirty flag
     */
    protected void unsetModified() {
        this.modified = false;
    }
    
    /**
     * private helper method to convert the list of primary keys
     * to a list of id's for the reference manager
     */
    private String[] getPkIds() {
        String[] ids = new String[pkList.size()];
        for (int i=0;i<pkList.size();i++)
            ids[i] = ((PrimaryKey) pkList.get(i)).getId();
        return ids;
    }
    
    /**
     * subclasses of RemoteObjectList need to be able to
     * create the correct type of primary key for the
     * objects that it aggregates
     */
    protected abstract PrimaryKey idToPK(String id);
    
    public PrimaryKey create(Connection conn) throws SQLException {
        refManager.setRefs(getPkIds());
        refManager.create(conn);
        return null;
    }
    
    public void load(Connection conn) throws SQLException {
        refManager.load(conn);
        String[] ids = refManager.getRefs();
        LinkedList pks = new LinkedList();
        for (int i=0;i<ids.length;i++) {
            pks.add(idToPK(ids[i]));
        }
        set(pks);
    }
    
    public void store(Connection conn) throws SQLException {
        refManager.setRefs(getPkIds());
        refManager.store(conn);
    }
    
    public void remove(Connection conn) throws SQLException {
        refManager.setRefs(getPkIds());
        refManager.remove(conn);
    }
    
    
    /////////////////////////////// contains methods //////////////////////////////////////////
    
    /**
     * Returns true if this list contains an element identified by the primary key
     *
     * @param pk the primary key to search for
     * @return true if this list contains an element identified by the primary key
     */
    public boolean contains(PrimaryKey pk) {
        return this.pkList.contains(pk);
    }
    
    /**
     * Returns true if this list contains the specified remote object
     *
     * @param obj the remote object to search for
     * @return true if this list contains the remote object
     */
    public boolean contains(java.lang.Object obj) {
        if (!this.entityList.contains(null)) {
            return this.entityList.contains(obj);
        } else {
            try {
                PrimaryKey pk = (PrimaryKey) ((EntityBeanRemoteI) obj).getPrimaryKey();
                return this.pkList.contains(pk);
            } catch (RemoteException re) {
                throw new CollectionException(re);
            }
        }
    }
    
    
    /**
     * Returns true if this list contains all of the elements of the specified collection
     *
     * @param collection collection to be checked for containment in this list
     * @return  true if this list contains all of the elements of the specified collection
     */
    public boolean containsAll(java.util.Collection collection) {
        Iterator iter = collection.iterator();
        Object obj = iter.next();
        if (obj instanceof PrimaryKey) {
            return this.pkList.containsAll(collection);
        } else {
            if (this.entityList.contains(null))
                ensureEntitiesAreLoaded();
            return this.entityList.containsAll(collection);
        }
    }
    
    
    
    //////////////////////////////// index methods /////////////////////////////////////
    
    /**
     * Returns the index in this list of the first occurrence of an element identified by the primary key,
     * or -1 if this list contains no such element
     *
     * @param pk the primary key to search for
     * @return  the index in this list of the first occurrence of the specified primary key, or -1 if this list contains no such element
     */
    public int indexOf(PrimaryKey pk) {
        return this.pkList.indexOf(pk);
    }
    
    /**
     * Returns the index in this list of the first occurrence of the specified element, or -1 if this list does not contain this element
     *
     * @param obj element to search for
     * @return the index in this list of the first occurrence of the specified element, or -1 if this list does not contain this element
     */
    public int indexOf(java.lang.Object obj) {
        EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) obj;
        try {
            return this.indexOf(ejbremote.getPrimaryKey());
        } catch (RemoteException re) {
            throw new CollectionException(re);
        }
    }
    
    /**
     * Returns the index in this list of the last occurrence of an element identified by the specified primary key, or -1 if this list contains no such element
     *
     * @param objthe  primary key  to search for
     * @return the index in this list of the last occurrence of an element identified by the specified primary key, or -1 if this list contains no such element
     */
    public int lastIndexOf(PrimaryKey pk) {
        return this.pkList.lastIndexOf(pk);
    }
    
    /**
     * Returns the index in this list of the last occurrence of the specified element, or -1 if this list does not contain this element
     *
     * @param obj element to search for
     * @return the index in this list of the last occurrence of the specified element, or -1 if this list does not contain this element
     */
    public int lastIndexOf(java.lang.Object obj) {
        EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) obj;
        try {
            return this.lastIndexOf(ejbremote.getPrimaryKey());
        } catch (RemoteException re) {
            throw new CollectionException(re);
        }
    }
    
    
    
    ///////////////////////////////// getters ////////////////////////////////////
    
    /**
     * finds an object by its primary key
     *
     * @param pk the primary key to search for
     * @return the object identified by the primary key
     */
    public Object get(PrimaryKey pk) {
        int idx = this.pkList.indexOf(pk);
        if (idx == -1) return null;
        if (this.entityList.get(idx) == null) {
            try {
                EntityBeanRemoteI ejbremote = findEntity(pk);
                this.entityList.set(idx, ejbremote);
                return ejbremote;
            } catch (RemoteException re) {
                throw new CollectionException(re);
            } catch (FinderException fe) {
                throw new CollectionException(fe);
            }
        } else {
            return this.entityList.get(idx);
        }
    }
    
    /**
     * Returns the element at the specified position in this list
     *
     * @param idx index of element to return
     * @return  the element at the specified position in this list
     */
    public java.lang.Object get(int idx) {
        if (this.entityList.get(idx) == null) {
            try {
                EntityBeanRemoteI ejbremote = findEntity((PrimaryKey)this.pkList.get(idx));
                this.entityList.set(idx, ejbremote);
                return ejbremote;
            } catch (RemoteException re) {
                throw new CollectionException(re);
            } catch (FinderException fe) {
                throw new CollectionException(fe);
            }
        } else {
            return this.entityList.get(idx);
        }
    }
    
    /**
     * Returns the number of elements in this list
     *
     * @return the number of elements in this list
     */
    public int size() {
        return this.pkList.size();
    }
    
    /**
     * Returns true if this list contains no elements
     *
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return this.pkList.isEmpty();
    }
    
    
    
    
    
    ///////////////////////// methods that add elements ////////////////////////////
    
    /**
     * Inserts the specified element at the specified position in this list
     *
     * @param idx index at which the specified element is to be inserted
     * @param obj element to be inserted
     */
    public synchronized void add(int idx, java.lang.Object obj) {
        if (obj instanceof PrimaryKey) {
            this.pkList.add(idx, obj);
            this.entityList.add(idx, null);
            setModified();
        } else {
            makeBackup();
            try {
                EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) obj;
                this.pkList.add(idx, ejbremote.getPrimaryKey());
                this.entityList.add(idx, ejbremote);
                setModified();
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Appends all of the elements in the specified collection to the end of this list,
     * in the order that they are returned by the specified collection's iterator
     *
     * @param coll collection whose elements are to be added to this list
     * @return  true if this list changed as a result of the call
     */
    public synchronized boolean addAll(java.util.Collection collection) {
        if (collection.size() < 1) return false;
        Iterator iter = collection.iterator();
        Object obj = iter.next();
        if (obj instanceof PrimaryKey) {
            this.pkList.addAll(collection);
            boolean retval = this.entityList.addAll(getListOfNulls(collection.size()));
            setModified();
            return retval;
        } else {
            makeBackup();
            try {
                this.entityList.addAll(collection);
                boolean retval = this.pkList.addAll(getPrimaryKeys(collection));
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Appends all of the elements in the specified collection to the end of this list,
     * in the order that they are returned by the specified collection's iterator
     *
     * @param coll collection whose elements are to be added to this list
     * @return  true if this list changed as a result of the call
     */
    public synchronized boolean addAll(int idx, java.util.Collection collection) {
        if (collection.size() < 1) return false;
        Iterator iter = collection.iterator();
        Object obj = iter.next();
        if (obj instanceof PrimaryKey) {
            this.pkList.addAll(idx, collection);
            boolean retval = this.entityList.addAll(idx, getListOfNulls(collection.size()));
            setModified();
            return retval;
        } else {
            makeBackup();
            try {
                this.entityList.addAll(idx, collection);
                boolean retval = this.pkList.addAll(idx, getPrimaryKeys(collection));
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Appends the specified element to the end of this list
     *
     * @param obj element to be appended to this list
     * @return  true
     */
    public synchronized boolean add(java.lang.Object obj) {
        if (obj instanceof PrimaryKey) {
            this.entityList.add(null);
            boolean retval = this.pkList.add(obj);
            setModified();
            return retval;
        } else {
            EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) obj;
            makeBackup();
            try {
                this.entityList.add(ejbremote);
                boolean retval = this.pkList.add(ejbremote.getPrimaryKey());
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    
    
    
    ////////////////////////////// setters ///////////////////////////////////////
    
    /**
     * Replaces all of the elements of this list with the elements in the specified collection
     *
     * @param coll  the collection of new elements
     */
    public synchronized void set(Collection collection) {
        if (collection.size() < 1) return;
        Iterator iter = collection.iterator();
        Object obj = iter.next();
        clear();
        if (obj instanceof PrimaryKey) {
            this.pkList.addAll(collection);
            this.entityList.addAll(getListOfNulls(collection.size()));
            setModified();
        } else {
            makeBackup();
            try {
                this.entityList.addAll(collection);
                this.pkList.addAll(getPrimaryKeys(collection));
                setModified();
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Replaces the element at the specified position in this list with the specified element
     *
     * @param idx index of the element to replace
     * @param obj element to be stored at the specified position
     * @return the element previously at the specified position
     */
    public synchronized java.lang.Object set(int idx, java.lang.Object obj) {
        makeBackup();
        if (obj instanceof PrimaryKey) {
            try {
                this.pkList.set(idx, obj);
                Object retval = this.entityList.set(idx, findEntity((PrimaryKey) obj));
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            } catch (FinderException fe) {
                restoreFromBackup();
                throw new CollectionException(fe);
            }
        } else {
            EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) obj;
            try {
                this.pkList.set(idx, ejbremote.getPrimaryKey());
                Object retval = this.entityList.set(idx, ejbremote);
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Update an existing element, based on the primary key of the supplied model object.
     *
     * @param element the model to use to update the collection's corrspeonding element
     */
    public synchronized void update(ModelI model) {
        if (model.isAnonymous())
            throw new CollectionException("Can't select an entity to update from an anonymous model");
        EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) get(model.getPK());
        makeBackup();
        try {
            ejbremote.setFromModel(model);
        } catch (RemoteException re) {
            restoreFromBackup();
            throw new CollectionException(re);
        }
    }
    
    
    
    /////////////////////////////// removal methods ////////////////////////////////
    
    /** removes all elements from this list
     */
    public synchronized void clear() {
        this.pkList.clear();
        this.entityList.clear();
        setModified();
    }
    
    /**
     * Removes the element at the specified position in this list
     *
     * @param idx the index of the element to removed
     * @return  the element previously at the specified position
     */
    public synchronized java.lang.Object remove(int idx) {
        this.pkList.remove(idx);
        Object retval = this.entityList.remove(idx);
        setModified();
        return retval;
    }
    
    /**
     * Retains only the elements in this list that are contained in the specified collection
     *
     * @param coll collection that defines which elements this set will retain
     * @return true if this list changed as a result of the call
     */
    public synchronized boolean retainAll(java.util.Collection collection) {
        if (collection.size() < 1) return false;
        Iterator iter = collection.iterator();
        Object obj = iter.next();
        makeBackup();
        if (obj instanceof PrimaryKey) {
            try {
                this.pkList.retainAll(collection);
                Collection entities = getEntities(collection);
                boolean retval = this.entityList.retainAll(entities);
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            } catch (FinderException fe) {
                restoreFromBackup();
                throw new CollectionException(fe);
            }
        } else {
            try {
                this.entityList.retainAll(collection);
                Collection pks = getPrimaryKeys(collection);
                boolean retval = this.pkList.retainAll(pks);
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Removes from this list all the elements that are contained in the specified collection
     *
     * @param coll collection that defines which elements will be removed from this list
     * @return true if this list changed as a result of the call
     */
    public synchronized boolean removeAll(java.util.Collection collection) {
        if (collection.size() < 1) return false;
        Iterator iter = collection.iterator();
        Object obj = iter.next();
        makeBackup();
        if (obj instanceof PrimaryKey) {
            try {
                this.pkList.removeAll(collection);
                Collection entities = getEntities(collection);
                boolean retval = this.entityList.removeAll(entities);
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            } catch (FinderException fe) {
                restoreFromBackup();
                throw new CollectionException(fe);
            }
        } else {
            try {
                this.entityList.removeAll(collection);
                Collection pks = getPrimaryKeys(collection);
                boolean retval = this.pkList.removeAll(pks);
                setModified();
                return retval;
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            }
        }
    }
    
    /**
     * Removes the element at the specified position in this list
     *
     * @param pk the primary key to search for
     * @return  the element identified the by supplied primary key
     */
    public synchronized Object removeByPK(PrimaryKey pk) {
        int idx = this.pkList.indexOf(pk);
        if (idx == -1) return null;
        makeBackup();
        this.pkList.remove(idx);
        EntityBeanRemoteI ejbremote = (EntityBeanRemoteI) this.entityList.remove(idx);
        if (ejbremote == null) {
            try {
                ejbremote = findEntity(pk);
            } catch (RemoteException re) {
                restoreFromBackup();
                throw new CollectionException(re);
            } catch (FinderException fe) {
                restoreFromBackup();
                throw new CollectionException(fe);
            }
        }
        setModified();
        return ejbremote;
    }
    
    /**
     * Removes the first occurrence in this list of the specified element
     *
     * @param obj element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public synchronized boolean remove(java.lang.Object obj) {
        if (obj instanceof PrimaryKey) {
            int idx = this.pkList.indexOf(obj);
            if (idx == -1) return false;
            this.entityList.remove(idx);
            boolean retval = this.pkList.remove(obj);
            setModified();
            return retval;
        } else {
            if (this.entityList.contains(null))
                ensureEntitiesAreLoaded();
            int idx = this.entityList.indexOf(obj);
            if (idx == -1) return false;
            this.pkList.remove(idx);
            boolean retval = this.entityList.remove(obj);
            setModified();
            return retval;
        }
    }
    
    
    
    
    //////////////////////// getters that return collections ///////////////////////////
    
    /**
     * Returns an iterator over the elements in this list in proper sequence
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    public java.util.Iterator iterator() {
        ensureEntitiesAreLoaded();
        return this.entityList.iterator();
    }
    
    /**
     * Returns a list iterator of the elements in this list (in proper sequence), starting at the specified position in this list
     *
     * @param idx index of first element to be returned from the list iterator (by a call to the next method).
     * @return a list iterator of the elements in this list (in proper sequence), starting at the specified position in this list
     */
    public java.util.ListIterator listIterator(int idx) {
        ensureEntitiesAreLoaded();
        return this.entityList.listIterator(idx);
    }
    
    /**
     * Returns a list iterator of the elements in this list (in proper sequence).
     *
     * @return a list iterator of the elements in this list (in proper sequence).
     */
    public java.util.ListIterator listIterator() {
        ensureEntitiesAreLoaded();
        return this.entityList.listIterator();
    }
    
    /**
     * Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive
     *
     * @param idx1 low endpoint (inclusive) of the subList
     * @param idx2 high endpoint (exclusive) of the subList.
     *
     * @return  a view of the specified range within this list
     */
    public java.util.List subList(int idx1, int idx2) {
        ensureEntitiesAreLoaded();
        return this.entityList.subList(idx1, idx2);
    }
    
    /**
     * Returns an array containing all of the elements in this list in proper sequence; the runtime type of the returned array is that of the specified array
     *
     * @param objs the array into which the elements of this list are to be stored, if it is big enough; otherwise, a new array of the same runtime type is allocated for this purpose
     *
     * @return  an array containing the elements of this list
     */
    public java.lang.Object[] toArray(java.lang.Object[] objs) {
        ensureEntitiesAreLoaded();
        return this.entityList.toArray(objs);
    }
    
    /**
     * Returns an array containing all of the elements in this list in proper sequence
     *
     * @return an array containing all of the elements in this list in proper sequence
     */
    public java.lang.Object[] toArray() {
        ensureEntitiesAreLoaded();
        return this.entityList.toArray();
    }
    
    /**
     * For EJB 1.0 compatibility
     *
     * @return
     */
    public Enumeration enumeration() {
        if (this.entityList.contains(null))
            ensureEntitiesAreLoaded();
        return new Vector(this.entityList).elements();
    }
    
    
    
    /////////////////////////////// equality test ////////////////////////////
    
    /**
     * Compares the specified object with this list for equality
     * two lists are defined to be equal if they contain the same elements in the same order
     *
     * @param obj the object to be compared for equality with this list
     *
     * @return  true if the specified object is equal to this list
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoteObjectList))
            return false;
        RemoteObjectList other = (RemoteObjectList) obj;
        if (size() != other.size())
            return false;
        Iterator myIter = this.iterator();
        Iterator otherIter = other.iterator();
        while (myIter.hasNext()) {
            //
            // equality test for remote objects is defined as
            // javax.ejb.EJBObject.isIdentical(javax.ejb.EJBObject)
            //
            EntityBeanRemoteI rem1 = (EntityBeanRemoteI) myIter.next();
            EntityBeanRemoteI rem2 = (EntityBeanRemoteI) otherIter.next();
            try {
                if (!rem1.isIdentical(rem2))
                    return false;
            } catch (RemoteException re) {
                throw new CollectionException(re);
            }
        }
        return true;
    }
    
}
