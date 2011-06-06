package com.freshdirect.delivery.restriction.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.ejb.DlvManagerDAO;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.GeographyRestrictedDay;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvRestrictionDAO {

	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");
	
	private static Category LOGGER = LoggerFactory.getInstance(DlvRestrictionDAO.class);
	
	public static List<RestrictionI> getDlvRestrictions(Connection conn) throws SQLException {

		PreparedStatement ps = conn
			.prepareStatement("select id,criterion, type, name, message, day_of_week, start_time, end_time, reason, media_path from dlv.restricted_days " +
					"where reason not in ('WIN','BER','ACL')");
		ResultSet rs = ps.executeQuery();
		List<RestrictionI> restrictions = new ArrayList<RestrictionI>();
		while (rs.next()) {

			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String msg = rs.getString("MESSAGE");
			String mediaPath = rs.getString("MEDIA_PATH");
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

			Date startDate = new Date(rs.getTimestamp("START_TIME").getTime());
			Date endDate = new Date(rs.getTimestamp("END_TIME").getTime());
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
					restrictions.add(new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate,mediaPath));
				} else {
					restrictions.add(new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate,mediaPath));
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restrictions.add(new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime, mediaPath));

			} else {
				// ignore	
			}

		}
		rs.close();
		ps.close();
		//Add the the alcohol restrictions.
		restrictions.addAll(getAlcoholRestrictions(conn));
		return restrictions;
	}
	
	private static final String GET_ALCOHOL_RESTRICTIONS = "select  r.ID,r.TYPE,r.NAME,r.START_TIME,r.END_TIME,r.REASON,r.MESSAGE,r.CRITERION,r.MEDIA_PATH, "+
										"D.DAY_OF_WEEK, D.RES_START_TIME,D.RES_END_TIME, M.ID MUNICIPALITY_ID,M.STATE,M.COUNTY,M.CITY,M.ALCOHOL_RESTRICTED "+ 
										"from dlv.restricted_days r,DLV.RESTRICTION_DETAIL d, DLV.MUNICIPALITY_INFO m, DLV.MUNICIPALITY_RESTRICTION_DATA mr "+
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

	private static final String GEOGRAPHY_RESTRICTION = "select gr.RESTRICTION_ID ID, gr.NAME NAME, ACTIVE, COMMENTS, MESSAGE, SERVICE_TYPE, SHOW_MESSAGE" +
			",START_DATE, END_DATE,  DAY_OF_WEEK, CONDITION, START_TIME, END_TIME  " +
			"from dlv.GEO_RESTRICTION gr, dlv.GEO_RESTRICTION_BOUNDARY gb , dlv.GEO_RESTRICTION_DAYS gd " +
			"where gr.BOUNDARY_CODE = gb.code and gr.RESTRICTION_ID = gd.RESTRICTION_ID and gr.ACTIVE = 'X' and (gr.SERVICE_TYPE = ? or gr.SERVICE_TYPE is null) " +
			"and mdsys.sdo_relate(gb.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?,?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE'";
//	select * from dlv.GEO_RESTRICTION_BOUNDARY gr where mdsys.sdo_relate(gr.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(-73.952006,40.59712,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE'
	//1910 AVE V  	11229  	40.59712  	-73.952006
	public static List<GeographyRestriction> getGeographicDlvRestrictions(Connection conn, AddressModel address,String query) throws SQLException {
		
		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			try {
				DlvManagerDAO.geocodeAddress(conn, address, false);
			} catch (Exception e) {
				// If Invalid Address Exception or SQL Exception was thrown it would have never reached this section
				// So this error can be safely ignored
				e.printStackTrace();
			}
		}
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, address.getServiceType().getName());
		ps.setDouble(2, address.getLongitude());
		ps.setDouble(3, address.getLatitude());
		
		ResultSet rs = ps.executeQuery();
		List<GeographyRestriction> restrictions = new ArrayList<GeographyRestriction>();
		GeographyRestriction restriction = null;
		GeographyRestrictedDay restrictedDay = null;
		EnumLogicalOperator condition = null;
		int dayOfWeek = -1;
		//Date startDate = null;
		//Date endDate = null;
		while (rs.next()) {			
			if(restriction == null || !rs.getString("ID").equalsIgnoreCase(restriction.getId())) {
				restriction = new GeographyRestriction();
				restriction.setId(rs.getString("ID"));
				restriction.setName(rs.getString("NAME"));
				restriction.setActive(rs.getString("ACTIVE"));
				restriction.setComments(rs.getString("COMMENTS"));
				restriction.setMessage(rs.getString("MESSAGE"));
				restriction.setShowMessage(rs.getString("SHOW_MESSAGE"));
				restriction.setServiceType(rs.getString("SERVICE_TYPE"));
				restriction.setRange(new Date(rs.getTimestamp("START_DATE").getTime()), new Date(rs.getTimestamp("END_DATE").getTime()));
				restrictions.add(restriction);
			}
			
			restrictedDay = new GeographyRestrictedDay();
			dayOfWeek = rs.getInt("DAY_OF_WEEK");
			restrictedDay.setDayOfWeek(dayOfWeek);
			condition = EnumLogicalOperator.getEnum(rs.getString("CONDITION"));
			if(condition != null) {
				restrictedDay.setCondition(condition);
				if(rs.getTimestamp("START_TIME") != null) {
					//startDate = new Date(rs.getTimestamp("START_TIME").getTime());
					restrictedDay.setStartTime(new TimeOfDay(rs.getTime("START_TIME")));
				}
				if(rs.getTimestamp("END_TIME") != null) {
					//endDate = new Date(rs.getTimestamp("END_TIME").getTime());
					restrictedDay.setEndTime(new TimeOfDay(rs.getTime("END_TIME")));
				}								
			}
			restriction.setRestrictedDay(dayOfWeek, restrictedDay);
		}

		rs.close();
		ps.close();

		return restrictions;
	}
	
	public static List<GeographyRestriction> getGeographicDlvRestrictionsForReservation(Connection conn, AddressModel address) throws SQLException {
		String query = GEOGRAPHY_RESTRICTION;
		query = query+ " and (gr.view_type is null or gr.view_type = 'RSV')";
		return getGeographicDlvRestrictions(conn, address, query);
	}
	
	public static List<GeographyRestriction> getGeographicDlvRestrictionsForAvailable(Connection conn, AddressModel address) throws SQLException {
		String query = GEOGRAPHY_RESTRICTION;
		query = query+ " and (gr.view_type is null or gr.view_type = 'AVL')";
		return getGeographicDlvRestrictions(conn, address, query);
	}
	
	public static List<GeographyRestriction> getGeographicDlvRestrictions(Connection conn, AddressModel address) throws SQLException {
		String query = GEOGRAPHY_RESTRICTION;
		query = query+ " and (gr.view_type is null or gr.view_type = 'CHK')";
		return getGeographicDlvRestrictions(conn, address, query);
	}

}