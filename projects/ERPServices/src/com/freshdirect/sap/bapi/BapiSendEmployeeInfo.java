package com.freshdirect.sap.bapi;

import java.util.Date;


public interface BapiSendEmployeeInfo  extends BapiFunctionI{

	public void addEmployee(String employeeId, String firstName, String lastName, String middleInitial
			, String shortName, String jobType, Date hireDate, String status, String supervisorId
			, String supervisorFirstName, String supervisorMiddleInitial, String supervisorLastName
			, String supervisorShortName,Date terminationDate);

	
}
