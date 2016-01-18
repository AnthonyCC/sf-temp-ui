package com.freshdirect.webapp.ajax.expresscheckout.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SinglePageCheckoutPotatoTag extends SimpleTagSupport {

	private String name = "singlePageCheckoutPotato";
	private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	private static final String MP_EWALLET_CARD="MP_CARD";
	private static final String WALLET_SESSION_CARD_ID="WALLET_CARD_ID";
	private final String EWALLET_ERROR_CODE = "WALLET_ERROR";
	
	
	
/*	Constants used for EWallet Express Checkout
 * private final static String EWALLET_REQ_ATTR_PAIRING_END = "Pairing_End";
	private final static String EWALLET_REQ_ATTR_ACTION_EXP_CHECKOUT = "MP_Express_Checkout";
	private final static String EWALLET_REQ_ATTR_INVALID_PAYMENTMETHOD = "invalidPaymentMethod";
	private final static String EWALLET_BOOLEAN_TRUE = "true";
	private final static String EWALLET_REQ_ATTR_PAYMENT_DATA = "paymentData";
	private static final String MASTERPASS_WALLET_TYPE_NAME="MP";
	private final static String EWALLET_REQ_ATTR_WALLET_TYPE_NAME = "ewalletType";
	private final static String EWALLET_REQ_ATTR_ACTION = "action";
	private final static String EWALLET_REQ_ATTR_MP_ALL_PAYMENT = "MP_All_PayMethod_In_Ewallet";
	private final static String EWALLET_SESSION_ATTR_PAYMENTDATA = "mpEwalletPaymentData";*/

	private static final String MP_REQ_ATTR_ACTION_COMPLETED_VALUE="Standard_Checkout";
	private final static String EWALLET_REQ_ATTR_ACTION_COMPLETED = "actionCompleted";
	
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		try {
			SinglePageCheckoutData result = SinglePageCheckoutFacade.defaultFacade().load(user, request);
			CheckoutService.defaultService().applyAtpCheck(user);
			// Check whether EWallet Card used for order 
			checkEWalletCard(result.getPayment(),request);
			removeOlderEwalletPaymentMethod(result.getPayment(),request);
			
			// EWallet ExpressCheckout Code Starts
			/* 
			ErpCustEWalletModel erpCustEWalletModel = FDCustomerManager.findLongAccessTokenByCustID(user.getFDCustomer().getErpCustomerPK(),MASTERPASS_WALLET_TYPE_NAME);	
			List<PaymentData> walletCards = new ArrayList<PaymentData>();
			if (erpCustEWalletModel != null	&& erpCustEWalletModel.getLongAccessToken() != null
					&& !erpCustEWalletModel.getLongAccessToken().isEmpty()) {
				try {
						request.setAttribute(EWALLET_REQ_ATTR_ACTION, EWALLET_REQ_ATTR_MP_ALL_PAYMENT);
						request.setAttribute(EWALLET_REQ_ATTR_WALLET_TYPE_NAME,MASTERPASS_WALLET_TYPE_NAME);
						request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request, context.getResponse());
						if (request.getSession().getAttribute(EWALLET_SESSION_ATTR_PAYMENTDATA) != null){
						walletCards = (List<PaymentData>) request.getSession().getAttribute(EWALLET_SESSION_ATTR_PAYMENTDATA);
						}
//						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// If EWallet Card used for order 
			checkEWalletCard(result.getPayment(),request);
			
			if (result.getPayment() != null) {
				List<PaymentData> payments = result.getPayment().getPayments();
				PaymentData mpPaymentMethod = null;
				for (PaymentData data : payments) {
					String actionCompleted = "";
					if(request.getAttribute(EWALLET_REQ_ATTR_ACTION_COMPLETED) !=null ){
						actionCompleted =request.getAttribute(EWALLET_REQ_ATTR_ACTION_COMPLETED).toString();
					}
					if( actionCompleted!= null && actionCompleted.equalsIgnoreCase(EWALLET_REQ_ATTR_PAIRING_END) || actionCompleted.equalsIgnoreCase(MP_REQ_ATTR_ACTION_COMPLETED_VALUE) ){
						if(data.geteWalletID()!=null && data.geteWalletID().equals("1")){
							data.setSelected(true);
							result.getPayment().setSelected(data.getId());
						}else{
							data.setSelected(false);
						}
					}else{
						if (data.isSelected() && data.geteWalletID()!=null && data.geteWalletID().equals("1")) {
							request.setAttribute(EWALLET_REQ_ATTR_PAYMENT_DATA, data);
							try {
								if( actionCompleted!= null && !actionCompleted.equalsIgnoreCase(EWALLET_REQ_ATTR_PAIRING_END)){
									request.setAttribute(EWALLET_REQ_ATTR_ACTION, EWALLET_REQ_ATTR_ACTION_EXP_CHECKOUT);
									request.setAttribute(EWALLET_REQ_ATTR_WALLET_TYPE_NAME,MASTERPASS_WALLET_TYPE_NAME);
									request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request, context.getResponse());
									if(request.getAttribute(EWALLET_REQ_ATTR_INVALID_PAYMENTMETHOD)!=null){
										String paymentMethod = (String)request.getAttribute(EWALLET_REQ_ATTR_INVALID_PAYMENTMETHOD);
										if(paymentMethod.equalsIgnoreCase(EWALLET_BOOLEAN_TRUE)){
											HttpServletResponse  response = (HttpServletResponse) context.getResponse();
											response.sendRedirect("/expressco/checkout.jsp");
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if(data.geteWalletID()!=null && data.geteWalletID().equals("1")){
						mpPaymentMethod = new PaymentData();
						mpPaymentMethod = data;
					}
				}
				// Add and Display EWallet Cards along with FD stored Payment Methods 
				if (erpCustEWalletModel != null	&& erpCustEWalletModel.getLongAccessToken() != null
						&& !erpCustEWalletModel.getLongAccessToken().isEmpty()) {
					if(mpPaymentMethod !=null)
    					addPaymentMethods(walletCards, mpPaymentMethod, payments);
    				else
    					payments.addAll(walletCards);
				}
			}
			*/
			
			Map<String, ?> potato = SoyTemplateEngine.convertToMap(result);
			context.setAttribute(name, potato);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (TemplateException e) {
			throw new JspException(e);
		} catch (RedirectToPage e) {
			throw new JspException(e);
		}
	}

	/**
	 * @param formpaymentData
	 * @param request
	 */
	private void removeOlderEwalletPaymentMethod(FormPaymentData formpaymentData,HttpServletRequest request){
		if (formpaymentData != null && formpaymentData.getPayments()!=null) {
			List<PaymentData> payments = formpaymentData.getPayments();
			List<PaymentData> paymentsNew = new ArrayList<PaymentData>();
			boolean selectedMacted = false;
			String selectedWalletCardId="";
			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
			}
			for (PaymentData data : payments) {
				if(data.geteWalletID() == null){
					if(formpaymentData.getSelected() != null && formpaymentData.getSelected().equals(data.getId())){
						selectedMacted = true;
					}
					paymentsNew.add(data);
				}else{
					int ewalletId = EnumEwalletType.getEnum("MP").getValue();
					if(data.geteWalletID()!=null && data.geteWalletID().equals(""+ewalletId) && selectedWalletCardId.equals(data.getId())){
						paymentsNew.add(data);
						selectedMacted = true;
					}
				}
			}
			if(paymentsNew.isEmpty() || !selectedMacted){
				formpaymentData.setSelected("");
			}
			formpaymentData.setPayments(paymentsNew);
			
		}
	}
	/**
	 * @param formpaymentData
	 * @param request
	 */
	private void checkEWalletCard(FormPaymentData formpaymentData,HttpServletRequest request){
		if (formpaymentData != null) {
			if(request.getSession().getAttribute(EWALLET_ERROR_CODE) != null ){
				formpaymentData.setWalletErrorMsg(request.getSession().getAttribute(EWALLET_ERROR_CODE).toString());
				request.getSession().removeAttribute(EWALLET_ERROR_CODE);
			}
			List<PaymentData> payments = formpaymentData.getPayments();
			String session_card = "";
			String actionCompleted = "";
			if(request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME) != null){
				session_card = request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME).toString();
			}
			String selectedWalletCardId="";
			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
			}
			if(request.getAttribute(EWALLET_REQ_ATTR_ACTION_COMPLETED) !=null ){
				actionCompleted =request.getAttribute(EWALLET_REQ_ATTR_ACTION_COMPLETED).toString();
			}
			if( (actionCompleted != null && actionCompleted.equalsIgnoreCase(MP_REQ_ATTR_ACTION_COMPLETED_VALUE)) 
					|| (session_card != null && session_card.equals(MP_EWALLET_CARD)) ){
				for (PaymentData data : payments) {
					int ewalletId = EnumEwalletType.getEnum("MP").getValue();
					if(data.geteWalletID() != null && data.geteWalletID().equals(""+ewalletId) && selectedWalletCardId.equals(data.getId())){
						data.setSelected(true);
						formpaymentData.setSelected(data.getId());
						data.setMpLogoURL(FDStoreProperties.getMasterpassLogoURL());
						request.getSession().setAttribute(EWALLET_SESSION_ATTRIBUTE_NAME, MP_EWALLET_CARD);
					}else{
						data.setSelected(false);
					}
				}
			}
		}
	}
	/**
	 * Related to EWallet Express Checkout Functionality
	 * Method is used to remove the duplicate card display on UI
	 * @param walletCards
	 * @param mpPaymentMethod
	 * @param fdPayments
	 */
	/*private void addPaymentMethods(List<PaymentData> walletCards,PaymentData mpPaymentMethod,List<PaymentData> fdPayments ){
	
		for(PaymentData mpdata : walletCards){
			if( !( mpdata.getAccountNumber() != null && mpdata.getAccountNumber().equalsIgnoreCase(mpPaymentMethod.getAccountNumber()) && 
				mpdata.getExpiration() != null && mpdata.getExpiration().equalsIgnoreCase(mpPaymentMethod.getExpiration()) &&
				mpdata.getBankAccountType() != null &&  mpdata.getBankAccountType().equalsIgnoreCase(mpPaymentMethod.getType()) &&
				mpdata.getNameOnCard() != null && mpdata.getNameOnCard().equalsIgnoreCase(mpPaymentMethod.getNameOnCard()))
					) {
				fdPayments.add(mpdata);
			}
		}
	}*/
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
