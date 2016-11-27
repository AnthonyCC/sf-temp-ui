<%@page import="com.freshdirect.fdstore.customer.FDUserI"%><%@page 
import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%><%@page 
import="com.freshdirect.webapp.taglib.fdstore.SessionName"%><%@page 
import="com.freshdirect.logistics.delivery.model.EnumDeliveryStatus"%><%@page 
import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%><%@page 
import="com.freshdirect.common.address.AddressModel"%><%
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
boolean needDefaultContainer = true;

if (user.getLevel() == FDUserI.GUEST) {
	EnumDeliveryStatus deliveryStatus=user.getDeliveryStatus();
	
	if (EnumDeliveryStatus.DONOT_DELIVER.equals(deliveryStatus)) {
		needDefaultContainer = false;
%>
<div id="nodeliver-form" class="invisible message" data-type="sitemessage">
	<div class="nodeliver-form">
		<% if (!user.isFutureZoneNotificationEmailSentForCurrentAddress()) { %>
			<form class="n"><label class="e"><b>That e-mail has already been added!</b><br>Please enter a different e-mail address.</label><label class="n"></label><div><input type="text" id="location-email" class="placeholder" placeholder="enter your e-mail address"><input type="image" src="/media_stat/images/locationbar/button_submit.png" id="location-submit"></div><div class="p"></div></form>
			<div class=""><label class="n">Enter your email address and we'll notify you when service expands in your area.</label></div>
		<% } %>
		<div class="">We currently serve the New York and Philadelphia metropolitan areas. Please feel free to continue browsing, but you will not able to place an order. <a href="/help/delivery_zones.jsp" class="delivery-popuplink">Click here</a> to see current delivery zones. To enter a different zip code, please enter in the box in the upper left.</div>
	</div>
</div>
<div id="nodeliver-thanks" class="invisible" data-type="sitemessage">
	<div class="nodeliver-form"><b>Thanks for your email!</b> We will notify you once we start delivering your area.</div>
</div><% 
	
	} else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(deliveryStatus) ) {
		needDefaultContainer = false;
		
		if(user.isMoreInfoPopupShownForCurrentAddress()) {
			%><div id="partialdeliver" class="invisible message" data-type="sitemessage"><% 
		} else {
			user.setMoreInfoPopupShownForCurrentAddress(true);
			%><div id="partialdeliver" class="invisible message moreinfo <%= user.isCorporateUser() ? "cos" : "" %>" data-type="sitemessage"><% 
		}
		%><div class="partial"><span class="orange">We don't currently deliver to all parts of your neighborhood.</span> Please feel free to continue shopping, but note that if your address is not within one of our delivery zones, you will not able to place an order to delivery. <a href="/help/delivery_zones.jsp" class="delivery-popuplink">Click here</a> to see current delivery zones. To enter a different zip code, please enter in the box in the upper left.</div><b style="display:none"><%= ((AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR)) %></b></div><%
	} 
}

if (needDefaultContainer) {
	 %><div class="message" data-type="sitemessage"></div><%
}%>