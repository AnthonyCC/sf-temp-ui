package com.freshdirect.webapp.ajax.standingorder;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import com.freshdirect.fdstore.EnumEStoreId;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.common.customer.EnumStandingOrderActiveType;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAdapter;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.data.SuccessPageData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class ManageStandingOrderServlet extends HttpServlet {

	private static final long serialVersionUID = -3650318272577031376L;
	private String spName = "singlePageCheckoutPotato";
	private static final Logger LOG = LoggerFactory.getInstance(ManageStandingOrderServlet.class);
	public static final String INITIATOR_NAME = "CUSTOMER";
	
	public static final String CANCEL_ALL_DELIVERIES="Cancel all deliveries";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String soId = request.getParameter("soId");
		if(null !=soId && !"".equals(soId)){
			try {
				FDStandingOrder so = FDStandingOrdersManager.getInstance().load(
						new PrimaryKey(soId));
				FDSessionUser u = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
				Map<String, Object> returnSO = StandingOrderHelper.convertStandingOrderToSoy(true,so,false, u);

				if (u != null) {
					StandingOrderHelper.populateCurrentDeliveryDate(u, returnSO);
					returnSO.put("modifyingOrderId", FDUserUtil.getModifyingOrderId(u));
					
				}
				writeResponseData(response, returnSO);
				
			} catch (FDResourceException e) {
				LOG.error("Unable to fetch SO with Id:"+ soId, e);
				throw new ServletException(e);
			} catch(PricingException pe){
				LOG.error("Unable to fetch SO with Id:"+ soId, pe);
				throw new ServletException(pe);
			} catch(FDInvalidConfigurationException pe){
				LOG.error("Unable to fetch SO with Id:"+ soId, pe);
				throw new ServletException(pe);
			} catch (HttpErrorResponse e) {
				LOG.error("Unable to fetch SO with Id:"+ soId, e);
				throw new ServletException(e);
			} 
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			String action = request.getParameter("action");
			String soId = request.getParameter("soId");
			String soName = request.getParameter("soName");
			String freq=request.getParameter("frequency");
			String deleteDate=request.getParameter("deleteDate");
			JspFactory factory = JspFactory.getDefaultFactory();
			PageContext pageContext = factory.getPageContext(this, request, response, null, true, JspWriter.DEFAULT_BUFFER, true);
			FDSessionUser u = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
            String errorMessage=null;
	        if(u!=null){
	            	
				if ("settings".equalsIgnoreCase(action) ) {
					
					u.setCurrentStandingOrder(null);
					u.setSoTemplateCart(new FDCartModel());
					if (soId != null && !"".equals(soId)) {

	
						FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
						if(null != so.getAddressId() && null != so.getStartTime()){											//COS17-56   //APPDEV-7816
							FDDeliveryZoneInfo zoneInfo =StandingOrderHelper.getZoneInfoFromLogistics(so);
							so.setZone(zoneInfo.getZoneCode());
						}
						u.setCurrentStandingOrder(so);
						so.setNewSo(true);
						if(!so.getStandingOrderCart().getOrderLines().isEmpty()){
							so.getStandingOrderCart().refreshAll(true);
						}
						
						u.setSoTemplateCart(so.getStandingOrderCart());
						u.setCheckoutMode(EnumCheckoutMode.CREATE_SO);
						
						SinglePageCheckoutData result = SinglePageCheckoutFacade.defaultFacade().load(u, request);
						Map<String, ?> potato = SoyTemplateEngine.convertToMap(result);
						
						if(null != so.getDeliveryAddress()){
							ErpAddressModel erpAddressModel=so.getDeliveryAddress();
							u.getSoTemplateCart().setDeliveryAddress(erpAddressModel);
							u.getSoTemplateCart().recalculateTaxAndBottleDeposit(erpAddressModel.getZipCode());
							u.getSoTemplateCart().updateSurcharges(new FDRulesContextImpl(u));
						}
						writeResponseData( response, potato );
						 
						pageContext.setAttribute(spName, potato);
	
					}
				} else if ("delete".equalsIgnoreCase(action)) {
					if (soId != null && !"".equals(soId)) {
						FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
						if (!so.isDeleted()) {
							u.setRefreshSO3(true);
							FDActionInfo info = AccountActivityUtil.getActionInfo(pageContext.getSession());
							if ("Y".equalsIgnoreCase(so.getActivate()) && !"null".equalsIgnoreCase(deleteDate) && !"".equalsIgnoreCase(deleteDate)) {
								if (!CANCEL_ALL_DELIVERIES.equalsIgnoreCase(deleteDate))
									FDStandingOrdersManager.getInstance().deleteActivatedSO(info, so,deleteDate);
								else if(CANCEL_ALL_DELIVERIES.equalsIgnoreCase(deleteDate)){
									cancelNextDelivery(so, u);
								}
							} else {
								FDStandingOrdersManager.getInstance().delete(info, so);
							}
						}

					}
				} else if("onloadNewStandingOrder".equalsIgnoreCase(action)){
					u.setRefreshSO3(true);
					if (("".equalsIgnoreCase(soId) || soId==null) && (freq == null || "".equalsIgnoreCase(freq)))
				      u.getCurrentStandingOrder().setFrequency(Integer.parseInt("1"));
					
					errorMessage= onloadNewStandingOrder(soName,u,pageContext);
					writeResponseData( response, errorMessage );
	
				} else if("create".equalsIgnoreCase(action)){
					u.setRefreshSO3(true);
					errorMessage=createStandingOrder(soName,u,pageContext);
					if(null == errorMessage){
						SuccessPageData successData=new SuccessPageData();
						successData.setSoId(u.getCurrentStandingOrder().getId());
						writeResponseData( response, successData );
					}
					else{
						writeResponseData( response, errorMessage );
					}
	
				} else if("selectFreq".equalsIgnoreCase(action) || "selectFreq2".equalsIgnoreCase(action)){
					 if(freq!=null){
						 u.setRefreshSO3(true);
						 u.getCurrentStandingOrder();
						    u.getCurrentStandingOrder().setNewSo(true);
						    u.getCurrentStandingOrder().setFrequency(Integer.parseInt(freq));
					 }
					 if("selectFreq2".equalsIgnoreCase(action)){
						SinglePageCheckoutData result = SinglePageCheckoutFacade.defaultFacade().load(u, request);
						Map<String, ?> potato = SoyTemplateEngine.convertToMap(result);
							
						writeResponseData( response, potato );
					 }else{
						 SuccessPageData successData=new SuccessPageData();
						 successData.setSoId(u.getCurrentStandingOrder().getId());
						 writeResponseData( response, successData );

					 }

	
				} else if("activate".equalsIgnoreCase(action)) {
					    boolean flg=acitivateSO(u);
					    u.setRefreshSO3(true);
					    LOG.info("Standing Order activated " + u.getCurrentStandingOrder().getId() + " " + flg) ;
				} else if("changename".equalsIgnoreCase(action)){
					// Need to update name by ID
					errorMessage = validateStandingOrderName(soName, u);
					if(null ==errorMessage){
						FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
						if(null !=so){
						FDListManager.renameShoppingList(so.getCustomerListId(), soName);
						u.getCurrentStandingOrder().setCustomerListName(soName);
						u.setRefreshSO3(true);
						}else{
							errorMessage = "Standing order is not exist !";
						}
					}
					writeResponseData( response, errorMessage );

				} else if ("setOpenStatus".equalsIgnoreCase(action)) {
					boolean openStatusTo = Boolean.parseBoolean(request.getParameter("setOpenStatusTo"));
					((FDSessionUser)u).setSoContainerOpen(openStatusTo);
					//no response
				}
			}else{
				//throw new ServletException("Reload the page");
				response.sendRedirect(response.encodeRedirectURL("/quickshop/standing_orders.jsp")) ;
		   }
		} catch (FDResourceException e) {
			throw new ServletException(e);
		} catch (TemplateException e) {
			throw new ServletException(e);
		} catch (RedirectToPage e) {
			throw new ServletException(e);
		} catch (JspException e) {
			throw new ServletException(e);
		} catch (HttpErrorResponse e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		} catch (FDInvalidConfigurationException e) {
			LOG.error("error while activating the standing order", e);
			throw new ServletException(e);

		} 
	}

	private boolean acitivateSO(FDUserI u) throws FDResourceException, FDInvalidConfigurationException {
		
		FDStandingOrder so=u.getCurrentStandingOrder();
		// Set the payment 
		u.getSoTemplateCart().setPaymentMethod(u.getCurrentStandingOrder().getPaymentMethod());
		
		// Set the Address 
		
		u.getSoTemplateCart().setDeliveryAddress(u.getCurrentStandingOrder().getDeliveryAddress());
		
		
		FDCartModel cart=  u.getSoTemplateCart();
		
		cart.setEStoreId(EnumEStoreId.FD);
		cart.getAvalaraTaxValue(new AvalaraContext(cart));
		
		FDStandingOrdersManager.getInstance().activateStandingOrder(u.getCurrentStandingOrder());
		
		// Sending confirmation Email 
		if(so.getTipAmount()>0.0){
			cart.setChargeAmount(EnumChargeType.TIP, so.getTipAmount());
		}

		u.setSoTemplateCart(cart);
		
		FDOrderI order=new FDStandingOrderAdapter(cart,so);
		
		FDCustomerManager.sendEmail(FDEmailFactory.getInstance().createSOActivateConfirmation(so.getUserInfo(), order, so));
		
		u.getCurrentStandingOrder().setActivate("Y");
		
		return true;
	}



	protected String createStandingOrder(String soName, FDSessionUser u, PageContext pageContext) throws JspException {
		soName = soName != null ? soName.trim() : "";
		String returnMessage = null;
		try {
			returnMessage = validateStandingOrderName(soName, u);
			if(null == returnMessage){
				FDStandingOrder so = null!=u.getCurrentStandingOrder()? u.getCurrentStandingOrder():new FDStandingOrder();
				so.setActivate(EnumStandingOrderActiveType.getEnum(1).getName());
				so.setCustomerListName(soName);
				so.setCustomerId(u.getIdentity().getErpCustomerPK());
				so.setNewSo(true);
				StandingOrderUtil.createStandingOrder(pageContext.getSession(), u.getSoTemplateCart(), so, null);
				u.setCurrentStandingOrder(so);
				u.setCheckoutMode( EnumCheckoutMode.CREATE_SO );
				returnMessage = so.getId();
			}
		} catch (FDResourceException e2) {
			throw new JspException(e2);
		}
		return returnMessage;
	}

	protected String onloadNewStandingOrder(String soName, FDSessionUser u, PageContext pageContext) throws JspException {
		soName = soName != null ? soName.trim() : "";
		String returnMessage = null;
		try {
			returnMessage = validateStandingOrderName(soName, u);
				FDStandingOrder so = null!=u.getCurrentStandingOrder()? u.getCurrentStandingOrder():new FDStandingOrder();
				so.setActivate(EnumStandingOrderActiveType.getEnum(1).getName());
				so.setCustomerListName(soName);
				so.setCustomerId(u.getIdentity().getErpCustomerPK());
				so.setNewSo(true);
				try {
					so.setPaymentMethodId(FDCustomerManager.getDefaultPaymentMethodPK(u.getIdentity()));
				} catch (FDResourceException e1) {
					LOG.error("SO:Unable to set PaymentMethodId:"+ e1);
				}
				u.setCurrentStandingOrder(so);
				u.setCheckoutMode( EnumCheckoutMode.CREATE_SO );
				returnMessage = so.getId();
		} catch (FDResourceException e2) {
			throw new JspException(e2);
		}
		return returnMessage;
	}
	
	/**
	 * @param soName
	 * @param u
	 * @param returnMessage
	 * @return
	 * @throws FDResourceException
	 */
	private String validateStandingOrderName(String soName, FDSessionUser u)
			throws FDResourceException {
		String returnMessage = null;
		if ("".equalsIgnoreCase(soName)) {
			returnMessage = "Please specify name for standing order!";
		} else if (soName.length() > FDCustomerCreatedList.MAX_NAME_LENGTH) {
			returnMessage ="List name too long (max " + FDCustomerCreatedList.MAX_NAME_LENGTH + " characters)!";
		} else if (FDListManager.isCustomerList(u, EnumCustomerListType.SO, soName)) {
			returnMessage = "The name '" + soName + "' is already in use. Please specify another name.";
		}
		return returnMessage;
	}

	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}

	protected boolean synchronizeOnUser() {
		return false;
	}

	public final static void configureJsonResponse(HttpServletResponse response) {
		// Set common response properties for JSON response
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/json");
		response.setLocale(Locale.US);
		response.setCharacterEncoding("ISO-8859-1");
	}

	protected final static <T> void writeResponseData(HttpServletResponse response, T responseData) throws HttpErrorResponse {

		// Set response parameters
		configureJsonResponse(response);

		// Serialize data to JSON and write out the result
		try {

			Writer writer = new StringWriter();
			new ObjectMapper().writeValue(writer, responseData);

			ServletOutputStream out = response.getOutputStream();
			String responseStr = writer.toString();

			// LOG.debug( "Generated response data: " + responseStr );

			out.print(responseStr);

			out.flush();

		} catch (JsonGenerationException e) {
			returnHttpError(500, "Error writing JSON response", e); // 500
																	// Internal
																	// Server
																	// Error
		} catch (JsonMappingException e) {
			returnHttpError(500, "Error writing JSON response", e); // 500
																	// Internal
																	// Server
																	// Error
		} catch (IOException e) {
			returnHttpError(500, "Error writing JSON response", e); // 500
																	// Internal
																	// Server
																	// Error
		} catch (Exception e) {
			returnHttpError(500, "Error writing JSON response", e); // 500
																	// Internal
																	// Server
																	// Error
		}
	}

	public final static void returnHttpError(int errorCode) throws HttpErrorResponse {
		LOG.error("Aborting with HTTP" + errorCode);
		throw new HttpErrorResponse(errorCode);
	}

	public final static void returnHttpError(int errorCode, String errorMessage) throws HttpErrorResponse {
		LOG.error(errorMessage);
		throw new HttpErrorResponse(errorCode);
	}

	public final static void returnHttpError(int errorCode, String errorMessage, Throwable e) throws HttpErrorResponse {
		LOG.error(errorMessage, e);
		throw new HttpErrorResponse(errorCode);
	}

	public final static class HttpErrorResponse extends Exception {

		private static final long serialVersionUID = -4320607318778165536L;

		public HttpErrorResponse(int errorCode) {
			this.errorCode = errorCode;
		}

		private int errorCode;

		public int getErrorCode() {
			return errorCode;
		}
	}
	
	public static void cancelNextDelivery(FDStandingOrder so, FDUserI u)  {
		try {
			StandingOrderHelper.setUpcomingStandingOrder(so, u);
			FDActionInfo info = new FDActionInfo(
					EnumTransactionSource.WEBSITE,
					so.getCustomerIdentity(), INITIATOR_NAME,
					"Cancel the standing order based on template criteria ",
					null, null);
			if (so.getUpcomingDelivery() != null && so.getUpcomingDelivery().getErpSalesId() != null) {
				FDCustomerManager.cancelOrder(info, so.getUpcomingDelivery().getErpSalesId(), true, 0, false);
			}
			FDStandingOrdersManager.getInstance().delete(info, so);
		} catch (Exception e) {
			LOG.error("Got the exception while cancelling the next delivery of SO template:"+so.getId(), e);
		}
	}
}
