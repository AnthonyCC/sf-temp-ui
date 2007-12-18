package com.freshdirect.dataloader.geocodeloader.ejb;



import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.dataloader.geocodeloader.GeoCodeFailedException;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.command.Capture;
import com.freshdirect.payment.command.PaymentCommandI;
import com.freshdirect.payment.ejb.PaymentGatewayHome;
import com.freshdirect.payment.ejb.PaymentGatewaySB;
import com.freshdirect.payment.ejb.PaymentHome;
import com.freshdirect.payment.ejb.PaymentSB;

public class GeoCodeLoaderSessionBean extends SessionBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(GeoCodeLoaderSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

//	private final static String QUERY_SELECT_DELIVERYINFO =
//		" select *   from  ( select a.*, rownum rnum   from  ( "+ 
//		" select max(salesaction_id) as id, address1, scrubbed_address, zip, city, STATE   from cust.deliveryinfo "+ 
//		" where geoloc is null group by address1, scrubbed_address, zip, city, STATE order by id "+   
//		" ) a   where rownum <= ? ) where rnum > ? ";

	private final static String QUERY_SELECT_DELIVERYINFO =
			"  	select *   from  ( select a.*, rownum rnum   from  ( "+
			"   select salesaction_id as id, address1, scrubbed_address, zip, city, STATE   from cust.deliveryinfo where geoloc is null order by salesaction_id "+ 
			" ) a   where rownum <= ? ) where rnum > ? ";


	private final static String QUERY_UPDATE_DELIVERYINFO =
		"UPDATE CUST.DELIVERYINFO set GEOLOC=MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL)"+
		" WHERE salesaction_id=? ";
		//" WHERE SCRUBBED_ADDRESS=? and ZIP=? ";

	
	/**
	 * This method runs a AUTH_QUERY against the erpcustomer and get all the sales, that need payment authorization
	 * and then authorizes them one by one by calling ErpCustomerManager.
	 * 
	 */
	public void geoCodeDeliveryInfo(int startIndex, int endIndex) {
		// connect to db and get the records
		// start geocoding for the records retrieved 
		// once done finish quietly
		System.out.println("geoCodeDeliveryInfo  startIndex :"+startIndex+"endIndex :"+endIndex);
		List geoCodeList=new ArrayList();
	
		List addressList=getDeliveryInfoModel(startIndex,endIndex);
		
		// iterate through the list and generate geocode
		
		if(addressList.size()>0){
			
			for (int i=0;i<addressList.size();i++){
				
				AddressModel model=(AddressModel)addressList.get(i);
				try
				{
					geoCodeAddress(model);
				}
				catch(GeoCodeFailedException e){
					LOGGER.info("GeoCode failed for address : " + model.getAddress1()+"zip:"+model.getZipCode());
					continue;
				}
				catch(Exception e){
					LOGGER.info("Exception occured while processing Address : " + model.getAddress1()+"zip:"+model.getZipCode());
					continue;
				}
				
				
				geoCodeList.add(model);
			}
			// once geocode is generated update the database
			
			  System.out.println("GEOCODE LIST :"+geoCodeList.size());
			  updateDeliveryInfoModel(geoCodeList);

		}
		
		  //   
		  return;
	}
	
	
	public void updateDeliveryInfoModel(List addressList){
		Connection con = null;
		//List addressList = new ArrayList();
		//UserTransaction utx = null;
		
		try {
			//utx = this.getSessionContext().getUserTransaction();
			//utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_UPDATE_DELIVERYINFO);
			
			for(int i=0;i<addressList.size();i++){
			 
			    AddressModel model=(AddressModel)addressList.get(i);
			    ps.setDouble(1, model.getAddressInfo().getLongitude());
				ps.setDouble(2, model.getAddressInfo().getLatitude());
//				ps.setString(3,model.getScrubbedStreet());
//				ps.setString(4,model.getZipCode());
				ps.setString(3,model.getId());
				ps.executeUpdate();
				
			}			
			//ps.executeBatch();			
			ps.close();			
			//utx.commit();

		} catch (Exception e) {
			LOGGER.warn(e);
//			try {
//				//utx.rollback();
//			} catch (SystemException se) {
//				LOGGER.warn("Error while trying to rollback transaction", se);
//			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

	
	private void geoCodeAddress(AddressModel dlvAddress) throws GeoCodeFailedException, FDResourceException{		
		try {
			DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress(dlvAddress);
		    String geocodeResult = geocodeResponse.getResult();
		    if(!"GEOCODE_OK".equalsIgnoreCase(geocodeResult))
		    {
		    		throw new GeoCodeFailedException("ID :"+dlvAddress.getId());
		    }
			dlvAddress = geocodeResponse.getAddress();
			
		} catch (FDInvalidAddressException iae) {			
			throw new GeoCodeFailedException("ID :"+dlvAddress.getId());		
		}
		
	}	
	
	
	private List getDeliveryInfoModel(int startIndex, int endIndex)
	{
		Connection con = null;
		List addressList = new ArrayList();

		//UserTransaction utx = null;
		try {
			//utx = this.getSessionContext().getUserTransaction();
			//utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_DELIVERYINFO);
			ps.setInt(1,endIndex);
			ps.setInt(2,startIndex);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				
				AddressModel address=new AddressModel();
				address.setId(rs.getString("ID"));
				address.setAddress1(rs.getString("SCRUBBED_ADDRESS"));
				//address.setAddress2(rs.getString("ADDRESS2"));
				//address.setApartment(rs.getString("APARTMENT"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				address.setCountry("US");
				//address.setPhone( this.convertPhoneNumber(rs.getString("PHONE"), rs.getString("PHONE_EXT")) );
				//address.setInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
				AddressInfo info = new AddressInfo();
				info.setScrubbedStreet(rs.getString("SCRUBBED_ADDRESS"));
				address.setAddressInfo(info);
				addressList.add(address);
			}

			rs.close();
			ps.close();            
			//utx.commit();

		} catch (Exception e) {
			LOGGER.warn(e);
//			try {
//				//utx.rollback();				
//			} catch (SystemException se) {
//				LOGGER.warn("Error while trying to rollback transaction", se);
//			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		
		return addressList;
	}
	
 }

