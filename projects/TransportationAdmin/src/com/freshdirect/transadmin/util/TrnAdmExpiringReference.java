package com.freshdirect.transadmin.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for caching a single object, with timed expiration and appropriate
 * synchronization.
 */
public abstract class TrnAdmExpiringReference {

	private final long refreshPeriod;
	private long lastRefresh = 0;
	private Map referentHolder = new HashMap();
	private Map referentMap = new HashMap();

	private String fileStore = null;
	
	protected static final String STORE_EMPLOYEEPUNCHINFODATA = "TRANSAPP_STORE_EMPLOYEE_PUNCHINFODATA.ser";
	
	protected static final String STORE_ROUTEINFODATA = "TRANSAPP_STORE_ROUTEINFODATA.ser";
		
	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 */
	public TrnAdmExpiringReference(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}
	
	public TrnAdmExpiringReference(long refreshPeriod, String fileStore) {
		this.refreshPeriod = refreshPeriod;
		this.fileStore = fileStore;
	}

	public synchronized Object get(Object key) {

		Set keySet = referentMap.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Object time = (Object) iterator.next();
			Long lastRefreshTime = (Long) referentMap.get(time);

			if (System.currentTimeMillis() - lastRefreshTime.longValue() > this.refreshPeriod) {
				// System.out.println("removing the cache content :"+time);
				iterator.remove();
				referentHolder.remove(time);
			}

		}

		Long refreshObj = (Long) referentMap.get(key);
		if (refreshObj != null) {
			lastRefresh = refreshObj.longValue();
			if (System.currentTimeMillis() - lastRefresh > this.refreshPeriod) {
				Object value = this.load(key);				
				referentHolder.put(key, value);
				lastRefresh = System.currentTimeMillis();
				referentMap.put(key, new Long(lastRefresh));
				return value;
			} else {
				return referentHolder.get(key);
			}
		} else {
			Object value = this.load(key);			
			referentHolder.put(key, value);
			lastRefresh = System.currentTimeMillis();
			referentMap.put(key, new Long(lastRefresh));
			return value;
		}
	}

	/**
	 * Template method that gets invoked whenever the reference has to be loaded
	 */
	protected abstract Object load(Object requestParam);

	public synchronized void forceRefresh() {
		referentHolder = new HashMap();
		referentMap = new HashMap();
	}
	
	public synchronized Object getEx(Object requestParam) {
		if(this.referentHolder == null || (this.referentHolder != null && this.referentHolder.get(requestParam) == null)) {
			return readFromStore();
		}
		return this.referentHolder != null ? this.referentHolder.get(requestParam) : Collections.EMPTY_LIST;		
	}
	
	protected void writeToStore(Object data) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileStore));
			oos.writeObject(data);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	protected Object readFromStore() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileStore));
			return ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
