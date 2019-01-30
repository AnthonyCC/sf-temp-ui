package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAbstractSettlementModel;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.customer.ErpFailedChargeSettlementModel;
import com.freshdirect.customer.ErpFailedSettlementModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.customer.ejb.ErpCreateCaseCommand;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.fdstore.ecoupon.FDCouponManagerHome;
import com.freshdirect.fdstore.ecoupon.FDCouponManagerSB;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.CaptureStrategy;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.PayPalCaptureResponse;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.referral.extole.RafUtil;
;
// this Session bean has been migrated to 2.0 check with 2.0 team if nay changes required
public class PaymentSessionBean extends SessionBeanSupport{
	
	private final static Category LOGGER = LoggerFactory.getInstance( PaymentSessionBean.class );

	private transient ErpSaleHome erpSaleHome = null;
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	private static FDCouponManagerHome FdCouponHome =null;
		
	/**
	 * capture the authorization for a given sale id
	 * 
	 * @param String saleId to capture
	 * 
	 * @throws RemoteException if there is a problem in accessing any remote resources
	 */
	public void captureAuthorization(String saleId) throws ErpTransactionException {
		ErpSaleModel sale = null;
		boolean freeOrder = false;
		boolean ebtOrder = false;
		if(this.erpSaleHome == null){
			this.lookupErpSaleHome();
		}
		
		
		ErpPaymentMethodI paymentMethod = null;
		int captureCount = 0;
		
		UserTransaction utx = null;
		try{
			
			LOGGER.info("Capture authorization - start. saleId="+saleId);
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			sale = (ErpSaleModel) eb.getModel();
			EnumSaleStatus status = sale.getStatus();
			if(status.equals(EnumSaleStatus.PAYMENT_PENDING)){
				//if sale is already capture donot throw ErpTransactionException LOG and return
				LOGGER.warn("Sale is already Captured saleId: "+saleId);
				utx.commit();
				return;
			}
			
			paymentMethod = sale.getCurrentOrder().getPaymentMethod();
			paymentMethod.setCustomerId(sale.getCustomerPk().getId());
			captureCount = sale.getCaptures().size();
			
			EnumPaymentType paymentType = eb.getCurrentOrder().getPaymentMethod().getPaymentType();
			freeOrder = EnumPaymentType.MAKE_GOOD.equals(paymentType) || EnumPaymentType.ON_FD_ACCOUNT.equals(paymentType);
			ebtOrder = EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType());
			utx.commit();
		
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		}
		
		Map requiredCaptures = new CaptureStrategy(sale).getOutstandingCaptureAmounts();
		PaymentGatewayContext context = new PaymentGatewayContext(getGatewayType(paymentMethod), null);
//		Gateway gateway = GatewayFactory.getGateway(context);	
		
		try{
				
			List fdCaptures = freeOrder ? new ArrayList() : Collections.EMPTY_LIST;
			if(!requiredCaptures.isEmpty()) {
				Gateway gateway = GatewayFactory.getGateway(context);	
				for(Iterator i = requiredCaptures.keySet().iterator(); i.hasNext(); ) {
					double remainingAmount = 0;
					ErpAffiliate aff = (ErpAffiliate) i.next();
					Double amount = (Double) requiredCaptures.get(aff);
					if(amount != null) {
						remainingAmount = amount.doubleValue();
					}
					List auths = sale.getApprovedAuthorizations(aff, paymentMethod);
					if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
						auths = mergeEcheckAuthorizations(auths, paymentMethod);
					}
					
					Map captureMap = getCaptureAmounts(gateway,auths, remainingAmount);
			
					utx = null;
					
					for (Iterator j = auths.iterator(); j.hasNext(); ) {
						if(captureMap.isEmpty()) break;
						
						utx = this.getSessionContext().getUserTransaction();
						utx.begin();
						
						ErpAuthorizationModel auth = (ErpAuthorizationModel)j.next();
						Double captureAmount = (Double) captureMap.get(auth.getPK().getId());
						if(captureAmount == null){
							try {
								if (EnumPaymentMethodType.PAYPAL.equals(auth.getPaymentMethodType()) && EnumCardType.PAYPAL.equals(auth.getCardType())) {
									Request request = GatewayAdapter.getReverseAuthRequest(paymentMethod, auth);
									request.getBillingInfo().setEwalletTxId(auth.getEwalletTxId());
									gateway.reverseAuthorize(request);
								}
							} catch (Exception e) {
								LOGGER.info("Reversal of PayPal auth failed for seq number " + auth.getSequenceNumber() + " of order id " + saleId + ". It can be ignored");
							}
							utx.commit();
							continue;
						}
						
						ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
						//before capturing in Paylinx making sure that I will be able to add capture to sale
						EnumSaleStatus status = eb.getStatus();
						if ( !status.isCapturable()) {
							throw new ErpTransactionException("Sale status is not ENROUTE or PENDING or CAPTURE_PENDING or REDELIVERY");
						}
	
						ErpCaptureModel capture = null;
						if(freeOrder){
							capture = this.doFDCapture(auth, captureAmount.doubleValue(), 0.0);
							fdCaptures.add(capture);
						}else{
							
							String orderNumber = saleId + "X" + captureCount;
							
							capture = capturePayment(paymentMethod, gateway, auth, captureAmount,orderNumber,false);
							 
							// START APPDEV-5490 
							boolean isRejected=false;
							if(EnumPaymentMethodType.PAYPAL.equals(auth.getPaymentMethodType()) && PayPalCaptureResponse.Processor.SETTLEMENT_DECLINED.getResponseCode().
									equalsIgnoreCase(capture.getSettlementResponseCode())){
								LOGGER.info("Paypal capture is declined . Retry with new auth sale id : "+saleId );
								// Write Logic to authorize amount 
								ErpAuthorizationModel newAuth = gateway.authorize(paymentMethod, orderNumber,auth.getAmount(),auth.getTax(),auth.getMerchantId());
								
								if(newAuth.isApproved()){
									capture = capturePayment(paymentMethod, gateway, newAuth,captureAmount, orderNumber,true);
								}else if(isNotCaptureApproved(capture, newAuth)){
									isRejected=true;
								}

							} if(EnumPaymentMethodType.PAYPAL.equals(auth.getPaymentMethodType()) && (PayPalCaptureResponse.Processor.RISK_REJECTED.getResponseCode().
									equalsIgnoreCase(capture.getSettlementResponseCode()) || isRejected)){
								LOGGER.info("Paypal capture is rejected after retry. Mark settlement failure  sale id ."+ saleId );
								
								forceSettlementFailure(saleId, capture);
								utx.commit();
								continue;
							}
							// END APPDEV-5490 
						}
						
						eb.addCapture(capture);
						utx.commit();
						LOGGER.info("Capture Amount: "+captureAmount);
						LOGGER.info("Remaining Amount: "+remainingAmount);
					}
				
				}
			
				LOGGER.info("Capture authorization - done. saleId="+saleId);
			}
				
				
			LOGGER.info("Forcing the Status - start. saleId="+saleId);
			utx = null;
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			eb.forcePaymentStatus();
			utx.commit();
			LOGGER.info("Forcing the Status - done. saleId="+saleId);
			
			if(freeOrder && !fdCaptures.isEmpty()){
				utx = null;
				for(Iterator i = fdCaptures.iterator(); i.hasNext(); ) {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();
					eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
					ErpCaptureModel capture = (ErpCaptureModel) i.next();
					ErpSettlementModel settlementModel = this.getFDSettlement(capture);
					ErpAbstractOrderModel order = eb.getFirstOrderTransaction();
					if(null != order && null !=order.getRafTransModel()){
						settlementModel.setRafTransModel(RafUtil.getApproveTransModel());
					}
					eb.addSettlement(settlementModel);
					utx.commit();
				}
				ErpAbstractOrderModel order = eb.getCurrentOrder();
				if(EnumSaleStatus.SETTLED.equals(eb.getStatus()) && FDStoreProperties.getAvalaraTaxEnabled() && null!=order.getTaxationType() && EnumNotificationType.AVALARA.getCode().equals(order.getTaxationType().getCode())){
					NotificationModel notificationModel = new NotificationModel(saleId, EnumNotificationType.AVALARA, EnumSaleStatus.PENDING, "Avalara", eb.getInvoice().getAmount(), eb.getCaptureDate(),null);
					getPostSettlementNotificationHome().create(notificationModel);
				}
			}
			/*
			 *  In the case of gro orders that requires GC payments only we put the order
			 *  in Settlement Pending status for settlement reconcilation to pick it up.
			 */			
			if(!freeOrder && (null == requiredCaptures || requiredCaptures.isEmpty())){
				//Update the status to 'SETTLEMENT_PENDING'- for GiftCard used only orders.
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
				eb.markAsSettlementPending();
				utx.commit();
			}
			
			//Committing coupons, if any.
//			FDCouponManager.postConfirmPendingCouponTransactions(saleId);

			FDCouponManagerSB sb;
			try {
				sb = getFDCouponManagerHome().create();
				sb.postConfirmPendingCouponTransactions(saleId);
			} catch (RemoteException e1) {
				FdCouponHome=null;
				throw e1;
			} catch (CreateException e1) {
				FdCouponHome=null;
				throw e1;
			}
			
			
		}catch(ErpTransactionException e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error rolling back transaction", se);
			}
			throw new EJBException(e);
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error rolling back transaction", se);
			}
			throw new EJBException(e);
		}
	}

	/**
	 * @param saleId
	 * @param capture 
	 * @throws FinderException
	 * @throws RemoteException
	 * @throws ErpTransactionException
	 */
	private void forceSettlementFailure(String saleId, ErpCaptureModel capture) throws FinderException, RemoteException,
			ErpTransactionException {
		ErpSaleEB saleEB = getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));	
		ErpFailedSettlementModel failedTransaction = new ErpFailedSettlementModel();		
		failedTransaction.setCardType(EnumCardType.PAYPAL);
		failedTransaction.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
		failedTransaction.setAmount(capture.getAmount());
		failedTransaction.setResponseCode(EnumPaymentResponse.getEnum(capture.getResponseCode()));
		failedTransaction.setCcNumLast4(capture.getCcNumLast4());
		failedTransaction.setAbaRouteNumber(capture.getAbaRouteNumber());
		failedTransaction.setBankAccountType(capture.getBankAccountType());
		failedTransaction.setDescription(capture.getDescription());
		failedTransaction.setTransactionSource(EnumTransactionSource.SYSTEM);
		failedTransaction.setAffiliate(capture.getAffiliate());
		((ErpSaleModel)saleEB.getModel()).addFailedSettlementPYPL(failedTransaction);
		saleEB.markAsPaypalSettlementFailed();
		createCase(saleId, capture.getCustomerId(), CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR), "Sale failed to settle");
	}
	
	private void createCase(String saleId, String customerId, CrmCaseSubject subject, String summary) {
		CrmSystemCaseInfo info = new CrmSystemCaseInfo(new PrimaryKey(customerId), new PrimaryKey(saleId), subject, summary);
		new ErpCreateCaseCommand(LOCATOR, info).execute();
	}

	/**
	 * @param capture
	 * @param newAuth
	 * @return
	 */
	private boolean isNotCaptureApproved(ErpCaptureModel capture, ErpAuthorizationModel newAuth) {
		return !newAuth.isApproved() || PayPalCaptureResponse.Processor.RISK_REJECTED.
				getResponseCode().equals(capture.getSettlementResponseCode())
				|| PayPalCaptureResponse.Processor.SETTLEMENT_DECLINED.
				getResponseCode().equals(capture.getSettlementResponseCode());
	}

	/**
	 * @param paymentMethod
	 * @param gateway
	 * @param auth
	 * @param captureAmount
	 * @param orderNumber
	 * @param isReAuth 
	 * @return
	 * @throws ErpTransactionException
	 */
	private ErpCaptureModel capturePayment(ErpPaymentMethodI paymentMethod, Gateway gateway,
			ErpAuthorizationModel auth, Double captureAmount, String orderNumber, boolean isReAuth)
			throws ErpTransactionException {
		
		return gateway.capture(auth, paymentMethod, captureAmount.doubleValue(), 0.0, orderNumber);
	}
	
	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	
	private PostSettlementNotificationHome getPostSettlementNotificationHome() {
		try {
			return (PostSettlementNotificationHome) LOCATOR.getRemoteHome("freshdirect.payment.Notification");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	private FDCouponManagerHome getFDCouponManagerHome() {
		if(FdCouponHome!=null)
			return FdCouponHome;
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			FdCouponHome= (FDCouponManagerHome) ctx.lookup(FDStoreProperties.getFDCouponManagerHome());
			return FdCouponHome;
		} catch (NamingException ne) {
			throw new EJBException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	
	
	}
	
	private ErpCaptureModel doFDCapture(ErpAuthorizationModel auth, double amount, double tax) {
		
		ErpCaptureModel capture = new ErpCaptureModel();
		capture.setAuthCode(auth.getAuthCode());
		capture.setResponseCode(auth.getResponseCode().getCode());
		capture.setDescription(auth.getDescription());
		capture.setSequenceNumber("FD CAPTURE");
		capture.setTransactionSource(EnumTransactionSource.SYSTEM);
		capture.setAmount(amount);
		capture.setTax(tax);
		capture.setAffiliate(auth.getAffiliate());
		
		capture.setGatewayOrderID(auth.getGatewayOrderID());
		capture.setProfileID(auth.getProfileID());
		capture.setCcNumLast4(auth.getCcNumLast4())	;
		return capture;
	}
	
	private ErpCaptureModel doEBTCapture(ErpAuthorizationModel auth, double amount, double tax) {
		
		ErpCaptureModel capture = new ErpCaptureModel();
		capture.setCardType(EnumCardType.EBT);
		capture.setAuthCode(auth.getAuthCode());
		capture.setResponseCode(auth.getResponseCode().getCode());
		capture.setDescription(auth.getDescription());
		capture.setSequenceNumber("EBT CAPTURE");
		capture.setTransactionSource(EnumTransactionSource.SYSTEM);
		capture.setAmount(amount);
		capture.setTax(tax);
		capture.setAffiliate(auth.getAffiliate());
			
		return capture;
	}
	
	private ErpSettlementModel getFDSettlement(ErpCaptureModel capture){
		ErpSettlementModel settlement = new ErpSettlementModel();
		settlement.setAmount(capture.getAmount());
		settlement.setSequenceNumber("FD SETTLEMENT");
		settlement.setTax(capture.getTax());
		settlement.setTransactionDate(new Date());
		settlement.setTransactionSource(EnumTransactionSource.SYSTEM);
		settlement.setAffiliate(capture.getAffiliate());
		
		return settlement;
	}
	
	private ErpSettlementModel getEBTSettlement(ErpCaptureModel capture){
		ErpSettlementModel settlement = new ErpSettlementModel();
		settlement.setAmount(capture.getAmount());
		settlement.setSequenceNumber("EBT SETTLEMENT");
		settlement.setTax(capture.getTax());
		settlement.setTransactionDate(new Date());
		settlement.setTransactionSource(EnumTransactionSource.SYSTEM);
		settlement.setAffiliate(capture.getAffiliate());
		
		return settlement;
	}
	
	public void deliveryConfirm(String saleId) throws ErpTransactionException {
		UserTransaction utx = null;
		if(this.erpSaleHome == null){
			this.lookupErpSaleHome();
		}
		try{
			LOGGER.info("change the Status to CPG - start. saleId="+saleId);
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			ErpDeliveryConfirmModel deliveryConfirmModel = new ErpDeliveryConfirmModel();
			ErpSaleModel sale=(ErpSaleModel)eb.getModel();
			if(sale.hasCouponDiscounts()){
				ErpCouponTransactionModel transModel = new ErpCouponTransactionModel();
				transModel.setTranStatus(EnumCouponTransactionStatus.PENDING);
				transModel.setTranType(EnumCouponTransactionType.CONFIRM_ORDER);
				Date date =new Date();
				transModel.setCreateTime(date);
				transModel.setTranTime(date);
				deliveryConfirmModel.setCouponTransModel(transModel);
			}
			eb.addDeliveryConfirm(deliveryConfirmModel);
			utx.commit();
			LOGGER.info("change the Status to CPG - done. saleId="+saleId);
			
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error rolling back transaction", se);
			}
			throw new EJBException(e);
		}
	}

	public void unconfirm(String saleId) throws ErpTransactionException {
		UserTransaction utx = null;
		if(this.erpSaleHome == null){
			this.lookupErpSaleHome();
		}
		try{
			LOGGER.info("unconfirm: change the Status from CPG to ENR - start. saleId="+saleId);
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			eb.markAsEnroute();
			utx.commit();
			LOGGER.info("unconfirm: change the Status from CPG to ENR - done. saleId="+saleId);
			ErpCouponTransactionModel couponTransModel =getConfirmPendingCouponTransaction(saleId);
			if(null !=couponTransModel){
				couponTransModel.setTranTime(new Date());
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.CANCEL);
				updateCouponTransaction(couponTransModel);
			}
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error rolling back transaction", se);
			}
			throw new EJBException(e);
		}
	}

	private void updateCouponTransaction(
			ErpCouponTransactionModel couponTransModel)  throws FDResourceException,RemoteException,CreateException{
		FDCouponManagerSB sb;
		try {
			sb = getFDCouponManagerHome().create();
			sb.updateCouponTransaction(couponTransModel);
			
		} catch (RemoteException e1) {
			FdCouponHome=null;
			throw e1;
		} catch (CreateException e1) {
			FdCouponHome=null;
			throw e1;
		}
		}

	private ErpCouponTransactionModel getConfirmPendingCouponTransaction(
			String saleId) throws FDResourceException,RemoteException,CreateException {
		FDCouponManagerSB sb;
			try {
				sb = getFDCouponManagerHome().create();
				ErpCouponTransactionModel erpCoupMod = sb.getConfirmPendingCouponTransaction(saleId);
				return erpCoupMod;
			} catch (RemoteException e1) {
				FdCouponHome=null;
				throw e1;
			} catch (CreateException e1) {
				FdCouponHome=null;
				throw e1;
			}
			}

	protected static Map<String,Double> getCaptureAmounts(Gateway gateway, List<ErpAuthorizationModel> auths, double amount){
		if(auths.isEmpty()){
			return Collections.EMPTY_MAP;
		}
		
		Collections.sort(auths, AUTH_COMPARATOR);
		ErpAuthorizationModel a = (ErpAuthorizationModel) auths.get(0);
		
		String largestAuth = a.getPK().getId();
		double remainingAmount = amount;
		
		Map<String,Double> ret = new HashMap<String,Double>();
		for(Iterator<ErpAuthorizationModel> i = auths.iterator(); i.hasNext() && remainingAmount > 0; ){
			a =  i.next();
			double captureAmount = Math.min(a.getAmount(), remainingAmount);
			if(GatewayType.CYBERSOURCE.equals(gateway.getType())) {
				if(captureAmount <= 1 && !ret.isEmpty()){
					Double d = (Double) ret.get(largestAuth);
					ret.put(largestAuth, new Double(MathUtil.roundDecimal(d.doubleValue()+captureAmount)));
				}else{
					ret.put(a.getPK().getId(), new Double(MathUtil.roundDecimal(captureAmount)));
				}
			} else {
				ret.put(a.getPK().getId(), new Double(MathUtil.roundDecimal(captureAmount)));
			}
			remainingAmount = MathUtil.roundDecimal(remainingAmount - captureAmount);
		}
		return ret;
	}
	private static List mergeEcheckAuthorizations(List auths, ErpPaymentMethodI paymentMethod) {
		ErpAuthorizationModel combinedAuth = null;
		for(Iterator i = auths.iterator(); i.hasNext(); ){
			ErpAuthorizationModel a = (ErpAuthorizationModel) i.next();
			if(combinedAuth == null) {
				combinedAuth = new ErpAuthorizationModel();
				combinedAuth.setPaymentMethodType(paymentMethod.getPaymentMethodType());
				combinedAuth.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
				combinedAuth.setBankAccountType(paymentMethod.getBankAccountType());
				if(!StringUtil.isEmpty(paymentMethod.getMaskedAccountNumber())) {
					int l=paymentMethod.getMaskedAccountNumber().length();
					combinedAuth.setCcNumLast4(l>4?
							paymentMethod.getMaskedAccountNumber().substring(l-4,l):paymentMethod.getMaskedAccountNumber());
				}
				
				combinedAuth.setAuthCode(a.getAuthCode());
				combinedAuth.setAvs(a.getAvs());
				combinedAuth.setDescription(a.getDescription());
				combinedAuth.setMerchantId(a.getMerchantId());
				combinedAuth.setPK(a.getPK());
				combinedAuth.setResponseCode(a.getResponseCode());
				combinedAuth.setReturnCode(a.getReturnCode());
				combinedAuth.setSequenceNumber(a.getSequenceNumber());
				combinedAuth.setTransactionDate(a.getTransactionDate());
				combinedAuth.setTransactionInitiator(a.getTransactionInitiator());
				combinedAuth.setTransactionSource(a.getTransactionSource());
				combinedAuth.setAffiliate(a.getAffiliate());
				combinedAuth.setGatewayOrderID(a.getGatewayOrderID());
				combinedAuth.setProfileID(a.getProfileID());
			}
			
			combinedAuth.setAmount(combinedAuth.getAmount() + a.getAmount());
			combinedAuth.setTax(combinedAuth.getTax() + a.getTax());
		}
		
		if(combinedAuth == null) {
			return Collections.EMPTY_LIST;
		}
		List lst = new ArrayList();
		lst.add(combinedAuth);
		return lst;
	}
	
	/**
	 * Method to lookup a reference to ErpSaleEntityBean's home interface
	 */
	
	private void lookupErpSaleHome() {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.erpSaleHome = (ErpSaleHome) ctx.lookup("java:comp/env/ejb/ErpSale");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}
	
	private static final Comparator AUTH_COMPARATOR = new Comparator(){
				public int compare(Object o1, Object o2) {
					ErpTransactionModel tx1 = (ErpTransactionModel) o1;
					ErpTransactionModel tx2 = (ErpTransactionModel) o2;
					double amt1 = tx1.getAmount();
					double amt2 = tx2.getAmount();
					if(amt1 < amt2)
						return 1;
					if(amt1 == amt2)
						return 0;
					return -1;
				}
			};

	public void captureAuthEBTSale(String saleId) throws ErpTransactionException {

		ErpSaleModel sale = null;
		boolean isEBTOrder = false;
		if(this.erpSaleHome == null){
			this.lookupErpSaleHome();
		}
		
		ErpPaymentMethodI paymentMethod = null;
		UserTransaction utx = null;
		try{
			
			LOGGER.info("settleEBTOrder - start. saleId="+saleId);
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			sale = (ErpSaleModel) eb.getModel();
			EnumSaleStatus status = sale.getStatus();
			if(status.equals(EnumSaleStatus.SETTLED)){
				//If sale is already 'settled', do not throw ErpTransactionException LOG and return
				LOGGER.warn("Sale is already Captured saleId: "+saleId);
				utx.commit();
				return;
			}
			
			paymentMethod = sale.getCurrentOrder().getPaymentMethod();
			isEBTOrder = EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType());
			utx.commit();
			if(!isEBTOrder){
				LOGGER.warn("Sale is not by EBT payment: "+saleId);
				return;
			}
		
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		}
		
		Map requiredCaptures = new CaptureStrategy(sale).getOutstandingCaptureAmounts();
				
		try{
				
			List ebtCaptures = isEBTOrder ? new ArrayList() : Collections.EMPTY_LIST;
			if(!requiredCaptures.isEmpty()) {
				for(Iterator i = requiredCaptures.keySet().iterator(); i.hasNext(); ) {
					double remainingAmount = 0;
					ErpAffiliate aff = (ErpAffiliate) i.next();
					Double amount = (Double) requiredCaptures.get(aff);
					if(amount != null) {
						remainingAmount = amount.doubleValue();
					}
					List auths = sale.getApprovedAuthorizations(aff, paymentMethod);
					Map captureMap = getCaptureAmounts(GatewayFactory.getGateway(GatewayType.PAYMENTECH),auths, remainingAmount);
			
					utx = null;
					
					for (Iterator j = auths.iterator(); j.hasNext(); ) {
						if(captureMap.isEmpty()) break;
						
						utx = this.getSessionContext().getUserTransaction();
						utx.begin();
						
						ErpAuthorizationModel auth = (ErpAuthorizationModel)j.next();
						Double captureAmount = (Double) captureMap.get(auth.getPK().getId());
						if(captureAmount == null){
							utx.commit();
							continue;
						}
						
						ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
						//before capturing, making sure that we will be able to add capture to sale
						EnumSaleStatus status = eb.getStatus();
						if ( !status.isCapturable()) {
							throw new ErpTransactionException("Sale status is not ENROUTE or PENDING or CAPTURE_PENDING or REDELIVERY");
						}
	
						ErpCaptureModel capture = null;
						capture = this.doEBTCapture(auth, captureAmount.doubleValue(), 0.0);
						ebtCaptures.add(capture);
						
						eb.addCapture(capture);
						utx.commit();
						LOGGER.info("Capture Amount: "+captureAmount);
						LOGGER.info("Remaining Amount: "+remainingAmount);
					}
				
				}
			
				LOGGER.info("Capture authorization - done. saleId="+saleId);
			}
				
				
			LOGGER.info("Forcing the Status - start. saleId="+saleId);
			utx = null;
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			eb.forcePaymentStatus();
			utx.commit();
			LOGGER.info("Forcing the Status - done. saleId="+saleId);
			
			if(!ebtCaptures.isEmpty()){
				utx = null;
				for(Iterator i = ebtCaptures.iterator(); i.hasNext(); ) {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();
					eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
					ErpCaptureModel capture = (ErpCaptureModel) i.next();
					ErpSettlementModel settlementModel = this.getEBTSettlement(capture);
					ErpAbstractOrderModel order = eb.getFirstOrderTransaction();
					if(null != order && null !=order.getRafTransModel()){
						settlementModel.setRafTransModel(RafUtil.getApproveTransModel());
					}
					eb.addSettlement(settlementModel);
					utx.commit();
				}
			}
			/*
			 *  In the case of gro orders that requires GC payments only we put the order
			 *  in Settlement Pending status for settlement reconcilation to pick it up.
			 */			
			if(null == requiredCaptures || requiredCaptures.isEmpty()){
				//Update the status to 'SETTLEMENT_PENDING'- for GiftCard used only orders.
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
				eb.markAsSettlementPending();
				utx.commit();
			}
			
			//Committing coupons, if any.
			postConfirmPendingCouponTransactions(saleId);
			
			//TODO: Decide whether to introduce new status 'SETTLEMENT_SAP_PENDING' or not.
			//Check the sale status, if its settled, settle the sale in SAP.
			/*eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			sale = (ErpSaleModel) eb.getModel();
			EnumSaleStatus status = sale.getStatus();
			if(EnumSaleStatus.SETTLED.equals(status)){
				//TODO:Settle the order in SAP.
				if(!SapProperties.isBlackhole()){
					SapSendEBTSettlementCommand command = createSapSendEBTSettlementCommand(sale);
					command.execute();
				}
				
			}*/
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error rolling back transaction", se);
			}
			throw new EJBException(e);
		}
			
	}
	
	private void postConfirmPendingCouponTransactions(String saleId)throws FDResourceException,RemoteException,CreateException {
		FDCouponManagerSB sb;
		try {
			sb = getFDCouponManagerHome().create();
			sb.postConfirmPendingCouponTransactions(saleId);
			
		} catch (RemoteException e1) {
			FdCouponHome=null;
			throw e1;
		} catch (CreateException e1) {
			FdCouponHome=null;
			throw e1;
		}
		}

	private GatewayType getGatewayType(ErpPaymentMethodI pm) {
		String profileId = pm.getProfileID();
		if (StringUtil.isEmpty(profileId) && !EnumCardType.PAYPAL.equals(pm.getCardType())) {
			return GatewayType.CYBERSOURCE;
		} else {
			EnumCardType type = pm.getCardType();
			if (type != EnumCardType.PAYPAL) {
				return GatewayType.PAYMENTECH;
			} else {
				return GatewayType.PAYPAL;
			}
		}
	}
}
