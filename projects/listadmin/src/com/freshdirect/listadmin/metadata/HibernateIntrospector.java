package com.freshdirect.listadmin.metadata;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import Zql.ZExp;
import Zql.ZQuery;
import Zql.ZSelectItem;
import Zql.ZqlParser;

import com.freshdirect.listadmin.core.ListadminDaoFactory;

/**
 * This may be obsolete.  Since we can build objects dynamically from their
 * SQL why bother with all that dangerous mucking about with hibernate?
 */
public class HibernateIntrospector implements IntrospectorI {
	private Map fields      = null;
	private Map allFields   = null;
	private Map subSelects  = null;
	private Map columnMap   = null; 
	private Map tableMap    = null; 
	private Map extraWheres = null;
	private Map needsQuotes = null;
	private Map sqlMap      = null;
	private Map unparsed    = null;
	
	public HibernateIntrospector(Map fields2, Map allFields2, Map subSelects2, Map columnMap2, Map tableMap2, Map extraWheres2, Map needsQuotes2, Map sqlMap2, Map unparsed2) {
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
		// I wish there were some way we could avoid doing this, because it's expensive.
		// But, astoundingly, there seems to be no route from a session factory back
		// to its configuration
		Configuration cfg = new Configuration().configure();
		
		SessionFactory sf = ListadminDaoFactory.getInstance().getSessionFactory();
		Map metaData      = sf.getAllClassMetadata();
		
		for(Iterator it=metaData.keySet().iterator();it.hasNext();) {
			String name        = (String) it.next();
			ClassMetadata data = (ClassMetadata) metaData.get(name);
			String shortName   = getClassName(name);

			String propertyNames[] = data.getPropertyNames();
			Type types[]           = data.getPropertyTypes();
			
			for(int i=0;i<propertyNames.length;i++) {
				needsQuotes.put(shortName + "." + propertyNames[i],types[i] instanceof StringType ? Boolean.TRUE : Boolean.FALSE);
			}
			
			PersistentClass pc = cfg.getClassMapping(name);
			Table t            = pc.getTable();
			ZQuery stmt        = null;
			
			// at some point we may need to worry about:
			// t.getSchema();
			
			
			if(t != null) {
				String subSelect = t.getSubselect();
				if(subSelect != null) {
					subSelect = subSelect.replace('\n',' ').trim() + ";";
					
					subSelects.put(shortName,subSelect);
					
					try {
						ZqlParser parser = new ZqlParser();
						
						parser.initParser(new ByteArrayInputStream(subSelect.getBytes()));
						stmt = (ZQuery) parser.readStatement();
						
						ZExp zwhere = stmt.getWhere();
						extraWheres.put(shortName,zwhere.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if(stmt != null) {
					String mappedTables = "";
					
					for(Iterator it2=stmt.getFrom().iterator();it2.hasNext();) {
						mappedTables = mappedTables + it2.next();
						if(it2.hasNext()) mappedTables = mappedTables + ",";
					}
					
					tableMap.put(shortName,mappedTables);
				} else {
					tableMap.put(shortName, t.getName());
				}
				
				for(Iterator it2 = pc.getPropertyIterator();it2.hasNext();) {
					Property prop = (Property) it2.next();
					
					if(prop != null) {
						if(prop.getValue() instanceof SimpleValue) {
							SimpleValue value = (SimpleValue) prop.getValue();
							
							// We assume, for the time being at least, that each property maps to a single
							// column.
							Column column     = (Column) value.getColumnIterator().next();
							
							if(stmt != null) {
								// If this is a compound object (made up of multiple tables with
								// a subselect joining them) then we get the real column by looking
								// the sql (column.getName() would return Classname.fieldname)

								try {
									for(Iterator it3=stmt.getSelect().iterator();it3.hasNext();) {
										ZSelectItem zsi = (ZSelectItem) it3.next();
										String tmp      = zsi.getExpression().toString();
										if(tmp.toLowerCase().endsWith(column.getName().toLowerCase())) {
											columnMap.put(shortName + "." + prop.getNodeName(),tmp.toLowerCase());
										}
									}
								} catch (Throwable th) {}
							} else {
								// it's not a compound object, so column.getName() returns
								// tablename.fieldname
								columnMap.put(shortName + "." + prop.getNodeName(), t.getName() + "." + column.getName());	
							}
						}
					}
				}
			}

			
			String fieldsA[] = data.getPropertyNames();
			List fieldsL     = new ArrayList();
			for(int i=0;i<fieldsA.length;i++) {
				fieldsL.add(fieldsA[i]);
			}
			
			List allFieldsL = new ArrayList();
			allFieldsL.addAll(fieldsL);
			allFieldsL.add(data.getIdentifierPropertyName());
			
			fields.put(shortName,fieldsL);
			allFields.put(shortName,allFieldsL);		
		}
	}
	
	public static String getClassName(String name) {
		int pos = name.lastIndexOf(".");
		return name.substring(pos+1);
	}
}
