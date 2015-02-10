package com.freshdirect.sap.jco;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiSendEmployeeInfo;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class JcoBapiSendEmployeeInfo extends JcoBapiFunction implements BapiSendEmployeeInfo {
	
	protected JCoTable employee;
	
	public JcoBapiSendEmployeeInfo() throws JCoException
	{
		super("ZBAPI_EMPLOYEE_INFO_UPDATE");
		
		this.employee = this.function.getTableParameterList().getTable( "T_ZHR_EMPLOYEE" );
	}


	public void addEmployee(String employeeId, String firstName, String lastName, String middleInitial
			, String shortName, String jobType, Date hireDate, String status, String supervisorId
			, String supervisorFirstName, String supervisorMiddleInitial, String supervisorLastName
			, String supervisorShortName,Date terminationDate)
	{
		employee.appendRow();
		employee.setValue("ZEMPLOYEEID", employeeId);
		employee.setValue("ZFIRSTNAME", firstName);
		employee.setValue("ZLASTNAME", lastName);
		employee.setValue("ZMIDDLEINITIAL", middleInitial);
		
		employee.setValue("ZSHORTNAME", shortName);
		employee.setValue("ZJOBTYPE", jobType);
		employee.setValue("ZHIREDATE", hireDate);
		employee.setValue("ZSTATUS", status);
		employee.setValue("ZSUPERVISORID", supervisorId);
		employee.setValue("ZSUPFIRSTNAME", supervisorFirstName);
		employee.setValue("ZSUPMIDDLENAME", supervisorMiddleInitial);
		employee.setValue("ZSUPLASTNAME", supervisorLastName);
		
		employee.setValue("ZSUPSHORTNAME", supervisorShortName);
		employee.setValue("ZTERMINATIONDATE", terminationDate);
	}
	
	
}
