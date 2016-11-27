package com.freshdirect.dataloader.addressscrubbing.ejb;



import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
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
    public final static String NO_MATCH="No Match";
    public final static String SUGGESTIONS="Suggestions";

    private final static Logger LOGGER = LoggerFactory.getInstance(AddressScrubbingLoaderSessionBean.class);

    private final static String QUERY_ADDRESS_COUNT = "SELECT count(id) totalcount FROM CUST.ADDRESS " +
    		" where EXT_SCRUBBED_ADDRESS is null and EXT_SCRUB_STATUS IS NULL";
    		
	private final static String QUERY_SELECT_ADDRESS = "SELECT ID,address1,address2,apartment,city,state,zip " +
			" FROM CUST.ADDRESS where EXT_SCRUBBED_ADDRESS is null and EXT_SCRUB_STATUS IS NULL and rownum < 100000";
	
	private final static String QUERY_UPDATE_SS_SCRUBBED_ADDRESS =
		"UPDATE CUST.ADDRESS set EXT_SCRUBBED_ADDRESS = ?,EXT_SCRUB_STATUS = ?  WHERE id = ? ";
	
	private final static String QUERY_SELECT_EXCEPTION_ADDRESS_WITH_APT_RANGE = "SELECT ID,SCRUBBED_ADDRESS,CITY,STATE,ZIPCODE,APT_NUM_LOW,APT_NUM_HIGH FROM " +
			" DLV.ZIPPLUSFOUR_EXCEPTIONS WHERE EXT_IS_VALID = 'Y' and (APT_NUM_LOW IS NOT NULL  and APT_NUM_LOW <> APT_NUM_HIGH )";
	
	private final static String QUERY_SELECT_EXCEPTION_ADDRESS_WITHOUT_APT_RANGE = "  SELECT ID,SCRUBBED_ADDRESS,CITY,STATE,ZIPCODE,APT_NUM_LOW  " +
			" FROM DLV.ZIPPLUSFOUR_EXCEPTIONS where EXT_IS_VALID = 'Y' and (APT_NUM_LOW IS NULL  or APT_NUM_LOW = APT_NUM_HIGH )";
	
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
					ps.setString(1, scrubbedAddress.getSsScrubbedAddress());
					if(scrubbedAddress.getScrubbingResult() != null && scrubbedAddress.getScrubbingResult().equals("Suggestions")){
						ps.setString(2, "No Match");
					}else{
						ps.setString(2, scrubbedAddress.getScrubbingResult());
					}
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
			System.out.println("Without Apratment Range - Scrbbing Address List size = "+addressList.size());
			List<AddressScrubbingResponse> addressScrubbingResponses = getScrubbedAddress(addressList);
			// Update Exception Table
			scrubbedExceptionAddressBatchUpdate(addressScrubbingResponses);
			LOGGER.info("Exception Address Validation Exception BATCH PROCESS COMPLETED....");
		}else{
			LOGGER.info("No Exception Address found for validation..");
			System.out.println("No Address found for scrubbing");
		}
		
		// Get Exception Address with Appartment Range
		processExceptionAddressWithAptRange();
	}
	
	/**
	 * @throws RemoteException
	 */
	private void processExceptionAddressWithAptRange() throws RemoteException{
		// Get all the address with APT range
		LOGGER.info("Start processExceptionAddressWithAptRange() ..");
		Map<String,List<ScrubbedAddress>> aptRangeAddress = getExceptionAddressModelWithAptRange();
		Map<String,String> notValidException = new HashMap<String,String>();
		if(aptRangeAddress != null && !aptRangeAddress.isEmpty()){
			for(String key:aptRangeAddress.keySet()){
				List<ScrubbedAddress> addressList =aptRangeAddress.get(key);
				if(addressList != null && addressList.size()>0){
					System.out.println("Without Apratment Range - Scrbbing Address List size = "+addressList.size());
					List<AddressScrubbingResponse> addressScrubbingResponses = getScrubbedAddress(addressList);
					if(verifyAddressScrubbingResponseForAptRange(addressScrubbingResponses)){
						notValidException.put(key, "N");
						System.out.println(" No more Valid Exceptions : "+key);
					}
				}
			}
		}
		
		if(notValidException != null && !notValidException.isEmpty()){
			scrubbedExceptionAddressBatchUpdate(notValidException);
		}
		LOGGER.info("Exist processExceptionAddressWithAptRange() ..");
	}
	
	/**
	 * @param addressScrubbingResponses
	 * @return
	 */
	private boolean verifyAddressScrubbingResponseForAptRange(List<AddressScrubbingResponse> addressScrubbingResponses){
		//No Match
		LOGGER.info("start verifyAddressScrubbingResponseForAptRange() ..");
		for (AddressScrubbingResponse response : addressScrubbingResponses) {
			List<ScrubbedAddress> scrubbedAddresses = response.getScrubbedAddress();
			for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
				if(scrubbedAddress.getScrubbingResult() != null && 
						(scrubbedAddress.getScrubbingResult().equals(NO_MATCH) || scrubbedAddress.getScrubbingResult().equals(SUGGESTIONS))){
					return false;
				}
			}
		}
		if(addressScrubbingResponses == null || addressScrubbingResponses.isEmpty()){
			return false;
		}
		return true;
	}
	private Map<String,List<ScrubbedAddress>> getExceptionAddressModelWithAptRange() 
	{
		LOGGER.info("Inside getExceptionAddressModelWithAptRange() ..");
		Connection con = null;
		Map<String,List<ScrubbedAddress>> aptRangeAddress = new HashMap<String,List<ScrubbedAddress>>();

		try {
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_EXCEPTION_ADDRESS_WITH_APT_RANGE);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ScrubbedAddress address=new ScrubbedAddress();
				address.setId(rs.getString("ID"));
				address.setAddress1(rs.getString("SCRUBBED_ADDRESS"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIPCODE"));
				address.setApartment(rs.getString("APT_NUM_LOW"));
				List<String> aptRanges = increment(rs.getString("APT_NUM_LOW"),rs.getString("APT_NUM_HIGH"));
				List<ScrubbedAddress>  lstAddress= addAprtToAddress(aptRanges,address); 
				aptRangeAddress.put(rs.getString("ID"),lstAddress);
				
			}

			rs.close();
			ps.close();            
			
			LOGGER.info("Exit getExceptionAddressModelWithAptRange() ..");
		} catch (Exception e) {
			LOGGER.error("Exception while getting Exception address with Apt Range from DB "+e.getMessage());
			throw new EJBException(e);
		} finally {
                    close(con);
		}
		LOGGER.info("Exist getExceptionAddressModelWithAptRange() ..");
		return aptRangeAddress;
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
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_EXCEPTION_ADDRESS_WITHOUT_APT_RANGE);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ScrubbedAddress address=new ScrubbedAddress();
				address.setId(rs.getString("ID"));
				address.setAddress1(rs.getString("SCRUBBED_ADDRESS"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIPCODE"));
				address.setApartment(rs.getString("APT_NUM_LOW"));
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
	
	/**
	 * @param notValidException
	 */
	public void scrubbedExceptionAddressBatchUpdate(Map<String,String> notValidException){
		LOGGER.info("Inside scrubbedExceptionAddressBatchUpdate() ..");
		Connection connection = null ;
		try{
			connection = this.getConnection();
			PreparedStatement ps = connection.prepareStatement(QUERY_UPDATE_ZIPPLUSFOUR_EXCEPTIONS);
			final int batchSize = 1000;		// JDBC batch size is 1000
			int count = 0;
			for (String id : notValidException.keySet()) {
				ps.setString(1, "N");
				ps.setString(2, id);
				ps.addBatch();
			    if(++count % batchSize == 0) {
			        ps.executeBatch();
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
	
	/**
	 * @param low
	 * @param high
	 * @return
	 */
	public List<String> increment(String low, String high){
		// Check for last character as String
		List<String> ranges = new ArrayList<String>();
		try{
			if(StringUtils.isNumeric(low) && StringUtils.isNumeric(high) ){
				ranges = createAptRangeInt(Integer.parseInt(low), Integer.parseInt(high), "", "START");
				return ranges; 
			}else if(StringUtils.isAlpha(low) && StringUtils.isAlpha(high) && (low.length() == 1 && high.length() == 1)){
				ranges = createAptRangeChar(new Character(low.charAt(0)),new Character(high.charAt(0)),"","START");
			}else if(StringUtils.isAlphanumeric(low) && StringUtils.isAlphanumeric(high)){
				
				String lowLastChar = low.charAt(low.length()-1)+"";
				String highLastChar = high.charAt(high.length()-1)+"";
				String lowFirstChar = low.charAt(0)+"";
				String highFirstChar = high.charAt(0)+"";
				
				if(StringUtils.isAlpha(lowLastChar) && StringUtils.isAlpha(highLastChar)){
					if(lowLastChar.equals(highLastChar)){
						// Increment digit 
						int st = 0;
						int end = 0;
						if(StringUtils.isNumeric(low.substring(0, low.indexOf(lowLastChar)))){
							st = Integer.parseInt(low.substring(0, low.indexOf(lowLastChar)));
						}
						if(StringUtils.isNumeric(high.substring(0, high.indexOf(lowLastChar)))){
							end = Integer.parseInt(high.substring(0, high.indexOf(lowLastChar)));
						}
						ranges = createAptRangeInt(st, end , lowLastChar,"END");
					}else {
						String lFirst = ""+low.substring(0,low.indexOf(lowLastChar));
						String hFirst = ""+high.substring(0,high.indexOf(highLastChar));
						if(StringUtils.isNumeric(lFirst) && StringUtils.isNumeric(hFirst)){
							if(lFirst.equals(hFirst)){
								String append = hFirst ; 
								ranges = createAptRangeChar(new Character(low.charAt(low.length() -1 )),new Character(high.charAt(high.length() -1 )),append,"START");
							}
						}
					}
				}else if(StringUtils.isAlpha(lowFirstChar) && StringUtils.isAlpha(highFirstChar)){
					int st = 0;
					int end = 0;
					if(lowFirstChar.equals(highFirstChar)){
						if(StringUtils.isNumeric(low.substring(1, low.length()))){
							st = Integer.parseInt(low.substring(1, low.length()));
						}
						if(StringUtils.isNumeric(high.substring(1, high.length()))){
							end = Integer.parseInt(high.substring(1, high.length()));
						}
						ranges = createAptRangeInt(st, end , lowFirstChar,"START");
					}else {
						String lFirst = ""+low.substring(1, low.length());
						String hFirst = ""+high.substring(1, high.length());
						if(StringUtils.isNumeric(lFirst) && StringUtils.isNumeric(hFirst)){
							if(lFirst.equals(hFirst)){
								String append = hFirst ; 
								ranges = createAptRangeChar(new Character(low.charAt(0)),new Character(high.charAt(0)),append,"END");
							}
						}
						
					}
				}
			}
			if(StringUtils.isNumeric(low) && StringUtils.isAlphanumeric(high)){
				ranges.add(low);
				ranges.add(high);
			}
			if(StringUtils.isAlphanumeric(low) && StringUtils.isNumeric(high)){
				ranges.add(low);
				ranges.add(high);
			}
		}catch(Exception ex){	// Catch exception while creating Apartment Ranges and set APT_LOW and APT_HIGH as two ranges
//			ex.printStackTrace();
			ranges.add(low);
			ranges.add(high);
		}
		if(ranges != null && ranges.isEmpty()){
			ranges.add(low);
			ranges.add(high);
		}
//		disp(ranges);
		LOGGER.info("Exist increment() ..");
		return ranges;
	}
	
	/**
	 * @param st
	 * @param end
	 * @param str
	 * @param appendOption
	 * @return
	 */
	private List<String> createAptRangeInt(int st, int end , String str, String appendOption){
		List<String> ranges = new ArrayList<String>();
		for(int i = st; i <= end; i++){
			String apt = "";
			if(appendOption.equals("END")){
				apt = i+str;
			}else{
				apt = str+i;
			}
			ranges.add(apt);
		}
		return ranges;
	}
	
	/**
	 * @param st
	 * @param end
	 * @param str
	 * @param appendOption
	 * @return
	 */
	private List<String> createAptRangeChar(Character st, Character end , String str, String appendOption){
		List<String> ranges = new ArrayList<String>();
		Character temp = st;
		while(!temp.toString().equals(end.toString())){
			String apt = "";
			if(appendOption.equals("END")){
				apt = temp+str;
			}else{
				apt = str+temp;
			}
			ranges.add(apt);
			temp++;
		}
		if(temp.toString().equals(end.toString())){
			String apt = "";
			if(appendOption.equals("END")){
				apt = temp+str;
			}else{
				apt = str+temp;
			}
			ranges.add(apt);
		}
		return ranges;
	}
	
	/*private void disp(List<String> ranges){
		System.out.println("Apt Ranges...");
		for(String str : ranges){
			System.out.print(str+" ,");
		}
		System.out.println();
	}*/
	/**
	 * @param ranges
	 * @param address
	 * @return
	 */
	private List<ScrubbedAddress> addAprtToAddress(List<String> ranges, ScrubbedAddress address){
		LOGGER.info("Inside addAprtToAddress() ..");
		List<ScrubbedAddress> scrubbedAddresses = new ArrayList<ScrubbedAddress>();
		long counter = 0;
		for(String apt : ranges){
			counter++;
			ScrubbedAddress scrubbedAddress = new ScrubbedAddress();
			scrubbedAddress.setId(counter+"");
			scrubbedAddress.setAddress1(address.getAddress1());
			scrubbedAddress.setCity(address.getCity());
			scrubbedAddress.setState(address.getState());
			scrubbedAddress.setZipCode(address.getZipCode());
			scrubbedAddress.setApartment(apt);
			scrubbedAddresses.add(scrubbedAddress);
		}
		LOGGER.info("Exist addAprtToAddress() ..");
		return scrubbedAddresses;
	}
 }

