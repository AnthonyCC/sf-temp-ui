<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:AdjustAvailability/>
<%
String redir = request.getParameter("successPage");
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
if (user.getShoppingCart().containsDlvPassOnly()) {
	redir = "/order/place_order_build.jsp?dlvpasserror=true";
}
else if (!user.isOrderMinimumMet()) {
	session.setAttribute(SessionName.ATP_WARNING,
		"Unfortunately your order total has been adjusted and falls below our $"+ (int)user.getMinimumOrderAmount() +
		" minimum. Please <a href='/order/place_order_build.jsp'>shop for replacement items</a> and then return to Checkout."+
		" We apologize for the inconvenience.");
}
response.sendRedirect(redir);
%>