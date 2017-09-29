<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>
<%@ page import="com.freshdirect.fdstore.ecoupon.model.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.FDProductInfo" %>
<%@ page import="com.freshdirect.fdstore.FDCachedFactory" %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.framework.util.*''%> 
<%@ page import='com.freshdirect.fdstore.customer.*''%> 
<%@ page import='com.freshdirect.fdstore.ecoupon.*''%>
<%@ page import='com.freshdirect.dataloader.ecoupon.*''%>
<%@ page import="java.util.*" %>

<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ include file="/shared/template/includes/yui.jspf" %>

<fd:CheckLoginStatus guestAllowed='true' />
<html lang="en-US" xml:lang="en-US">
<head>
	<title>FDCoupon Test Page</title>
	
	<script type="text/javascript">
	function doClip(couponId) {
		
		YAHOO.util.Connect.asyncRequest('GET', '/api/cp_api.jsp?action=clip&cpid='+couponId, {
			  success: function(o) { alert(o.responseText);},
			  failure: function(o) { alert(o.responseText);},
			  argument: couponId
			});
	}
</script>

</head>
<body>
<form method="POST">
    Refresh Coupons Cache: <input type="submit" name="refresh" value="REFRESH"/>
    </form>
<%!
    String getProducts(List<FDCouponUPCInfo> upcs) {
		StringBuffer result = new StringBuffer();
		if(upcs != null) {
			String upcA = null;
			for(FDCouponUPCInfo upc : upcs) {
				upcA = StringUtil.convertToUPCA(upc.getUpc());
				FDProductInfo cachedProductInfo = FDCachedFactory.getProductInfoByUpc(upcA);
				if(cachedProductInfo != null && cachedProductInfo.getSkuCode() != null && cachedProductInfo.getSkuCode().length() > 0) {
					result.append(upc.getUpc()).append(" [").append(cachedProductInfo.getSkuCode()).append("] ").append(",");
				}
				
			}
		}
      	return result.toString();
    }
    %>
    
<%	if("REFRESH".equals(request.getParameter("refresh"))){
		FDCouponFactory.getInstance().forceRefresh();
	}
	long startTime = System.currentTimeMillis();
	FDCouponActivityContext couponActivityContext =AccountActivityUtil.getCouponActivityContext(session);
	List<FDCouponInfo> coupons = FDCouponFactory.getInstance().getCoupons();//FDCouponManager.loadCoupons(couponActivityContext);
	//FDCouponMetaDataCronRunner.main(null);
	long endTime = System.currentTimeMillis();
%>

<table style="border-width:1px;border-spacing:0px;border-style:outset;border-color:red;" cellpadding="5">
	<caption><i>All Active Coupons</i> <b>(Execution Time: <%= (endTime - startTime) %> ms)</b></caption>
	<tr>
		<th>&nbsp;</th>
		<th>ID</th>
		<th>CATEGORY</th>	
		<th>S_DESC</th>				
		<th>REQ_DESCRIPTION</th>		
		<th>EXP_DATE</th>
		<th>ENABLED</th>
		<th>VALUE</th>
		<th>REQ_QNT</th>
		<th>REQ_UPC</th>
	</tr>
	<% if(coupons != null) { 
		for(FDCouponInfo coupon : coupons) {
	%>
		<tr>
			<td><input type="checkbox" id="<%=coupon.getCouponId() %>" onclick="doClip(this.id)" /></td>
			<td><%=coupon.getCouponId() %></td>
			<td><%=coupon.getCategory() %></td>			
			<td><%=coupon.getShortDescription() %></td>						
			<td><%=coupon.getRequirementDescription() %></td>		
			<td><%=coupon.getExpirationDate() %></td>
			<td><%=""+coupon.isEnabled() %></td>
			<td><%=coupon.getValue() %></td>
			<td><%=coupon.getRequiredQuantity() %></td>
			<td style="width:400px;font-size:10px"><%=getProducts(coupon.getRequiredUpcs()) %></td>
		</tr>
	<% 	}
	 } %>
</table>
<br/>
<%
	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);	
	startTime = System.currentTimeMillis();
	FDCustomerCouponWallet customerCoupon = FDCustomerCouponUtil.getCustomerCoupons(session);
	endTime = System.currentTimeMillis();
%>

<table style="border-width:1px;border-spacing:0px;border-style:outset;border-color:red;" cellpadding="5">
	<caption><i>Customer Coupons</i> <b>(Execution Time: <%= (endTime - startTime) %> ms)</b></caption>
	<tr>
		<th style="width:350px;">Available</th>
		<th style="width:350px;">Clipped/Active</th>
		<th style="width:350px;">Clipped/Redeemed</th>
		<th style="width:350px;">Clipped/Pending</th>
		<th style="width:350px;">Clipped/Expired</th>
	</tr>
	<% if(customerCoupon != null) {%>
		<tr>
			<td><%=customerCoupon.getAvailableIds() %></td>
			<td><%=customerCoupon.getClippedActiveIds() %></td>
			<td><%=customerCoupon.getClippedRedeemedIds() %></td>			
			<td><%=customerCoupon.getClippedPendingIds() %></td>					
			<td><%=customerCoupon.getClippedExpiredIds() %></td>
		</tr>	<% 	
	 } %>
		
	</table>
	<br/>
<%		
	startTime = System.currentTimeMillis();
	FDCustomerCouponUtil.evaluateCartAndCoupons(session);
	endTime = System.currentTimeMillis();
%>	
<fd:GetCart id='cart'>
	<table style="border-width:1px;border-spacing:0px;border-style:outset;border-color:red;" cellpadding="5">
		<caption><i>Customer Cart</i> <b>(Execution Time: <%= (endTime - startTime) %> ms)</b></caption>
		<% for (FDCartLineI orderLine : cart.getOrderLines()) { %>
		<tr>
			<td style="width:250px;"><%= orderLine.getSku().getSkuCode()%></td>
			<td style="width:600px;"><%= orderLine.getDescription() %></td>
			<td style="width:300px;"><%= orderLine.getUpc() %></td>
			<td style="width:200px;"><%= orderLine.getPrice() %></td>
			<td style="width:200px;"><%= orderLine.getQuantity() %></td>
			<td style="width:200px;"><%= orderLine.getCouponStatus() != null ? orderLine.getCouponStatus().getDescription() : "" %></td>
		</tr>		
			
		<% } %>
	</table>
</fd:GetCart>

</body>
</html>
