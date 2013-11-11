<%@page import="com.freshdirect.webapp.util.ShoppingCartUtil"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.atp.*" %>
<%@ page import="com.freshdirect.fdstore.util.*" %>
<%@ page import="com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.delivery.restriction.EnumDlvRestrictionReason' %>
<%@ page import='com.freshdirect.common.pricing.MunicipalityInfo' %>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.commons.lang.StringUtils'%>
<%@ page import='com.freshdirect.fdstore.util.TimeslotLogic'%>


<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_2_UNAVAIL_TOTAL = 970;
%>

<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Unavailability</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
java.text.DateFormat dateFormatter = new java.text.SimpleDateFormat("EEEEE, MMM d");
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDCartModel cart = user.getShoppingCart();
FDReservation reservation = cart.getDeliveryReservation();

//button include count
int incNextButtonCount = 0;

// get map of cartLineId -> unav FDAvailabilityInfos
Map<String,FDAvailabilityInfo> invsInfoMap = cart.getUnavailabilityMap();
Date day = null;
if(invsInfoMap.size() > 0 ){
%>
<table width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
<tr><td><span class="title18">Some Items Unavailable for <%= dateFormatter.format(reservation.getStartTime()) %></span></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"><br>
</td></tr>


<% 
Double subTotal = null;


FDCartModel clonedCart = new FDCartModel( cart );
clonedCart.setUnavailablePasses(cart.getUnavailablePasses());
clonedCart.setAvailability(cart.getAvailability());
subTotal = ShoppingCartUtil.getSubTotal(clonedCart);
TimeslotLogic.applyOrderMinimum(user, reservation.getTimeslot(), subTotal);		
	
if(subTotal!=null && subTotal < reservation.getMinOrderAmt()) {
	String view_cart_redir = "/checkout/view_cart.jsp";
	String timeslot_redir = "/checkout/step_2_select.jsp";
%>
<%@ include file="/shared/includes/delivery/i_variable_unavail_minnotmet_messages.jspf"%> 
<% } 
else{%>

<tr><td class="text11">
		We're sorry, but some items are not available for delivery on <%= dateFormatter.format(reservation.getStartTime()) %>.
		If you choose to continue, the items below will be adjusted to the quantity we can deliver.
		<b>Everything else in your cart is available.</b>
		
		<br><br>
			
		<font class="text12bold" color="#CC3300">
			All items will remain in your cart until you click below to Continue Checkout.
		</font>
		
		<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4">
</td></tr>
<%} %>

</table>
<br> 

<!-- ===================================================== composite goes here ============================================================= -->

<table BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>">
<tr VALIGN="TOP">
<td WIDTH="<%=(W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-18)/2%>" COLSPAN="2" CLASS="text12bold">Unavailable for <%= dateFormatter.format(reservation.getStartTime()) %> <%= FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime()) %><br></td>
<td WIDTH="18"><br></td>
<td WIDTH="<%=(W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-18)/2%>" COLSPAN="2" CLASS="text12bold">Details<br></td>
</tr>
<tr><td colspan="5">&nbsp;</td></tr>

<%
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
        }    %>

   <logic:iterate collection="<%=groupingMap.get(groupKey)%>" id="key" type="java.lang.String">
	<%
	FDCartLineI cartLine = cart.getOrderLineById( Integer.parseInt(key) );
	FDAvailabilityInfo info = (FDAvailabilityInfo)invsInfoMap.get(key);
	%>
	<tr VALIGN="TOP">
	<td WIDTH="30">&nbsp;&nbsp;<b><%= quantityFormatter.format( cartLine.getQuantity() ) %></b></td>
	<td WIDTH="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-367%>">
		<b><%=cartLine.getDescription() %></b>
		<%= "".equals(cartLine.getConfigurationDesc()) ? "" : "(" + cartLine.getConfigurationDesc() + ")" %>
	</td>
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
		} else if (info instanceof FDStockAvailabilityInfo) {
			double availQty = ((FDStockAvailabilityInfo)info).getQuantity();
			%>
			<b><%= availQty==0 ? "None" : quantityFormatter.format(availQty) %> Available</b>
			<%
		} else if (info instanceof FDCompositeAvailabilityInfo) {
			%>
			The following options are unavailable:<br>
			<%
			Map componentInfos = ((FDCompositeAvailabilityInfo)info).getComponentInfo();
			boolean singleOptionIsOut= false;
			for (Iterator i = componentInfos.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry e = (Map.Entry)i.next();
				String componentKey = (String)e.getKey();
				if (componentKey != null) {
					FDAvailabilityInfo componentInfo = (FDAvailabilityInfo)e.getValue();
					FDProduct fdp = cartLine.lookupFDProduct();
					String matNo = StringUtils.right(componentKey, 9);
					FDVariationOption option = fdp.getVariationOption(matNo);
					if (option!=null) {
						%><b><%= option.getDescription() %></b><br><%
						//Check to see if this option is the only option for the variation
						FDVariation[] vars = fdp.getVariations();
						for (int vi=0; vi <vars.length;vi++){
							FDVariation aVar = vars[vi];
							if (vars[vi].getVariationOption(matNo)!=null && vars[vi].getVariationOptions().length>0) {
							    singleOptionIsOut=true;
							};
						}
						
					}
				}
			}
			if (!singleOptionIsOut) {
			%>
			<a href="/product_modify.jsp?cartLine=<%= cartLine.getRandomId() %>&skuCode=<%=cartLine.getSku().getSkuCode()%>">Click here</a> to select other options.
 			<%
			}
		} else if (info instanceof FDMuniAvailabilityInfo) {
			MunicipalityInfo muni = ((FDMuniAvailabilityInfo)info).getMunicipalityInfo();
			%>FreshDirect does not deliver alcohol outside NY.<%
		} else {
			%>Out of stock<%
		}
		%>
	</td>
	</tr>
   </logic:iterate>
</logic:iterate>
<tr><td colspan=5><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0"></td></tr>
<tr>
	<td colspan=5 >
	<br>
	</td>
</tr>
<%
day = cart.getFirstAvailableDate();
if (day != null) {
	java.text.SimpleDateFormat SF = new java.text.SimpleDateFormat("EEEEE, MMM. d");
	%>
	<tr>
		<td colspan="5">
			<b>The first day on which all items will be available is <%=SF.format(day)%></b>
		</td>
	</tr>
	<tr>
		<td colspan="5">
		<br>
		</td>
	</tr>
	<%
}
%>
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
		<td WIDTH="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-367%>">
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
<CENTER>
<img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"><br>
<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>">
	<tr valign="TOP">
		<td width="25"><a href="<% if (day != null) { %>/checkout/step_2_select<% } else { %>/index<% } %>.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" width="28" height="28" alt="" border="0"></a></td>
		<td width="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0" alt="" /></td>
		<td width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL/2-35%>">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
			<% if (day != null) { %>
			<a href="/checkout/step_2_select.jsp"><img src="/media_stat/images/template/checkout/choose_another_time.gif" width="180" height="9" alt="Choose another delivery time" border="0"></a><br>
			All items will be kept in your cart.
			<% } else { %>
			<a href="/index.jsp"><img src="/media_stat/images/template/checkout/shop_for_replacements.gif" width="140" height="9" alt="" border="0"></a><br>
			Everything else in your cart is available.
			<% } %>
		</td>
		<td width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL/2%>" align="right" valign="bottom">
			<% if(reservation!=null && !reservation.isMinOrderMet()){%>
				<a href="/checkout/view_cart.jsp" ><img name="viewcartbutton1" onmouseout="swapImage('viewcartbutton1','/media_stat/images/buttons/button_continueshop_f1.png')" onmouseover="swapImage('viewcartbutton1','/media_stat/images/buttons/button_continueshop_f2.png')" src="/media_stat/images/buttons/button_continueshop_f1.png" border="0"></a>
			<%}else{
				%><a class="imgButtonOrange" href="/checkout/step_2_adjust.jsp?successPage=<%=request.getParameter("successPage")%>">choose payment <img src="/media_stat/images/buttons/button_arrow.gif" alt="" /></a>
			<%}
			incNextButtonCount++;
			%>
		</td>
	</tr>
</table>
<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>

</CENTER>

</tmpl:put>
</tmpl:insert>
