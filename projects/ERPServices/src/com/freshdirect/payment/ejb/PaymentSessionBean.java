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

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import weblogic.wsee.util.StringUtil;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.fdstore.ecoupon.FDCouponManager;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.CaptureStrategy;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaylinxException;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
;

public class PaymentSessionBean extends SessionBeanSupport{
	
	private final static Category LOGGER = LoggerFactory.getInstance( PaymentSessionBean.class );

	private transient ErpSaleHome erpSaleHome = null;
	private transient PaymentGatewayHome gatewayHome=null;
		
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
		PaymentGatewayContext context = new PaymentGatewayContext(StringUtil.isEmpty(paymentMethod.getProfileID())?GatewayType.CYBERSOURCE:GatewayType.PAYMENTECH, null);
		Gateway gateway = GatewayFactory.getGateway(context);	
		
		try{
				
			List fdCaptures = freeOrder ? new ArrayList() : Collections.EMPTY_LIST;
			if(!requiredCaptures.isEmpty()) {
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
							capture = gateway.capture(auth, paymentMethod, captureAmount.doubleValue(), 0.0, orderNumber);
							
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
					eb.addSettlement(this.getFDSettlement(capture));
					utx.commit();
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
			FDCouponManager.postConfirmPendingCouponTransactions(saleId);
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
			ErpCouponTransactionModel couponTransModel =FDCouponManager.getConfirmPendingCouponTransaction(saleId);
			if(null !=couponTransModel){
				couponTransModel.setTranTime(new Date());
				couponTransModel.setTranStatus(EnumCouponTransactionStatus.CANCEL);
				FDCouponManager.updateCouponTransaction(couponTransModel);
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

	public void voidCaptures(String saleId) throws ErpTransactionException {
		List captures = null;
		Date captureDate = null;
		if(this.erpSaleHome == null){
			this.lookupErpSaleHome();
		}

		UserTransaction utx = null;
		try{
	
			LOGGER.info("Void Capture - start. saleId="+saleId);
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
	
			ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
	
			if(!status.equals(EnumSaleStatus.PAYMENT_PENDING)){
				throw new ErpTransactionException("Sale is not captured yet "+saleId);
			}
	
			captures = eb.getCaptures();
			captureDate = eb.getCaptureDate();
	
			utx.commit();

		}catch(ErpTransactionException te){
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw te;	
		}catch(Exception e){
			LOGGER.warn(e);
			try{
				utx.rollback();
			}catch(SystemException se){
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		}
		
		Calendar captureCal = new GregorianCalendar();
		captureCal.setTime(captureDate);
		captureCal.add(Calendar.DATE, 1);
		captureCal.set(Calendar.HOUR_OF_DAY, 14);
		captureCal.set(Calendar.MINUTE, 0);
		captureCal.set(Calendar.SECOND, 0);
		captureCal.set(Calendar.MILLISECOND, 0);
		Date allowedDate = captureCal.getTime();
		
		if(!new Date().before(allowedDate)){
			throw new ErpTransactionException("It is too late to Unconfirm this order");
		}
		
		if(captures != null && captures.size() > 0){
			
			utx = null;
			try{
	
				for (Iterator i = captures.iterator(); i.hasNext(); ) { 
					
					ErpCaptureModel capture = (ErpCaptureModel)i.next();
					
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();
		
					ErpSaleEB eb = erpSaleHome.findByPrimaryKey(new PrimaryKey(saleId));
					ErpSaleModel sale = (ErpSaleModel) eb.getModel();
					ErpPaymentMethodI paymentMethod = sale.getCurrentOrder().getPaymentMethod();
					
					ErpVoidCaptureModel voidCapture = null;
					
					Gateway gateway = GatewayFactory.getGateway(capture.getProfileID());
					voidCapture = gateway.voidCapture(paymentMethod, capture);
							
					voidCapture.setTransactionSource(EnumTransactionSource.TRANSPORTATION);
					eb.addVoidCapture(voidCapture);
					utx.commit();
				}

				LOGGER.info("Void Capture done. saleId="+saleId);
	
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
				String accountNumber = paymentMethod.getAccountNumber();
				combinedAuth.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
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
					eb.addSettlement(this.getEBTSettlement(capture));
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
			FDCouponManager.postConfirmPendingCouponTransactions(saleId);
			
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
	
	private  void lookupGatewayHome() {
		
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.gatewayHome = (PaymentGatewayHome) ctx.lookup("freshdirect.gateway.PaymentGateway");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}
}
