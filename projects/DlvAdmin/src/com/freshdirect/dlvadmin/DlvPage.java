package com.freshdirect.dlvadmin;

import java.util.Date;

import javax.servlet.ServletRequest;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

public class DlvPage extends BasePage implements IExternalPage, PageValidateListener {

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
	}

	private ServletRequest getRequest() {
		return this.getRequestCycle().getRequestContext().getRequest();
	}
	
	public boolean isUserAdmin() {
		return PageRegistry.isUserAdmin(getRequest());
	}
	
	public boolean isUserGuest() {
		return PageRegistry.isUserGuest(getRequest());
	}
	
	public boolean isUserMarketing() {
		return PageRegistry.isUserMarketing(getRequest());
	}

	public String getUserRole(){
		return PageRegistry.getUserRole(getRequest());
    }

	public void pageValidate(PageEvent event) {
		String role = this.getUserRole();
		if (PageRegistry.isAccessable(this.getPageName(), role)) {
			return;		
		}
		//send to which page? login.html?
		throw new PageRedirectException("ShowCurrentTimeslots");
	}
	
	public void detach() {
		this.currentDate = null;
		((DlvAdminEngine) this.getEngine()).cleanCache();
		super.detach();
	}
	
	private Date currentDate;

	/**
	 * Get the current time (generated once per request).
	 * 
	 * @return current time
	 */
	public Date getCurrentDate() {
		if (currentDate == null) {
			currentDate = new Date();
		}
		return currentDate;
	}

}
