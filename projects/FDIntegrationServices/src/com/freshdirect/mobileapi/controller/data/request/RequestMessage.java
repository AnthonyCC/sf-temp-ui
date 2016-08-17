package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * 
 * @author 
 *
 */
public class RequestMessage extends Message {

    private String source;

    
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
        
}
