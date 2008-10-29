package com.freshdirect.transadmin.datamanager.util;

import java.util.Comparator;
import java.util.Map;

import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnRouteNumber;

public class CutOffComparator implements Comparator {
	
	private Map referenceData = null;
	
	public CutOffComparator(Map referenceData) {
		this.referenceData = referenceData;
	}
	
	public int compare(Object obj1, Object obj2){

//		parameter are of type Object, so we have to downcast it to Employee objects

		String cutOffId1 = ( (TrnRouteNumber) obj1).getRouteNumberId().getCutOffId();

		String cutOffId2 = ( (TrnRouteNumber) obj2).getRouteNumberId().getCutOffId();
		
		TrnCutOff cutOff1 = (TrnCutOff)referenceData.get(cutOffId1);
		TrnCutOff cutOff2 = (TrnCutOff)referenceData.get(cutOffId2);
		
		if(cutOff1 == null || cutOff2 == null) {
			return 0;
		}
		
		if(cutOff1 != null || cutOff2 == null) {
			return -1;
		}
		
		if(cutOff1 == null || cutOff2 != null) {
			return 1;
		}

		if( cutOff1.getSequenceNo().doubleValue() > cutOff2.getSequenceNo().doubleValue() ) {
			return 1;
		} else if( cutOff1.getSequenceNo().doubleValue() < cutOff2.getSequenceNo().doubleValue() ) {
			return -1;
		} else {
			return 0;
		}
	}

	public Map getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(Map referenceData) {
		this.referenceData = referenceData;
	}
}
