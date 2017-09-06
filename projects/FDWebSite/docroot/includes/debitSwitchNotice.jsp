<%
/* placeholder for debitSwitchNotice logic
 * this should be moved to APIs when there's more time and additional functionality
 *
 * there's a prop check (FDStoreProperties.isDebitSwitchNoticeEnabled), 
 * but it's used to wrap the js open call instead of the content call here.
 *
 * auto opening of overlay happens (debitSwitchNoticePopup.js) if there's 
 * no cookie set (FreshDirect.debitSwitchNotice)
 *
 * there's a third piece of text shown in checkout (faq link), added to the 
 * soy file (addpaymentmethodpopup.soy)
 */
%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature" %>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:CheckLoginStatus id="debitSwitchNotice_user" />
<%
	Collection debitSwitchNotice_paymentMethods = null;
	FDIdentity debitSwitchNotice_identity = null;
	if(debitSwitchNotice_user != null && debitSwitchNotice_user.getIdentity() != null) {
		debitSwitchNotice_identity = debitSwitchNotice_user.getIdentity();
		debitSwitchNotice_paymentMethods = FDCustomerManager.getPaymentMethods(debitSwitchNotice_identity);
	}
	boolean qualifiesForDebitSwitch = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, debitSwitchNotice_user) && 
			(debitSwitchNotice_paymentMethods != null && debitSwitchNotice_paymentMethods.size() > 1);
	String debitSwitchNotice_uri = request.getRequestURI();
	boolean onPaymentInfo = (debitSwitchNotice_uri.indexOf("payment_information.jsp") != -1) ? true : false;

	if (qualifiesForDebitSwitch) {
		if (onPaymentInfo) { /* payment info page */
			%><fd:IncludeMedia name="/media/editorial/site_pages/debitswitch/payment_information.ftl"></fd:IncludeMedia><%
		} else { /* overlay content */
			%><fd:IncludeMedia name="/media/editorial/site_pages/debitswitch/overlay.ftl"></fd:IncludeMedia><%
		}
	}
%>