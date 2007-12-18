package com.freshdirect.dlvadmin;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.util.exception.ExceptionDescription;

public class ExceptionPage extends org.apache.tapestry.pages.Exception {
	
	private ExceptionDescription currentException;

	public ExceptionDescription getCurrentException() {
		return currentException;
	}

	public void setCurrentException(ExceptionDescription currentException) {
		this.currentException = currentException;
	}

	public void setExceptions(ExceptionDescription[] arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void pageDetached(PageEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
