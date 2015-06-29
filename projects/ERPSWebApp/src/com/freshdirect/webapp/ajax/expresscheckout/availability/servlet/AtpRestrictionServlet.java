package com.freshdirect.webapp.ajax.expresscheckout.availability.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;

public class AtpRestrictionServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8187391559285687167L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			boolean isAvailability = AvailabilityService.defaultService().checkCartAtpAvailability(user);
			writeResponseData(response, isAvailability);
		} catch (FDResourceException e) {
			BaseJsonServlet.returnHttpError(500, "Failed to check ATP restrictions for user.");
		}
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
