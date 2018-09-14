<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />
<% //expanded page dimensions
final int W_YA_ADD_CREDITCARD = 970;

request.setAttribute("sitePage", "www.freshdirect.com"+request.getRequestURI());

String template = "/common/template/dnav.jsp"; //default
boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com" : request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}

FDSessionUser sessionUser = (FDSessionUser) session.getAttribute(SessionName.USER);
%>

<tmpl:insert template='<%=template %>'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Add Credit / Debit Card"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add Credit / Debit Card</tmpl:put> --%>

    <tmpl:put name='content' direct='true'>
<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='/your_account/payment_information.jsp'>
<%
boolean proceedThruCheckout = false;
if (!result.hasError("payment_method_fraud") && !result.hasError("technical_difficulty")) {%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; 
	if( result.hasError("auth_failure") ) {
		errorMsg=result.getError("auth_failure").getDescription();
	} %>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<%} else {%>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

<%} %>

<form method="post" fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter><%-- intentionally using JUST the fdform attribute, since css is tied to that --%>
	<table width="<%= (mobWeb) ? "100%" : W_YA_ADD_CREDITCARD %>" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td class="text11" WIDTH="<%= W_YA_ADD_CREDITCARD %>">
				<span class="title18">Add Credit / Debit Card</span><br>
			</td>
		</tr>
	</table>
	<input id="hash" name="hash" type="hidden" value="<%= sessionUser.getAddCcUuid() %>" />
	
	<%@ include file="/includes/ckt_acct/i_creditcard_fields.jspf" %>
	<br><br>
	
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%= (mobWeb) ? "100%" : W_YA_ADD_CREDITCARD %>" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
		
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" width="<%= (mobWeb) ? "100%" : W_YA_ADD_CREDITCARD %>">
		<TR VALIGN="BOTTOM">
			<TD width="<%= (mobWeb) ? "100%" : W_YA_ADD_CREDITCARD %>" style="text-align: right;">
				<a class="cssbutton green transparent small" href="<%=cancelPage%>">CANCEL</a>
				<button class="cssbutton green small" name="checkout_credit_card_edit">SAVE CHANGES</button>
			</TD>
		</TR>
	</TABLE>
</form>
<br>
<%--<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP"><TD WIDTH="640"><%@ include file="/includes/i_footer_account.jspf"%></TD></TR>
</TABLE>--%>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>
