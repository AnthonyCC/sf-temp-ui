package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionDetailModel;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponHistoryInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
/**
 * 
 * @author ksriram
 *
 */
public class FDCouponManagerSessionBean extends ERPSessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getInstance(FDCouponManagerSessionBean.class);
	
	public void loadAndSaveCoupons(FDCouponActivityContext couponActivityContext) throws FDResourceException,CouponServiceException {
		Connection conn = null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		try {
			List<FDCouponInfo> coupons = FDCouponGateway.getCoupons(couponActivityContext);
			conn = getConnection();
			FDCouponManagerDAO.storeCoupons(conn, coupons);
		} catch (SQLException e) {
			LOGGER.info("Exception in loadAndSaveCoupons(): "+e);
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
		
	}
	
	public List<FDCouponInfo> getActiveCoupons() throws FDResourceException{
		Connection conn =null;
		List<FDCouponInfo> coupons= null;
		try {
			conn = getConnection();
			coupons = FDCouponManagerDAO.getActiveCoupons(conn,null);
		} catch (SQLException e) {
			LOGGER.info("Exception in getActiveCoupons(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
		return coupons;
	}
	
	public List<FDCouponInfo> getActiveCoupons(Date lastModified) throws FDResourceException {
		Connection conn =null;
		List<FDCouponInfo> coupons= null;
		try {
			conn = getConnection();
			coupons = FDCouponManagerDAO.getActiveCoupons(conn,lastModified);
		} catch (SQLException e) {
			LOGGER.info("Exception in getActiveCoupons(): "+e);
		} finally {
            close(conn);
		}
		return coupons;
	}
	
	public FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext)  throws CouponServiceException {
		return FDCouponGateway.getCouponsForUser(couponCustomer,couponActivityContext);		
	}
	
	public  boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext) throws CouponServiceException {
		boolean isSuccess= false;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return isSuccess;
		}
		isSuccess = FDCouponGateway.doClipCoupon(couponId,couponCustomer,couponActivityContext);		
		return isSuccess;
	}
	
	public Map<String, FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart,FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		Map<String, FDCouponEligibleInfo> fdEligibleCoupons =null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return null;
		}
		fdEligibleCoupons = FDCouponGateway.evaluateCartAndCoupons(couponCart,couponActivityContext);		
		return fdEligibleCoupons;
	}	
	
	public List<ErpCouponTransactionModel> getPendingCouponTransactions() throws FDResourceException {
		Connection conn =null;
		List<ErpCouponTransactionModel> couponTransactions= null;
		try {
			conn = getConnection();
			couponTransactions = FDCouponTransactionDAO.getSubmitPendingCouponTransactions(conn);
		} catch (SQLException e) {
			LOGGER.info("Exception in getPendingCouponTransactions(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
		return couponTransactions;
	}
	
	public void postSubmitPendingCouponTransactions() throws FDResourceException {
		Connection conn =null;
		List<ErpCouponTransactionModel> couponTransactions= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		try {
			conn = getConnection();
			couponTransactions = FDCouponTransactionDAO.getSubmitPendingCouponTransactions(conn);
			FDCouponActivityContext couponActivityContext = new FDCouponActivityContext(EnumTransactionSource.SYSTEM, "SYSTEM", null);
			if(null !=couponTransactions){
				for (Iterator<ErpCouponTransactionModel> iterator = couponTransactions.iterator(); iterator.hasNext();) {
					ErpCouponTransactionModel couponTransModel = iterator.next();
					boolean isSuccess =postSubmitOrder(conn, couponTransModel,couponActivityContext);
					//Mark all other 'Submit Pending' coupon transactions for the sale, if any, as 'Cancelled'.
					if(isSuccess){
						FDCouponTransactionDAO.cancelSubmitPendingCouponTransactions(conn, couponTransModel.getSaleId());
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.info("Exception in postSubmitPendingCouponTransactions(): "+e);
			throw new FDResourceException(e);
		}  finally {
            close(conn);
		}
	}

	public void postSubmitPendingCouponTransactions(String saleId) throws FDResourceException {
		Connection conn =null;
		ErpCouponTransactionModel couponTransModel= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		try {
			conn = getConnection();
			couponTransModel = FDCouponTransactionDAO.getSubmitPendingCouponTransaction(conn, saleId);
			FDCouponActivityContext couponActivityContext = new FDCouponActivityContext(EnumTransactionSource.SYSTEM, "SYSTEM", null);
			if(null !=couponTransModel){
				boolean isSuccess =postSubmitOrder(conn, couponTransModel,couponActivityContext);
				//Mark all other 'Submit Pending' coupon transactions for the sale, if any, as 'Cancelled'.
				if(isSuccess){
					FDCouponTransactionDAO.cancelSubmitPendingCouponTransactions(conn, couponTransModel.getSaleId());
				}
			}
		} catch (SQLException e) {
			LOGGER.info("Exception in postSubmitPendingCouponTransactions(): "+e);
			throw new FDResourceException(e);
		}  finally {
            close(conn);
		}
	}
	
	private boolean postSubmitOrder(Connection conn, ErpCouponTransactionModel couponTransModel,FDCouponActivityContext couponActivityContext) throws FDResourceException{

		boolean isSuccess= false;
		try {
			String saleId=couponTransModel.getSaleId();
			Date currentTime= new Date();
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();					
			FDCouponCustomer request = new FDCouponCustomer();
			request.setCouponCustomerId(saleEB.getCustomerPk().getId());
			CouponCart couponCart = new CouponCart(request,order.getOrderLines(),saleId,couponTransModel.getTranType());
			try {
				Map<String,FDCouponEligibleInfo> fdEligibleCoupons =FDCouponGateway.submitOrder(couponCart,couponActivityContext);
				isSuccess= true;
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.SUCCESS);
				couponTransModel.setTranTime(currentTime);
				if(null !=fdEligibleCoupons){
					List<ErpCouponTransactionDetailModel> detailList = new ArrayList<ErpCouponTransactionDetailModel>();
					couponTransModel.setTranDetails(detailList);
					for (Iterator<String> iterator = fdEligibleCoupons.keySet().iterator(); iterator.hasNext();) {
						String key=iterator.next();
						FDCouponEligibleInfo fdCouponEligibleInfo = (FDCouponEligibleInfo) fdEligibleCoupons.get(key);
						List<ErpOrderLineModel> orderLines =order.getOrderLines();
						for (Iterator iterator2 = orderLines.iterator(); iterator2
								.hasNext();) {
							ErpOrderLineModel erpOrderLineModel = (ErpOrderLineModel) iterator2.next();
							ErpCouponDiscountLineModel couponDiscount =erpOrderLineModel.getCouponDiscount();
							if(null!=couponDiscount && fdCouponEligibleInfo.getCouponId().equalsIgnoreCase(couponDiscount.getCouponId())){
								ErpCouponTransactionDetailModel tranDetailModel = new ErpCouponTransactionDetailModel();
								tranDetailModel.setCouponId(fdCouponEligibleInfo.getCouponId());
								tranDetailModel.setDiscountAmt(fdCouponEligibleInfo.getDiscount());
								tranDetailModel.setTransTime(currentTime);	
								tranDetailModel.setCouponTransId(couponTransModel.getId());
								tranDetailModel.setCouponLineId(couponDiscount.getId());
								detailList.add(tranDetailModel);
							}
							
						}
						
						
					}
				}
			} catch (CouponServiceException e) {
				LOGGER.info("Error in PostSubmitOrder() :"+e);
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.FAILURE);
				couponTransModel.setTranTime(currentTime);
				couponTransModel.setErrorMessage(e.getMessage());
				couponTransModel.setErrorDetails(e.getDetails());							
			}
			FDCouponTransactionDAO.updateCouponTransaction(conn, couponTransModel);
			
		} catch (RemoteException e) {
			LOGGER.info("Exception in postSubmitOrder(): "+e);
			throw new FDResourceException(e);
		} catch (FinderException e) {
			LOGGER.info("Exception in postSubmitOrder(): "+e);
			throw new FDResourceException(e);
		} catch (SQLException e) {
			LOGGER.info("Exception in postSubmitOrder(): "+e);
			throw new FDResourceException(e);
		}
		return isSuccess;
	}

	
	public void postCancelPendingCouponTransactions() throws FDResourceException {
		Connection conn =null;
		List<ErpCouponTransactionModel> couponTransactions= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		try {
			conn = getConnection();
			couponTransactions = FDCouponTransactionDAO.getCancelPendingCouponTransactions(conn);
			FDCouponActivityContext couponActivityContext = new FDCouponActivityContext(EnumTransactionSource.SYSTEM, "SYSTEM", null);
			if(null !=couponTransactions){
				for (Iterator<ErpCouponTransactionModel> iterator = couponTransactions.iterator(); iterator.hasNext();) {
					ErpCouponTransactionModel couponTransModel = iterator.next();
					boolean isSuccess =postCancelOrder(conn, couponTransModel,couponActivityContext);

					//Mark all other 'Submit Pending' and 'Confirm Pending' coupon transactions for the sale, if any, as 'Cancelled'.
					if(isSuccess){
						FDCouponTransactionDAO.cancelSubmitPendingCouponTransactions(conn, couponTransModel.getSaleId());
						FDCouponTransactionDAO.cancelConfirmPendingCouponTransactions(conn, couponTransModel.getSaleId());
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.info("Exception  in postCancelPendingCouponTransactions(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
	}

	private boolean postCancelOrder(Connection conn, ErpCouponTransactionModel couponTransModel,FDCouponActivityContext couponActivityContext) throws FDResourceException {
		
		String saleId=couponTransModel.getSaleId();
		Date currentTime= new Date();
		boolean isSuccess= false;
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			FDCouponCustomer request = new FDCouponCustomer();
			request.setCouponCustomerId(saleEB.getCustomerPk().getId());
			try {
				FDCouponGateway.cancelOrder(saleId,request,couponActivityContext);
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.SUCCESS);
				couponTransModel.setTranTime(currentTime);
				isSuccess = true;
			} catch (CouponServiceException e) {
				LOGGER.info("Error in PostCancelOrder() :"+e);
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.FAILURE);
				couponTransModel.setErrorMessage(e.getMessage());
				couponTransModel.setErrorDetails(e.getDetails());
				couponTransModel.setTranTime(currentTime);
			}
			
			FDCouponTransactionDAO.updateCouponTransaction(conn, couponTransModel);
		} catch (RemoteException e) {
			LOGGER.info("Exception in postCancelOrder(): "+e);
			throw new FDResourceException(e);
		} catch (FinderException e) {
			LOGGER.info("Exception in postCancelOrder(): "+e);
			throw new FDResourceException(e);
		} catch (SQLException e) {
			LOGGER.info("Exception in postCancelOrder(): "+e);
			throw new FDResourceException(e);
		}
		return isSuccess;
	}
	
	
	public void postConfirmPendingCouponTransactions() throws FDResourceException {
		Connection conn =null;
		List<ErpCouponTransactionModel> couponTransactions= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		try {
			conn = getConnection();
			couponTransactions = FDCouponTransactionDAO.getConfirmPendingCouponTransactions(conn);
			postConfirmPendingCouponTransactions(conn, couponTransactions);
		} catch (SQLException e) {
			LOGGER.info("Exception in postConfirmPendingCouponTransactions(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
	}
	
	public List<String> getConfirmPendingCouponSales() throws FDResourceException {
		Connection conn =null;
		List<String> couponSales= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return Collections.EMPTY_LIST;
		}
		try {
			conn = getConnection();
			couponSales = FDCouponTransactionDAO.getConfirmPendingCouponSales(conn);
		} catch (SQLException e) {
			LOGGER.info("Exception in getConfirmPendingCouponSales(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
		return couponSales;
	}
	
	public void postConfirmPendingCouponTransactions(String saleId) throws FDResourceException {
		Connection conn =null;
		List<ErpCouponTransactionModel> couponTransactions= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		try {
			conn = getConnection();
			ErpCouponTransactionModel couponTransaction = FDCouponTransactionDAO.getConfirmPendingCouponTransaction(conn,saleId);
			if(null !=couponTransaction){
				couponTransactions = new ArrayList<ErpCouponTransactionModel>();
				couponTransactions.add(couponTransaction);
				postConfirmPendingCouponTransactions(conn, couponTransactions);
			}
		} catch (SQLException e) {
			LOGGER.info("Exception in postConfirmPendingCouponTransactions(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
	}

	private void postConfirmPendingCouponTransactions(Connection conn,
			List<ErpCouponTransactionModel> couponTransactions)
			throws FDResourceException, SQLException {
		FDCouponActivityContext couponActivityContext = new FDCouponActivityContext(EnumTransactionSource.SYSTEM, "SYSTEM", null);
		if(null !=couponTransactions){
			for (Iterator<ErpCouponTransactionModel> iterator = couponTransactions.iterator(); iterator.hasNext();) {
				ErpCouponTransactionModel couponTransModel = iterator.next();
				boolean isSuccess=postConfirmOrder(conn, couponTransModel,couponActivityContext);
				//Mark all other 'Confirm Pending' coupon transactions for the sale, if any, as 'Cancelled'.
				if(isSuccess){
					FDCouponTransactionDAO.cancelConfirmPendingCouponTransactions(conn, couponTransModel.getSaleId());
				}
			}
		}
	}

	private boolean postConfirmOrder(Connection conn, ErpCouponTransactionModel couponTransModel,FDCouponActivityContext couponActivityContext) throws FDResourceException {
		String saleId=couponTransModel.getSaleId();
		Date currentTime= new Date();
		boolean isSuccess= false;
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));					
			FDCouponCustomer request = new FDCouponCustomer();
			request.setCouponCustomerId(saleEB.getCustomerPk().getId());
			try {
				ErpSaleModel sale =(ErpSaleModel)saleEB.getModel();
				Set<ErpOrderLineModel> couponOrderLines = sale.getAllCouponDiscounts();
				Set<String> coupons = new HashSet<String>();
				ErpInvoiceModel invoiceModel = sale.getLastInvoice();
				if(null !=invoiceModel && null !=invoiceModel.getInvoiceLines() && null !=couponOrderLines){					
					for (Iterator<ErpOrderLineModel> iterator1 = couponOrderLines.iterator(); iterator1.hasNext();) {
						ErpOrderLineModel couponOrderLine = iterator1.next();
						ErpInvoiceLineModel invoiceLine =invoiceModel.getInvoiceLine(couponOrderLine.getOrderLineNumber());
						if(null !=invoiceLine && invoiceLine.getQuantity() > 0){
							coupons.add(couponOrderLine.getCouponDiscount().getCouponId());
						}						
					}					
				}
				
				FDCouponGateway.confirmOrder(saleId,request,couponActivityContext,coupons);
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.SUCCESS);
				couponTransModel.setTranTime(currentTime);
				List<ErpCouponTransactionDetailModel> detailList = new ArrayList<ErpCouponTransactionDetailModel>();
				if(null !=coupons && !coupons.isEmpty()){
					for (Iterator<String> iterator = coupons.iterator(); iterator
							.hasNext();) {
						String couponId = iterator.next();
						ErpCouponTransactionDetailModel tranDetailModel = new ErpCouponTransactionDetailModel();
						couponTransModel.setTranDetails(detailList);
						tranDetailModel.setCouponId(couponId);
//						tranDetailModel.setDiscountAmt(fdCouponEligibleInfo.getDiscount());
						tranDetailModel.setTransTime(currentTime);	
						tranDetailModel.setCouponTransId(couponTransModel.getId());
//						tranDetailModel.setCouponLineId(couponDiscount.getId());
						detailList.add(tranDetailModel);
					}
					
				}
				isSuccess = true;
			} catch (CouponServiceException e) {
				LOGGER.info("Error in PostConfirmOrder() :"+e);
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.FAILURE);
				couponTransModel.setTranTime(currentTime);
				couponTransModel.setErrorMessage(e.getMessage());
				couponTransModel.setErrorDetails(e.getDetails());							
			}
			FDCouponTransactionDAO.updateCouponTransaction(conn, couponTransModel);
			
		} catch (RemoteException e) {
			LOGGER.info("Exception in postConfirmOrder(): "+e);
			throw new FDResourceException(e);
		} catch (FinderException e) {
			LOGGER.info("Exception in postConfirmOrder(): "+e);
			throw new FDResourceException(e);
		} catch (SQLException e) {
			LOGGER.info("Exception in postConfirmOrder(): "+e);
			throw new FDResourceException(e);
		}
		return isSuccess;
	}
	
	public void postCouponOrder(ErpCouponTransactionModel couponTransModel,FDCouponActivityContext couponActivityContext) throws FDResourceException{
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return;
		}
		if(null !=couponTransModel && null !=couponTransModel.getTranType()){
			Connection conn =null;
			try {
				conn = getConnection();
				if(EnumCouponTransactionType.CREATE_ORDER.equals(couponTransModel.getTranType()) || EnumCouponTransactionType.MODIFY_ORDER.equals(couponTransModel.getTranType())){
					postSubmitOrder(conn, couponTransModel,couponActivityContext);
				}else if(EnumCouponTransactionType.CANCEL_ORDER.equals(couponTransModel.getTranType()) ){
					postCancelOrder(conn, couponTransModel,couponActivityContext);
				}else if(EnumCouponTransactionType.CONFIRM_ORDER.equals(couponTransModel.getTranType()) ){
					postConfirmOrder(conn, couponTransModel,couponActivityContext);
				}
			} catch (SQLException e) {
				LOGGER.info("Exception in postCouponOrder(): "+e);
				throw new FDResourceException(e);
			} finally {
	            close(conn);
			}
		}
	}
	
	public void updateCouponTransaction(ErpCouponTransactionModel transModel) throws FDResourceException{
		Connection conn =null;
		try {
			conn = getConnection();
			FDCouponTransactionDAO.updateCouponTransaction(conn,transModel);
		} catch (SQLException e) {
			LOGGER.info("Exception in updateCouponTransaction(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
	}
	
	public List<String> getSubmitPendingCouponSales() throws FDResourceException {
		Connection conn =null;
		List<String> couponSales= null;
		if(FDCouponProperties.isCouponsBlackHoleEnabled()){
			LOGGER.debug("Coupons blackhole enabled.");
			return Collections.EMPTY_LIST;
		}
		try {
			conn = getConnection();
			couponSales = FDCouponTransactionDAO.getSubmitPendingCouponSales(conn);
		} catch (SQLException e) {
			LOGGER.info("Exception in getSubmitPendingCouponSales(): "+e);
			throw new FDResourceException(e);
		} finally {
            close(conn);
		}
		return couponSales;
	}
}
