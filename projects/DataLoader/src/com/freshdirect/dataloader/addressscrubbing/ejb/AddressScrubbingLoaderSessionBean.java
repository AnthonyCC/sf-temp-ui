package com.freshdirect.dataloader.addressscrubbing.ejb;



import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.controller.data.response.AddressScrubbingResponse;
import com.freshdirect.logistics.delivery.dto.ScrubbedAddress;

/**
 * @author Aniwesh Vatsal
 *
 */
public class AddressScrubbingLoaderSessionBean extends SessionBeanSupport {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String SUCCESS = "SUCCESS";

    private final static Logger LOGGER = LoggerFactory.getInstance(AddressScrubbingLoaderSessionBean.class);

	private final static String QUERY_SELECT_ADDRESS = "SELECT ID,address1,address2,apartment,city,state,zip " +
			" FROM CUST.ADDRESS where id not in (SELECT addr.id from CUST.ADDRESS addr , cust.ss_scrubbing_log sslog where " +
			" addr.id = sslog.address_id) and SSSCRUBBED_ADDRESS is null";
	
	private final static String QUERY_UPDATE_SS_SCRUBBED_ADDRESS =
		"UPDATE CUST.ADDRESS set SSSCRUBBED_ADDRESS = ? WHERE id = ? ";
	
	private final static String QUERY_INSERT_SS_SCRUBBED_ADDRESS =
			"INSERT INTO CUST.SS_SCRUBBING_LOG (ADDRESS_ID , SCRUB_STATUS) values(?,?) ";
	
	private final static String DELETE_QUERY_SS_SCRUBBED_ADDRESS  = "DELETE from cust.ss_scrubbing_log where scrub_status ='No Match'";
	
	/**
	 * This method runs a Address SQL query against the CUST.ADDRESS and get all the address, that need scrubbing 
	 * and then scrubbed all of them using SmartyStreets.
	 * 
	 */
	public void getAddress() throws RemoteException{
		// connect to db and get the records
		// start scrubbing for the records retrieved 
		// once done finish quietly
		LOGGER.info("Inside getAddress: Pull all the addresses for scrubbing");
		List<ScrubbedAddress> addressList=getCustomerAddressModel();
		if(addressList != null && addressList.size()>0){
			LOGGER.info("Scrbbing Address List size = "+addressList.size());
			System.out.println("Scrbbing Address List size = "+addressList.size());
			List<AddressScrubbingResponse> addressScrubbingResponses = getScrubbedAddress(addressList);
			displayScrubbedAddress(addressScrubbingResponses);
			// Update CUST.ADDRESS table 
			scrubbedAddBatchUpdate(addressScrubbingResponses);
			LOGGER.info("UPDATING CUST.ADDRESS IS COMPLETED.......");
			System.out.println("UPDATING IS COMPLETED...");
			// INSERT into SS_SCRUBBING_LOG DB table
			scrubbedAddLogBatchInsert(addressScrubbingResponses);
			System.out.println("Record inserted into SSSCRUBBED_LOG Table is Completed...");
			LOGGER.info("SCRUBBING BATCH PROCESS COMPLETED....");
		}else{
			LOGGER.info("No Address found for scrubbing..");
			System.out.println("No Address found for scrubbing");
		}
	}
	
	
	/**
	 * Send address for scrubbing
	 * @param addressList
	 * @return
	 */
	private List<AddressScrubbingResponse> getScrubbedAddress(List<ScrubbedAddress> addressList) throws RemoteException{
		LOGGER.info("Inside getScrubbedAddress() ..");
		List<AddressScrubbingResponse> addressScrubbingResponses = new ArrayList<AddressScrubbingResponse>();
		try {
			int starIndex = 0;
			while(starIndex < addressList.size()){
				int endIndex = starIndex+99;
				if( endIndex > addressList.size()){
					endIndex = addressList.size();
				}
				AddressScrubbingResponse response = FDDeliveryManager.getInstance().getScrubbedAddress(addressList.subList(starIndex, endIndex));
				if(response != null && response.getStatus().equals(SUCCESS))
				{
					addressScrubbingResponses.add(response);
				}
				starIndex = endIndex;
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error("Exception while scrubbing the address "+e.getMessage());
		} catch (FDInvalidAddressException e) {
			LOGGER.error("Exception while scrubbing the address (Invalid Address) "+e.getMessage());
			e.printStackTrace();
		}
		
		LOGGER.info("Exist getScrubbedAddress() ..");
		return addressScrubbingResponses;
	}
	
	/**
	 * @param addressScrubbingResponses
	 */
	private void displayScrubbedAddress(List<AddressScrubbingResponse>  addressScrubbingResponses){
		LOGGER.info("Inside displayScrubbedAddress() ..");
		for(AddressScrubbingResponse addressScrubbingResponse: addressScrubbingResponses){
			List<ScrubbedAddress> scrubbedAddresses = addressScrubbingResponse.getScrubbedAddress();
			for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
				System.out.println("AddressId : "+scrubbedAddress.getId() +" Scrubbing Result : "+scrubbedAddress.getScrubbingResult());
			}
		}
		LOGGER.info("Exist displayScrubbedAddress() ..");
	}
	
	/**
	 * Get all the address from DB for scrubbing
	 * @param maxRecords
	 * @return
	 */
	private List<ScrubbedAddress> getCustomerAddressModel() 
	{
		LOGGER.info("Inside getCustomerAddressModel() ..");
		Connection con = null;
		List<ScrubbedAddress> addressList = new ArrayList<ScrubbedAddress>();

		//UserTransaction utx = null;
		try {
			//utx = this.getSessionContext().getUserTransaction();
			//utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_ADDRESS);
//			ps.setInt(1,maxRecords);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ScrubbedAddress address=new ScrubbedAddress();
				address.setId(rs.getString("ID"));
				address.setAddress1(rs.getString("address1"));
				address.setAddress2(rs.getString("address2"));
				address.setApartment(rs.getString("apartment"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				addressList.add(address);
			}

			rs.close();
			ps.close();            

		} catch (Exception e) {
			LOGGER.error("Exception while getting address from DB "+e.getMessage());
			throw new EJBException(e);
		} finally {
                    close(con);
		}
		LOGGER.info("Exist getCustomerAddressModel() ..");
		return addressList;
	}
	
	/**
	 * Insert the scrubbed address into DB
	 * @param addressScrubbingResponses
	 */
	public void scrubbedAddLogBatchInsert(List<AddressScrubbingResponse>  addressScrubbingResponses) {
		LOGGER.info("Inside scrubbedAddLogBatchInsert() ..");
		Connection connection = null ;
		try{
			connection = this.getConnection();
			PreparedStatement ps = connection.prepareStatement(QUERY_INSERT_SS_SCRUBBED_ADDRESS);
			final int batchSize = 1000;
			int count = 0;
			for (AddressScrubbingResponse response : addressScrubbingResponses) {
				List<ScrubbedAddress> scrubbedAddresses = response.getScrubbedAddress();
				for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
					ps.setString(1, scrubbedAddress.getId());
					ps.setString(2, scrubbedAddress.getScrubbingResult());
					ps.addBatch();
				    if(++count % batchSize == 0) {
				        ps.executeBatch();
				    }
				}
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		}catch(SQLException exception){
			LOGGER.error("Exception while Insert into SSSCRUBBED_LOG table "+exception.getMessage());
			exception.printStackTrace();
		}finally{
               close(connection);
		}
		LOGGER.info("Exist scrubbedAddLogBatchInsert() ..");
	}
	
	/**
	 * Update CUST.ADDRESS table with Scrubbed address
	 * @param addressScrubbingResponses
	 */
	public void scrubbedAddBatchUpdate(List<AddressScrubbingResponse>  addressScrubbingResponses){
		LOGGER.info("Inside scrubbedAddBatchUpdate() ..");
		Connection connection = null ;
		try{
			connection = this.getConnection();
			PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE_SS_SCRUBBED_ADDRESS);
			final int batchSize = 1000;		// JDBC batch size is 1000
			int count = 0;
			for (AddressScrubbingResponse response : addressScrubbingResponses) {
				List<ScrubbedAddress> scrubbedAddresses = response.getScrubbedAddress();
				for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
					if(scrubbedAddress.getSsScrubbedAddress() != null && scrubbedAddress.getSsScrubbedAddress().length() > 0 ){
						ps.setString(1, scrubbedAddress.getSsScrubbedAddress());
						ps.setString(2, scrubbedAddress.getId());
						ps.addBatch();
					    if(++count % batchSize == 0) {
					        ps.executeBatch();
					    }
					}
				}
			}
			ps.executeBatch(); // update remaining records
			ps.close();
		}catch(SQLException exception){
			LOGGER.error("Exception while Update into ADDREESS table "+exception.getMessage());
			exception.printStackTrace();
		}finally{
               close(connection);
		}
		LOGGER.info("Exist scrubbedAddBatchUpdate() ..");
	}
 }

