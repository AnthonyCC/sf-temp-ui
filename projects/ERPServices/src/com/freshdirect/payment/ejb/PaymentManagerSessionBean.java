package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.customer.ejb.ErpCreateCaseCommand;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.customer.ejb.ErpSaleInfoDAO;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.AuthorizationCommand;
import com.freshdirect.payment.AuthorizationStrategy;
import com.freshdirect.payment.EnumPaymentMethodType;

public class PaymentManagerSessionBean extends SessionBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(PaymentManagerSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	private final static int AUTH_HOURS;
	
	static {
		int hours = 48;
		try {
			hours = Integer.parseInt(ErpServicesProperties.getAuthHours());
		} catch (NumberFormatException ne) {
			LOGGER.warn("authHours is not in correct format", ne);
		}
		AUTH_HOURS = hours;
	}
	

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.payment.ejb.PaymentManagerHome";
	}

	
	private boolean isAuthorizationRequired(Date deliveryTime){
		
		if (!"true".equalsIgnoreCase(ErpServicesProperties.getAuthorize())) {
			return false;
		}
		
		long currentTime = System.currentTimeMillis();
		long difference = deliveryTime.getTime() - currentTime;
		difference = difference / (1000 * 60 * 60);

		LOGGER.debug("difference :"+difference);
		
		if (difference > AUTH_HOURS) {
			return false;
		}

		return true;
	}
	
	public List authorizeSaleRealtime(String saleId) throws ErpAuthorizationException, ErpAddressVerificationException {
						
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpSaleModel sale = (ErpSaleModel) saleEB.getModel();
			ErpAbstractOrderModel order = sale.getCurrentOrder();
			AuthorizationStrategy strategy = new AuthorizationStrategy(sale);
			ErpPaymentMethodI payment=saleEB.getCurrentOrder().getPaymentMethod();			
			

			PrimaryKey erpCustomerPk=saleEB.getCustomerPk();
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(erpCustomerPk);
			List paymentList=customerEB.getPaymentMethods();
			  if(paymentList!=null && paymentList.size()>0){
				
				a:for(int j=0;j<paymentList.size();j++){
					ErpPaymentMethodI custPayment=(ErpPaymentMethodI)paymentList.get(j);
					if(payment.getAccountNumber().equalsIgnoreCase(custPayment.getAccountNumber())){
						payment=custPayment;
						break a;
					}
				}
			  }									
			
			  int orderCount=ErpSaleInfoDAO.getPreviousOrderHistory(getConnection(), sale.getCustomerPk().getId()); 
			  
			if(payment.isAvsCkeckFailed() && !payment.isBypassAVSCheck() && orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit()){
				throw new ErpAddressVerificationException("The address you entered does not match the information on file with your card provider, please contact a FreshDirect representative at 9999 for assistance.");				
			}

			
			if(!isAuthorizationRequired(order
					.getDeliveryInfo()
					.getDeliveryStartTime())){
				return Collections.EMPTY_LIST;
			}

						
			AuthorizationCommand cmd = new AuthorizationCommand(strategy.getOutstandingAuthorizations(), saleEB.getAuthorizations().size());									
			cmd.execute();						
			List auths = cmd.getAuthorizations();						
			
			
			for (Iterator i = auths.iterator(); i.hasNext();) {							
				ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();				
				if (auth.isApproved() && auth.hasAvsMatched()) {
					if(payment.isBypassAVSCheck()){						
							    payment.setAvsCkeckFailed(false);
								payment.setBypassAVSCheck(false);
								customerEB.updatePaymentMethodNewTx(payment);							  
					}
					saleEB.addAuthorization(auth);
				} else if(!auth.isApproved()){				
				    logAuthorizationActivity(saleEB.getCustomerPk().getId(), auth, false);
				    SessionContext ctx = getSessionContext();
				    ctx.setRollbackOnly();
				    throw new ErpAuthorizationException("There was a problem with the given payment method");
				  
			    }else{			    	
					if(auth.isApproved() && !auth.hasAvsMatched()){						
						if(!payment.isBypassAVSCheck()){														
							if(orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit()){	                           								  
									payment.setAvsCkeckFailed(true);
									customerEB.updatePaymentMethodNewTx(payment);								  	
									logAuthorizationActivity(saleEB.getCustomerPk().getId(), auth, true);							
									CrmSystemCaseInfo info =new CrmSystemCaseInfo(
											sale.getCustomerPk(),
										CrmCaseSubject.getEnum(CrmCaseSubject.CODE_AVS_FAILED),									
										"AVS Exception : Found Regular order for new customer with AVS Exception for saleId "+sale.getId()+ " and customer userId ="+customerEB.getUserId());
									ErpCreateCaseCommand caseCmd=new ErpCreateCaseCommand(LOCATOR, info);
									caseCmd.setRequiresNewTx(true);
									caseCmd.execute(); 
									SessionContext ctx = getSessionContext();
									ctx.setRollbackOnly();
									throw new ErpAddressVerificationException("The address you entered does not match the information on file with your card provider, please contact a FreshDirect representative at 9999 for assistance.");
									
							}
						}
							saleEB.addAuthorization(auth);
						
				     }
			      }					
			}

			return auths;
		}catch (SQLException e) {
			throw new EJBException(e);		
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (ErpTransactionException e) {
			throw new EJBException(e);
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	
	private ErpAuthorizationModel createAVSFailedAuthModel(ErpPaymentMethodI pm, double amount, double tax){
						
			ErpAuthorizationModel auth = new ErpAuthorizationModel();
			auth.setTransactionSource(EnumTransactionSource.SYSTEM);
			auth.setReturnCode(0);
			auth.setAuthCode("FD1000");
			auth.setResponseCode(EnumPaymentResponse.DECLINED);	
			auth.setDescription("AVS FAILED RESPONSE");
			auth.setSequenceNumber("FD AUTH");
			auth.setAvs("X");
			auth.setAmount(amount);
			auth.setTax(tax);
			auth.setCardType(pm.getCardType());
			String accountNumber = pm.getAccountNumber();
			if(accountNumber != null && accountNumber.length() >= 4){
				auth.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
			}
			return auth;				
	}
	
	
	
	public EnumPaymentResponse authorizeSale(String saleId) {

				
		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpSaleModel sale = (ErpSaleModel) saleEB.getModel();
			ErpAbstractOrderModel order = sale.getCurrentOrder();
			String customerId = sale.getCustomerPk().getId();
						
			ErpPaymentMethodI payment=saleEB.getCurrentOrder().getPaymentMethod();

			
			PrimaryKey erpCustomerPk=saleEB.getCustomerPk();
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(erpCustomerPk);
			List paymentList=customerEB.getPaymentMethods();
			  if(paymentList!=null && paymentList.size()>0){
				
				a:for(int j=0;j<paymentList.size();j++){
					ErpPaymentMethodI custPayment=(ErpPaymentMethodI)paymentList.get(j);
					if(payment.getAccountNumber().equalsIgnoreCase(custPayment.getAccountNumber())){
						payment=custPayment;
						break a;
					}
				}
			  }									

									
			if(!isAuthorizationRequired(order
					.getDeliveryInfo()
					.getDeliveryStartTime())){
				return EnumPaymentResponse.ERROR;
			}

			List auths=null;
			boolean isAVSFailureCase=false;
			
			
			  int orderCount=ErpSaleInfoDAO.getPreviousOrderHistory(getConnection(), sale.getCustomerPk().getId()); 
			  			
			
			if(payment.isAvsCkeckFailed() && !payment.isBypassAVSCheck()  && orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit()){
				//throw new ErpAddressVerificationException("The address you entered does not match the information on file with your card provider, please contact a FreshDirect representative at 9999 for assistance.");
				ErpAuthorizationModel model=createAVSFailedAuthModel(payment, order.getAmount(), 0);
				auths=new ArrayList();
				auths.add(model);
				isAVSFailureCase=true;
			}else{
             			
				AuthorizationStrategy strategy = new AuthorizationStrategy(sale);
				AuthorizationCommand cmd = new AuthorizationCommand(strategy.getOutstandingAuthorizations(), saleEB.getAuthorizations().size());
				cmd.execute();	
				auths = cmd.getAuthorizations();
			}
			
			EnumPaymentResponse response = EnumPaymentResponse.APPROVED;
			EnumSaleType saleType=sale.getType();
			if(EnumSaleType.SUBSCRIPTION.equals(saleType) && auths.size()==0) {
							/* SessionContext ctx = getSessionContext();
							   ctx.setRollbackOnly();
							*/
				return EnumPaymentResponse.ERROR;
			}

			boolean isAVSCaseCreated=false;
			for (Iterator i = auths.iterator(); i.hasNext();) {
				ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();					
				if (auth.isApproved() && auth.hasAvsMatched()) {
					if(payment.isBypassAVSCheck()){						
							    payment.setAvsCkeckFailed(false);
								payment.setBypassAVSCheck(false);
								customerEB.updatePaymentMethodNewTx(payment);							  
					}
					//saleEB.addAuthorization(auth);
				}
				
				if(auth.isApproved() && !auth.hasAvsMatched()){					
					if(!payment.isBypassAVSCheck()){
						int count=ErpSaleInfoDAO.getPreviousOrderHistory(getConnection(), sale.getCustomerPk().getId());
						if(orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit()){
							isAVSFailureCase=true;
							payment.setAvsCkeckFailed(true);
							customerEB.updatePaymentMethodNewTx(payment);							
							auth.setResponseCode(EnumPaymentResponse.DECLINED);
							if(!isAVSCaseCreated){ 
								isAVSCaseCreated=true;
								CrmSystemCaseInfo info =new CrmSystemCaseInfo(
										sale.getCustomerPk(),
									CrmCaseSubject.getEnum(CrmCaseSubject.CODE_AVS_FAILED),									
									"AVS Exception : Found Regular order for new customer with AVS Exception for saleId "+sale.getId()+ " and customer userId ="+customerEB.getUserId());
								ErpCreateCaseCommand caseCmd=new ErpCreateCaseCommand(LOCATOR, info);
								caseCmd.setRequiresNewTx(true);
								caseCmd.execute(); 
							}
							//SessionContext ctx = getSessionContext();
							//ctx.setRollbackOnly();
							//throw new ErpAddressVerificationException("The address you entered does not match the information on file with your card provider, please contact a FreshDirect representative at 9999 for assistance.");							
						}
					}	
			    }				
				
				saleEB.addAuthorization(auth);
				if (!auth.isApproved()) {
					response = auth.getResponseCode();
					logAuthorizationActivity(customerId, auth,isAVSFailureCase);
				}

				CrmSystemCaseInfo caseInfo = this.buildAuthorizationCase(customerId, saleId, auth);
				if (caseInfo != null && !isAVSFailureCase) {
					// create a case in seperate tx so even if case creation fails we
					// still have record of failed authorization
					this.createCase(caseInfo, true);
				}
				isAVSFailureCase=false;
				
			}
			return response;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (ErpTransactionException e) {
			throw new EJBException(e);
		} catch (FinderException e) {
			throw new EJBException(e);
		}
		catch (SQLException e) {
			throw new EJBException(e);		
		}
	}

	/**
	 * Adds auth failures to cusotmer activity log.
	 */
	private void logAuthorizationActivity(String customerPK, ErpAuthorizationModel auth, boolean isAVSFailure) {
		if (!auth.isApproved()) {
			ErpActivityRecord rec = new ErpActivityRecord();
			rec.setActivityType(EnumAccountActivityType.AUTHORIZATION_FAILED);
			rec.setSource(EnumTransactionSource.SYSTEM);
			rec.setInitiator("SYSTEM");
			rec.setCustomerId(customerPK);
			if(isAVSFailure){
			  rec.setNote("Authorization failure due to AVS failure - authorization result: " + auth.getProcResponseCode() + " AVS: " + auth.getAvs());
			}else{
			  rec.setNote("Authorization failure - authorization result: " + auth.getProcResponseCode() + " AVS: " + auth.getAvs());
			}
			new ErpLogActivityCommand(LOCATOR, rec, true).execute();
		}
	}

	private CrmSystemCaseInfo buildAuthorizationCase(String customerPK, String saleId, ErpAuthorizationModel auth) {
		CrmSystemCaseInfo caseInfo = null;
		if (!auth.isApproved()) {
			CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_AUTHORIZATION_FAILED);
			String summary = "Authorization failure - authorization result: "
				+ auth.getResponseCode().getName()
				+ " AVS: "
				+ auth.getAvs();
			PrimaryKey salePK = saleId != null ? new PrimaryKey(saleId) : null;
			caseInfo = new CrmSystemCaseInfo(new PrimaryKey(customerPK), salePK, subject, summary);
		}

		return caseInfo;
	}

	private void createCase(CrmSystemCaseInfo caseInfo, boolean requiresNewTx) {
		ErpCreateCaseCommand cmd = new ErpCreateCaseCommand(LOCATOR, caseInfo);
		cmd.setRequiresNewTx(requiresNewTx);
		cmd.execute();
	}

	public void voidCapturesNoTrans(String saleId) throws ErpTransactionException {
		List captures = null;
		Date captureDate = null;

		try {

			LOGGER.info("Void Capture - start. saleId=" + saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();

			if (!status.equals(EnumSaleStatus.PAYMENT_PENDING)) {
				throw new ErpTransactionException("Sale is not captured yet " + saleId);
			}

			captures = eb.getCaptures();
			captureDate = eb.getCaptureDate();

			Calendar captureCal = new GregorianCalendar();
			captureCal.setTime(captureDate);
			captureCal.add(Calendar.DATE, 1);
			captureCal.set(Calendar.HOUR_OF_DAY, 14);
			captureCal.set(Calendar.MINUTE, 0);
			captureCal.set(Calendar.SECOND, 0);
			captureCal.set(Calendar.MILLISECOND, 0);
			Date allowedDate = captureCal.getTime();

			if (!new Date().before(allowedDate)) {
				throw new ErpTransactionException("It is too late to Unconfirm this order");
			}

			if (captures != null && captures.size() > 0) {

				for (Iterator i = captures.iterator(); i.hasNext();) {

					ErpCaptureModel capture = (ErpCaptureModel) i.next();
					ErpVoidCaptureModel voidCapture = null;
					if (EnumPaymentMethodType.ECHECK.equals(capture.getPaymentMethodType())) {
						voidCapture = CPMServerGateway.voidECCapture(capture);
					} else {
						voidCapture = CPMServerGateway.voidCCCapture(capture); // default to credit card
					}
					voidCapture.setTransactionSource(EnumTransactionSource.TRANSPORTATION);
					eb.addVoidCapture(voidCapture);
				}

				eb.reverseChargePayment();
				LOGGER.info("Void Capture done. saleId=" + saleId);
			}

		} catch (ErpTransactionException te) {
			throw te;
		} catch (Exception e) {
			LOGGER.warn(e);
			throw new ErpTransactionException(e);
		}
	}

	public void captureAuthorizations(String saleId, List auths) throws ErpTransactionException {
		for (Iterator i = auths.iterator(); i.hasNext();) {
			ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();
			this.captureAuthorization(saleId, auth);
		}
	}

	private ErpCaptureModel captureAuthorization(String saleId, ErpAuthorizationModel auth) throws ErpTransactionException {
		try {

			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));

			ErpPaymentMethodI paymentMethod = saleEB.getCurrentOrder().getPaymentMethod();
			ErpCaptureModel capture = null;
			if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
				List captures = saleEB.getCaptures();
				int captureCount = (captures != null) ? captures.size() : 0;
				String orderNumber = saleId + "X" + captureCount;
				capture = CPMServerGateway.captureECAuthorization(auth, paymentMethod, auth.getAmount(), auth.getTax(), orderNumber);
			} else {
				capture = CPMServerGateway.captureCCAuthorization(auth, auth.getAmount(), auth.getTax());
			}
			saleEB.addCapture(capture);
			return capture;
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}

	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale", ErpSaleHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpCustomer", ErpCustomerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	
	public List authorizeSaleRealtime(String saleId, EnumSaleType saleType) throws ErpAuthorizationException, ErpAddressVerificationException {

		if(EnumSaleType.REGULAR.equals(saleType)) {
			return authorizeSaleRealtime(saleId);
		}

		try {
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpSaleModel sale = (ErpSaleModel) saleEB.getModel();
			ErpAbstractOrderModel order = sale.getCurrentOrder();
			ErpPaymentMethodI payment=saleEB.getCurrentOrder().getPaymentMethod();
			

			PrimaryKey erpCustomerPk=saleEB.getCustomerPk();
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(erpCustomerPk);
			List paymentList=customerEB.getPaymentMethods();
			  if(paymentList!=null && paymentList.size()>0){
				
				a:for(int j=0;j<paymentList.size();j++){
					ErpPaymentMethodI custPayment=(ErpPaymentMethodI)paymentList.get(j);
					if(payment.getAccountNumber().equalsIgnoreCase(custPayment.getAccountNumber())){
						payment=custPayment;
						break a;
					}
				}
			  }							
						  			  			 
  
			int orderCount=ErpSaleInfoDAO.getPreviousOrderHistory(getConnection(), sale.getCustomerPk().getId());  
			
			if(payment.isAvsCkeckFailed() && !payment.isBypassAVSCheck() && orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit()){
				throw new ErpAddressVerificationException("The address you entered does not match the information on file with your card provider, please contact a FreshDirect representative at 9999 for assistance.");
				
			}

			if(!isAuthorizationRequired( order.getRequestedDate())){
			    return Collections.EMPTY_LIST;	
			}

						
			AuthorizationStrategy strategy = new AuthorizationStrategy(sale);
			AuthorizationCommand cmd = null;
			cmd=new AuthorizationCommand(strategy.getOutstandingAuthorizations(), saleEB.getAuthorizations().size());
			cmd.execute();
			List auths = cmd.getAuthorizations();
			
			if(EnumSaleType.SUBSCRIPTION==saleType)
			{
				for (Iterator i = auths.iterator(); i.hasNext();) {
					ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();					
					if (auth.isApproved()) {
						saleEB.addAuthorization(auth);
					} else {
						logAuthorizationActivity(saleEB.getCustomerPk().getId(), auth, false);
						SessionContext ctx = getSessionContext();
						ctx.setRollbackOnly();
						throw new ErpAuthorizationException("There was a problem with the given payment method");
					}
				}
			}else if(EnumSaleType.GIFTCARD==saleType || EnumSaleType.DONATION==saleType){
				a:for (Iterator i = auths.iterator(); i.hasNext();) {					
					ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();					
					if (auth.isApproved() && auth.hasAvsMatched()) {						
						if(payment.isBypassAVSCheck()){						
						    payment.setAvsCkeckFailed(false);
							payment.setBypassAVSCheck(false);
							customerEB.updatePaymentMethodNewTx(payment);							  
						}						
						saleEB.addAuthorization(auth);
					} else {
						if(auth.isApproved() && !auth.hasAvsMatched()){																				
							
							
							if(orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit() && EnumSaleType.GIFTCARD==saleType && payment.isBypassAVSCheck()){
								saleEB.addAuthorization(auth);
								continue a;
							}
							else if(orderCount<ErpServicesProperties.getAvsErrorOrderCountLimit() && EnumSaleType.GIFTCARD==saleType && !payment.isBypassAVSCheck()){
								
								payment.setAvsCkeckFailed(true);
								customerEB.updatePaymentMethodNewTx(payment);									
								logAuthorizationActivity(saleEB.getCustomerPk().getId(), auth,true);
								
								CrmSystemCaseInfo info =new CrmSystemCaseInfo(
										sale.getCustomerPk(),
									CrmCaseSubject.getEnum(CrmCaseSubject.CODE_AVS_FAILED),									
									"AVS Exception : Found order for new customer with AVS Exception for saleId "+sale.getId()+ " and customer userId ="+customerEB.getUserId());
								ErpCreateCaseCommand caseCmd=new ErpCreateCaseCommand(LOCATOR, info);
								caseCmd.setRequiresNewTx(true);
								caseCmd.execute(); 
								SessionContext ctx = getSessionContext();
								ctx.setRollbackOnly();
								throw new ErpAddressVerificationException("The address you entered does not match the information on file with your card provider, please contact a FreshDirect representative at 9999 for assistance.");
								
							}else{
								saleEB.addAuthorization(auth);
								String saleTypeStr="Gift Card";
								if(EnumSaleType.DONATION==saleType)
								{
									saleTypeStr="Donation";
								}
								CrmSystemCaseInfo info =new CrmSystemCaseInfo(
												sale.getCustomerPk(),
											CrmCaseSubject.getEnum(CrmCaseSubject.CODE_AVS_FAILED),
											"Found "+saleTypeStr+" order with AVS Exception(old customer) for saleId "+sale.getId());								
									new ErpCreateCaseCommand(LOCATOR, info).execute();
								
                                continue a;
							}
						}	
						logAuthorizationActivity(saleEB.getCustomerPk().getId(), auth, false);
						SessionContext ctx = getSessionContext();
						ctx.setRollbackOnly();
						throw new ErpAuthorizationException("There was a problem with the given payment method");
					}
				}				
			}

			return auths;
		}
		catch (SQLException e) {
			throw new EJBException(e);
		}
		catch (RemoteException e) {
			throw new EJBException(e);
		} catch (ErpTransactionException e) {
			throw new EJBException(e);
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}


}