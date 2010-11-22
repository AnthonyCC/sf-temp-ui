package com.freshdirect.routing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.dao.IRoutingInfoDAO;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.model.ServiceTimeScenario;
import com.freshdirect.routing.model.ServiceTimeTypeModel;
import com.freshdirect.routing.model.WaveInstance;
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
			"or (D.SCENARIO_DATE is null and D.DAY_OF_WEEK is null)) ORDER BY SCENARIO_DATE, DAY_OF_WEEK NULLS LAST";            
	
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
		
		return tmpModel;
	}
	
	private static final String GET_SCENARIO_ZONEMAPPING = "select DZ.ZONE_CODE " +
			", DZ.SERVICETIME_TYPE , DZ.SERVICETIME_OVERRIDE , DZ.SERVICETIME_OPERATOR , DZ.SERVICETIME_ADJUSTMENT, " +
			"FIXED_SERVICE_TIME ,VARIABLE_SERVICE_TIME " +
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
	
	private static final String GET_PLANBYDATE_QRY = "select P.PLAN_DATE DISPATCH_DATE, P.ZONE ZONE, P.CUTOFF_DATETIME CUT_OFF " +
			", P.START_TIME DISPATCH_TIME, P.FIRST_DLV_TIME, P.LAST_DLV_TIME " +
			", Z.STEM_TO_TIME TO_ZONETIME, Z.STEM_FROM_TIME FROM_ZONETIME, Z.LOADING_PRIORITY " +
			"from transp.plan p, transp.zone z where P.PLAN_DATE = ? and P.ZONE = Z.ZONE_CODE and P.ZONE is not null and (P.IS_OPEN = '' or P.IS_OPEN is null) " +
			"order by P.ZONE, P.CUTOFF_DATETIME, P.FIRST_DLV_TIME";
	//Result Description -> Map<ZoneCode, Map<DispatchTIme, Map<CutOffTime, IWaveInstance>>>
	public Map<String, Map<RoutingTimeOfDay, Map<Date, List<IWaveInstance>>>> getPlannedDispatchTree(final Date deliveryDate)  throws SQLException {
		
		final Map<String, Map<RoutingTimeOfDay, Map<Date, List<IWaveInstance>>>> result = new HashMap<String
																							, Map<RoutingTimeOfDay, Map<Date, List<IWaveInstance>>>>();
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
				    		Date _firstDeliveryTime = rs.getTimestamp("FIRST_DLV_TIME");
				    		Date _lastDeliveryTime = rs.getTimestamp("LAST_DLV_TIME");
				    		Date _startTime = rs.getTimestamp("DISPATCH_TIME");
				    		Date _cutOffTime = rs.getTimestamp("CUT_OFF");
				    		
				    		String _zoneCode = rs.getString("ZONE");
				    		int toZoneTime = rs.getInt("TO_ZONETIME");
				    		int fromZoneTime = rs.getInt("FROM_ZONETIME");
				    		
				    		if(_firstDeliveryTime != null && _lastDeliveryTime != null 
				    					&& _startTime != null && _cutOffTime != null && _zoneCode != null) {
				    			
					    		RoutingTimeOfDay _dispatchTime = new RoutingTimeOfDay(_startTime);
					    						    		
					    		Date startTime = RoutingDateUtil.addMinutes(_firstDeliveryTime
					    														, (toZoneTime != 0 ? -toZoneTime : -fromZoneTime));
					    		Date endTime = RoutingDateUtil.addMinutes(_lastDeliveryTime
					    														, (fromZoneTime != 0 ? fromZoneTime : toZoneTime));
					    		
					    		int runTime = RoutingDateUtil.getDiffInMinutes(endTime, startTime);
					    		
					    		RoutingTimeOfDay _waveStartTime = new RoutingTimeOfDay(startTime);
					    		
					    		IWaveInstance waveInstance = new WaveInstance();
					    		waveInstance.setCutOffTime(_cutOffTime);
					    		waveInstance.setDispatchTime(_dispatchTime);
					    		waveInstance.setWaveStartTime(_waveStartTime);
					    		waveInstance.setMaxRunTime(runTime);
					    		waveInstance.setPreferredRunTime(runTime);
					    		
					    		if(!result.containsKey(_zoneCode)) {
					    			result.put(_zoneCode, new HashMap<RoutingTimeOfDay, Map<Date, List<IWaveInstance>>>());
					    		}
					    		if(!result.get(_zoneCode).containsKey(_dispatchTime)) {
					    			result.get(_zoneCode).put(_dispatchTime, new HashMap<Date, List<IWaveInstance>>());
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
}
