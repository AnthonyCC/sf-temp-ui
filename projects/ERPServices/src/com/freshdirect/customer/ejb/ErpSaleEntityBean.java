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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCancelOrderModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpChargeSettlementModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.customer.ErpFailedChargeSettlementModel;
import com.freshdirect.customer.ErpFailedSettlementModel;
import com.freshdirect.customer.ErpFundsRedepositModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
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
import com.freshdirect.framework.core.EntityBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
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

	private static Category LOGGER = LoggerFactory.getInstance(ErpSaleEntityBean.class);

	private ErpSaleModel model;

	private ErpComplaintList complaints;

	public void initialize() {
		this.model = new ErpSaleModel(null, null, new ArrayList(), new ArrayList(), null, null, Collections.EMPTY_SET, new ArrayList(), null,null);
		this.complaints = new ErpComplaintList();
	}

	public ModelI getModel() {
		return this.model.deepCopy();
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
			this.model.forcePaymentStatus();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void forceSettlement() throws ErpTransactionException {
		try{
			this.model.forceSettlement();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			conn = this.getConnection();
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
			conn = this.getConnection();
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

	public Collection ejbFindByStatus(EnumSaleStatus saleStatus) throws FinderException {
		return this.performFind("SELECT ID FROM CUST.SALE WHERE STATUS=?", saleStatus.getStatusCode());
	}

	private Collection performFind(String statement, String parameter) throws FinderException {
		Connection conn = null;
		try {
			conn = this.getConnection();

			PreparedStatement ps = conn.prepareStatement(statement);
			if (parameter != null) {
				ps.setString(1, parameter);
			}
			ResultSet rs = ps.executeQuery();

			List lst = new ArrayList();
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

/*	*//**
	 * Create sales with a create order transcation. Status will be NEW.
	 *//*
	public PrimaryKey ejbCreate(PrimaryKey customerPk, ErpCreateOrderModel createOrder, Set usedPromotionCodes)
		throws CreateException {
		Connection conn = null;
		try {
			this.initialize();
			this.model = new ErpSaleModel(customerPk, createOrder, usedPromotionCodes);
			conn = this.getConnection();
			return this.create(conn);

		} catch (SQLException sqle) {
			LOGGER.warn("Error in ejbCreate", sqle);
			throw new CreateException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}*/

	/**
	 * Create sales with a create order transcation and uses a delivery pass. Status will be NEW.
	 */
	public PrimaryKey ejbCreate(PrimaryKey customerPk, ErpCreateOrderModel createOrder, Set usedPromotionCodes, String dlvPassId,EnumSaleType saleType)
		throws CreateException {
		Connection conn = null;
		try {
			this.initialize();
			this.model = new ErpSaleModel(customerPk, createOrder, usedPromotionCodes, dlvPassId,saleType);
			conn = this.getConnection();
			return this.create(conn);

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

	public void ejbPostCreate(PrimaryKey customerPk, ErpCreateOrderModel createOrder, Set usedPromotionCodes, String dlvPassId,EnumSaleType type) {
		// this space is intentionally blank :)
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		this.setPK(new PrimaryKey(this.getNextId(conn, "CUST")));
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALE (ID,CUSTOMER_ID,STATUS,SAP_NUMBER, DLV_PASS_ID,TYPE,CROMOD_DATE) values (?,?,?,?,?,?,?)");
		ps.setString(1, this.getPK().getId());
		ps.setString(2, this.model.getCustomerPk().getId());
		ps.setString(3, this.model.getStatus().getStatusCode());
		ps.setString(4, this.model.getSapOrderNumber());
		String dlvPassId = this.model.getDeliveryPassId();
		if(dlvPassId != null){
			//True when this order uses a delivery pass.
			ps.setString(5, dlvPassId);
		}else{
			//True when this order contains a delivery pass.
			ps.setNull(5, Types.VARCHAR);
		}

		ps.setString(6,this.model.getType().getSaleType());
		//Added as part of PERF-27 task.
		ps.setTimestamp(7, new java.sql.Timestamp(this.model.getCurrentOrder().getTransactionDate().getTime()));

		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
		} catch (SQLException sqle) {
			this.setPK(null);
			throw sqle;
		} finally {
			ps.close();
		}

		// create children
		ErpTransactionList txList = this.getTransactionPBList();
		txList.create(conn);

	
		complaints.create(conn);
		if(this.model.hasUsedPromotionCodes()==true) {
			ErpPromotionDAO.insert(conn, this.getPK(), this.model.getUsedPromotionCodes());
		}
		this.createCroModMaxDate(conn);

		return this.getPK();
	}

	private void createCroModMaxDate(Connection conn)throws SQLException {
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("INSERT INTO CUST.SALE_CRO_MOD_DATE(SALE_ID, MAX_DATE) VALUES(?,?)");
			ps.setString(1, this.getPK().getId());
			ps.setTimestamp(2, new java.sql.Timestamp(this.model.getCurrentOrder().getTransactionDate().getTime()));
			ps.execute();
		}finally{
			if(ps != null){
				ps.close();
			}
		}
	}

	private ErpTransactionList getTransactionPBList() throws SQLException{
		ErpTransactionList txList = new ErpTransactionList();
		txList.setParentPK(this.getPK());
		for(Iterator i = this.model.getTransactions().iterator(); i.hasNext(); ){
			ErpTransactionModel m = (ErpTransactionModel) i.next();			
			if(m.getTransactionType().isUpdatable()) {
				ErpUpdatableTransactionBean upb = ErpTransactionList.createUpdatablePersistentBean(m.getTransactionType(), m.getPK(), this.getPK());
				m.setCustomerId(this.model.getCustomerPk().getId());
				upb.setFromModel(m);
				txList.add(upb);
			} else {
				ErpTransactionPersistentBean pb = ErpTransactionList.createPersistentBean(m.getTransactionType(), m.getPK(), this.getPK());
				//Added as part of PERF-27 task.
				m.setCustomerId(this.model.getCustomerPk().getId());
				pb.setFromModel(m);
				txList.add(pb);
			}
		}
		return txList;
	}


	private ErpComplaintList buildComplaintPBList() {
		ErpComplaintList compList = new ErpComplaintList();
		compList.setParentPK(this.getPK());
		for(Iterator i = this.model.getComplaints().iterator(); i.hasNext(); ){
			ErpComplaintPersistentBean pb = new ErpComplaintPersistentBean();
			pb.setFromModel((ErpComplaintModel)i.next());
			compList.add(pb);
		}
		return compList;
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
			"SELECT CUSTOMER_ID, STATUS, SAP_NUMBER, WAVE_NUMBER, TRUCK_NUMBER, STOP_SEQUENCE, NUM_REGULAR_CARTONS, NUM_FREEZER_CARTONS, NUM_ALCOHOL_CARTONS, DLV_PASS_ID,TYPE FROM CUST.SALE WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new SQLException("No such ErpSale PK: " + this.getPK());
		}
		// load properties from result set
		PrimaryKey customerPk = new PrimaryKey(rs.getString(1));
		EnumSaleStatus status = EnumSaleStatus.getSaleStatus(rs.getString(2));
		String sapOrderNumber = rs.getString(3);
		ErpShippingInfo shippingInfo = new ErpShippingInfo(rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getInt(9));
		String dlvPassId = rs.getString(10);
		String _saleType=rs.getString(11);
		EnumSaleType saleType=EnumSaleType.REGULAR;
		if(_saleType!=null) {
			saleType=EnumSaleType.getSaleType(_saleType);
		}
		rs.close();
		ps.close();

		// load children

		ErpTransactionList txList = new ErpTransactionList();
		txList.setParentPK(this.getPK());
		txList.load(conn);
		

		ErpComplaintList compList = new ErpComplaintList();
		compList.setParentPK(this.getPK());
		compList.load(conn);

		Set usedPromotionCodes = ErpPromotionDAO.select(conn, this.getPK());

		PrimaryKey oldPk = model.getPK();

		List cartonInfo = ErpCartonsDAO.getCartonInfo(conn, this.getPK());		
		this.model = new ErpSaleModel(customerPk, status, txList.getModelList(), compList.getModelList(), sapOrderNumber, shippingInfo, usedPromotionCodes, cartonInfo, dlvPassId,saleType);
		this.model.setPK(oldPk);

		super.decorateModel(this.model);
	}

	String saleUpdateWithWaveSQL =
		"UPDATE CUST.SALE SET STATUS=?, SAP_NUMBER=?, TRUCK_NUMBER=?, STOP_SEQUENCE=?, NUM_REGULAR_CARTONS=?, NUM_FREEZER_CARTONS=?, NUM_ALCOHOL_CARTONS=?, DLV_PASS_ID=?, CROMOD_DATE=?, WAVE_NUMBER=? WHERE ID=?";

	String saleUpdateWithNoWaveSQL =
		"UPDATE CUST.SALE SET STATUS=?, SAP_NUMBER=?, TRUCK_NUMBER=?, STOP_SEQUENCE=?, NUM_REGULAR_CARTONS=?, NUM_FREEZER_CARTONS=?, NUM_ALCOHOL_CARTONS=?, DLV_PASS_ID=?, CROMOD_DATE=? WHERE ID=?";

	public void store(Connection conn) throws SQLException {
		if (isModified()) {
			PreparedStatement ps = null;
			ErpShippingInfo sInfo = this.model.getShippingInfo();
			if (sInfo != null && sInfo.getWaveNumber() != null) {
				ps = conn.prepareStatement(saleUpdateWithWaveSQL);
			} else {
				ps = conn.prepareStatement(saleUpdateWithNoWaveSQL);
			}
			int index = 1;
			ps.setString(index++, this.model.getStatus().getStatusCode());
			ps.setString(index++, this.model.getSapOrderNumber());
			ps.setString(index++, sInfo != null ? sInfo.getTruckNumber() : null);
			ps.setString(index++, sInfo != null ? sInfo.getStopSequence() : null);
			ps.setInt(index++, sInfo != null ? sInfo.getRegularCartons() : 0);
			ps.setInt(index++, sInfo != null ? sInfo.getFreezerCartons() : 0);
			ps.setInt(index++, sInfo != null ? sInfo.getAlcoholCartons() : 0);
			LOGGER.debug("Delivery pass id in store "+model.getDeliveryPassId());
			//Update delivery pass id.
			if(this.model.getDeliveryPassId() != null){
				ps.setString(index++, this.model.getDeliveryPassId());
			}else{
				ps.setNull(index++, Types.VARCHAR);
			}

			//Added as part of PERF-27 task.
			ps.setTimestamp(index++, new java.sql.Timestamp(this.model.getCurrentOrder().getTransactionDate().getTime()));

			// DO NOT UPDATE SALE.wave_number in database if getWaveNumber returns null
			if (sInfo != null && sInfo.getWaveNumber() != null) {
				ps.setString(index++, sInfo.getWaveNumber());
			}

			ps.setString(index++, this.getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not updated");
			}
			ps.close();

			ErpPromotionDAO.delete(conn, this.getPK());
			if(this.model.hasUsedPromotionCodes())
				ErpPromotionDAO.insert(conn, this.getPK(), this.model.getUsedPromotionCodes());
		}
		//store children
		ErpTransactionList txList = this.getTransactionPBList();
		if (txList.isModified()) {
			txList.store(conn);
			this.updateCroModMaxDate(conn);
		}
	
		if (complaints.isModified()) {
			complaints.store(conn);
		}

	}

	private void updateCroModMaxDate(Connection conn)throws SQLException {
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("UPDATE CUST.SALE_CRO_MOD_DATE SET MAX_DATE = ? WHERE SALE_ID = ?");
			ps.setTimestamp(1, new java.sql.Timestamp(this.model.getCurrentOrder().getTransactionDate().getTime()));
			ps.setString(2, this.getPK().getId());
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
		return this.model.getCustomerPk();
	}

	public EnumSaleStatus getStatus() {
		return this.model.getStatus();
	}

	public String getSapOrderNumber() {
		return this.model.getSapOrderNumber();
	}

	public Collection getTransactions() {
		return this.model.getTransactions();
	}

	public Collection getComplaints() {
		return this.model.getComplaints();
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
			this.model.submitFailed(message);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.createOrderComplete(sapOrderNumber);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Change the order. Can only change a SUBMITTED order.
	 * Status will be MODIFIED after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void modifyOrder(ErpModifyOrderModel model, Set usedPromotionCodes) throws ErpTransactionException {
		try{
			this.model.modifyOrder(model, usedPromotionCodes);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.modifyOrderComplete();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.cancelOrder(cancelOrder);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.cancelGCOrder(cancelOrder);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void cutoff() throws ErpTransactionException {
		try{
			this.model.cutoff();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	public void emailPending() throws ErpTransactionException{
		try{
			this.model.emailPending();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
		
	}

	public void setGiftCardRegPending() throws ErpTransactionException{
		try{
			this.model.setGiftCardRegPending();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.addInvoice(invoiceModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargeInvoice(ErpChargeInvoiceModel chargeInvoiceModel) throws ErpTransactionException {
		try{
			this.model.addChargeInvoice(chargeInvoiceModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public List reconcileSale() throws ErpTransactionException {
		try{
			return this.model.reconcileSale();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
		this.model.updateShippingInfo(shippingInfo);
		this.setModified();
	}

	public void markAsRedelivery() throws ErpTransactionException {
		try{
			this.model.markAsRedelivery();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void markAsEnroute() throws ErpTransactionException {
		try{
			this.model.markAsEnroute();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void markAsReturn() throws ErpTransactionException {
		try{
			this.model.markAsReturn();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addReturn(ErpReturnOrderModel returnModel) throws ErpTransactionException {
		try{
			this.model.addReturn(returnModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addRedelivery(ErpRedeliveryModel redeliveryModel) throws ErpTransactionException {
		try{
			this.model.addRedelivery(redeliveryModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel) throws ErpTransactionException {
		try{
			// If the order involves gift cards, do 'Post Auth', else proceed as usual.
			if(null != this.getCurrentOrder().getAppliedGiftcards() && this.getCurrentOrder().getAppliedGiftcards().size() > 0) {
				this.model.addDeliveryConfirm(deliveryConfirmModel,EnumSaleStatus.POST_AUTH_PENDING);
				this.setModified();
			}else{
				this.model.addDeliveryConfirm(deliveryConfirmModel,EnumSaleStatus.CAPTURE_PENDING);
				this.setModified();
			}
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	
	
	public void addRegisterGiftCard(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException {
		
		try{
			this.model.addRegisterGiftCard(registerGCModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
			

	public void addGiftCardEmailInfo(ErpGiftCardTransModel emailGCModel) throws ErpTransactionException {
		
		try{			
			this.model.addGiftCardEmailInfo(emailGCModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
	

	public void addGiftCardDeliveryConfirm(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException {
		
		try{
			this.model.addGiftCardDlvConfirm(registerGCModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.addComplaint(complaintModel);
			this.complaints = buildComplaintPBList();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void updateComplaint(ErpComplaintModel newComplaint) throws ErpTransactionException {
		try{
			this.model.updateComplaint(newComplaint);
			this.complaints = buildComplaintPBList();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargeback(ErpChargebackModel chargebackModel) throws ErpTransactionException {
		try{
			this.model.addChargeback(chargebackModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargebackReversal(ErpChargebackReversalModel cbkReversal) throws ErpTransactionException {
		try{
			this.model.addChargebackReversal(cbkReversal);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addAdjustment(ErpAdjustmentModel adjustmentModel) throws ErpTransactionException {
		try{
			this.model.addAdjustment(adjustmentModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addResubmitPayment(ErpResubmitPaymentModel resubmitPaymentModel) throws ErpTransactionException {
		try{
			this.model.addResubmitPayment(resubmitPaymentModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.addAuthorization(authorizationModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
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
			this.model.addGCBalanceTransfer(authorizationModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	

	public void addSettlement(ErpSettlementModel settlementModel) throws ErpTransactionException {
		try{
			this.model.addSettlement(settlementModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addFailedSettlement(ErpFailedSettlementModel failedSettlementModel) throws ErpTransactionException {
		try{
			this.model.addFailedSettlement(failedSettlementModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addChargeSettlement(ErpChargeSettlementModel chargeSettlementModel) throws ErpTransactionException {
		try{
			this.model.addChargeSettlement(chargeSettlementModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addFundsRedeposit(ErpFundsRedepositModel fundsRedepositModel) throws ErpTransactionException {
		try{
			this.model.addFundsRedeposit(fundsRedepositModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addFailedChargeSettlement(ErpFailedChargeSettlementModel failedChargeSettlementModel) throws ErpTransactionException {
		try{
			this.model.addFailedChargeSettlement(failedChargeSettlementModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addManualAuthorization(ErpAuthorizationModel authorizationModel) throws ErpTransactionException {
		try{
			this.model.addManualAuthorization(authorizationModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addReversal(ErpReversalModel reversalModel) throws ErpTransactionException {
		try{
			this.model.addReversal(reversalModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addCashback(ErpCashbackModel cashbackModel) throws ErpTransactionException {
		try{
			this.model.addCashback(cashbackModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}	

	public void addCapture(ErpCaptureModel captureModel) throws ErpTransactionException {
		try{
			this.model.addCapture(captureModel);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void addVoidCapture(ErpVoidCaptureModel voidCapture) throws ErpTransactionException {
		try{
			this.model.addVoidCapture(voidCapture);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public java.util.Date getCaptureDate() throws ErpTransactionException {
		try{
			return this.model.getCaptureDate();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Get how the current order looks like. Returns the last create or change order transaction.
	 */
	public ErpAbstractOrderModel getCurrentOrder() {
		return this.model.getCurrentOrder();
	}

	public List getAuthorizations() {
		return this.model.getAuthorizations();
	}

	public List getApprovedAuthorizations() {
		return this.model.getApprovedAuthorizations();
	}

	public double getOutstandingCaptureAmount() {
		return this.model.getOutstandingCaptureAmount();
	}

	public List getCaptures() {
		return this.model.getCaptures();
	}

	public List getSettlements() throws ErpTransactionException {
		try{
			return this.model.getSettlements();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public List getAdjustments() throws ErpTransactionException {
		try{
			return this.model.getAdjustments();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public List getFailedAuthorizations() {
		return this.model.getFailedAuthorizations();
	}

	public ErpInvoiceModel getInvoice() throws ErpTransactionException {
		try{
			return this.model.getInvoice();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public void updateCartonInfo(List cartonInfo) throws ErpTransactionException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();

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

	public List getCartonInfo() throws ErpTransactionException, RemoteException {
		List returnList = new ArrayList();
		Connection conn = null;
		try {
			conn = this.getConnection();

			returnList = ErpCartonsDAO.getCartonInfo(conn, this.getPK());

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
		return this.model.getChargeInvoice();
	}

	public  boolean getIsChargePayment(String authId) {
		return this.model.getIsChargePayment(authId);
	}

	public  boolean getIsChargePayment(double chargeAmount) {
		return this.model.getIsChargePayment(chargeAmount);
	}

	public boolean hasChargeSettlement() {
		return this.model.hasChargeSettlement();
	}

	public boolean hasFundsRedeposit() {
		return this.model.hasFundsRedeposit();
	}

	public EnumTransactionType getCurrentTransactionType() {
		return this.model.getCurrentTransactionType();
	}

	public List getFailedSettlements() {
		return this.model.getFailedSettlements();
	}

	public List getChargeSettlements() {
			return this.model.getChargeSettlements();
	}

	public List getFundsRedeposits(){
		return this.model.getFundsRedeposits();
	}

	public List getFailedChargeSettlements() {
		return this.model.getFailedChargeSettlements();
	}

	public void reverseChargePayment() throws ErpTransactionException {
		try{
			this.model.reverseChargePayment();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}

	public boolean getIsSettlementFailedAfterSettled() {
		return this.model.getIsSettlementFailedAfterSettled();
	}

	//For Gift Cards _ begin.
	
	public List getPendingGCAuthorizations(ErpPaymentMethodI pm) {
		return this.model.getPendingGCAuthorizations(pm);
	}
	
	public List getPendingGCReverseAuths(ErpPaymentMethodI pm) {
		return this.model.getPendingGCReverseAuths(pm);
	}

	public List getValidGCAuthorizations(ErpPaymentMethodI pm){
		return this.model.getValidGCAuthorizations(pm);
	}
	
	
	public List getPendingGCAuthorizations() {
		return this.model.getPendingGCAuthorizations();
	}
	
	public List getValidGCAuthorizations() {
		return this.model.getValidGCAuthorizations();
	}
	
	public List getValidGCPostAuthorizations() {
		return this.model.getValidGCPostAuthorizations();
	}
	
	public void addGCPreAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException {
		try {
			this.model.addGCAuthorization(auth);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	public void addPostAuthorization(ErpPostAuthGiftCardModel postAuth) throws ErpTransactionException {
		try {
			this.model.addPostAuthorization(postAuth);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}		
	}
	
	public void addReverseGCPreAuthorization(ErpReverseAuthGiftCardModel rauth) throws ErpTransactionException {
		try {
			this.model.addReverseGCAuthorization(rauth);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}

	}
	
	public void cancelGCPreAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException {
		try {
			this.model.cancelGCAuthorization(auth);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}

	}

	public boolean hasValidPostAuth(ErpPaymentMethodI pm, String preAuthCode) {
		return this.model.hasValidPostAuth(pm, preAuthCode);
	}
	
	public void updateGCAuthorization(ErpGiftCardAuthModel auth) throws ErpTransactionException {
		try {
			this.model.updateGCAuthorization(auth);
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	public void markAsCapturePending() throws ErpTransactionException {
		try {
			this.model.markAsCapturePending();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}		
		
	}
	
	public void markAsSettlementPending() throws ErpTransactionException {
		try {
			this.model.markAsSettlementPending();
			this.setModified();
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}			
		
	}
	
	private static class ErpTransactionList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			PrimaryKey ppk = (PrimaryKey) ErpTransactionList.this.getParentPK();
			List txList = new ArrayList();

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
		public void load(Connection conn) throws SQLException {
			PrimaryKey ppk = (PrimaryKey) ErpComplaintList.this.getParentPK();
			List complaintList = new ArrayList();
			complaintList.addAll(ErpComplaintPersistentBean.findByParent(conn, ppk));
			ErpComplaintList.this.set(complaintList);
		}
	}

	public Collection ejbFindByDeliveryPassId(String dlvPassId) throws FinderException {
		return this.performFind("SELECT ID FROM CUST.SALE WHERE DLV_PASS_ID = ?", dlvPassId);
	}

	/**
	 * This method updates the delivery pass id of the sale model.
	 * @param dlvPassId
	 * @throws RemoteException
	 */

	public void updateDeliveryPassId(String dlvPassId) {
			this.model.setDeliveryPassId(dlvPassId);
			this.setModified();
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
			conn = this.getConnection();
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
		return this.model.getRecentRegisteration();
	}
	
	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel, EnumSaleStatus enumSaleStatus) throws ErpTransactionException{
		try{
			this.model.addDeliveryConfirm(deliveryConfirmModel,enumSaleStatus);			
			this.setModified();
			
		}catch(ErpTransactionException e){
			this.getEntityContext().setRollbackOnly();
			throw e;
		}
	}
	
	/*public void addSettlementPending(ErpSettlementPendingModel erpSettlementPendingModel) throws ErpTransactionException{
		this.model.addSettlementPending(erpSettlementPendingModel);
		this.setModified();
	}*/
}
