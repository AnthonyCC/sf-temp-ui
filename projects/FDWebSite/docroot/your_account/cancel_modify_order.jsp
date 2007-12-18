
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />
<%
FDModifyCartModel modiCart = (FDModifyCartModel)user.getShoppingCart();
String orderId = modiCart.getOriginalOrder().getErpSalesId();
%>
<fd:ModifyOrderController orderId="<%=orderId%>" result="result" action="cancelModify" successPage='<%= "/your_account/order_details.jsp?orderId=" + orderId %>'></fd:ModifyOrderController>
