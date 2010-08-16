package com.freshdirect.transadmin.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.freshdirect.framework.util.ExpiringReference;

public abstract class CustomExpiringReference<X> extends ExpiringReference {
	
	private String fileStore = null;
	
	protected static final String STORE_EMPLOYEEDATA = "TRANSAPP_STORE_EMPLOYEEDATA.ser";
	
	protected static final String STORE_TERMINATEDEMPLOYEEDATA = "TRANSAPP_STORE_TERMINATED_EMPLOYEEDATA.ser";
	
	protected static final String STORE_ACTINACTEMPLOYEEDATA = "TRANSAPP_STORE_ACTINACT_EMPLOYEEDATA.ser";
	
	
	//protected final long storeRefreshPeriod = 60 * 60 * 1000;

	protected long storeLastRefresh = 0;
	
	public CustomExpiringReference(long refreshPeriod, String fileStore) {
		super(refreshPeriod);
		this.fileStore = fileStore;
	}
	
	protected void synchronizeStore(Object data) {
		if (System.currentTimeMillis() - storeLastRefresh > this.refreshPeriod) {
			writeToStore(data);
			storeLastRefresh = System.currentTimeMillis();
		}
	}
	
	public synchronized Object get() {
		Object o=super.get();
		synchronizeStore(o);
		return o;
	}
	
	public synchronized Object getEx() {
		if(this.referent == null) {
			return readFromStore();
		}
		
		return this.referent;		
	}
	
	protected void writeToStore(Object data) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fileStore));
			oos.writeObject(data);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected Object readFromStore() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					fileStore));
			return ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
