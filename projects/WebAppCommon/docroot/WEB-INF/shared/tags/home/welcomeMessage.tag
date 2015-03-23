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
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr align="center"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""></td></tr>
	<tr>
		<% if (user.getLevel() < FDUserI.RECOGNIZED) { %>
		     <td>
		     <% if(user.isDepotUser()){
		               String depotName = com.freshdirect.fdstore.FDDepotManager.getInstance().getDepot(user.getDepotCode()).getName(); %>
		               <font class="title14">Welcome to FreshDirect <%=depotName%> Depot.</font>
		     <% } else { %>
		               <font class="title16">Welcome to FreshDirect!</font>
		     <%}%>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt=""></td>
			<td align="right">Learn more <a href="/about/index.jsp">About Us</a>, or get <a href="/help/<% if(user.isDepotUser()){ %>delivery_info_depot.jsp<% } else { %>delivery_info.jsp<% } %>">Delivery & Pickup Info</a>.</td>
		<% } else if (validOrderCount == 0) { %>
			<td><font class="title14">Welcome <%=custFirstName%>. Thank you for signing up!</font><br><font class="text9">(If you're not <font color="#336600"><%=custFirstName%>, <a href="/logout.jsp?logoutPage=site_access">click here</a>!</font>)</font></td>
		<% } else { //orders > 0 
		
			//------------------------------------------------------------------------------------------------------------------ 
			//-------NOT USING SEGMENT MESSAGE TAG (GetSegmentMessageTag)-----------------------------------------
			//------------------------------------------------------------------------------------------------------------------ 
			if (null == segmentMessage || !segmentMessage.isLocation1()) { %>
				<td align="left">
					<%
					if (user.isEligibleForSignupPromotion()) { //has promo 
						SignupDiscountRule rule = user.getSignupDiscountRule();
					%>
					 	<font class="title14">Welcome back, <%=custFirstName%>!</font><br><font class="text9">(If you're not <font color="#336600"><%=custFirstName%>, <a href="/logout.jsp?logoutPage=site_access">click here</a>!</font>)</font>
					 	
					 	<% if (rule != null) { %>
					 		Get $<%=(int)rule.getMaxAmount()%> off your next order when you order 
					 		$<%=(int)rule.getMinSubtotal()%> or more. <a href="/promotion.jsp">See offer details</a>.
						<% } %>
					<% } else { %>
					  	<font class="title14">Welcome back, <%=custFirstName%>!</font>&nbsp;&nbsp;<font class="text9">(If you're not <font color="#336600"><%=custFirstName%>, <a href="/logout.jsp?logoutPage=site_access">click here</a>!</font>)</font>
					<% } %>
				</td>
				
				<% if (user.isChefsTable()) { // Chef's Table member %>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt=""></td>
					<td align="right"><font class="title12">Thanks for being one of our best customers!</font>&nbsp;&nbsp;<font class="title11"><font color="#336600"><a href="/your_account/manage_account.jsp">Click here for Chef's Table offers.</a></font></font></td>
				<% }  else if (user.isDlvPassActive()) { // user has active delivery pass %>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt=""></td>
					<td align="right"><font class="title12">Enjoy your Unlimited DeliveryPass membership!</font>&nbsp;&nbsp;<font class="title11"><font color="#336600"><a href="/your_account/delivery_pass.jsp">Click here for details.</a></font></font></td>
				<%
				}
			//------------------------------------------------------------------------------------------------------------------ 
			//-------USING SEGMENT MESSAGE IN GetSegmentMessageTag--------------------------------------
			//------------------------------------------------------------------------------------------------------------------ 	
			} else if (segmentMessage != null && segmentMessage.isLocation1()) {  %>
				<td><font class="title14"><%= segmentMessage.getGreeting()%><%=custFirstName%>!</font>&nbsp;&nbsp;<font class="text9">(If you're not <font color="#336600"><%=custFirstName%>, <a href="/logout.jsp?logoutPage=site_access">click here</a>!</font>)</font></td>
		     	<td><img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt=""></td>
				<td align="right"><font class="title12"><%=segmentMessage.getMessage()%></font>&nbsp;&nbsp;<font class="title11"><font color="#336600"><%=segmentMessage.getMessageLink()%></font></font></td>
			<% } 
		
		} %>
	</tr>
</table>
<%}%>
