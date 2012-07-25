package com.freshdirect.fdstore.standingorders;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.customer.FDCartLineI;


public class ProcessActionResult implements Serializable {
	private static final long serialVersionUID = -8086313206892349807L;

	public static enum Reason {
		INVALID_CONFIG, /* a line item has invalid configuration */
		ATP, /* ATP check failed on an item */
		UNAV, /* item is unavailable */
		GENERAL /* general issue raised */
	};
	
	private boolean generalIssue = false;
	private Map<FDCartLineI, Reason> unavItemsMap = new HashMap<FDCartLineI, Reason>();
	private Map<FDCartLineI, String> messages = new HashMap<FDCartLineI, String>();
	

	public void addUnavailableItem(FDCartLineI item, Reason reason, String message) {
		unavItemsMap.put(item, reason);
		if (message != null) {
			messages.put(item, message);
		}
	}
	
	public Set<FDCartLineI> getUnavailableItems() {
		return unavItemsMap.keySet();
	}
	
	public Reason getReason(FDCartLineI item) {
		return unavItemsMap.get(item);
	}
	
	public String getMessage(FDCartLineI item) {
		return messages.get(item);
	}

	
	/**
	 * Returns true if an issue raised during item list process without
	 * knowing what was broken
	 *  
	 * @return
	 */
	public boolean isGeneralIssue() {
		return generalIssue;
	}
	
	public void markGeneralIssue() {
		this.generalIssue = true;
	}

	public boolean isFail() {
		return generalIssue || unavItemsMap.size() > 0;
	}

	@Override
	public String toString() {
		return "Bad items: " + unavItemsMap.size() + " -> " + isFail();
	}
}
