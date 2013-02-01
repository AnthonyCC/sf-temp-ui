package com.freshdirect.transadmin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EventLogMessageGroup;
import com.freshdirect.transadmin.model.EventLogRouteModel;
import com.freshdirect.transadmin.model.EventLogSubType;
import com.freshdirect.transadmin.model.EventLogType;
import com.freshdirect.transadmin.model.EventModel;
import com.freshdirect.transadmin.model.MotEventModel;
import com.freshdirect.transadmin.model.MotEventType;
import com.freshdirect.transadmin.security.AuthUser;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public interface IEventLogManagerService {
	
	List<EventModel> lookUpEvents(Date eventDate) throws TransAdminServiceException;
	
	List<EventModel> lookUpEventForDateRange(Date eventDate) throws TransAdminServiceException;
	
	void logEvent(EventModel event) throws TransAdminServiceException;
	
	EventModel lookUpEventById(String eventID) throws TransAdminServiceException;
	
	List<EventLogType> lookUpEventTypes(String eventType) throws TransAdminServiceException;
	
	void deleteEventLog(String[] eventLogId) throws TransAdminServiceException;
	
	Map<String, List<String>> lookUpRouteWindows(Date deliveryDate) throws TransAdminServiceException;
	
	Map<String, EventLogRouteModel> lookUpRoutes(Date deliveryDate) throws TransAdminServiceException;
	
	void updateEventLog(EventModel event) throws TransAdminServiceException;
	
	List<EventLogMessageGroup> lookUpEventMessageGroup(String messageGroup) throws TransAdminServiceException;
	
	void logEventLogInfo(List<EventLogType> eventType, List<EventLogSubType> subType, List<EventLogMessageGroup> msgGroup) throws TransAdminServiceException;
	
	List<MotEventType> lookUpMotEventTypes(final String eventType) throws TransAdminServiceException;
	
	List<MotEventModel> lookUpMotEvents(Date eventDate) throws TransAdminServiceException;
	
	List<MotEventModel> lookUpMotEventForDateRange(Date eventDate) throws TransAdminServiceException;
	
	void logMotEvent(MotEventModel event) throws TransAdminServiceException;
	
	MotEventModel lookUpMotEventById(String eventID) throws TransAdminServiceException;
	
	void updateMotEventLog(MotEventModel event) throws TransAdminServiceException;
	
	void updateEventStatus(Set<MotEventModel> events) throws TransAdminServiceException;
	
	void logMotEventTypeInfo(List<MotEventType> eventType) throws TransAdminServiceException;
	
	List<EventLogMessageGroup> lookUpMotEventMessageGroup(String messageGroup) throws TransAdminServiceException;
	
	void logMotEventLogInfo(List<MotEventType> eventType, List<EventLogMessageGroup> msgGroup) throws TransAdminServiceException;

	Map<String, AuthUser> getAuthUserPrivileges() throws TransAdminServiceException;
	
}
