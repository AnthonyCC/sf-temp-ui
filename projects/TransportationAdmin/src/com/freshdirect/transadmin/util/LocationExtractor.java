package com.freshdirect.transadmin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.freshdirect.transadmin.datamanager.IRouteFileManager;
import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.OrderInfoModel;

public class LocationExtractor {
	
	private final static String localLookup = "t3://localhost:7001";
	
	private final static String standByLookup = "t3://appS1.nyc2.freshdirect.com:7001";
	
	private final static String ADDRESSLIST =  "C:\\UPSData\\Location\\LOCATIONFULL.ser";
	
	private final static String ADDRESSDESTINAIONLIST =  "C:\\UPSData\\Location\\unit\\";
			
	private static final String locationConfig = "com\\freshdirect\\routing\\util\\extractor\\routinglocationfilemapping.xml";
	
	
	
	private static final String ROW_IDENTIFIER = "row";
	
	private static final String ROW_BEAN_IDENTIFIER = "rowBean";
	
	public static void main(String a[]) throws Exception {
		//loadLocation(getConnection(), ADDRESSLIST);
		
		//splitFile(ADDRESSLIST, ADDRESSDESTINAIONLIST,10000);
		processLocationList(ADDRESSDESTINAIONLIST, 14, "address");
		
	}
	
	private static void processLocationList(String source, int count, String prefix) throws Exception  {
		IRouteFileManager fileManager = new RouteFileManager();
		for(int intCount=1;intCount<count;intCount++) {
			List outputDataList = deSerializeData(source+prefix+intCount+".ser");
			fileManager.generateRouteFile(TransportationAdminProperties.getRoutingLocationInputFormat()
					, source+"location"+intCount+".txt", ROW_IDENTIFIER,ROW_BEAN_IDENTIFIER, outputDataList,
					null);
		}
	}
	
	private static void splitFile(String source, String destination, int groupSize) {
		
		System.out.println("----------------------START splitFile--------------------------");
		ArrayList addressList = deSerializeData(source);
		
		ArrayList tmpList = new ArrayList();
		Iterator iterator = addressList.iterator();	
		int intCount = 0;
		int intRowCount = 0;
				
		while (iterator.hasNext()) {
			
			tmpList.add(iterator.next());
			intCount++;
			
           	if(intCount == groupSize) {
           		System.out.println("COMPLETE STAGE FAILED="+intCount);
           		intRowCount++;
           		serializeData(tmpList, destination+"address"+intRowCount+".ser");
           		
           		tmpList = new ArrayList();
           		intCount = 0;
           	}
           	
		}
		intRowCount++;
		serializeData(tmpList, destination+"address"+intRowCount+".ser");
			
		System.out.println("----------------------END splitFile--------------------------"+addressList.size());
	}

	private static void loadLocation(Connection connection, String destinationFile) throws Exception  {
		
		Statement st = null;
		ResultSet rs = null;
		
		try {
			
			ArrayList addressList = new ArrayList();
			
			st = connection.createStatement();
			System.out.println("----------------------START QUERY--------------------------");
			rs = st.executeQuery("SELECT * FROM DLV.DELIVERY_BUILDING");	
			System.out.println("----------------------END QUERY--------------------------");
			OrderInfoModel infoModel = null;
						
			while (rs.next()) {
				infoModel = new OrderInfoModel();
				infoModel.setLocationId(rs.getString("ID"));
				infoModel.setStreetAddress1(rs.getString("SCRUBBED_STREET"));
				infoModel.setApartmentNumber(null);
				infoModel.setCity(null);
				infoModel.setState(null);
				infoModel.setZipCode(rs.getString("ZIP"));
				infoModel.setCountry(rs.getString("COUNTRY"));
				infoModel.setEquipmentType(null);
				infoModel.setDeliveryZone(null);
				infoModel.setLatitude(rs.getString("LATITUDE"));
				infoModel.setLongitude(rs.getString("LONGITUDE"));
	           	
				addressList.add(infoModel);
	           	
			}			
			System.out.println("ADDRESS LIST:"+addressList.size());
			System.out.println("----------------------START SERIALIZE--------------------------");
			serializeData(addressList, destinationFile);
		} finally {
			if(rs != null)
				rs.close();
			if(st != null)
				st.close();
			connection.close();
		}
	}
	
	private static ArrayList deSerializeData(String link) {		
        return (ArrayList)deSerialize(link);
	}
	
	private static Object deSerialize(String link) {
		//System.out.println("----------------------START DESRIALIZE--------------------------");
		Object button = null;
		try {
			 // Deserialize from a file
	        File file = new File(link);
	        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
	        button = in.readObject();
	        in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		//System.out.println("----------------------END DESRIALIZE--------------------------");
        return button;
	}
	
	private static void serializeData(Object object, String link) {
		
		try {
		//		 Serialize to a file
	    ObjectOutput out = new ObjectOutputStream(new FileOutputStream(link));
	    out.writeObject(object);
	    out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
	    try {
	      return getDataSource(true,"fddatasource").getConnection();
	        } catch (NamingException ne) {
	        	ne.printStackTrace();
	            throw new SQLException("Unable to find DataSource to get Connection: " + ne.getMessage());
	    }
	  }

	public static DataSource getDataSource(boolean local, String source) throws NamingException {
	      InitialContext initCtx = null;
	        try {
	        	Hashtable ht = new Hashtable();
	        	  ht.put(Context.INITIAL_CONTEXT_FACTORY,
	        	         "weblogic.jndi.WLInitialContextFactory");
	        	  if(local) {
	        		  ht.put(Context.PROVIDER_URL,localLookup);
	        	  } else {
	        		  ht.put(Context.PROVIDER_URL,standByLookup);
	        	  }
	            initCtx = new InitialContext(ht);
	            return (DataSource) initCtx.lookup(source);//"java:comp/env/jdbc/dbpool"
	        } finally {
	          if (initCtx!=null) try {
	            initCtx.close();
	          } catch (NamingException ne) {}
	        }
	     }
	}
