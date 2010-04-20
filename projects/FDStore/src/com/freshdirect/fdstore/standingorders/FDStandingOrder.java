package com.freshdirect.fdstore.standingorders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.xml.ExcludeFromXmlSerializer;

public class FDStandingOrder extends ModelSupport {
	
	private static final long serialVersionUID = 9146272725813248955L;

	String customerId;			// customer ID (erps)
	String customerListId; 		// customer list ID : selected shopping list
	String addressId;			// delivery address ID
	String paymentMethodId; 	// payment method ID
	
	Date startTime; 			// delivery timeslot - start date
	Date endTime;				// dlv timeslot - end date
	Date nextDeliveryDate;		// next delivery date
	
	int frequency;				// frequency in weeks (chosen by the customer; can be one, two, three, and four weeks)
	
	boolean alcoholAgreement = false;	// agreed to alcohol delivery
	boolean deleted = false;	// deleted flag (false by default)
	
	String lastError = null;	// last exception or invalid condition
	String detailMessage = null;	// detailed error message

	String customerListName;	// Only used when standing order is not yet persisted!

	
	public FDStandingOrder() {
		super();
	}

	public FDStandingOrder(PrimaryKey pk, FDCustomerList list) {
		this();
		
		setPK(pk);
		
		setCustomerId(list.getCustomerPk().getId());
		setCustomerListId(list.getPK().getId());
	}


	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
		customerIdentity = null;
	}
	public String getCustomerListId() {
		return customerListId;
	}
	public void setCustomerListId(String customerListId) {
		this.customerListId = customerListId;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getNextDeliveryDate() {
		return nextDeliveryDate;
	}
	public void setNextDeliveryDate(Date nextDeliveryDate) {
		this.nextDeliveryDate = nextDeliveryDate;
	}

	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public boolean isAlcoholAgreement() {
		return alcoholAgreement;
	}
	public void setAlcoholAgreement(boolean alcoholAgreement) {
		this.alcoholAgreement = alcoholAgreement;	
		

	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public ErrorCode getLastError() {
		if ( lastError == null )
			return null;
		try {
			return ErrorCode.valueOf( lastError );
		} catch ( IllegalArgumentException e ) {
			return ErrorCode.GENERIC;
		} 
	}
	public String getErrorHeader() {
		return detailMessage;
	}
	public String getErrorDetail() {
		return detailMessage;
	}
	public void setLastError( ErrorCode lastErrorCode, String detailMessage ) {
		this.lastError = lastErrorCode.name();
		this.detailMessage = detailMessage;
	}
	public void setLastError( String lastErrorCode, String detailMessage ) {
		this.lastError = lastErrorCode;
		this.detailMessage = detailMessage;
	}
	public void clearLastError() {
		this.lastError = null;
		this.detailMessage = null;
	}
	public boolean isError() {
		return lastError != null;
	}

	public String getCustomerListName() {
		return customerListName;
	}

	public void setCustomerListName(String customerListName) {
		this.customerListName = customerListName;
	}


	public void setupDelivery(FDReservation r) {
		setStartTime(r.getStartTime());
		setEndTime(r.getEndTime());
		
		calculateNextDeliveryDate( r.getTimeslot().getBaseDate() );
	}

	/**
	 * Sets next delivery date based on baseDate parameter
	 * and frequency
	 * 
	 * @param baseDate
	 */
	public void calculateNextDeliveryDate(Date baseDate) {
		// set next delivery date
		nextDeliveryDate = getSubsequentDeliveryDate(baseDate);
	}

	/**
	 * Utility method that shifts a date with
	 * week frequency
	 * 
	 * @param baseDate
	 * @return date shifted with frequency weeks
	 */
	public Date getSubsequentDeliveryDate(Date baseDate) {
		// calculate next delivery

		Calendar cl = Calendar.getInstance();
		cl.setTime(baseDate);
		
		cl.add(Calendar.DATE, 7*frequency);
		
		cl.set(Calendar.HOUR, 0);
		cl.set(Calendar.MINUTE, 0);

		return cl.getTime();
	}
	
	/**
	 * Skip next delivery and jump to the following date according to the frequency
	 */
	public void skipDeliveryDate() {
		calculateNextDeliveryDate( nextDeliveryDate );
	}
	
	public void recalculateFrequency( int newFreq ) {
		Calendar nextDate = Calendar.getInstance();
		nextDate.setTime( nextDeliveryDate );
		nextDate.add( Calendar.WEEK_OF_YEAR, newFreq - frequency );
		while ( DeliveryInterval.isWithinDeliveryWindow( nextDate.getTime() ) ) {
			nextDate.add( Calendar.WEEK_OF_YEAR, 1 );		
		}
		frequency = newFreq;
		nextDeliveryDate = nextDate.getTime();
	}
	
	
	public static String[] i2s = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
	

	public String getFrequencyDescription() {
		if (frequency == 1) {
			return "every week";
		}
		return "every " + i2s[frequency] + " weeks";
	}
	
	
	public static final DateFormat DATE_FORMATTER =  new SimpleDateFormat("EEEE, MMMM d.");
	
	public String getNextDeliveryString() {
		return DATE_FORMATTER.format( getNextDeliveryDate() );
	}

	
	@Override
	public String toString() {
		return "SO["+getId()+", "+customerListName+", "+nextDeliveryDate+"]";
	}

	public static enum ErrorCode { 
		TECHNICAL( "Technical problem." ), 
		GENERIC( "Some error." ), 
		
		ADDRESS( "Invalid address." ), 
		PAYMENT( "Invalid payment method." ), 
		ALCOHOL( "Alcohol problems." ), 
		MINORDER( "Minimum order requirements not met." ), 		
		TIMESLOT( "No suitable timeslot found." ),
		CART( "Invalid cart contents." );
		
		private String errorText;
		
		private ErrorCode( String errorText ) {
			this.errorText = errorText;
		}
		
		public boolean isTechnical() {
			return name().equals( "TECHNICAL" );
		}
		
		public String getErrorText() {
			return errorText;
		}
		
	};
	
	
	// more heavyweight getter methods
	
	private FDIdentity customerIdentity = null;
	
	/**
	 * @return FDIdentity of the user of this standing order
	 * @throws FDResourceException
	 */
	public FDIdentity getCustomerIdentity() throws FDResourceException {
		if ( customerIdentity == null )
			customerIdentity = new FDIdentity( customerId, FDCustomerFactory.getFDCustomerIdFromErpId( customerId ) );
		return customerIdentity;
	}
	
	@ExcludeFromXmlSerializer
	public FDUserI getUser() throws FDResourceException, FDAuthenticationException {
		return FDCustomerManager.recognize( getCustomerIdentity() );	
	}	
	
	@ExcludeFromXmlSerializer
	public FDCustomerInfo getUserInfo() throws FDResourceException {
		return FDCustomerManager.getCustomerInfo( getCustomerIdentity() );	
	}	
	
	
	/**
	 * @return delivery address of this standing order as ErpAddressModel
	 * @throws FDResourceException
	 */
	public ErpAddressModel getDeliveryAddress() throws FDResourceException {
		return FDCustomerManager.getAddress( getCustomerIdentity(), addressId );		
	}
	
	/**
	 * @return customer shopping list associated with this standing order
	 * @throws FDResourceException
	 */
	@ExcludeFromXmlSerializer
	public FDStandingOrderList getCustomerList() throws FDResourceException {
		return FDListManager.getStandingOrderList( getCustomerIdentity(), customerListId );
	}
	
	/**
	 * @param user 
	 * @return last placed order by Standing Order which is not expired (?)
	 * @throws FDResourceException
	 * @throws FDAuthenticationException 
	 */
	@ExcludeFromXmlSerializer
	public FDOrderInfoI getLastOrder() throws FDResourceException, FDAuthenticationException {
		return getLastOrder( getUser() );		
	}
	@ExcludeFromXmlSerializer
	public FDOrderInfoI getLastOrder( FDUserI user ) throws FDResourceException {
		return FDStandingOrdersManager.getInstance().getLastOrder( user, this );		
	}

	/**
	 * @param user 
	 * @return all orders based on this standing order of this user
	 * @throws FDAuthenticationException 
	 * @throws FDResourceException 
	 */
	@ExcludeFromXmlSerializer
	public List<FDOrderInfoI> getAllOrders() throws FDResourceException, FDAuthenticationException {
		return getAllOrders( getUser() );
	}
	@ExcludeFromXmlSerializer
	public List<FDOrderInfoI> getAllOrders( FDUserI user ) throws FDResourceException {
		return FDStandingOrdersManager.getInstance().getAllOrders( user, this );		
	}
	
	@ExcludeFromXmlSerializer
	public void save() throws FDResourceException {
		FDStandingOrdersManager.getInstance().save( this );
	}
}
