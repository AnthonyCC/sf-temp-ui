package com.freshdirect.routing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.dao.IRoutingInfoDAO;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingEquipmentType;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.model.RegionModel;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingEquipmentType;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.routing.model.ServiceTimeScenario;
import com.freshdirect.routing.model.ServiceTimeTypeModel;
import com.freshdirect.routing.model.TrnFacility;
import com.freshdirect.routing.model.TrnFacilityType;
import com.freshdirect.routing.model.WaveInstance;
import com.freshdirect.routing.model.WaveSyncLockActivity;
import com.freshdirect.routing.model.ZoneScenarioModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class RoutingInfoDAO extends BaseDAO implements IRoutingInfoDAO   {

    private static final String GET_SCENARIOS_QRY = "SELECT * FROM DLV.SERVICETIME_SCENARIO";
	
	private static final String GET_SERVICETIMETYPE_QRY = "SELECT * FROM DLV.SERVICETIME_TYPE";
	
	private static final String UPDATE_REROUTERESERVATION_BYDATE = "UPDATE DLV.RESERVATION r SET r.DO_REROUTE = 'X' WHERE r.ID in " +
			"(SELECT rx.ID from dlv.reservation rx, dlv.timeslot t, dlv.zone z where t.BASE_DATE = ? " +
			" and rx.STATUS_CODE in ('5','10') and (rx.UNASSIGNED_ACTION is null or rx.UNASSIGNED_ACTION <> 'RESERVE_TIMESLOT') and rx.TIMESLOT_ID = t.ID and t.ZONE_ID = z.ID)";
	
	private static final String UPDATE_REROUTERESERVATION_BYDATEZONE = "UPDATE DLV.RESERVATION r SET r.DO_REROUTE = 'X' WHERE r.ID in " +
	"(SELECT rx.ID from dlv.reservation rx, dlv.timeslot t, dlv.zone z where t.BASE_DATE = ? " +
	" and rx.STATUS_CODE in ('5','10') and (rx.UNASSIGNED_ACTION is null or rx.UNASSIGNED_ACTION <> 'RESERVE_TIMESLOT') and rx.TIMESLOT_ID = t.ID and t.ZONE_ID = z.ID and z.ZONE_CODE in (";
	
	public Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws SQLException {
		
		final Map<String, IServiceTimeTypeModel> results = new HashMap<String, IServiceTimeTypeModel>();
		jdbcTemplate.query(GET_SERVICETIMETYPE_QRY, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		
				    		IServiceTimeTypeModel tmpModel = new ServiceTimeTypeModel();				    		
				    		tmpModel.setCode(rs.getString("CODE"));
				    		tmpModel.setName(rs.getString("NAME"));
				    		tmpModel.setDescription(rs.getString("DESCRIPTION"));
				    		tmpModel.setFixedServiceTime(rs.getDouble("FIXED_SERVICE_TIME"));
				    		tmpModel.setStopServiceTime(rs.getDouble("STOP_SERVICE_TIME"));
				    		tmpModel.setVariableServiceTime(rs.getDouble("VARIABLE_SERVICE_TIME"));
				    		
				    		results.put(tmpModel.getCode(), tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return results;
	}
	
	public Collection getRoutingScenarios()  throws SQLException {
		final Collection scenarios = new ArrayList();
		jdbcTemplate.query(GET_SCENARIOS_QRY, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		
				    		IServiceTimeScenarioModel tmpModel = new ServiceTimeScenario();				    		
				    		tmpModel.setCode(rs.getString("CODE"));				    		
				    		tmpModel.setDefaultCartonCount(rs.getDouble("DEFAULT_CARTONCOUNT"));				    		
				    		tmpModel.setDefaultCaseCount(rs.getDouble("DEFAULT_CASECOUNT"));				    		
				    		tmpModel.setDefaultFreezerCount(rs.getDouble("DEFAULT_FREEZERCOUNT"));				    		
				    						    		
				    		tmpModel.setDescription(rs.getString("DESCRIPTION"));				    		
				    		tmpModel.setIsDefault(rs.getString("IS_DEFAULT"));				    		
				    		tmpModel.setOrderSizeFormula(rs.getString("ORDERSIZE_FORMULA"));				    		
				    		tmpModel.setServiceTimeFactorFormula(rs.getString("SERVICETIME_FACTOR_FORMULA"));				    		
				    		tmpModel.setServiceTimeFormula(rs.getString("SERVICETIME_FORMULA"));
				    		
				    		tmpModel.setBalanceBy(rs.getString("BALANCE_BY"));
				    		tmpModel.setLoadBalanceFactor(rs.getDouble("LOADBALANCE_FACTOR"));
				    		tmpModel.setLateDeliveryFactor(rs.getDouble("LATEDELIVERY_FACTOR"));
				    		tmpModel.setNeedsLoadBalance("X".equalsIgnoreCase(rs.getString("NEEDS_LOADBALANCE")) ? true : false);
				    		tmpModel.setBulkThreshold(rs.getDouble("BULK_THRESHOLD"));
				    		scenarios.add(tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return scenarios;
	}
	
	private static final String GET_SCENARIOBYCODE_QRY = "SELECT * FROM DLV.SERVICETIME_SCENARIO S WHERE S.CODE = ?";
	
	private static final String GET_SCENARIOBYDATE_QRY = "SELECT S.* FROM DLV.SERVICETIME_SCENARIO S,  DLV.SCENARIO_DAYS D " +
			"where S.CODE = D.SCENARIO_CODE " +
			"and ((D.SCENARIO_DATE = ? or D.DAY_OF_WEEK = TO_CHAR(?, 'D')) " +
			"or (D.SCENARIO_DATE is null and D.DAY_OF_WEEK is null)) " +
			"and cutoff_time is null and start_time is null and end_time is null " +
			"ORDER BY SCENARIO_DATE, DAY_OF_WEEK NULLS LAST";            
	
	public IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws SQLException {
		
		final List<IServiceTimeScenarioModel> results = new ArrayList<IServiceTimeScenarioModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
				PreparedStatement ps =
					connection.prepareStatement(GET_SCENARIOBYDATE_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
				
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		results.add(getScenarioFromResultSet(rs));
				    		break;
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return results.size() > 0 ? results.get(0) : null;
	}
	
	private static final String GET_SCENARIO_QRY = "SELECT S.* FROM DLV.SERVICETIME_SCENARIO S,  DLV.SCENARIO_DAYS D " +
			"where S.CODE = D.SCENARIO_CODE " +
			"and ((D.SCENARIO_DATE = ? or D.DAY_OF_WEEK = TO_CHAR(?, 'D')) " +
			"or (D.SCENARIO_DATE is null and D.DAY_OF_WEEK is null)) " +
			"and  ((D.CUTOFF_TIME = ?  or (D.START_TIME <= ? AND D.END_TIME >= ?)) " +
			" or (D.CUTOFF_TIME  is null and D.START_TIME is null and D.END_TIME is null)) ORDER BY SCENARIO_DATE, DAY_OF_WEEK, START_TIME, END_TIME, CUTOFF_TIME NULLS LAST";            
	
	public IServiceTimeScenarioModel getRoutingScenarioEx(final Date deliveryDate, final Date cutoff,final Date startTime,final Date endTime)  throws SQLException {
		
		final List<IServiceTimeScenarioModel> results = new ArrayList<IServiceTimeScenarioModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
				PreparedStatement ps =
					connection.prepareStatement(GET_SCENARIO_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(3, new java.sql.Timestamp(cutoff.getTime()));
				ps.setTimestamp(4, new java.sql.Timestamp(startTime.getTime()));
				ps.setTimestamp(5, new java.sql.Timestamp(endTime.getTime()));
				
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		results.add(getScenarioFromResultSet(rs));
				    		break;
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return results.size() > 0 ? results.get(0) : null;
	}
	
	
	public IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws SQLException {
		
		final List<IServiceTimeScenarioModel> results = new ArrayList<IServiceTimeScenarioModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
				PreparedStatement ps =
					connection.prepareStatement(GET_SCENARIOBYCODE_QRY);
				ps.setString(1,code);
				
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		results.add(getScenarioFromResultSet(rs));	
				    		break;
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return results.size() > 0 ? results.get(0) : null;
	}
	
	private IServiceTimeScenarioModel getScenarioFromResultSet(ResultSet rs)  throws SQLException {
		
		IServiceTimeScenarioModel tmpModel = new ServiceTimeScenario();
		
		tmpModel.setCode(rs.getString("CODE"));				    		
		tmpModel.setDefaultCartonCount(rs.getDouble("DEFAULT_CARTONCOUNT"));				    		
		tmpModel.setDefaultCaseCount(rs.getDouble("DEFAULT_CASECOUNT"));				    		
		tmpModel.setDefaultFreezerCount(rs.getDouble("DEFAULT_FREEZERCOUNT"));				    		
					    		
		tmpModel.setDescription(rs.getString("DESCRIPTION"));				    		
		tmpModel.setIsDefault(rs.getString("IS_DEFAULT"));				    		
		tmpModel.setOrderSizeFormula(rs.getString("ORDERSIZE_FORMULA"));				    		
		tmpModel.setServiceTimeFactorFormula(rs.getString("SERVICETIME_FACTOR_FORMULA"));				    		
		tmpModel.setServiceTimeFormula(rs.getString("SERVICETIME_FORMULA"));
		
		tmpModel.setBalanceBy(rs.getString("BALANCE_BY"));
		tmpModel.setLoadBalanceFactor(rs.getDouble("LOADBALANCE_FACTOR"));
		tmpModel.setLateDeliveryFactor(rs.getDouble("LATEDELIVERY_FACTOR"));
		tmpModel.setNeedsLoadBalance("X".equalsIgnoreCase(rs.getString("NEEDS_LOADBALANCE")) ? true : false);
		
		tmpModel.setDefaultTrailerContainerCount(rs.getInt("TRAILER_CONTAINERMAX"));
		tmpModel.setDefaultContainerCartonCount(rs.getInt("TRAILER_CONTAINERCARTONMAX"));
		tmpModel.setBulkThreshold(rs.getDouble("BULK_THRESHOLD"));

		return tmpModel;
	}
	
	private static final String GET_SCENARIO_ZONEMAPPING = "select DZ.ZONE_CODE " +
			", DZ.SERVICETIME_TYPE , DZ.SERVICETIME_OVERRIDE , DZ.SERVICETIME_OPERATOR , DZ.SERVICETIME_ADJUSTMENT, " +
			"FIXED_SERVICE_TIME ,VARIABLE_SERVICE_TIME, STOP_SERVICE_TIME " +
			"FROM DLV.SCENARIO_ZONES DZ, DLV.SERVICETIME_TYPE ST where DZ.CODE = ? and DZ.SERVICETIME_TYPE = ST.CODE(+)";
	
	public Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws SQLException {
		
		final Map<String, IZoneScenarioModel> results = new HashMap<String, IZoneScenarioModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
				PreparedStatement ps =
					connection.prepareStatement(GET_SCENARIO_ZONEMAPPING);
				ps.setString(1,code);
				
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {
				    		IZoneScenarioModel model = new ZoneScenarioModel();				    						    		
				    		model.setZone(rs.getString("ZONE_CODE"));
				    		
				    		String serviceTimeType = rs.getString("SERVICETIME_TYPE");
				    		if(serviceTimeType != null) {
					    		IServiceTimeTypeModel locServiceTimeType = new ServiceTimeTypeModel();
					    		locServiceTimeType.setCode( serviceTimeType);
					    		locServiceTimeType.setFixedServiceTime(rs.getDouble("FIXED_SERVICE_TIME"));
					    		locServiceTimeType.setStopServiceTime(rs.getDouble("STOP_SERVICE_TIME"));
					    		locServiceTimeType.setVariableServiceTime(rs.getDouble("VARIABLE_SERVICE_TIME"));
					    		model.setServiceTimeType(locServiceTimeType);
				    		}    						    		
				    		
				    		model.setAdjustmentOperator(EnumArithmeticOperator.getEnum(rs.getString("SERVICETIME_OPERATOR")));
				    		model.setServiceTimeAdjustment(rs.getDouble("SERVICETIME_ADJUSTMENT"));
				    		model.setServiceTimeOverride(rs.getDouble("SERVICETIME_OVERRIDE"));
				    		results.put(model.getZone(), model);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return results;
	}
	
	public int flagReRouteReservation(final Date deliveryDate, final List<String> zones) throws SQLException {

		Connection connection=null;
		int result = 0;
		
		try{
			if(zones == null || zones.size() == 0) {
				
				result = this.jdbcTemplate.update(UPDATE_REROUTERESERVATION_BYDATE, new Object[] {deliveryDate});
			} else {
				StringBuffer updateQ = new StringBuffer();
				updateQ.append(UPDATE_REROUTERESERVATION_BYDATEZONE);
				int intCount = 0;
				for(String zone : zones) {
					updateQ.append("'").append(zone).append("'");
					intCount++;
					if(intCount != zones.size()) {
						updateQ.append(",");
					}
				}
				updateQ.append(") )");
				result = this.jdbcTemplate.update(updateQ.toString(), new Object[] {deliveryDate});
			}			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	private static final String GET_WAVEINSTANCE_BYSTATUS_QRY = "select P.DELIVERY_DATE DISPATCH_DATE, F.FACILITY_CODE ORIGIN_FACILITY, P.AREA ZONE, P.CUTOFF_DATETIME CUT_OFF " +
	", P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME, P.RESOURCE_COUNT, P.FORCE_SYNCHRONIZE, P.REFERENCE_ID, P.WAVEINSTANCE_ID, P.SOURCE, P.STATUS, P.NOTIFICATION_MSG " +
	", Z.PRETRIP_TIME, Z.POSTTRIP_TIME, Z.LOADING_PRIORITY, TR.IS_DEPOT IS_DEPOT, P.EQUIPMENT_TYPE, P.TODRESTRICTION, TR.CODE REGION  " +
	"from transp.WAVE_INSTANCE p, transp.zone z, transp.trn_area a, TRANSP.TRN_REGION TR, transp.trn_facility f where P.DELIVERY_DATE = ?  and P.AREA = Z.ZONE_CODE and z.AREA = a.CODE AND TR.CODE = A.REGION_CODE and f.ID = P.ORIGIN_FACILITY and STATUS = ? " +
	"order by P.DELIVERY_DATE, P.AREA, P.CUTOFF_DATETIME, P.TRUCK_DISPATCHTIME";
	
	private static final String GET_WAVEINSTANCE_QRY = "select P.DELIVERY_DATE DISPATCH_DATE, F.FACILITY_CODE ORIGIN_FACILITY, P.AREA ZONE, P.CUTOFF_DATETIME CUT_OFF " +
	", P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME, P.RESOURCE_COUNT, P.FORCE_SYNCHRONIZE, P.REFERENCE_ID, P.WAVEINSTANCE_ID, P.SOURCE, P.STATUS, P.NOTIFICATION_MSG " +
	", Z.PRETRIP_TIME, Z.POSTTRIP_TIME, Z.LOADING_PRIORITY, TR.IS_DEPOT IS_DEPOT, P.EQUIPMENT_TYPE, P.TODRESTRICTION, TR.CODE REGION " +
	"from transp.WAVE_INSTANCE p, transp.zone z, transp.trn_area a,  TRANSP.TRN_REGION TR, transp.trn_facility f where P.DELIVERY_DATE = ?  and P.AREA = Z.ZONE_CODE and z.AREA = a.CODE AND TR.CODE = A.REGION_CODE and f.ID = P.ORIGIN_FACILITY " +
	"order by P.DELIVERY_DATE, P.AREA, P.CUTOFF_DATETIME, P.TRUCK_DISPATCHTIME";
	
	private static final String GET_FUTURE_WAVEINSTANCE_QRY = "select P.DELIVERY_DATE DISPATCH_DATE, F.FACILITY_CODE ORIGIN_FACILITY, P.AREA ZONE, P.CUTOFF_DATETIME CUT_OFF " +
	", P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME, P.RESOURCE_COUNT, P.FORCE_SYNCHRONIZE, P.REFERENCE_ID, P.WAVEINSTANCE_ID, P.SOURCE, P.STATUS, P.NOTIFICATION_MSG " +
	", Z.PRETRIP_TIME, Z.POSTTRIP_TIME, Z.LOADING_PRIORITY, TR.IS_DEPOT IS_DEPOT, P.EQUIPMENT_TYPE, P.TODRESTRICTION, TR.CODE REGION  " +
	" from transp.WAVE_INSTANCE p, transp.zone z, transp.trn_area a, TRANSP.TRN_REGION TR, transp.trn_facility f where P.DELIVERY_DATE > sysdate  and P.AREA = Z.ZONE_CODE and z.AREA = a.CODE AND TR.CODE = A.REGION_CODE and f.ID = P.ORIGIN_FACILITY " +
	"order by P.DELIVERY_DATE, P.AREA, P.CUTOFF_DATETIME, P.TRUCK_DISPATCHTIME";
	
	private static final String GET_FUTURE_WAVEINSTANCE_BYSTATUS_QRY = "select P.DELIVERY_DATE DISPATCH_DATE, F.FACILITY_CODE ORIGIN_FACILITY, P.AREA ZONE, P.CUTOFF_DATETIME CUT_OFF " +
	", P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME, P.RESOURCE_COUNT, P.FORCE_SYNCHRONIZE, P.REFERENCE_ID, P.WAVEINSTANCE_ID, P.SOURCE, P.STATUS, P.NOTIFICATION_MSG " +
	", Z.PRETRIP_TIME, Z.POSTTRIP_TIME, Z.LOADING_PRIORITY, TR.IS_DEPOT IS_DEPOT, P.EQUIPMENT_TYPE, P.TODRESTRICTION, TR.CODE REGION  " +
	" from transp.WAVE_INSTANCE p, transp.zone z, transp.trn_area a,TRANSP.TRN_REGION TR, transp.trn_facility f where P.DELIVERY_DATE > sysdate  and P.AREA = Z.ZONE_CODE and z.AREA = a.CODE AND TR.CODE = A.REGION_CODE  and f.ID = P.ORIGIN_FACILITY and STATUS = ? " +
	"order by P.DELIVERY_DATE, P.AREA, P.CUTOFF_DATETIME, P.TRUCK_DISPATCHTIME";
	
	public Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> getWaveInstanceTree
																							(final Date deliveryDate, final  EnumWaveInstanceStatus status)  throws SQLException {
		final Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> result = new HashMap<Date, Map<String
																, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = null;
				if(deliveryDate != null) {
					if(status == null) {
						ps = connection.prepareStatement(GET_WAVEINSTANCE_QRY);
						ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
					} else {
						ps = connection.prepareStatement(GET_WAVEINSTANCE_BYSTATUS_QRY);
						ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
						ps.setString(2, status.getName());
					}		
				} else {
					if(status == null) {
						ps = connection.prepareStatement(GET_FUTURE_WAVEINSTANCE_QRY);						
					} else {
						ps = connection.prepareStatement(GET_FUTURE_WAVEINSTANCE_BYSTATUS_QRY);						
						ps.setString(1, status.getName());
					}
				}
				return ps;
			}
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {
					Date _dispatchDate = rs.getDate("DISPATCH_DATE");
					Date _startTime = rs.getTimestamp("TRUCK_DISPATCHTIME");
					Date _endTime = rs.getTimestamp("TRUCK_ENDTIME");
					Date _maxTime = rs.getTimestamp("MAX_TIME");
					
					RoutingTimeOfDay _cutOffTime = new RoutingTimeOfDay(rs.getTimestamp("CUT_OFF"));

					String _zoneCode = rs.getString("ZONE");
					String _routingWaveInstanceId = rs.getString("REFERENCE_ID");
					String _waveInstanceId = rs.getString("WAVEINSTANCE_ID"); 
					EnumWaveInstanceStatus status = EnumWaveInstanceStatus.getEnum(rs.getString("STATUS"));
					
					int preTripTime = rs.getInt("PRETRIP_TIME");
					int postTripTime = rs.getInt("POSTTRIP_TIME");
					int noOfResources = rs.getInt("RESOURCE_COUNT");
					boolean force = rs.getString("FORCE_SYNCHRONIZE") != null 
										? "Y".equalsIgnoreCase(rs.getString("FORCE_SYNCHRONIZE")) : false;
					boolean needsConsolidation = rs.getString("IS_DEPOT") != null 
										? "X".equalsIgnoreCase(rs.getString("IS_DEPOT")) : false;
					String notificationMsg = rs.getString("NOTIFICATION_MSG");
					EnumWaveInstancePublishSrc source = EnumWaveInstancePublishSrc.getEnum(rs.getString("SOURCE"));
					String originFacility = rs.getString("ORIGIN_FACILITY");
					
					IRoutingEquipmentType equipmentType = new RoutingEquipmentType();
					equipmentType.setEquipmentTypeID(rs.getString("EQUIPMENT_TYPE"));
					equipmentType.setRegionID(rs.getString("REGION"));
					 
					
					if(_dispatchDate != null && _endTime != null 
							&& _startTime != null && _cutOffTime != null && _zoneCode != null) {

						RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(_startTime);

						Date startTime = RoutingDateUtil.addMinutes(_startTime, preTripTime);
						Date endTime = RoutingDateUtil.addMinutes(_endTime, -postTripTime);
						
						int runTime = RoutingDateUtil.calcRunTime(startTime, endTime);
						
						int maxRunTime = 0;
						if(_maxTime != null) {
							Date maxTime = RoutingDateUtil.addMinutes(_maxTime, -postTripTime);
							maxRunTime = RoutingDateUtil.calcRunTime(startTime, maxTime);
						} else {
							maxRunTime = runTime;
						}

						RoutingTimeOfDay _waveStartTime = new RoutingTimeOfDay(startTime);

						IWaveInstance waveInstance = new WaveInstance();
						waveInstance.setCutOffTime(_cutOffTime);
						waveInstance.setDispatchTime(_dispatchTime);
						waveInstance.setWaveStartTime(_waveStartTime);
						waveInstance.setPreferredRunTime(runTime);
						waveInstance.setMaxRunTime(maxRunTime);
						waveInstance.setNoOfResources(noOfResources);
						waveInstance.setForce(force);
						waveInstance.setNeedsConsolidation(needsConsolidation);
						waveInstance.setRoutingWaveInstanceId(_routingWaveInstanceId);
						waveInstance.setWaveInstanceId(_waveInstanceId);
						waveInstance.setDeliveryDate(_dispatchDate);
						waveInstance.setNotificationMessage(notificationMsg);
						waveInstance.setStatus(status);
						waveInstance.setSource(source);
						waveInstance.setOriginFacility(originFacility);
						waveInstance.setTodRestrictionModel(rs.getString("TODRESTRICTION"));
						waveInstance.setEquipmentType(equipmentType);
						IAreaModel area = new AreaModel();
						area.setAreaCode(_zoneCode);
						waveInstance.setArea(area);
						
						if(!result.containsKey(_dispatchDate)) {
							result.put(_dispatchDate, new TreeMap<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>());
						}
						if(!result.get(_dispatchDate).containsKey(_zoneCode)) {
							result.get(_dispatchDate).put(_zoneCode, new HashMap<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>());
						}
						if(!result.get(_dispatchDate).get(_zoneCode).containsKey(_dispatchTime)) {
							result.get(_dispatchDate).get(_zoneCode).put(_dispatchTime, new HashMap<RoutingTimeOfDay, List<IWaveInstance>>());
						}
						if(!result.get(_dispatchDate).get(_zoneCode).get(_dispatchTime).containsKey(_cutOffTime)) {
							result.get(_dispatchDate).get(_zoneCode).get(_dispatchTime).put(_cutOffTime, new ArrayList<IWaveInstance>());
						}
						result.get(_dispatchDate).get(_zoneCode).get(_dispatchTime).get(_cutOffTime).add(waveInstance);
					}
				} while(rs.next());		        		    	
			}
		}
		);
		return result;
	}
	
	
	private static final String GET_PLANBYDATE_QRY = "select P.PLAN_DATE DISPATCH_DATE, P.ZONE ZONE, P.CUTOFF_DATETIME CUT_OFF, P.ORIGIN_FACILITY, P.DESTINATION_FACILITY " +
			", P.DISPATCH_GROUPTIME, P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME " +
			", Z.PRETRIP_TIME, Z.POSTTRIP_TIME, Z.LOADING_PRIORITY " +
		 	"from transp.plan p, transp.zone z, transp.trn_facility f where P.PLAN_DATE = ? and P.ZONE = Z.ZONE_CODE and P.ZONE is not null " +
			"and F.ID = P.ORIGIN_FACILITY and F.FACILITYTYPE_CODE <>'DPT' " +
			" order by P.ZONE, P.CUTOFF_DATETIME, P.TRUCK_DISPATCHTIME";
	//Result Description -> Map<ZoneCode, Map<DispatchTIme, Map<CutOffTime, IWaveInstance>>>
	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree(final Date deliveryDate)  throws SQLException {
		
		final Map<String, TrnFacility> facilityMap = retrieveTrnFacilityLocations();
		
		final Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> result = new HashMap<String
																							, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
				PreparedStatement ps =
					connection.prepareStatement(GET_PLANBYDATE_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
							
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {
				    		Date _startTime = rs.getTimestamp("TRUCK_DISPATCHTIME");
				    		Date _endTime = rs.getTimestamp("TRUCK_ENDTIME");
				    		Date _maxTime = rs.getTimestamp("MAX_TIME");
				    		
				    		RoutingTimeOfDay _cutOffTime = new RoutingTimeOfDay(rs.getTimestamp("CUT_OFF"));
				    		
				    		String _zoneCode = rs.getString("ZONE");
				    		int preTripTime = rs.getInt("PRETRIP_TIME");
				    		int postTripTime = rs.getInt("POSTTRIP_TIME");
				    		
				    		String originFacility =  rs.getString("ORIGIN_FACILITY");
				    		String destinationFacility =  rs.getString("DESTINATION_FACILITY");
				    		
				    		if(_startTime != null && _endTime != null 
				    					&& _cutOffTime != null && _zoneCode != null) {
				    			
					    		RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(_startTime);
					    						    		
					    		Date startTime = RoutingDateUtil.addMinutes(_startTime, preTripTime);
					    		Date endTime = RoutingDateUtil.addMinutes(_endTime, -postTripTime);
					    		
					    		int runTime = RoutingDateUtil.calcRunTime(startTime, endTime);
				    		
								int maxRunTime = 0;
								if(_maxTime != null) {
									Date maxTime = RoutingDateUtil.addMinutes(_maxTime, -postTripTime);
									maxRunTime = RoutingDateUtil.calcRunTime(startTime, maxTime);
								} else {
									maxRunTime = runTime;
								}

								RoutingTimeOfDay _waveStartTime = new RoutingTimeOfDay(startTime);
								
					    		IWaveInstance waveInstance = new WaveInstance();
					    		waveInstance.setCutOffTime(_cutOffTime);
					    		waveInstance.setDispatchTime(_dispatchTime);
					    		waveInstance.setWaveStartTime(_waveStartTime);
					    		waveInstance.setMaxRunTime(maxRunTime);
					    		waveInstance.setPreferredRunTime(runTime);
					    		
					    		waveInstance.setOriginFacility((facilityMap.get(originFacility)!=null)?facilityMap.get(originFacility).getName():"");
					    		waveInstance.setDestinationFacility((facilityMap.get(destinationFacility)!=null)?facilityMap.get(destinationFacility).getName():"");
					    		
					    		IAreaModel areaModel = new AreaModel();
					    		areaModel.setAreaCode(_zoneCode);
					    		waveInstance.setArea(areaModel);
					    		
					    		if(!result.containsKey(_zoneCode)) {
					    			result.put(_zoneCode, new HashMap<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>());
					    		}
					    		if(!result.get(_zoneCode).containsKey(_dispatchTime)) {
					    			result.get(_zoneCode).put(_dispatchTime, new HashMap<RoutingTimeOfDay, List<IWaveInstance>>());
					    		}
					    		
					    		if(result.get(_zoneCode).get(_dispatchTime).containsKey(_cutOffTime)) {
					    			List<IWaveInstance> _tmpWaves = result.get(_zoneCode).get(_dispatchTime).get(_cutOffTime);
					    			boolean matchFound = false;
					    			for(IWaveInstance _instance : _tmpWaves) {
					    				if(_instance.equals(waveInstance)) {
					    					_instance.setNoOfResources(_instance.getNoOfResources()+1);				    					
					    					matchFound = true;
					    					break;
					    				}
					    			}
					    			if(!matchFound) {
					    				_tmpWaves.add(waveInstance);
						    			waveInstance.setNoOfResources(1);
					    			}
					    		} else {
					    			List<IWaveInstance> _tmpWaves = new ArrayList<IWaveInstance>();
					    			_tmpWaves.add(waveInstance);
					    			waveInstance.setNoOfResources(1);
					    			result.get(_zoneCode).get(_dispatchTime).put(_cutOffTime, _tmpWaves);
					    		}
				    		}
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return result;
	}
	
	private static final String GET_FUTURE_WAVEINSTANCE_ERRORS_QRY = "select P.DELIVERY_DATE DISPATCH_DATE, P.AREA ZONE, P.CUTOFF_DATETIME CUT_OFF " +
		" , P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME, P.RESOURCE_COUNT, P.FORCE_SYNCHRONIZE, P.REFERENCE_ID, P.WAVEINSTANCE_ID, P.STATUS, P.NOTIFICATION_MSG " +
		" , Z.PRETRIP_TIME, Z.POSTTRIP_TIME, Z.LOADING_PRIORITY, TR.IS_DEPOT IS_DEPOT, TR.code REGION_CODE, TR.name REGION_NAME, TR.description REGION_DESCR " +
		" from transp.WAVE_INSTANCE p, transp.zone z, transp.trn_area a,TRANSP.TRN_REGION TR where P.DELIVERY_DATE > sysdate and (P.STATUS = 'NYN' or P.NOTIFICATION_MSG is not null)  and P.AREA = Z.ZONE_CODE and z.AREA = a.CODE AND TR.CODE = A.REGION_CODE " +
		" order by P.DELIVERY_DATE, P.AREA, P.CUTOFF_DATETIME, P.TRUCK_DISPATCHTIME";
	
	public List<IWaveInstance> getWaveInstanceWithErrors()  throws SQLException {
		final List<IWaveInstance> result = new ArrayList<IWaveInstance>();
		
		jdbcTemplate.query(GET_FUTURE_WAVEINSTANCE_ERRORS_QRY, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {
					
					EnumWaveInstanceStatus status = EnumWaveInstanceStatus.getEnum(rs.getString("STATUS"));
					String notificationMsg = rs.getString("NOTIFICATION_MSG");

					IWaveInstance waveInstance = new WaveInstance();
					waveInstance.setDeliveryDate(rs.getDate("DISPATCH_DATE"));
					waveInstance.setNotificationMessage(notificationMsg);
					waveInstance.setStatus(status);
					
					IAreaModel area = new AreaModel();
					area.setAreaCode(rs.getString("ZONE"));
					waveInstance.setArea(area);
					
					result.add(waveInstance);
					
				} while(rs.next());		        		    	
			}
		}
		);
		return result;
	}
	
	private static final String GET_INSYNC_ZONE_QRY = "select distinct P.AREA ZONE from transp.WAVE_INSTANCE p " +
			"where P.DELIVERY_DATE = ?  and P.REFERENCE_ID is not null ";
	
	public Set<String> getInSyncWaveInstanceZones(final Date deliveryDate)  throws SQLException {
		final Set<String> result = new HashSet<String>();
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(GET_INSYNC_ZONE_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				return ps;
			}
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {					
					result.add(rs.getString("ZONE"));
					
				} while(rs.next());		        		    	
			}
		}
		);
		System.out.println("IN SYNC WAVE INSTANCE ZONES:"+result);
		return result;
	}
	
	private static final String GET_CHECK_WAVEPUBLISH_QRY = "select * from TRANSP.WAVE_INSTANCE_PUBLISH p " +
						"where P.DELIVERY_DATE = ? ";

	public boolean isPlanPublished(final Date deliveryDate)  throws SQLException {
		final Set<String> result = new HashSet<String>();

		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(GET_CHECK_WAVEPUBLISH_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				return ps;
			}
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {					
					result.add(rs.getString("SOURCE"));
				} while(rs.next());		        		    	
			}
		}
		);
		if(result.size() > 0) {
			String src = result.iterator().next();
			if(EnumWaveInstancePublishSrc.getEnum(src) != null 
					&& EnumWaveInstancePublishSrc.PLAN.equals(EnumWaveInstancePublishSrc.getEnum(src))) {
				return true;
			}
		}
		return false;
	}
	
	private static final String GET_DYNAMIC_ZONEMAPPING_QRY = "select T.BASE_DATE D_DATE, Z.ZONE_CODE D_ZONE from dlv.timeslot t" +
			", dlv.zone z where T.BASE_DATE >= trunc(sysdate) and T.ZONE_ID = Z.ID and T.IS_DYNAMIC = 'X' group by T.BASE_DATE, Z.ZONE_CODE";
	
	public Map<Date, List<String>> getDynamicEnabledZoneMapping()  throws SQLException {
		final Map<Date, List<String>> result = new HashMap<Date, List<String>>();

		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(GET_DYNAMIC_ZONEMAPPING_QRY);				
				return ps;
			}
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {
					Date key = rs.getDate("D_DATE");
					if(!result.containsKey(key)) {
						result.put(key, new ArrayList<String>());
					}
					result.get(key).add(rs.getString("D_ZONE"));
				} while(rs.next());		        		    	
			}
		}
		);		
		return result;
	}

	private static final String GET_STATIC_ZONEDATE_QRY = "select z.zone_code from dlv.zone z, dlv.timeslot t where t.zone_id = z.id " +
			"and t.base_date = ? and t.is_dynamic is null group by z.zone_code having count(*) > 0";
	
	public List<String> getStaticZonesByDate(final Date deliveryDate)  throws SQLException {
		final List<String> zones = new ArrayList<String>();

		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(GET_STATIC_ZONEDATE_QRY);	
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				
				return ps;
			}
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {
					zones.add(rs.getString("zone_code"));
				} while(rs.next());		        		    	
			}
		}
		);		
		return zones;
	}

	
	private static final String GET_PLANNEDTRAILERBYDATE_QRY = "select P.PLAN_DATE DISPATCH_DATE, P.REGION, P.DISPATCH_GROUPTIME, P.TRUCK_DISPATCHTIME, P.TRUCK_ENDTIME, P.MAX_TIME, P.CUTOFF_DATETIME CUT_OFF, F.FACILITY_CODE, "+ 
			"F.LEAD_TO_TIME TO_LEADTIME, F.LEAD_FROM_TIME FROM_LEADTIME, F.ROUTING_CODE " +
			"from transp.plan p, TRANSP.TRN_FACILITY f "+
			"where P.DESTINATION_FACILITY = F.ID and P.PLAN_DATE = ? "+
			"and F.FACILITYTYPE_CODE = 'CD' ";
	
	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedTrailerDispatchTree(final Date deliveryDate, final Date cutOff)  throws SQLException {

		//Result Description -> Map<DestinatinFacility, Map<DispatchTIme, Map<CutOffTime, IWaveInstance>>>
		final Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> result 
							= new HashMap<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>();

		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_PLANNEDTRAILERBYDATE_QRY);
		if(cutOff != null){
			updateQ.append(" and to_char(P.CUTOFF_DATETIME, 'HH:MI AM') = to_char(?, 'HH:MI AM')");
		}
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			
				PreparedStatement ps =	connection.prepareStatement(updateQ.toString());
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				if(cutOff != null){
					ps.setTimestamp(2, new Timestamp(cutOff.getTime()));
				}
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	  	do {
					    		Date _startTime = rs.getTimestamp("TRUCK_DISPATCHTIME");
					    		Date _endTime = rs.getTimestamp("TRUCK_ENDTIME");
					    		Date _maxTime = rs.getTimestamp("MAX_TIME");
					    		RoutingTimeOfDay _cutOffTime = new RoutingTimeOfDay(rs.getTimestamp("CUT_OFF"));
					    		
					    		String _destCode = rs.getString("FACILITY_CODE");
					    		int toFacilityTime = rs.getInt("TO_LEADTIME");
					    		int fromFacilityTime = rs.getInt("FROM_LEADTIME");
					    		String routingCode = rs.getString("ROUTING_CODE");
					    		
					    		if(_startTime != null && _endTime != null 
				    					&& _cutOffTime != null && _destCode != null) {
					    			
					    			RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(_startTime);
					    			
					    			Date startTime = RoutingDateUtil.addMinutes(_startTime
					    															, (toFacilityTime != 0 ? toFacilityTime : fromFacilityTime));
					    			
					    			int runTime = RoutingDateUtil.calcRunTime(startTime, _endTime);
						    		
					    			int maxRunTime = 0;
									if(_maxTime != null) {
										maxRunTime = RoutingDateUtil.calcRunTime(startTime, _maxTime);
									} else {
										maxRunTime = runTime;
									}

					    			RoutingTimeOfDay _waveStartTime = new RoutingTimeOfDay(startTime);
					    								    			
					    			IWaveInstance waveInstance = new WaveInstance();
						    		waveInstance.setCutOffTime(_cutOffTime);
						    		waveInstance.setDispatchTime(_dispatchTime);
						    		waveInstance.setWaveStartTime(_waveStartTime);
						    		waveInstance.setMaxRunTime(maxRunTime);
						    		waveInstance.setPreferredRunTime(runTime);
						    		
						    		waveInstance.setRoutingCode(routingCode);
						    		
						    		if(!result.containsKey(_destCode)) {
						    			result.put(_destCode, new HashMap<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>());
						    		}
						    		if(!result.get(_destCode).containsKey(_dispatchTime)) {
						    			result.get(_destCode).put(_dispatchTime, new HashMap<RoutingTimeOfDay, List<IWaveInstance>>());
						    		}
						    		
						    		if(result.get(_destCode).get(_dispatchTime).containsKey(_cutOffTime)) {
						    			List<IWaveInstance> _tmpWaves = result.get(_destCode).get(_dispatchTime).get(_cutOffTime);
						    			boolean matchFound = false;
						    			for(IWaveInstance _instance : _tmpWaves) {
						    				if(_instance.equals(waveInstance)) {
						    					_instance.setNoOfResources(_instance.getNoOfResources()+1);				    					
						    					matchFound = true;
						    					break;
						    				}
						    			}
						    			if(!matchFound) {
						    				_tmpWaves.add(waveInstance);
							    			waveInstance.setNoOfResources(1);
						    			}
						    		} else {
						    			List<IWaveInstance> _tmpWaves = new ArrayList<IWaveInstance>();
						    			_tmpWaves.add(waveInstance);
						    			waveInstance.setNoOfResources(1);
						    			result.get(_destCode).get(_dispatchTime).put(_cutOffTime, _tmpWaves);
						    		}
					    		}
				    	  	} while(rs.next());
				      }
		});	
		
		return result;
	}
	
	private static final String SELECT_ACTIVE_FACILITYS = "SELECT F.ID CODE, FT.FACILITYTYPE_CODE, FT.DESCRIPTION" 
		+" from TRANSP.TRN_FACILITY F, TRANSP.TRN_FACILITYTYPE FT"
		+" where F.FACILITYTYPE_CODE = FT.FACILITYTYPE_CODE";
	
	public Map<String, TrnFacilityType> retrieveTrnFacilitys()throws SQLException{
		
		final Map<String, TrnFacilityType> result = new HashMap<String, TrnFacilityType>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			
				PreparedStatement ps =	connection.prepareStatement(SELECT_ACTIVE_FACILITYS);			
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	  	do{
				    	  		String code = rs.getString("CODE");			
				    			
				    			TrnFacilityType facilityType = new TrnFacilityType();
				    			
				    			facilityType.setName(rs.getString("FACILITYTYPE_CODE"));
				    			facilityType.setDescription("DESCRIPTION");			
				    			
				    			result.put(code, facilityType);				    	  		
				    	  	}while(rs.next());
				      }
		});	
		
		return result;
	}

	private static final String GET_ACTIVE_FACILITYS = "SELECT ID, FACILITY_CODE, "+
		" DESCRIPTION, ROUTING_CODE, PREFIX, LEAD_FROM_TIME, LEAD_TO_TIME, FACILITYTYPE_CODE from TRANSP.TRN_FACILITY ";
	
	public Map<String, TrnFacility> retrieveTrnFacilityLocations()throws SQLException{
		
		final Map<String, TrnFacility> result = new HashMap<String, TrnFacility>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			
				PreparedStatement ps =	connection.prepareStatement(GET_ACTIVE_FACILITYS);			
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	  	do {				    	  				
				    	  		TrnFacility _loc = new TrnFacility();
				    	  		_loc.setFacilityId(rs.getString("ID"));
				    	  		_loc.setName(rs.getString("FACILITY_CODE"));
				    	  		_loc.setRoutingCode(rs.getString("ROUTING_CODE"));
				    	  		_loc.setPrefix(rs.getString("PREFIX"));
				    	  		_loc.setTrnFacilityType(new TrnFacilityType());				    			
				    	  		_loc.getTrnFacilityType().setName(rs.getString("FACILITYTYPE_CODE"));
				    			
				    			result.put(_loc.getFacilityId(), _loc);
				    	  	}while(rs.next());
				      }
		});	
		
		return result;
	}
	
	private static final String GET_CUTOFF_SEQUENCE  = "SELECT SEQUENCENO, CUTOFF_TIME FROM TRANSP.TRN_CUTOFF";
		
		public Map<RoutingTimeOfDay, Integer> getCutoffSequence() throws SQLException{
			
			final Map<RoutingTimeOfDay, Integer> result = new HashMap<RoutingTimeOfDay, Integer>();
			
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
					PreparedStatement ps =	connection.prepareStatement(GET_CUTOFF_SEQUENCE);			
					return ps;
				}
			};
			
			jdbcTemplate.query(creator, 
					  new RowCallbackHandler() { 
					      public void processRow(ResultSet rs) throws SQLException {				    	
					    	  	do {				    	  				
					    	  		RoutingTimeOfDay _cutOffTime = new RoutingTimeOfDay(rs.getTimestamp("CUTOFF_TIME"));
					    	  		Integer sequence = rs.getInt("SEQUENCENO");
					    			result.put(_cutOffTime, sequence);
					    	  	}while(rs.next());
					      }
			});	
			
			return result;
		}

		private static final String GET_REGIONS  = "SELECT * FROM TRANSP.TRN_REGION";
		
		@Override
		public List<IRegionModel> getRegions() {
			
			final List<IRegionModel> result = new ArrayList<IRegionModel>();
			
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				
					PreparedStatement ps =	connection.prepareStatement(GET_REGIONS);			
					return ps;
				}
			};
			
			jdbcTemplate.query(creator, 
					  new RowCallbackHandler() { 
					      public void processRow(ResultSet rs) throws SQLException {				    	
					    	  	do {
					    	  		IRegionModel _rModel = new RegionModel();
					    	  		_rModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
		         					_rModel.setRegionCode(rs.getString("CODE"));
		         					_rModel.setName(rs.getString("NAME"));
		         					_rModel.setDescription(rs.getString("DESCRIPTION"));
		         					
					    			result.add(_rModel);
					    	  	}while(rs.next());
					      }
			});	
			
			return result;
		}
		
		private static final String GET_WAVEINSTANCE_DISPATCHTIME_QRY = "select p.*, a.delivery_rate,TR.CODE REGION, TR.IS_DEPOT, z.*, c.SHIFT from transp.WAVE_INSTANCE p , transp.zone z, transp.trn_area a, transp.trn_cutoff c  " +
				", TRANSP.TRN_REGION TR WHERE  P.DELIVERY_DATE = ? and P.AREA = Z.ZONE_CODE and z.AREA = a.CODE and TR.CODE = A.REGION_CODE and P.CUTOFF_DATETIME = C.CUTOFF_TIME order by p.truck_dispatchtime, p.area asc";
				
				public List<IWaveInstance> getWavesByDispatchTime(final Date deliveryDate)  throws SQLException {
					final List<IWaveInstance> result = new ArrayList<IWaveInstance>();
					
					PreparedStatementCreator creator = new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						
							PreparedStatement ps =	connection.prepareStatement(GET_WAVEINSTANCE_DISPATCHTIME_QRY);	
							ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
							return ps;
						}
					};
					
					
					jdbcTemplate.query(creator, 
							new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do {								
								Date _startTime = rs.getTimestamp("TRUCK_DISPATCHTIME");
								Date _endTime = rs.getTimestamp("TRUCK_ENDTIME");
								Date _maxTime = rs.getTimestamp("MAX_TIME");								
								RoutingTimeOfDay _cutOffTime = new RoutingTimeOfDay(rs.getTimestamp("CUTOFF_DATETIME"));

								int preTripTime = rs.getInt("PRETRIP_TIME");
								int postTripTime = rs.getInt("POSTTRIP_TIME");
								String _zoneCode = rs.getString("AREA");
								String _routingWaveInstanceId = rs.getString("REFERENCE_ID");
								String _waveInstanceId = rs.getString("WAVEINSTANCE_ID"); 
								int noOfResources = rs.getInt("RESOURCE_COUNT");
								Date _deliveryDate = rs.getDate("DELIVERY_DATE");
								String _shift = rs.getString("SHIFT");
								
								if(_startTime != null && _endTime != null 
										&& _cutOffTime != null && _zoneCode != null) {

									RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(_startTime);
						    		
						    		Date startTime = RoutingDateUtil.addMinutes(_startTime, preTripTime);
						    		Date endTime = RoutingDateUtil.addMinutes(_endTime, -postTripTime);
						    		
						    		int runTime = RoutingDateUtil.calcRunTime(startTime, endTime);
						    		
									int maxRunTime = 0;
									if(_maxTime != null) {
										Date maxTime = RoutingDateUtil.addMinutes(_maxTime, -postTripTime);
										maxRunTime = RoutingDateUtil.calcRunTime(startTime, maxTime);
									} else {
										maxRunTime = runTime;
									}
									
									IWaveInstance waveInstance = new WaveInstance();
									waveInstance.setCutOffTime(_cutOffTime);
									waveInstance.setDispatchTime(_dispatchTime);
									waveInstance.setMaxRunTime(maxRunTime);
									waveInstance.setPreferredRunTime(runTime);
									waveInstance.setNoOfResources(noOfResources);
									waveInstance.setRoutingWaveInstanceId(_routingWaveInstanceId);
									waveInstance.setWaveInstanceId(_waveInstanceId);
									waveInstance.setDeliveryDate(_deliveryDate);
									waveInstance.setShift(_shift);
									IAreaModel area = new AreaModel();
									area.setAreaCode(_zoneCode);
									area.setDeliveryRate(rs.getBigDecimal("DELIVERY_RATE").doubleValue());
									IRegionModel region = new RegionModel();
									region.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
									region.setRegionCode(rs.getString("REGION"));
									area.setRegion(region);
									waveInstance.setArea(area);
									result.add(waveInstance);
									
								}
									
							} while(rs.next());
						}
					}
					);
					return result;
				}
				
				private static final String GET_PLANTCAPACITY_QRY = "select p.* from transp.PLANT_CAPACITY p where p.day_of_week = ?";
				
				public Map<RoutingTimeOfDay, Integer> getPlantCapacityByDispatchTime(final Date deliveryDate)  throws SQLException {
					final Map<RoutingTimeOfDay, Integer> result = new HashMap<RoutingTimeOfDay, Integer>();
					
					PreparedStatementCreator creator = new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						
							PreparedStatement ps =	connection.prepareStatement(GET_PLANTCAPACITY_QRY);	
							ps.setString(1, DateUtil.formatDayOfWk(deliveryDate));
							return ps;
						}
					};
					
					jdbcTemplate.query(creator, 
							new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do {
								Date dispatchTime = rs.getTimestamp("DISPATCH_TIME");
								int capacity = rs.getInt("CAPACITY");
								RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(dispatchTime);
								result.put(_dispatchTime, capacity);
							} while(rs.next());		        		    	
						}
					}
					);
					return result;
				}
				
				private static final String GET_PLANTDISPATCH_MAPPING = "select * from transp.DISPATCH_MAPPING";
				
				public Map<RoutingTimeOfDay, RoutingTimeOfDay> getPlantDispatchMapping()  throws SQLException {
					final Map<RoutingTimeOfDay, RoutingTimeOfDay> result = new HashMap<RoutingTimeOfDay, RoutingTimeOfDay>();
					
					PreparedStatementCreator creator = new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						
							PreparedStatement ps =	connection.prepareStatement(GET_PLANTDISPATCH_MAPPING);	
							return ps;
						}
					};
					
					jdbcTemplate.query(creator, 
							new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do {
								Date dispatchTime = rs.getTimestamp("DISPATCH_TIME");
								Date plantDispatch= rs.getTimestamp("PLANT_DISPATCH");
								RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(dispatchTime);
								RoutingTimeOfDay _plantDispatch = new RoutingTimeOfDay(plantDispatch);
								result.put(_dispatchTime, _plantDispatch);
							} while(rs.next());		        		    	
						}
					}
					);
					return result;
				}
				
				private static final String GET_FUTURE_WAVEINSTANCE_DATES = "select distinct delivery_date from transp.WAVE_INSTANCE WHERE delivery_date > sysdate and reference_id is not null";
				
				public List<Date> getDeliveryDates()  throws SQLException {
					final List<Date> result = new ArrayList<Date>();
					
					PreparedStatementCreator creator = new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						
							PreparedStatement ps =	connection.prepareStatement(GET_FUTURE_WAVEINSTANCE_DATES);	
							return ps;
						}
					};
					
					jdbcTemplate.query(creator, 
							new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do {
								Date delivery_date = rs.getDate("delivery_date");
								result.add(delivery_date);
							} while(rs.next());		        		    	
						}
					}
					);
					return result;
				}
		
				private static final String GET_STATIC_ROUTES_BY_AREA = 
						"SELECT TA.CODE, T.CUTOFF_TIME, R.ORDER_ID, T.START_TIME, T.ROUTING_START_TIME, R.STATUS_CODE FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z , TRANSP.ZONE TZ, TRANSP.TRN_AREA TA " +
						"WHERE R.TIMESLOT_ID = T.ID AND R.STATUS_CODE in (10, 5) AND T.ZONE_ID = Z.ID AND T.BASE_DATE = ? AND Z.ZONE_CODE = TZ.ZONE_CODE " +
						"AND TZ.AREA = TA.CODE AND R.IN_UPS IS NULL";
				
				@SuppressWarnings("unchecked")
				public Map<String, Map<RoutingTimeOfDay, List<IRouteModel>>> getStaticRoutesByArea(final Date deliveryDate) throws SQLException {
					final Map<String, Map<RoutingTimeOfDay, List<IRouteModel>>> result = new HashMap<String, Map<RoutingTimeOfDay, List<IRouteModel>>>();
					
					PreparedStatementCreator creator = new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						
							PreparedStatement ps =	connection.prepareStatement(GET_STATIC_ROUTES_BY_AREA);	
							ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
							return ps;
						}
					};
					
					jdbcTemplate.query(creator, 
							new RowCallbackHandler() {
						public void processRow(ResultSet rs) throws SQLException {				    	
							do {
								String areaCode  = rs.getString("CODE");
								RoutingTimeOfDay cutoff = new RoutingTimeOfDay(rs.getTimestamp("CUTOFF_TIME"));
								//Date departureTime = rs.getTimestamp("START_TIME");
								int rsvStatusCode = rs.getInt("STATUS_CODE");
								
								IRoutingStopModel stop = new RoutingStopModel();
								stop.setOrderNumber(rs.getString("ORDER_ID"));
								Date departureTime = rs.getTimestamp("ROUTING_START_TIME");
								if(departureTime == null){
									departureTime = rs.getTimestamp("START_TIME");
								}
								stop.setStopDepartureTime(departureTime);
								
								if(!result.containsKey(areaCode))
									result.put(areaCode, new HashMap<RoutingTimeOfDay, List<IRouteModel>>());
								if(!result.get(areaCode).containsKey(cutoff)){
									result.get(areaCode).put(cutoff,new ArrayList<IRouteModel>());
									IRouteModel routeModel = new RouteModel();
									routeModel.setStops(new TreeSet());
									routeModel.setAllocatedStops(new TreeSet());
									result.get(areaCode).get(cutoff).add(routeModel);
								}
								if(rsvStatusCode == 10) {
									result.get(areaCode).get(cutoff).get(0).getStops().add(stop);
								}
								result.get(areaCode).get(cutoff).get(0).getAllocatedStops().add(stop);
							} while(rs.next());		        		    	
						}
					}
					);
					return result;
				}

		
				private static final String GET_ROUTING_WAVEINSTANCES_IDS = "select distinct REFERENCE_ID from transp.WAVE_INSTANCE p where P.DELIVERY_DATE = ?";
				
				@Override
				public Set retrieveRoutingWaveInstIds(final Date deliveryDate)
						throws SQLException {
						final Set result = new HashSet();
						PreparedStatementCreator creator=new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		
								PreparedStatement ps = null;
								if(deliveryDate != null) {
									ps = connection.prepareStatement(GET_ROUTING_WAVEINSTANCES_IDS);
									ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
}
								return ps;
							}
						};
						
						jdbcTemplate.query(creator, 
						new RowCallbackHandler() { 
							public void processRow(ResultSet rs) throws SQLException {				    	
							do {
								result.add(rs.getString("REFERENCE_ID"));
							} while(rs.next());		        		    	
							}
						}
						);
						return result;
				}	

	/* Wave Sync lock/unlock*/
	private static final String GET_WAVESYNCLOCKNEXTSEQ_QRY = "SELECT TRANSP.SYSTEM_SEQ.nextval FROM DUAL";
	
	public String getNewWaveSyncLockId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_WAVESYNCLOCKNEXTSEQ_QRY);
	}
	
	private static final String INSERT_WAVESYNC_LOCK_QRY = "insert into transp.WAVE_INSTANCE_LOCKACTIVITY (LOCK_ID, INITIATOR, LOCK_DATETIME) VALUES (?,?,?) ";
	
	public int addWaveSyncLockActivity(final String userId) throws SQLException {

		Connection connection = null;
		String waveSyncLockId = null;
		try {
			waveSyncLockId = this.getNewWaveSyncLockId();
			connection = this.jdbcTemplate.getDataSource().getConnection();
			return this.jdbcTemplate.update(INSERT_WAVESYNC_LOCK_QRY,
													new Object[] { waveSyncLockId, userId, new Timestamp(new Date().getTime()) });

		} finally {
			if (connection != null)
				connection.close();
		}
	}
	
	private static final String RELEASE_WAVESYNC_LOCK_QRY = "update transp.WAVE_INSTANCE_LOCKACTIVITY set RELEASELOCK_DATETIME = SYSDATE, LOCK_RELEASEDBY = ? " +
			" where RELEASELOCK_DATETIME IS NULL";
	
	public int releaseWaveSyncLock(final String userId) throws SQLException {

		Connection connection = null;

		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();

			return this.jdbcTemplate.update(RELEASE_WAVESYNC_LOCK_QRY,
					new Object[] { userId });

		} finally {
			if (connection != null)
				connection.close();
		}
	}
	
	private static final String GET_WAVESYNC_LOCK_QRY = "select * from transp.WAVE_INSTANCE_LOCKACTIVITY where RELEASELOCK_DATETIME IS NULL";
	
	public WaveSyncLockActivity isWaveSyncronizationLocked() throws SQLException {
		
		final WaveSyncLockActivity lockActivity = new WaveSyncLockActivity();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_WAVESYNC_LOCK_QRY);
				return ps;
			}  
		};
		
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						lockActivity.setLockId(rs.getString("LOCK_ID"));
						lockActivity.setInitiator(rs.getString("INITIATOR"));
						lockActivity.setLockDateTime(rs.getTimestamp("LOCK_DATETIME"));
						lockActivity.setUnLockedBy(rs.getString("LOCK_RELEASEDBY"));
						lockActivity.setReleaselockDateTime(rs.getTimestamp("RELEASELOCK_DATETIME"));
					} while(rs.next());		        		    	
				}
		});
		
		return lockActivity;
	}

	private static final String UPDATE_RESERVATION_ROUTINGSTATUS_BY_CRITERIA = "update dlv.reservation r set r.UPDATE_STATUS = ? where r.ID in "+ 
				" ( " +
				" 	select R.ID  "+
                " 	from dlv.reservation r, dlv.timeslot t, dlv.zone z  "+
                " 	where t.zone_id = z.id and R.TIMESLOT_ID = T.ID and R.STATUS_CODE in ('5','10') and t.is_dynamic = 'X' "+
                " 	and t.base_date = ? ";	
	
	public int flagReservationStatus(final Date deliveryDate, final Date cutoff, final String startTime,
			final String endTime, final String[] area) throws SQLException {
		
		int result = 0;
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(UPDATE_RESERVATION_ROUTINGSTATUS_BY_CRITERIA);
		if(cutoff != null){
			updateQ.append(" and to_char(t.cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')");
		}
		if(startTime != null){
			updateQ.append(" and to_date(to_char(t.start_time, 'HH:MI AM'), 'HH:MI AM') >= to_date(?, 'HH:MI AM')");
		}
		if(endTime != null){
			updateQ.append(" and to_date(to_char(t.start_time, 'HH:MI AM'), 'HH:MI AM') < to_date(?, 'HH:MI AM')");
		}		
		if(area != null && area.length > 0){
			updateQ.append(" and z.zone_code in (");
			for(int intCount = 0; intCount < area.length; intCount++ ) {
				updateQ.append("'").append(area[intCount]).append("'");				
				if(intCount < area.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}
				
		updateQ.append(")");
		Connection connection = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps =
					connection.prepareStatement(updateQ.toString());
			ps.setString(1, EnumRoutingUpdateStatus.OVERRIDDEN.value());
			ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
			int paramIndex = 3;
			if(cutoff != null) ps.setTimestamp(paramIndex++, new Timestamp(cutoff.getTime()));					
			if(startTime != null) ps.setString(paramIndex++, startTime);
			if(endTime != null) ps.setString(paramIndex++, endTime);
			
			result = ps.executeUpdate();
			ps.close();
		} finally {
			if (connection != null)
				connection.close();
		}
		
		return result;
	}
	
	
}
