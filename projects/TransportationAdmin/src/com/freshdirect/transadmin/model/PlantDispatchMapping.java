package com.freshdirect.transadmin.model;

import com.freshdirect.framework.util.TimeOfDay;

public class PlantDispatchMapping implements java.io.Serializable, TrnBaseEntityI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6824430022903168463L;
	
	private TimeOfDay dispatchTime;
	private TimeOfDay plantDispatch;
	
	public boolean isObsoleteEntity() {
		return false;
	}

	public TimeOfDay getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(TimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public TimeOfDay getPlantDispatch() {
		return plantDispatch;
	}

	public void setPlantDispatch(TimeOfDay plantDispatch) {
		this.plantDispatch = plantDispatch;
	}

}
