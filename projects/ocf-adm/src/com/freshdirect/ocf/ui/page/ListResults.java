/**
 * @author ekracoff
 * Created on Jun 10, 2005*/

package com.freshdirect.ocf.ui.page;

import com.freshdirect.ocf.core.OcfTableI;


public abstract class ListResults {
	
	public abstract OcfTableI getTable();
	
	public abstract void setTable(OcfTableI table);

}
