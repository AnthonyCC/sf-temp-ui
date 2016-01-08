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
    public final static String MATCH="Match";

    private final static Logger LOGGER = LoggerFactory.getInstance(AddressScrubbingLoaderSessionBean.class);

    private final static String QUERY_ADDRESS_COUNT = "SELECT count(id) totalcount FROM CUST.ADDRESS " +
    		" where EXT_SCRUBBED_ADDRESS is null and EXT_SCRUB_STATUS IS NULL";
    		
	private final static String QUERY_SELECT_ADDRESS = "SELECT ID,address1,address2,apartment,city,state,zip " +
			" FROM CUST.ADDRESS where EXT_SCRUBBED_ADDRESS is null and EXT_SCRUB_STATUS IS NULL and rownum < 100000";
	
	private final static String QUERY_UPDATE_SS_SCRUBBED_ADDRESS =
		"UPDATE CUST.ADDRESS set EXT_SCRUBBED_ADDRESS = ?,EXT_SCRUB_STATUS = ?  WHERE id = ? ";
	
	private final static String QUERY_SELECT_EXCEPTION_ADDRESS = "SELECT ID,SCRUBBED_ADDRESS,CITY,STATE,ZIPCODE FROM DLV.ZIPPLUSFOUR_EXCEPTIONS where EXT_IS_VALID = 'Y' ";
	
	private final static String QUERY_UPDATE_ZIPPLUSFOUR_EXCEPTIONS =
			"UPDATE DLV.ZIPPLUSFOUR_EXCEPTIONS set EXT_IS_VALID = ? WHERE id = ? ";

	/**
	 * @return
	 */
	public long getTotalAddressCount(){
		
		LOGGER.info("Inside getTotalAddressCount() ..");
		Connection con = null;
		long count=0;

		try {
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_ADDRESS_COUNT);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				count = rs.getLong("totalcount");
			}
			rs.close();
			ps.close();            

		} catch (Exception e) {
			LOGGER.error("Exception while getting address count from DB "+e.getMessage());
			throw new EJBException(e);
		} finally {
                    close(con);
		}
		LOGGER.info("Exist getTotalAddressCount() ..");
		return count;
	}
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
			LOGGER.info("SmartyStreets Scrbbing Address List size = "+addressList.size());
			List<AddressScrubbingResponse> addressScrubbingResponses = getScrubbedAddress(addressList);
			// Update CUST.ADDRESS table 
			scrubbedAddBatchUpdate(addressScrubbingResponses);
			LOGGER.info("UPDATING CUST.ADDRESS IS COMPLETED.......");
			LOGGER.info("SCRUBBING BATCH PROCESS COMPLETED....");
		}else{
			LOGGER.info("No Address found for scrubbing..");
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
	 * Get all the address from DB for scrubbing
	 * @param maxRecords
	 * @return
	 */
	private List<ScrubbedAddress> getCustomerAddressModel() 
	{
		LOGGER.info("Inside getCustomerAddressModel() ..");
		Connection con = null;
		List<ScrubbedAddress> addressList = new ArrayList<ScrubbedAddress>();

		try {
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_ADDRESS);
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
/*	public void scrubbedAddLogBatchInsert(List<AddressScrubbingResponse>  addressScrubbingResponses) {
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
	}*/
	
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
				System.out.println("scrubbedAddresses size : "+scrubbedAddresses.size());
				for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
					System.out.println(" Update scrubbedAddresses status : "+scrubbedAddress.getScrubbingResult()+" ID : "+scrubbedAddress.getId());
					ps.setString(1, scrubbedAddress.getSsScrubbedAddress());
					ps.setString(2, scrubbedAddress.getScrubbingResult());
					ps.setString(3, scrubbedAddress.getId());
					ps.addBatch();
				    if(++count % batchSize == 0) {
				        ps.executeBatch();
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
	
	
	/**
	 * @throws RemoteException
	 */
	public void verifyExceptionAddress() throws RemoteException{
		// connect to db and get the records
		// start scrubbing for the records retrieved 
		// once done finish quietly
		LOGGER.info("Inside verifyExceptionAddress: Pull all the addresses for scrubbing");
		List<ScrubbedAddress> addressList=getExceptionAddressModel();
		if(addressList != null && addressList.size()>0){
			LOGGER.info("Scrbbing Address List size = "+addressList.size());
			System.out.println("Scrbbing Address List size = "+addressList.size());
			List<AddressScrubbingResponse> addressScrubbingResponses = getScrubbedAddress(addressList);
//			displayScrubbedAddress(addressScrubbingResponses);
			// Update Exception Table
			scrubbedExceptionAddressBatchUpdate(addressScrubbingResponses);
			LOGGER.info("Exception Address Validation Exception BATCH PROCESS COMPLETED....");
		}else{
			LOGGER.info("No Exception Address found for validation..");
			System.out.println("No Address found for scrubbing");
		}
	}
	
	/**
	 * Get all the Exception address from DB for scrubbing  
	 * @param maxRecords
	 * @return
	 */
	private List<ScrubbedAddress> getExceptionAddressModel() 
	{
		LOGGER.info("Inside getExceptionAddressModel() ..");
		Connection con = null;
		List<ScrubbedAddress> addressList = new ArrayList<ScrubbedAddress>();

		try {
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_EXCEPTION_ADDRESS);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ScrubbedAddress address=new ScrubbedAddress();
				address.setId(rs.getString("ID"));
				address.setAddress1(rs.getString("SCRUBBED_ADDRESS"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIPCODE"));
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
		LOGGER.info("Exist getExceptionAddressModel() ..");
		return addressList;
	}
	
	/**
	 * Update DLV.ZIPPLUSFOUR_EXCEPTIONS table with Scrubbed address
	 * @param addressScrubbingResponses
	 */
	public void scrubbedExceptionAddressBatchUpdate(List<AddressScrubbingResponse>  addressScrubbingResponses){
		LOGGER.info("Inside scrubbedExceptionAddressBatchUpdate() ..");
		Connection connection = null ;
		try{
			connection = this.getConnection();
			PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE_ZIPPLUSFOUR_EXCEPTIONS);
			final int batchSize = 1000;		// JDBC batch size is 1000
			int count = 0;
			for (AddressScrubbingResponse response : addressScrubbingResponses) {
				List<ScrubbedAddress> scrubbedAddresses = response.getScrubbedAddress();
				for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
					if(scrubbedAddress.getScrubbingResult() != null && scrubbedAddress.getScrubbingResult().equals(MATCH)){
						ps.setString(1, "N");
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
			LOGGER.error("Exception while Update into ZIPPLUSFOUR_EXCEPTIONS table "+exception.getMessage());
			exception.printStackTrace();
		}finally{
               close(connection);
		}
		LOGGER.info("Exist scrubbedExceptionAddressBatchUpdate() ..");
	}
 }

