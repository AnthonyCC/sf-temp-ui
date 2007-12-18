package com.freshdirect.listadmin.render.sql;

import java.util.Iterator;
import java.util.Map;

import com.freshdirect.listadmin.db.Clause;
import com.freshdirect.listadmin.db.ConstantClause;
import com.freshdirect.listadmin.db.EnumDropdownClause;
import com.freshdirect.listadmin.db.JoinClause;
import com.freshdirect.listadmin.db.ParamClause;
import com.freshdirect.listadmin.db.QueryDropdownClause;
import com.freshdirect.listadmin.db.StaticDropdownClause;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.db.VirtualObject;
import com.freshdirect.listadmin.metadata.MetaDataUtils;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.query.QueryI;
import com.freshdirect.listadmin.render.QueryRendererI;
import com.freshdirect.framework.util.HashMessageFormat;

public class SqlQueryRenderer implements QueryRendererI {
	public final static String operators[]   = {"", "=", ">", "<", "in", "not in"}; 
			
	/* Renders a Zql-parsed query, which isn't being used right now although it
	 * could be
	public Object renderQuery(QueryI query, QueryContextI context) {
		StoredQuery q     = (StoredQuery) query;
		String extraWhere = "";		
		
		StringBuffer buffy = new StringBuffer();
		
		buffy.append("SELECT ");
		
		Set fields  = q.getFields();
		Set objects = q.getObjects();		
		
		for(Iterator it=fields.iterator();it.hasNext();) {
			String fieldName = ((TemplateField) it.next()).getFieldName();
			
			fieldName = MetaDataUtils.getMappedColumn(fieldName);
						
			buffy.append(fieldName);
			if(it.hasNext()) buffy.append(",");
		}
		
		buffy.append(" FROM ");
		
		for(Iterator it=objects.iterator();it.hasNext();) {
			VirtualObject qt = (VirtualObject) it.next();

			if(MetaDataUtils.getUnparsed(qt.getTableName())) {
				return renderUnparsedQuery(query,context);
			}
			
			buffy.append(MetaDataUtils.getMappedTable(qt.getTableName()));

			if(it.hasNext()) buffy.append(",");
			
			String tmp = MetaDataUtils.getExtraWhere(qt.getTableName());
			
			if(extraWhere != null && !"".equals(extraWhere) && tmp != null && !"".equals(tmp)) {
				extraWhere = extraWhere + " AND " + tmp;
			} else if((extraWhere == null || "".equals(extraWhere)) && tmp != null && !"".equals(tmp)) {
				extraWhere = tmp;
			}
		}		
		
		buffy.append(" WHERE ");


		if(extraWhere != null && !"".equals(extraWhere)) {
			buffy.append(extraWhere);
			buffy.append(" AND ");
		}
		
		buildWhereClause(null,q,context,buffy);
		
		String ret = buffy.toString().trim();
		
		if(ret.endsWith(" AND")) {
			buffy = new StringBuffer(ret.substring(0,ret.length()-4));
		} if(ret.endsWith(" WHERE")) {
			buffy = new StringBuffer(ret.substring(0,ret.length()-6));
		}
		
		Set groupBys = q.getGroupBy();
		if(!groupBys.isEmpty()) {
			buffy.append(" GROUP BY ");
			for(Iterator it=groupBys.iterator();it.hasNext();) {
				QueryGroupBy gb = (QueryGroupBy) it.next();
				buffy.append(MetaDataUtils.getMappedColumn(gb.getFieldName()));
			}
		}
		
		// And as a final pass, fill in any escapes within the query
		return HashMessageFormat.format(buffy.toString(),(Map) context);
	}
	*/
	
	public Object renderQuery(QueryI query, QueryContextI context) {
		StoredQuery q = (StoredQuery) query;
		
		VirtualObject obj  = (VirtualObject) q.getObjects().iterator().next();
		String sql         = obj.getSqlText();
		StringBuffer buffy = new StringBuffer();
		
		buffy.append("select * from (");
		buffy.append(sql);		
		buffy.append(") WHERE ");
		
		buildWhereClause(null,q,context,buffy);
		
		String ret = buffy.toString().trim();
		
		if(ret.endsWith(" AND")) {
			buffy = new StringBuffer(ret.substring(0,ret.length()-4));
		} if(ret.endsWith(" WHERE")) {
			buffy = new StringBuffer(ret.substring(0,ret.length()-6));
		}

		// And as a final pass, fill in any escapes within the query
		sql = cleanParams(buffy.toString());
				
		return HashMessageFormat.format(sql,(Map) context);
	}

	// TODO: redundant with SqlIntrospector, put in a util class
	// or, better yet, find some way to only do this once.
	private String cleanParams(String sql) {
		StringBuffer buffy = new StringBuffer();
		int pos            = 0;
		
		while(pos != -1) {
			pos = sql.indexOf('{');
			
			if(pos != -1) {
				buffy.append(sql.substring(0,pos+1));
				sql = sql.substring(pos+1);
				
				if(pos != -1) {
					int pos2 = sql.indexOf(":");
					int pos3 = sql.indexOf("}");
					
					if(pos2 != -1 && pos2 < pos3) {
						buffy.append(sql.substring(0,pos2));
					} else {
						buffy.append(sql.substring(0,pos3));
					}
					
					sql = sql.substring(pos3);
				}
			}
		}
		buffy.append(sql);
		
		return buffy.toString();
	}
	
	private void buildWhereClause(QueryI parent, QueryI query, QueryContextI context, StringBuffer buffy) {
		for(Iterator it=query.getClauses().iterator();it.hasNext();) {
			Clause c = (Clause) it.next();
			
			// This, well there's no other word for it, this sucks.  I want to keep render code separate
			// from the clauses, because someday we may want to render clauses as something other than 
			// sql (code to traverse ContentNodes, for example).  So, no getSQL() in clauses. But switching
			// on the subclass rather defeats the whole point of an object-oriented language, now doesn't it?
			//
			// I suppose the best middle ground would be
			// buffy.append((String) SqlClauseRenderFactory.getRenderer((ClauseI) it.next()).render(context));
			// but there's some stuff I need to think through before that can be finished
			if(c instanceof ConstantClause) {
				ConstantClause cc = (ConstantClause) c;
				buffy.append(MetaDataUtils.getMappedColumn(cc.getColumn()));
				buffy.append(operators[cc.getOperatorId()]);
				buffy.append(formatForSql(cc.getColumn(),cc.getConstant()));
			} else if(c instanceof ParamClause) {
				ParamClause cc = (ParamClause) c;
				buffy.append(MetaDataUtils.getMappedColumn(cc.getColumn()));
				buffy.append(operators[cc.getOperatorId()]);
				buffy.append(formatForSql(cc.getColumn(),context.get(cc.getClauseId())));
			} else if(c instanceof StaticDropdownClause) {
				StaticDropdownClause cc = (StaticDropdownClause) c;
				buffy.append(MetaDataUtils.getMappedColumn(cc.getColumn()));
				buffy.append(operators[cc.getOperatorId()]);
				buffy.append(formatForSql(cc.getColumn(),context.get(cc.getClauseId())));
			} else if(c instanceof QueryDropdownClause) {
				QueryDropdownClause cc = (QueryDropdownClause) c;
				buffy.append(MetaDataUtils.getMappedColumn(cc.getColumn()));
				buffy.append(operators[cc.getOperatorId()]);
				buffy.append(formatForSql(cc.getColumn(),context.get(cc.getClauseId())));
			} else if(c instanceof EnumDropdownClause) {
				EnumDropdownClause cc = (EnumDropdownClause) c;
				buffy.append(MetaDataUtils.getMappedColumn(cc.getColumn()));
				buffy.append(operators[cc.getOperatorId()]);
				buffy.append(formatForSql(cc.getColumn(),context.get(cc.getClauseId())));
			} else if(c instanceof JoinClause) {
				JoinClause cc = (JoinClause) c;
				buffy.append(MetaDataUtils.getMappedColumn(cc.getParentColumn()));
				buffy.append('=');
				buffy.append(MetaDataUtils.getMappedColumn(cc.getChildColumn()));
			}
			
			buffy.append(" AND ");
		}		
	}
	
	private String formatForSql(String column, String value) {
		if(Boolean.TRUE.equals(MetaDataUtils.getNeedsQuotes(column))) {
			return "'" + value + "'";
		}
		
		return value;
	}
}
