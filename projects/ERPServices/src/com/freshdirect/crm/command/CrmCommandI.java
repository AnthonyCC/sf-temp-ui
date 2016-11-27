package com.freshdirect.crm.command;

import java.io.Serializable;

import com.freshdirect.fdstore.FDResourceException;

public interface CrmCommandI extends Serializable {

	public CrmCommandResultI execute() throws FDResourceException;

	public void setContext(CrmCommandContext context);

}
