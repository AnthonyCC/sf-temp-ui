package com.freshdirect.listadmin.render.sql;

import com.freshdirect.listadmin.db.ConstantClause;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.render.ClauseRendererI;

public class ConstantClauseRenderer implements ClauseRendererI{
	ConstantClause clause;
	
	public ConstantClauseRenderer(ConstantClause c) {
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
		buffy.append(clause.getConstant());
		
		return buffy.toString();
	}
}
