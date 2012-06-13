package com.freshdirect.fdstore.rules;

import java.io.Serializable;

public class Premium implements Serializable {
	
	private double value;
	
	public Premium() {
		
	}
	
	public Premium(double value) {
		this();
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return  "Premium[" + value + "]";
	}
}
