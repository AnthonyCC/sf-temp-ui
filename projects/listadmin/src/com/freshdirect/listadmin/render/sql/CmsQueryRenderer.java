package com.freshdirect.listadmin.render.sql;

import java.util.Iterator;

import com.freshdirect.listadmin.db.Clause;
import com.freshdirect.listadmin.db.ParamClause;
import com.freshdirect.listadmin.db.StaticDropdownClause;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.query.QueryI;

/**
 * This class is probably obsolete, it was written on the assumption that OCF queries would be
 * run from within CMS.
 * 
 * @author lPekowsky
 *
 */
public class CmsQueryRenderer extends SqlQueryRenderer {
	public Object renderQuery(QueryI query, QueryContextI context) {
		StoredQuery q = (StoredQuery) query;
		String theSql = (String) super.renderQuery(query,context);
		
		StringBuffer buffy = new StringBuffer();
		
		buffy.append("<CmsReport id=\"");
		buffy.append(q.getStoredQueryId());
		buffy.append("\">");
		
		buffy.append("<name>");
		buffy.append(q.getName());
		buffy.append("</name>");
		
		buffy.append("<description>");
		buffy.append("A description");
		buffy.append("</description>");
		
		buffy.append("<script>");
		buffy.append("<![CDATA[");
		buffy.append(theSql);
		buffy.append("]]>");
		buffy.append("</script>");
		
		appendParameters(q,buffy);		

		buffy.append("</CmsReport>");
		
		return buffy.toString();
	}
	
	
	private void appendParameters(StoredQuery q,StringBuffer buffy) {
		buffy.append("<parameters>");
		
		for(Iterator it=q.getClauses().iterator();it.hasNext();) {
			Clause c = (Clause) it.next();
			
			// This, as previously mentioned, sucks
			if(c instanceof ParamClause) {
				ParamClause cc = (ParamClause) c;
				// String dbName  = MetaDataUtils.getMappedColumn(cc.getColumn());
				buffy.append(cc.getParam() + "=" + cc.getParam());
			} else if(c instanceof StaticDropdownClause) {
				// StaticDropdownClause cc = (StaticDropdownClause) c;
			}
			
			if(it.hasNext()) 
				buffy.append("|");
		}
		
		buffy.append("</parameters>");
	}
}
