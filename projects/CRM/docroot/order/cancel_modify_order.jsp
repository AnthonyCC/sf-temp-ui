<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
FDModifyCartModel modiCart = (FDModifyCartModel)user.getShoppingCart();
String orderId = modiCart.getOriginalOrder().getErpSalesId();
%>
<fd:ModifyOrderController orderId="<%=orderId%>" result="result" action="cancelModify" successPage='<%= "/main/order_details.jsp?orderId=" + orderId %>'></fd:ModifyOrderController>
