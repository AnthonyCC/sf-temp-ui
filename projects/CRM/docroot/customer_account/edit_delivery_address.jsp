<%@ page import="com.freshdirect.webapp.taglib.fdstore.AddressName,
                 com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Edit Delivery Address</tmpl:put>

	<tmpl:put name="header" direct="true"><jsp:include page="/includes/customer_header.jsp" /></tmpl:put>
		
	<tmpl:put name='content' direct='true'>
	<%
        String actionName = "editAddress";
        String addressId = request.getParameter("addressId");
	%>
		
		<br clear="all">
	</tmpl:put>

</tmpl:insert>