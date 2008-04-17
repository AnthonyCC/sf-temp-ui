<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Add Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<crm:GetFDUser id="user">
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
	<tr>
	<td width="80%">&nbsp;Step 3 of 4: <a href="/checkout/checkout_select_payment.jsp" class="checkout_header_step">Select Payment Method</a> > Add Checking Account <% if (!user.isActive()) { %>&nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a><% } %></td>
	<td align="right"></td>
	</tr>
</table>
<%@ include file="/includes/i_modifyorder.jspf" %>
	<%
	ErpPaymentMethodI paymentMethod = PaymentManager.createInstance(EnumPaymentMethodType.ECHECK);
	String actionName= "addPaymentMethod";
	%>
	
    <crm:CrmPaymentMethodController paymentMethod="<%=paymentMethod%>" result="result" actionName="<%=actionName%>" successPage="/checkout/checkout_select_payment.jsp">
        <%@ include file="/includes/checking_account_details.jspf" %>
    </crm:CrmPaymentMethodController>
	<br clear="all">
</crm:GetFDUser>
</tmpl:put>
</tmpl:insert>
