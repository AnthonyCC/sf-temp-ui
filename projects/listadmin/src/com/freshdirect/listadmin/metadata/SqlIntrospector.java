package com.freshdirect.listadmin.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.freshdirect.framework.util.HashMessageFormat;
import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.VirtualObject;

public class SqlIntrospector implements IntrospectorI {
	private Map fields      = null;
	private Map allFields   = null;
	private Map subSelects  = null;
	private Map columnMap   = null; 
	private Map tableMap    = null; 
	private Map extraWheres = null;
	private Map needsQuotes = null;
	private Map sqlMap      = null;
	private Map unparsed    = null;
	
	public SqlIntrospector(Map fields2, Map allFields2, Map subSelects2, Map columnMap2, Map tableMap2, Map extraWheres2, Map needsQuotes2, Map sqlMap2, Map unparsed2) {
		fields      = fields2;
		allFields   = allFields2;
		subSelects  = subSelects2;
		columnMap   =  columnMap2;
		tableMap    =  tableMap2;
		extraWheres = extraWheres2;
		needsQuotes = needsQuotes2;
		sqlMap      = sqlMap2;
		unparsed    = unparsed2;
	}
	
	
	public void introspect() {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();
		List 			l    = sess.createCriteria(VirtualObject.class).list();
		
		for(Iterator it=l.iterator();it.hasNext();) {
			doOneObject((VirtualObject) it.next());
		}
	}
	
	private void doOneObject(VirtualObject vo) {
		String sql       = vo.getSqlText();
		String shortName = vo.getName();		
		// ZQuery stmt      = null;
		Map aliases      = new HashMap();
		
		sqlMap.put(shortName,sql);
		
		/* There doesn't seem to be much to be gained by using Zql
		   as opposed to just wrapping the sql and asking the DB
		   for the metadata.
		 
		 if(!sql.endsWith(";")) {
			sql = sql + ";";
		 }
		
		try {
			ZqlParser parser = new ZqlParser();
			
			parser.initParser(new ByteArrayInputStream(sql.getBytes()));
			stmt = (ZQuery) parser.readStatement();
			
			// Use Zql to get the set of tables behind this "object"
			String mappedTables = "";

			for(Iterator it2=stmt.getFrom().iterator();it2.hasNext();) {
				ZFromItem item = (ZFromItem) it2.next();
				
				mappedTables = mappedTables + item;
				if(it2.hasNext()) mappedTables = mappedTables + ",";
				
				String tableName = item.getTable().toLowerCase();
				String alias     = item.getAlias();
				
				if(alias != null) {
					alias = alias.toLowerCase();
				}
				
				aliases.put(tableName,alias == null ? tableName : alias);
			}
			
			tableMap.put(shortName,mappedTables);
			
			
			// Also use Zql to get the where clause
			// Perhaps this should break the where into individual
			// clauses so they can be stored as ConstantClause objects.
			// But on the other hand this may not be needed.
			ZExp zwhere = stmt.getWhere();
			if(zwhere != null) {
				extraWheres.put(shortName,zwhere.toString());
			}
					
			// Getting the columns is a bit trickier.  We need to hit the database
			// in order to expand * and get the types.  So, append "and 1=0" to the
			// where clause so we can get the metdata without running some huge
			// query
			ZExpression limiter = new ZExpression("=",new ZConstant("1",ZConstant.NUMBER),new ZConstant("0",ZConstant.NUMBER));
			
			if(zwhere != null) {
				ZExp newWhere       = new ZExpression("AND", zwhere, limiter);
				stmt.addWhere(newWhere);
			} else {
				stmt.addWhere(limiter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			// Oh dear, Zql croaked.  
		}
		*/
		
		unparsed.put(shortName,Boolean.TRUE);
		
		sql = cleanParams(sql);
		
		// Take the original query "select ..." and turn it into 
		// select * from (select ...) where 1=0
	    // Then run it to get the metadata
		sql = "select * from (" + sql + ") where 1=0";
		
		// Parameters like {foo} are OK within strings, but not
		// as numbers, so we replace them all with 0.  This is
		// an invalid date but that's OK - we don't want any data
		// back anyway and Oracle seems not to get far enough for
		// the bad dates to be a problem.
		sql = HashMessageFormat.format(sql,new HashMap(),true,"0");
		
		
		List fieldsL = new ArrayList();
		
		try {
			Connection c = JndiWrapper.getConnection("testDB");
			
			Statement st           = c.createStatement();
			ResultSet rs           = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int colCount = rsmd.getColumnCount()+1;
			for(int i=1;i<colCount;i++) {
				String colName     = rsmd.getColumnName(i).toLowerCase();
				String tableName   = rsmd.getTableName(i).toLowerCase();
				String colTypeName = rsmd.getColumnTypeName(i);
				
				System.out.println(colName + " -- " + colTypeName);
				
				if(aliases.get(tableName) != null) {
					columnMap.put(shortName + "." + colName,aliases.get(tableName) + "." + colName);
				} else {
					columnMap.put(shortName + "." + colName,colName);
				}
				
				needsQuotes.put(shortName + "." + colName,
						colTypeName.startsWith("INT") ? Boolean.FALSE : Boolean.TRUE);
				
				fieldsL.add(colName);
			}
		} catch (Exception e) {
			// Even worse, the query died
			e.printStackTrace();
		}
		
		fields.put(shortName,fieldsL);
		allFields.put(shortName,fieldsL);		
	}
	
	
	public static String getClassName(String name) {
		int pos = name.lastIndexOf(".");
		return name.substring(pos+1);
	}

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
		
		System.out.println(buffy);
		
		return buffy.toString();
	}
}
