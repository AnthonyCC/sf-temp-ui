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
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.model.RestrictedAddressModel;
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
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvRestrictionDAO {

	private static Category LOGGER = LoggerFactory.getInstance(DlvRestrictionDAO.class);
	
	private static String DELIVERY_RESTRICTIONS_RETURN = 
		"select ID,TYPE,NAME,DAY_OF_WEEK,START_TIME,END_TIME,REASON,MESSAGE,CRITERION FROM dlv.restricted_days where id=?";
	
	public static RestrictionI getDlvRestriction(Connection conn, String restrictionId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELIVERY_RESTRICTIONS_RETURN);									
		ps.setString(1, restrictionId);				
		RestrictionI restriction=null;
		ResultSet rs = ps.executeQuery();
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
					restriction=new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate);
				} else {
					restriction=new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate);
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restriction=new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime);

			} else {
				// ignore	
			}
		}

		LOGGER.debug("restriction :"+restriction);
		rs.close();
		ps.close();

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
					restriction=new OneTimeReverseRestriction(id,criterion, reason, name, msg, startDate, endDate);
				} else {
					restriction=new OneTimeRestriction(id,criterion, reason, name, msg, startDate, endDate);
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(type)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restriction=new RecurringRestriction(id,criterion, reason, name, msg, dayOfWeek, startTime, endTime);

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
	
	private static final String DELIVERY_RESTRICTIONS_UPDATE="update dlv.restricted_days set type=?, NAME= ?, REASON=?, MESSAGE=?, CRITERION=?, DAY_OF_WEEK=? , START_TIME=?, END_TIME=? where ID=?";
	
	public static void updateDeliveryRestriction(Connection conn, RestrictionI restriction) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELIVERY_RESTRICTIONS_UPDATE);
												
		ps.setString(1, restriction.getType().getName());
		ps.setString(2, restriction.getName());		
		ps.setString(3, restriction.getReason().getName());
		ps.setString(4, restriction.getMessage());
		ps.setString(5, restriction.getCriterion().getName());
		if(restriction instanceof OneTimeReverseRestriction){
			OneTimeReverseRestriction otrRestriction=(OneTimeReverseRestriction)restriction;
			ps.setNull(6,Types.INTEGER);
			ps.setDate(7,new java.sql.Date(otrRestriction.getDateRange().getStartDate().getTime()));			
			ps.setTimestamp(8,new Timestamp(otrRestriction.getDateRange().getEndDate().getTime()));
		}else if(restriction instanceof OneTimeRestriction){
			OneTimeRestriction otRestriction=(OneTimeRestriction)restriction;
			ps.setNull(6,Types.INTEGER);
			ps.setDate(7,new java.sql.Date(otRestriction.getDateRange().getStartDate().getTime()));
			LOGGER.debug("otRestriction.getDateRange().getEndDate() :"+otRestriction.getDateRange().getEndDate());
			ps.setTimestamp(8,new Timestamp(otRestriction.getDateRange().getEndDate().getTime()));
		}else if(restriction instanceof RecurringRestriction){
			RecurringRestriction rRestriction=(RecurringRestriction)restriction;
			ps.setInt(6,rRestriction.getDayOfWeek());			
			Date newStartDate=null;
			Date newEndDate=null;
			try {
				newStartDate=format.parse(DEFAULT_RECURRING_DATE+rRestriction.getTimeRange().getStartTime());
				newEndDate=format.parse(DEFAULT_RECURRING_DATE+rRestriction.getTimeRange().getEndTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				LOGGER.warn(e);				
			}			
			ps.setTimestamp(7,new Timestamp(newStartDate.getTime()));
			ps.setTimestamp(8,new Timestamp(newEndDate.getTime()));			
		}
		ps.setString(9, restriction.getId());		
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
	
	private static final String DELIVERY_RESTRICTION_INSERT="insert into dlv.restricted_days(ID,TYPE,NAME,REASON,MESSAGE,CRITERION,DAY_OF_WEEK,START_TIME,END_TIME)"+ 
                                                            "values(?,?,?,?,?,?,?,?,?)";
	
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
	}
