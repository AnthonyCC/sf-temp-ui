<%@ taglib uri='logic' prefix='logic' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%
String[] registration = {
"Registration",
"/kbit/policy.jsp?show=About", "Service & Business Model",
"/kbit/policy.jsp?show=Signup", "Signup & Service Availability",
"/kbit/procedure.jsp?show=CreditCard", "Adding Credit Card" 
};

String[] order = {
"Shopping",
"/kbit/policy.jsp?show=Shopping", "Shopping",
"/kbit/policy.jsp?show=Delivery", "Delivery Address", 
"/kbit/policy.jsp?show=Payment", "Payment Method", 
"/kbit/procedure.jsp?show=CreditCard", "Adding Credit Card", 
"/kbit/procedure.jsp?show=ChargeAuth", "Charges and Authorizations", 
"/kbit/procedure.jsp?show=MakeGoodOrder", "Make Good Order", 
"/kbit/store.jsp?show=mea", "Meat", 
"/kbit/store.jsp?show=veg", "Vegetable", 
"/kbit/store.jsp?show=cof", "Coffee",
"/kbit/store.jsp?show=bak", "Bakery",  
"/kbit/store.jsp?show=fro", "Frozen"   
//"/kbit/procedure.jsp?show=LateDelivery", "Late Delivery"
};

String[] checkout = {
"Checkout",
"/kbit/policy.jsp?show=Delivery", "Delivery Address", 
"/kbit/policy.jsp?show=Payment", "Payment Method", 
"/kbit/procedure.jsp?show=CreditCard", "Adding Credit Card", 
"/kbit/procedure.jsp?show=ChargeAuth", "Charges and Authorizations", 
"/kbit/procedure.jsp?show=MakeGoodOrder", "Make Good Order"//, 
//"/kbit/procedure.jsp?show=LateDelivery", "Late Delivery"
};

String[] returns = {
"Product Credit Policy",
"/kbit/store.jsp?show=mea", "Meat", 
"/kbit/store.jsp?show=bak", "Bakery",
"/kbit/store.jsp?show=fro", "Frozen" 
};

String[] phone = {
"Phone Call",
"/kbit/index.jsp", "Useful Numbers",
"/kbit/phone.jsp?show=technique", "Calling techniques",
"/kbit/phone.jsp?show=etiquette#Hold", "How to place customers on Hold",
"/kbit/phone.jsp?show=etiquette#Keyspell", "Keyspelling for information verification",
"/kbit/phone.jsp?show=system", "ACD Phone System"
};

String[] freshdirect = {
"About FD",
"/kbit/policy.jsp?show=About", "Service & Business Model"
};

String currentPage = request.getRequestURI(); 

boolean inOrder = currentPage.indexOf("order") > -1? true: false;
boolean inCheckout = currentPage.indexOf("checkout") > -1? true: false;
boolean inRegistration = currentPage.indexOf("registration") > -1? true: false;
boolean inReturns = currentPage.indexOf("returns") > -1? true: false;

List kbcont = new ArrayList();

if (inRegistration) {
	kbcont.add(registration);
};

if (inOrder) {
	kbcont.add(order);
};

if (inCheckout) {
	kbcont.add(checkout);
};

if (inReturns) {
	kbcont.add(returns);
};

kbcont.add(phone);
kbcont.add(freshdirect);

String topicTitle = "";
%>

<!-- <logic:iterate id='category' collection="<%= kbcont %>" type="String[]" indexId="i">
	<% StringBuffer links = new StringBuffer(); %>
	<% topicTitle = category[0]; %>
	<% for (int n = 1; n < category.length; n++) { %>
		<%  if (n%2 != 0) { %>
			<% links.append("<a  href=\\\"javascript:popResize('"+category[n]+"','715','940','kbit')\\\">"); %>
		<% } else { %>
			<%  links.append(category[n]+"</a><br>"); %>
		<% } %>
	<% } %>
	<script language="Javascript">
	var helpLinks<%=i.intValue()%> = "<%=links.toString()%>";
	</script>&middot;&nbsp;<a id="topic<%=i.intValue()%>" onclick="hoverTopic('topic<%=i.intValue()%>','<%=topicTitle%> Help',helpLinks<%=i.intValue()%>);" onmouseout="return nd();"><%=topicTitle%></a>&nbsp;&nbsp;
</logic:iterate> -->
<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmFDUpdatesHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for FD Updates Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" >FD Updates</a>&nbsp;&nbsp;
<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmMainHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" >Help</a>&nbsp;&nbsp;
<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmPromotionsHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Promotions Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" >Promotions</a>&nbsp;&nbsp;
<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmTimeSlotHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Timeslot Request Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" >Timeslot Request</a>&nbsp;&nbsp;