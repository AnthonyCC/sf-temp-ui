<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.net.*"%>
<%@ page import='com.freshdirect.framework.webapp.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>

<fd:CheckLoginStatus id="yuzer" guestAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:DepotLoginController actionName='checkByDepotCode' successPage='/checkout/step_1_choose.jsp' result='result'>

<% String[] checkErrorType = {"depotCode", "depotAccessCode", "technical_difficulty"}; %>
<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %> 
</fd:ErrorHandler>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	double cartTotal = user.getShoppingCart().getTotal();
%>

<%@ include file="/includes/i_modifyorder.jspf" %>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<FORM method="post">
	<TR VALIGN="TOP">
	<TD CLASS="text12" WIDTH="500" VALIGN="bottom"> 
		<FONT CLASS="title18">FreshDirect Depot Delivery</FONT><BR>
		FreshDirect offers depot delivery to select companies. If you are eligible for depot delivery, choose your company's name below. Then enter your access code to continue with checkout. <br>
		<br>
		If you're not eligible, <a href="/checkout/step_1_choose.jsp">click here</a> to continue with home delivery checkout.<br>
		
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0"></TD>
	<TD WIDTH="145" ALIGN="RIGHT" VALIGN="top" CLASS="text10bold" >
		<input type="image" name="form_action_name" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" VSPACE="2" border="0"><BR>
		<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>: <%= currencyFormatter.format(cartTotal) %></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="top">
		<input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif" BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"></TD>
	</TR>
</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" BORDER="0"><BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
<tr><td align="right">* Required information</td></tr>
</table>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="170" height="1" alt="" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="510" height="1" alt="" border="0"></td>
</tr>
<TR VALIGN="TOP">
	<TD align="right">* Find your company name:&nbsp;</td>
	<td>
<fd:GetDepots id='depots' >
		<select STYLE="width:230px" CLASS="text12" name="depotCode">
			<option value="001">CHOOSE A DEPOT LOCATION
			<logic:iterate id="depot" collection="<%= depots %>" type="com.freshdirect.delivery.depot.DlvDepotModel">
				<option value="<%=depot.getDepotCode()%>" <%= depot.getDepotCode().equals(request.getParameter("depotCode")) ? "SELECTED" : "" %>><%=depot.getName()%>
			</logic:iterate>
		</select>
		<fd:ErrorHandler result='<%=result%>' name='depotCode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
		<img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0">
</fd:GetDepots>	
	</td>
</TR>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<TR VALIGN="TOP">
	<TD align="right">* Enter your access code:&nbsp;</TD>
	<td>
		<input class="text11" type="text" size="21" value="<%= request.getParameter("depotAccessCode")!=null?request.getParameter("depotAccessCode"):"" %>"  name="depotAccessCode" required="true" tabindex="1"> <fd:ErrorHandler result='<%=result%>' name='depotAccessCode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><br>
	</td>	
</TR>
<tr>
<td colspan=2 CLASS="text12">Don't know your code? Contact us at <%=user.getCustomerServiceContact()%>. If your company is not listed above, <a href="/checkout/step_1_choose.jsp">click here</a> to continue with home delivery checkout.
<br><br>
</td>
</tr>
</TABLE>	
	

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
		<TR VALIGN="TOP">
			<TD WIDTH="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/x_green.gif" WIDTH="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></TD>
			<TD WIDTH="350"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
			<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE"><input type="image" name="checkout_delivery_address_select"  src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="117" HEIGHT="9" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"><BR>Go to Step 1: Delivery Address<BR></TD>
			<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT><input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></TD>
		</TR>
	</TABLE>
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:DepotLoginController>
</tmpl:put>
</tmpl:insert>