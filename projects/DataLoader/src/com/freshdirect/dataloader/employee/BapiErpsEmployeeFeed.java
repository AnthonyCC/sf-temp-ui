package com.freshdirect.dataloader.employee;


import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.erp.ErpEmployeeInfo;
import com.freshdirect.erp.ejb.ErpEmployeeManagerHome;
import com.freshdirect.erp.ejb.ErpEmployeeManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.command.SapSendEmployeeInfo;
import com.sap.mw.jco.JCO;

public class BapiErpsEmployeeFeed implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( BapiErpsEmployeeFeed.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	static JCO.MetaData smeta = null;
	

	public JCO.MetaData[] getStructureMetaData() {
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZBAPI_EMPLOYEE_FEED");//BAPI Name
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("BapiErpsEmployeeFeed execute invoked");
		Context ctx = null;
		
		try {
			LOGGER.debug("Storing Employee Feed info");
			
			
			ctx = ErpServicesProperties.getInitialContext();
			ErpEmployeeManagerHome mgr = (ErpEmployeeManagerHome)ctx.lookup("freshdirect.erp.EmployeeManager");
			ErpEmployeeManagerSB sb = mgr.create();
			List employees =  sb.getEmployees();
			
			 SapSendEmployeeInfo sendEmployees = new SapSendEmployeeInfo();
		        
				
		        if(employees != null) {
		        	Iterator _iterator = employees.iterator();
		        	ErpEmployeeInfo model = null;
		        	SapSendEmployeeInfo.EmployeeInfo tmpModel = null; 
		        	while(_iterator.hasNext()) {
		        		model = (ErpEmployeeInfo)_iterator.next();
		        		tmpModel = sendEmployees.new EmployeeInfo(
		        				model.getEmployeeId(), model.getFirstName(), model.getLastName(), model.getMiddleInitial()
		    					, model.getShortName(), model.getJobType(), model.getHireDate(), model.getStatus(), model.getSupervisorId()
		    					, model.getSupervisorFirstName(), model.getSupervisorMiddleInitial(), model.getSupervisorLastName()
		    					, model.getSupervisorShortName(), model.getTerminationDate());
		        		sendEmployees.addEmployee(tmpModel);
		        	}
		        }
		        sendEmployees.execute();
		        
			LOGGER.debug("Stored Employee Feed info");
			output.setValue("S", "RETURN");
			output.setValue("All good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store COOL info", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}  finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
	}

	
}