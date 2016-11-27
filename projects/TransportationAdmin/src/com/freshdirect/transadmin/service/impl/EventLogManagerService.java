package com.freshdirect.transadmin.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.dao.IEventLogDAO;
import com.freshdirect.transadmin.model.EventLogMessageGroup;
import com.freshdirect.transadmin.model.EventLogRouteModel;
import com.freshdirect.transadmin.model.EventLogSubType;
import com.freshdirect.transadmin.model.EventLogType;
import com.freshdirect.transadmin.model.EventModel;
import com.freshdirect.transadmin.model.MotEventModel;
import com.freshdirect.transadmin.model.MotEventType;
import com.freshdirect.transadmin.security.AuthUser;
import com.freshdirect.transadmin.service.IEventLogManagerService;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public class EventLogManagerService implements IEventLogManagerService {
	
	private IEventLogDAO eventLogDAO;

	public IEventLogDAO getEventLogDAO() {
		return eventLogDAO;
	}

	public void setEventLogDAO(IEventLogDAO eventLogDAO) {
		this.eventLogDAO = eventLogDAO;
	}
	
	public List<EventModel> lookUpEvents(Date eventDate) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpEvents(eventDate);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public EventModel lookUpEventById(String eventID) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpEventById(eventID);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public List<EventModel> lookUpEventForDateRange(Date eventDate) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpEventForDateRange(eventDate);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}

	public void logEvent(EventModel event) throws TransAdminServiceException {
		try{
			getEventLogDAO().logEvent(event);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public List<EventLogType> lookUpEventTypes(String eventType) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpEventTypes(eventType);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
		
	}
	
	public void deleteEventLog(String[] eventLogId) throws TransAdminServiceException {
		try{
			getEventLogDAO().deleteEventLog(eventLogId);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public Map<String, List<String>> lookUpRouteWindows(Date deliveryDate) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpRouteWindows(deliveryDate);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public Map<String, EventLogRouteModel> lookUpRoutes(Date deliveryDate) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpRoutes(deliveryDate);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public void updateEventLog(EventModel event) throws TransAdminServiceException {
		try {
			getEventLogDAO().updateEventLog(event);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public List<EventLogMessageGroup> lookUpEventMessageGroup(String messageGroup) throws TransAdminServiceException {
		try {
			return getEventLogDAO().lookUpEventMessageGroup(messageGroup);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public void logEventLogInfo(List<EventLogType> eventType, List<EventLogSubType> subType, List<EventLogMessageGroup> msgGroup) throws TransAdminServiceException {
		try {			
			getEventLogDAO().clearEventLogType();
			getEventLogDAO().clearEventLogMessageGroup();
			getEventLogDAO().logEventTypeInfo(eventType);
			getEventLogDAO().logEventMessageGroupInfo(msgGroup);
			getEventLogDAO().logEventSubTypeInfo(subType);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	
	public List<MotEventType> lookUpMotEventTypes(String eventType) throws TransAdminServiceException {
		try {			
			return getEventLogDAO().lookUpMotEventTypes(eventType);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public List<MotEventModel> lookUpMotEvents(Date eventDate) throws TransAdminServiceException {
		try {			
			return getEventLogDAO().lookUpMotEvents(eventDate);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public List<MotEventModel> lookUpMotEventForDateRange(Date eventDate) throws TransAdminServiceException {
		try {			
			return getEventLogDAO().lookUpMotEventForDateRange(eventDate);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public MotEventModel lookUpMotEventById(String eventID) throws TransAdminServiceException {
		try{
			return getEventLogDAO().lookUpMotEventById(eventID);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public void logMotEvent(MotEventModel event) throws TransAdminServiceException {
		try {			
			getEventLogDAO().logMotEvent(event);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}	 
	
	public void updateMotEventLog(MotEventModel event) throws TransAdminServiceException {
		try {			
			getEventLogDAO().updateMotEventLog(event);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public void updateEventStatus(Set<MotEventModel> events) throws TransAdminServiceException {
		try {			
			getEventLogDAO().updateEventStatus(events);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public void logMotEventTypeInfo(List<MotEventType> eventType) throws TransAdminServiceException {
		try {	
			getEventLogDAO().clearMotEventLogType();
			getEventLogDAO().logMotEventTypeInfo(eventType);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public List<EventLogMessageGroup> lookUpMotEventMessageGroup(String messageGroup) throws TransAdminServiceException {
		try {
			return getEventLogDAO().lookUpMotEventMessageGroup(messageGroup);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public void logMotEventLogInfo(List<MotEventType> eventType, List<EventLogMessageGroup> msgGroup) throws TransAdminServiceException {
		try {			
			getEventLogDAO().clearMotEventLogMessageGroup();
			getEventLogDAO().clearMotEventLogType();
			getEventLogDAO().logMotEventMessageGroupInfo(msgGroup);
			getEventLogDAO().logMotEventTypeInfo(eventType);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
	
	public Map<String, AuthUser> getAuthUserPrivileges() throws TransAdminServiceException {
		try {
			return getEventLogDAO().getAuthUserPrivileges();
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_EVENTLOG_ERROR);
		}
	}
}
