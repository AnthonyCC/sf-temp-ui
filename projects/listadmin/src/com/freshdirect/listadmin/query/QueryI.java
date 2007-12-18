package com.freshdirect.listadmin.query;

import java.util.Set;

public interface QueryI {
	// "tables" is probably the wrong word, if we want this to be
	// really general
	public Set getTables();
	
	public void addClause(ClauseI clause);
	public Set getClauses();
	
	public boolean getRunnable(QueryContextI context);
}
