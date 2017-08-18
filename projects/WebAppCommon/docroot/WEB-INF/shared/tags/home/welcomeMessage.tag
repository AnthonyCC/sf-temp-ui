<%@tag  import="com.freshdirect.common.customer.EnumServiceType"
 		import="com.freshdirect.fdstore.promotion.SignupDiscountRule"
		import="com.freshdirect.fdstore.customer.FDUserI"%><%@ 
	attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %><%@ 
	attribute name="segmentMessage" required="true" rtexprvalue="true" type="com.freshdirect.webapp.taglib.fdstore.SegmentMessage" %><%@
	attribute name="isCosPage" required="true" rtexprvalue="true" type="java.lang.Boolean" %><%
	
	boolean isCosUser = EnumServiceType.CORPORATE.equals(user.getUserServiceType());
	if (isCosUser&&isCosPage || !isCosUser&&!isCosPage){
	
		int validOrderCount = user.getAdjustedValidOrderCount();
		String custFirstName = user.getFirstName();
%>
<div id="welcome-section-container">
	<% if (user.getLevel() < FDUserI.RECOGNIZED) { %>
		<div class="text-left inline">
		     <% if(user.isDepotUser()){
		               String depotName = com.freshdirect.fdstore.FDDeliveryManager.getInstance().getDepot(user.getDepotCode()).getName(); %>
		               <h1 class="welcome-msg-title-small bold">Welcome to FreshDirect <%=depotName%> Depot.</h1>
		     <% } else { %>
		               <h1 class="welcome-msg-title-big bold">Welcome to FreshDirect!</h1>
		     <%}%>
		</div>
		<div class="fl-right inline">
			<span class="small-text">
				Learn more <a href="/about/index.jsp">About Us</a>, or get <a href="/help/<% if(user.isDepotUser()){ %>delivery_info_depot.jsp<% } else { %>delivery_info.jsp<% } %>">Grocery Delivery & Pickup Info</a>.
			</span>
		</div>
	<% } else if (validOrderCount == 0) { %>
		<div class="text-left inline">
			<h1 class="welcome-msg-title-small bold">Welcome <%=custFirstName%>. Thank you for signing up!</h1>
			<a href="/logout.jsp?logoutPage=site_access">(If you're not <%=custFirstName%>, click here!)</a>
		</div>		
	<% } else {  //orders %>
		<div class="text-left inline">
			<% if (null == segmentMessage || !segmentMessage.isLocation1()) { 
				if (user.isEligibleForSignupPromotion()) { //has promo 
					SignupDiscountRule rule = user.getSignupDiscountRule(); %>
			
					<h1 class="welcome-msg-title-small bold">Welcome back, <%=custFirstName%>!</h1>
					<a href="/logout.jsp?logoutPage=site_access">(If you're not <%=custFirstName%>, click here!)</a>
					 	
					<% if (rule != null) { %>
					 	Get $<%=(int)rule.getMaxAmount()%> off your next order when you order 
					 	$<%=(int)rule.getMinSubtotal()%> or more. <a href="/promotion.jsp">See offer details.</a>
						<% } %>
				<% } else { %>
				  	<h1 class="welcome-msg-title-small inline bold">Welcome back, <%=custFirstName%>!</h1>
				  	<a href="/logout.jsp?logoutPage=site_access">(If you're not <%=custFirstName%>, click here!)</a>
				<% } %>
		</div>
				<% if (user.isChefsTable()) { // Chef's Table member %>
					<div class="fl-right inline small-text bold">
						<span>Thanks for being one of our best customers!</span>
						<a href="/your_account/manage_account.jsp">Click here for Chef's Table offers.</a>
					</div>
				<% } else if (user.isDlvPassActive()) { // user has active delivery pass %>
					<div class="fl-right small-text bold">
						<a href="/your_account/delivery_pass.jsp">Enjoy your Unlimited DeliveryPass membership! Click here for details.</a>
					</div>
				<% }
			//------------------------------------------------------------------------------------------------------------------ 
			//-------USING SEGMENT MESSAGE IN GetSegmentMessageTag--------------------------------------
			//------------------------------------------------------------------------------------------------------------------ 	
			} else if (segmentMessage != null && segmentMessage.isLocation1()) {  %>
				<div class="text-left">
					<h1 class="welcome-msg-title-small bold"><%= segmentMessage.getGreeting()%><%=custFirstName%>!</h1>
					<a href="/logout.jsp?logoutPage=site_access">(If you're not <%=custFirstName%>, click here!)</a>
				</div>
				<div class="fl-right inline small-text bold"> <!-- mi van??? -->
					<span><%=segmentMessage.getMessage()%></span>
					<span><%=segmentMessage.getMessageLink()%></span>
				</div>
			<% }
		} %>
		<br class="clear" />
</div>
<%}%>
