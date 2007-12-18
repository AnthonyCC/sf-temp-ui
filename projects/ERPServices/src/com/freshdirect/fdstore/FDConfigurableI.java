package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Map;

public interface FDConfigurableI extends Serializable {

	public double getQuantity();

	public String getSalesUnit();

	/** @return Map of String (ERP characteristic) -> String (ERP char. value) */
	public Map getOptions();

}