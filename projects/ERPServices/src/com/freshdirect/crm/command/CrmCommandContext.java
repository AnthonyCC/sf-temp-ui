package com.freshdirect.crm.command;

import java.sql.Connection;
import java.sql.SQLException;

import com.freshdirect.crm.ejb.CrmAgentHome;
import com.freshdirect.crm.ejb.CrmCaseHome;

public interface CrmCommandContext {

	public CrmAgentHome getCrmAgentHome();

	public CrmCaseHome getCrmCaseHome();

	public Connection getConnection() throws SQLException;

}
