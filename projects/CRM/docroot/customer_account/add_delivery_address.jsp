<%@ page import="com.freshdirect.webapp.taglib.fdstore.AddressName,
                 com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Add Delivery Address</tmpl:put>

    	<tmpl:put name='content' direct='true'>
        <%
            String actionName = "addAddress";
            ErpAddressModel address = new ErpAddressModel();
	    request.setAttribute("addAddress","true");
        %>
		
		<br clear="all">
	    </tmpl:put>

</tmpl:insert>
