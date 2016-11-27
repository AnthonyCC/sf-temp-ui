<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:AdjustAvailability/>
<%
String redir = request.getParameter("successPage");
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
if (user.getShoppingCart().containsDlvPassOnly()) {
	redir = "/checkout/view_cart.jsp";
}
else if (!user.isOrderMinimumMet() && user.getMasqueradeContext() == null) {
	redir = "/checkout/view_cart.jsp";
	String isEBT = request.getParameter("ebt");
	if("true".equals(isEBT)){
		session.setAttribute(SessionName.ATP_WARNING,
				"Because some items were ineligible to be payed by EBT, they've been removed from your cart."
				+ " Unfortunately this means that your order total has been adjusted and falls below our $"
				+ (int)user.getMinimumOrderAmount()
				+ " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout."
				+ " We apologize for the inconvenience.");
	}else{
		session.setAttribute(SessionName.ATP_WARNING,
		"Because some items were unavailable for delivery at the time you selected, they've been removed from your cart."
		+ " Unfortunately this means that your order total has been adjusted and falls below our $"
		+ (int)user.getMinimumOrderAmount()
		+ " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout."
		+ " We apologize for the inconvenience.");
	}
}else if(user.getShoppingCart().getSubTotal() < user.getShoppingCart().getDeliveryReservation().getMinOrderAmt()){
	redir = "/checkout/view_cart.jsp";
}
response.sendRedirect(redir);
%>
