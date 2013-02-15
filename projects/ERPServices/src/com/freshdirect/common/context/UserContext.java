package com.freshdirect.common.context;

import java.io.Serializable;

public class UserContext implements Serializable {
	
	public UserContext(boolean alcoholRestricted) {
		super();
		this.alcoholRestricted = alcoholRestricted;
	}

	private boolean alcoholRestricted;

	public boolean isAlcoholRestricted() {
		return alcoholRestricted;
	}

	public void setAlcoholRestricted(boolean alcoholRestricted) {
		this.alcoholRestricted = alcoholRestricted;
	}
	
	
}
