package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * 
 * @author Rob
 *
 */
public class Timezone extends Message {

    private String timezone;
    
    private boolean excludeaddr;

    public boolean getExcludeaddr() {
		return excludeaddr;
	}

	public void setExcludeaddr(boolean excludeaddr) {
		this.excludeaddr = excludeaddr;
	}

	public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
