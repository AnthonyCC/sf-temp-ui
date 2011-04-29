package com.freshdirect.delivery.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvRestrictionDAO {

	private static Category LOGGER = LoggerFactory.getInstance(DlvRestrictionDAO.class);
	
	private static String DELIVERY_RESTRICTIONS_RETURN = 
		"select ID,TYPE,NAME,DAY_OF_WEEK,START_TIME,END_TIME,REASON,MESSAGE,CRITERION,MEDIA_PATH FROM dlv.restricted_days where id=?";
	
	public static RestrictionI getDlvRestriction(Connection conn, String restrictionId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELIVERY_RESTRICTIONS_RETURN);									
		ps.setString(1, restrictionId);				
		RestrictionI restriction=null;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String msg = rs.getString("MESSAGE");
			String path = rs.getString("MEDIA_PATH");
			EnumDlvRestrictionCriterion criterion = EnumDlvRestrictionCriterion.getEnum(rs.getString("CRITERION"));
			if (criterion == null) {
				// skip unknown criteria
				continue;
			}

			EnumDlvRestrictionReason reason = EnumDlvRestrictionReason.getEnum(rs.getString("REASON"));
			if (reason == null) {
				// skip unknown reasons
				continue;
			}

			java.util.Date startDate = new java.util.Date(rs.getTimestamp("START_TIME").getTime());
			java.util.Date endDate = new java.util.Date(rs.getTimestamp("END_TIME").getTime());
			int dayOfWeek = rs.getInt("DAY_OF_WEEK");

			String typeCode = rs.getString("TYPE");
			EnumDlvRestrictionType type = EnumDlvRestrictionType.getEnum(typeCode);
			if (type == null && "PTR".equals(typeCode)) {
				type = EnumDlvRestrictionType.RECURRING_RESTRICTION;
			}

			if (EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(type)) {

				endDate = DateUtil.roundUp(endDate);

				// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
				if (reason.isSpecialHoliday()) {
					restriction=new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate,path);
				} else {
					restriction=new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate,path);
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restriction=new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime,path);

			} else {
				// ignore	
			}
		}

		LOGGER.debug("restriction :"+restriction);
		rs.close();
		ps.close();

		return restriction;

	}
	
	private static final String GET_ALCOHOL_RESTRICTION = "select  r.ID,r.TYPE,r.NAME,r.START_TIME,r.END_TIME,r.REASON,r.MESSAGE,r.CRITERION,r.MEDIA_PATH, "+
											"D.DAY_OF_WEEK, D.RES_START_TIME,D.RES_END_TIME, M.ID MUNICIPALITY_ID,M.STATE,M.COUNTY,M.CITY,M.ALCOHOL_RESTRICTED "+ 
											"from dlv.restricted_days r,DLV.RESTRICTION_DETAIL d, DLV.MUNICIPALITY_INFO m, DLV.MUNICIPALITY_RESTRICTION_DATA mr "+
											"where R.ID = D.RESTRICTION_ID(+) "+
											"and R.ID = MR.RESTRICTION_ID "+
											"and M.ID = MR.MUNICIPALITY_ID "+
											"and M.ID = ? and R.ID = ?";
	public static AlcoholRestriction getAlcoholRestriction(Connection conn, String restrictionId, String municipalityId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		AlcoholRestriction restriction = null;
		try {
			ps = conn.prepareStatement(GET_ALCOHOL_RESTRICTION);
	
			ps.setString(1, municipalityId);
			ps.setString(2, restrictionId);
			rs = ps.executeQuery();
			if(rs.next()) {
				String name = null;
				String msg = null;
				String path =null;
				EnumDlvRestrictionReason reason = null;
				EnumDlvRestrictionCriterion criterion = null;
				EnumDlvRestrictionType type = null;
				java.util.Date startDate = null;
				java.util.Date endDate = null;
				String state = null;
				String county = null;
				boolean alcoholRestricted = false;
		
	
				Map<Integer, List<TimeOfDayRange>> timeRangeMap = new HashMap<Integer, List<TimeOfDayRange>>();
			
				do {

					name = rs.getString("NAME");
					msg = rs.getString("MESSAGE");
					path = rs.getString("MEDIA_PATH");
					criterion = EnumDlvRestrictionCriterion.getEnum(rs.getString("CRITERION"));
					if (criterion == null) {
						// skip unknown criteria
						continue;
					}
	
					reason = EnumDlvRestrictionReason.getEnum(rs.getString("REASON"));
					if (reason == null) {
						// skip unknown reasons
						continue;
					}
	
					startDate = new java.util.Date(rs.getTimestamp("START_TIME").getTime());
					endDate = new java.util.Date(rs.getTimestamp("END_TIME").getTime());
	
					String typeCode = rs.getString("TYPE");
					type = EnumDlvRestrictionType.getEnum(typeCode);
					if (type == null && "PTR".equals(typeCode)) {
						type = EnumDlvRestrictionType.RECURRING_RESTRICTION;
					}
					state = rs.getString("state");
					county = rs.getString("county");
					municipalityId = rs.getString("MUNICIPALITY_ID");
					alcoholRestricted = Boolean.getBoolean(rs.getString("ALCOHOL_RESTRICTED"));
					
					String startTimeText = rs.getString("RES_START_TIME");
					if(startTimeText == null || startTimeText.length() == 0){
						//no timeslot defined.
						continue;
					}
					TimeOfDay startTime = new TimeOfDay(startTimeText);
					TimeOfDay endTime = new TimeOfDay(rs.getString("RES_END_TIME"));

					int dayOfWeek = rs.getInt("DAY_OF_WEEK");
					Integer key = new Integer(dayOfWeek);
					if(timeRangeMap.get(key) == null) {
						List<TimeOfDayRange> timeRanges = new ArrayList<TimeOfDayRange>();
						timeRanges.add(new TimeOfDayRange(startTime, endTime));
						timeRangeMap.put(key, timeRanges);
					} else {
						List<TimeOfDayRange> timeRanges = timeRangeMap.get(key);
						timeRanges.add(new TimeOfDayRange(startTime, endTime));
						timeRangeMap.put(key, timeRanges);
					}

				} while(rs.next());
				restriction = new AlcoholRestriction(restrictionId, criterion, reason, name, msg, startDate, endDate,type,
						path, state, county, null, municipalityId, alcoholRestricted);
				restriction.setTimeRangeMap(timeRangeMap);
			}
		} finally {
			if(rs != null) rs.close();
			if(ps != null) ps.close();
		}
		return restriction;
	}


	private static final String PLATTER_RESTRICTION_RETURN="select * from dlv.restricted_days where reason=? and type=? and CRITERION=? order by day_of_week";

	
	
	public static List getDlvRestrictions(Connection conn,String dlvReason,String dlvType,String dlvCriterion) throws SQLException {
		List restrictionList=new ArrayList();
		PreparedStatement ps = conn.prepareStatement(PLATTER_RESTRICTION_RETURN);
		ps.setString(1,dlvReason);													
		ps.setString(2,dlvType);
		ps.setString(3,dlvCriterion);
		RestrictionI restriction=null;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String msg = rs.getString("MESSAGE");
			String path = rs.getString("MEDIA_PATH");
			EnumDlvRestrictionCriterion criterion = EnumDlvRestrictionCriterion.getEnum(rs.getString("CRITERION"));
			if (criterion == null) {
				// skip unknown criteria
				continue;
			}

			EnumDlvRestrictionReason reason = EnumDlvRestrictionReason.getEnum(rs.getString("REASON"));
			if (reason == null) {
				// skip unknown reasons
				continue;
			}

			java.util.Date startDate = new java.util.Date(rs.getTimestamp("START_TIME").getTime());
			java.util.Date endDate = new java.util.Date(rs.getTimestamp("END_TIME").getTime());
			int dayOfWeek = rs.getInt("DAY_OF_WEEK");

			String typeCode = rs.getString("TYPE");
			EnumDlvRestrictionType type = EnumDlvRestrictionType.getEnum(typeCode);
			if (type == null && "PTR".equals(typeCode)) {
				type = EnumDlvRestrictionType.RECURRING_RESTRICTION;
			}

			if (EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(type)) {

				endDate = DateUtil.roundUp(endDate);

				// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
				if (reason.isSpecialHoliday()) {
					restriction=new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate, path);
				} else {
					restriction=new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate, path);
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restriction=new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime, path);

			} else {
				// ignore	
			}
			restrictionList.add(restriction);
		}

		LOGGER.debug("restriction :"+restriction);
		rs.close();
		ps.close();

		return restrictionList;

	}

	private static final String ADDRESS_RESTRICTION_RETURN="select scrubbed_address, apartment, zipcode, reason, date_modified, modified_by from dlv.restricted_address  where  scrubbed_address =UPPER(?)  AND  zipcode =?";
	
	public static RestrictedAddressModel getAddressRestriction(Connection conn,String address1,String apartment,String zipCode) throws SQLException {
	
		String sql=ADDRESS_RESTRICTION_RETURN;
		if(apartment!=null && apartment.trim().length()>0){
		 sql=sql+" AND  apartment =?";	
		}
		
		LOGGER.debug("sql "+sql);
		PreparedStatement ps = conn.prepareStatement(sql);
		LOGGER.debug("address1 "+address1);
		LOGGER.debug("zipCode "+zipCode);
		LOGGER.debug("apartment"+apartment);
				
		ps.setString(1,address1);															
		ps.setString(2,zipCode);
		if(apartment!=null && apartment.trim().length()>0){
			ps.setString(3,apartment);
		}
		 
		RestrictedAddressModel restriction=null;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			restriction= new  RestrictedAddressModel();
			restriction.setAddress1(rs.getString("scrubbed_address"));
			restriction.setApartment(rs.getString("apartment"));
			restriction.setZipCode(rs.getString("zipCode"));
			EnumRestrictedAddressReason reason = EnumRestrictedAddressReason.getRestrictionReason(rs.getString("reason"));
			restriction.setReason(reason);	
			java.util.Date dateModified = new java.util.Date(rs.getTimestamp("date_modified").getTime());			
			restriction.setLastModified(dateModified);
			restriction.setModifiedBy(rs.getString("modified_by"));						
		
		}
		
		LOGGER.debug("restriction :"+restriction);
		rs.close();
		ps.close();

		return restriction;

	}

	
	
	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");
	private final static String DEFAULT_RECURRING_DATE = "01/01/2004 ";
	private static final SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	
	private static final String DELIVERY_RESTRICTIONS_UPDATE="update dlv.restricted_days set type=?, NAME= ?, REASON=?, MESSAGE=?, MEDIA_PATH=?, CRITERION=?, DAY_OF_WEEK=? , START_TIME=?, END_TIME=? where ID=?";
	
	public static void updateDeliveryRestriction(Connection conn, RestrictionI restriction) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELIVERY_RESTRICTIONS_UPDATE);
												
		ps.setString(1, restriction.getType().getName());
		ps.setString(2, restriction.getName());		
		ps.setString(3, restriction.getReason().getName());
		ps.setString(4, restriction.getMessage());
		ps.setString(5, restriction.getPath());
		ps.setString(6, restriction.getCriterion().getName());
		if(restriction instanceof OneTimeReverseRestriction){
			OneTimeReverseRestriction otrRestriction=(OneTimeReverseRestriction)restriction;
			ps.setNull(7,Types.INTEGER);
			ps.setDate(8,new java.sql.Date(otrRestriction.getDateRange().getStartDate().getTime()));			
			ps.setTimestamp(9,new Timestamp(otrRestriction.getDateRange().getEndDate().getTime()));
		}else if(restriction instanceof OneTimeRestriction){
			OneTimeRestriction otRestriction=(OneTimeRestriction)restriction;
			ps.setNull(7,Types.INTEGER);
			ps.setDate(8,new java.sql.Date(otRestriction.getDateRange().getStartDate().getTime()));
			LOGGER.debug("otRestriction.getDateRange().getEndDate() :"+otRestriction.getDateRange().getEndDate());
			ps.setTimestamp(9,new Timestamp(otRestriction.getDateRange().getEndDate().getTime()));
		}else if(restriction instanceof RecurringRestriction){
			RecurringRestriction rRestriction=(RecurringRestriction)restriction;
			ps.setInt(7,rRestriction.getDayOfWeek());			
			Date newStartDate=null;
			Date newEndDate=null;
			try {
				newStartDate=format.parse(DEFAULT_RECURRING_DATE+rRestriction.getTimeRange().getStartTime());
				newEndDate=format.parse(DEFAULT_RECURRING_DATE+rRestriction.getTimeRange().getEndTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				LOGGER.warn(e);				
			}			
			ps.setTimestamp(8,new Timestamp(newStartDate.getTime()));
			ps.setTimestamp(9,new Timestamp(newEndDate.getTime()));			
		}
		ps.setString(10, restriction.getId());		
		int rowCount= ps.executeUpdate();
		if(rowCount==0){
			throw new SQLException("restriction data could not be updated"+restriction);
		}
				
		ps.close();				

	}
	
	
	
    private static final String ADDRESS_RESTRICTIONS_UPDATE="update dlv.restricted_address set scrubbed_address=?, apartment=?, zipCode=?, reason=?,date_modified=sysdate,modified_by=? where scrubbed_address=? and zipCode=?";
	
	public static void updateAddressRestriction(Connection conn, RestrictedAddressModel restriction,String address1,String apartment,String zipCode) throws SQLException {
		
		String sql=ADDRESS_RESTRICTIONS_UPDATE;
		if(apartment!=null && apartment.trim().length()>0){
			sql=sql+" and  apartment=?";
		}
		
		PreparedStatement ps = conn.prepareStatement(sql);
												
		ps.setString(1, restriction.getAddress1());
		ps.setString(2, restriction.getApartment());		
		ps.setString(3, restriction.getZipCode());
		ps.setString(4, restriction.getReason().getCode());
		ps.setString(5, restriction.getModifiedBy());
		
		ps.setString(6, address1);
		ps.setString(7, zipCode);		
		if(apartment!=null && apartment.trim().length()>0){
			ps.setString(8, apartment);	
		}								
		int rowCount= ps.executeUpdate();
		if(rowCount==0){
			throw new SQLException("address restriction data could not be updated"+restriction);
		}
				
		ps.close();				

	}

	
	
	private static String DELIVERY_RESTRICTIONS_DELETE = 
		"DELETE FROM dlv.restricted_days where ID=?";

	public static void deleteDeliveryRestriction(Connection conn, String restrictionId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELIVERY_RESTRICTIONS_DELETE);
				
		ps.setString(1, restrictionId);		
		int rowCount= ps.executeUpdate();
		if(rowCount==0){
			throw new SQLException("restriction data could not be deleted"+restrictionId);
		}
		LOGGER.debug("restriction is deleted :"+restrictionId);		
		ps.close();				
	}
	
	private static String ADDRESS_RESTRICTIONS_DELETE = 
		"delete from dlv.restricted_address where scrubbed_address=? and zipcode=?";

	public static void deleteAddressRestriction(Connection conn, String address1,String apartment,String zipCode) throws SQLException {
		String sql=ADDRESS_RESTRICTIONS_DELETE;
		if(apartment!=null && apartment.trim().length()>0){
			sql=sql+" and  apartment=?";
		}
		
		LOGGER.debug("sql "+sql);
		PreparedStatement ps = conn.prepareStatement(sql);
		LOGGER.debug("address1 "+address1);
		LOGGER.debug("zipCode "+zipCode);
		LOGGER.debug("apartment"+apartment);
		
		ps.setString(1, address1);
		ps.setString(2, zipCode);		
		if(apartment!=null && apartment.trim().length()>0){
			ps.setString(3, apartment);	
		}	
		
		
		int rowCount= ps.executeUpdate();
		if(rowCount==0){
			throw new SQLException("address restriction data could not be deleted"+address1);
		}
				
		ps.close();				
	}
	
	private static final String DELIVERY_RESTRICTION_INSERT="insert into dlv.restricted_days(ID,TYPE,NAME,REASON,MESSAGE,CRITERION,DAY_OF_WEEK,START_TIME,END_TIME.MEDIA_PATH)"+ 
                                                            "values(?,?,?,?,?,?,?,?,?,?)";
	
	public static void insertDeliveryRestriction(Connection conn, RestrictionI restriction) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELIVERY_RESTRICTION_INSERT);
		String id = SequenceGenerator.getNextId(conn, "CUST");
		ps.setString(1, id);
		ps.setString(2, restriction.getType().getName());
		ps.setString(3, restriction.getName());		
		ps.setString(4, restriction.getReason().getName());
		ps.setString(5, restriction.getMessage());
		ps.setString(6, restriction.getCriterion().getName());
		if(restriction instanceof OneTimeReverseRestriction){
			OneTimeReverseRestriction otrRestriction=(OneTimeReverseRestriction)restriction;
			ps.setNull(7,Types.INTEGER);
			ps.setDate(8,new java.sql.Date(otrRestriction.getDateRange().getStartDate().getTime()));
			ps.setTimestamp(9,new Timestamp(otrRestriction.getDateRange().getEndDate().getTime()));
		}else if(restriction instanceof OneTimeRestriction){						
			OneTimeRestriction otRestriction=(OneTimeRestriction)restriction;
			ps.setNull(7,Types.INTEGER);
			ps.setDate(8,new java.sql.Date(otRestriction.getDateRange().getStartDate().getTime()));
			ps.setTimestamp(9,new Timestamp(otRestriction.getDateRange().getEndDate().getTime()));
		}else if(restriction instanceof RecurringRestriction){
			RecurringRestriction rRestriction=(RecurringRestriction)restriction;
			ps.setInt(7,rRestriction.getDayOfWeek());			
			ps.setDate(8,new java.sql.Date(rRestriction.getTimeRange().getStartTime().getNormalDate().getTime()));
			ps.setTimestamp(9,new Timestamp(rRestriction.getTimeRange().getEndTime().getNormalDate().getTime()));			
		}
		ps.setString(10, restriction.getPath());
		//ps.setString(9, restriction.getId());		
		int rowCount= ps.executeUpdate();
		if(rowCount==0){
			throw new SQLException("restriction data could not be inserted"+restriction);
		}
		LOGGER.debug("Data is inserted1 :"+restriction);
		
		ps.close();						
	}
	
	private static final String ADDRESS_RESTRICTION_INSERT="insert into dlv.restricted_address(SCRUBBED_ADDRESS, APARTMENT,ZIPCODE,REASON,DATE_MODIFIED,MODIFIED_BY) values (?,?,?,?,sysdate,?)";
	
	public static void insertAddressRestriction(Connection conn, RestrictedAddressModel restriction) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ADDRESS_RESTRICTION_INSERT);		
		ps.setString(1, restriction.getAddress1());
		ps.setString(2, restriction.getApartment());		
		ps.setString(3, restriction.getZipCode());
		ps.setString(4, restriction.getReason().getCode());
		ps.setString(5, restriction.getModifiedBy());
		
		//ps.setString(9, restriction.getId());		
		int rowCount= ps.executeUpdate();
		if(rowCount==0){
			throw new SQLException("restriction data could not be inserted"+restriction);
		}
		LOGGER.debug("Data is inserted :"+restriction);
		
		ps.close();						
	}
	
	private static final String ALCOHOL_RESTRICTION_INSERT="insert into dlv.restricted_days(ID,TYPE,NAME,REASON,MESSAGE,CRITERION,DAY_OF_WEEK," +
															" START_TIME,END_TIME,MEDIA_PATH) values(?,?,?,?,?,?,?,?,?,?)";

	private static final String ALCOHOL_RESTRICTION_DETAIL_INSERT="insert into DLV.RESTRICTION_DETAIL(ID, RESTRICTION_ID, DAY_OF_WEEK, RES_START_TIME, RES_END_TIME) " +
																	"values(?,?,?,?,?)";
	
//	private static String muniSql = "select ID from dlv.municipality_info where state = ?  ";
	
	private static final String ALCOHOL_MUNICIPALITY_RESTRICTION_INSERT="insert into DLV.MUNICIPALITY_RESTRICTION_DATA(ID, MUNICIPALITY_ID, RESTRICTION_ID) " +
	"values(?,?,?)";

	public static String insertAlcoholRestriction(Connection conn, AlcoholRestriction restriction) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(ALCOHOL_RESTRICTION_INSERT);
			String id = SequenceGenerator.getNextId(conn, "CUST");
			ps.setString(1, id);
			ps.setString(2, restriction.getType().getName());
			ps.setString(3, restriction.getName());		
			ps.setString(4, restriction.getReason().getName());
			ps.setString(5, restriction.getMessage());
			ps.setString(6, restriction.getCriterion().getName());
			
			ps.setNull(7,Types.INTEGER);
			ps.setDate(8,new java.sql.Date(restriction.getDateRange().getStartDate().getTime()));
			ps.setTimestamp(9,new Timestamp(restriction.getDateRange().getEndDate().getTime()));
			ps.setString(10, restriction.getPath());
			//ps.setString(9, restriction.getId());		
			int rowCount= ps.executeUpdate();
			if(rowCount==0){
				throw new SQLException("restriction data could not be inserted"+restriction);
			}
			LOGGER.debug("Data is inserted1 :"+restriction);
			ps.close();
			Map<Integer, List<TimeOfDayRange>> timeRangeMap = restriction.getTimeRangeMap();
			if(timeRangeMap != null && !timeRangeMap.isEmpty()) {
				//Insert Into Restriction details.
				ps = conn.prepareStatement(ALCOHOL_RESTRICTION_DETAIL_INSERT);
		
				Set<Integer> dayOfWeeks = timeRangeMap.keySet();
				for(Iterator<Integer> outer = dayOfWeeks.iterator(); outer.hasNext();){
					int dayOfWeek = outer.next();
					String detailId = SequenceGenerator.getNextId(conn, "CUST");
					List<TimeOfDayRange> timeRanges = timeRangeMap.get(dayOfWeek);
					Iterator<TimeOfDayRange> inner = timeRanges.iterator();
					while(inner.hasNext()){
						ps.setString(1, detailId);
						ps.setString(2, id);
						ps.setInt(3, dayOfWeek);
						TimeOfDayRange timeRange = inner.next();
						TimeOfDay startTime = timeRange.getStartTime();
						TimeOfDay endTime = timeRange.getEndTime();
						ps.setString(4, startTime.getAsString());
						ps.setString(5, endTime.getAsString());
						ps.addBatch();
					}
				}
				ps.executeBatch();
				ps.close();
			}
			
			//insert mapping.
			//Fetch the Municipality ID from MUNICIPALITY_INFO table.
/*			StringBuffer buf = new StringBuffer(muniSql);
			if(restriction.getCounty() != null && restriction.getCounty().length() > 0) {
				buf.append(" and county ='").append(restriction.getCounty()).append("'");
			}
			if(restriction.getCity() != null && restriction.getCity().length() > 0) {
				buf.append(" and city ='").append(restriction.getCity()).append("'");
			}
	
			ps = conn.prepareStatement(buf.toString());
			ps.setString(1, restriction.getState());//Mandatory field
			rs = ps.executeQuery();
			String municipalityId =  null;
			if(rs.next()){
				municipalityId = rs.getString("ID");
			} else {
				throw new FDRuntimeException("Unable to find the MunicipalityInfo for given State "+restriction.getState()+" County "+restriction.getCounty()+" City "+restriction.getCity());
			}
			ps.close();
*/			if(restriction.getMunicipalityId() != null){
				//Create Mapping
				ps =  conn.prepareStatement(ALCOHOL_MUNICIPALITY_RESTRICTION_INSERT);
				String pk = SequenceGenerator.getNextId(conn, "CUST");
				ps.setString(1, pk);
				ps.setString(2, restriction.getMunicipalityId());
				ps.setString(3, id);
				ps.executeUpdate();
			}
			return id;
		} finally {
			if(rs != null) rs.close();
			if(ps != null) ps.close();
		}
	}
	
	private static final String ALCOHOL_RESTRICTION_UPDATE="update dlv.restricted_days set TYPE=?, NAME=?, REASON=?, MESSAGE=?, CRITERION=?, START_TIME=?, END_TIME=?, MEDIA_PATH=? WHERE ID = ?";

	private static final String ALCOHOL_RESTRICTION_DETAIL_DELETE="delete from DLV.RESTRICTION_DETAIL where RESTRICTION_ID = ?";
	

	public static void updateAlcoholRestriction(Connection conn, AlcoholRestriction restriction) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(ALCOHOL_RESTRICTION_UPDATE);
			ps.setString(1, restriction.getType().getName());
			ps.setString(2, restriction.getName());		
			ps.setString(3, restriction.getReason().getName());
			ps.setString(4, restriction.getMessage());
			ps.setString(5, restriction.getCriterion().getName());
			
			ps.setDate(6,new java.sql.Date(restriction.getDateRange().getStartDate().getTime()));
			ps.setTimestamp(7,new Timestamp(restriction.getDateRange().getEndDate().getTime()));
			ps.setString(8, restriction.getPath());
			ps.setString(9, restriction.getId());		
			int rowCount= ps.executeUpdate();
			if(rowCount==0){
				throw new SQLException("restriction data could not be inserted"+restriction);
			}
			LOGGER.debug("Data is Updated :"+restriction);
			ps.close();
			//Delete all existing restriction detail entries.
			ps = conn.prepareStatement(ALCOHOL_RESTRICTION_DETAIL_DELETE);
			ps.setString(1, restriction.getId());
			ps.executeUpdate();
			ps.close();
			Map<Integer, List<TimeOfDayRange>> timeRangeMap = restriction.getTimeRangeMap();
			if(timeRangeMap != null && !timeRangeMap.isEmpty()) {
				//Insert Into Restriction details.
				ps = conn.prepareStatement(ALCOHOL_RESTRICTION_DETAIL_INSERT);
		
				Set<Integer> dayOfWeeks = timeRangeMap.keySet();
				for(Iterator<Integer> outer = dayOfWeeks.iterator(); outer.hasNext();){
					int dayOfWeek = outer.next();
					String detailId = SequenceGenerator.getNextId(conn, "CUST");
					List<TimeOfDayRange> timeRanges = timeRangeMap.get(dayOfWeek);
					Iterator<TimeOfDayRange> inner = timeRanges.iterator();
					while(inner.hasNext()){
						ps.setString(1, detailId);
						ps.setString(2, restriction.getId());
						ps.setInt(3, dayOfWeek);
						TimeOfDayRange timeRange = inner.next();
						TimeOfDay startTime = timeRange.getStartTime();
						TimeOfDay endTime = timeRange.getEndTime();
						ps.setString(4, startTime.getAsString());
						ps.setString(5, endTime.getAsString());
						ps.addBatch();
					}
				}
				ps.executeBatch();
				ps.close();
			}
			
		} finally {
			if(ps != null) ps.close();
		}
	}

	private static final String ALCOHOL_RESTRICTION_DELETE="delete from dlv.restricted_days where ID = ?";
	private static final String ALCOHOL_MUNICIPALITY_RESTRICTION_DELETE="delete from DLV.MUNICIPALITY_RESTRICTION_DATA where RESTRICTION_ID=?";
	
	public static void deleteAlcoholRestriction(Connection conn, String restrictionId) throws SQLException {
		PreparedStatement ps = null;
		try {
			//Delete all existing restriction detail entries.
			ps = conn.prepareStatement(ALCOHOL_RESTRICTION_DETAIL_DELETE);
			ps.setString(1, restrictionId);
			ps.executeUpdate();
			ps.close();

			//Delete Mapping
			ps = conn.prepareStatement(ALCOHOL_MUNICIPALITY_RESTRICTION_DELETE);
			ps.setString(1, restrictionId);
			ps.executeUpdate();
			ps.close();
			
			//Delete Restriction
			ps = conn.prepareStatement(ALCOHOL_RESTRICTION_DELETE);
			ps.setString(1, restrictionId);
			ps.executeUpdate();
			ps.close();
			
			
		} finally {
			if(ps != null) ps.close();
		}
	}

	private static final String UPDATE_ALCOHOL_RESTRICTED_FLAG = "update dlv.MUNICIPALITY_INFO set ALCOHOL_RESTRICTED = ? where ID = ?";

	

	public static void updateAlcoholRestrictedFlag(Connection conn, String municipalityId, boolean restricted) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(UPDATE_ALCOHOL_RESTRICTED_FLAG);
			if(restricted){
				ps.setString(1, "X");	
			} else {
				ps.setNull(1, Types.VARCHAR);
			}
			
			ps.setString(2, municipalityId);		
			int rowCount= ps.executeUpdate();
			if(rowCount==0){
				throw new SQLException("ALCOHOL RESTRICTED FLAG could not be updated for "+municipalityId);
			}
			LOGGER.debug("ALCOHOL RESTRICTED FLAG updated for "+municipalityId);
		} finally {
			if(ps != null) ps.close();
		}
	}
	
	private static final String GET_MUNICIPALITY_STATE_COUNTIES = "select state, county from  DLV.MUNICIPALITY_INFO order by state";

	public static Map<String, List<String>> getMunicipalityStateCounties(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, List<String>> stateCounties = new HashMap<String, List<String>>();
		Set<String> counties = new HashSet<String>();
		String state = "";
		try {
			ps = conn.prepareStatement(GET_MUNICIPALITY_STATE_COUNTIES);
			rs = ps.executeQuery();
			while(rs.next()) {
				if(state.length() == 0 || state.equals(rs.getString("STATE"))){
					if(rs.getString("COUNTY") != null)
						counties.add(rs.getString("COUNTY"));
				} else {
					stateCounties.put(state, new ArrayList<String>(counties));
					counties.clear();
					if(rs.getString("COUNTY") != null)
						counties.add(rs.getString("COUNTY"));
				}
				state = rs.getString("STATE");
			}
			//add the last state
			stateCounties.put(state, new ArrayList<String>(counties));
		} finally {
			if(rs != null) rs.close();
			if(ps != null) ps.close();
		}
		return stateCounties;
	}

}
