package com.freshdirect.customer;

import java.io.Serializable;

public interface ErpReturnLineI extends Serializable {

	public double getQuantity();

	public boolean isRestockingOnly();

}