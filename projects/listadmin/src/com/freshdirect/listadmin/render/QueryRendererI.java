package com.freshdirect.listadmin.render;

import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.query.QueryI;

public interface QueryRendererI {
	public Object renderQuery(QueryI query, QueryContextI context);
}
