/**
 * @author ekracoff
 * Created on May 3, 2005*/

package com.freshdirect.ocf.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.HashMessageFormat;
import com.freshdirect.ocf.core.OcfTableI.Column;
import com.freshdirect.ocf.core.OcfTableI.Row;

public class ProfileAction extends Action implements ActionI {
	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * New version, allows the value to be a HashMessageFormat template.  The previous version has
	 * been renamed _OLD and placed below, to allow quick reversion if needed.
	 */
	public void execute(OcfTableI customers) {
		Column cols[]  = customers.getColumns();
		String names[] = new String[cols.length];
		int custRow    = customers.getColumnPosByName("CUSTOMER_ID");
		
		HashMessageFormat hmf = new HashMessageFormat(value);
		
		for(int i=0;i<cols.length;i++) {
			names[i] = cols[i].getName();
		}
		
		try {
			for(Iterator i = customers.getRows().iterator(); i.hasNext();) {
				Row row           = (Row)i.next();
				String erpId      = (String) row.getValues()[custRow]; 
				Map templateData  = new HashMap();
				Object vals[]     = row.getValues();

				for(int j=0;j<cols.length;j++) {
					templateData.put(names[j],vals[j]);
				}

				String fdId         = FDCustomerFactory.getFDCustomerFromErpId(erpId).getPK().getId();
				FDIdentity identity = new FDIdentity(erpId, fdId);
				FDCustomerManager.setProfileAttribute(identity, key, hmf.format(templateData));
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public void execute_OLD(OcfTableI customers) {
		try {
			List custs = customers.getValuesByColumn("CUSTOMER_ID");
			for (Iterator i = custs.iterator(); i.hasNext();) {
				String erpId = (String) i.next();
				String fdId = FDCustomerFactory.getFDCustomerFromErpId(erpId).getPK().getId();

				FDIdentity identity = new FDIdentity(erpId, fdId);
				FDCustomerManager.setProfileAttribute(identity, key, value);
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}
	public String toString() {
		return "Profile action: set '" + key + "' to '" + value + "'";
	}
}