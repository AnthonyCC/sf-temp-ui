package com.freshdirect.dataloader.autoorder.create.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class ResourceUtil {
	private final static Category LOGGER = LoggerFactory.getInstance( ResourceUtil.class );
	
	private static Properties properties = new Properties();
	private final static String PROP_DATASOURCE_CONN="datasource.conn";
	private final static String PROP_DATASOURCE_USER="datasource.user";
	private final static String PROP_DATASOURCE_PASSWORD="datasource.password";
	
	private final static String PROP_DATASOURCE_DEVCONN="datasource.devconn";
	private final static String PROP_DATASOURCE_DEVUSER="datasource.devuser";
	private final static String PROP_DATASOURCE_DEVPASSWORD="datasource.devpassword";
	
	private final static String PROP_NUM_THREADS="performance.numthreads";
	private final static String PROP_CONN_RESET="performance.connreset";
	
	private final static int DEFAULT_NUMTHREADS=15;
	private final static int DEFAULT_CONN_RESET=1000;
	
	public final static String PROP_FILENAME = "enterpriseservices.properties";
	
	private final static String CONSUMER_CLASS="com.freshdirect.autoorder.create.command.DefaultConsumer";
	private final static String CONSUMER_PROP="app.consumerclass";
	
	private final static String CONSUMER_ORDERLINE_COUNT="app.orderlinecount";
	
	
	public static void loadProperties() throws IOException{
			InputStream configStream =
				ClassLoader.getSystemClassLoader().getResourceAsStream(PROP_FILENAME);

			if(configStream == null){
				throw new IOException("Properties file not found on classpath");
			}
	        properties.load(configStream);
	        
			System.out.println("Loaded with the following properties:");
			for (Object element : properties.entrySet()) {
				Entry e = (Entry) element;
				System.out.println(e.getKey() + ":    " + e.getValue());
			}
	}
	
	static {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			loadProperties();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			LOGGER.error(sqle);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection c = DriverManager.getConnection(
				properties.getProperty(PROP_DATASOURCE_CONN), 
				properties.getProperty(PROP_DATASOURCE_USER), 
				properties.getProperty(PROP_DATASOURCE_PASSWORD));
		
		return c;
	}
	
	public static Connection getDevConnection() throws SQLException {
		Connection c = DriverManager.getConnection(
				properties.getProperty(PROP_DATASOURCE_DEVCONN), 
				properties.getProperty(PROP_DATASOURCE_DEVUSER), 
				properties.getProperty(PROP_DATASOURCE_DEVPASSWORD));
		
		return c;
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static String getConsumerClass() {
		String consumerClass = CONSUMER_CLASS;
		if(properties.getProperty(CONSUMER_PROP) != null) {
			consumerClass = properties.getProperty(CONSUMER_PROP);
		}
		return consumerClass; 
	}
	
	public static int getOrderLineCount() {		
		if(properties.getProperty(CONSUMER_ORDERLINE_COUNT) != null) {
			Integer.parseInt(properties.getProperty(CONSUMER_PROP));
		}
		return 3; 
	}
	
	public static int getNumThreads(){
		int numThreads = DEFAULT_NUMTHREADS;
		try {
			numThreads = new Integer(properties.getProperty(PROP_NUM_THREADS)).intValue();
		} catch (NumberFormatException e){
			System.out.println("Num Threads property invalid, using default " + DEFAULT_NUMTHREADS + " threads");
		}
		return numThreads; 
	}
	
	public static int getConnReset(){
		int connReset = DEFAULT_CONN_RESET;
		try {
			connReset = new Integer(properties.getProperty(PROP_CONN_RESET)).intValue();
		} catch (NumberFormatException e){
			System.out.println("ConnReset property invalid, using default " + DEFAULT_CONN_RESET + "ms");
		}
		return connReset;
	}
}
