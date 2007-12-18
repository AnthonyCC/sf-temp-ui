package com.freshdirect.listadmin.render;

import com.freshdirect.listadmin.query.QueryContextI;

public interface ClauseRendererI {
	public Object render(QueryContextI context);
}
