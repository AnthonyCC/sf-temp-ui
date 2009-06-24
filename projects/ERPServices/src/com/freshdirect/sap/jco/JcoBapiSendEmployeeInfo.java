package com.freshdirect.sap.jco;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiSendEmployeeInfo;
import com.sap.mw.jco.JCO;

public class JcoBapiSendEmployeeInfo extends JcoBapiFunction implements BapiSendEmployeeInfo {
	
	protected JCO.Table employee;
	
	
	public JcoBapiSendEmployeeInfo() {
		super("ZBAPI_EMPLOYEE_INFO_UPDATE");
		
		this.employee = this.function.getTableParameterList().getTable( "T_ZHR_EMPLOYEE" );
	}


	public void addEmployee(String employeeId, String firstName, String lastName, String middleInitial
			, String shortName, String jobType, Date hireDate, String status, String supervisorId
			, String supervisorFirstName, String supervisorMiddleInitial, String supervisorLastName
			, String supervisorShortName,Date terminationDate) {
		
		employee.appendRow();
		employee.setValue(employeeId, "ZEMPLOYEEID");
		employee.setValue(firstName, "ZFIRSTNAME");
		employee.setValue(lastName, "ZLASTNAME");
		employee.setValue(middleInitial, "ZMIDDLEINITIAL");
		
		employee.setValue(shortName, "ZSHORTNAME");
		employee.setValue(jobType, "ZJOBTYPE");
		employee.setValue(hireDate, "ZHIREDATE");
		employee.setValue(status, "ZSTATUS");
		employee.setValue(supervisorId, "ZSUPERVISORID");
		employee.setValue(supervisorFirstName, "ZSUPFIRSTNAME");
		employee.setValue(supervisorMiddleInitial, "ZSUPMIDDLENAME");
		employee.setValue(supervisorLastName, "ZSUPLASTNAME");
		
		employee.setValue(supervisorShortName, "ZSUPSHORTNAME");
		employee.setValue(terminationDate, "ZTERMINATIONDATE");
	}
	
	
}
