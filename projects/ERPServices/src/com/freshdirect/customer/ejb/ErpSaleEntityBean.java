package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Price;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCancelOrderModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpChargeSettlementModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpClientCodeReport;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.customer.ErpFailedChargeSettlementModel;
import com.freshdirect.customer.ErpFailedSettlementModel;
import com.freshdirect.customer.ErpFundsRedepositModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpResubmitPaymentModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpReversalModel;
import com.freshdirect.customer.ErpSaleI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.EntityBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardAuthModel;
import com.freshdirect.giftcard.ErpGiftCardTransModel;
import com.freshdirect.giftcard.ErpPostAuthGiftCardModel;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.giftcard.ErpReverseAuthGiftCardModel;
import com.freshdirect.giftcard.ejb.ErpBalanceTransferPersistentBean;
import com.freshdirect.giftcard.ejb.ErpPostAuthPersistentBean;
import com.freshdirect.giftcard.ejb.ErpPreAuthPersistentBean;
import com.freshdirect.giftcard.ejb.ErpReverseAuthPersistentBean;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * ErpSale entity bean implementation.
 * @version	$Revision:84$
 * @author	 $Author:Kashif Nadeem$
 * @ejbHome <{ErpSaleHome}>
 * @ejbRemote <{ErpSaleEB}>
 * @ejbPrimaryKey <{PrimaryKey}>
 * @stereotype fd-entity
 */
public class ErpSaleEntityBean extends EntityBeanSupport implements ErpSaleI {

	private static final long	serialVersionUID	= 4750923330554772316L;

	private static Category LOGGER = LoggerFactory.getInstance(ErpSaleEntityBean.class);

	private ErpSaleModel model;

	private ErpComplaintList complaints;

	public void initialize() {
		model = new ErpSaleModel(null, null, new ArrayList<ErpTransactionModel>(), new ArrayList<ErpComplaintModel>(), null, null, Collections.<String>emptySet(), new ArrayList<ErpCartonInfo>(), null, null, null);
		complaints = new ErpComplaintList();
	}

	public ModelI getModel() {
		return model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		throw new UnsupportedOperationException("setFromModel not supported");
	}

	public void setPK(PrimaryKey pk) {
		model.setPK(pk);
	}

	public PrimaryKey getPK() {
		return model.getPK();
	}
	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.customer.ejb.ErpSaleHome";
	}

	public void forcePaymentStatus() throws ErpTransactionException {
		try{
			model.forcePaymentStatus();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void forceSettlement() throws ErpTransactionException {
		try{
			model.forceSettlement();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	public void forceSettlementFailed() throws ErpTransactionException {
		try{
			model.forceSettlementFailed();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALE where ID=?");
			ps.setString(1, pk.getId());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find ErpSale with PK " + pk);
			}

			PrimaryKey foundPk = new PrimaryKey(rs.getString(1));
			rs.close();
			ps.close();

			return foundPk;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}

	public PrimaryKey ejbFindByComplaintId(String complaintId) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT SALE_ID FROM CUST.COMPLAINT WHERE ID = ?");
			ps.setString(1, complaintId);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find ErpSale for ComplaintId: " + complaintId);
			}
			PrimaryKey pk = new PrimaryKey(rs.getString("SALE_ID"));

			rs.close();
			ps.close();

			return pk;

		} catch (SQLException e) {
			throw new FinderException(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException closing connection", e);
			}
		}
	}

	public Collection<PrimaryKey> ejbFindByStatus(EnumSaleStatus saleStatus) throws FinderException {
		return performFind("SELECT ID FROM CUST.SALE WHERE STATUS=?", saleStatus.getStatusCode());
	}

	private Collection<PrimaryKey> performFind(String statement, String parameter) throws FinderException {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(statement);
			if (parameter != null) {
				ps.setString(1, parameter);
			}
			ResultSet rs = ps.executeQuery();

			List<PrimaryKey> lst = new ArrayList<PrimaryKey>();
			while (rs.next()) {
				String id = rs.getString(1);
				lst.add(new PrimaryKey(id));
			}

			rs.close();
			ps.close();

			return lst;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}


	/**
	 * Create sales with a create order transcation and uses a delivery pass. Status will be NEW.
	 */
	public PrimaryKey ejbCreate(PrimaryKey customerPk, ErpCreateOrderModel createOrder, Set<String> usedPromotionCodes, String dlvPassId,EnumSaleType saleType)
		throws CreateException {
	    
		Connection conn = null;
		try {
			initialize();
			model = new ErpSaleModel(customerPk, createOrder, usedPromotionCodes, dlvPassId,saleType);
			conn = getConnection();
			return create(conn);

		} catch (SQLException sqle) {
			LOGGER.warn("Error in ejbCreate", sqle);
			throw new CreateException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				throw new EJBException("Error in ejbcreate while closing the connection.");
			}
		}
	}

	public void ejbPostCreate(PrimaryKey customerPk, ErpCreateOrderModel createOrder, Set<String> usedPromotionCodes, String dlvPassId,EnumSaleType type) {
		// this space is intentionally blank :)
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		setPK(new PrimaryKey(getNextId(conn, "CUST")));
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALE (ID,CUSTOMER_ID,STATUS,SAP_NUMBER, DLV_PASS_ID,TYPE,CROMOD_DATE) values (?,?,?,?,?,?,?)");
		ps.setString(1, getPK().getId());
		ps.setString(2, model.getCustomerPk().getId());
		ps.setString(3, model.getStatus().getStatusCode());
		ps.setString(4, model.getSapOrderNumber());
		String dlvPassId = model.getDeliveryPassId();
		if(dlvPassId != null){
			//True when this order uses a delivery pass.
			ps.setString(5, dlvPassId);
		}else{
			//True when this order contains a delivery pass.
			ps.setNull(5, Types.VARCHAR);
		}

		ps.setString(6,model.getType().getSaleType());
		//Added as part of PERF-27 task.
		ps.setTimestamp(7, new java.sql.Timestamp(model.getCurrentOrder().getTransactionDate().getTime()));

		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
		} catch (SQLException sqle) {
			setPK(null);
			throw sqle;
		} finally {
			ps.close();
		}

		// create children
		ErpTransactionList txList = getTransactionPBList();
		txList.create(conn);

		// create client codes
		createClientCodes(conn);
	
		complaints.create(conn);
		if(model.hasUsedPromotionCodes()==true) {
			ErpPromotionDAO.insert(conn, getPK(), model.getUsedPromotionCodes());
		}
		createCroModMaxDate(conn);

		return getPK();
	}
	
	private void deleteClientCodes(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.ORDERLINE_CLIENTCODE WHERE SALE_ID = ?");
		ps.setString(1, getPK().getId());
		ps.executeUpdate();
		ps.close();		
	}

	private void createClientCodes(Connection conn) throws SQLException {
		PreparedStatement ps;
		Map<String,List<ErpClientCodeReport>> clientCodes = new HashMap<String, List<ErpClientCodeReport>>();
		ErpAbstractOrderModel currentOrder = getCurrentOrder();
		List<ErpOrderLineModel> orderLines = currentOrder.getOrderLines();
		for (ErpOrderLineModel item : orderLines) {
			// basic error resolution
			if (item.getCartlineId() == null)
				continue;
			
			ArrayList<ErpClientCodeReport> ccList = new ArrayList<ErpClientCodeReport>();
			for (ErpClientCode cc : item.getClientCodes()) {
				ErpClientCodeReport ccr = new ErpClientCodeReport(cc);
				double disAmount;
				Price p = new Price(item.getBasePrice());
				if (item.getDiscount() != null) {
					try {
						Price discountP = PricingEngine.applyDiscount(p, 1, item.getDiscount());
						disAmount = discountP.getBasePrice();
					} catch (PricingException e) {
						disAmount = 0.0;
					}
				} else {
					disAmount = item.getBasePrice();
				}
				ccr.setUnitPrice(MathUtil.roundDecimal(disAmount));
				ccr.setTaxRate(item.getTaxRate());
				ccr.setDeliveryDate(currentOrder.getRequestedDate());
				ccr.setProductDescription(item.getDescription());
				ccList.add(ccr);
			}

			// we'll overwrite possible duplicates
			clientCodes.put(item.getCartlineId(), ccList);
		}
		ps = conn.prepareStatement("INSERT INTO CUST.ORDERLINE_CLIENTCODE (CLIENT_CODE, QUANTITY, ORDINAL, CUSTOMER_ID, CARTLINE_ID, SALE_ID, UNIT_PRICE, TAX_RATE, PRODUCT_DESCRIPTION, DELIVERY_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		for (Map.Entry<String, List<ErpClientCodeReport>> cartItem : clientCodes.entrySet()) {
			for (int i = 0; i < cartItem.getValue().size(); i++) {
				ErpClientCodeReport ccItem = cartItem.getValue().get(i);
				ps.setString(1, ccItem.getClientCode());
				ps.setInt(2, ccItem.getQuantity());
				ps.setInt(3, i);
				ps.setString(4, model.getCustomerPk().getId());
				ps.setString(5, cartItem.getKey());
				ps.setString(6, getPK().getId());
				ps.setDouble(7, ccItem.getUnitPrice());
				ps.setDouble(8, ccItem.getTaxRate());
				ps.setString(9, ccItem.getProductDescription());
				ps.setDate(10, new java.sql.Date(ccItem.getDeliveryDate().getTime()));
				ps.addBatch();
			}
		}
		ps.executeBatch();
		ps.close();
	}

	private void createCroModMaxDate(Connection conn)throws SQLException {
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("INSERT INTO CUST.SALE_CRO_MOD_DATE(SALE_ID, MAX_DATE) VALUES(?,?)");
			ps.setString(1, getPK().getId());
			ps.setTimestamp(2, new java.sql.Timestamp(model.getCurrentOrder().getTransactionDate().getTime()));
			ps.execute();
		}finally{
			if(ps != null){
				ps.close();
			}
		}
	}

	private ErpTransactionList getTransactionPBList() throws SQLException{
		ErpTransactionList txList = new ErpTransactionList();
		txList.setParentPK(getPK());
		for ( ErpTransactionModel m : model.getTransactions() ) {
			if(m.getTransactionType().isUpdatable()) {
				ErpUpdatableTransactionBean upb = ErpTransactionList.createUpdatablePersistentBean(m.getTransactionType(), m.getPK(), getPK());
				m.setCustomerId(model.getCustomerPk().getId());
				upb.setFromModel(m);
				txList.add(upb);
			} else {
				ErpTransactionPersistentBean pb = ErpTransactionList.createPersistentBean(m.getTransactionType(), m.getPK(), getPK());
				//Added as part of PERF-27 task.
				m.setCustomerId(model.getCustomerPk().getId());
				pb.setFromModel(m);
				txList.add(pb);
			}
		}
		return txList;
	}


	private ErpComplaintList buildComplaintPBList() {
		ErpComplaintList compList = new ErpComplaintList();
		compList.setParentPK(getPK());
		for ( ErpComplaintModel m : model.getComplaints() ) {
			ErpComplaintPersistentBean pb = new ErpComplaintPersistentBean();
			pb.setFromModel( m );
			compList.add(pb);
		}
		return compList;
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
			"SELECT CUSTOMER_ID, STATUS, SAP_NUMBER, WAVE_NUMBER, TRUCK_NUMBER, STOP_SEQUENCE, NUM_REGULAR_CARTONS, NUM_FREEZER_CARTONS, NUM_ALCOHOL_CARTONS, DLV_PASS_ID, TYPE, STANDINGORDER_ID FROM CUST.SALE WHERE ID=?");
		ps.setString(1, getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new SQLException("No such ErpSale PK: " + getPK());
		}
		// load properties from result set
		PrimaryKey customerPk = new PrimaryKey(rs.getString(1));
		EnumSaleStatus status = EnumSaleStatus.getSaleStatus(rs.getString(2));
		String sapOrderNumber = rs.getString(3);
		ErpShippingInfo shippingInfo = new ErpShippingInfo(rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getInt(9));
		String dlvPassId = rs.getString(10);
		String _saleType=rs.getString(11);
		String standingOrderId = rs.getString(12);
		EnumSaleType saleType=EnumSaleType.REGULAR;
		if(_saleType!=null) {
			saleType=EnumSaleType.getSaleType(_saleType);
		}
		rs.close();
		ps.close();

		// load children

		ErpTransactionList txList = new ErpTransactionList();
		txList.setParentPK(getPK());
		txList.load(conn);
		
		ErpComplaintList compList = new ErpComplaintList();
		compList.setParentPK(getPK());
		compList.load(conn);

		Set<String> usedPromotionCodes = ErpPromotionDAO.select(conn, getPK());

		PrimaryKey oldPk = model.getPK();

		List<ErpCartonInfo> cartonInfo = ErpCartonsDAO.getCartonInfo(conn, getPK());		
		model = new ErpSaleModel(customerPk, status, txList.getModelList(), compList.getModelList(), sapOrderNumber, shippingInfo, usedPromotionCodes, cartonInfo, dlvPassId, saleType, standingOrderId);
		model.setPK(oldPk);
		
		super.decorateModel(model);

		// load client codes
		loadClientCodes(conn);
	}

	private void loadClientCodes(Connection conn) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		ps = conn.prepareStatement("SELECT CLIENT_CODE, QUANTITY, CARTLINE_ID FROM CUST.ORDERLINE_CLIENTCODE WHERE SALE_ID = ? ORDER BY CARTLINE_ID, ORDINAL");
		ps.setString(1, getPK().getId());
		rs = ps.executeQuery();
		Map<String,List<ErpClientCode>> clientCodes = new HashMap<String, List<ErpClientCode>>();
		while (rs.next()) {
			ErpClientCode cc = new ErpClientCode();
			cc.setClientCode(rs.getString("CLIENT_CODE"));
			cc.setQuantity(rs.getInt("QUANTITY"));
			String cartLine = rs.getString("CARTLINE_ID");
			if (!clientCodes.containsKey(cartLine))
				clientCodes.put(cartLine, new ArrayList<ErpClientCode>());
			clientCodes.get(cartLine).add(cc);
		}
		rs.close();
		ps.close();
		List<ErpOrderLineModel> orderLines = getCurrentOrder().getOrderLines();
		for (ErpOrderLineModel ol : orderLines)
			if (clientCodes.containsKey(ol.getCartlineId()))
				ol.getClientCodes().addAll(clientCodes.get(ol.getCartlineId()));
	}

	String saleUpdateWithWaveSQL =
		"UPDATE CUST.SALE SET STATUS=?, SAP_NUMBER=?, TRUCK_NUMBER=?, STOP_SEQUENCE=?, NUM_REGULAR_CARTONS=?, NUM_FREEZER_CARTONS=?, NUM_ALCOHOL_CARTONS=?, DLV_PASS_ID=?, CROMOD_DATE=?, WAVE_NUMBER=? WHERE ID=?";

	String saleUpdateWithNoWaveSQL =
		"UPDATE CUST.SALE SET STATUS=?, SAP_NUMBER=?, TRUCK_NUMBER=?, STOP_SEQUENCE=?, NUM_REGULAR_CARTONS=?, NUM_FREEZER_CARTONS=?, NUM_ALCOHOL_CARTONS=?, DLV_PASS_ID=?, CROMOD_DATE=? WHERE ID=?";

	public void store(Connection conn) throws SQLException {
		if (isModified()) {
			PreparedStatement ps = null;
			ErpShippingInfo sInfo = model.getShippingInfo();
			if (sInfo != null && sInfo.getWaveNumber() != null) {
				ps = conn.prepareStatement(saleUpdateWithWaveSQL);
			} else {
				ps = conn.prepareStatement(saleUpdateWithNoWaveSQL);
			}
			int index = 1;
			ps.setString(index++, model.getStatus().getStatusCode());
			ps.setString(index++, model.getSapOrderNumber());
			ps.setString(index++, sInfo != null ? sInfo.getTruckNumber() : null);
			ps.setString(index++, sInfo != null ? sInfo.getStopSequence() : null);
			ps.setInt(index++, sInfo != null ? sInfo.getRegularCartons() : 0);
			ps.setInt(index++, sInfo != null ? sInfo.getFreezerCartons() : 0);
			ps.setInt(index++, sInfo != null ? sInfo.getAlcoholCartons() : 0);
			LOGGER.debug("Delivery pass id in store "+model.getDeliveryPassId());
			//Update delivery pass id.
			if(model.getDeliveryPassId() != null){
				ps.setString(index++, model.getDeliveryPassId());
			}else{
				ps.setNull(index++, Types.VARCHAR);
			}

			//Added as part of PERF-27 task.
			ps.setTimestamp(index++, new java.sql.Timestamp(model.getCurrentOrder().getTransactionDate().getTime()));

			// DO NOT UPDATE SALE.wave_number in database if getWaveNumber returns null
			if (sInfo != null && sInfo.getWaveNumber() != null) {
				ps.setString(index++, sInfo.getWaveNumber());
			}

			ps.setString(index++, getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not updated");
			}
			ps.close();

			ErpPromotionDAO.delete(conn, getPK());
			if(model.hasUsedPromotionCodes())
				ErpPromotionDAO.insert(conn, getPK(), model.getUsedPromotionCodes());
		}
		//store children
		ErpTransactionList txList = getTransactionPBList();
		if (txList.isModified()) {
			txList.store(conn);
			updateCroModMaxDate(conn);
		}
		
		deleteClientCodes(conn);
		createClientCodes(conn);
	
		if (complaints.isModified()) {
			complaints.store(conn);
		}

	}

	private void updateCroModMaxDate(Connection conn)throws SQLException {
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("UPDATE CUST.SALE_CRO_MOD_DATE SET MAX_DATE = ? WHERE SALE_ID = ?");
			ps.setTimestamp(1, new java.sql.Timestamp(model.getCurrentOrder().getTransactionDate().getTime()));
			ps.setString(2, getPK().getId());
			ps.execute();
		}finally{
			if(ps != null){
				ps.close();
			}
		}
	}

	public void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Removal of sales not possible");
	}

	public PrimaryKey getCustomerPk() {
		return model.getCustomerPk();
	}

	public EnumSaleStatus getStatus() {
		return model.getStatus();
	}

	public String getSapOrderNumber() {
		return model.getSapOrderNumber();
	}

	public Collection<ErpTransactionModel> getTransactions() {
		return model.getTransactions();
	}

	public Collection<ErpComplaintModel> getComplaints() {
		return model.getComplaints();
	}

	/**
	 * Notification that the create order did not make it into SAP.
	 * Status will be NOT_SUBMITTED after this operation.
	 *
	 * @param message detailed description of the error
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void submitFailed(String message) throws ErpTransactionException {
		try{
			model.submitFailed(message);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Notification that the create order is successfully completed in SAP.
	 * Status will be SUBMITTED after this operation.
	 *
	 * @param sapOrderNumber order number assigned in SAP
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void createOrderComplete(String sapOrderNumber) throws ErpTransactionException {
		try{
			model.createOrderComplete(sapOrderNumber);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Change the order. Can only change a SUBMITTED order.
	 * Status will be MODIFIED after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void modifyOrder(ErpModifyOrderModel model, Set<String> usedPromotionCodes) throws ErpTransactionException {
		try{
			this.model.modifyOrder(model, usedPromotionCodes);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Notification that the last change order is successfully completed in SAP.
	 * Status will be SUBMITTED after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void modifyOrderComplete() throws ErpTransactionException {
		try{
			model.modifyOrderComplete();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Cancel the order. The Sale must be in SUBMITTED status.
	 * Status will be MODIFIED after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void cancelOrder(ErpCancelOrderModel cancelOrder) throws ErpTransactionException {
		try{
			model.cancelOrder(cancelOrder);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Cancel the order. The Gift Card Sale must be in ENROUTE status.
	 * Status will be MODIFIED after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void cancelGCOrder(ErpCancelOrderModel cancelOrder) throws ErpTransactionException {
		try{
			model.cancelGCOrder(cancelOrder);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	/**
	 * Notification that the cancel order is successfully completed in SAP.
	 * Status will be CANCELED after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void cancelOrderComplete() throws ErpTransactionException {
		try{
			model.cancelOrderComplete();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void cutoff() throws ErpTransactionException {
		try{
			model.cutoff();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	public void emailPending() throws ErpTransactionException{
		try{
			model.emailPending();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
		
	}

	public void setGiftCardRegPending() throws ErpTransactionException{
		try{
			model.setGiftCardRegPending();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
		
	}	
	

	/**
	 * Add the invoice coming from SAP. The Sale must be in the INPROCESS status.
	 * Status will be ENROUTE after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void addInvoice(ErpInvoiceModel invoiceModel) throws ErpTransactionException {
		try{
			model.addInvoice(invoiceModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargeInvoice(ErpChargeInvoiceModel chargeInvoiceModel) throws ErpTransactionException {
		try{
			model.addChargeInvoice(chargeInvoiceModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public List<CrmSystemCaseInfo> reconcileSale() throws ErpTransactionException {
		try{
			return model.reconcileSale();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * update the truckNumber and stopSequence number of sale after SAP sends this
	 * information in invoice batch.
	 *
	 * @param String truckNumber truck number the sale is on
	 * @param String stopSequence stop sequnce on the route for this sale
	 */
	public void updateShippingInfo(ErpShippingInfo shippingInfo) {
		model.updateShippingInfo(shippingInfo);
		setModified();
	}

	public void markAsRedelivery() throws ErpTransactionException {
		try{
			model.markAsRedelivery();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void markAsEnroute() throws ErpTransactionException {
		try{
			model.markAsEnroute();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void markAsReturn() throws ErpTransactionException {
		try{
			model.markAsReturn();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addReturn(ErpReturnOrderModel returnModel) throws ErpTransactionException {
		try{
			model.addReturn(returnModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addRedelivery(ErpRedeliveryModel redeliveryModel) throws ErpTransactionException {
		try{
			model.addRedelivery(redeliveryModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel) throws ErpTransactionException {
		try{
			// If the order involves gift cards, do 'Post Auth', else proceed as usual.
			if(null != getCurrentOrder().getAppliedGiftcards() && getCurrentOrder().getAppliedGiftcards().size() > 0) {
				model.addDeliveryConfirm(deliveryConfirmModel,EnumSaleStatus.POST_AUTH_PENDING);
				setModified();
			}else{
				model.addDeliveryConfirm(deliveryConfirmModel,EnumSaleStatus.CAPTURE_PENDING);
				setModified();
			}
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	
	
	public void addRegisterGiftCard(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException {
		
		try{
			model.addRegisterGiftCard(registerGCModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
			

	public void addGiftCardEmailInfo(ErpGiftCardTransModel emailGCModel) throws ErpTransactionException {
		
		try{			
			model.addGiftCardEmailInfo(emailGCModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
	

	public void addGiftCardDeliveryConfirm(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException {
		
		try{
			model.addGiftCardDlvConfirm(registerGCModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
	
	
	/**
	 * Add an ErpComplaintModel to the list of complaints. The Sale must be in the COMPLETE status.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void addComplaint(ErpComplaintModel complaintModel) throws ErpTransactionException {
		try{
			model.addComplaint(complaintModel);
			complaints = buildComplaintPBList();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void updateComplaint(ErpComplaintModel newComplaint) throws ErpTransactionException {
		try{
			model.updateComplaint(newComplaint);
			complaints = buildComplaintPBList();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargeback(ErpChargebackModel chargebackModel) throws ErpTransactionException {
		try{
			model.addChargeback(chargebackModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargebackReversal(ErpChargebackReversalModel cbkReversal) throws ErpTransactionException {
		try{
			model.addChargebackReversal(cbkReversal);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addAdjustment(ErpAdjustmentModel adjustmentModel) throws ErpTransactionException {
		try{
			model.addAdjustment(adjustmentModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addResubmitPayment(ErpResubmitPaymentModel resubmitPaymentModel) throws ErpTransactionException {
		try{
			model.addResubmitPayment(resubmitPaymentModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * this will add an authorization to the sale
	 *
	 * @param ErpAuthorizationModel authorizationModel
	 * @return void
	 */
	public void addAuthorization(ErpAuthorizationModel authorizationModel) throws ErpTransactionException {
		try{
			model.addAuthorization(authorizationModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	
	/**
	 * this will add an authorization to the sale
	 *
	 * @param ErpAuthorizationModel authorizationModel
	 * @return void
	 */
	public void addGiftCardBalanceTransfer(ErpGiftCardTransModel authorizationModel) throws ErpTransactionException {
		try{
			model.addGCBalanceTransfer(authorizationModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	

	public void addSettlement(ErpSettlementModel settlementModel) throws ErpTransactionException {
		try{
			model.addSettlement(settlementModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addFailedSettlement(ErpFailedSettlementModel failedSettlementModel) throws ErpTransactionException {
		try{
			model.addFailedSettlement(failedSettlementModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargeSettlement(ErpChargeSettlementModel chargeSettlementModel) throws ErpTransactionException {
		try{
			model.addChargeSettlement(chargeSettlementModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addFundsRedeposit(ErpFundsRedepositModel fundsRedepositModel) throws ErpTransactionException {
		try{
			model.addFundsRedeposit(fundsRedepositModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addFailedChargeSettlement(ErpFailedChargeSettlementModel failedChargeSettlementModel) throws ErpTransactionException {
		try{
			model.addFailedChargeSettlement(failedChargeSettlementModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addManualAuthorization(ErpAuthorizationModel authorizationModel) throws ErpTransactionException {
		try{
			model.addManualAuthorization(authorizationModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addReversal(ErpReversalModel reversalModel) throws ErpTransactionException {
		try{
			model.addReversal(reversalModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addCashback(ErpCashbackModel cashbackModel) throws ErpTransactionException {
		try{
			model.addCashback(cashbackModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}	

	public void addCapture(ErpCaptureModel captureModel) throws ErpTransactionException {
		try{
			model.addCapture(captureModel);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addVoidCapture(ErpVoidCaptureModel voidCapture) throws ErpTransactionException {
		try{
			model.addVoidCapture(voidCapture);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public java.util.Date getCaptureDate() throws ErpTransactionException {
		try{
			return model.getCaptureDate();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Get how the current order looks like. Returns the last create or change order transaction.
	 */
	public ErpAbstractOrderModel getCurrentOrder() {
		return model.getCurrentOrder();
	}

	public List<ErpAuthorizationModel> getAuthorizations() {
		return model.getAuthorizations();
	}

	public List<ErpAuthorizationModel> getApprovedAuthorizations() {
		return model.getApprovedAuthorizations();
	}

	public double getOutstandingCaptureAmount() {
		return model.getOutstandingCaptureAmount();
	}

	public List<ErpCaptureModel> getCaptures() {
		return model.getCaptures();
	}

	public List<ErpSettlementModel> getSettlements() throws ErpTransactionException {
		try{
			return model.getSettlements();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public List<ErpAdjustmentModel> getAdjustments() throws ErpTransactionException {
		try{
			return model.getAdjustments();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public List<ErpAuthorizationModel> getFailedAuthorizations() {
		return model.getFailedAuthorizations();
	}

	public ErpInvoiceModel getInvoice() throws ErpTransactionException {
		try{
			return model.getInvoice();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void updateCartonInfo(List<ErpCartonInfo> cartonInfo) throws ErpTransactionException, RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();

			ErpCartonsDAO.delete(conn, getPK());

			ErpCartonsDAO.insert(conn, cartonInfo);

		} catch (SQLException sqle) {
			throw new ErpTransactionException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}

	public List<ErpCartonInfo> getCartonInfo() throws ErpTransactionException, RemoteException {
		List<ErpCartonInfo> returnList = new ArrayList<ErpCartonInfo>();
		Connection conn = null;
		try {
			conn = getConnection();

			returnList = ErpCartonsDAO.getCartonInfo(conn, getPK());

		} catch (SQLException sqle) {
			throw new ErpTransactionException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
		return returnList;
	}

	public ErpChargeInvoiceModel getChargeInvoice() {
		return model.getChargeInvoice();
	}

	public  boolean getIsChargePayment(String authId) {
		return model.getIsChargePayment(authId);
	}

	public  boolean getIsChargePayment(double chargeAmount) {
		return model.getIsChargePayment(chargeAmount);
	}

	public boolean hasChargeSettlement() {
		return model.hasChargeSettlement();
	}

	public boolean hasFundsRedeposit() {
		return model.hasFundsRedeposit();
	}

	public EnumTransactionType getCurrentTransactionType() {
		return model.getCurrentTransactionType();
	}

	public List<ErpFailedSettlementModel> getFailedSettlements() {
		return model.getFailedSettlements();
	}

	public List<ErpChargeSettlementModel> getChargeSettlements() {
		return model.getChargeSettlements();
	}

	public List<ErpFundsRedepositModel> getFundsRedeposits(){
		return model.getFundsRedeposits();
	}

	public List<ErpFailedChargeSettlementModel> getFailedChargeSettlements() {
		return model.getFailedChargeSettlements();
	}

	public void reverseChargePayment() throws ErpTransactionException {
		try {
			model.reverseChargePayment();
			setModified();
		} catch ( ErpTransactionException e ) {
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public boolean getIsSettlementFailedAfterSettled() {
		return model.getIsSettlementFailedAfterSettled();
	}

	//For Gift Cards _ begin.
	
	public List<ErpPreAuthGiftCardModel> getPendingGCAuthorizations(ErpPaymentMethodI pm) {
		return model.getPendingGCAuthorizations(pm);
	}
	
	public List<ErpReverseAuthGiftCardModel> getPendingGCReverseAuths(ErpPaymentMethodI pm) {
		return model.getPendingGCReverseAuths(pm);
	}

	public List<ErpPreAuthGiftCardModel> getValidGCAuthorizations(ErpPaymentMethodI pm){
		return model.getValidGCAuthorizations(pm);
	}	
	
	public List<ErpPreAuthGiftCardModel> getPendingGCAuthorizations() {
		return model.getPendingGCAuthorizations();
	}
	
	public List<ErpReverseAuthGiftCardModel> getPendingReverseGCAuthorizations() {
		return model.getPendingReverseGCAuthorizations();
	}
	
	public List<ErpPreAuthGiftCardModel> getValidGCAuthorizations() {
		return model.getValidGCAuthorizations();
	}
	
	public List<ErpPostAuthGiftCardModel> getValidGCPostAuthorizations() {
		return model.getValidGCPostAuthorizations();
	}
	
	public void addGCPreAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException {
		try {
			model.addGCAuthorization(auth);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	public void addPostAuthorization(ErpPostAuthGiftCardModel postAuth) throws ErpTransactionException {
		try {
			model.addPostAuthorization(postAuth);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
	
	public void addReverseGCPreAuthorization(ErpReverseAuthGiftCardModel rauth) throws ErpTransactionException {
		try {
			model.addReverseGCAuthorization(rauth);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}

	}
	
	public void cancelGCPreAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException {
		try {
			model.cancelGCAuthorization(auth);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}

	}

	public boolean hasValidPostAuth(ErpPaymentMethodI pm, String preAuthCode) {
		return model.hasValidPostAuth(pm, preAuthCode);
	}
	
	public void updateGCAuthorization(ErpGiftCardAuthModel auth) throws ErpTransactionException {
		try {
			model.updateGCAuthorization(auth);
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	public void markAsCapturePending() throws ErpTransactionException {
		try {
			model.markAsCapturePending();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}		
		
	}
	
	public void markAsSettlementPending() throws ErpTransactionException {
		try {
			model.markAsSettlementPending();
			setModified();
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}			
		
	}
	
	private static class ErpTransactionList extends DependentPersistentBeanList {

		private static final long	serialVersionUID	= 4204931950533869261L;

		public void load(Connection conn) throws SQLException {
			PrimaryKey ppk = (PrimaryKey) ErpTransactionList.this.getParentPK();
			List<DependentPersistentBeanSupport> txList = new ArrayList<DependentPersistentBeanSupport>();

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.SALESACTION WHERE SALE_ID=?");
			ps.setString(1, ppk.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PrimaryKey txPk = new PrimaryKey(rs.getString("ID"));
				EnumTransactionType txType = EnumTransactionType.getTransactionType(rs.getString("ACTION_TYPE"));
				if(txType.isUpdatable()) {
					ErpUpdatableTransactionBean updatableTxBean = createUpdatablePersistentBean(txType, txPk, ppk);
					updatableTxBean.setParentPK(ppk);
					updatableTxBean.load(conn);
					txList.add(updatableTxBean);
				} else {
					ErpTransactionPersistentBean txBean = createPersistentBean(txType, txPk, ppk);
					txBean.setParentPK(ppk);
					txBean.load(conn);
					txList.add(txBean);
				}
			}
			rs.close();
			ps.close();

			ErpTransactionList.this.set(txList);
		}
		
		public static ErpUpdatableTransactionBean createUpdatablePersistentBean(EnumTransactionType txType, PrimaryKey txPk, PrimaryKey ppk) throws SQLException{
			//Added for Gift cards
			if (EnumTransactionType.PREAUTH_GIFTCARD.equals(txType)) {
				return new ErpPreAuthPersistentBean(txPk);
			}
			if (EnumTransactionType.REVERSEAUTH_GIFTCARD.equals(txType)) {
				return new ErpReverseAuthPersistentBean(txPk);
			}
			if (EnumTransactionType.POSTAUTH_GIFTCARD.equals(txType)) {
				return new ErpPostAuthPersistentBean(txPk);
			}
			if (EnumTransactionType.BALANCETRANSFER_GIFTCARD.equals(txType)) {
				return new ErpBalanceTransferPersistentBean(txPk);
			}
			
			
			throw new SQLException("Unknown transaction type " + txType.getCode() + " encountered in sale " + ppk);
		}

		public static ErpTransactionPersistentBean createPersistentBean(EnumTransactionType txType, PrimaryKey txPk, PrimaryKey ppk) throws SQLException{
			if(EnumTransactionType.CREATE_ORDER.equals(txType)){
				return new ErpCreateOrderPersistentBean(txPk);
			}
			if (EnumTransactionType.MODIFY_ORDER.equals(txType)) {
				return new ErpModifyOrderPersistentBean(txPk);
			}
			if (EnumTransactionType.INVOICE.equals(txType)) {
				return new ErpInvoicePersistentBean(txPk);
			}
			if (EnumTransactionType.AUTHORIZATION.equals(txType)) {
				return new ErpAuthorizationPersistentBean(txPk);
			}
			if (EnumTransactionType.CAPTURE.equals(txType)) {
				return new ErpCapturePersistentBean(txPk);
			}
			if (EnumTransactionType.SUBMIT_FAILED.equals(txType)) {
				return new ErpSubmitFailedPersistentBean(txPk);
			}
			if (EnumTransactionType.REVERSAL.equals(txType)) {
				return new ErpReversalPersistentBean(txPk);
			}
			if (EnumTransactionType.CASHBACK.equals(txType)) {
				return new ErpCashbackPersistentBean(txPk);
			}
			if (EnumTransactionType.SETTLEMENT.equals(txType)) {
				return new ErpSettlementPersistentBean(txPk);
			}
			if (EnumTransactionType.CHARGEBACK.equals(txType)) {
				return new ErpChargebackPersistentBean(txPk);
			}
			if (EnumTransactionType.ADJUSTMENT.equals(txType)) {
				return new ErpAdjustmentPersistentBean(txPk);
			}
			if (EnumTransactionType.CHARGE.equals(txType)) {
				return new ErpChargePersistentBean(txPk);
			}
			if (EnumTransactionType.CANCEL_ORDER.equals(txType)) {
				return new ErpCancelOrderPersistentBean(txPk);
			}
			if (EnumTransactionType.RETURN_ORDER.equals(txType)) {
				return new ErpReturnOrderPersistentBean(txPk);
			}
			if (EnumTransactionType.REDELIVERY.equals(txType)) {
				return new ErpRedeliveryPersistentBean(txPk);
			}
			if (EnumTransactionType.VOID_CAPTURE.equals(txType)) {
				return new ErpVoidCapturePersistentBean(txPk);
			}
			if (EnumTransactionType.CHARGEBACK_REVERSAL.equals(txType)) {
				return new ErpChargebackReversalPersistentBean(txPk);
			}
			if (EnumTransactionType.SETTLEMENT_FAILED.equals(txType)) {
				return new ErpFailedSettlementPersistentBean(txPk);
			}
			if (EnumTransactionType.SETTLEMENT_CHARGE.equals(txType)) {
				return new ErpChargeSettlementPersistentBean(txPk);
			}
			if (EnumTransactionType.FUNDS_REDEPOSIT.equals(txType)) {
				return new ErpFundsRedepositPersistentBean(txPk);
			}
			if (EnumTransactionType.SETTLEMENT_CHARGE_FAILED.equals(txType)) {
				return new ErpFailedChargeSettlementPersistentBean(txPk);
			}
			if (EnumTransactionType.INVOICE_CHARGE.equals(txType)) {
				return new ErpChargeInvoicePersistentBean(txPk);
			}
			if (EnumTransactionType.DELIVERY_CONFIRM.equals(txType)) {
				return new ErpDeliveryConfirmPersistentBean(txPk);
			}
			if (EnumTransactionType.REGISTER_GIFTCARD.equals(txType)) {
				return new ErpRegisterGCPersistentBean(txPk);
			}
			if (EnumTransactionType.EMAIL_GIFTCARD.equals(txType)) {
				return new ErpEmailGiftCardPersistentBean(txPk);
			}
			if (EnumTransactionType.GIFTCARD_DLV_CONFIRM.equals(txType)) {
				return new ErpGiftCardDlvConfirmPersistentBean(txPk);
			}
			throw new SQLException("Unknown transaction type " + txType.getCode() + " encountered in sale " + ppk);
		}
	}

	private static class ErpComplaintList extends DependentPersistentBeanList {
		
		private static final long	serialVersionUID	= 8774828244401069072L;

		public void load(Connection conn) throws SQLException {
			PrimaryKey ppk = (PrimaryKey) ErpComplaintList.this.getParentPK();
			List<DependentPersistentBeanSupport> complaintList = new ArrayList<DependentPersistentBeanSupport>();
			complaintList.addAll(ErpComplaintPersistentBean.findByParent(conn, ppk));
			ErpComplaintList.this.set(complaintList);
		}
	}

	public Collection<PrimaryKey> ejbFindByDeliveryPassId(String dlvPassId) throws FinderException {
		return performFind("SELECT ID FROM CUST.SALE WHERE DLV_PASS_ID = ?", dlvPassId);
	}

	/**
	 * This method updates the delivery pass id of the sale model.
	 * @param dlvPassId
	 * @throws RemoteException
	 */

	public void updateDeliveryPassId(String dlvPassId) {
			model.setDeliveryPassId(dlvPassId);
			setModified();
	}

	private final static String GET_NTH_NONCOS_REG_ORDER_QUERY="select id from ( select s.id,sa.requested_date, row_number() over (order by sa.REQUESTED_DATE DESC) row_num from cust.sale s, cust.salesaction sa, cust.deliveryinfo di,cust.paymentinfo pi where "+
                                                                   " s.CUSTOMER_ID=? and s.type='REG' and s.id=sa.sale_id and pi.salesaction_id=sa.id and "+
                                                                   " sa.action_type IN ('CRO','MOD') and sa.action_date=(select max_date from cust.sale_cro_mod_date scmd where scmd.sale_id=s.id) and "+
                                                                   " di.salesaction_id=sa.id AND s.status=?  and di.delivery_type <>'C' and pi.payment_method_type=?  and pi.on_fd_account<>'M' ) where row_num=1";

	private final static String GET_NTH_NONCOS_SUB_ORDER_QUERY="select id from ( select s.id,sa.requested_date, row_number() over (order by sa.REQUESTED_DATE DESC) row_num from cust.sale s, cust.salesaction sa, cust.deliveryinfo di,cust.paymentinfo pi where "+
    " s.CUSTOMER_ID=? and s.type='SUB' and s.id=sa.sale_id and pi.salesaction_id=sa.id and "+
    " sa.action_type IN ('CRO','MOD') and sa.action_date=(select max_date from cust.sale_cro_mod_date scmd where scmd.sale_id=s.id) and "+
    " di.salesaction_id=sa.id AND s.status=?  and di.delivery_type <>'C' and pi.payment_method_type=?) where row_num=1";

	public PrimaryKey ejbFindByCriteria(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus, EnumPaymentMethodType pymtMethodType) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		PreparedStatement ps =null;
		try {
			if(null==saleType) {
				saleType=EnumSaleType.REGULAR;
			}
			conn = getConnection();
			if(EnumSaleType.REGULAR.equals(saleType)) {
			  ps=conn.prepareStatement(GET_NTH_NONCOS_REG_ORDER_QUERY);
			}
			else {
				ps=conn.prepareStatement(GET_NTH_NONCOS_SUB_ORDER_QUERY);
			}
			ps.setString(1, customerID);
			ps.setString(2, saleStatus.getStatusCode());
			ps.setString(3,pymtMethodType.getName());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException(new StringBuffer(100).append("Unable to find ErpSale for customer : " )
						                                               .append(customerID)
						                                               .append(" with sale status as ")
						                                               .append(saleStatus.getStatusCode())
						                                               .append(" and payment type as ")
						                                               .append(pymtMethodType.getDescription())
						                                               .toString());
			}

			PrimaryKey foundPk = new PrimaryKey(rs.getString(1));
			rs.close();
			ps.close();

			return foundPk;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}

	public ErpRegisterGiftCardModel getRecentRegistration() {
		return model.getRecentRegisteration();
	}
	
	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel, EnumSaleStatus enumSaleStatus) throws ErpTransactionException{
		try{
			model.addDeliveryConfirm(deliveryConfirmModel,enumSaleStatus);			
			setModified();
			
		}catch(ErpTransactionException e){
			getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
}
