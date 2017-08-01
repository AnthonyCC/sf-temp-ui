package com.freshdirect.fdstore.standingorders.ejb;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.BasicSaleInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumStandingOrderType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDOrderSearchCriteria;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.FDStandingOrderSkuResultInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.payment.service.FDECommerceService;

/**
 * @author kumarramachandran
 *
 */
public class FDStandingOrdersSessionBean extends FDSessionBeanSupport {
	
	private static final long serialVersionUID = 1021328260150726448L;

	private final static Category LOGGER = LoggerFactory.getInstance(FDStandingOrdersSessionBean.class);
	private static final int LOCK_TIMEOUT = 120; //seconds

	/**
	 * Creates a new instance based on an existing customer list
	 * 
	 * @param list
	 * @return
	 * @throws FDResourceException 
	 */
	public FDStandingOrder createStandingOrder(FDCustomerList list) throws FDResourceException {
		if (list == null) {
			return null;
		}

		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			String pk = dao.createEmptyStandingOrder(conn, list.getCustomerPk().getId(), list.getId());
			if (pk != null) {
				return new FDStandingOrder(new PrimaryKey(pk), list);
			}
			LOGGER.error("Failed to create standing order instance for customer list " + list.getId());
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in createStandingOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}		
		return null;	
	}


	public Collection<FDStandingOrder> loadActiveStandingOrders(boolean isNewSo) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Collection<FDStandingOrder> ret = dao.loadActiveStandingOrders(conn,isNewSo);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadActiveStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public Collection<FDStandingOrder> loadCustomerStandingOrders(FDIdentity identity) throws FDResourceException, FDInvalidConfigurationException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Collection<FDStandingOrder> ret = dao.loadCustomerStandingOrders(conn, identity,false);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadCustomerStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public Collection<FDStandingOrder> loadCustomerNewStandingOrders(FDIdentity identity) throws FDResourceException, FDInvalidConfigurationException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Collection<FDStandingOrder> ret = dao.loadCustomerStandingOrders(conn, identity,true);
			
			ret = getStandingOrderDetails(ret);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadCustomerNewStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	/*
	 *  To get the standing order details about the product
	 */
	public Collection<FDStandingOrder> getStandingOrderDetails(Collection<FDStandingOrder> fdStandingOrders)
			throws FDResourceException, FDInvalidConfigurationException {
		
		FDIdentity customerIdentity = null;

		for (FDStandingOrder so : fdStandingOrders) {

			if (customerIdentity == null) {
				customerIdentity = getCustomerIdentity(so.getCustomerId());
			}
			populateSODetails(customerIdentity, so);
		}

		return fdStandingOrders;

	}


	/**
	 * @param customerIdentity
	 * @param so
	 * @throws FDResourceException
	 * @throws FDInvalidConfigurationException
	 */
	public void populateSODetails(FDIdentity customerIdentity,FDStandingOrder so) throws FDResourceException,FDInvalidConfigurationException {
		FDStandingOrderList fdSdingOrderList = FDListManager.getStandingOrderList(customerIdentity,
				so.getCustomerListId());
		List<FDProductSelectionI> productSelectionList = null;
		if (null != fdSdingOrderList) {
			productSelectionList = OrderLineUtil
					.getValidProductSelectionsFromCCLItems(fdSdingOrderList.getLineItems());

			for (FDProductSelectionI fdSelection : productSelectionList) {
				FDCartLineI cartLine = new FDCartLineModel(fdSelection);
				if (!cartLine.isInvalidConfig()) {
					// cartLine.refreshConfiguration();
					if(fdSelection.getCustomerListLineId()!=null)
					cartLine.setCustomerListLineId(fdSelection.getCustomerListLineId());
					so.getStandingOrderCart().addOrderLine(cartLine);
				}
			}

			if (null != productSelectionList && !productSelectionList.isEmpty()) {
				so.getStandingOrderCart().refreshAll(true);
			}
		}
	}

	public FDIdentity getCustomerIdentity(String customerId) throws FDResourceException {

		FDIdentity customerIdentity = new FDIdentity(customerId, FDCustomerFactory.getFDCustomerIdFromErpId(customerId));
		return customerIdentity;
	}
	
	public FDStandingOrder load(PrimaryKey pk) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrder ret = dao.load(conn, pk.getId());
			
			if(null!=ret){
				populateSODetails(getCustomerIdentity(ret.getCustomerId()), ret);
			}
		    
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in load() : " + e.getMessage(), e );
			throw new FDResourceException(e);
		} catch (FDInvalidConfigurationException e) {
			LOGGER.error( "SQL ERROR in load() : " + e.getMessage(), e );	
			throw new FDResourceException(e);

		} finally {
			close(conn);
		}
	}
	public FDStandingOrder loadSOCron(PrimaryKey pk) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrder ret = dao.load(conn, pk.getId());
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in load() : " + e.getMessage(), e );
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	// "cancel all delivaries" deletion flow coming from manage servlet
	public void deleteActivatedSO(FDActionInfo info, FDStandingOrder so, String deleteDate ) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			dao.deleteActivatedSO(conn, so.getId(),deleteDate);
			ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_TEMPLATE_DEL_DATE_SET);
			rec.setStandingOrderId(so.getId());
			this.logActivity(rec);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in deleteActivatedSO() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	// coming from servlet deletes unactivated SO templates
	public void delete(FDActionInfo info, FDStandingOrder so) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();

			dao.deleteStandingOrder(conn, so.getId(), so.getCustomerListId());
			ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_DELETED);
			rec.setStandingOrderId(so.getId());
			FDStandingOrdersManager.getInstance().deletesoTemplate(so.getId());
			this.logActivity(rec);
		} catch (SQLException e) {
			LOGGER.error("SQL ERROR in delete() : " + e.getMessage(), e);
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public String save(FDActionInfo info, FDStandingOrder so, String saleId) throws FDResourceException {
		String primaryKey = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			try {
				FDStandingOrderList customerList = so.getCustomerList();
				if (customerList != null)
					customerList.setModificationDate( new Date() );
			} catch (FDResourceException e) {
				LOGGER.warn( "FDResourceException catched", e );
			}
			
			if (so.getId() == null ||  so.getId().isEmpty()) {
				// create object
				LOGGER.debug( "Creating new standing order." );
				primaryKey = dao.createStandingOrder(conn, so);
				ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_CREATED);
				rec.setChangeOrderId(saleId);
				rec.setStandingOrderId(primaryKey);
				this.logActivity(rec);
				if(so.getAddressId()!=null 
						&& so.getTimeSlotId()!=null && so.getReservedDayOfweek() !=0
						)
				{
					
				ErpAddressModel address=getShipToAddress(so.getAddressId());
				FDStandingOrdersManager.getInstance().saveStandingOrderToLogistics(so.getId(),
						so.getTimeSlotId(),Integer.toString(so.getReservedDayOfweek()),
						so.getUser().getHistoricOrderSize(),
						so.getCustomerId(),address,so.getUser().isNewSO3Enabled());
				}
			} else {
				LOGGER.debug( "Updating existing standing order." );
				dao.updateStandingOrder(conn, so);
				primaryKey = so.getId();
				ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_MODIFIED);
				rec.setChangeOrderId(saleId);
				rec.setStandingOrderId(primaryKey);
				this.logActivity(rec);
				if(so.getAddressId()!=null 
						&& so.getTimeSlotId()!=null && so.getReservedDayOfweek()!=0
						)
				{
				ErpAddressModel address=getShipToAddress(so.getAddressId());
				FDStandingOrdersManager.getInstance().saveStandingOrderToLogistics(so.getId(),
						so.getTimeSlotId(),Integer.toString(so.getReservedDayOfweek()),
						so.getUser().getHistoricOrderSize(),
						so.getCustomerId(),address,so.getUser().isNewSO3Enabled());
				}
			}
							
			return primaryKey;
			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in save() : " + e.getMessage(), e );
			throw new FDResourceException(e);
		} catch(FDAuthenticationException fae){
			LOGGER.error( "FDAuthenticationException in save() : " + fae.getMessage(), fae );
			throw new FDResourceException(fae);
		}
		finally {
			close(conn);
		}
	}
	
	private static final String SHIP_TO_ADDRESS_QUERY = "SELECT * FROM CUST.ADDRESS WHERE ID = ?";

	private ErpAddressModel getShipToAddress(String addressId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(SHIP_TO_ADDRESS_QUERY);
			ps.setString(1, addressId);
			rs = ps.executeQuery();
			ErpAddressModel address = null;
			if (rs.next()) {
				address = this.loadDeliveryAddressFromResultSet(rs);
				address.setPK(new PrimaryKey(rs.getString("ID")));
				address.setCompanyName(rs.getString("COMPANY_NAME"));
				address.setServiceType(EnumServiceType.getEnum(rs
						.getString("SERVICE_TYPE")));
			}
			return address;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			close(conn);
		}
	}
	
	private ErpAddressModel loadDeliveryAddressFromResultSet(ResultSet rs) throws SQLException {
		ErpAddressModel address = new ErpAddressModel();
		address.setFirstName(rs.getString("FIRST_NAME"));
		address.setLastName(rs.getString("LAST_NAME"));
		address.setAddress1(rs.getString("ADDRESS1"));
		address.setAddress2(rs.getString("ADDRESS2"));
		address.setApartment(rs.getString("APARTMENT"));
		address.setCity(rs.getString("CITY"));
		address.setState(rs.getString("STATE"));
		address.setZipCode(rs.getString("ZIP"));
		address.setCountry(rs.getString("COUNTRY"));
		address.setPhone(new PhoneNumber(rs.getString("PHONE")));
		address.setAltFirstName(rs.getString("ALT_FIRST_NAME"));
		address.setAltLastName(rs.getString("ALT_LAST_NAME"));
		address.setAltApartment(rs.getString("ALT_APARTMENT"));
		address.setAltPhone(new PhoneNumber(rs.getString("ALT_PHONE")));
		address.setAltContactPhone(new PhoneNumber(rs
				.getString("ALT_CONTACT_PHONE")));
		address.setInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
		address.setAltDelivery(EnumDeliverySetting.getDeliverySetting(rs
				.getString("ALT_DEST")));

		return address;
	}

	public void assignStandingOrderToOrder(PrimaryKey salePK, PrimaryKey standingOrderPK) throws FDResourceException {		

		LOGGER.debug( "assigning SO["+standingOrderPK+"] to SALE["+salePK+"]" );
		Connection conn=null;
		try {
			 conn = getConnection();
			
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.assignToSale(conn, standingOrderPK.getId(), salePK.getId());
			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in assignStandingOrderToOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		}finally {
			close(conn);
		}
	}

	public void markSaleAltDeliveryDateMovement(PrimaryKey salePK) throws FDResourceException {		

		Connection conn=null;
		try {
			conn = getConnection();
			
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.markSaleAltDeliveryDateMovement(conn, salePK.getId());
			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in markSaleAltDeliveryDateMovement() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		}finally {
			close(conn);
		}
	}

	public void logActivity(ErpActivityRecord record) {
		new ErpLogActivityCommand(LOCATOR, record).execute();
	}
	
	public FDStandingOrderInfoList getActiveStandingOrdersCustInfo(FDStandingOrderFilterCriteria filter) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrderInfoList ret = dao.getActiveStandingOrdersCustInfo(conn,filter);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadActiveStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void clearStandingOrderErrors(String[] soIDs,String agentId)throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			if(null !=soIDs && soIDs.length>0){
				Date date = new Date();
				String[] ids = new String[soIDs.length];
				for (int i = 0; i < soIDs.length; i++) {
					ids[i]=soIDs[i].split(",")[0];
				}
				dao.clearStandingOrderErrors(conn,ids);
				
				for (int i = 0; i < soIDs.length; i++) {
					ErpActivityRecord rec = new ErpActivityRecord();
					rec.setActivityType(EnumAccountActivityType.STANDINGORDER_ERROR_CLEARED);
					rec.setSource(EnumTransactionSource.CUSTOMER_REP);
					rec.setInitiator(agentId);
					rec.setStandingOrderId(soIDs[i].split(",")[0]);
					rec.setCustomerId(soIDs[i].split(",")[1]);
					rec.setDate(date);
					this.logActivity(rec);
				}
				
			}			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadActiveStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public FDStandingOrderInfoList getFailedStandingOrdersCustInfo() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrderInfoList ret = dao.getFailedStandingOrdersCustInfo(conn);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getFailedStandingOrdersCustInfo() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public FDStandingOrderInfoList getMechanicalFailedStandingOrdersCustInfo() throws FDResourceException {
		Connection conn = null;
		FDStandingOrderInfoList ret = null;
		
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			ret = dao.getMechanicalFailedStandingOrdersCustInfo(conn);

		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getMechanicalFailedStandingOrdersCustInfo() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}

		// Add Zone info to Standing Order
		if(ret !=null ){
			addZoneInfoToStandingOrder(ret);
		}
		
		return ret;
	}
	
	
	private void addZoneInfoToStandingOrder(FDStandingOrderInfoList ret){
		
		List<FDStandingOrderInfo> standingOrdersInfo = ret.getStandingOrdersInfo();
		
		for(FDStandingOrderInfo soInfo : standingOrdersInfo ){
			
			try {
				// retrieve standing order id
				String soId = soInfo.getSoID();
				
				// load Standing Order
				FDStandingOrder so = loadSOCron( new PrimaryKey( soId ) );
								
				// retrieve address from Standign Order
				AddressModel deliveryAddressModel = FDCustomerManager.getAddress( so.getCustomerIdentity(), so.getAddressId() );
				
				//Date startDateTime = soInfo.getStartTime(); 
				Date startDateTime = soInfo.getNextDate();
				FDDeliveryZoneInfo zoneInfo = null;
				
				try {
					// zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(deliveryAddressModel, selectedTimeslot.getStartDateTime(), customerUser.getHistoricOrderSize(), (reservation!=null)?reservation.getRegionSvcType():null);
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDateTime);
					CustomerAvgOrderSize historicSize = FDCustomerManager.getHistoricOrderSize(so.getCustomerIdentity().getErpCustomerPK());
					if(deliveryAddressModel!=null && StringUtils.isNotBlank(deliveryAddressModel.getAddress1())){
						zoneInfo = FDDeliveryManager.getInstance().getZoneInfo(deliveryAddressModel, cal.getTime(), historicSize, null, (so.getCustomerIdentity()!=null)?so.getCustomerIdentity().getErpCustomerPK():null);
					}
				} catch (FDInvalidAddressException e) {
					LOGGER.info( "Invalid zone info. - FDInvalidAddressException", e );
				}
				
				// add zone to Standing Order
				if(zoneInfo != null){
					soInfo.setZone(zoneInfo.getZoneCode());
				}
			} catch (FDResourceException re) {
				LOGGER.error( "Could not retrieve standing order! - FDResourceException", re );
			}catch (Exception re) {
				LOGGER.error( "Could not retrieve standing order! ", re );
			}
			
			
		}
		
	}		
	
	
	public Map<Date,Date> getStandingOrdersAlternateDeliveryDates() throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Map<Date,Date> map = dao.getStandingOrdersAlternateDeliveryDates(conn);
			
			return map;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getStandingOrdersAlternateDeliveryDates() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public List<FDStandingOrderAltDeliveryDate> getStandingOrderAltDeliveryDates()  throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			List<FDStandingOrderAltDeliveryDate> altDeliveryDates = dao.getStandingOrderAltDeliveryDates(conn);
			
			return altDeliveryDates;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getStandingOrdersAlternateDeliveryDates() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public Map<Date, List<FDStandingOrderAltDeliveryDate>> getStandingOrdersGlobalAlternateDeliveryDates() throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Map<Date, List<FDStandingOrderAltDeliveryDate>> altDeliveryDates = dao.getStandingOrdersGlobalAlternateDeliveryDates(conn);
			
			return altDeliveryDates;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getStandingOrdersAlternateDeliveryDates() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void addStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			dao.addStandingOrderAltDeliveryDate(conn, altDeliveryDate);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in addStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public boolean lock(FDStandingOrder so, String lockId) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			return dao.lockStandingOrder(conn, so.getId(), lockId, LOCK_TIMEOUT);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in lock() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public boolean unlock(FDStandingOrder so, String lockId) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			return dao.unlockStandingOrder(conn, so.getId(), lockId, LOCK_TIMEOUT);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in unlock() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public String getLockId(String soId) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			return dao.getLockIdStandingOrder(conn, soId, LOCK_TIMEOUT);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getLockId() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		
	}
	
	
	public void checkForDuplicateSOInstances(FDIdentity identity) throws FDResourceException, FDInvalidConfigurationException {
		
		Connection conn=null;
		try{
			conn=getConnection();
			FDStandingOrderDAO dao=new FDStandingOrderDAO();
			List<String> hashes=dao.getDuplicateWarningInfos(conn, identity.getErpCustomerPK());
		
			FDOrderHistory history = (FDOrderHistory) FDCustomerManager.getOrderHistoryInfo(identity); //or should we get the FDUser object instead?
			List<FDOrderInfoI> soiList = (List<FDOrderInfoI>) history.getStandingOrderInstances(null);
			
			//load customer Standing Orders
			List<FDStandingOrder> standingOrders=(List<FDStandingOrder>) loadCustomerStandingOrders(identity);
			
			//group orders by standing order id
			Map<String,List<FDOrderInfoI>> soIds = new HashMap<String, List<FDOrderInfoI>>();
			for(FDOrderInfoI order : soiList){
				if(soIds.get(order.getStandingOrderId())!=null){
					soIds.get(order.getStandingOrderId()).add(order);
				}else{
					List<FDOrderInfoI> local = new ArrayList<FDOrderInfoI>();
					local.add(order);
					soIds.put(order.getStandingOrderId(), local);
				}
			}				
		
			//check if multiple SOI found for one SO
			for(String key : soIds.keySet()) {
				
				if(soIds.get(key).size() > 1) {
					
					List<Long> ids=new ArrayList<Long>();
					for(FDOrderInfoI o:soIds.get(key)){
						ids.add(Long.parseLong(o.getErpSalesId()));
					}

					String hash=createHash(ids);
					if(!hashes.contains(hash)){ //check if email already sent about these orders
						FDStandingOrder actual=null;
						
						for(FDStandingOrder so:standingOrders){ //find the real SO object for the orders
							if(so.getId().equals(key)){
								actual=so;
								break;
							}
						}
						
						LOGGER.debug("Sending email to customer...");
						doEmail(FDEmailFactory.getInstance().createDuplicateSOInstanceEmail( FDCustomerManager.getCustomerInfo(identity), actual, soIds.get(key)));
						//dao.storeDuplicateWarningInfo(conn, identity.getErpCustomerPK(), hash);
					}
					
				}
			}
		
		}catch (SQLException e) {
			LOGGER.error( "SQL ERROR in checkForDuplicateSOInstances() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}

	}
	
	private String createHash(List<Long> source){
		
		Collections.sort(source);
		StringBuilder builder=new StringBuilder();
		for(Long instance:source){
			builder.append(instance);
		}
		
		return builder.toString();
	}
	
	public void doEmail(XMLEmailI email) throws FDResourceException {
		try {
			MailerGatewaySB mailer = getMailerHome().create();
			mailer.enqueueEmail(email);
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewayBean");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewayBean");
		}
	}
	
	public void updateStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			dao.updateStandingOrderAltDeliveryDate(conn, altDeliveryDate);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in updateStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public void deleteStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			dao.deleteStandingOrderAltDeliveryDate(conn, altDeliveryDate);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in deleteStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void insertIntoCoremetricsUserinfo(FDUserI fdUser, int flag) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			dao.insertIntoCoremetricsUserinfo(conn, fdUser, flag);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in deleteStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public boolean getCoremetricsUserinfo(FDUserI fdUser) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			return dao.getCoremetricsUserinfo(conn, fdUser);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in deleteStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public FDStandingOrderAltDeliveryDate getStandingOrderAltDeliveryDateById(String id) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			return dao.getStandingOrderAltDeliveryDateById(conn,id);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in deleteStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		} 
	}
	
	public void deleteStandingOrderAltDeliveryDateById(String[] altIds) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			dao.deleteStandingOrderAltDeliveryDateById(conn, altIds);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in deleteStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDRuntimeException(e);
		} finally {
			close(conn);
		}
	}
	
	public void addStandingOrderAltDeliveryDates(List<FDStandingOrderAltDeliveryDate> altDeliveryDates) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			dao.addStandingOrderAltDeliveryDates(conn, altDeliveryDates);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in addStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			throw new FDRuntimeException(e);
		} finally {
			close(conn);
		}
	}
	
	public boolean checkIfAlreadyExists(FDStandingOrderAltDeliveryDate altDate) throws FDResourceException{
		Connection conn = null;
		boolean isDuplicate=false;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			isDuplicate =dao.checkIfAlreadyExists(conn, altDate);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in addStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return isDuplicate;
	}
	
	public boolean isValidSoId(String soId) throws FDResourceException{
		Connection conn = null;
		boolean isValid=false;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			isValid =dao.isValidSoId(conn, soId);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in addStandingOrderAltDeliveryDate() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return isValid;
	}
	
	public FDStandingOrderSkuResultInfo replaceSkuCode(String existingSku,
			String replacementSku) throws FDResourceException {

		Connection conn = null;
		FDStandingOrderSkuResultInfo result = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			result = dao.replaceSkuCode(conn,
					existingSku, replacementSku);
			FDStandingOrderAuditDataDAO auditDataDAO = new FDStandingOrderAuditDataDAO();
			auditDataDAO.auditCall(conn,
					prepareAuditInfoBean(existingSku, replacementSku,result.getProductSkuList().size()));
			return result;
		} catch (SQLException e) {
			result = new FDStandingOrderSkuResultInfo();
			LOGGER.error("SQL ERROR in replaceSkuCode() : " + e.getMessage(), e);			
					result.setErrorMessage("Error while Auditing the User Activity.Rolling back the Trasaction");
					getSessionContext().setRollbackOnly();
					return result;
			}
			
		 finally {
				close(conn);
			}
		}

	private AuditDataBeanInfo prepareAuditInfoBean(String existingSku,
			String replacementSku, int numofStandingOrders) {
		AuditDataBeanInfo auditDataBeanInfo = new AuditDataBeanInfo();
		auditDataBeanInfo.setUserId(getSessionContext().getCallerPrincipal().getName());
		auditDataBeanInfo.setType(EnumStandingOrderType.SO_SKU_REPLACEMENT.getName());
		auditDataBeanInfo.setComment(replacementSku + " replaced " + existingSku + " in " + numofStandingOrders + " Standing Orders");
		return auditDataBeanInfo;
	}
	
	public FDStandingOrderSkuResultInfo validateSkuCode(String existingSku, String replacementSku) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();			
			return dao.validateSkuCode(conn,existingSku,replacementSku);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in validateSkuCode() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}			
	}
	
	public void persistUnavailableDetailsToDB(List<Result> resultsList) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.persistUnavailableDetailsToDB(conn,resultsList);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in validateSkuCode() : " + e.getMessage(), e );
			e.printStackTrace();
			getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}			
	}
	
	public UnavDetailsReportingBean getDetailsForReportGeneration() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			return dao.getDetailsForReportGeneration(conn);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in validateSkuCode() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}			
	}
	
	public Collection<FDStandingOrder> getValidStandingOrder(FDIdentity identity) throws FDResourceException, RemoteException, FDInvalidConfigurationException
	{
	Connection conn = null;
	try {
		conn = getConnection();
		FDStandingOrderDAO dao = new FDStandingOrderDAO();
		
		Collection<FDStandingOrder> ret = dao.getValidStandingOrder(conn, identity);
		
		return ret;
	} catch (SQLException e) {
		LOGGER.error( "SQL ERROR in loadCustomerStandingOrders() : " + e.getMessage(), e );
		e.printStackTrace();
		throw new FDResourceException(e);
	} finally {
		close(conn);
	}
	}
	
    /*
       *  To activate Standing order 
      */
	public boolean activateStandingOrder(FDStandingOrder so) throws FDResourceException,RemoteException{
		Connection conn = null;
		boolean isActivate=false;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			isActivate = dao.activateStandingOrder(conn, so);
			FDStandingOrdersManager.getInstance().activateStandingOrderInLogistics(so.getId());		
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in activateStandingOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return isActivate;

	}
	public boolean checkIfCustomerHasStandingOrder(FDIdentity identity) throws FDResourceException,RemoteException{
		Connection conn = null;
		boolean hasExtisingSO=false;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			hasExtisingSO = dao.checkIfCustomerHasStandingOrder(conn, identity);
						
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in checkIfCustomerHasStandingOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return hasExtisingSO;
	}
	
	public boolean updateDefaultStandingOrder(String listId,FDIdentity identity)throws FDResourceException,RemoteException{
		Connection conn = null;
		boolean isUpdate=false;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			isUpdate = dao.updateDefaultStandingOrder(conn,listId,identity);
						
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in updateDefaultStandingOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return isUpdate;
	}
	
	public void	turnOffReminderOverLayNewSo(String standingOrderId)throws FDResourceException,RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.turnOffReminderOverLayNewSo(conn, standingOrderId);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in turnOffReminderOverLayNewSo(): " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void	updateSoCartOverlayFirstTimePreferences(String customerId)throws FDResourceException,RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO.updateSoCartOverlayFirstTimePreferences(conn, customerId);	
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in updateSoCartOverlayFirstTimePreferences(): " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void	updateNewSoFeaturePreferences(String customerId)throws FDResourceException,RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO.updateNewSoFeaturePreferences(conn, customerId);	
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in updateNewSoFeaturePreferences(): " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void	updateDeActivatedSOError(String soId)throws FDResourceException,RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO.updateDeActivatedSOError(conn, soId);	
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in updateDeActivatedSOError(): " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

}
