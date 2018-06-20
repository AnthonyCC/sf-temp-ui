package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;


public class DlvPassRequest extends Message{

	private boolean dlvPassCart;

	public boolean isDlvPassCart() {
		return dlvPassCart;
	}

	public void setDlvPassCart(boolean dlvPassCart) {
		this.dlvPassCart = dlvPassCart;
	} 
	
	
}
