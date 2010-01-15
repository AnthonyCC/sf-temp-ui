package com.freshdirect.cms.application.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.service.hibernate.HibernateContentService;


public class HibernateContentServiceTest extends TestCase {	
	static SessionFactory sessionFactory = null; 

	
	public void setUp() throws Exception {
		// Create a cute lil' hsqldb database for testing.  No need to 
		// bother Oracle with this stuff.
		
		Class.forName("org.hsqldb.jdbcDriver");
		
		// The . means in-memory - will not be written to disk
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa","");
		Statement stmt  = conn.createStatement();
		
		stmt.executeUpdate("create table test_table(id varchar(20), the_string varchar(30), the_other_class_id varchar(20), the_int int)");
		stmt.executeUpdate("create table test_relationship_table(id varchar(20), test_table_id varchar(20), message varchar(30))");
		
		stmt.execute("insert into test_table values('testObject','hello','testObject',8)");
		stmt.execute("insert into test_relationship_table values('testObject','testObject','hello again')");
		
		stmt.execute("insert into test_relationship_table values('testObject1','testObject','hello again1')");
		stmt.execute("insert into test_relationship_table values('testObject2','testObject','hello again2')");
		stmt.execute("insert into test_relationship_table values('testObject3','testObject','hello again3')");
		
		
		stmt.close();
		conn.close();
		
		if(sessionFactory == null) {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}
	
	public void tearDown() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa","");
		Statement stmt  = conn.createStatement();
		stmt.execute("drop table test_table");
		stmt.execute("drop table test_relationship_table");
		stmt.close();
		conn.close();
	}
	
	/*
	public void testObjectSimpleGet() throws Exception {
		Object theObject = new TestClass("testObject","hello",8,"hello again");
		
		HibernateContentService service = new HibernateContentService(sessionFactory, theObject);

		ContentNodeI testNode = service.getContentNode(TEST_KEY);
		assertEquals(4, testNode.getAttributes().size());
		assertEquals(new Integer(8), testNode.getAttributeValue("theInt"));
		assertEquals("hello", testNode.getAttributeValue("theString"));
	}
	*/
	
	public void testHibernateSimpleGet() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);

		ContentNodeI testNode = service.getContentNode(TEST_KEY);
		assertEquals(4, testNode.getAttributes().size());
		assertEquals(new Integer(8), testNode.getAttributeValue("theInt"));
		assertEquals("hello", testNode.getAttributeValue("theString"));
	}

	
	public void testCompoundGet() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);

		ContentNodeI testNode = service.getContentNode(TEST_KEY2);
		assertEquals(1, testNode.getAttributes().size());
		assertEquals(testNode.getAttributeValue("message"),"hello again");
	}
	
	public void testSimpleSet() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);

		ContentNodeI testNode = service.getContentNode(TEST_KEY);
		assertEquals("hello", testNode.getAttributeValue("theString"));
		
		String newString = "A different message";
		testNode.setAttributeValue("theString", newString);
		
		assertEquals(newString, testNode.getAttributeValue("theString"));
		
		CmsRequest req = new CmsRequest(null);
		req.addNode(testNode);		
		service.handle(req);
		
		testNode  = service.getContentNode(TEST_KEY);
		assertEquals(newString,testNode.getAttributeValue("theString"));
	}
	
	
	@SuppressWarnings("deprecation")
	public void testCreatePrototype() {
		HibernateContentService service = new HibernateContentService(sessionFactory);
		ContentNodeI node = service.createPrototypeContentNode(TEST_KEY);
		
		Map m = node.getAttributes();
		
		assertTrue(m.containsKey("theInt"));
		assertTrue(m.containsKey("theString"));
		assertTrue(m.containsKey("theSet"));
		
		AttributeI att = node.getAttribute("theString");
		att.setValue("I am an attribute");

		assertEquals(node.getAttributeValue("theString"),"I am an attribute");
	}
	
	public void testSimpleSaveNode() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);
		ContentNodeI node = service.createPrototypeContentNode(TEST_NEW_KEY);
		String testString = "I am an attribute";
		
		node.setAttributeValue("theString", testString);
		node.setAttributeValue("theInt", new Integer(88));
		
		CmsRequest req = new CmsRequest(null);
		req.addNode(node);
		
		service.handle(req);
		
		// Make sure it made it to the DB
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa","");
		Statement stmt  = conn.createStatement();
		ResultSet rs    = stmt.executeQuery("select * from test_table");
		boolean foundIt = false;
		int count       = 0;
		
		while(rs.next()) {
			foundIt = foundIt || testString.equals(rs.getString("the_string"));
			count++;
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		assertEquals(count,2);
		assertTrue(foundIt);
	}
	
	public void testManyToOneGet() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);

		ContentNodeI testNode = service.getContentNode(TEST_KEY);
		
		Object obj = testNode.getAttributeValue("theSet");
		
		assertTrue(obj instanceof Collection);
		
		Collection c = (Collection) obj;
		assertEquals(4,c.size());
		
		ContentKey key = new ContentKey(TEST_TYPE2,"testObject1");
		assertTrue(c.contains(key));
		
		testNode = service.getContentNode(key);
		assertEquals("hello again1", testNode.getAttributeValue("message"));
		
		testNode = service.getContentNode(new ContentKey(TEST_TYPE2,"testObject2"));
		assertEquals("hello again2", testNode.getAttributeValue("message"));
	}

	public void testHibernateSimpleUpdate() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);

		ContentNodeI testNode = service.getContentNode(TEST_KEY);
		assertEquals(4, testNode.getAttributes().size());
		String testString = "Yet another message";
		
		
		testNode.setAttributeValue("theString", testString);
		
		CmsRequest req = new CmsRequest(null);
		req.addNode(testNode);
		service.handle(req);
		
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa","");
		Statement stmt  = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select the_string from test_table where id='testObject'");
		
		rs.next();
		
		assertEquals(testString,rs.getString("the_string"));
		
		rs.close();
		stmt.close();
		conn.close();
	}		
	
	/*
	public void testParents() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);

		Set parents = service.getParentKeys(TEST_KEY2);
		
		assertEquals(1,parents.size());
	}
	
	public void testManyToOneParents() throws Exception {
		HibernateContentService service = new HibernateContentService(sessionFactory);
		ContentKey key = new ContentKey(TEST_TYPE2,"testObject1");
		
		Set parents = service.getParentKeys(key);
		
		assertEquals(1,parents.size());
		
		assertTrue(parents.contains(TEST_KEY));
	}
	*/

	
	private final static ContentType TEST_TYPE   = ContentType.get("TestClass");
	private final static ContentKey TEST_KEY     = new ContentKey(TEST_TYPE,"testObject");
	private final static ContentKey TEST_NEW_KEY = new ContentKey(TEST_TYPE,"testObject2");
	
	private final static ContentType TEST_TYPE2   = ContentType.get("TestRelationshipClass");
	private final static ContentKey TEST_KEY2     = new ContentKey(TEST_TYPE2,"testObject");
}
