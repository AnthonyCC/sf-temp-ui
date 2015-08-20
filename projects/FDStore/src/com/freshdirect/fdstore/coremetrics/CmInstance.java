package com.freshdirect.fdstore.coremetrics;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.EnumEStoreId;

/**
 * CoreMetrics instance identifiers
 * 
 * @author segabor
 *
 */
public enum CmInstance {
	UNKNOWN,
	/** FD Storefront */
	FDW(EnumEStoreId.FD, CmFacade.WEB),
	/** FD Phone Apps */
	FDP(EnumEStoreId.FD, CmFacade.PHONE),
	/** FD Tablet App */
	FDT(EnumEStoreId.FD, CmFacade.TABLET),
	/** FDX Storefront */
	SDSW(EnumEStoreId.FDX, CmFacade.WEB),
	/** FDX Phone App */
	SDSP(EnumEStoreId.FDX, CmFacade.PHONE),
	/** FDX Tablet App */
	SDST(EnumEStoreId.FDX, CmFacade.TABLET),
	/** Global Context : no storeno facade */
	GLOBAL(null, null)
	;

	EnumEStoreId eStoreId;
	CmFacade facade;

	CmInstance() {
		this.eStoreId = null;
		this.facade = null;
	}

	CmInstance(EnumEStoreId eStoreId, CmFacade facade) {
		this.eStoreId = eStoreId;
		this.facade = facade;
	}


	public EnumEStoreId getEStoreId() {
		return eStoreId;
	}

	/**
	 * Get up-front type
	 * @see CmFacade 
	 * @return
	 */
	public CmFacade getFacade() {
		return facade;
	}


	private static final Map<String,CmInstance> client2inst = new HashMap<String,CmInstance>(6);
	private static final Map<String,CmInstance> client2inst_tst = new HashMap<String,CmInstance>(6);
	static {
		// production client IDs
		client2inst.put("90391309", FDW);
		client2inst.put("51640003", FDP);
		client2inst.put("51640002", FDT);
		client2inst.put("51640006", SDSW);
		client2inst.put("51640004", SDSP);
		client2inst.put("51640005", SDST);

		// test client IDs
		client2inst_tst.put("60391309", FDW);
		client2inst_tst.put("81640004", FDP);
		client2inst_tst.put("81640005", FDT);
		client2inst_tst.put("81640006", SDSW);
		client2inst_tst.put("81640002", SDSP);
		client2inst_tst.put("81640007", SDST);
	}


	/**
	 * Determine CoreMetrics instance by client ID
	 * 
	 * @param clientId CoreMetrics client ID
	 * @return
	 */
	public static CmInstance lookupByClientId(final String clientId) {
		return client2inst.get(clientId);
	}

	/**
	 * Determine CoreMetrics instance by test client ID
	 * 
	 * @param clientId CoreMetrics test client ID
	 * @return
	 */
	public static CmInstance lookupByTestClientId(final String clientId) {
		return client2inst_tst.get(clientId);
	}

	/**
	 * Return corresponding CM client ID
	 * @param test look for test account
	 */
	public String getClientId(final boolean test) {
		if (test) {
			for (Map.Entry<String, CmInstance> e : client2inst_tst.entrySet()) {
				if (this.equals(e.getValue())) {
					return e.getKey();
				}
			}
		} else {
			for (Map.Entry<String, CmInstance> e : client2inst.entrySet()) {
				if (this.equals(e.getValue())) {
					return e.getKey();
				}
			}
		}
		return null;
	}
}
