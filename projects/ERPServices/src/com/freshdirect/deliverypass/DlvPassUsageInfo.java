package com.freshdirect.deliverypass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.customer.ErpActivityRecord;

public class DlvPassUsageInfo implements Serializable{
	/** Sorts orders by dlv. start time, descending */
	private final static Comparator DLV_PASS_USAGE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((DlvPassUsageLine) o2).getDeliveryDate().compareTo(((DlvPassUsageLine) o1).getDeliveryDate());
		}
	};

	private List usageLines =  new ArrayList();
	
	public DlvPassUsageInfo() {
		super();
	}

	public List getUsageLines() {
		return usageLines;
	}

	public void addUsageLine(DlvPassUsageLine usageLine) {
		this.usageLines.add(usageLine);
		Collections.sort(usageLines, DLV_PASS_USAGE_COMPARATOR);
	}
	
	 	
}
