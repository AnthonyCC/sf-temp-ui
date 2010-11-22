<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.util.JspLogger"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.delivery.restriction.EnumDlvRestrictionReason' %>
<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.atp.*" %>
<%@ page import='com.freshdirect.common.pricing.MunicipalityInfo' %>
<%@ page import="com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo" %>
<%@ page import="com.freshdirect.fdstore.atp.FDStockAvailabilityInfo" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/template/top_nav.jsp'>

<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>
<tmpl:put name='title' direct='true'>Checkout > Items unavailable</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDCartModel cart = user.getShoppingCart();

// get map of cartLineId -> unav FDAvailabilityInfos
Map<String,FDAvailabilityInfo> invsInfoMap = cart.getUnavailabilityMap();
Date day = null;
if(invsInfoMap.size() > 0 ){
%>

<div class="content_fixed" align="center">
<table width="690">
<tr><td><span class="title18">Some Items Unavailable on <%= CCFormatter.formatRequestedDate(cart.getDeliveryReservation().getStartTime()) %></span></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="693" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"><br>
</td></tr>
<tr><td>
	<span class="text11">
		We're sorry, but some items are not available for delivery on <%= CCFormatter.formatRequestedDate(cart.getDeliveryReservation().getStartTime()) %>.
		If you choose to continue, the items below will be adjusted to the quantity we can deliver.
		<b>Everything else in your cart is available.</b>
		
		<br><br>
		
		<span class="error">
			All items will remain in your cart until you click below to Continue Checkout.
		</span>

		
		<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4">
	</span>
</td></tr>
</table>
	
<br><br>
<table BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="657">
<tr VALIGN="TOP">
<td WIDTH="320" COLSPAN="2" CLASS="text12bold">Unavailable for <%= CCFormatter.formatRequestedDate(cart.getDeliveryReservation().getStartTime()) %> <%= FDTimeslot.format(cart.getDeliveryReservation().getStartTime(), cart.getDeliveryReservation().getEndTime()) %><br></td>
<td WIDTH="17"><br></td>
<td WIDTH="320" COLSPAN="2" CLASS="text12bold">Details<br></td>
</tr>
<% // start of grouping logic --
Map<String,List<String>> groupingMap = new HashMap<String,List<String>>();
List<String> regularItems = new ArrayList<String>();
List<String> groupingKeyList = new ArrayList<String>();

for (String key : invsInfoMap.keySet()) {
	FDCartLineI cartLine = cart.getOrderLineById( Integer.parseInt(key) );
	String rcpSrcId= cartLine.getRecipeSourceId();
	if (rcpSrcId!=null) {
	 	String deptDecs=cartLine.getDepartmentDesc();
		List<String> rcpItems = (List<String>)groupingMap.get(deptDecs);
		if (rcpItems==null) {
		   rcpItems= new ArrayList<String>();
		   groupingMap.put(deptDecs,rcpItems);
		   groupingKeyList.add(deptDecs);
		}
		rcpItems.add(key);
	} else {
		regularItems.add(key);
	}
}

if (regularItems.size()>0) {
  groupingKeyList.add("nonRecipeItems");
  groupingMap.put("nonRecipeItems",regularItems);
}

String lastGroupKey="";
%> 

<logic:iterate collection="<%=groupingKeyList%>" id="groupKey" type="java.lang.String">
<%
	if (!lastGroupKey.equals(groupKey)) {  %>
		<tr valign="top"><td colspan="5" class="text11orbold">
		  <%="".equals(lastGroupKey) ? "" : "<br>"%>
		  <%="nonRecipeItems".equals(groupKey) ? "<br>Other Unavailable Items" :"From: Recipe: "+groupKey.substring("Recipe: ".length()).toUpperCase()%>
		  <br><br>
		</td></tr>		
<%		lastGroupKey=groupKey;  		
        }    
%>

   <logic:iterate collection="<%=groupingMap.get(groupKey)%>" id="key" type="java.lang.String">
	<%FDCartLineI cartLine = cart.getOrderLineById(Integer.parseInt(key));
	  FDAvailabilityInfo info = (FDAvailabilityInfo)invsInfoMap.get(key);
	%>
	<tr VALIGN="TOP">
	<td WIDTH="30">&nbsp;&nbsp;<b><%= CCFormatter.formatQuantity( cartLine.getQuantity() ) %></b></td>
	<td WIDTH="280"><%=cartLine.getDescription() +"("+cartLine.getConfigurationDesc()+")"%></td>
	<td WIDTH="17">&nbsp;</td>
	<td WIDTH="5">&nbsp;&nbsp;</td>
	<td WIDTH="315">
		<%
		if (info instanceof FDRestrictedAvailabilityInfo) {
			EnumDlvRestrictionReason rsn = ((FDRestrictedAvailabilityInfo)info).getRestriction().getReason();
			if (EnumDlvRestrictionReason.KOSHER.equals(rsn)) {
				%><a href="javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')">Kosher production item</a> - not available Fri, Sat, Sun AM, and holidays<%
			} else {
				%><%= ((FDRestrictedAvailabilityInfo)info).getRestriction().getMessage() %><%
			}
		} else if(info instanceof FDStockAvailabilityInfo){%>
			<b><%=CCFormatter.formatQuantity(((FDStockAvailabilityInfo)info).getQuantity())%> Available</b>
		<%} else if (info instanceof FDMuniAvailabilityInfo) {
			MunicipalityInfo muni = ((FDMuniAvailabilityInfo)info).getMunicipalityInfo();%>
			FreshDirect does not deliver alcohol outside NY.
		<% } else { %>
			Out of stock
		<% } %>
	</td>
	</tr>
  </logic:iterate>
</logic:iterate>

<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0"></td></tr>
<tr>
	<td colspan="5">
	<br>
	</td>
</tr>
<% 
day = cart.getFirstAvailableDate();
if(day != null){
%>
	<tr>
		<td colspan="5">
			<b>The first day on which all items will by available is <%=CCFormatter.formatRequestedDate(day)%></b>
		</td>
	</tr>
	<tr>
		<td colspan="5">
		<br>
		</td>
	</tr>
<%}%>
</table>
<%}%>

<%-- DELIVERY PASS BLOCK BEGIN--%>
<table>
<% 
	List unavailPasses = cart.getUnavailablePasses();
	if (unavailPasses != null && unavailPasses.size() > 0) { 
%>
	<tr valign="top"><td colspan="5" class="text11orbold">DeliveryPass</td></tr>
	<tr><td class="text11"colspan="5">
			We're sorry, the pass(es) below will be removed from your cart for the following reason(s).
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4">
	</td></tr>

	<logic:iterate collection="<%= unavailPasses %>" id="info" type="com.freshdirect.deliverypass.DlvPassAvailabilityInfo">
		<%
			Integer key = info.getKey();
			FDCartLineI cartLine = cart.getOrderLineById(key.intValue());
		%>
		<tr VALIGN="TOP">
		<td WIDTH="30">&nbsp;&nbsp;<b><%= quantityFormatter.format( cartLine.getQuantity() ) %></b></td>
		<td WIDTH="280">
			<b><%=cartLine.getDescription() %></b>
			<%= "".equals(cartLine.getConfigurationDesc()) ? "" : "(" + cartLine.getConfigurationDesc() + ")" %>
		</td>
		<td WIDTH="17">&nbsp;</td>
		<td WIDTH="5">&nbsp;&nbsp;</td>
		<td WIDTH="315">
		<b><%= info.getReason() %></b>
		</td>
		</tr>
		
	</logic:iterate>	

<% } %>
</table>
<%-- DELIVERY PASS BLOCK END--%>

<img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="693" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"><br>
<table border="0" cellspacing="0" cellpadding="0" width="675">
	<tr valign="TOP">
		<td width="25"><a href="/order/place_order_build.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" width="28" height="28" alt="" border="0"></a></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
		<td width="350">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
			<a href="/order/place_order_build.jsp"><img src="/media_stat/images/template/checkout/shop_for_replacements.gif" width="140" height="9" alt="" border="0"></a><br>
			Everything else in your cart is available.
		</td>
		<td></td>
		<td width="265" align="RIGHT" valign="MIDDLE">
			<a href="/checkout/checkout_ATP_adjust.jsp?successPage=<%=request.getParameter("successPage")%>"><img src="/media_stat/images/buttons/continue_checkout.gif" width="117" height="9" border="0" alt="CONTINUE CHECKOUT" vspace="0"></a>
			<br>Items will be removed from your cart<br>
		</td>
		<td width="35" align="RIGHT" valign="MIDDLE">
			<font class="space2pix"><br></font><a href="/checkout/checkout_ATP_adjust.jsp?successPage=<%=request.getParameter("successPage")%>"><img src="/media_stat/images/buttons/checkout_arrow.gif" width="29" height="29" border="0" alt="CONTINUE CHECKOUT" vspace="0"></a>
		</td>
	</tr>
</table>
<br><br>

</div>
</tmpl:put>
</tmpl:insert>
