package com.freshdirect;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.TestCase;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeTable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.mockejb.jndi.MockContextFactory;

public abstract class DbTestCaseSupport extends TestCase {

	private final static String JDBC_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=nyc1dbcl01-vip01.nyc1.freshdirect.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=nyc1dbcl01-vip02.nyc1.freshdirect.com)(PORT=1521))(LOAD_BALANCE=yes)(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=appunit_test)))";

	private final static String JDBC_USER = "fdstore";
	
	private final static String JDBC_PASSWORD = "fdstore";

	private final static String JNDI_PREFIX = "java:comp/env/jdbc/";
	
	private final static String DATASOURCE_NAME = "testDB";
	
	private final static DataSource DATASOURCE = createDataSource();

	protected Connection conn;
	private IDataSet metaData;
	private IDatabaseConnection dbConnection;

	public DbTestCaseSupport(String name) {
		super(name);
	}

	protected IDataSet loadDataSet(String dataSetName) throws DataSetException {
		return this.loadDataSet(dataSetName, this.metaData);
	}

	protected IDataSet loadDataSet(String dataSetName, IDataSet meta) throws DataSetException {
		InputStream is = getClass().getResourceAsStream(dataSetName);
		if (is==null) {
			throw new DataSetException("Dataset not found "+dataSetName);
		}
		Reader dataSetReader = new InputStreamReader(is);
		try {
			if (meta==null) {
				return new FlatXmlDataSet(dataSetReader);
			}
			return new FlatXmlDataSet(dataSetReader, meta);
		} catch (IOException e) {
			throw new DataSetException(e);
		}
	}

	protected void setUpDataSet(String dataSetName) throws DatabaseUnitException {
		IDataSet dataSet = this.loadDataSet(dataSetName);
		try {
			DatabaseOperation.INSERT.execute(this.dbConnection, dataSet);
		} catch (SQLException e) {
			throw new DatabaseUnitException(e);
		}
	}

	protected abstract String getSchema();
	protected abstract String[] getAffectedTables();

	protected IDataSet getActualDataSet() throws DatabaseUnitException {
		try {
			return this.dbConnection.createDataSet(this.getAffectedTables());
		} catch (SQLException e) {
			throw new DatabaseUnitException(e);
		}
	}
	
	protected void assertDataSet(String dataSet) throws Exception {
		Assertion.assertEquals(this.loadDataSet(dataSet), this.getActualDataSet());		
	}	

	/* Compares the reference dataSet (parameter) to the actual dataSet but ignores the columns given in the ignoreColumns map.
	 * The keys of the map are the table names, the values of the map are sets of strings designating the ignored column names in the specified table.
	 */
	protected void assertDataSet(String dataSet, Map ignoreColumns) throws Exception {
		IDataSet reference = this.loadDataSet(dataSet);
		ReplacementDataSet actual = new ReplacementDataSet(this.getActualDataSet());
		for (Iterator it = ignoreColumns.keySet().iterator(); it.hasNext();) {
			String tableName = (String) it.next();
			ITable refTable = reference.getTable(tableName);
			ITable actTable = actual.getTable(tableName);
			for (Iterator it2 = ((Set) ignoreColumns.get(tableName)).iterator(); it2.hasNext();) {
				String columnName = (String) it2.next();
				for (int i = 0; i < refTable.getRowCount(); i++) {
					Object replacement = refTable.getValue(i, columnName);
					Object original = actTable.getValue(i, columnName);
					actual.addReplacementObject(original, replacement);
				}
			}
		}
		Assertion.assertEquals(reference, actual);		
	}	
	
	protected void assertPartialDataSet(String dataSet, String tableName) throws Exception {
		ITable expected = this.loadDataSet(dataSet, null).getTable(tableName);
		CompositeTable actual = new CompositeTable(expected.getTableMetaData(), this.getActualDataSet().getTable(tableName));
		Assertion.assertEquals(expected, actual);
	}

	
	protected void dbUnitSetUp(Context context) throws Exception {
		context.rebind(JNDI_PREFIX + DATASOURCE_NAME, getDataSource());

		// set up the affected tables
		this.conn = this.getDataSource().getConnection();
		this.dbConnection = this.getSchema() != null ? new DatabaseConnection(this.conn, this.getSchema()) : new DatabaseConnection(this.conn);
		dbConnection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
		this.deleteTables(this.getAffectedTables());
		this.metaData = this.getActualDataSet();
		
	}
	
	protected void dbUnitRegisterPool(Context context) throws Exception {
		context.rebind(JNDI_PREFIX + "dbpool", getDataSource());
	}
	
	protected void dbUnitTearDown(Context context) throws Exception {
		// reset / clear the affected tables
		if (!this.conn.isClosed()) {
			this.conn.rollback();
			this.deleteTables(this.getAffectedTables());
			this.conn.close();
		}		
	}
	
	protected void setUp() throws Exception {
		// FIXME: the below assumes that we don't have a JNDI infrastructure below us
		//        i.e. we're _not_ running in a JNDI-capable container
		MockContextFactory.setAsInitial();
		Context context = new InitialContext();

		dbUnitSetUp(context);
	}

	private void deleteTables(String[] tables) throws SQLException {
		if (tables == null) return ;
		try {
			conn.setAutoCommit(false);
			for (int i = tables.length - 1; i >= 0; i--) {
				System.out.println("DbTestCaseSupport.deleteTables() " + tables[i]);
				conn.createStatement().execute("DELETE FROM " + tables[i]);
				
			}
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw e;			
		} finally {
			conn.setAutoCommit(true);
		}
	}

	protected void tearDown() throws Exception {
		dbUnitTearDown(null);
		// FIXME: the below assumes that we don't have a JNDI infrastructure below us
		//        i.e. we're _not_ running in a JNDI-capable container
		MockContextFactory.revertSetAsInitial();
	}

	protected DataSource getDataSource() {
		return DATASOURCE;
	}

	private static DataSource createDataSource() {
		
		// first create a DataSource
		OracleConnectionPoolDataSource datasource;
		try {
			datasource = new OracleConnectionPoolDataSource();
		} catch (SQLException e) {
			throw new RuntimeException("Unable to initialize datasource: " + e.getMessage());
		}

		datasource.setURL(JDBC_URL);
		datasource.setUser(JDBC_USER);
		datasource.setPassword(JDBC_PASSWORD);
		
		// then set up a mock JNDI repository, and associate the datasource with a name
		// FIXME: the below assumes that we don't have a JNDI infrastructure below us
		//        i.e. we're _not_ running in a JNDI-capable container
		try {
			MockContextFactory.setAsInitial();
			Context context = new InitialContext();
			context.bind(JNDI_PREFIX + DATASOURCE_NAME, datasource);
		} catch (NamingException e) {
			System.out.println("naming exception: " + e);
			e.printStackTrace();
		} finally {
			MockContextFactory.revertSetAsInitial();
		}
		
		return datasource;
	}

	/**
	 *  Execute a SQL script.
	 *  Currently the implementation is very simple, doesn't take
	 *  SQL comments into account, and just splits the supplied string
	 *  at ';' characters.
	 *  Later it could handle really everything needed.
	 *  
	 *  @param sqlScript the SQL script to execute.
	 *  @throws SQLException on errors.
	 */
	protected void executeSqlScript(String sqlScript) throws SQLException {
		StringTokenizer tokenizer = new StringTokenizer(sqlScript, ";");
		
		while (tokenizer.hasMoreTokens()) {
			String    sqlCommand = tokenizer.nextToken().trim();
			if (sqlCommand.length() == 0) {
				break;
			}
			Statement stmt       = conn.createStatement();
			
			stmt.executeUpdate(sqlCommand);
			
			stmt.close();
		}
	}
}
