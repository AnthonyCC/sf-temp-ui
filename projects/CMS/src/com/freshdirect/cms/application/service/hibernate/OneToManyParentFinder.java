package com.freshdirect.cms.application.service.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.OneToManyPersister;
import org.hibernate.type.CollectionType;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;

/**
 * Parent lookup strategy for one-to-many relationships.
 * 
 * @author lpekowsky
 */
class OneToManyParentFinder implements ParentFinderI {
	
	private final CollectionType type;
	private final Class parentClass;
	private final String propertyName;
	private final ContentType parentType;
	
	public OneToManyParentFinder(Class parentClass, ContentType parentType, String propertyName,CollectionType ct) {
		this.parentClass  = parentClass;
		this.propertyName = propertyName;
		this.parentType   = parentType;
		type = ct;
	}

	public List findParents(HibernateContentNode node, SessionFactory sessionFactory) {
		Session sess = sessionFactory.openSession();
		Object obj   = node.getObject();
		OneToManyPersister per = (OneToManyPersister) type.getAssociatedJoinable((SessionFactoryImplementor) sessionFactory);
		String eltCol = per.getElementColumnNames()[0];
		String keyCol = per.getKeyColumnNames()[0];
		
		Configuration cfg = new Configuration().configure();
		
		
		String parentTable = cfg.getClassMapping(parentClass.getName()).getTable().getName();
		String childTable  = cfg.getClassMapping(obj.getClass().getName()).getTable().getName();
			
		//ClassMetadata parentMeta = sessionFactory.getClassMetadata(parentClass);
		ClassMetadata childMeta  = sessionFactory.getClassMetadata(obj.getClass());
		
		
		// And it really kicks my head
		StringBuffer buffy = new StringBuffer();
		buffy.append("select ");
		buffy.append(parentTable);
		buffy.append(".");
		buffy.append("id"); // cfg.getClassMapping(parentClass.getName()).getTable().getPrimaryKey().

		buffy.append(" from ");
		buffy.append(parentTable);
		buffy.append(", ");
		buffy.append(childTable);
		
		buffy.append(" where ");
		buffy.append(parentTable);
		buffy.append(".");
		buffy.append(eltCol);
		buffy.append(" = ");
		buffy.append(childTable);
		buffy.append(".");
		buffy.append(keyCol);
		buffy.append(" and ");
		
		buffy.append(childTable);
		buffy.append(".");
		buffy.append("id");   // generalize.
		buffy.append("=");
		buffy.append("'");
		buffy.append(childMeta.getIdentifier(obj,EntityMode.POJO));
		buffy.append("'");
		
		System.out.println(buffy.toString());

		List l = new ArrayList();
		
		try {
			// How odd.  This next line
			// l = sess.createSQLQuery(buffy.toString()).addEntity(parentTable,parentClass).list();
			// results in the following being printed when showSql is on:
			// Hibernate: select test_table.id from test_table, test_relationship_table where test_table.id = test_relationship_table.test_table_id and test_relationship_table.id='testObject'
			// Hm, looks like perfectly cromulent sql to me.  And yet this causes a SqlException:
			//   Caused by: java.sql.SQLException: Column not found: id0_
			// So:
			//  (1) hibernate's sql printer is a big stinkin' lier!
			//  (2) createSQLQuery kinda sucks.
			// Oh well, looks like hibernate has failed us.  Back to JDBC!
			
			Connection conn = sess.connection();
			Statement st    = conn.createStatement();
			ResultSet rs    = st.executeQuery(buffy.toString());
			while(rs.next()) {
				l.add(new ContentKey(parentType,rs.getObject(1).toString()));
			}
			rs.close();
			st.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		sess.close();
		return l;
	}
}
