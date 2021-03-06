<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%><%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_EDIT_CREDITCARD = 970;
%>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<% 
String template = "/common/template/dnav.jsp";
String pageType = "ebt_edit";
String gcPage = (request.getParameter("gc")!=null)?request.getParameter("gc").toLowerCase():null;
    if (gcPage != null && ("true".equals(gcPage) && FDStoreProperties.isGiftCardEnabled())) {
            template = "/common/template/giftcard.jsp";
            pageType = "gc_ebt_edit";
    }
String rhPage = (request.getParameter("rh")!=null)?request.getParameter("rh").toLowerCase():null;
    if (rhPage != null && ("true".equals(rhPage) && FDStoreProperties.isRobinHoodEnabled())) {
            template = "/common/template/robinhood.jsp";
    }
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='<%=template%>'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Edit EBT Card"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Edit EBT Card</tmpl:put> --%>
<tmpl:put name='pageType' direct='true'><%= pageType %></tmpl:put>
<tmpl:put name='content' direct='true'>
	<style>
		.W_YA_EDIT_CREDITCARD { width: <%= W_YA_EDIT_CREDITCARD %>px; }
	</style>
<%
String successRedirect = "/your_account/payment_information.jsp";
if("true".equals(request.getParameter("gc"))) {
    successRedirect = "/gift_card/purchase/purchase_giftcard.jsp";
}else if("true".equals(request.getParameter("rh"))) { //Robin Hood..
    successRedirect = "/robin_hood/rh_submit_order.jsp";
}
boolean proceedThruCheckout=false;
%>
<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='<%=successRedirect%>'>
<% 
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
<FORM fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter name='ccEdit' method='POST'>
<table class="W_YA_EDIT_CREDITCARD" cellspacing="0" cellpadding="0" border="0">
<tr>
<td class="text11" class="W_YA_EDIT_CREDITCARD">
<font class="title18">Edit EBT Card</font><br>
</td>
</tr>
</table>
<%@ include file="/includes/ckt_acct/i_ebtcard_fields.jspf"%>
<br/><br/>
<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" class="W_YA_EDIT_CREDITCARD" HEIGHT="1" BORDER="0"><BR/>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%= W_YA_EDIT_CREDITCARD %>">
<TR VALIGN="TOP">
    <TD class="W_YA_EDIT_CREDITCARD" ALIGN="RIGHT">
        <a class="cssbutton green transparent small" href="<%=cancelPage%>">CANCEL</a>
        <button class="cssbutton green small" name="checkout_credit_card_edit">SAVE CHANGES</button>
    </TD>
</TR>
</TABLE>
<br/>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>