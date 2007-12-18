package com.freshdirect.listadmin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.core.ListadminDaoFactoryTestHelper;
import com.freshdirect.listadmin.db.Clause;
import com.freshdirect.listadmin.db.ConstantClause;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.ParamClause;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.db.StoredQueryValue;
import com.freshdirect.listadmin.db.Template;
import com.freshdirect.listadmin.db.TemplateField;
import com.freshdirect.listadmin.db.TemplateGroupBy;
import com.freshdirect.listadmin.db.TemplateOrderBy;
import com.freshdirect.listadmin.db.VirtualObject;
import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;



public class ListAdminTests extends TestCase {
	private static boolean hibernateSetup = false;
	
	ListadminDao		dao;
	
	static {
		// Configure log4j.
		DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.xml"));
	}
	
	public void setUp() throws Exception {		
		// Create a cute lil' hsqldb database for testing.  No need to 
		// bother Oracle with this stuff.
		
		Class.forName("org.hsqldb.jdbcDriver");
		
		// The . means in-memory - will not be written to disk
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa","");
		Statement stmt  = conn.createStatement();
		
		execSqlFile(stmt,"com/freshdirect/listadmin/createSql");
		
		stmt.close();
		
		
		// Set up hibernate
		
			Configuration cfg = new Configuration()
			.setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver")
			.setProperty("hibernate.connection.url","jdbc:hsqldb:.")
			.setProperty("hibernate.connection.username","sa")
			.setProperty("hibernate.connection.password","")
			.setProperty("hibernate.connection.pool_size","1")
			.setProperty("hibernate.show_sql","true")
			.setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect")
			.setProperty("hibernate.hbm2ddl.auto","update")
			.addClass(VirtualObject.class)
			.addClass(Template.class)
			.addClass(TemplateField.class)
			.addClass(TemplateOrderBy.class)
			.addClass(TemplateGroupBy.class)
			.addClass(Clause.class)
			.addClass(StoredQuery.class)
			.addClass(StoredQueryValue.class);
			
			SessionFactory factory = cfg.buildSessionFactory();
			
			ListadminDaoFactoryTestHelper.setSessionFactory(factory);
			dao = ListadminDaoFactory.getInstance().getListadminDao();
			
			hibernateSetup = true;
		
		
		// Although the listadmin infrastructure gets everything via hibernate.cfg, the
		// queries themselves need a JNDI datasource to run.  Fortunately, JavaRanch 
		// (http://www.javaranch.com/codebarn.jsp) provides a set of classes that
		// simulate a jndi environment!
		
		URL url =  getClass().getClassLoader().getResource("com/freshdirect/listadmin/jndiDatasources.properties");
		
		if(JNDIUnitTestHelper.notInitialized()){
			JNDIUnitTestHelper.init(url.getFile());
		}
		
		dao.beginTransaction();
	}
	
	private void execSqlFile(Statement stmt, String filename) throws Exception {
		// There are too many tables with too many columns to inline it all 
		// here, get it from an external file
		InputStream in     = getClass().getClassLoader().getResourceAsStream(filename);
		BufferedReader inr = new BufferedReader(new InputStreamReader(in));
		String line;
		
		while((line = inr.readLine()) != null) {
			try {
				stmt.executeUpdate(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		inr.close();
		in.close();
	}
	
	public void tearDown() throws Exception {
		try {
			dao.commitTransaction();
		} catch (Exception r) {
			r.printStackTrace();
		}
		
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa","");
		Statement stmt  = conn.createStatement();
		
		execSqlFile(stmt,"com/freshdirect/listadmin/deleteSql");
		
		stmt.close();
		
	}
	
	/**
	 * Just to make sure the javaranch stuff is working
	 * @throws Exception
	 */
	public void testJndi() throws Exception {
		DataSource ds = JndiWrapper.getDataSource("testDB");
		
		assertNotNull(ds);
	}
	
	public void testSimpleQuery() {
		StoredQuery sq = makeSimpleQuery("select * from test_table"); 
		
		assertEquals("select * from (select * from test_table)",sq.getSql());		
	}
	
	public void testConstantClause() {
		ConstantClause cc = new ConstantClause();
		cc.setColumn("foo");
		cc.setOperatorId(1);
		cc.setConstant("bar");
		
		StoredQuery sq = makeQueryWithClause(cc);
		
		assertEquals("select * from (select * from test_table) WHERE foo=bar",sq.getSql());		
	}

	public void testParamClause() {
		ParamClause pc = new ParamClause();
		pc.setColumn("foo");
		pc.setOperatorId(1);
		pc.setParam("bar");

		StoredQuery sq = makeQueryWithClause(pc);

		StoredQueryValue sqv = new StoredQueryValue();
		sqv.setName(pc.getClauseId());
		sqv.setValue("baz");
		sq.getValues().add(sqv);
		
		Session sess = dao.currentSession();
		sess.save(sq);
		
		assertEquals("select * from (select * from test_table) WHERE foo=baz",sq.getSql());		
	}

	public void testEmbeddedParamClause() throws Exception {
		StoredQuery sq = makeSimpleQuery("select * from test_table where foo={bar}");
		List l         = sq.getTemplate().getAllClauses();
		
		assertEquals(1,l.size());
		
		ParamClause pc = (ParamClause) l.get(0);
		
		assertEquals(pc.getClauseId(),"bar");
	}
	
	public void testEmbeddedQueryClause() throws Exception {
		// Create a query we can reference
		try {
			dao.beginTransaction();
			makeSimpleQuery("select * from test_table", "menuQuery");
			dao.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StoredQuery sq1 = makeSimpleQuery("select * from test_table where foo={bar:query=menuQuery}");
		List l         = sq1.getTemplate().getAllClauses();
		
		assertEquals(1,l.size());
		
		ParamClause pc = (ParamClause) l.get(0);
		
		assertEquals(pc.getClauseId(),"bar");
	}
	
	public void testResolvedEmbeddedParamClause() {
		doTestEmbeddedClause("select * from test_table where foo={bar}");
	}

	public void testResolvedEmbeddedQueryClause() {
		doTestEmbeddedClause("select * from test_table where foo={bar:query=fake}");
	}

	public void testResolvedEmbeddedSelectClause() {
		doTestEmbeddedClause("select * from test_table where foo={bar:select=fake1,fake2,fake3}");
	}
	
	public void doTestEmbeddedClause(String sql) {
		StoredQuery sq = makeSimpleQuery(sql);

		StoredQueryValue sqv = new StoredQueryValue();
		sqv.setName("bar");
		sqv.setValue("baz");
		sq.getValues().add(sqv);
		
		Session sess = dao.currentSession();
		sess.save(sq);
		
		assertEquals("select * from (select * from test_table where foo=baz)",sq.getSql());		
	}
	
	private StoredQuery makeSimpleQuery(String sql) {
		return makeSimpleQuery(sql,"test");
	}
	
	private StoredQuery makeSimpleQuery(String sql, String name) {
		Session sess = dao.currentSession();
		
		VirtualObject vo = new VirtualObject();
		vo.setName(name);
		vo.setSqlText(sql);
		sess.save(vo);
		
		Template t = new Template();
		t.setName(name);
		t.getObjects().add(vo);
		sess.save(t);
		
		StoredQuery sq = new StoredQuery();
		sq.setDataSourceName("testDB");
		sq.setName(name);
		sq.setTemplate(t);
		
		sess.save(sq);
		
		return sq;
	}
	
	private StoredQuery makeQueryWithClause(Clause c) {
		Session sess = dao.currentSession();
		
		VirtualObject vo = new VirtualObject();
		vo.setName("test");
		vo.setSqlText("select * from test_table");
		sess.save(vo);
		
		Template t = new Template();
		t.setName("test");
		t.getObjects().add(vo);
		sess.save(t);
		
		t.getClauses().add(c);
		sess.save(t);
		
		StoredQuery sq = new StoredQuery();
		sq.setDataSourceName("testDB");
		sq.setName("test");
		sq.setTemplate(t);
		
		sess.save(sq);

		return sq;
	}
}
