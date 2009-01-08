package com.freshdirect.delivery.restriction.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.GeographyRestrictedDay;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;

public class DlvRestrictionDAO {

	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");

	public static List getDlvRestrictions(Connection conn) throws SQLException {

		PreparedStatement ps = conn
			.prepareStatement("select id,criterion, type, name, message, day_of_week, start_time, end_time, reason from dlv.restricted_days");
		ResultSet rs = ps.executeQuery();
		List restrictions = new ArrayList();
		while (rs.next()) {

			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String msg = rs.getString("MESSAGE");
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
					restrictions.add(new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate));
				} else {
					restrictions.add(new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate));
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restrictions.add(new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime));

			} else {
				// ignore	
			}

		}

		rs.close();
		ps.close();

		return restrictions;
	}
	private static final String GEOGRAPHY_RESTRICTION = "select ID, gr.NAME NAME, INACTIVE, COMMENTS, MESSAGE,START_DATE, END_DATE,  DAY_OF_WEEK, CONDITION, START_TIME, END_TIME  from dlv.GEO_RESTRICTION gr, dlv.GEO_RESTRICTION_BOUNDARY gb , dlv.GEO_RESTRICTION_DAYS gd where gr.BOUNDARY_CODE = gb.code and gr.ID = gd.RESTRICTION_ID and (gr.INACTIVE is null or gr.INACTIVE <> 'X') and mdsys.sdo_relate(gb.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?,?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE'";
//	select * from dlv.GEO_RESTRICTION_BOUNDARY gr where mdsys.sdo_relate(gr.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(-73.952006,40.59712,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE'
	//1910 AVE V  	11229  	40.59712  	-73.952006
	public static GeographyRestriction getGeographicDlvRestrictions(Connection conn, AddressModel address) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(GEOGRAPHY_RESTRICTION);				
		ps.setDouble(1, address.getLongitude());
		ps.setDouble(2, address.getLatitude());
		
		ResultSet rs = ps.executeQuery();
		GeographyRestriction restriction = null;
		GeographyRestrictedDay restrictedDay = null;
		EnumLogicalOperator condition = null;
		int dayOfWeek = -1;
		Date startDate = null;
		Date endDate = null;
		while (rs.next()) {			
			if(restriction == null) {
				restriction = new GeographyRestriction();
				restriction.setId(rs.getString("ID"));
				restriction.setName(rs.getString("NAME"));
				restriction.setIsActive(rs.getString("INACTIVE"));
				restriction.setComments(rs.getString("COMMENTS"));
				restriction.setMessage(rs.getString("MESSAGE"));
				restriction.setRange(new Date(rs.getTimestamp("START_DATE").getTime()), new Date(rs.getTimestamp("END_DATE").getTime()));
			}
			
			restrictedDay = new GeographyRestrictedDay();
			dayOfWeek = rs.getInt("DAY_OF_WEEK");
			restrictedDay.setDayOfWeek(dayOfWeek);
			condition = EnumLogicalOperator.getEnum(rs.getString("CONDITION"));
			if(condition != null) {
				restrictedDay.setCondition(condition);
				if(rs.getTimestamp("START_TIME") != null) {
					startDate = new Date(rs.getTimestamp("START_TIME").getTime());
					restrictedDay.setStartTime(new TimeOfDay(startDate));
				}
				if(rs.getTimestamp("END_TIME") != null) {
					endDate = new Date(rs.getTimestamp("END_TIME").getTime());
					restrictedDay.setEndTime(new TimeOfDay(endDate));
				}								
			}
			restriction.setRestrictedDay(dayOfWeek, restrictedDay);
		}

		rs.close();
		ps.close();

		return restriction;
	}

}