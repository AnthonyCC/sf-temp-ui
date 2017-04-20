<%@ page import="java.util.*"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.fdstore.referral.ReferralPromotionModel"%>
<%@ page import = "com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.EnumEStoreId' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>

<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<% //expanded page dimensions
final int W_YA_MANAGE_TOTAL = 970;
final int W_YA_CSICON = 40;
final int W_YA_MAIN = 601;
final int W_YA_CT = 328;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
boolean selectedAddressIsHome = (user != null && (EnumServiceType.HOME).equals(user.getSelectedServiceType())) ? true : false;
boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/no_nav.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
}
%>
<%!
private String getTimeslotString(Calendar startTimeCal, Calendar endTimeCal){
		StringBuffer sb = new StringBuffer();
		int startHour = startTimeCal.get(Calendar.HOUR_OF_DAY);
		sb.append(startHour==12 ? "noon" : (startHour > 12 ? startHour - 12+"": startHour+""));
		int startMin = startTimeCal.get(Calendar.MINUTE);
		if(startMin != 0){
			sb.append(":"+startMin);
		}
		sb.append(startTimeCal.get(Calendar.AM_PM)==1?" PM":" AM");
		
		sb.append(" - ");
		
		int endHour = endTimeCal.get(Calendar.HOUR_OF_DAY); 
		sb.append(endHour == 0 ? "12" : (endHour == 12 ? "noon" : (endHour > 12 ? endHour - 12+"" : endHour+"")));
		int endMin = endTimeCal.get(Calendar.MINUTE);
		if(endMin != 0){
			sb.append(":"+endMin);
		}
		sb.append(endTimeCal.get(Calendar.AM_PM)==1?" PM":" AM");
		return sb.toString();
	}
%>
<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account</tmpl:put>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="manage_account"></fd:SEOMetaTag>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<jwr:style src="/your_account.css" media="all"/>
<% 
DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");

        //--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/your_account");
request.setAttribute("listPos", "SystemMessage,CategoryNote");

%>

<%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){%>
			<script type="text/javascript">
			if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) {
				
				FreshDirect.components.ifrPopup.open({ url: '/registration/tcaccept_lite.jsp?successPage=nonIndex', width: 320, height: 400, opacity: .5}); 
				} else {
					
				doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>');
			}
			
			</script>
<%}%>
<fd:OrderHistoryInfo id='orderHistoryInfo'>		
<%  if (orderHistoryInfo != null && orderHistoryInfo.size() != 0) {
		for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
		    FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
			if (orderInfo.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) { 
			    Calendar dlvStart = Calendar.getInstance();
				dlvStart.setTime(orderInfo.getDeliveryStartTime());
				Calendar dlvEnd = Calendar.getInstance();
				dlvEnd.setTime(orderInfo.getDeliveryEndTime());

				String deliveryTime = getTimeslotString(dlvStart, dlvEnd);	
				String errorMsg= "We were unable to deliver your order (#"+orderInfo.getErpSalesId()+") scheduled for between "+deliveryTime+" on "+dateFormatter.format( orderInfo.getRequestedDate() )+". Please contact us as soon as possible at "+user.getCustomerServiceContact()+" to reschedule delivery.";
				ActionResult result = new ActionResult();
			%>
			<%@ include file="/includes/i_error_messages.jspf"%>
			<% } %>
<%          break;
		}
	}
%>
</fd:OrderHistoryInfo>
<div class="manage-account-container">
<div class="manage-account-heading">
    <h1 tabindex="0" class="title18 text-left no-margin"><%= mobWeb ? "" : "Welcome to " %>Your Account</h1>
    <p class="text13 text-left no-margin">If you need to make any changes or updates to your account information, this is the place to do it.</p>
</div>

<table width="<%= mobWeb ? "100%" : W_YA_MANAGE_TOTAL %>" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="<%= mobWeb ? "100%" : W_YA_MAIN %>" valign="top" >
<!-- ct user logo -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td valign="top" class="text13">
	<% if(user.isChefsTable()) { %>
	<table align="left" border="0" cellspacing="0" cellpadding="0">
	<tr valign="top">
		<td><img src="/media_stat/images/template/checkout/loy_ctcard_top.gif" alt="Chef's Table Member Card"/></td>
	</tr>
	<tr>
		<td style="color: white; background: #221e1f; left: inherit; text-align: center;">
			<%= user.getFirstName() %> <%= user.getLastName() %>
			<br/>
			<% if(user.isChefsTable() && !user.getChefsTableInduction().equals("0") && user.getChefsTableInduction().length() == 8) { %>
				Since <%= user.getChefsTableInduction().substring(0,4) %>
			<% }%>
			<br/>
			<br/>
		</td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/template/checkout/loy_ctcard_bot.gif" alt="" /></td>
	</tr>
	</table>
	<% } %>
    </td></tr><tr>
	<td valign="top" class="text13 manage_account"> <% if(user.isChefsTable()) { %> <br><br> <% } %>
		<font class="text13bold"><a href="<%=response.encodeURL("/your_account/order_history.jsp")%>"><%= mobWeb ? "Order History" : "Your Orders" %></a></font><br><span class="manage_account_desc">Check your order status and update open orders.</span><br>
		<a id="yourAccount_reorder" href="/quickshop/qs_past_orders.jsp"><div class="new_purple_button_style"><div id="reorder-icon-big"></div><%= mobWeb ? "Last Order" : "<b>Reorder</b> from Past Orders" %></div></a>
		<br>
		
		<% if ( user.isEligibleForStandingOrders() &&  !mobWeb) { %>
			<font class="text13bold">
			<a href="<%=response.encodeURL("/quickshop/standing_orders.jsp")%>">Standing Orders</a>
			</font><br><span class="manage_account_desc">Review your recurring orders and make changes.</span>
			<br><br>
		<%}%>
		<%if(user.isEligibleForPreReservation()){%>
			<div class="<%= (!selectedAddressIsHome) ? "selectedAddressIsHome-false": "" %>">
				<% if (selectedAddressIsHome) { %><a href="<%=response.encodeURL("/your_account/reserve_timeslot.jsp")%>"><% } %><span class="text13bold">Reserve a Delivery Time</span><% if (selectedAddressIsHome) { %></a><% } %>
				<% if (!selectedAddressIsHome) { %><span class="manage_account_desc">(Only available for Home Delivery)</span><% } %>
				<div class="manage_account_desc">Reserve your delivery timeslot before you place your order.</div>
			</div>
			<br />
		<%}%>
		<%if(user.isEligibleForDeliveryPass() && !EnumEStoreId.FDX.equals(user.getUserContext().getStoreContext().getEStoreId()) && !mobWeb){%>
			<div class="<%= (!selectedAddressIsHome) ? "selectedAddressIsHome-false": "" %>">				
				<% if (selectedAddressIsHome) { %><a href="<%=response.encodeURL("/your_account/delivery_pass.jsp")%>"><% } %><span class="text13bold">FreshDirect DeliveryPass</span><% if (selectedAddressIsHome) { %></a><% } %>
				<% if (!selectedAddressIsHome) { %><span class="manage_account_desc">(Only available for Home Delivery)</span><% } %>
				<div class="manage_account_desc">See your membership and renewal details.</div>
			</div>
			<br />
		<%}%>
		
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/delivery_information.jsp")%>">Delivery Addresses</a>
		</font><br>
		<span class="manage_account_desc">Update your delivery address information.</span>
		<br><br>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/payment_information.jsp")%>">Payment Options</a>
		</font><br>
		<span class="manage_account_desc">Update your payment information or add a new payment option.</span>
		<br><br>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/signin_information.jsp")%>"><%= mobWeb ? "" : "Your " %>Account Preferences</a>
		</font><br>
		<span class="manage_account_desc">Change your user name, password, and other account preferences.</span>
		<br><br>
		<% if (!FDStoreProperties.isEmailOptdownEnabled()) { %>
			<%-- <a href="<%=response.encodeURL("/your_account/newsletter.jsp")%>"><font class="text13bold">President's Picks Newsletter</a></font><br />
				Subscribe to the President's Picks alert to get each week's deals delivered to your inbox.<br /><br /> --%>
		<%}%>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/reminder_service.jsp")%>">Reminder Service</a>
		</font><br>
		<span class="manage_account_desc">Change your e-mail reminder preferences.</span>
		<br><br>
		<% if (!mobWeb){ %>
        <font class="text13bold">
		<a href="<%=response.encodeURL("/quickshop/all_lists.jsp")%>">Your Shopping Lists</a> </font>
		<br>
		<span class="manage_account_desc">Visit "Reorder" to view, edit and shop with your shopping lists.</span>
		<br><br>
        <font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/customer_profile_summary.jsp")%>">Your Profile</a> </font>  <font class="text13bold" color="#990000"></font>
		<br>
		<span class="manage_account_desc">Tell us your food preferences.</span>
		<br><br>
		<% } %>
		<% 
			/*
				Changing to site feature
				if(FDStoreProperties.isGiftCardEnabled()) { 
			*/
			if(user.isGiftCardsEnabled() && !mobWeb) {
		%>
				<a href="<%=response.encodeURL("/your_account/giftcards.jsp")%>" class="text13bold">Gift Cards</a>
				<br>
				<span class="manage_account_desc">View your history of received and purchased FreshDirect Gift Cards.</span>
				<br><br>
		<% } %>
		<% if(!FDStoreProperties.isExtoleRafEnabled() && user !=null && user.isReferralProgramAvailable()) { 
			FDIdentity customerIdentity = null;
			if (user!=null && user.getLevel() == 2){
				customerIdentity = user.getIdentity();
			}
			ReferralPromotionModel rpModel = FDReferralManager.getReferralPromotionDetails(customerIdentity.getErpCustomerPK());
			if(rpModel != null) {
		%>
		
        <font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/brownie_points.jsp")%>">Refer A Friend</a> </font>  <font class="text13bold" color="#990000"></font>
		<br>
		<span class="manage_account_desc">Invite your friends and earn $$$ credits.</span>
		<br><br>
		<% } }else if(FDStoreProperties.isExtoleRafEnabled() && user.isReferralProgramAvailable()) { %>
		 <font class="text13bold">
		<a href="<%=response.encodeURL(FDStoreProperties.getPropExtoleMicrositeUrl())%>" target="_blank">Refer A Friend</a> </font>  <font class="text13bold" color="#990000"></font>
		<br>
		<span class="manage_account_desc">Invite your friends and earn $$$ credits.</span>
		<br><br>
		<% }  %>	<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/credits.jsp")%>">Account Credits</a> </font>  <font class="text13bold" color="#990000"></font>
		<br>
		<span class="manage_account_desc">View your credit balance and credit history.</span>
		<br><br>
	</td>
	<td width="30" class="NOMOBWEB"><img src="/media_stat/images/layout/clear.gif" ALT="" width="30" height="1"></td>
	</tr>
</table>
</td>
<% if(!user.isChefsTable() && (user.isOkayToDisplayCTEligibility() && !user.hasQualifiedForCT())) { %>
<TD width="20"><img src="/media_stat/images/layout/clear.gif" alt="" width="20" height="1"></td>
<TD width="1" bgcolor="#ff9900"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
<TD width="20"><img src="/media_stat/images/layout/clear.gif" alt="" width="20" height="1"></td>
<% } %>
<td valign="top" width="<%= W_YA_CT %>" class="NOMOBWEB">
	<!-- ct user logo -->
	<% 
    if(!user.isChefsTable() && user.isOkayToDisplayCTEligibility() && !user.hasQualifiedForCT()) {
    
%>

 	
	<table align="center" border="0" cellspacing="0" cellpadding="0" width="<%= W_YA_CT %>">
		<tr valign="top">
			<td valign="top" class="text13">
			<td><img src="/media_stat/images/chefs_table/ct_almost_hdr.gif"/><br><br></td>
		</tr>
	</table>
	
	<table align="center" border="0" cellspacing="0" cellpadding="0" width="<%= W_YA_CT %>">
		<tr valign="top">
			<td valign="top" class="text13">We enroll new members in our Chef's Table rewards program every month. 
			<br><br><font class="text13bold">
			<% if(user.isCloseToCTEligibilityByOrderCount() && user.isCloseToCTEligibilityByOrderTotal()) { %>
				Simply receive <%=user.getOrderCountRemainingForChefsTableEligibility()%> more <%=(user.getOrderCountForChefsTableEligibility() < 11 ? "orders" :"order" )%> or spend <%= user.getOrderTotalRemainingForChefsTableEligibility() %> by <%= user.getEndChefsTableQualifyingDate() %> to qualify!</td>
			<% } else if( user.isCloseToCTEligibilityByOrderCount() && !user.isCloseToCTEligibilityByOrderTotal()) { %>
				Simply receive <%=user.getOrderCountRemainingForChefsTableEligibility()%> more <%=(user.getOrderCountForChefsTableEligibility() < 11 ? "orders" :"order" )%> by <%= user.getEndChefsTableQualifyingDate() %> to qualify!</td>
			<% } else if( !user.isCloseToCTEligibilityByOrderCount() && user.isCloseToCTEligibilityByOrderTotal()) { %>
				Simply spend <%= user.getOrderTotalRemainingForChefsTableEligibility() %> by <%= user.getEndChefsTableQualifyingDate() %>, to qualify!</td>
			<% } %>
			</font>
			<td valign="top" colspan="2"><img src="/media_stat/images/chefs_table/ct_almost_card.gif"/></td>
		</tr>
		<tr valign="top">
			<td valign="top" class="text13" colspan="2"><br>There are no fees, no forms and no hidden costs. <font class="text13bold">
				<a href="javascript:popup('/shared/template/generic_popup.jsp?contentPath=/media/brands/fd_chefstable/fd_chefstable_pop.html&windowSize=large&name=Chefs+Table+Almost','large')"><b>Click here to learn more.</b></a>
				</font> 
			</td>
		</tr>
	</table><br>
<%  }
	if(user.isChefsTable() || (user.isOkayToDisplayCTEligibility() && !user.hasQualifiedForCT())) {
		%>
		<table align="center" border="0" cellspacing="0" cellpadding="0" width="<%= W_YA_CT %>">
		<tr valign="top">
			<td valign="top" class="text13">
			<% if(!user.isChefsTable() && user.isOkayToDisplayCTEligibility()) { %>
				<fd:IncludeMedia name="/media/editorial/site_pages/account/right_ct_new.html" />
			<% } else if( user.isChefsTable()) { %>
				<fd:IncludeMedia name="/media/editorial/site_pages/account/right_ct.html" />
			<% } %>
			</td>
		</tr>
		</table>
	<% }else{ %>
		<table align="center" border="0" cellspacing="0" cellpadding="0" width="<%= W_YA_CT %>">
		<tr valign="top">
			<td valign="top" class="text13">
		<fd:IncludeMedia name="/media/editorial/site_pages/account/right_all.html" />
		</td>
		</tr>
		</table>
        <% } %>
</td>
</tr>
</table>
<div class="continue-shopping text-left">
  <a class="no-decor" href="/index.jsp">
    <div class="col-left">
      <button class="cssbutton green icon-arrow-left-before notext round"></button>
    </div>
    <div class="col-right">
      <p><span class="continue-shopping-label">Continue Shopping</span></p>
      <p>from <strong>Home Page</strong></p>
    </div>
  </a>
</div>
</div>
 </tmpl:put>
</tmpl:insert>
