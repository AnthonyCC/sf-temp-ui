package com.freshdirect.transadmin.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.transadmin.exception.TransAdminCacheException;

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
	
	protected static final String STORE_TRUCKINFODATA = "TRANSAPP_STORE_TRUCKINFODATA.ser";
	
	protected static final String STORE_ROUTEINFODATA = "TRANSAPP_STORE_ROUTEINFODATA.ser";
	
	protected static final String STORE_EMPLOYEEDATA = "TRANSAPP_STORE_EMPLOYEEDATA.ser";
	
	protected static final String STORE_TERMINATEDEMPLOYEEDATA = "TRANSAPP_STORE_TERMINATED_EMPLOYEEDATA.ser";
	
	protected static final String STORE_ACTINACTEMPLOYEEDATA = "TRANSAPP_STORE_ACTINACT_EMPLOYEEDATA.ser";
		
	/**
	 * @param refreshPeriod in milliseconds
	 */
	public TrnAdmExpiringReference(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}
	
	public TrnAdmExpiringReference(long refreshPeriod, String fileStore) {
		this.refreshPeriod = refreshPeriod;
		this.fileStore = fileStore;
	}

	@SuppressWarnings("unchecked")
	public synchronized Object get(Object key) {

		Long refreshObj = (Long) referentMap.get(key);
		
		boolean needReload = false;
		if (refreshObj != null) {
			lastRefresh = refreshObj.longValue();
			if (System.currentTimeMillis() - lastRefresh > this.refreshPeriod) {
				needReload = true;						
			}
		} else {
			needReload = true;						
		}
		
		if(needReload) {
			try {
				Object newValue = this.load(key);
				referentHolder.put(key, newValue);
				lastRefresh = System.currentTimeMillis();
				referentMap.put(key, new Long(lastRefresh));
				writeToStore();
			} catch(TransAdminCacheException cacheExp) {				
				// Set the referent to empty to read from store	
				referentHolder = new HashMap();
			}
		}
		
		if(referentHolder.keySet() == null || referentHolder.keySet().size() == 0) {
			referentHolder = (HashMap)this.readFromStore();
		}
		
		return referentHolder.get(key);
	}

	/**
	 * Template method that gets invoked whenever the reference has to be loaded
	 */
	protected abstract Object load(Object requestParam) throws TransAdminCacheException;

	public synchronized void forceRefresh() {
		referentHolder = new HashMap();
		referentMap = new HashMap();
	}	
		
	protected void writeToStore() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileStore));
			oos.writeObject(referentHolder);
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
	
	public long getLastRefresh() {
		return System.currentTimeMillis() - lastRefresh;
	}
}
