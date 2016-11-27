package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
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

public interface IEventLogDAO {
	
	List<EventModel> lookUpEvents(Date eventDate) throws SQLException;
	
	List<EventModel> lookUpEventForDateRange(Date eventDate) throws SQLException;
	
	EventModel lookUpEventById(String eventID) throws SQLException;
	
	void logEvent(EventModel event) throws SQLException;
	
	List<EventLogType> lookUpEventTypes(String eventType) throws SQLException;
	
	void deleteEventLog(String[] eventLogId) throws SQLException;
	
	Map<String, List<String>> lookUpRouteWindows(Date deliveryDate) throws SQLException;
	
	Map<String, EventLogRouteModel> lookUpRoutes(Date deliveryDate) throws SQLException;
	
	void updateEventLog(EventModel event) throws SQLException;
	
	List<EventLogMessageGroup> lookUpEventMessageGroup(final String messageGroup) throws SQLException;
	
	void clearEventLogType() throws SQLException;
	
	void clearEventLogMessageGroup() throws SQLException;
	
	void logEventTypeInfo(List<EventLogType> eventType) throws SQLException;
	
	void logEventSubTypeInfo(List<EventLogSubType> eventType) throws SQLException;
	
	void logEventMessageGroupInfo(List<EventLogMessageGroup> eventType) throws SQLException;
	
	List<MotEventType> lookUpMotEventTypes(String eventType) throws SQLException;
	
	List<MotEventModel> lookUpMotEvents(Date eventDate) throws SQLException;
	
	List<MotEventModel> lookUpMotEventForDateRange(Date eventDate) throws SQLException;
	
	MotEventModel lookUpMotEventById(String eventID) throws SQLException;
	
	void logMotEvent(MotEventModel event) throws SQLException;
	
	void updateMotEventLog(MotEventModel event) throws SQLException;
	
	void updateEventStatus(Set<MotEventModel> events) throws SQLException;
	
	void clearMotEventLogType() throws SQLException;
	
	void logMotEventTypeInfo(List<MotEventType> eventType) throws SQLException;
	
	List<EventLogMessageGroup> lookUpMotEventMessageGroup(final String messageGroup) throws SQLException;
	
	void logMotEventMessageGroupInfo(List<EventLogMessageGroup> messageGroup) throws SQLException;
	
	void clearMotEventLogMessageGroup() throws SQLException;

	Map<String, AuthUser> getAuthUserPrivileges() throws SQLException;
			
}
