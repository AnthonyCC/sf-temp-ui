package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import org.apache.log4j.Logger;

import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service.DeliveryPassService;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumTransactionSource;

public class DeliveryPassServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -6503107227290115125L;
	private static final Logger LOG = LoggerFactory.getInstance(DeliveryPassServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			DeliveryPassData responseData = DeliveryPassService.defaultService().loadDeliveryPasses(user);
			writeResponseData(response, responseData);
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to load delivery pass products", e);
		} catch (FDSkuNotFoundException e) {
			returnHttpError(500, "Delivery pass product SKU code not found.", e);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Terms & Conditions media for Delivery Pass.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Terms & Conditions HTML media for Delivery Pass.", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		Map<String, Object> responseData= new HashMap<String, Object>();
		try {
			if (null != user && null != user.getIdentity() && user.getLevel() == FDUserI.SIGNED_IN) {
				String actionName = request.getParameter("action");
				if (null != user.getDlvPassInfo() && (EnumDlvPassStatus.ACTIVE.equals(user.getDlvPassInfo().getStatus())
						|| EnumDlvPassStatus.PENDING.equals(user.getDlvPassInfo().getStatus())
						|| EnumDlvPassStatus.READY_TO_USE.equals(user.getDlvPassInfo().getStatus()))) {
					if (actionName.equalsIgnoreCase("FLIP_AUTORENEW_ON")
							|| actionName.equalsIgnoreCase("FLIP_AUTORENEW_OFF")) {
						boolean autoRenewDpON = actionName.equalsIgnoreCase("FLIP_AUTORENEW_ON") ? true : false;
						FDIdentity identity = user.getIdentity();
						String dpType = user.getDlvPassInfo().getAutoRenewDPType().getAutoRenewalSKU();
						if (null != dpType) {
							try {
								FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession(), "DeliveryPass auto-renew Opt-in");
								FDCustomerManager.updateDpOptinDetails(autoRenewDpON, identity.getFDCustomerPK(), dpType, info, info.geteStore());
								responseData.put("STATUS", "SUCCESS");
							} catch (FDResourceException e) {
								LOG.warn("Expection while opting-in for DP.", e);
								returnHttpError(500, "Failed to save Opt-in for DP");
							}
						}
					}
				}else { // DP is not active
					responseData.put("STATUS", "ERROR");
					responseData.put("MESSAGE", "You dont have a Active DeliveryPass.");
					responseData.put("ERRORTYPE", "ineligible");
				}
			} else {
				responseData.put("STATUS", "ERROR");
				responseData.put("MESSAGE", "Opt in changes are not committed, please refresh the page and do sign-in");
				if(null != user && null != user.getIdentity() &&  user.getLevel() != FDUserI.SIGNED_IN){
					responseData.put("ERRORTYPE", "User not logged in");
				} else{
					responseData.put("ERRORTYPE", "session timeout");
				}
				
			}
			writeResponseData(response, responseData);
		} catch (Exception e) {
			returnHttpError(500, "Failed to render Terms & Conditions HTML media for Delivery Pass.", e);
		}
	}

/*	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		Map<String, Object> responseData= new HashMap<String, Object>();
		try {
			if (null != user && null != user.getIdentity()) {
				boolean autoRenewDpON = Boolean.parseBoolean(request.getParameter("autoRenewDpON"));
				FDIdentity identity = user.getIdentity();
				if (null != user.getDlvPassInfo() && EnumDlvPassStatus.ACTIVE.equals(user.getDlvPassInfo().getStatus())) {
					String dpType = user.getDlvPassInfo().getAutoRenewDPType().getCode().toString();
					if (null != dpType) {
						try {
							FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession(), "DeliveryPass auto-renew Opt-in");
							FDCustomerManager.updateDpOptinDetails(autoRenewDpON, identity.getFDCustomerPK(), dpType, info, info.geteStore());
							responseData.put("STATUS", "SUCCESS");
						} catch (FDResourceException e) {
							LOG.warn("Expection while opting-in for DP.", e);
							returnHttpError(500, "Failed to save Opt-in for DP");
						}
					}
				}else { // DP is not active
					responseData.put("STATUS", "ERROR");
					responseData.put("MESSAGE", "You dont have a Active DeliveryPass.");
					responseData.put("ERRORTYPE", "ineligible");
				}
			} else {
				responseData.put("STATUS", "ERROR");
				responseData.put("MESSAGE", "Opt in changes are not committed, please refresh the page and do sign-in");
				responseData.put("ERRORTYPE", "session timeout");
			}
			writeResponseData(response, responseData);
		} catch (Exception e) {
			returnHttpError(500, "Failed to render Terms & Conditions HTML media for Delivery Pass.", e);
		}
	}*/
	
	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}
}
