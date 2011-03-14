
<%@page import="java.util.Locale"%>
<%@page import="com.freshdirect.common.pricing.MaterialPrice"%>
<%@page import="com.freshdirect.fdstore.GroupScalePricing"%><%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.customer.ErpPaymentMethodI"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.customer.ErpAddressModel"%>
<%@page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@page import="com.freshdirect.common.address.AddressModel"%>
<%@page import="com.freshdirect.delivery.depot.DlvLocationModel"%>
<%@page import="com.freshdirect.fdstore.FDDepotManager"%>
<%@page import="com.freshdirect.delivery.depot.DlvDepotModel"%><%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.PromotionHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.fdstore.promotion.Promotion' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@page import="com.freshdirect.common.pricing.util.GroupScaleUtil"%>
<%@page import="com.freshdirect.fdstore.FDGroup"%>
<%!
private final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");

%>
<% 
FDUserI user = (FDUserI)session.getAttribute( SessionName.USER );
String groupId = request.getParameter("grpId");
int version = Integer.parseInt(request.getParameter("version"));

JSONObject json = new JSONObject();
json.put("status", "ok");
FDGroup group = new FDGroup(groupId, version);
GroupScalePricing result = GroupScaleUtil.lookupGroupPricing(group);
StringBuffer buf1 = new StringBuffer();
if(result == null){
		buf1.append( " <span class=\"text12rbold\">Sorry Problem loading Group Info</span><br />" );
		json.put("errorInfo", buf1.toString());
		json.put("status", "error");
}
else {
	MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, user.getPricingZoneId());
	buf1.append( " <span class=\"text14rbold\">Buy More &amp; Save!</span><br />" );
	buf1.append( "<span class=\"text12bold\">Any "+result.getLongDesc()+"</span><br />" );
	buf1.append( "<span class=\"text14rbold\">" );
	buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
	if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
		buf1.append(matPrice.getScaleUnit().toLowerCase());
	buf1.append(" or more");
	buf1.append( " for " );
	buf1.append( FORMAT_CURRENCY.format( matPrice.getPrice() ) );
	buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
	buf1.append( "</span><br />" );
	json.put("groupScaleInfo", buf1.toString());
	json.put("status", "ok");
}
%>

 <%=json.toString()%>

