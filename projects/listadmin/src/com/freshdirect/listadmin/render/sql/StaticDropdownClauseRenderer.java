package com.freshdirect.listadmin.render.sql;

import com.freshdirect.listadmin.db.StaticDropdownClause;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.render.ClauseRendererI;

public class StaticDropdownClauseRenderer implements ClauseRendererI {
	StaticDropdownClause clause;
	
	public StaticDropdownClauseRenderer(StaticDropdownClause c) {
		clause = c;
	}
	
	public Object render(QueryContextI context) {
		String parentAlias = "how do I get this";
		StringBuffer buffy = new StringBuffer();
		
		buffy.append(parentAlias);
		buffy.append('.');
		buffy.append(clause.getColumn());
		buffy.append(' ');
		buffy.append(SqlQueryRenderer.operators[clause.getOperatorId()]);
		buffy.append(context.get(clause.getClauseId()));
		
		return buffy.toString();
	}
}
