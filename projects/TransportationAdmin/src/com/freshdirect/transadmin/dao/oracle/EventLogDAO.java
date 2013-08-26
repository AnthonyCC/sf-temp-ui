package com.freshdirect.transadmin.dao.oracle;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.transadmin.dao.IEventLogDAO;
import com.freshdirect.transadmin.model.EventLogMessageGroup;
import com.freshdirect.transadmin.model.EventLogRouteModel;
import com.freshdirect.transadmin.model.EventLogSubType;
import com.freshdirect.transadmin.model.EventLogType;
import com.freshdirect.transadmin.model.EventModel;
import com.freshdirect.transadmin.model.MotEventModel;
import com.freshdirect.transadmin.model.MotEventType;
import com.freshdirect.transadmin.security.AuthUser;
import com.freshdirect.transadmin.security.AuthUser.Privilege;
import com.freshdirect.transadmin.util.TransStringUtil;

public class EventLogDAO implements IEventLogDAO   {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
		
	private static final String GET_EVENTLOG = " SELECT el.*, els.*, et.eventtypename, est.eventsubtypename from transp.eventlogbook el, transp.eventstopnumberbreakdown els, transp.eventtype et, transp.eventsubtype est "+
													" where el.id = els.eventlogid(+) and el.eventsubtype = est.id(+) and el.eventtype = et.id(+) and el.eventdate = ?  ";
	
	private static final String GET_EVENTLOG_BY_ID = " SELECT el.*, els.*, et.eventtypename, est.eventsubtypename from transp.eventlogbook el, transp.eventstopnumberbreakdown els, transp.eventtype et, transp.eventsubtype est "+
													" where el.id = els.eventlogid(+) and el.eventsubtype = est.id(+) and el.eventtype = et.id(+) and el.id = ?  ";
	
	private static final String GET_EVENTLOG_DATERANGE_QRY = " SELECT el.*, els.*, et.eventtypename, est.eventsubtypename from transp.eventlogbook el, transp.eventstopnumberbreakdown els, transp.eventtype et, transp.eventsubtype est "+
													" where el.id = els.eventlogid(+) and el.eventsubtype = est.id(+) and el.eventtype = et.id(+) and el.eventdate >= ?  ";;
	
	private static final String GET_EVENTLOGNEXTSEQ_QRY = "SELECT TRANSP.EVENTLOGSEQ.nextval FROM DUAL";
	
	private static final String INSERT_EVENT_LOG = "INSERT INTO TRANSP.EVENTLOGBOOK(ID, EVENTDATE, ROUTENUMBER, TRUCKNUMBER, ROUTEWINDOWSTARTTIME, ROUTEWINDOWENDTIME, EVENTTYPE, EVENTSUBTYPE, EVENTDESCRIPTION, CROSSSTREETS, EMPLOYEENUMBER, SCANNERNUMBER, DATECREATED, CREATEDBY, REFERENCETICKETID )" +
															" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String INSERT_EVENTLOG_STOP = "INSERT INTO TRANSP.EVENTSTOPNUMBERBREAKDOWN (EVENTLOGID, STOPNUMBER) VALUES (?,?) ";
	
	private static final String CLEAR_EVENTLOG_EVENTS = "DELETE from transp.eventlogbook where id in ";
	
	private static final String CLEAR_EVENTLOG_STOP = "DELETE from transp.eventstopnumberbreakdown where eventlogid in ";
	
	private static final String UPDATE_EVENTLOG_QRY = "UPDATE TRANSP.EVENTLOGBOOK SET EVENTDATE=?, ROUTENUMBER=?, TRUCKNUMBER=?, ROUTEWINDOWSTARTTIME=?, ROUETWINDOWENDTIME=?, EVENTTYPE=?, EVENTSUBTYPE=?, EVENTDESCRIPTION=?, CROSSSTREETS=?, EMPLOYEENUMBER=?, SCANNERNUMBER=?, DATECREATED=?, CREATEDBY=? where ID = ? ";
	
	private static final String GET_EVENTLOGTYPE_QRY = " SELECT lt.*, st.id subtype_id, st.eventsubtypename subtype_name, st.eventsubtypedescription subtype_desc, st.isactive isSubTypeActive, mg.group_name, mg.email "+ 
            " from transp.eventtype lt, transp.eventsubtype st, transp.eventlog_messagegroup mg "+ 
            " where lt.id=st.eventtypeid(+) and st.msggroup_id=mg.id(+) ";
	
	private static final String GET_ROUTE_WINDOWS = "select "+ 
			" bs.ROUTE_NO, bs.WINDOW_STARTTIME, bs.WINDOW_ENDTIME, BS.STOP_SEQUENCE  " + 
			" from " +
			" transp.handoff_batch b, transp.handoff_batchstop bs " +
			" where " +
			" b.batch_id = bs.batch_id and b.delivery_date = ?  and b.batch_status in ('CPD','CPD/ADC','CPD/ADF') " + 
			" GROUP BY " +
			" bs.ROUTE_NO, bs.WINDOW_STARTTIME, bs.WINDOW_ENDTIME, BS.STOP_SEQUENCE  " +
			" ORDER BY " +
			" bs.ROUTE_NO, bs.WINDOW_STARTTIME, bs.WINDOW_ENDTIME, BS.STOP_SEQUENCE  ";
	
	private static final String GET_EVENTMESSAGEGROUP_QRY = " select * from transp.eventlog_messagegroup ";
	
	private static final String CLEAR_EVENTLOG_TYPE = "DELETE from transp.eventtype ";
	
	private static final String CLEAR_EVENTLOG_MSGGROUP = "DELETE from transp.eventlog_messagegroup ";
	
	private static final String INSERT_EVENTLOG_TYPE = " INSERT INTO TRANSP.EVENTTYPE (EVENTTYPENAME, EVENTTYPEDESCRIPTION, CUSTOMER_REQ, EMPLOYEE_REQ, CREATEDBY, DATECREATED) VALUES (?,?,?,?,?,?) ";
	
	private static final String INSERT_EVENTLOG_SUBTYPE = " INSERT INTO TRANSP.EVENTSUBTYPE (EVENTSUBTYPENAME,DESCRIPTION,EVENTTYPEID, MSGGROUP_ID, CREATEDBY, DATECREATED) VALUES (?,?,?,?,?,?) ";
	
	private static final String INSERT_EVENTLOG_MSGGROUP = " INSERT INTO TRANSP.EVENTLOG_MESSAGEGROUP (GROUP_NAME, EMAIL, CREATEDBY, DATECREATED) VALUES (?,?,?,?) ";
	
	
	/*Mot Event Log*/
	
	private static final String GET_MOTEVENTLOGNEXTSEQ_QRY = "SELECT TRANSP.MOTEVENTLOGSEQ.nextval FROM DUAL";
	
	private static final String GET_MOTEVENTLOG_QRY = " SELECT el.*, els.*, et.moteventtypename from transp.moteventlogbook el, transp.motstopnumberbreakdown els, transp.moteventtype et "+ 
                  													" where el.id = els.moteventlogid(+) and el.eventtypeid = et.id(+) and el.eventdate = ? ";
	
	private static final String GET_MOTEVENTLOG_BY_ID = " SELECT el.*, els.*, et.moteventtypename from transp.moteventlogbook el, transp.motstopnumberbreakdown els, transp.moteventtype et "+ 
            	  													" where el.id = els.moteventlogid(+) and el.eventtypeid = et.id(+) and el.id = ? ";
	
	private static final String GET_MOTEVENTLOG_DATERANGE_QRY = " SELECT el.*, els.*, et.moteventtypename from transp.moteventlogbook el, transp.motstopnumberbreakdown els, transp.moteventtype et "+ 
                  													" where el.id = els.moteventlogid(+) and el.eventtypeid = et.id(+) and el.eventdate >= ? ";
	
	private static final String INSERT_MOTEVENT_LOG = "INSERT INTO TRANSP.MOTEVENTLOGBOOK(ID, EVENTDATE, ROUTENUMBER, MOTROUTENUMBER, NEXTELNUMBER, EVENTTYPEID, EVENTDESCRIPTION, TICKETNUMBER, DATEVERIFIED, VERIFIEDBY, DATECREATED, CREATEDBY )" +
													  " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String UPDATE_MOTEVENTLOG_QRY = "UPDATE TRANSP.MOTEVENTLOGBOOK SET EVENTDATE=?, ROUTENUMBER=?, MOTROUTENUMBER=?, NEXTELNUMBER=?, EVENTTYPEID=?, EVENTDESCRIPTION=?, TICKETNUMBER=?, DATEVERIFIED=?, VERIFIEDBY=?, DATECREATED=?, CREATEDBY=? where ID = ? ";
	
	private static final String INSERT_MOTEVENTLOG_STOP = "INSERT INTO TRANSP.MOTSTOPNUMBERBREAKDOWN (MOTEVENTLOGID, MOTSTOPNUMBER) VALUES (?,?) ";
	
	private static final String CLEAR_MOTEVENTLOG_STOP = "DELETE from TRANSP.MOTSTOPNUMBERBREAKDOWN where moteventlogid in ";
	
	private static final String GET_MOTEVENTLOGTYPE_QRY = " SELECT lt.*, mg.group_name, mg.email "+
															" from transp.moteventtype lt, transp.moteventlog_messagegroup mg "+
															" where lt.msggroup_id = mg.id(+) ";
	
	private static final String GET_MOTEVENTMESSAGEGROUP_QRY = " select * from transp.moteventlog_messagegroup ";
	
	private static final String INSERT_MOTEVENTLOG_MSGGROUP = " INSERT INTO TRANSP.MOTEVENTLOG_MESSAGEGROUP (GROUP_NAME, EMAIL, CREATEDBY, DATECREATED) VALUES (?,?,?,?) ";
		
	private static final String CLEAR_MOTEVENTLOG_MSGGROUP = "DELETE from transp.moteventlog_messagegroup ";
	
	private static final String UPDATE_MOTEVENT_STATUS = "UPDATE TRANSP.MOTEVENTLOGBOOK SET DATEVERIFIED=?, VERIFIEDBY=? WHERE ID = ? ";
	
	private static final String INSERT_MOTEVENTLOG_TYPE = "INSERT INTO TRANSP.MOTEVENTTYPE (MOTEVENTTYPENAME,MOTEVENTTYPEDESCRIPTION,MSGGROUP_ID,CREATEDBY,DATECREATED) VALUES (?,?,?,?,?) ";
	
	private static final String CLEAR_MOTEVENTLOG_TYPE = "DELETE from transp.moteventtype ";
		
	private static final String GET_AUTHUSER_PRIVILEGES = "select u.username, up.privilege_name from TRANSP.AUTHUSERS u, TRANSP.AUTHUSER_PRIVILEGE up where u.username=up.username(+) ";
	
	public void clearEventLogType() throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_EVENTLOG_TYPE, new Object[] {});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void clearEventLogMessageGroup() throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_EVENTLOG_MSGGROUP, new Object[] {});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void logEventTypeInfo(List<EventLogType> eventType) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate eventTypeUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_EVENTLOG_TYPE);
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			
			eventTypeUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(EventLogType _type : eventType) {
				
				eventTypeUpdater.update(new Object[] { 
											_type.getName()
											, _type.getDescription()											
											, _type.getCustomerReq()
											, _type.getEmployeeReq()
											, _type.getUserId()
											, new Date()
									
							});
			}			
			eventTypeUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void logEventSubTypeInfo(List<EventLogSubType> subType) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate eventTypeUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_EVENTLOG_SUBTYPE);
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventTypeUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			eventTypeUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(EventLogSubType _subType : subType) {
				
				eventTypeUpdater.update(new Object[] { 
											  _subType.getName()
											, _subType.getDescription()
											, _subType.getEventTypeId()
											, _subType.getMsgGroup().getGroupName()
											, _subType.getUserId()
											, new Date()
									
							});
			}			
			eventTypeUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void logEventMessageGroupInfo(List<EventLogMessageGroup> messageGroup) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate msgGroupUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_EVENTLOG_MSGGROUP);
			msgGroupUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			msgGroupUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			msgGroupUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			msgGroupUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			msgGroupUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(EventLogMessageGroup _msgGroup : messageGroup) {
				
				msgGroupUpdater.update(new Object[] { 
												_msgGroup.getGroupName()
													, _msgGroup.getEmail()
													, _msgGroup.getUserId()
													, new Date()
									
							});
			}			
			msgGroupUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public EventModel lookUpEventById(final String eventID) throws SQLException {
				
		final Map<String, EventModel> eventMapping = new HashMap<String, EventModel>();
			
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_EVENTLOG_BY_ID);
					ps.setString(1, eventID);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					getEventLogResultSet(eventMapping, rs);
				}
			});
			return eventMapping.get(eventID);			
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	private void getEventLogResultSet(
			final Map<String, EventModel> eventMapping, ResultSet rs)
			throws SQLException {
		do {
			String eventId = rs.getString("ID");
			String stopNumber = rs.getString("STOPNUMBER");
			
			if(!eventMapping.containsKey(eventId)) {
				
				EventModel event = new EventModel();
				
				event.setId(eventId);
				event.setEventDate(rs.getTimestamp("EVENTDATE"));
				event.setRoute(rs.getString("ROUTENUMBER"));
				event.setTruck(rs.getString("TRUCKNUMBER"));
				event.setWindowStartTime(rs.getTimestamp("ROUTEWINDOWSTARTTIME"));
				event.setWindowEndTime(rs.getTimestamp("ROUTEWINDOWENDTIME"));
				event.setEventType(rs.getString("EVENTTYPENAME"));
				event.setEventSubType(rs.getString("EVENTSUBTYPENAME"));
				event.setDescription(rs.getString("EVENTDESCRIPTION"));
				event.setCrossStreet(rs.getString("CROSSSTREETS"));
				event.setEmployeeId(rs.getString("EMPLOYEENUMBER"));
				event.setScannerNumber(rs.getString("SCANNERNUMBER"));
				event.setTransactionDate(rs.getTimestamp("DATECREATED"));
				event.setUserId(rs.getString("CREATEDBY"));
				
				event.setStops(new HashSet<String>());
				eventMapping.put(eventId, event);
			}
			if(stopNumber != null)
				eventMapping.get(eventId).getStops().add(stopNumber);		
		} while (rs.next());
	}
	
	
	public List<EventModel> lookUpEvents(final Date eventDate) throws SQLException {
				
		final List<EventModel> result = new ArrayList<EventModel>();
		final Map<String, EventModel> eventMapping = new HashMap<String, EventModel>();
			
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_EVENTLOG);
					ps.setDate(1, new java.sql.Date(eventDate.getTime()));
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					getEventLogResultSet(eventMapping, rs);
				}				
			});
			result.addAll(eventMapping.values());
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public List<EventModel> lookUpEventForDateRange(final Date eventDate) throws SQLException {
		
		final List<EventModel> result = new ArrayList<EventModel>();
		final Map<String, EventModel> eventMapping = new HashMap<String, EventModel>();
			
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_EVENTLOG_DATERANGE_QRY);
					ps.setDate(1, new java.sql.Date(eventDate.getTime()));
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					getEventLogResultSet(eventMapping, rs);
				}
			});
			result.addAll(eventMapping.values());
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public String getNewEventLogId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_EVENTLOGNEXTSEQ_QRY);
	}
	
	public void logEvent(EventModel event) throws SQLException {
		
		Connection connection = null;
		String eventLogId = null;
		try {
			eventLogId = this.getNewEventLogId();			
			this.jdbcTemplate.update(INSERT_EVENT_LOG ,
												new Object[]{ eventLogId
																, event.getEventDate()
																, event.getRoute()
																, event.getTruck()
																, event.getWindowStartTime()
																, event.getWindowEndTime()
																, event.getEventType()
																, event.getEventSubType()
																, event.getDescription()
																, event.getCrossStreet()
																, event.getEmployeeId()
																, event.getScannerNumber()
																, event.getTransactionDate()
																, event.getUserId()
																, event.getEventRefId()
															});
			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			if(event.getStops().size() > 0) {
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_EVENTLOG_STOP);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
							
				for(String stop : event.getStops()) {
					batchUpdater.update(new Object[]{ eventLogId, stop });
				}			
				batchUpdater.flush();
			}
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateEventLog(EventModel event) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate eventUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_EVENTLOG_QRY);
			eventUpdater.declareParameter(new SqlParameter(Types.DATE));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			eventUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.compile();
			
			BatchSqlUpdate stopUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_EVENTLOG_STOP);
			stopUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			stopUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			stopUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			eventUpdater.update(new Object[]{ 
											  event.getEventDate()
											, event.getRoute()
											, event.getTruck()
											, event.getWindowStartTime()
											, event.getWindowEndTime()
											, event.getEventType()
											, event.getEventSubType()
											, event.getDescription()
											, event.getCrossStreet()
											, event.getEmployeeId()
											, event.getScannerNumber()
											, event.getTransactionDate()
											, event.getUserId()
											, event.getId()
							});
						
			eventUpdater.flush();
			
			StringBuffer updateQ = new StringBuffer();
			updateQ.append(CLEAR_EVENTLOG_STOP);
			updateQ.append(" ( ");
			updateQ.append("'").append(event.getId()).append("'");				
			updateQ.append(" ) ");
			
			//Clear existing stops for event
			this.jdbcTemplate.update(updateQ.toString(), new Object[] { });
			
			for(String stop : event.getStops()) {
				stopUpdater.update(new Object[]{ event.getId(), stop });
			}			
			stopUpdater.flush();
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<EventLogType> lookUpEventTypes(final String eventType) throws SQLException {
		
		final List<EventLogType> result = new ArrayList<EventLogType>();
		final Map<String, EventLogType> eventTypeMapping = new HashMap<String, EventLogType>();
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_EVENTLOGTYPE_QRY);
		if(eventType != null && !TransStringUtil.isEmpty(eventType)){
			updateQ.append(" and lt.id = ? ");
		}
		updateQ.append(" ORDER BY lt.id ");
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(updateQ.toString());
					if(eventType != null && !TransStringUtil.isEmpty(eventType)){
						ps.setString(1, eventType);
					}
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						String typeId = rs.getString("ID");
						String typeName = rs.getString("EVENTTYPENAME");
						String typeDesc = rs.getString("EVENTTYPEDESCRIPTION");
						String isTypeActive = rs.getString("ISACTIVE");
						
						String subTypeId = rs.getString("SUBTYPE_ID");
						String subTypeName = rs.getString("SUBTYPE_NAME");
						String subTypeDesc = rs.getString("SUBTYPE_DESC");						
						String isSubTypeActive = rs.getString("ISSUBTYPEACTIVE");
						
						String groupName = rs.getString("GROUP_NAME");
						String email = rs.getString("EMAIL");
						if(!eventTypeMapping.containsKey(typeId)
								&& "X".equals(isTypeActive)){
							EventLogType eventType = new EventLogType(typeId, typeName, typeDesc);
							eventType.setTransactionDate(rs.getTimestamp("DATECREATED"));
							eventType.setUserId(rs.getString("CREATEDBY"));
							eventType.setCustomerReq(rs.getString("ISLOCATIONREQUIRED"));
							eventType.setEmployeeReq(rs.getString("ISEMPLOYEEREQUIRED"));
							eventType.setOrderNumberReq(rs.getString("ORDERNUM_REQ"));
							
							eventTypeMapping.put(typeId, eventType);
						}
						
						if("X".equals(isTypeActive)
								&& "X".equals(isSubTypeActive) && subTypeId != null && subTypeName != null && subTypeDesc != null
								&& !"".equals(subTypeName) && !"".equals(subTypeDesc)) {
							EventLogSubType subType = new EventLogSubType();							
							if(groupName != null)
								subType.setMsgGroup(new EventLogMessageGroup(groupName, email));
							subType.setId(subTypeId);
							subType.setName(subTypeName);
							subType.setDescription(subTypeDesc);
							subType.setEventTypeId(typeId);
							subType.setEventTypeName(typeName);
							
							eventTypeMapping.get(typeId).getSubTypes().add(subType);
						}
					} while (rs.next());			
				}
			});
			result.addAll(eventTypeMapping.values());
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public List<EventLogMessageGroup> lookUpEventMessageGroup(final String messageGroup) throws SQLException {
		
		final List<EventLogMessageGroup> result = new ArrayList<EventLogMessageGroup>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_EVENTMESSAGEGROUP_QRY);
		if(messageGroup != null && !TransStringUtil.isEmpty(messageGroup)){
			updateQ.append(" and group_name = ? ");
		}
		updateQ.append(" ORDER BY group_name asc ");
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(updateQ.toString());
					if(messageGroup != null && !TransStringUtil.isEmpty(messageGroup)){
						ps.setString(1, messageGroup);
					}
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						EventLogMessageGroup groupModel = new EventLogMessageGroup();
						result.add(groupModel);
						groupModel.setGroupName(rs.getString("GROUP_NAME"));
						groupModel.setEmail(rs.getString("EMAIL"));
					} while (rs.next());			
				}
			});			
			
		} finally {
			if (connection != null)	connection.close();
		}	
		return result;
	}
	
	public void deleteEventLog(String[] eventLogId) throws SQLException {
		Connection connection = null;
		StringBuffer updateQ = new StringBuffer();
		updateQ.append(CLEAR_EVENTLOG_STOP);
		if(eventLogId != null && eventLogId.length > 0){
			updateQ.append(" ( ");
			for(int intCount = 0; intCount < eventLogId.length; intCount++ ) {
				updateQ.append("'").append(eventLogId[intCount]).append("'");				
				if(intCount < eventLogId.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}
		
		StringBuffer updateEventsQ = new StringBuffer();
		updateEventsQ.append(CLEAR_EVENTLOG_EVENTS);
		if(eventLogId != null && eventLogId.length > 0){
			updateEventsQ.append(" ( ");
			for(int intCount = 0; intCount < eventLogId.length; intCount++ ) {
				updateEventsQ.append("'").append(eventLogId[intCount]).append("'");				
				if(intCount < eventLogId.length-1) {
					updateEventsQ.append(",");
				}
			}
			updateEventsQ.append(")");
		}
		
		try {
			
			this.jdbcTemplate.update(updateQ.toString(), new Object[] { });
			
			this.jdbcTemplate.update(updateEventsQ.toString(), new Object[] { });
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}	
	
	
	public Map<String, List<String>> lookUpRouteWindows(final Date deliveryDate) throws SQLException {
		
		final Map<String, List<String>> routeWindowMapping = new HashMap<String, List<String>>();
		
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_ROUTE_WINDOWS);
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						String routeNo = rs.getString("ROUTE_NO");
						Date windowStartTime = rs.getTimestamp("WINDOW_STARTTIME");
						Date windowEndTime = rs.getTimestamp("WINDOW_ENDTIME");
						
						if(!routeWindowMapping.containsKey(routeNo)){
							routeWindowMapping.put(routeNo, new ArrayList<String>());
						}
						
						String windowTime = null;
						try {
							windowTime = TransStringUtil.getServerTime(windowStartTime) + " - " + TransStringUtil.getServerTime(windowEndTime);
						} catch (ParseException e) {
							// Do Nothing
						}
						routeWindowMapping.get(routeNo).add(windowTime);
							
					} while (rs.next());			
				}
			});
			return routeWindowMapping;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public Map<String, EventLogRouteModel> lookUpRoutes(final Date deliveryDate) throws SQLException {
		
		final Map<String, EventLogRouteModel> routeMapping = new HashMap<String, EventLogRouteModel>();
		
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_ROUTE_WINDOWS);
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						String routeNo = rs.getString("ROUTE_NO");
						Date windowStartTime = rs.getTimestamp("WINDOW_STARTTIME");
						Date windowEndTime = rs.getTimestamp("WINDOW_ENDTIME");
						String stopNo = rs.getString("STOP_SEQUENCE");
						
						if(!routeMapping.containsKey(routeNo)){
							EventLogRouteModel routeModel = new EventLogRouteModel();
							routeModel.setRouteNo(routeNo);
							routeMapping.put(routeNo, routeModel);
						}
						
						if(!routeMapping.get(routeNo).getStops().contains(stopNo)){
							routeMapping.get(routeNo).getStops().add(stopNo);
						}
						
						String windowTime = null;
						try {
							windowTime = TransStringUtil.getServerTime(windowStartTime) + " - " + TransStringUtil.getServerTime(windowEndTime);
							if(!routeMapping.get(routeNo).getWindows().contains(windowTime)){
								routeMapping.get(routeNo).getWindows().add(windowTime);
							}
						} catch (ParseException e) {
							// Do Nothing
						}
					} while (rs.next());			
				}
			});
			return routeMapping;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public MotEventModel lookUpMotEventById(final String eventID) throws SQLException {
		
		final Map<String, MotEventModel> eventMapping = new HashMap<String, MotEventModel>();				
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_MOTEVENTLOG_BY_ID);					
					ps.setString(1, eventID);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					getMotEventLogResultSet(eventMapping, rs);
				}
			});			
			return eventMapping.get(eventID);
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	
	public List<MotEventModel> lookUpMotEvents(final Date eventDate) throws SQLException {
		
		final List<MotEventModel> result = new ArrayList<MotEventModel>();
		final Map<String, MotEventModel> eventMapping = new HashMap<String, MotEventModel>();
				
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_MOTEVENTLOG_QRY);
					ps.setDate(1, new java.sql.Date(eventDate.getTime()));

					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					getMotEventLogResultSet(eventMapping, rs);
				}
			});
			result.addAll(eventMapping.values());
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public List<MotEventModel> lookUpMotEventForDateRange(final Date eventDate) throws SQLException {
		
		final List<MotEventModel> result = new ArrayList<MotEventModel>();
		final Map<String, MotEventModel> eventMapping = new HashMap<String, MotEventModel>();
				
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_MOTEVENTLOG_DATERANGE_QRY);
					ps.setDate(1, new java.sql.Date(eventDate.getTime()));

					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					getMotEventLogResultSet(eventMapping, rs);
				}
			});
			result.addAll(eventMapping.values());
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	private void getMotEventLogResultSet(
			final Map<String, MotEventModel> eventMapping,
			ResultSet rs) throws SQLException {
		do {
			String eventId = rs.getString("ID");
			String stopNumber = rs.getString("MOTSTOPNUMBER");
			
			if(!eventMapping.containsKey(eventId)) {
				
				MotEventModel event = new MotEventModel();
				
				event.setId(eventId);
				event.setEventDate(rs.getTimestamp("EVENTDATE"));
				event.setRoute(rs.getString("ROUTENUMBER"));
				event.setAddHocRoute(rs.getString("MOTROUTENUMBER"));
				event.setEventType(rs.getString("MOTEVENTTYPENAME"));							
				event.setDescription(rs.getString("EVENTDESCRIPTION"));
				event.setNextel(rs.getString("NEXTELNUMBER"));
				event.setTicketNumber(rs.getString("TICKETNUMBER"));							
				event.setTransactionDate(rs.getTimestamp("DATECREATED"));
				event.setUserId(rs.getString("CREATEDBY"));
				event.setVerifiedBy(rs.getString("VERIFIEDBY"));
				event.setVerifiedDate(rs.getTimestamp("DATEVERIFIED"));
				event.setVerified(event.getVerifiedDate() != null ? true : false);
				
				event.setStops(new HashSet<String>());
				eventMapping.put(eventId, event);
			}
			if(stopNumber != null)
				eventMapping.get(eventId).getStops().add(stopNumber);		
		} while (rs.next());
	}
	
	public String getNewMotEventLogId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_MOTEVENTLOGNEXTSEQ_QRY);
	}
	
	public void logMotEvent(MotEventModel event) throws SQLException {
		
		Connection connection = null;
		String eventLogId = null;
		try {
			eventLogId = this.getNewMotEventLogId();			
			this.jdbcTemplate.update(INSERT_MOTEVENT_LOG ,
												new Object[]{ eventLogId
																, event.getEventDate()
																, event.getRoute()
																, event.getAddHocRoute()
																, event.getNextel()
																, event.getEventType()
																, event.getDescription()
																, event.getTicketNumber()
																, event.getVerifiedDate()
																, event.getVerifiedBy()
																, new Date()
																, event.getUserId() });
			
			connection = this.jdbcTemplate.getDataSource().getConnection();

			if(event.getStops().size() > 0){
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_MOTEVENTLOG_STOP);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
							
				for(String stop : event.getStops()) {
					batchUpdater.update(new Object[]{ eventLogId, stop });
				}			
				batchUpdater.flush();
			}			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateMotEventLog(MotEventModel event) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate eventUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_MOTEVENTLOG_QRY);
			eventUpdater.declareParameter(new SqlParameter(Types.DATE));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			eventUpdater.compile();
			
			BatchSqlUpdate stopUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_MOTEVENTLOG_STOP);
			stopUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			stopUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			stopUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			eventUpdater.update(new Object[]{ 
											  event.getEventDate()
											, event.getRoute()
											, event.getAddHocRoute()
											, event.getNextel()
											, event.getEventType()
											, event.getDescription()
											, event.getTicketNumber()
											, event.getVerifiedDate()
											, event.getVerifiedBy()
											, event.getTransactionDate()
											, event.getUserId()
											, event.getId()
							});
						
			eventUpdater.flush();
			
			StringBuffer updateQ = new StringBuffer();
			updateQ.append(CLEAR_MOTEVENTLOG_STOP);
			updateQ.append(" ( ");
			updateQ.append("'").append(event.getId()).append("'");				
			updateQ.append(" ) ");
			
			//Clear existing stops for event
			this.jdbcTemplate.update(updateQ.toString(), new Object[] { });
			
			for(String stop : event.getStops()) {
				stopUpdater.update(new Object[]{ event.getId(), stop });
			}			
			stopUpdater.flush();
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<MotEventType> lookUpMotEventTypes(final String eventType) throws SQLException {
		
		final List<MotEventType> result = new ArrayList<MotEventType>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_MOTEVENTLOGTYPE_QRY);
		if(eventType != null && !TransStringUtil.isEmpty(eventType)){
			updateQ.append(" and lt.id = ? ");
		}
		updateQ.append(" ORDER BY lt.id ");
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(updateQ.toString());
					if(eventType != null && !TransStringUtil.isEmpty(eventType)){
						ps.setString(1, eventType);
					}
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						String typeId = rs.getString("ID");
						String typeName = rs.getString("MOTEVENTTYPENAME");
						String typeDesc = rs.getString("MOTEVENTTYPEDESCRIPTION");
						String isTypeActive = rs.getString("ISACTIVE");
						String groupName = rs.getString("GROUP_NAME");
						String email = rs.getString("EMAIL");
						String orderNoReq = rs.getString("ORDERNUM_REQ");
						
						MotEventType eventType = null;
						if("X".equals(isTypeActive)) {
							eventType = new MotEventType(typeId, typeName, typeDesc);
							if(groupName != null && !"".equals(groupName))
								eventType.setMsgGroup(new EventLogMessageGroup(groupName, email));
							eventType.setTransactionDate(rs.getTimestamp("DATECREATED"));
							eventType.setUserId(rs.getString("CREATEDBY"));
							eventType.setOrderNumberReq(orderNoReq);
							result.add(eventType);
						}
					} while (rs.next());			
				}
			});
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public void updateEventStatus(Set<MotEventModel> events) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_MOTEVENT_STATUS);
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.compile();
			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			for(MotEventModel model : events) {
				batchUpdater.update(new Object[]{ 
												  model.getVerifiedDate()											
												, model.getVerifiedBy()
												, model.getId()
										});
			
			}
			batchUpdater.flush();
		} finally {
			if (connection != null) connection.close();
		}
	}
	
	public void clearMotEventLogType() throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_MOTEVENTLOG_TYPE, new Object[] {});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void logMotEventTypeInfo(List<MotEventType> eventType) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_MOTEVENTLOG_TYPE);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));	
			batchUpdater.compile();
			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			for(MotEventType _type : eventType) {
				batchUpdater.update(new Object[]{ _type.getName()
												, _type.getDescription()
												, _type.getMsgGroup().getGroupName()
												, _type.getUserId()
												, _type.getTransactionDate()
										});
			}
			batchUpdater.flush();
		} finally {
			if (connection != null) connection.close();
		}
	}
	
	public void logMotEventMessageGroupInfo(List<EventLogMessageGroup> messageGroup) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate msgGroupUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_MOTEVENTLOG_MSGGROUP);
			msgGroupUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			msgGroupUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			msgGroupUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			msgGroupUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			msgGroupUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(EventLogMessageGroup _msgGroup : messageGroup) {
				
				msgGroupUpdater.update(new Object[] { 
												_msgGroup.getGroupName()
													, _msgGroup.getEmail()
													, _msgGroup.getUserId()
													, new Date()
									
							});
			}			
			msgGroupUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public List<EventLogMessageGroup> lookUpMotEventMessageGroup(final String messageGroup) throws SQLException {
		
		final List<EventLogMessageGroup> result = new ArrayList<EventLogMessageGroup>();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_MOTEVENTMESSAGEGROUP_QRY);
		if(messageGroup != null && !TransStringUtil.isEmpty(messageGroup)){
			updateQ.append(" and group_name = ? ");
		}
		updateQ.append(" ORDER BY group_name asc ");
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(updateQ.toString());
					if(messageGroup != null && !TransStringUtil.isEmpty(messageGroup)){
						ps.setString(1, messageGroup);
					}
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						EventLogMessageGroup groupModel = new EventLogMessageGroup();
						result.add(groupModel);
						groupModel.setGroupName(rs.getString("GROUP_NAME"));
						groupModel.setEmail(rs.getString("EMAIL"));
					} while (rs.next());			
				}
			});			
			
		} finally {
			if (connection != null)	connection.close();
		}	
		return result;
	}
	
	public void clearMotEventLogMessageGroup() throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_MOTEVENTLOG_MSGGROUP, new Object[] {});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public Map<String, AuthUser> getAuthUserPrivileges() throws SQLException {
		final Map<String, AuthUser> userMapping = new HashMap<String, AuthUser>();
		
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_AUTHUSER_PRIVILEGES);				
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					String userName = rs.getString("USERNAME");
					String privilege = rs.getString("PRIVILEGE_NAME");
					
					AuthUser user = null;
					if(!userMapping.containsKey(userName)) {
						user = new AuthUser();
						userMapping.put(userName, user);
					}
					if(privilege != null && !TransStringUtil.isEmpty(privilege)) {
						userMapping.get(userName).getPrivilages().add(Privilege.valueOf(privilege));	
					}
				}
			});						
		} finally {
			if (connection != null)	connection.close();
		}	
		
		return userMapping;
		
	}
}
