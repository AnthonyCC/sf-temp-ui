package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;

public class ExternalAccountService implements AccountService {

	private static Category LOGGER = LoggerFactory.getInstance(ExternalAccountService.class);
	
	private String signUpUnrecognized = "/social/signup_lite_social.jsp?is_forwarded=true";
	private String socialLoginMergePage ="/social/social_login_merge.jsp";
	private String socialCustomMessage ="/social/social_custom_message.jsp";
	String updatedSuccessPage ="/index.jsp";
	
    @Override
    public String login(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String userToken = (request.getParameter("userToken") == null) ? "" : (String) request.getParameter("userToken");
        String providerName = (request.getParameter("provider") == null) ? "" : (String) request.getParameter("provider");
        String userId = (request.getParameter("email") == null) ? "" : (String) request.getParameter("email");

        if (userId == null || userId.length() == 0) {
            // User email not found. User will not be able to create account with this social.
            // The Social Provider did not return user email or user email missing.
            return socialCustomMessage;
        }

        // call ejb to check if user token exist in the in fd social database.
        // if exists then return user_id otherwise return null;
        String userIdInDb = "";
        if (userId != null && userId.length() > 0) {
            userIdInDb = getUserIdForUserToken(userToken);
        }

        // The social login token exist in db. Take user to home page.
        // Login the user in the fd system with fd user and fd password

        if (userIdInDb != null && userIdInDb.length() > 0 && userId != null && userId.equalsIgnoreCase(userIdInDb)) {
            ActionResult actionResult = new ActionResult();
            String updatedSuccessPage = UserUtil.loginUser(session, request, response, actionResult, userId, null, "", this.updatedSuccessPage, true);

            if (updatedSuccessPage != null) {
                // redirect to successpage
                try {
                    response.sendRedirect("/social/success.jsp?successPage=" + updatedSuccessPage.substring(1, this.updatedSuccessPage.length()));
                    LOGGER.info("successPage:" + updatedSuccessPage.substring(1, this.updatedSuccessPage.length()));
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }

        } else {
            // check with the email id if an account with this email id exist in
            // our system
            // if exist then take user to merge page. in
            // that cause user has to log
            // in to our site with either fresh direct account or social login.
            // if no such account exist then take to sign up page.

            // If user already a customer of fresh direct.
            if (userId != null && isUserEmailAlreadyExist(userId)) {
                // Recognized User
                return socialLoginMergePage;

            } else {
                return signUpUnrecognized;
            }
        }
        return updatedSuccessPage;

    }
	
	@Override
	public String register(FDSessionUser user, PageContext pageContext,
			ActionResult actionResult, int registrationType) throws Exception {


		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
		HttpSession session = pageContext.getSession();
		String successPage = null;
		
		pageContext.getSession().removeAttribute("LITESIGNUP_COMPLETE");
		/*if(pageContext.getSession().getAttribute("REFERRALNAME") != null ) {
			if(!"done".equals(request.getParameter("submission"))) {
				actionResult.addError(new ActionError("Dummy","Dummy"));
				return true;
			}
		}*/
		RegistrationAction ra = new RegistrationAction(registrationType);

		HttpContext ctx =
			new HttpContext(
					session,
					request,
					response);

		ra.setHttpContext(ctx);
		ra.setResult(actionResult);
		/* APPDEV-1888 Refer a Friend */
		String result = ra.executeEx();
		if((Action.SUCCESS).equals(result)) {
			//if referral information is available, record it.
		
				if(FDStoreProperties.isExtoleRafEnabled() && pageContext.getSession().getAttribute("CLICKID") != null){
				try {
					//user = (FDSessionUser) session.getAttribute(USER);
					LOGGER.debug(user.getIdentity().getErpCustomerPK());
					LOGGER.debug(user.getUserId());
					String rafPromoCode=(String) pageContext.getSession().getAttribute("COUPONCODE");
					String rafClickId=(String)pageContext.getSession().getAttribute("CLICKID");
					LOGGER.debug(rafClickId);
					LOGGER.debug(rafPromoCode);
					LOGGER.debug("Adding referral record for CID:" + user.getIdentity().getErpCustomerPK() + "-email:" + user.getUserId() + "-reflink:" + (String) pageContext.getSession().getAttribute("CLICKID"));
					String customerId = user.getIdentity().getErpCustomerPK();
				//	String referralCustomerId = FDCustomerManager.recordReferral(customerId, (String) pageContext.getSession().getAttribute("CLICKID"), user.getRafClickId());
				//	user.setReferralCustomerId(referralCustomerId);
					user.setRafPromoCode(rafPromoCode);
					user.setRafClickId(rafClickId);
					user.setRafFriendSignedUp(true);
					session.setAttribute(SessionName.USER, user);
					//Record the referee signup in referral activitylog
					ErpActivityRecord rec = new ErpActivityRecord();
					rec.setActivityType(EnumAccountActivityType.REFEREE_SIGNEDUP);
					rec.setSource(EnumTransactionSource.WEBSITE);
					rec.setInitiator("CUSTOMER");
				//	rec.setCustomerId(referralCustomerId);
					rec.setDate(new Date());
					rec.setNote("<a href=\"/main/summary.jsp?erpCustId=" + customerId + "\">"+user.getUserId() + "</a> <a href=\"/main/summary.jsp?erpCustId=" + customerId + "\">ID #" + customerId + "</a>");
		//			new ErpLogActivityCommand(FDServiceLocator.getInstance(), rec).execute();
					successPage="/registration/invite_signup2.jsp";
					//this.setAjax(true);
				} catch (Exception e) {
					LOGGER.error("Exception when trying to update FDCustomer with referral ID",e);
				}
			} else {
				if("true".equals(pageContext.getRequest().getParameter("LITESIGNUP"))) {
					successPage = "/registration/signup_lite.jsp";
					//this.setAjax(true);
					pageContext.getSession().setAttribute("LITESIGNUP_COMPLETE", "true");
				}
			}

			user = (FDSessionUser) session.getAttribute(SessionName.USER);
			if (user != null) {
				user.setJustSignedUp(true);
			}
		}

	return successPage;
	
	}

	
	private void logActivity(ErpActivityRecord rec) {

		try {
			FDECommerceService.getInstance().logActivity(rec);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getUserIdForUserToken(String userToken) {

		try {
			return ExternalAccountManager.getUserIdForUserToken(userToken);
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
		
	}

	private boolean isUserEmailAlreadyExist(String email) {
		try {
			return ExternalAccountManager.isUserEmailAlreadyExist(email);
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		}

		return false;
	}
	
}
