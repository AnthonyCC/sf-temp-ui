package com.freshdirect.routing.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.routing.dao.IRoutingInfoDAO;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.ServiceTimeScenario;

public class RoutingInfoDAO extends BaseDAO implements IRoutingInfoDAO   {
	
	private static final String GET_SCENARIOS_QRY = "SELECT * FROM DLV.SERVICETIME_SCENARIO";
	
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
				    		tmpModel.setDefaultServiceTimeType(rs.getString("DEFAULT_SERVICETIME_TYPE"));				    		
				    		tmpModel.setDefaultZoneType(rs.getString("DEFAULT_ZONE_TYPE"));				    		
				    		tmpModel.setDescription(rs.getString("DESCRIPTION"));				    		
				    		tmpModel.setIsDefault(rs.getString("IS_DEFAULT"));				    		
				    		tmpModel.setOrderSizeFormula(rs.getString("ORDERSIZE_FORMULA"));				    		
				    		tmpModel.setServiceTimeFactorFormula(rs.getString("SERVICETIME_FACTOR_FORMULA"));				    		
				    		tmpModel.setServiceTimeFormula(rs.getString("SERVICETIME_FORMULA"));
				    		scenarios.add(tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return scenarios;
	}
}
