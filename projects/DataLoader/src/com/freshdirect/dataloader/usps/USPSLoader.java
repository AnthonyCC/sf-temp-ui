
package com.freshdirect.dataloader.usps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipException;

import com.freshdirect.dataloader.usps.citystate.CityStateLoader;
import com.freshdirect.dataloader.usps.zipcode.ZipPlus4Loader;

public class USPSLoader {
	private static Properties properties = new Properties();
	private final static String PROP_DATASOURCE_CONN="datasource.conn";
	private final static String PROP_DATASOURCE_USER="datasource.user";
	private final static String PROP_DATASOURCE_PASSWORD="datasource.password";
	private final static String PROP_ZIPLOW="ziplow";
	private final static String PROP_ZIPHIGH="ziphigh";
	
	private final static String PROP_FILENAME = "USPSLoader.properties";
	
	private File zpfTempDir;
	private File csTempDir;
	
	private final String LOAD_PREP_PROC="USPS_LOAD_PREP";
	private final String FLIP_TABLES_PROC="USPS_LOAD_FLIP";
	
	
	public static void main(String[] args) throws IOException {
		String path = null;
		
		if(args.length > 0){
			path = args[0];
			USPSLoader loader = new USPSLoader();
			loader.loadProperties();
			loader.go(path);
		} else {
			System.out.println("Please pass in the path to the root data, if your loading off cd please indicate the drive letter.");
		}

	}
	
	private void go(String path){
		try{
			long startTime = System.currentTimeMillis();
			
			createTempDirs();
			unzipFiles(path);
			
			System.out.print("generating load tables...");
			runStoredProc(LOAD_PREP_PROC);
			System.out.print("[DONE]\n");
			
			loadZipPlus4();
			loadCityState();
			
			System.out.print("flipping tables...");
			runStoredProc(FLIP_TABLES_PROC);
			System.out.print("[DONE]\n");

			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
		    System.out.println("Finished loading - elapsed time:" + elapsedTime);
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			zpfTempDir.delete();
			csTempDir.delete();
		}
	}
	
	private void createTempDirs(){
		String tmpDir = System.getProperty("java.io.tmpdir");
		File file = new File(tmpDir + "zpfData");
		file.deleteOnExit();
		
		zpfTempDir = new File(tmpDir + "zpf");
		csTempDir = new File(tmpDir + "citystate");
		
		zpfTempDir.mkdir();
		System.out.println("created temp dir for zip4 - " + zpfTempDir.getAbsolutePath());
		
		csTempDir.mkdir();
		System.out.println("created temp dir for zip4 - " + csTempDir.getAbsolutePath());
	}
	
	private void unzipFiles(String path) throws ZipException, IOException{
		File rootDir = new File(path);
		if(!rootDir.isDirectory()){
			throw new FileNotFoundException(path + " is not a directory or could not be found");
		}

		unzipAllFiles(new File(rootDir.getAbsolutePath() + File.separator + "zip4"), zpfTempDir, true);
		unzipAllFiles(new File(rootDir.getAbsolutePath() + File.separator + "ctystate"), csTempDir, false);
	}
	
	private void runStoredProc(String proc) throws SQLException{
		
		CallableStatement cs = null;
		Connection conn = null;
	    try {
	    	conn = getConnection();
	      // Call a procedure with no parameters
	        cs = conn.prepareCall("{call " + proc + "}");
	        cs.execute();
		} catch(SQLException e){
			throw e;
		} finally{
			if(cs != null){
				cs.close();
			}
			if(conn != null){
				conn.close();
			}
		}
	}
	
	private void loadZipPlus4(){
		File[] files = zpfTempDir.listFiles();
		for(int i = 0; i < files.length; i++){
			File file = files[i];
			if(file.getName().indexOf(".txt") > -1){
				ZipPlus4Loader loader = new ZipPlus4Loader();
				loader.load(file.getAbsolutePath());
			}
		}
	}
	
	private void loadCityState(){
		File[] files = csTempDir.listFiles();
		for(int i = 0; i < files.length; i++){
			File file = files[i];
			if(file.getName().indexOf(".txt") > -1){
				CityStateLoader loader = new CityStateLoader();
				loader.load(file.getAbsolutePath());
			}
		}
	}
	
	private void unzipAllFiles(File dir, File destDir, boolean zpf) throws ZipException, IOException{
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			File file = files[i];
			if(zpf){
				int name = Integer.parseInt(file.getName().substring(0, file.getName().indexOf(".")));
				if(name < Integer.parseInt(properties.getProperty(PROP_ZIPLOW)) || name > Integer.parseInt(properties.getProperty(PROP_ZIPHIGH))){
					continue;
				}
			}
			Unzip.unzip(file, destDir);
		}
	}
	
	public void loadProperties() throws IOException{
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
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection c = DriverManager.getConnection(
				properties.getProperty(PROP_DATASOURCE_CONN), 
				properties.getProperty(PROP_DATASOURCE_USER), 
				properties.getProperty(PROP_DATASOURCE_PASSWORD));
		return c;
	}
	

}
