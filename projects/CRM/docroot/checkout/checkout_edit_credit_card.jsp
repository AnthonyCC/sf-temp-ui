<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Edit Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>
<crm:GetFDUser id="user">
	<jsp:include page='/includes/order_header.jsp'/>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
		<tr>
		<td width="80%">&nbsp;Step 3 of 4: <a href="/checkout/checkout_select_payment.jsp" class="checkout_header_step">Select Payment Method</a> > Edit Credit Card <% if (!user.isActive()) { %>&nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is <a href="<%= response.encodeURL("/main/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a><% } %></td>
		<td align="right"></td>
		</tr>
	</table>
	<%@ include file="/includes/i_modifyorder.jspf" %>

	<%
	String paymentId = request.getParameter("paymentId");
	String actionName= "editPaymentMethod";
	%>
    <crm:CrmGetPaymentMethod id="paymentMethod" paymentId="<%=paymentId%>" user="<%=user%>">
        <crm:CrmPaymentMethodController paymentMethod="<%=paymentMethod%>" result="result" actionName="<%=actionName%>" successPage="/checkout/checkout_select_payment.jsp">
            <%@ include file="/includes/credit_card_details.jspf" %>
        </crm:CrmPaymentMethodController>
    </crm:CrmGetPaymentMethod>
	<br clear="all">
</crm:GetFDUser>
</tmpl:put>
</tmpl:insert>
