package com.freshdirect.webapp.listeners;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUser;

public interface FDEventProcessorI {

	void process(FDUser user, HttpSession session);
		
}
