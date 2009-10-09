/**
 * 
 */
package com.freshdirect.framework.util;

import java.io.Serializable;

public class Latch implements Serializable {
	private static final long serialVersionUID = -5565193126379596331L;

	boolean value;

	public Latch(boolean value) {
		super();
		this.value = value;
	}
	
	public void set() {
		value = true;
	}
	
	public void reset() {
		value = false;
	}
	
	public boolean isSet() {
		return value;
	}
	
	public boolean isReset() {
		return !value;
	}
}