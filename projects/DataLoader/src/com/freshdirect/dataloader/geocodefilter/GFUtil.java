package com.freshdirect.dataloader.geocodefilter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class GFUtil {
	private final static Category LOGGER = LoggerFactory.getInstance( GFUtil.class );
	
	private static Properties properties = new Properties();
	private final static String PROP_DATASOURCE_CONN="datasource.conn";
	private final static String PROP_DATASOURCE_USER="datasource.user";
	private final static String PROP_DATASOURCE_PASSWORD="datasource.password";
	private final static String PROP_NUM_THREADS="performance.numthreads";
	private final static String PROP_CONN_RESET="performance.connreset";
	
	private final static int DEFAULT_NUMTHREADS=15;
	private final static int DEFAULT_CONN_RESET=1000;
	
	public final static String PROP_FILENAME = "GeocodeFilter.properties";
	
	public final static String PROP_ADDRESS_STREET1 = "address.street1";
	public final static String PROP_ADDRESS_STREET2 = "address.street2";
	public final static String PROP_ADDRESS_CITY = "address.city";
	public final static String PROP_ADDRESS_STATE = "address.state";
	public final static String PROP_ADDRESS_ZIPCODE = "address.zipcode";
	
	public static void loadProperties() throws IOException{
			InputStream configStream =
				ClassLoader.getSystemClassLoader().getResourceAsStream(PROP_FILENAME);

			if(configStream == null){
				throw new IOException("Properties file not found on classpath");
			}
	        properties.load(configStream);
	        
			System.out.println("Loaded with the following properties:");
			for(Iterator i = properties.entrySet().iterator(); i.hasNext();){
				Entry e = (Entry) i.next();
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
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
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
