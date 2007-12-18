package com.freshdirect.listadmin.render.sql;

import com.freshdirect.listadmin.db.ParamClause;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.render.ClauseRendererI;

public class ParamClauseRenderer implements ClauseRendererI {
	ParamClause clause;
	
	public ParamClauseRenderer(ParamClause c) {
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
