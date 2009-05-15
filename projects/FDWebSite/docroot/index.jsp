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
<fd:CheckLoginStatus guestAllowed='true' />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();
	boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();
        
        request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
        request.setAttribute("listPos", "SystemMessage,HPLeftTop,HPLeftMiddle,HPLeftBottom,HPMiddleBottom,HPRightBottom");
%>
<tmpl:insert template='/common/template/no_shell.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>
<% 
boolean showAltHome = false;
if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() || (request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1))) showAltHome = true;
%>	
<table width="745" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
    <td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="204" height="1"></td>
    <td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
    <td height="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
    <td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
    <td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="512" height="1"></td>
    <td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
  </tr>
  <tr> 
    <td colspan="3" align="center" <% if (showAltHome) { %>valign="top"<% } else { %>style="border-left: solid 1px #999966; border-right: solid 1px #999966;"<% } %>>
		<% if (showAltHome) { %>
			<table width="216" border="0" cellspacing="0" cellpadding="0">
				<td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
		<% } %>
				<%-- PROMO 1 --%>
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
				<%-- END PROMO 1 --%>
	<% if (showAltHome) { %>
			<tr> 
				<td height="5"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" vspace="0"></td>
    			<td height="5" style="border-bottom: solid 1px #999966;"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="204" height="1" vspace="0"></td>
   			 	<td height="5"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" vspace="0"></td>
		    </tr>
			<tr> 
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" vspace="0"></td>
		    </tr>
			<tr> 
			<td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" vspace="0"></td>
			<td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
			<td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" vspace="0"></td>
		    </tr>
			<tr> 
				<td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
					<%-- PROMO 2a --%>
					<% if (FDStoreProperties.isAdServerEnabled()) { %>
						<SCRIPT LANGUAGE=JavaScript>
						<!--
						OAS_AD('HPLeftMiddle');
						//-->
						</SCRIPT>
					<% } else { %>
						<a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/farm_fresh_hdr.gif" width="170" height="42" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br><a href="/about/index.jsp">Click here to learn more<br>about FreshDirect!</a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0"><br><a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/corn.jpg" width="195" height="85" border="0" vspace="0"></a>
					<% } %>
					<%-- END PROMO 2a --%>				
				</td>
			</tr>
  			<tr> 
				<td height="5"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" vspace="0"></td>
    			<td height="5" style="border-bottom: solid 1px #999966;"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="204" height="1" vspace="0"></td>
   			 	<td height="5"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" vspace="0"></td>
		    </tr>
			<tr> 
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" vspace="0"></td>
		    </tr>
			<tr>
				<td colspan="3">
					<%-- PROMO 3a --%>
					<% if (FDStoreProperties.isAdServerEnabled()) { %>
						<fd:IncludeMedia name="/media/editorial/home/home_bottom_left.html" />
					<% } else { %>
						<fd:IncludeMedia name="/media/editorial/home/home_bottom_left_default.html" />
					<% } %>
					<%-- END PROMO 3a --%>	
			</td>
			</tr>
		</table>
	<% } %>		
	</td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td colspan="3" align="center" <% if (showAltHome) { %>valign="top"<% } else { %>rowspan="5" style="border-left: solid 1px #999966; border-right: solid 1px #999966;"<% } %>>
	<% if (showAltHome) { %>
		<table width="524" border="0" cellspacing="0" cellpadding="0">
			<tr> 
				<td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
	<% } %>
	<%-- MAIN CONTENT--%>
	<% if (showAltHome) {
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
	  } else {   
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
				<table width="490" cellpadding="0" cellspacing="0" border="0">
				<%
				for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
				     FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
				     
				     if (orderInfo.isPending() && orderInfo.getOrderStatus() != EnumSaleStatus.REFUSED_ORDER) {
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
		<img src="/media_stat/images/layout/cccccc.gif" width="490" height="1" vspace="8"><table width="490" cellpadding="0" cellspacing="0" border="0"><tr><td><font class="text9"><b>You have a delivery slot reserved for:</b></font> <a href="/your_account/reserve_timeslot.jsp"><%=CCFormatter.formatReservationDate(rsv.getStartTime())%> @ <%=FDTimeslot.format(rsv.getStartTime(), rsv.getEndTime())+"ppp"%></a></td>
		</tr></table>
		<%}%>
		<img src="/media_stat/images/layout/cccccc.gif" width="490" height="1" vspace="8"><br>
		<%@ include file="/includes/i_departments.jspf" %>
	<% } %>
	<%-- END MAIN CONTENT--%>
		<% if (showAltHome) { %>				
				</td>
			</tr>
  			<tr> 
				<td height="5"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" vspace="0"></td>
    			<td height="5" style="border-bottom: solid 1px #999966;"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="512" height="1" vspace="0"></td>
   			 	<td height="5"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" vspace="0"></td>
		  </tr>
		</table>
	<% } %>
	</td>
  </tr>
  <% if (!showAltHome) { %>
	  <tr> 
		<td height="5"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" vspace="0"></td>
		<td height="5" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
		<td height="5"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" vspace="0"></td>
		<td height="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
	  </tr>
	  <tr> 
		<td height="4" colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" vspace="0"></td>
	  </tr>
	  <tr> 
		<td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" vspace="0"></td>
		<td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
		<td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" vspace="0"></td>
		<td height="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
	  </tr>  
	  <tr> 
		<td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
			<%-- PROMO 2--%>
			<% if (FDStoreProperties.isAdServerEnabled()) { %>
				<SCRIPT LANGUAGE=JavaScript>
				<!--
				OAS_AD('HPLeftMiddle');
				//-->
				</SCRIPT>
			<% } else { %>
				<a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/farm_fresh_hdr.gif" width="170" height="42" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br><a href="/about/index.jsp">Click here to learn more<br>about FreshDirect!</a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0"><br><a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/corn.jpg" width="195" height="85" border="0" vspace="0"></a>
			<% } %>
			<%-- END PROMO 2--%>
		</td>
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	  </tr>
	  <%-- BOTTOM CORNERS for both left and right panels, comment out for showAltHome --%>
	  <tr> 
		<td height="5"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
		<td height="5" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		<td height="5"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
		<td height="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		<td height="5"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
		<td height="5" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		<td height="5"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
	  </tr>
	  <%-- SPACERS --%>
	  <tr> 
		<td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="204" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="512" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
	  </tr>
	  <%-- PROMO 3, 4, 5 --%>
	  <tr valign="top"> 
		<td colspan="7">
		<% if (FDStoreProperties.isAdServerEnabled()) { %>
			<fd:IncludeMedia name="/media/editorial/home/home_bottom_new.html" />
		<% } else { %>
			<fd:IncludeMedia name="/media/editorial/home/home_bottom_default.html" />
		<% } %>
		</td>
	  </tr>
	  <%-- END PROMO 3, 4, 5 --%>
  <% } %>
</table>
</tmpl:put>
</tmpl:insert>