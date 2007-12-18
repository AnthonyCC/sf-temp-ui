package com.freshdirect.listadmin.ui.page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.StoredQuery;

public class RunQuery extends AppPage implements IExternalPage {
	StoredQuery query = null;
	
	public void activateExternalPage(Object[] args, IRequestCycle cycle) {
		if(args.length != 0 && args[0] != null) {
			setStoredQueryId((String) args[0]);
		}
	}
	
	public String getStoredQueryId() {return query.getStoredQueryId();}
	public void setStoredQueryId(String storedQueryId) {
		ListadminDao dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session      sess = dao.currentSession();
		List         l    = sess.createCriteria(StoredQuery.class).add(Expression.idEq(storedQueryId)).list();
		query             = (StoredQuery) l.get(0);
	}
	
	public String getSql() {
		return query.getSql();
	}
	
	private boolean didRun = false;
	
	public List getResults() {
		if(!didRun) {
			try {
				run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return results;
	}
	

	public List getColNames() {
		if(!didRun) {
			try {
				run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return colNames;
	}

	
	private List results  = new ArrayList();
	private List colNames = new ArrayList();
	
	private void run() throws SQLException, NamingException {
		Connection conn = JndiWrapper.getConnection(query.getDataSourceName());
		String sql      = query.getSql();
		Statement st    = conn.createStatement();
		ResultSet rs    = st.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount    = rsmd.getColumnCount()+1;
		
		List headings   = new ArrayList();
		for(int i=1;i<colCount;i++) {
			// HTML IN JAVA!  NONONONONOOOOO!
			headings.add("<b>" + rsmd.getColumnLabel(i) + "</b>");
		}
		
		results.add(headings);
		
		while(rs.next()) {
			List row = new ArrayList();
			
			for(int i=1;i<colCount;i++) {
				row.add("" + rs.getObject(i));
			}
			
			results.add(row);
		}
		
		didRun = true;
	}

	public StoredQuery getQuery() {
		return query;
	}

	public void setQuery(StoredQuery query) {
		this.query = query;
	}
}
