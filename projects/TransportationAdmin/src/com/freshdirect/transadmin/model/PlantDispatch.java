package com.freshdirect.transadmin.model;

import com.freshdirect.framework.util.TimeOfDay;

public class PlantDispatch implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3061490966372677865L;
	private TimeOfDay dispatchTime;
	private TimeOfDay plantDispatch;

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
