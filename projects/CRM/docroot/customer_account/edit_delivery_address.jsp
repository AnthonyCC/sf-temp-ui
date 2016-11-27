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
		<crm:GetFDUser id="user">
			<crm:CrmGetAddress addressId="<%=addressId%>" id="address" user="<%=user%>">
				<crm:CrmAddressController address="<%=address%>" result="result" actionName="<%=actionName%>" successPage="/main/account_details.jsp">
					<%@ include file="/includes/delivery_address_details.jspf" %>
				</crm:CrmAddressController>
			</crm:CrmGetAddress>
		</crm:GetFDUser>
		<br clear="all">
	</tmpl:put>

</tmpl:insert>