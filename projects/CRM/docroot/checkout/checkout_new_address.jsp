<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.delivery.*"%>
<%@ page import="com.freshdirect.fdstore.*"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="crm" prefix="crm" %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Add Delivery Address</tmpl:put>
<fd:RegistrationController actionName="addDeliveryAddress" result="result" successPage="/checkout/checkout_select_address.jsp?addressStatus=new">
<crm:GetFDUser id="user">
<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
	<tr>
	<td width="80%">&nbsp;Step 1 of 4 <a href="/checkout/checkout_select_address.jsp" class="checkout_header_step">Select Delivery Address</a> > Add New Address <% if (!user.isActive()) { %>&nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is <a href="<%= response.encodeURL("/main/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a><% } %></td>
	<td align="right"></td>
	</tr>
</table>
<%@ include file="/includes/i_modifyorder.jspf" %>
<%
        String addressId = "";
		ErpAddressModel address = new ErpAddressModel();
%>
	        	<%@ include file="/includes/delivery_address_details.jspf" %>
		<br clear="all">
</tmpl:put>
</crm:GetFDUser>
</fd:RegistrationController>
</tmpl:insert>
