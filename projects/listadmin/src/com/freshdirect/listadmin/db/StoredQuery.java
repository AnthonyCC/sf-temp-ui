package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.query.ClauseI;
import com.freshdirect.listadmin.query.MapQueryContext;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.query.QueryI;
import com.freshdirect.listadmin.render.QueryRendererI;
import com.freshdirect.listadmin.render.sql.SqlQueryRenderer;

public class StoredQuery implements QueryI, Serializable {
	private String storedQueryId;
	private String name;
	private String dataSourceName;
	private Set values = new HashSet();
	private Template template;
	
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public Set getValues() {
		return values;
	}
	public void setValues(Set values) {
		this.values = values;
	}
	public String getStoredQueryId() {
		return storedQueryId;
	}
	public void setStoredQueryId(String storedQueryId) {
		this.storedQueryId = storedQueryId;
	}
	
	public String getSql() {
		QueryRendererI renderer = new SqlQueryRenderer();
		QueryContextI context   = new MapQueryContext();
		
		for(Iterator it=getValues().iterator();it.hasNext();) {
			StoredQueryValue v = (StoredQueryValue) it.next();
			
			context.put(v.getName(),v.getValue());
		}
		
		String sql = (String) renderer.renderQuery(this,context);
		
		return sql;
	}
	
	public DataSource getDataSource() throws SQLException, NamingException {
		return JndiWrapper.getDataSource(getDataSourceName());
	}
	
	public static StoredQuery getById(String id) {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();

		List l       = sess.createCriteria(StoredQuery.class).add(Expression.idEq(id)).list();
		
		if(l != null && l.size() != 0) {
			return (StoredQuery) l.get(0);
		}
		
		return null;
	}
	
	public static StoredQuery getByName(String name) {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();

		List l       = sess.createCriteria(StoredQuery.class).add(Expression.eq("name",name)).list(); 
		
		if(l != null && l.size() != 0) {
			return (StoredQuery) l.get(0);
		}
		
		return null;
	}
	 
	 
	//XXX: This should not be here!
	public void save() throws ParseException {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();
		
		dao.beginTransaction();
		
		// Clean out any old values
		for(Iterator it=getValues().iterator();it.hasNext();) {
			sess.delete(it.next());
		}
		getValues().clear();
		
		sess.save(this);
		
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		
		for(Iterator it=getTemplate().getAllClauses().iterator();it.hasNext();) {
			Clause c             = (Clause) it.next();
			String key           = c.getClauseId();
			StoredQueryValue sqv = null;
			Object val           = c.getRuntimeValue();
			
			// The myws date/time picker only works with 
			// mm/dd/yyyy format (and wants to give us a Date).
			// Oracle only works with dd-MMM-yyyy and needs a string.
			// Hence, this crud:
			if(val instanceof Date) {
				sqv = new StoredQueryValue(key,formater.format(val));
			} else {
				sqv = new StoredQueryValue(key,val.toString());	
			}
			
			sqv.setStoredQueryId(getStoredQueryId());
			getValues().add(sqv);
			sess.save(sqv);
		}
		
		dao.commitTransaction();
	}
	
	public boolean getRunnable(QueryContextI context) {
		return true;
	}
	
	public Set getFields() {
		return getTemplate().getFields();
	}
	
	public Set getObjects() {
		return getTemplate().getObjects();
	}
	
	public Set getTables() {
		return getTemplate().getObjects();
	}

	public void addClause(ClauseI clause) {
		// 
	}

	public Set getClauses() {
		Set ret = new HashSet();
		ret.addAll(getTemplate().getParamClauses());
		
		return ret;
	}
}
