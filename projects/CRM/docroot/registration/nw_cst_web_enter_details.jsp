<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting" %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.survey.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.payment.EnumBankAccountType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.util.AccountUtil" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%//***********************************  Error Handling ******************************************%>
<%!

String[] checkGiftCardBuyerForm = {
		EnumUserInfoName.DLV_FIRST_NAME.getCode(), EnumUserInfoName.DLV_LAST_NAME.getCode(),
		EnumUserInfoName.DLV_HOME_PHONE.getCode(), EnumUserInfoName.DLV_WORK_PHONE.getCode(), 
		EnumUserInfoName.DLV_ALT_PHONE.getCode(), EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode(),
		EnumUserInfoName.EMAIL.getCode(), 
		EnumUserInfoName.REPEAT_EMAIL.getCode(), EnumUserInfoName.PASSWORD.getCode(), 
		EnumUserInfoName.REPEAT_PASSWORD.getCode(), EnumUserInfoName.PASSWORD_HINT.getCode(),
		EnumUserInfoName.CUSTOMER_AGREEMENT.getCode()};
						
%>
<% System.out.println("a"); %>

<%

 System.out.println("b");


System.out.println("c");
String fldTitle 			= request.getParameter("title");
String fldName 				= request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode());
String fldLastName 			= request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode());
String fldHomePhone 		= request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode());
String fldBusinessPhone 	= request.getParameter("busphone");
String fldBusinessPhoneExt 	= request.getParameter("busphoneext");
String fldCellPhone 		= request.getParameter("cellphone");
String fldCellPhoneExt 		= request.getParameter("cellphoneext");
String serviceType = request.getParameter("serviceType");

String fldEmail 			= request.getParameter(EnumUserInfoName.EMAIL.getCode());
String fldRepeatEmail 		= request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode());
String fldPassword          = request.getParameter(EnumUserInfoName.PASSWORD.getCode());
String fldRepeatPassword    = request.getParameter(EnumUserInfoName.REPEAT_PASSWORD.getCode());
String fldPasswordHint      = request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode());
String fldTermsAndConditions = request.getParameter(EnumUserInfoName.CUSTOMER_AGREEMENT.getCode());
String fldHowDidYouHear 	= request.getParameter("howDidYouHear");
if ("yes".equalsIgnoreCase(fldTermsAndConditions)) {
    fldTermsAndConditions = "checked";
} else {
    fldTermsAndConditions = "";
}
System.out.println("d");

 System.out.println("f");

%>


<% System.out.println("g"); %>


<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>New Customer > Enter Details</tmpl:put>
	<tmpl:put name='content' direct='true'>

	<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

	<fd:GiftCardBuyerController actionName='registerGiftCardBuyer' result="result" registrationType='10' successPage='/main/account_details.jsp'>
		<form name="registration" method="post" action="nw_cst_web_enter_details.jsp">
			<input type="hidden" name="terms" value="true" />
			<%@ include file="/registration/includes/i_header.jspf" %>
			
			<div class="content_scroll" style="padding: 0px; height: 85%;">
				<%@ include file="/registration/includes/i_name_and_contact_info.jspf" %><br />
				<%@ include file="/registration/includes/i_delivery_address_info.jspf" %><br />
				<%@ include file="/registration/includes/i_payment_info.jspf" %><br />
			</div>
			<% System.out.println("h"); %>
		</form>
	</fd:GiftCardBuyerController>

</tmpl:put>

</tmpl:insert>
