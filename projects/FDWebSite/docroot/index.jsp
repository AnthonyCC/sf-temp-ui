<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
<%@ page import='com.freshdirect.fdstore.FDTimeslot'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>


<fd:css href="/assets/css/homepage/homepage.css"/>

	
	
<% //expanded page dimensions
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;
%>


<fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" />

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();
	boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();
        
        request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
%>

<tmpl:insert template='/common/template/no_shell.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>

<fd:GetSegmentMessage id='segmentMessage' user="<%=user%>">

<%
	boolean location2Media = false;
	if(null != segmentMessage && segmentMessage.isLocation2()) {
        	location2Media = true;
        }
        String mktgPath = "";
        String winbackPath = "";
    /* This was HP Phase 1 which has been replaced by HP Phase 2 APPDEV-484 
    if(!user.isCampaignMsgLimitViewed() && (!user.getWinbackPath().equals("false") || !user.getMarketingPromoPath().equals("false"))) {
	        location2Media = true;
	        mktgPath = user.getMarketingPromoPath().trim();
	        winbackPath = user.getWinbackPath().trim();
        } 
        */
        if(location2Media) {
        	request.setAttribute("listPos", "SystemMessage,HPLeftTop,HPLeftMiddle,HPLeftBottom,HPWideBottom,HPWideTop");
        } else {
        	request.setAttribute("listPos", "SystemMessage,HPLeftTop,HPLeftMiddle,HPLeftBottom,HPMiddleBottom,HPRightBottom,HPWideBottom,HPWideTop");
        }
%>

<% 
boolean showAltHome = false;
if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() || 
	(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1))) 
		showAltHome = true;
%>
 
		<div class="holder">
		
		<%-- PROMO 1 --%>
			<div class="ad ad1">
				<div class="adbox">
					<table width="100%" height="100%">
						<tr>
						<td align="center">
						<%if ( mainPromo ) {%>
							<%@ include file="includes/home/i_main_promo.jspf" %>
						<%} else if (FDStoreProperties.isAdServerEnabled()) {%>
							<SCRIPT LANGUAGE=JavaScript>
							<!--
							OAS_AD('HPLeftTop');
							//-->
							</SCRIPT>
						
						<%}else {%>
							<%@ include file="includes/home/i_current_promo.jspf" %>
						<%}%>
						</td>
						</tr>
					</table>
					<div class="adbox_bottom"></div>
				</div>
			</div> 
		<%-- END PROMO 1 --%>
			
		<%-- PROMO 2--%>
			<div class="ad ad2">
				<div class="adbox">
					<table width="100%" height="100%">
						<tr>
						<td align="center">
						<% if (FDStoreProperties.isAdServerEnabled()) { %>
							<SCRIPT LANGUAGE=JavaScript>
							<!--
							OAS_AD('HPLeftMiddle');
							//-->
							</SCRIPT>
						<% } else { %>
							<a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/farm_fresh_hdr.gif" width="170" height="42" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br><a href="/about/index.jsp">Click here to learn more<br>about FreshDirect!</a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0"><br><a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/corn.jpg" width="195" height="85" border="0" vspace="0"></a>
						<% } %>
						</td>
						</tr>
					</table>
					<div class="adbox_bottom"></div>
				</div>
			</div> 
		<%-- END PROMO 2--%>
		
		
		<%-- PROMO 3--%>	
			<div class="ad ad3">
				<div class="adbox">
					<table width="100%" height="100%">
						<tr>
						<td align="center">
							<SCRIPT LANGUAGE=JavaScript>
							<!--
							OAS_AD('HPLeftBottom');
							//-->
							</SCRIPT>
						</td>
						</tr>
					</table>
					<div class="adbox_bottom"></div>
				</div>
			</div> 
		<%-- END PROMO 3--%>
			
		<%-- MAIN CONTENT--%>
		
		
			<div class="content"> 
			
				<% if (showAltHome && !location2Media) {
			String mediaPath = null;
	          	if(validOrderCount < 1){
	            	mediaPath=FDStoreProperties.getHPLetterMediaPathForNewUser();
	          	} else { 
	          		mediaPath=FDStoreProperties.getHPLetterMediaPathForOldUser();
	          	}
		%>
			<fd:IncludeMedia name="<%=mediaPath%>" />
		<%    
	        	// update user already visited home page letter
	        	user.setHomePageLetterVisited(true);
	        	// not sure we need to do this here because saving cart too often is not recomended
	          
	        	if(user instanceof FDSessionUser){                
	        		FDSessionUser sessionUser=(FDSessionUser)user;
	                	sessionUser.saveCart(true);          
	        	}
	      } else if (!showAltHome && location2Media) {
	%>      
			<%@ include file="includes/home/i_intro_hdr.jspf"%>
				<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
					<% int pendingOrderCount = 0;%>
					<fd:OrderHistoryInfo id='orderHistoryInfo'>
					<%
					// also need to know how many orders the customer has that are not still pending
					pendingOrderCount = 0;
					for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
						FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
						if (orderInfo.isPending()) {
							pendingOrderCount++;
						}
					} 			
					if (orderHistoryInfo != null && orderHistoryInfo.size() != 0 && pendingOrderCount > 0) {
					%>
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
						<%
						for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
							FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
                            String ordDeliveryType = orderInfo.getDeliveryType().toString();
                            //gift cards
                            String gcCodePersonal = EnumDeliveryType.GIFT_CARD_PERSONAL.getCode();
                            String gcCodeCorporate = EnumDeliveryType.GIFT_CARD_CORPORATE.getCode();
                            //robin hood
                            String donatePersonal = EnumDeliveryType.DONATION_INDIVIDUAL.getCode();
                            String donateCorporate = EnumDeliveryType.DONATION_BUSINESS.getCode();	      				     
	      				     if (orderInfo.isPending() && orderInfo.getOrderStatus() != EnumSaleStatus.REFUSED_ORDER 
                            && (!(ordDeliveryType).equals(gcCodePersonal) && !(ordDeliveryType).equals(gcCodeCorporate))
                            && (!(ordDeliveryType).equals(donatePersonal) && !(ordDeliveryType).equals(donateCorporate))          
                            ){                            
	      				%>
	      				       <tr><td><img src="/media_stat/images/layout/clear.gif" width="310" height="6"></td>
	      					<td><img src="/media_stat/images/layout/clear.gif" width="150" height="6"></td></tr>
	      				       <tr><td colspan="2" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
	      				       <tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
	      				       <tr><td><font class="text9"><b>Your order will be delivered on:</b></font> 
	      				       		<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>">
	      				       		<%= new SimpleDateFormat("EEE MM/dd/yy").format(orderInfo.getRequestedDate()) %> 
	      				       		@ <%= FDTimeslot.format(orderInfo.getDeliveryStartTime(),orderInfo.getDeliveryEndTime())%></a></td>
	      				       <td align="right">
	      				       <% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
	      						<font class="text9">To make changes, <a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>">click here</a>.</font>
	      				       <% } else { %>
	      						&nbsp;
	      				       <% } %></td></tr>
	      
	      				     <% } else if (orderInfo.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) { %>
	      					       <tr><td colspan="2" class="text10rbold"><b>Pending Order: Please contact us at <%=user.getCustomerServiceContact()%> as soon as possible to reschedule delivery.</b></td></tr>	
	      				<%   }
	      				     break;
	      				} 
	      				%>
	      
	      				</table>
	      		     <% } %>
	      		     </fd:OrderHistoryInfo>
		<% }
	      
		  } else if(!showAltHome && !location2Media) {   
	     %>
	   	<%@ include file="includes/home/i_intro_hdr.jspf"%>
	   		<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
	   						
	   			<fd:OrderHistoryInfo id='orderHistoryInfo'>
	   			<%
	   			// also need to know how many orders the customer has that are not still pending
	   			int pendingOrderCount = 0;
	   			for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
	   				FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
	   				if (orderInfo.isPending()) {
	   					pendingOrderCount++;
	   				}
	   			} 			
	   			if (orderHistoryInfo != null && orderHistoryInfo.size() != 0 && pendingOrderCount > 0) {
	   			%>
	   				<table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin-bottom: 6px;">
	   				<%
	   				for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
	   				     FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
						 String ordDeliveryType = orderInfo.getDeliveryType().toString();
						//gift cards
						String gcCodePersonal = EnumDeliveryType.GIFT_CARD_PERSONAL.getCode();
						String gcCodeCorporate = EnumDeliveryType.GIFT_CARD_CORPORATE.getCode();
						//robin hood
						String donatePersonal = EnumDeliveryType.DONATION_INDIVIDUAL.getCode();
						String donateCorporate = EnumDeliveryType.DONATION_BUSINESS.getCode();
	   				     
						if (orderInfo.isPending() && orderInfo.getOrderStatus() != EnumSaleStatus.REFUSED_ORDER 
							&& (!(ordDeliveryType).equals(gcCodePersonal) && !(ordDeliveryType).equals(gcCodeCorporate))
							&& (!(ordDeliveryType).equals(donatePersonal) && !(ordDeliveryType).equals(donateCorporate))          
						){
	   				%>
	   				       <tr><td><img src="/media_stat/images/layout/clear.gif" width="310" height="6"></td>
	   					<td><img src="/media_stat/images/layout/clear.gif" width="150" height="6"></td></tr>
	   				       <tr><td colspan="2" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
	   				       <tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
	   				       <tr><td><font class="text9"><b>Your order will be delivered on:</b></font> 
	   				       		<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>">
	   				       		<%= new SimpleDateFormat("EEE MM/dd/yy").format(orderInfo.getRequestedDate()) %> 
	   				       		@ <%= FDTimeslot.format(orderInfo.getDeliveryStartTime(),orderInfo.getDeliveryEndTime())%></a></td>
	   				       <td align="right">
	   				       <% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
	   						<font class="text9">To make changes, <a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>">click here</a>.</font>
	   				       <% } else { %>
	   						&nbsp;
	   				       <% } %></td></tr>
	   
	   				     <% } else if (orderInfo.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) { %>
	   					       <tr><td colspan="2" class="text10rbold"><b>Pending Order: Please contact us at <%=user.getCustomerServiceContact()%> as soon as possible to reschedule delivery.</b></td></tr>	
	   				<%   }
	   				     break;
	   				} 
	   				%>
	   				</table>
	   		     <% } %>
	   		     </fd:OrderHistoryInfo>
	   		<% } %>
	   		<% if(user.isEligibleForPreReservation() && user.getReservation() != null){
	   			FDReservation rsv = user.getReservation();
	   		%>
	   		<img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="8"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td><font class="text9"><b>You have a delivery slot reserved for:</b></font> <a href="/your_account/reserve_timeslot.jsp"><%=CCFormatter.formatReservationDate(rsv.getStartTime())%> @ <%=FDTimeslot.format(rsv.getStartTime(), rsv.getEndTime())%></a></td>
	   		</tr></table>
	   		<% }
	   	}
	   	
	        if (location2Media) {
				%>

			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr><td><img src="/media_stat/images/layout/clear.gif" width="310" height="6"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="150" height="6"></td></tr>
			<tr><td colspan="2" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
			<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
			</table>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr><td>
				<fd:IncludeMedia name="/media/editorial/hp_notes/winback/lapsed.html" />

				<% user.setCampaignMsgViewed(user.getCampaignMsgViewed() + 1); %>
			</td></tr>
			</table>

		<% } %>
	   		<img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="8"><br>
	   		<%@ include file="/includes/i_departments.jspf" %> 
	   		
	   	
	   		
	   <%-- END MAIN CONTENT--%>
	   		
	   	<%-- PROMO 4 BOTTOM--%>
			<div class="ad4">
				<div class="adbox4">
					<span>
						<SCRIPT LANGUAGE=JavaScript>
						<!--
						OAS_AD('HPWideBottom');
						//-->
						</SCRIPT>
					</span>
					<div class="adbox_bottom4"></div>
				</div>			
			</div> 
		<%-- END PROMO 4 BOTTOM--%>
		</div> 
	</div>
</fd:GetSegmentMessage>
</tmpl:put>
</tmpl:insert>
