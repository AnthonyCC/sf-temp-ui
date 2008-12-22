package com.freshdirect.routing.util.extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.DlvZipInfoModel;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.routing.util.extractor.model.ExtractAddressModel;

public class BaseExtractor {
	
	private final String localLookup = "t3://localhost:7001";

	private final String standByLookup = "t3://appS1.nyc2.freshdirect.com:7001";
	
	protected void serializeData(Object object, String link) {

		try {
			// Serialize to a file
			ObjectOutput out = new ObjectOutputStream(
					new FileOutputStream(link));
			out.writeObject(object);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() throws SQLException {
		try {
			return getDataSource(true, "fddatasource").getConnection();
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new SQLException(
					"Unable to find DataSource to get Connection: "
							+ ne.getMessage());
		}
	}

	protected DataSource getDataSource(boolean local, String source)
			throws NamingException {
		InitialContext initCtx = null;
		try {
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			if (local) {
				ht.put(Context.PROVIDER_URL, localLookup);
			} else {
				ht.put(Context.PROVIDER_URL, standByLookup);
			}
			initCtx = new InitialContext(ht);
			return (DataSource) initCtx.lookup(source);// "java:comp/env/jdbc/dbpool"
		} finally {
			if (initCtx != null)
				try {
					initCtx.close();
				} catch (NamingException ne) {
				}
		}
	}
	
	protected Object deSerialize(String link) {
		// System.out.println("----------------------START
		// DESRIALIZE--------------------------");
		Object button = null;
		try {
			// Deserialize from a file
			File file = new File(link);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));
			button = in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("----------------------END
		// DESRIALIZE--------------------------");
		return button;
	}
	
	protected void writeFile(String content , String file) {
		  try {
		        BufferedWriter out = new BufferedWriter(new FileWriter(file));
		        out.write(content);
		        out.close();
		        System.out.println("writeFile");
		    } catch (Exception e) {
		    	
		    	e.printStackTrace();	
		    }
   }
	
	protected void splitFile(String source, String destination,
			int groupSize, String prefix) {

		System.out
				.println("----------------------START splitFile--------------------------");
		ArrayList addressList = (ArrayList)deSerialize(source);

		ArrayList tmpList = new ArrayList();
		Iterator iterator = addressList.iterator();
		int intCount = 0;
		int intRowCount = 0;

		while (iterator.hasNext()) {

			tmpList.add(iterator.next());
			intCount++;

			if (intCount == groupSize) {
				System.out.println("COMPLETE STAGE FAILED=" + intCount);
				intRowCount++;
				serializeData(tmpList, destination + prefix + intRowCount
						+ ".ser");

				tmpList = new ArrayList();
				intCount = 0;
			}

		}
		intRowCount++;
		serializeData(tmpList, destination + prefix + intRowCount + ".ser");

		System.out
				.println("----------------------END splitFile--------------------------"
						+ addressList.size());
	}	
	
	protected AddressModel getFDAddress(ExtractAddressModel addressEx) {
		AddressModel address = null;
		if(addressEx != null) {
			address = new AddressModel();
			address.setAddress1(addressEx.getAddress1());
			address.setAddress2(addressEx.getAddress2());
			address.setApartment(addressEx.getApartment());
			address.setCity(addressEx.getCity());
			address.setState(addressEx.getState());
			address.setZipCode(addressEx.getZip());
			address.setCountry(addressEx.getCountry());
		}
		return address;
	}
	
	protected EnumDeliveryStatus getServiceStatus(List dlvInfos) {
		//
		// no geographic info for this zipcode, is pickup available?
		//
		if (dlvInfos.size() == 0) {
			return EnumDeliveryStatus.DONOT_DELIVER;
		}
		//
		// is delivery currently available?
		// find the largest coverage with the latest startDate earlier than
		// seven days from now
		//
		GregorianCalendar delWindCal = new GregorianCalendar();
		delWindCal.add(Calendar.DAY_OF_YEAR, 7);
		java.util.Date deliveryWindow = delWindCal.getTime();
		DlvZipInfoModel bestDelivery = null;
		
		for (Iterator i = dlvInfos.iterator(); i.hasNext();) {
		   DlvZipInfoModel dlvInfo = (DlvZipInfoModel) i.next();
		   if (dlvInfo.getStartDate().before(deliveryWindow)) {
			    if (bestDelivery == null) {
			     bestDelivery = dlvInfo;
			    } else {
			    	if (dlvInfo.getStartDate().after(bestDelivery.getStartDate()) && dlvInfo.getCoverage() > bestDelivery.getCoverage()) {
			    		bestDelivery = dlvInfo;
			    	} else if (DateUtil.isSameDay(bestDelivery.getStartDate(), dlvInfo.getStartDate())
			    		&& (dlvInfo.getCoverage() > bestDelivery.getCoverage())) {
			    		bestDelivery = dlvInfo;
			    	}
			    }
		   }
		}		
		
		if (bestDelivery != null) {
			if (bestDelivery.getCoverage() > 0.9) {
				return EnumDeliveryStatus.DELIVER;
			} 
			
			if (bestDelivery.getCoverage() >= 0.1) {
				return EnumDeliveryStatus.PARTIALLY_DELIVER;
			}
		}
		
		return EnumDeliveryStatus.DONOT_DELIVER;

	}
	
	protected String trimNoNull(String str) {
		return (str != null ? str.trim() : "");
	}
}
