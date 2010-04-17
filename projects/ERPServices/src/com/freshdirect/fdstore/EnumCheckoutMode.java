package com.freshdirect.fdstore;

public enum EnumCheckoutMode {
	NORMAL,			// standard checkout process
	CREATE_SO,		// create standing order (new SO + CL, new order)
	MODIFY_SO		// modify standing order (update SO + CL, no new order)
}
