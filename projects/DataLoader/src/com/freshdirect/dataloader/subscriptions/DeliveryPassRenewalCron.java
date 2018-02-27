
package com.freshdirect.dataloader.subscriptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.configuration.RootConfiguration;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.ejb.DlvPassManagerHome;
import com.freshdirect.deliverypass.ejb.DlvPassManagerSB;
import com.freshdirect.ecomm.gateway.DlvPassManagerService;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.storeapi.configuration.StoreAPIConfig;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.smartstore.fdstore.CohortSelector;

public class DeliveryPassRenewalCron {

	private final static Category LOGGER = LoggerFactory.getInstance(DeliveryPassRenewalCron.class);

	private final static String CLASS_NAME=DeliveryPassRenewalCron.class.getSimpleName();

//	private static final ServiceLocator LOCATOR=new ServiceLocator();

	public static void main(String[] args) {


		Object[] autoRenewInfo=null;
		List arCustomers=new ArrayList(10);
		List arSKUs=new ArrayList(10);
		Context ctx=null;
		try {
			initializeSpringContext();
			ctx = getInitialContext();
			
			autoRenewInfo=getAutoRenewalInfo();
			if(autoRenewInfo!=null) {
				arCustomers=(List)autoRenewInfo[0];
				arSKUs=(List)autoRenewInfo[1];
				String erpCustomerID="";
				String arSKU="";
				LOGGER.info("DeliveryPassRenewalCron : "+arCustomers.size()+" customers eligible for auto-renewal.");
				for(int i=0;i<arCustomers.size();i++) {
					erpCustomerID=arCustomers.get(i).toString();
					arSKU="MKT0072630";
					if(arSKUs.get(i)!=null) {
						arSKU=arSKUs.get(i).toString();
					}
					try {
						placeOrder(erpCustomerID,arSKU);
					} catch (FDResourceException e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));	
						email(erpCustomerID,sw.toString());
					} catch (Exception e){
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));	
						email(erpCustomerID,sw.toString());
					}
				}
			}

		} catch (NamingException e) {
			LOGGER.error("Error running DeliveryPassRenewalCron :",e);
			email("ALL",e.toString());
		}  finally {
			emailPendingPassReport();
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				LOGGER.error(ne);
			}
		}
	}

	public static String placeOrder(FDActionInfo actionInfo, CustomerRatingAdaptor cra, String arSKU, ErpPaymentMethodI pymtMethod, ErpAddressModel dlvAddress, UserContext userCtx) {
		String orderID = null;
		FDCartModel cart=null;
		try {
			cart = getCart(arSKU,pymtMethod,dlvAddress,actionInfo.getIdentity().getErpCustomerPK(),userCtx);
			if(FDStoreProperties.getAvalaraTaxEnabled()){
			AvalaraContext context = new AvalaraContext(cart);
			context.setCommit(false);
			cart.getAvalaraTaxValue(context);
			actionInfo.setTaxationType(EnumNotificationType.AVALARA);
			}
			orderID = FDCustomerManager.placeSubscriptionOrder(actionInfo, cart, null, false, cra, null);
		}  catch (FDResourceException e) {
			LOGGER.warn(e);
			email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());
		} catch (ErpFraudException e) {
			LOGGER.warn(e);
			email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());
		} catch (DeliveryPassException e) {
			LOGGER.warn(e);
			email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());
		} catch (FDPaymentInadequateException e) {
			LOGGER.warn(e);
			email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());

		}
		return orderID;
	}

	private static ErpPaymentMethodI getMatchedPaymentMethod(ErpPaymentMethodI pymtMethod, Collection<ErpPaymentMethodI> pymtMethods) {
		if(pymtMethods==null ||pymtMethods.isEmpty())
			return null;
		Iterator<ErpPaymentMethodI> it=pymtMethods.iterator();
		ErpPaymentMethodI _pymtMethod=null;
		boolean exists=false;
		List<ErpPaymentMethodI> matchedPymtMethods=new ArrayList<ErpPaymentMethodI>(pymtMethods.size());
		while (it.hasNext()&& !exists) {
			_pymtMethod=(ErpPaymentMethodI)it.next();
			if( !StringUtil.isEmpty(pymtMethod.getProfileID()) && pymtMethod.getProfileID().equals(_pymtMethod.getProfileID()))	
			    {
				matchedPymtMethods.add(_pymtMethod);
				exists = true;
			} else if(pymtMethod.getCardType().equals(_pymtMethod.getCardType()) ) {
				
				if(!StringUtils.isEmpty(pymtMethod.getMaskedAccountNumber()) && !StringUtils.isEmpty(_pymtMethod.getMaskedAccountNumber())&& _pymtMethod.getMaskedAccountNumber().length()>=4) {
					if(pymtMethod.getMaskedAccountNumber().endsWith(_pymtMethod.getMaskedAccountNumber().substring(_pymtMethod.getMaskedAccountNumber().length()-4))) {
						matchedPymtMethods.add(_pymtMethod);
						exists = true;
					}
				}
			}
		}
		if(matchedPymtMethods.size()==0)
			return exists?_pymtMethod:null;	
		else {
			for (ErpPaymentMethodI temp : matchedPymtMethods) {
				if(temp.getCardType().equals(EnumCardType.PAYPAL) || temp.getCardType().equals(EnumCardType.ECP) || !isExpiredCC(temp)) {
					return temp;
				}
			}
			return null;
		}
	}
	private static String placeOrder(String erpCustomerID, String arSKU) throws FDResourceException {

		FDIdentity identity=null;
		FDActionInfo actionInfo=null;
		ErpPaymentMethodI pymtMethod=null;
		FDOrderI lastOrder=null;
		FDUser user=null;
		CustomerRatingAdaptor cra=null;
		lastOrder=getLastNonCOSOrder(erpCustomerID);
		String orderID="";
		if(lastOrder!=null) {
			try {
			identity=getFDIdentity(erpCustomerID);
				actionInfo=getFDActionInfo(identity);
				user=FDCustomerManager.getFDUser(identity);
				actionInfo.setIdentity(user.getIdentity());
			} catch (FDAuthenticationException ae) {
				LOGGER.warn("Unable to place deliveryPass autoRenewal order for customer :"+erpCustomerID);
				StringWriter sw = new StringWriter();
				ae.printStackTrace(new PrintWriter(sw));	
				email(erpCustomerID,sw.getBuffer().toString());
			}

			if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user)){
				if(null==user.getFDCustomer().getDefaultPaymentMethodPK() || null == user.getFDCustomer().getDefaultPaymentType() || 
						user.getFDCustomer().getDefaultPaymentType().getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName())){
					ErpPaymentMethodI defaultPmethod = PaymentMethodUtil.getSystemDefaultPaymentMethod(actionInfo, user.getPaymentMethods(), true);
					if(null != defaultPmethod){
						PaymentMethodUtil.updateDefaultPaymentMethod(actionInfo, user.getPaymentMethods(), defaultPmethod.getPK().getId(), EnumPaymentMethodDefaultType.DEFAULT_SYS, false);
						pymtMethod = defaultPmethod;
					}
				}
				else{
					pymtMethod = getPaymentMethod(user.getFDCustomer().getDefaultPaymentMethodPK(), user.getPaymentMethods()) ;
				}
			}else{
			pymtMethod=getMatchedPaymentMethod(lastOrder.getPaymentMethod(),getPaymentMethods(user.getIdentity()));
			}
			
			if(pymtMethod!=null) {
				if(!pymtMethod.getCardType().equals(EnumCardType.PAYPAL) && !pymtMethod.getCardType().equals(EnumCardType.ECP) && isExpiredCC(pymtMethod)) {
					LOGGER.warn("Autorenewal order payment method is expired for customer :"+erpCustomerID);
					createCase(erpCustomerID,CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING,DlvPassConstants.AUTORENEW_PYMT_METHOD_CC_EXPIRED);
					FDCustomerInfo customerInfo=FDCustomerManager.getCustomerInfo(identity);
					XMLEmailI email =FDEmailFactory.getInstance().createAutoRenewDPCCExpiredEmail(customerInfo);
					 		
					FDCustomerManager.sendEmail(email);
				} else {
					try {
						
						cra=new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
						orderID=placeOrder(actionInfo,cra,arSKU,pymtMethod,lastOrder.getDeliveryAddress(),user.getUserContext());

					}
					catch(FDResourceException fe) {
						LOGGER.warn("Unable to place deliveryPass autoRenewal order for customer :"+erpCustomerID);
						StringWriter sw = new StringWriter();
						fe.printStackTrace(new PrintWriter(sw));	
						email(erpCustomerID,sw.getBuffer().toString());
					}					
				}
				
			} else {
				LOGGER.warn("Unable to find payment method for autoRenewal order for customer :"+erpCustomerID);
				createCase(erpCustomerID,CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING,DlvPassConstants.AUTORENEW_PYMT_METHOD_UNKNOWN);
			}
		}
		return orderID;

	}



	public static FDCartModel getCart(String skuCode,ErpPaymentMethodI paymentMethod,ErpAddressModel deliveryAddress, String erpCustomerID,UserContext userCtx) {

		FDCartModel cart=null;
		try {
			cart=new FDCartModel();
			cart.addOrderLine(getCartLine(skuCode));
			cart.setPaymentMethod(paymentMethod);
			cart.setDeliveryAddress(deliveryAddress);
			cart.recalculateTaxAndBottleDeposit(cart.getDeliveryAddress().getZipCode());
			cart.handleDeliveryPass();
			FDReservation reservation=getFDReservation(erpCustomerID,deliveryAddress.getId());
			cart.setDeliveryReservation(reservation);
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));

	        cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(userCtx));
	        cart.setEStoreId(userCtx.getStoreContext().getEStoreId());
		} catch (FDSkuNotFoundException e) {
			LOGGER.warn("Unable to create shopping cart for customer :"+erpCustomerID);
			LOGGER.warn(e);
			cart=null;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
			email(erpCustomerID,sw.toString());
		} catch (FDResourceException e) {
			LOGGER.warn("Unable to create shopping cart for customer :"+erpCustomerID);
			LOGGER.warn(e);
			cart=null;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
			email(erpCustomerID,sw.toString());
		} catch (FDInvalidAddressException e) {
			LOGGER.warn("Unable to create shopping cart for customer :"+erpCustomerID);
			LOGGER.warn(e);
			cart=null;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
			email(erpCustomerID,sw.toString());
		}
		return cart;
	}

	private static FDCartLineI getCartLine(String skuCode) throws FDSkuNotFoundException, FDResourceException  {

		ProductModel prodNode = null;
		FDProduct product=null;
		FDSalesUnit salesUnit = null;
		HashMap<String, String> varMap = new HashMap<String, String>();
		double quantity = 1.0D;

		prodNode = ContentFactory.getInstance().getProduct(skuCode);
		product=FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
		salesUnit=product.getSalesUnits()[0];
		//TODO Need to pre-select pricing zone based on last order delivery type and zipcode.
		FDCartLineModel cartLine = new FDCartLineModel(new FDSku(product), prodNode
				, new FDConfiguration(quantity, salesUnit
				.getName(), varMap), null, ContentFactory.getInstance().getCurrentUserContext());

		try {
			cartLine.refreshConfiguration();
		} catch (FDInvalidConfigurationException e) {
			throw new FDResourceException(e);
		}
		return cartLine;
	}

	private static FDActionInfo getFDActionInfo(FDIdentity identity) {
		return new FDActionInfo(EnumTransactionSource.SYSTEM,identity,CLASS_NAME,"",null ,null);
	}

	private static FDIdentity getFDIdentity(String erpCustomerID) {
		return new FDIdentity(erpCustomerID,null);
	}


	private static synchronized Context getInitialContext() throws NamingException {

		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}


	private static FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, 
				customerID, addressID,false, null,20,null,false,null,null);
		return reservation;

	}

	private static FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub
		return null;
	}
	private static FDDeliveryZoneInfo getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		FDDeliveryZoneInfo zInfo =new FDDeliveryZoneInfo("1","1","1",EnumZipCheckResponses.DELIVER);
		return zInfo;
	}

	
	
	

	private static Object[] getAutoRenewalInfo() {

		try {
			return FDCustomerManager.getAutoRenewalInfo();
		}
		catch(FDResourceException fe) {
			LOGGER.error("Error running DeliveryPassRenewalCron :",fe);
			return null;
		}
		
		/* Object[] obj=new Object[2];
		ArrayList<String> customers=new ArrayList<String>(2);
		customers.add("10255972995");
		//customers.add("145636033");
		ArrayList<String> skus=new ArrayList<String>(2);
		skus.add("MKT0072733");
		//skus.add("MKT0072733");
		obj[0]=customers;
		obj[1]=skus;
		return obj;*/


	}

	private static FDOrderI getLastNonCOSOrder(String erpCustomerID) throws FDResourceException {

		try {
			return FDCustomerManager.getLastNonCOSOrder(erpCustomerID, EnumSaleType.REGULAR, EnumSaleStatus.SETTLED);
		} catch (FDResourceException e) {
			LOGGER.warn("Unable to find payment method for autoRenewal order for customer :"+erpCustomerID);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
			email(erpCustomerID,sw.toString());
			return null;
		}
		catch (ErpSaleNotFoundException e) {
			LOGGER.info("Unable to find a home/pickup order using credit card for customer: "+erpCustomerID);
			createCase(erpCustomerID,CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING,DlvPassConstants.AUTORENEW_PYMT_METHOD_UNKNOWN);
			return null;
		}
	}

	private static Collection<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException {

		try {
			return FDCustomerManager.getPaymentMethods(identity);
		} catch (FDResourceException e) {
			String customerID=identity.getErpCustomerPK();
			LOGGER.warn("Unable to find payment method for autoRenewal order for customer :"+customerID);
			createCase(customerID,CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING,DlvPassConstants.AUTORENEW_PYMT_METHOD_UNKNOWN);
			return null;
		}
	}

	private static CrmSystemCaseInfo buildCase(String customerPK, String saleId, CrmCaseSubject subject, String summary) {

		PrimaryKey salePK = saleId != null ? new PrimaryKey(saleId) : null;
		return new CrmSystemCaseInfo(new PrimaryKey(customerPK), salePK, subject, summary);
	}

	private static void createCase(String customerID, String subject, String summary) throws FDResourceException {

		CrmSystemCaseInfo caseInfo=buildCase( customerID, null,CrmCaseSubject.getEnum(subject),summary);
		FDCustomerManager.createCase(caseInfo);
		/*ErpCreateCaseCommand cmd = new ErpCreateCaseCommand(LOCATOR, caseInfo);
		cmd.setRequiresNewTx(true);
		cmd.execute();*/
	}

	public static void email(String customerID, String exceptionMsg) {

		try {
			Date now = DateUtil.truncate(new Date());
			String subject="Unable to autorenew deliverypass  for customer id :	"+customerID;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			StringBuffer buff = new StringBuffer();
			String br = "\n";
			buff.append(subject).append(" on ").append(dateFormatter.format(now)).append(br);
			buff.append(br);
			buff.append("Exception is :").append(br);
			buff.append(exceptionMsg);

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
							subject, buff.toString());

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending autorenewal exception email: ", e);
		}
	}

	private static boolean isExpiredCC(ErpPaymentMethodI paymentMethod) {
		if(paymentMethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime()))
			return true;
		return false;
		
	}
	
	private static void emailPendingPassReport()
	{
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, 1);
		Context ctx = null;
		List<List<String>> pendingPasses =null;
		try
		{
			
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.DlvPassManagerSB)){
				pendingPasses = DlvPassManagerService.getInstance().getPendingPasses();
			}else{
				ctx = getInitialContext();
				DlvPassManagerSB dlvPassManagerSB = null;
				DlvPassManagerHome dph =(DlvPassManagerHome) ctx.lookup("freshdirect.erp.DlvPassManager");
				dlvPassManagerSB = dph.create();
				pendingPasses =dlvPassManagerSB.getPendingPasses();
			}
			
			if(pendingPasses.size()>0)
				email( cal.getTime(),pendingPasses);
		}
		catch(NamingException e)
		{   
			email(cal.getTime(),e);
		}  catch (RemoteException e) {
			
			email(cal.getTime(),e);
		} catch (CreateException e) {
			
			email(cal.getTime(),e);
		}
		finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {

				// TODO Auto-generated catch block
				ne.printStackTrace();
			
			}
		}
	}
	
	private static void email(Date processDate, Exception e) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="Pending DeliveryPass Report	for "+ (processDate != null ? dateFormatter.format(Calendar.getInstance()) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			
				buff.append("<B> Error running report</B>");
				buff.append(e.toString());
			
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException _e) {
			LOGGER.warn("Error Sending DeliveryPass report email: ", _e);
		}
		
	}
	private static void email(Date processDate, List<List<String>> info) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="Pending DeliveryPass Report	for"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(info != null && info.size()>0) {
				buff.append(getDataAsString(info));
			} else if (info==null) {
				buff.append("<B> Error running report</B>");
			}
			else {
				buff.append("<B> Not data returned</B>");
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getDPReportMailFrom(),
					ErpServicesProperties.getDPReportMailTo(),ErpServicesProperties.getDPReportMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}
		
	}
	private static String getDataAsString(List<List<String>> customers) {
    	
    	StringBuffer buf=new StringBuffer(1000);
    	buf.append("<b> Total pending delivery-passes on past orders :"+customers.size()+"</b>");
    	buf.append("<br><br><table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
		buf.append("<tr>").append(buildSimpleTag("th","Customer Name"))
						  .append(buildSimpleTag("th","User Id"))
						  .append(buildSimpleTag("th","Order #"))
						  .append(buildSimpleTag("th","Order Type"))
						  .append(buildSimpleTag("th","Order Status"))
						  .append(buildSimpleTag("th","Delivery Date"))
						  .append("</tr>");
		List<String> customerInfo=null;
		for(Iterator<List<String>> i = customers.iterator(); i.hasNext();){
			customerInfo  =  i.next();
				buf.append("<tr>");
				for(Iterator<String> j = customerInfo.iterator(); j.hasNext();) {
					buf.append(buildSimpleTag("td",j.next()));
				}
				buf.append("</tr>");
		}
		buf.append("</table>");
		return buf.toString();
    }
   
	private static ErpPaymentMethodI getPaymentMethod(String paymentMethodPk, Collection<ErpPaymentMethodI> paymentMethods){
		for(Iterator<ErpPaymentMethodI> i=paymentMethods.iterator(); i.hasNext();){
			ErpPaymentMethodI pmethod = i.next();
			if(paymentMethodPk.equals(pmethod.getPK().getId())){
				return pmethod;
			}
		}
		return null;
	}
   
    private static String buildSimpleTag( String tagName,String input) {
    	return new StringBuilder().append("<")
    	                          .append(tagName)
    	                          .append(">").append(input)
    	                          .append("</")
    	                          .append(tagName)
    	                          .append(">").toString();
    }
    
    private static void initializeSpringContext() {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfiguration.class);
        rootContext.register(StoreAPIConfig.class);
        rootContext.refresh();
    }
}
