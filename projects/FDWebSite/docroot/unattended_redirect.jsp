<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="true" />
<%

	String newAddressId = "";
	
	FDSessionUser dlvInfoUser = (FDSessionUser) session.getAttribute(SessionName.USER);
	Collection shippingAddresses = FDCustomerManager.getShipToAddresses(dlvInfoUser.getIdentity());
	Integer addressCount = shippingAddresses.size();
	ErpAddressModel newAddress = null;


	if (addressCount > 0) {
		//we only have one address, since the user just signed up, so it's new
		for(Iterator i = shippingAddresses.iterator(); i.hasNext();) {
			ErpAddressModel thisAddress = (ErpAddressModel)i.next();
			newAddressId = (String)thisAddress.getPK().getId();
			newAddress = thisAddress;
		}
	}
	if ( newAddress != null ) {
		//check for unattended
		%>
		<fd:UnattendedDelivery id="zone" address="<%= newAddress %>" checkUserOptions="true">
			<%
				if (zone.isUnattended() && !EnumUnattendedDeliveryFlag.OPT_OUT.equals(newAddress.getUnattendedDeliveryFlag())) {
					//set attribute so we redirect to homepage afterward instead of your account
					session.setAttribute("redirectToIndex", "true");
					//redirect
					response.sendRedirect(response.encodeRedirectURL("/your_account/edit_delivery_address_unattended.jsp?page=udConfirm&addressId="+newAddressId));
				}
			%>
		</fd:UnattendedDelivery>
		<%
	}
	//fallback
	response.sendRedirect(response.encodeRedirectURL("/index.jsp"));
%>
