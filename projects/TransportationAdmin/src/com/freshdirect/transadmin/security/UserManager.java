package com.freshdirect.transadmin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.security.MenuBuilder.MenuBuilderException;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.IEventLogManagerService;

public class UserManager {
		
	private static UserManager instance = null;
	
	public UserManager() {
		
	}
	
	public static UserManager getInstance() {
	      if(instance == null) {
	         instance = new UserManager();
	      }
	      return instance;
	}
	
	private IEventLogManagerService eventLogService;
	
	public IEventLogManagerService getEventLogService() {
		return eventLogService;
	}

	public void setEventLogService(IEventLogManagerService eventLogService) {
		this.eventLogService = eventLogService;
	}
}
