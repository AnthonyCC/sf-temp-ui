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
final int W_CHECKOUT_STEP_2_UNAVAIL_TOTAL = 970;
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDCartModel cart = user.getShoppingCart();
boolean isEBTBlocked = false;
String resultPage = null;
List<FDCartLineI> ebtIneligibleOrderLines = new ArrayList<FDCartLineI>();
cart.setEbtIneligibleOrderLines(ebtIneligibleOrderLines);


// get map of cartLineId -> unav FDAvailabilityInfos
%>

<table width="690" >
<tr><td><span class="title18">Some Items are not eligible to be payed by EBT</span></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"><br>
</td></tr>
<tr><td class="text11">
		We're sorry, but some items are not available for payment through EBT card.
		If you choose to continue, the items below will be removed from your cart.
		<b>Everything else in your cart is eligible.</b>
		
		<br><br>
			
		<font class="text12bold" color="#CC3300">
			All items will remain in your cart until you click below to Continue Checkout.
		</font>
		
		<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4">
</td></tr>
</table>
<br>
<%-- DELIVERY PASS BLOCK BEGIN--%>
<table>
	<logic:iterate collection="<%=cart.getOrderLines()%>" id="cartLine" type="com.freshdirect.fdstore.customer.FDCartLineI">
	<%
	boolean isEBTBlockedItem = cartLine.getProductRef().lookupProductModel().isExcludedForEBTPayment();
	if(isEBTBlockedItem){
		ebtIneligibleOrderLines.add(cartLine);
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
 		
	</td>
	</tr>
	<% } %>
   </logic:iterate>	
</table>
<%-- DELIVERY PASS BLOCK END--%>

<img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="693" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"><br>
<table border="0" cellspacing="0" cellpadding="0" width="690">
	<tr valign="TOP">
		
		<td><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
		<td width="350" align="LEFT">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
			<a href="/order/place_order_build.jsp"><img src="/media_stat/images/navigation/ebt_choose_non_ebt.jpg" width="280" height="32" alt="" border="0"></a><br>			
		</td>
		
		<td width="350" align="RIGHT" valign="MIDDLE">
			<font class="space2pix"><br></font><a href="/checkout/checkout_ATP_adjust.jsp?successPage=<%=request.getParameter("successPage")%>&ebt=true"><img src="/media_stat/images/navigation/ebt_check_forward.jpg" WIDTH="254" HEIGHT="32" border="0" alt="CONTINUE CHECKOUT" vspace="0"></a>
		</td>
	</tr>
</table>
<br><br>


</tmpl:put>
</tmpl:insert>
