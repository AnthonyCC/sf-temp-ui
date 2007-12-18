<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.delivery.*"%>
<%@ page import="com.freshdirect.delivery.restriction.DlvRestrictionsList"%>
<%@ page import="com.freshdirect.fdstore.*"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="crm" prefix="crm" %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.delivery.*"%>
<%@ page import="com.freshdirect.fdstore.*"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Edit Delivery Address</tmpl:put>

<fd:RegistrationController actionName="editDeliveryAddress" result="result" successPage="/checkout/checkout_select_address.jsp">
<crm:GetFDUser id="user">
<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
	<TR>
	<TD WIDTH="80%">&nbsp;Step 1 of 4: <a href="/checkout/checkout_select_address.jsp" class="checkout_header_step">Select Delivery Address</a> > Edit Address <% if (!user.isActive()) { %>&nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is <a href="<%= response.encodeURL("/main/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a><% } %></TD>
	<td align="right"></td>
	</TR>
</TABLE>
<%@ include file="/includes/i_modifyorder.jspf" %>

<%
        String addressId = request.getParameter("addressId");
%>
			<crm:CrmGetAddress addressId="<%=addressId%>" id="address" user="<%=user%>">
	        	<%@ include file="/includes/delivery_address_details.jspf" %>
			</crm:CrmGetAddress>
		<br clear="all">
		
</tmpl:put>
</crm:GetFDUser>
</fd:RegistrationController>

</tmpl:insert>
