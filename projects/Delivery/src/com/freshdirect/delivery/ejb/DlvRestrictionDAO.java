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
import java.util.StringTokenizer;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.announcement.EnumPlacement;
import com.freshdirect.delivery.announcement.EnumUserDeliveryStatus;
import com.freshdirect.delivery.announcement.EnumUserLevel;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.CompositeRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvRestrictionDAO {

	private static Category LOGGER = LoggerFactory.getInstance(DlvRestrictionDAO.class);
	
	private static String DELIVERY_RESTRICTIONS_RETURN = 
		"select ID,TYPE,NAME,DAY_OF_WEEK,START_TIME,END_TIME,REASON,MESSAGE,CRITERION,MEDIA_PATH FROM CUST.restricted_days where id=?";
	
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
				if(!EnumDlvRestrictionReason.PLATTER.equals(reason)){
					endDate = DateUtil.roundUp(endDate);
				}

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
											"from CUST.restricted_days r,CUST.RESTRICTION_DETAIL d, CUST.MUNICIPALITY_INFO m, CUST.MUNICIPALITY_RESTRICTION_DATA mr "+
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


	private static final String PLATTER_RESTRICTION_RETURN="select * from CUST.restricted_days where reason=? and type=? and CRITERION=? order by day_of_week";

	
	
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

	private static final String ADDRESS_RESTRICTION_RETURN="select scrubbed_address, apartment, zipcode, reason, date_modified, modified_by from CUST.restricted_address  where  scrubbed_address =UPPER(?)  AND  zipcode =?";
	
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
	
	private static final String DELIVERY_RESTRICTIONS_UPDATE="update CUST.restricted_days set type=?, NAME= ?, REASON=?, MESSAGE=?, MEDIA_PATH=?, CRITERION=?, DAY_OF_WEEK=? , START_TIME=?, END_TIME=? where ID=?";
	
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
			if(EnumDlvRestrictionReason.PLATTER.equals(restriction.getReason())){
				ps.setTimestamp(8,new Timestamp(otRestriction.getDateRange().getStartDate().getTime()));
			}else{
				ps.setDate(8,new java.sql.Date(otRestriction.getDateRange().getStartDate().getTime()));
			}
//			ps.setDate(8,new java.sql.Date(otRestriction.getDateRange().getStartDate().getTime()));
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
	
	
	
    private static final String ADDRESS_RESTRICTIONS_UPDATE="update CUST.restricted_address set scrubbed_address=?, apartment=?, zipCode=?, reason=?,date_modified=sysdate,modified_by=? where scrubbed_address=? and zipCode=?";
	
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
		"DELETE FROM CUST.restricted_days where ID=?";

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
		"delete from CUST.restricted_address where scrubbed_address=? and zipcode=?";

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
	
	private static final String DELIVERY_RESTRICTION_INSERT="insert into CUST.restricted_days(ID,TYPE,NAME,REASON,MESSAGE,CRITERION,DAY_OF_WEEK,START_TIME,END_TIME,MEDIA_PATH)"+ 
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
			if(EnumDlvRestrictionReason.PLATTER.equals(restriction.getReason())){
				ps.setTimestamp(8,new Timestamp(otRestriction.getDateRange().getStartDate().getTime()));
			}else{
				ps.setDate(8,new java.sql.Date(otRestriction.getDateRange().getStartDate().getTime()));
			}
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
	
	private static final String ADDRESS_RESTRICTION_INSERT="insert into CUST.restricted_address(SCRUBBED_ADDRESS, APARTMENT,ZIPCODE,REASON,DATE_MODIFIED,MODIFIED_BY) values (?,?,?,?,sysdate,?)";
	
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
	
	private static final String ALCOHOL_RESTRICTION_INSERT="insert into CUST.restricted_days(ID,TYPE,NAME,REASON,MESSAGE,CRITERION,DAY_OF_WEEK," +
															" START_TIME,END_TIME,MEDIA_PATH) values(?,?,?,?,?,?,?,?,?,?)";

	private static final String ALCOHOL_RESTRICTION_DETAIL_INSERT="insert into CUST.RESTRICTION_DETAIL(ID, RESTRICTION_ID, DAY_OF_WEEK, RES_START_TIME, RES_END_TIME) " +
																	"values(?,?,?,?,?)";	
	private static final String ALCOHOL_MUNICIPALITY_RESTRICTION_INSERT="insert into CUST.MUNICIPALITY_RESTRICTION_DATA(ID, MUNICIPALITY_ID, RESTRICTION_ID) " +
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
	
	private static final String ALCOHOL_RESTRICTION_UPDATE="update CUST.restricted_days set TYPE=?, NAME=?, REASON=?, MESSAGE=?, CRITERION=?, START_TIME=?, END_TIME=?, MEDIA_PATH=? WHERE ID = ?";

	private static final String ALCOHOL_RESTRICTION_DETAIL_DELETE="delete from CUST.RESTRICTION_DETAIL where RESTRICTION_ID = ?";
	

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

	private static final String ALCOHOL_RESTRICTION_DELETE="delete from CUST.restricted_days where ID = ?";
	private static final String ALCOHOL_MUNICIPALITY_RESTRICTION_DELETE="delete from CUST.MUNICIPALITY_RESTRICTION_DATA where RESTRICTION_ID=?";
	
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

	private static final String UPDATE_ALCOHOL_RESTRICTED_FLAG = "update CUST.MUNICIPALITY_INFO set ALCOHOL_RESTRICTED = ? where ID = ?";

	

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
	
	private static final String GET_MUNICIPALITY_STATE_COUNTIES = "select state, county from  CUST.MUNICIPALITY_INFO order by state";

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

	public static List<RestrictionI> getDlvRestrictions(Connection conn) throws SQLException {

/*		PreparedStatement ps = conn
			.prepareStatement("select id,criterion, type, name, message, day_of_week, start_time, end_time, reason, media_path from CUST.restricted_days " +
					"where reason not in ('WIN','BER','ACL') order by type") ;*/
		
		PreparedStatement ps = conn
				.prepareStatement("select  r.ID,d.id DETAIL_ID, r.TYPE,r.NAME,r.START_TIME,r.END_TIME,r.REASON,r.MESSAGE,r.CRITERION,r.MEDIA_PATH, "+
		"r.DAY_OF_WEEK, D.DAY_OF_WEEK DETAIL_DAY_OF_WEEK, D.RES_START_TIME,D.RES_END_TIME "+ 
		"from CUST.restricted_days r,CUST.RESTRICTION_DETAIL d "+
		"where R.ID = D.RESTRICTION_ID(+) "+
		"and R.REASON NOT IN ('ACL','WIN','BER') " +
		" ORDER BY R.ID");
		ResultSet rs = ps.executeQuery();
		List<RestrictionI> restrictions = new ArrayList<RestrictionI>();
		String restrictionId = "";
		String name = null;
		String msg = null;
		String path =null;
		EnumDlvRestrictionReason reason = null;
		EnumDlvRestrictionCriterion criterion = null;
		EnumDlvRestrictionType type = null;
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		Map<Integer, List<TimeOfDayRange>> timeRangeMap = new HashMap<Integer, List<TimeOfDayRange>>();
		while (rs.next()) {
			String restrictionDetailId=rs.getString("DETAIL_ID");
			if(null==restrictionDetailId){
				String id = rs.getString("ID");
				String lName = rs.getString("NAME");
				String lMessage = rs.getString("MESSAGE");
				String lMediaPath = rs.getString("MEDIA_PATH");
				EnumDlvRestrictionCriterion lCriterion = EnumDlvRestrictionCriterion.getEnum(rs.getString("CRITERION"));
				if (lCriterion == null) {
					// skip unknown criteria
					continue;
				}
	           
				
				EnumDlvRestrictionReason lReason = EnumDlvRestrictionReason.getEnum(rs.getString("REASON"));
				if (lReason == null) {
					// skip unknown reasons
					continue;
				}
	
				java.util.Date lStartDate = new Date(rs.getTimestamp("START_TIME").getTime());
				java.util.Date lEndDate = new Date(rs.getTimestamp("END_TIME").getTime());
				int dayOfWeek = rs.getInt("DAY_OF_WEEK");
	
				String typeCode = rs.getString("TYPE");
				EnumDlvRestrictionType lType = EnumDlvRestrictionType.getEnum(typeCode);
				if (lType == null && "PTR".equals(typeCode)) {
					lType = EnumDlvRestrictionType.RECURRING_RESTRICTION;
				}
	
				if (EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(lType)) {
	
					lEndDate = DateUtil.roundUp(lEndDate);
	
					// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
					if (lReason.isSpecialHoliday()) {
						restrictions.add(new OneTimeReverseRestriction(id,lCriterion, lReason, lName, lMessage, lStartDate, lEndDate,lMediaPath));
					} else {
						restrictions.add(new OneTimeRestriction(id,lCriterion, lReason, lName, lMessage, lStartDate, lEndDate,lMediaPath));
					}
	
				} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(lType)) {
	
					TimeOfDay startTime = new TimeOfDay(lStartDate);
					TimeOfDay endTime = new TimeOfDay(lEndDate);
					// round up 11:59 to next midnight
					if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
						endTime = TimeOfDay.NEXT_MIDNIGHT;
					}
					restrictions.add(new RecurringRestriction(id,lCriterion, lReason, lName, lMessage, dayOfWeek, startTime, endTime, lMediaPath));
	
				} else {
					// ignore	
				}
			}else{
				
				if(restrictionId.length() == 0 || restrictionId.equals(rs.getString("ID"))){
					restrictionId = rs.getString("ID");
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
					
					String startTimeText = rs.getString("RES_START_TIME");
					if(startTimeText == null || startTimeText.length() == 0){
						//no timeslot defined.
						continue;
					}
					TimeOfDay startTime = new TimeOfDay(startTimeText);
					TimeOfDay endTime = new TimeOfDay(rs.getString("RES_END_TIME"));

					int dayOfWeek = rs.getInt("DETAIL_DAY_OF_WEEK");
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
				} else {
					CompositeRestriction restriction = new CompositeRestriction(restrictionId, criterion, reason, name, msg,new DateRange(startDate, endDate), type, path);
					restriction.setTimeRangeMap(new HashMap<Integer, List<TimeOfDayRange>>(timeRangeMap));
					restrictions.add(restriction);
					timeRangeMap.clear();
					restrictionId = rs.getString("ID");
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
									
					String startTimeText = rs.getString("RES_START_TIME");
					if(startTimeText == null || startTimeText.length() == 0){
						//no timeslot defined.
						continue;
					}
					TimeOfDay startTime = new TimeOfDay(startTimeText);
					TimeOfDay endTime = new TimeOfDay(rs.getString("RES_END_TIME"));
	
					int dayOfWeek = rs.getInt("DETAIL_DAY_OF_WEEK");
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
				}
			}
		}
		if(restrictionId.length() > 0) {
			//Add the last one.
			CompositeRestriction restriction = new CompositeRestriction(restrictionId, criterion, reason, name, msg, new DateRange(startDate, endDate),type,path);
			restriction.setTimeRangeMap(new HashMap<Integer, List<TimeOfDayRange>>(timeRangeMap));
			restrictions.add(restriction);
		}
		rs.close();
		ps.close();
		//Add the the alcohol restrictions.
		restrictions.addAll(getAlcoholRestrictions(conn));
		return restrictions;
	}
	
	private static final String GET_ALCOHOL_RESTRICTIONS = "select  r.ID,r.TYPE,r.NAME,r.START_TIME,r.END_TIME,r.REASON,r.MESSAGE,r.CRITERION,r.MEDIA_PATH, "+
										"D.DAY_OF_WEEK, D.RES_START_TIME,D.RES_END_TIME, M.ID MUNICIPALITY_ID,M.STATE,M.COUNTY,M.CITY,M.ALCOHOL_RESTRICTED "+ 
										"from CUST.restricted_days r,CUST.RESTRICTION_DETAIL d, CUST.MUNICIPALITY_INFO m, CUST.MUNICIPALITY_RESTRICTION_DATA mr "+
										"where R.ID = D.RESTRICTION_ID(+) "+
										"and R.ID = MR.RESTRICTION_ID "+
										"and M.ID = MR.MUNICIPALITY_ID " +
										"and R.REASON IN ('ACL','WIN','BER') " +
										"and M.ALCOHOL_RESTRICTED is NULL ORDER BY R.ID";
	private static List<AlcoholRestriction> getAlcoholRestrictions(Connection conn) throws SQLException {
		List<AlcoholRestriction> restrictions=new ArrayList<AlcoholRestriction>();		

		PreparedStatement ps = conn.prepareStatement(GET_ALCOHOL_RESTRICTIONS);

		String restrictionId = "";
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
		String municipalityId = null;
		boolean alcoholRestricted = false;

		ResultSet rs = ps.executeQuery();
		Map<Integer, List<TimeOfDayRange>> timeRangeMap = new HashMap<Integer, List<TimeOfDayRange>>();
		while (rs.next()) {
			if(restrictionId.length() == 0 || restrictionId.equals(rs.getString("ID"))){
				restrictionId = rs.getString("ID");
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
			} else {
				AlcoholRestriction restriction = new AlcoholRestriction(restrictionId, criterion, reason, name, msg, startDate, endDate,type,
						path, state, county, null, municipalityId, alcoholRestricted);
				restriction.setTimeRangeMap(new HashMap<Integer, List<TimeOfDayRange>>(timeRangeMap));
				restrictions.add(restriction);
				timeRangeMap.clear();
				restrictionId = rs.getString("ID");
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

			}
		}
		if(restrictionId.length() > 0) {
			//Add the last one.
			AlcoholRestriction restriction = new AlcoholRestriction(restrictionId, criterion, reason, name, msg, startDate, endDate,type,
					path, state, county, null, municipalityId, alcoholRestricted);
			restriction.setTimeRangeMap(new HashMap<Integer, List<TimeOfDayRange>>(timeRangeMap));
			restrictions.add(restriction);
		}
		
		LOGGER.debug("restrictions size :"+restrictions.size());
		rs.close();
		ps.close();

		return restrictions;
	}	

	

	public static boolean isAlcoholDeliverable(Connection conn,
			String scrubbedAddress, String zipcode, String apartment) throws SQLException {

		PreparedStatement ps = conn
				.prepareStatement("SELECT * FROM CUST.RESTRICTED_ADDRESS WHERE SCRUBBED_ADDRESS = ? AND APARTMENT = ? AND ZIPCODE = ? AND REASON = ?");
		ps.setString(1, scrubbedAddress);
		if (apartment == null || "".equals(apartment)) {
			ps.setNull(2, Types.VARCHAR);
		} else {
			ps.setString(2, apartment);
		}
		ps.setString(3, zipcode);
		ps.setString(4, EnumRestrictedAddressReason.ALCOHOL.getCode());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return false;
		}

		rs.close();
		ps.close();

		return true;
	}

	public static EnumRestrictedAddressReason isAddressRestricted(
			Connection conn, AddressModel address) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn
					.prepareStatement("SELECT APARTMENT, REASON FROM CUST.RESTRICTED_ADDRESS WHERE SCRUBBED_ADDRESS = ? AND ZIPCODE = ? and REASON <> ?");
			ps.setString(1, address.getScrubbedStreet());
			ps.setString(2, address.getZipCode());
			ps.setString(3, EnumRestrictedAddressReason.ALCOHOL.getCode());
			rs = ps.executeQuery();
			while (rs.next()) {
				String apt = rs.getString("APARTMENT");
				if (rs.wasNull() || "".equals(apt)) {
					String reason = rs.getString("REASON");
					return EnumRestrictedAddressReason
							.getRestrictionReason(reason);
				}

				if (apt.equalsIgnoreCase(address.getApartment())) {
					String reason = rs.getString("REASON");
					return EnumRestrictedAddressReason
							.getRestrictionReason(reason);
				}
			}
			return EnumRestrictedAddressReason.NONE;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
	}

}
