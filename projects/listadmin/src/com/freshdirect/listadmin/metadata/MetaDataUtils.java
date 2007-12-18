package com.freshdirect.listadmin.metadata;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This has grown somewhat unwieldy.  The idea is that at startup we collect various bits
 * of information about the objects we can present to the user: columns they can choose
 * to select and put in where/groupby/sortby clauses; underlying tables; and a number of
 * maps that go from object names to underlying table names and such.
 * 
 *  When this was just hibernate everything was pretty straightforward, just ask hibernate
 *  for each object, and the fields/properties within each object.
 *  
 *  Next we added "virtual objects" made of a simple query (select a1,b1,c1 from x1,y1,z1 where p1 and q1).
 *  These are pulled apart using Zql so that the business users can build up more complex queries by
 *  joining multiple such objects, for example selecting objects 1 and 2 and setting x1=x2 would render
 *  as 
 *  
 *  select a1,b1,c1,a2,b2,c2 from x1,y1,z1,x2,y2,z2 where p1 and q1 and p2 and q2 and x1=x2
 *  
 *  In order to merge the underlying sql for two different objects, it is necessary to 
 *  decompose them first.
 * 
 * But some queries are not simple enough to be parsed by Zql.  There's no way we can merge
 * such a query with another one, but we can at least parametarize them and add additional
 * constraints.  This is done by taking the sql and wrapping it as "select * from (original sql) where 1=0"
 * to get the metadata, and then replacing 1=0 with the additional constraints at runtime. 
 *  
 * @author lPekowsky
 *
 */
public class MetaDataUtils {
	private static Map fields      = null;
	private static Map allFields   = null;
	private static Map subSelects  = null;
	private static Map columnMap   = null; 
	private static Map tableMap    = null; 
	private static Map extraWheres = null;
	private static Map needsQuotes = null;
	private static Map sqlMap      = null;
	private static Map unparsed    = null;
	
	static {
		setup();
	}
	
	public static boolean getUnparsed(String name) {
		return unparsed.get(name) != null;
	}
	
	public static String getSql(String tableName) {
		String tmp = (String) sqlMap.get(tableName);
		
		if(tmp == null) {
			return "";
		}
		
		return tmp;
	}
	
	public static List getFields(String className) {
		return (List) fields.get(className);
	}
	
	public static List getAllFields(String className) {
		return (List) allFields.get(className);
	}
	
	public static String getSubSelect(String className) {
		return (String) subSelects.get(className);
	}
	
	public static Boolean getNeedsQuotes(String column) {
		return (Boolean) needsQuotes.get(column);
	}
	
	public static String getMappedColumn(String column) {
		String tmp = (String) columnMap.get(column);
		if(tmp == null) {
			return column;
		} else {
			return tmp;
		}
	}
	
	public static String getMappedTable(String table) {
		return (String) tableMap.get(table);
	}

	public static String getExtraWhere(String table) {
		return (String) extraWheres.get(table); 
	}
	public static Set getAvailableClasses() {
		return fields.keySet();
	}
	
	public static void setup() {
		fields      = new HashMap();
		allFields   = new HashMap();
		subSelects  = new HashMap();
		columnMap   = new HashMap();
		tableMap    = new HashMap();
		extraWheres = new HashMap();
		needsQuotes = new HashMap();
		sqlMap      = new HashMap();
		unparsed    = new HashMap();
		

		// HibernateIntrospector hi = new HibernateIntrospector(fields,allFields,subSelects,
		//		columnMap,tableMap,extraWheres,needsQuotes,sqlMap,unparsed);
		// hi.instrospect();
		// Or, alternately, don't.
		
		SqlIntrospector si = new SqlIntrospector(fields,allFields,subSelects,
				columnMap,tableMap,extraWheres,needsQuotes,sqlMap,unparsed);
		
		si.introspect();
	}
}
