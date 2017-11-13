<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.customer.ejb.EnumCustomerListType"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerListInfo"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerList"%>
<%@ page import="com.freshdirect.fdstore.lists.FDListManager"%>
<%@ page import="com.freshdirect.framework.util.DateUtil"%>
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.fdstore.lists.FDStandingOrderList"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />

<%-- redirect back to old quickshop page if not allowed to see the new (partial rollout check) --%>
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_SO %>"/>

<%  //--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
        
%>

<tmpl:insert template='/quickshop/includes/qs_template.jsp'>
    <tmpl:put name="soytemplates"><soy:import packageName="quickshop"/></tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - Standing Orders</tmpl:put>
    
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="standing_orders"></fd:SEOMetaTag>
	</tmpl:put>
    <tmpl:put name="jsmodules"><%@ include file="/common/template/includes/i_jsmodules.jspf" %><jwr:script src="/qscommon.js" useRandomParam="false" /><script>
    var FreshDirect = FreshDirect || {};

    (function (fd) {
    	"use strict";

    	var $ = fd.libs.$;
    	
    	fd.quickshop.common.tabMeta.update();
    	if (fd.common.tabbedRecommender) {
    		var firstTab = $('[data-component="tabbedRecommender"] [data-tabname]:first-child');
    		fd.common.tabbedRecommender.selectTab($('[data-component="tabbedRecommender"]'),firstTab.data('tabname'),firstTab);
    	}
    }(FreshDirect));
    
    //Show the OAS AD APPDEV-4294 
    $jq(function()
    {
    	showStandardAds();
    });
    
    </script></tmpl:put>
	
    <tmpl:put name='containerClass'>qs-so</tmpl:put>
    <tmpl:put name='soSelected'>selected</tmpl:put>
				   
    <tmpl:put name='extraJs'><%@ include file="/shared/template/includes/ccl.jspf" %> <%-- FIXME: absolute horror, currently needed for some functionality on standing orders pages, an extreme cleanup or replacement would be essential here ... --%>
    </tmpl:put>

    <tmpl:put name='searchbox'>
    </tmpl:put>

    <tmpl:put name='pagination'>
    </tmpl:put>

    <tmpl:put name='menu' direct='true'>
    </tmpl:put>

    <tmpl:put name='ymalcarousel'>
    </tmpl:put>

    <tmpl:put name='sort'>
    </tmpl:put>

    <tmpl:put name='listheader'>
    </tmpl:put>

    <tmpl:put name='listactions'>
    </tmpl:put>

    <tmpl:put name='content' direct='true'>
    
	
		<div id="inner-container">
			
			<img style="margin: 35px 0px 20px 0px" src="/media_stat/images/template/quickshop/about_so.png" width="328" height="32" alt="About Standing Order">			
			<div style="margin: 0px 6px 30px 6px">
			
				<div style="margin-bottom: 1em">FreshDirect's Standing Orders let you get the foods you love without lifting a finger. They're simple to set up and update anytime. We automatically submit your order seven days before the Modify Next Delivery Only, and send you an email notification. That way you have plenty of time to make changes.</div>
				<div style="margin-bottom: 1em">Once your first Standing Order has been delivered, you can come back to "Reorder" and make changes for future deliveries. And if you ever need help, you can always <a href="/help/index.jsp">contact us</a>.</div>
				<div><b>Create a new Standing Order below!</b></div>
			</div>


			<% if (user.isEligibleForStandingOrders()) { %>			
				
				<fd:ManageStandingOrders id="sorders">
					<% 
					boolean firstSO = true;
					int ctr = 0; //for zebra coloring
					for (FDStandingOrder so : sorders) {
						
						if (firstSO){
							firstSO = false;
							%>
							<table width="100%" bgcolor="#333333" style="margin-bottom: 18px;">
							<tr><td><img src="/media_stat/images/template/quickshop/your_standing_orders2.png" width="152" height="22" /></td>
							<td>
								<fd:GetStandingOrderHelpInfo id="helpSoInfo">
									<script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
									<a class="text13" style="float: right; text-align: right; color: white; vertical-align: middle; padding-right:7px;" href="/unsupported.jsp" onClick="return CCL.help_so(helpSoInfo, this);">Need Help?</a>
								</fd:GetStandingOrderHelpInfo>
							</td></tr>
							</table>
							<%
						}
						
						String listName = FDListManager.getListName( user.getIdentity(), so.getCustomerListId() );
						FDStandingOrderList list = (FDStandingOrderList) FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.SO, listName);
					    int n = list.getLineItems().size();
			
					    ErpAddressModel addr = so.getDeliveryAddress();		
						final String nextDlvDateText = new SimpleDateFormat("EEEE, MMMM d.").format( so.getNextDeliveryDate() );
						String bgColor = (ctr++%2==0) ? "background-color:#ececec;" : ""; //zebra coloring
						%>
						<div style="margin:0em;padding:0em 1em 1em 0.5em;<%= bgColor %>">
							<h2 style="margin: 5em 5m 5em 5em;"><a class="title15" href="<%= so.getLandingPage() %>"><%=StringUtil.escapeHTML(so.getCustomerListName())%></a></h2>
							<div class="title11" style="margin: 1em 1em 0em;">
								
								<% if (so.isError()) { %>				
									<div style="padding: 1em 0 1em 0">
										<div style="color: #CC3300">IMPORTANT NOTE: We were not able to schedule a delivery for <%= nextDlvDateText %></div>
										<div style="margin-top: 1em; color: #CC3300"><%=so.getErrorHeader()%></div>
									</div>
								<% } else { %>				
									<div style="text-align: left; font-weight: bold;">
										<%@ include file="/quickshop/includes/so_next_delivery.jspf" %>
										<div style="padding: 1em 0 1em 0">
										<div style="font-weight: normal;">Delivered <%= so.getFrequencyDescription() %>, <%= StandingOrderHelper.getDeliveryDate(so,false) %></div>
											<% if (addr != null) { 
												String apartment = addr.getApartment();
												if (null == apartment || apartment.length()==0){
													apartment = "";
												} else {
													apartment = ", " + apartment;
												}
											%>
												<div style="font-weight: normal;"><%= addr.getScrubbedStreet() %><%=apartment%></div>
											<% } %>				
										</div>					
									</div>
								<% } %>
												
								<a class="title12" href="<%= so.getLandingPage() %>">View or Edit Details &raquo;</a>
							</div>
						</div>
						<div style="margin:1em;"></div>
					<% } %>
				</fd:ManageStandingOrders>
				
				<hr style="margin: 2em 0 1em 0; width: 100%; height: 1px; background-color: #996699; color: #996699; line-height: 1px; border: none;"/>	
				
				<fd:CreateStandingOrder result="result">
					<div id="create-so-box" style="padding: 1em 1em">
						<div class="title11" style="margin-bottom: 0.5em;">Start a new standing order:</div>
						<fd:ErrorHandler result='<%= result %>' field='<%= new String[]{"SO_NAME", "SO_FREQ"} %>' id='errorMsg'>
							<%@ include file="/includes/i_error_messages.jspf" %>
						</fd:ErrorHandler>
						<form method="POST">
							<input type="hidden" name="action" value="create">
							<table style="width: 100%; border: 0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="text9">Name:</td>
									<td style="width: 20px">&nbsp;</td>
									<td class="text9">Deliver my order:</td>
									<td style="width: 20px">&nbsp;</td>
									<td></td>
								</tr>
								<tr>
									<td><input name="soName" type="text"></input></td>
									<td style="width: 20px">&nbsp;</td>
									<td>
										<select name="soFreq">
											<% for (EnumStandingOrderFrequency frqItem : EnumStandingOrderFrequency.values()) { %>
												<option value="<%= frqItem.getFrequency() %>"><%= frqItem.getTitle() %></option>
											<% } %>								
										</select>
									</td>
									<td style="width: 20px">&nbsp;</td>
									<td>
										<input type="image" src="/media_stat/images/buttons/so_start_shopping.gif" alt="Start shopping"/>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</fd:CreateStandingOrder>
			<% } %>	

		</div>
    </tmpl:put>
    
</tmpl:insert>