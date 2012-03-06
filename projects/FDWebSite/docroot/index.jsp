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
<%@ page import="com.freshdirect.cms.ContentKey"%>
<%@ page import="com.freshdirect.fdstore.content.StoreModel"%>
<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>


	
	
<% //expanded page dimension
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


<fd:css href="/assets/css/homepage/homepage.css"/>
<fd:GetSegmentMessage id='segmentMessage' user="<%=user%>">

<%
	boolean location2Media = false;
	if(null != segmentMessage && segmentMessage.isLocation2()) {
        	location2Media = true;
    }
   	request.setAttribute("listPos", "SystemMessage,HPFeatureTop,HPFeature,HPTab1,HPTab2,HPTab3,HPTab4,HPFeatureBottom,HPWideBottom");
%>

<% 
boolean showAltHome = false;
if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() || 
	(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1))) 
		showAltHome = true;
%>
 
		<div class="holder">
		
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
					<%
						int pendingOrderCount = 0;
						List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();
						validPendingOrders.addAll(user.getPendingOrders());
						//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
						pendingOrderCount = validPendingOrders.size();

						if (pendingOrderCount > 0) {

							FDOrderInfoI orderInfo = (FDOrderInfoI) validPendingOrders.get(0);

					%>
						<div class="index_ordMod_cont" id="index_table_ordModify_0">
		   					<div class="index_ordMod_cont_child">
								<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>" class="orderNumb"><%= orderInfo.getErpSalesId() %></a>
								<span style="padding-left: 30px;"><span class="dow"><%= new SimpleDateFormat("EEEEE").format(orderInfo.getRequestedDate()) %></span> <%= new SimpleDateFormat("MM/dd/yyyy").format(orderInfo.getRequestedDate()) %> 
									<span class="pipeSep">|</span> <%= FDTimeslot.format(orderInfo.getDeliveryStartTime(),orderInfo.getDeliveryEndTime())%></span>
	
								<div class="ordModifyButCont">
									<% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
										<form name="modify_order" id="modify_order" method="POST" action="/your_account/modify_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>&action=modify">
									<% } %>
									<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>" class="">view details</a>&nbsp;
									<% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
											<input type="hidden" name="orderId" value="<%= orderInfo.getErpSalesId() %>" />
											<input type="hidden" name="action" value="modify" />
											<table class="butCont fright" style="margin-left: 10px;">
												<tr>
													<td class="butOrangeLeft"><!-- --></td>
													<td class="butOrangeMiddle"><a class="butText" href="/your_account/modify_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>" onclick="$('modify_order').submit(); return false;">modify order</a></td>
													<td class="butOrangeRight"><!-- --></td>
												</tr>
											</table>
										</form>
												
										
									<% } else { %>
										&nbsp;
									<% } %>
								</div>
							</div>
						</div>
					<% } %>
			<% }
	      
		  } else if(!showAltHome && !location2Media) {
	     %>
	   	<%@ include file="includes/home/i_intro_hdr.jspf" %>
	   		<% if(user.isEligibleForPreReservation() && user.getReservation() != null) {
	   			FDReservation rsv = user.getReservation();
	   		%>
		   		<img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="8" alt="" />
		   		
		   		<table width="100%" cellpadding="0" cellspacing="0" border="0">
		   			<tr>
		   				<td>
		   					<font class="text9"><b>You have a delivery slot reserved for:</b></font> <a href="/your_account/reserve_timeslot.jsp"><%=CCFormatter.formatReservationDate(rsv.getStartTime())%> @ <%=FDTimeslot.format(rsv.getStartTime(), rsv.getEndTime())%></a></td>
		 	  		</tr>
		 	  	</table>
	   		<% } %>
	   		<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
					<%
						int pendingOrderCount = 0;
						List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();
						validPendingOrders.addAll(user.getPendingOrders());
						//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
						pendingOrderCount = validPendingOrders.size();

						if (pendingOrderCount > 0) {

							FDOrderInfoI orderInfo = (FDOrderInfoI) validPendingOrders.get(0);

					%>
						<div class="index_ordMod_cont" id="index_table_ordModify_1">
		   					<div class="index_ordMod_cont_child">
								<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>" class="orderNumb"><%= orderInfo.getErpSalesId() %></a>
								<span style="padding-left: 30px;"><span class="dow"><%= new SimpleDateFormat("EEEEE").format(orderInfo.getRequestedDate()) %></span> <%= new SimpleDateFormat("MM/dd/yyyy").format(orderInfo.getRequestedDate()) %> 
									<span class="pipeSep">|</span> <%= FDTimeslot.format(orderInfo.getDeliveryStartTime(),orderInfo.getDeliveryEndTime())%></span>
	
								<div class="ordModifyButCont">
									<% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
										<form name="modify_order" id="modify_order" method="POST" action="/your_account/modify_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>&action=modify">
									<% } %>
									<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>" class="">view details</a>&nbsp;
									<% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
											<input type="hidden" name="orderId" value="<%= orderInfo.getErpSalesId() %>" />
											<input type="hidden" name="action" value="modify" />
											<table class="butCont fright" style="margin-left: 10px;">
												<tr>
													<td class="butOrangeLeft"><!-- --></td>
													<td class="butOrangeMiddle"><a class="butText" href="/your_account/modify_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>" onclick="$('modify_order').submit(); return false;">modify order</a></td>
													<td class="butOrangeRight"><!-- --></td>
												</tr>
											</table>
										</form>
												
										
									<% } else { %>
										&nbsp;
									<% } %>
								</div>
							</div>
						</div>
					<% } %>
			<% } %>
	   	<%
	   	}
	   	
		if (location2Media) { %>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" id="index_table_ordModify_2">
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="310" height="6"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="150" height="6"></td>
				</tr>
				<tr>
					<td colspan="2" bgcolor="#ccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				</tr>
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" alt="" /></td>
				</tr>
			</table>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td>
						<fd:IncludeMedia name="/media/editorial/hp_notes/winback/lapsed.html" />
						<% user.setCampaignMsgViewed(user.getCampaignMsgViewed() + 1); %>
					</td>
				</tr>
			</table>
		<% } %>

			<div class="oas_feature_frame" style="padding-top: 10px;">
				<span>
					<script type="text/javascript">
						OAS_AD('HPFeatureTop');
					</script>
				</span>
			</div>			

			<div class="oas_feature_left left">
				<script type="text/javascript">
					OAS_AD('HPFeature');
				</script>
	   		</div>
	   		<div class="oas_feature_right right">
	   			<div class="oas_feature_right_tab">
		   			<script type="text/javascript">
						OAS_AD('HPTab1');
					</script>
				</div>
	   			<div class="oas_feature_right_tab">
		   			<script type="text/javascript">
						OAS_AD('HPTab2');
					</script>
				</div>
	   			<div class="oas_feature_right_tab">
		   			<script type="text/javascript">
						OAS_AD('HPTab3');
					</script>
				</div>
	   			<div class="oas_feature_right_tab">
		   			<script type="text/javascript">
						OAS_AD('HPTab4');
					</script>
				</div>
	   		</div>
	   		<div class="clear" style="font-size: 0px;"></div>
	   		
			<div class="oas_feature_frame">
				<span>
		   			<script type="text/javascript">
						OAS_AD('HPFeatureBottom');
					</script>
				</span>
			</div>			
	   		
	   		<%
	   		StoreModel store = (StoreModel) ContentFactory.getInstance().getContentNode("Store", "FreshDirect");
	   		if (store != null) {
	    		Html edtMed = store.getEditorial();
				if ( edtMed != null ) { %>
				<fd:IncludeHtml html="<%= edtMed %>"/>
			<%
				} else {
					String categoryLinks = FDStoreProperties.getHPCategoryLinksFallback();
					if ( categoryLinks != null ) {
			%><fd:IncludeMedia name="<%= categoryLinks %>"></fd:IncludeMedia><%
					}
				}
			} %>
	   <%-- END MAIN CONTENT--%>
	   		
		<div class="oas_home_bottom">
   			<script type="text/javascript">
				OAS_AD('HPWideBottom');
			</script>
		</div>			
		</div> 
	</div>
</fd:GetSegmentMessage>
</tmpl:put>
</tmpl:insert>