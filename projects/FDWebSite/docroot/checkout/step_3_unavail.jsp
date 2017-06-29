<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.atp.*" %>
<%@ page import="com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.delivery.restriction.EnumDlvRestrictionReason' %>
<%@ page import='com.freshdirect.common.pricing.MunicipalityInfo' %>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.commons.lang.StringUtils'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_2_UNAVAIL_TOTAL = 970;
%>

<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Checkout - Unavailability"/>
  </tmpl:put>
  <tmpl:put name='title'>FreshDirect - Checkout - Unavailability</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
java.text.DateFormat dateFormatter = new java.text.SimpleDateFormat("EEEEE, MMM d");
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDCartModel cart = user.getShoppingCart();
boolean isEBTBlocked = false;
String resultPage = null;
List<FDCartLineI> ebtIneligibleOrderLines = new ArrayList<FDCartLineI>();
cart.setEbtIneligibleOrderLines(ebtIneligibleOrderLines);

Date day = null;
if(true ){//if(isEBTBlocked ){
%>
<table width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
<tr><td><span class="title18">Some items are ineligible with EBT Card payment</span></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" alt="" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="6" border="0"><br>
</td></tr>
<tr><td class="text11">
		We're sorry, but some items are restricted for payment with EBT card.
		If you choose to continue, the items below will be removed from your cart.
		<b>Everything else in your cart is eligible for EBT card payment.</b>
		
		<br><br>
			
		<font class="text12bold" color="#CC3300">
			All items will remain in your cart until you click the button below to proceed with checkout.
		</font>
		
		<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4">
</td></tr>
</table>
<br> 

<!-- ===================================================== composite goes here ============================================================= -->

<table BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>">
<tr VALIGN="TOP">
<td WIDTH="<%=(W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-18)/2%>" COLSPAN="2" CLASS="text12bold">Ineligible items restricted by EBT payment<br></td>
<td WIDTH="18"><br></td>
<!-- <td WIDTH="<%=(W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-18)/2%>" COLSPAN="2" CLASS="text12bold">Details<br></td> -->
</tr>
<tr><td colspan="5">&nbsp;</td></tr>





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

<tr><td colspan=5><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0"></td></tr>
<tr>
	<td colspan=5 >
	<br>
	</td>
</tr>
</table>
<%} else {
	resultPage = request.getParameter("successPage");
	response.encodeRedirectURL( resultPage );%>
	<META HTTP-EQUIV="refresh" CONTENT="0;URL=<%=resultPage%>" lang="en-US">
<%} %>

<CENTER>
<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" alt="" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="6" border="0"><br>
<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>">
	<tr valign="TOP">
		<td width="350"><a href="/checkout/step_3_choose.jsp?duplicateCheck=skip"><img src="/media_stat/images/navigation/ebt_choose_non_ebt.jpg" width="280" height="32" alt="" border="0"></a></td>
		
		<td></td>
		<td width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL-410%>" align="RIGHT" valign="MIDDLE">
			<a href="/checkout/step_2_adjust.jsp?successPage=<%=request.getParameter("successPage")%>&ebt=true"><img src="/media_stat/images/navigation/ebt_check_forward.jpg" WIDTH="254" HEIGHT="32" border="0" alt="CONTINUE CHECKOUT" vspace="0"></a>			
		</td>
		
	</tr>
</table>
<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" alt="" width="<%=W_CHECKOUT_STEP_2_UNAVAIL_TOTAL%>" height="1" border="0"><br>

</CENTER>

</tmpl:put>
</tmpl:insert>
